package io.github.willqi.pizzaserver.network.protocol.versions.v419.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory;
import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.network.protocol.packets.LoginPacket;
import io.github.willqi.pizzaserver.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.network.utils.ByteBufUtility;
import io.github.willqi.pizzaserver.player.data.Device;
import io.github.willqi.pizzaserver.player.data.skin.Skin;
import io.github.willqi.pizzaserver.player.data.skin.SkinAnimation;
import io.github.willqi.pizzaserver.player.data.skin.SkinPersonaPiece;
import io.github.willqi.pizzaserver.player.data.skin.SkinPersonaPieceTint;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.*;

public class V419LoginPacketHandler implements ProtocolPacketHandler<LoginPacket> {

    private static final Gson GSON = new Gson();

    private static final PublicKey MOJANG_KEY;

    static {
        // Public mojang key
        // https://github.com/CloudburstMC/Server/blob/106eef8fbfbfd6f061f641ee75b0a1279ced8739/src/main/java/org/cloudburstmc/server/utils/ClientChainData.java#L52
        try {
            MOJANG_KEY = getKey("MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAE8ELkixyLcwlZryUQcu1TvPOmI2B7vX83ndnWRUaXm74wFfa5f/lwQNTfrLVHa2PmenpGI6JhIMUJaWZrjmMj90NoKNFSNBuKdm8rYiXsfaz3K36x/1U26HpG0ZxK/V1V");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public LoginPacket decode(ByteBuf buffer) {
        LoginPacket packet = new LoginPacket();
        packet.setProtocol(buffer.readInt());

        int chainAndSkinDataLength = VarInts.readUnsignedInt(buffer);
        ByteBuf chainAndSkinData = buffer.readSlice(chainAndSkinDataLength);
        String chainDataString = ByteBufUtility.readLEString(chainAndSkinData);
        String skinDataString = ByteBufUtility.readLEString(chainAndSkinData);

        parseChainData(packet, chainDataString);
        parseSkinData(packet, skinDataString);

        return packet;
    }

    @Override
    public void encode(LoginPacket packet, ByteBuf buffer) {

    }


    /**
     * Extract the relevant JWT data from the chain/skin chains
     * @param data the string
     * @return the JSON data object
     */
    private static JsonObject extractJWTData(String data) {
        String jwtData = data.split("\\.")[1];
        String decodedData = decodeB64(jwtData);
        return GSON.fromJson(decodedData, JsonObject.class);
    }

    private static String decodeB64(String encoded) {
        return new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
    }

    private static PublicKey getKey(String base64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(base64)));
    }

    /**
     * It is valid if each part of the chain signs the next chain.
     * Additionally, at somepoint it must be signed by Mojang's public key.
     * @param chainParts The "chain" json string array property from the parsed chain data string.
     * @return if the chain is signed properly.
     */
    private static boolean isAuthenticated(JsonArray chainParts) {
        DefaultJWSVerifierFactory verifier = new DefaultJWSVerifierFactory();
        boolean mojangSigned = false;
        PublicKey prevKey = null;

        try {
            for (JsonElement element : chainParts) {
                String chain = element.getAsString();

                // Was this signed by Mojang or by the previous key?
                JWSObject chainJWS = JWSObject.parse(chain);
                if (chainJWS.verify(verifier.createJWSVerifier(chainJWS.getHeader(), MOJANG_KEY))) {
                    mojangSigned = true;
                }
                // This should also be signed by the previous key.
                if (prevKey != null && !chainJWS.verify(verifier.createJWSVerifier(chainJWS.getHeader(), prevKey))) {
                    return false;
                }
                Map<String, Object> payload = chainJWS.getPayload().toJSONObject();
                if (!payload.containsKey("identityPublicKey")) {
                    return false;   // We need to ensure that each part is signed by the next.
                }
                prevKey = getKey((String)payload.get("identityPublicKey"));
            }
        } catch (ParseException | JOSEException | NoSuchAlgorithmException | InvalidKeySpecException exception) {
            return false;
        }

        return mojangSigned;
    }


    /**
     * Parse the chain and set all relevant properties in the LoginPacket
     * @param packet the LoginPacket
     * @param chainDataString chain string
     */
    private static void parseChainData(LoginPacket packet, String chainDataString) {
        JsonObject encodedChainDataObj = GSON.fromJson(chainDataString, JsonObject.class);
        JsonArray encodedChainArray = encodedChainDataObj.get("chain").getAsJsonArray();

        JsonObject chainData = extractJWTData(encodedChainArray.get(2).getAsString());
        String xuid = chainData.get("extraData")
                .getAsJsonObject()
                .get("XUID")
                .getAsString();
        UUID uuid = UUID.fromString(chainData.get("extraData")
                .getAsJsonObject()
                .get("identity")
                .getAsString());
        String username = chainData.get("extraData")
                .getAsJsonObject()
                .get("displayName")
                .getAsString();

        packet.setAuthenticated(isAuthenticated(encodedChainArray));
        packet.setXuid(xuid);
        packet.setUuid(uuid);
        packet.setUsername(username);
    }


    private static void parseSkinData(LoginPacket packet, String skinDataString) {
        JsonObject skinData = extractJWTData(skinDataString);
        Device playerDevice = Device.getPlatformByOS(skinData.get("DeviceOS").getAsInt());
        String languageCode = skinData.get("LanguageCode").getAsString();

        Skin.Builder skinBuilder = new Skin.Builder();

        skinBuilder.setSkinId(skinData.get("SkinId").getAsString())
                .setPlayFabId(skinData.has("PlayFabId") ? skinData.get("PlayFabId").getAsString() : "")
                .setSkinResourcePatch(decodeB64(skinData.get("SkinResourcePatch").getAsString()))
                .setGeometryData(decodeB64(skinData.get("SkinGeometryData").getAsString()))
                .setSkinHeight(skinData.get("SkinImageHeight").getAsInt())
                .setSkinWidth(skinData.get("SkinImageWidth").getAsInt())
                .setSkinData(Base64.getDecoder().decode(skinData.get("SkinData").getAsString()))
                .setCapeHeight(skinData.get("CapeImageHeight").getAsInt())
                .setCapeWidth(skinData.get("CapeImageWidth").getAsInt())
                .setCapeData(Base64.getDecoder().decode(skinData.get("CapeData").getAsString()))
                .setCapeId(skinData.get("CapeId").getAsString())
                .setCapeOnClassic(skinData.get("CapeOnClassicSkin").getAsBoolean())
                .setPremium(skinData.get("PremiumSkin").getAsBoolean())
                /* Parse persona specific data */
                .isPersona(skinData.get("PersonaSkin").getAsBoolean())
                .setAnimationData(decodeB64(skinData.get("SkinAnimationData").getAsString()))
                .setArmSize(skinData.get("ArmSize").getAsString())
                .setSkinColor(skinData.get("SkinColor").getAsString());

        List<SkinAnimation> animations = new ArrayList<>();
        for (JsonElement element : skinData.get("AnimatedImageData").getAsJsonArray()) {
            JsonObject animation = element.getAsJsonObject();
            animations.add(
                    new SkinAnimation.Builder()
                            .setType(animation.get("Type").getAsInt())
                            .setFrame(animation.get("Frames").getAsInt())
                            .setSkinHeight(animation.get("ImageWidth").getAsInt())
                            .setSkinWidth(animation.get("ImageHeight").getAsInt())
                            .setSkinData(Base64.getDecoder().decode(animation.get("Image").getAsString()))
                            .build()
            );
        }
        skinBuilder.setAnimations(animations);

        List<SkinPersonaPiece> pieces = new ArrayList<>();
        for (JsonElement element : skinData.get("PersonaPieces").getAsJsonArray()) {
            JsonObject piece = element.getAsJsonObject();
            pieces.add(
                    new SkinPersonaPiece.Builder()
                        .setId(piece.get("PieceId").getAsString())
                        .setType(piece.get("PieceType").getAsString())
                        .setPackId(UUID.fromString(piece.get("PackId").getAsString()))
                        .setProductId(piece.get("ProductId").getAsString().length() > 0 ? UUID.fromString(piece.get("ProductId").getAsString()) : null)
                        .setDefault(piece.get("IsDefault").getAsBoolean())
                        .build()
            );
        }
        skinBuilder.setPieces(pieces);

        List<SkinPersonaPieceTint> tints = new ArrayList<>();
        for (JsonElement element : skinData.get("PieceTintColors").getAsJsonArray()) {
            JsonObject tint = element.getAsJsonObject();
            JsonArray colorsArray = tint.get("Colors").getAsJsonArray();

            String[] colors = new String[colorsArray.size()];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = colorsArray.get(i).getAsString();
            }
            tints.add(new SkinPersonaPieceTint(tint.get("PieceType").getAsString(), colors));
        }
        skinBuilder.setTints(tints);

        Skin skin = skinBuilder.build();

        packet.setDevice(playerDevice);
        packet.setLanguageCode(languageCode);
        packet.setSkin(skin);



    }

}
