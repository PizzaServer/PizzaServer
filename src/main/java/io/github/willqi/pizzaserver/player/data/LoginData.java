package io.github.willqi.pizzaserver.player.data;

import com.google.gson.*;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory;
import com.nukkitx.protocol.bedrock.data.skin.*;
import io.github.willqi.pizzaserver.Server;
import io.netty.util.AsciiString;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * Represents the chain/skin data in the LoginPacket
 */
public class LoginData {

    private static final PublicKey MOJANG_KEY;

    static {
        // https://github.com/CloudburstMC/Server/blob/106eef8fbfbfd6f061f641ee75b0a1279ced8739/src/main/java/org/cloudburstmc/server/utils/ClientChainData.java#L52
        try {
            MOJANG_KEY = getKey("MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAE8ELkixyLcwlZryUQcu1TvPOmI2B7vX83ndnWRUaXm74wFfa5f/lwQNTfrLVHa2PmenpGI6JhIMUJaWZrjmMj90NoKNFSNBuKdm8rYiXsfaz3K36x/1U26HpG0ZxK/V1V");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError(e);
        }
    }

    private int protocol;

    private String xuid;
    private UUID uuid;
    private String username;

    private String gameVersion;
    private Device device;
    private String language;
    private boolean authenticated;

    private Skin skin;

    public LoginData(int protocol, AsciiString chainData, AsciiString skinData) throws JsonParseException {
        this.protocol = protocol;

        this.parseChainData(chainData);
        this.parseSkinData(skinData);
    }

    public int getProtocolVersion() {
        return this.protocol;
    }

    public String getXuid() {
        return this.xuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getGameVersion() {
        return this.gameVersion;
    }

    public Device getDevice() {
        return this.device;
    }

    public String getLanguage() {
        return this.language;
    }

    public Skin getSkin() {
        return this.skin;
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    private void parseChainData(AsciiString chainData) throws JsonParseException {
        Gson gson = new Gson();

        JsonObject chainDataObj = gson.fromJson(chainData.toString(), JsonObject.class);
        JsonArray chain = chainDataObj.get("chain").getAsJsonArray();
        JsonObject parsedChainData = decodeChain(chain.get(2).getAsString());

        this.xuid = parsedChainData.get("extraData")
                .getAsJsonObject()
                .get("XUID")
                .getAsString();
        this.uuid = UUID.fromString(parsedChainData.get("extraData")
                .getAsJsonObject()
                .get("identity")
                .getAsString());
        this.username = parsedChainData.get("extraData")
                .getAsJsonObject()
                .get("displayName")
                .getAsString();

        this.authenticated = isAuthenticated(chain.getAsJsonArray());

    }

    private void parseSkinData(AsciiString skinData) throws JsonParseException {

        JsonObject parsedSkinData = decodeChain(skinData.toString());

        this.device = Device.getPlatformByOS(parsedSkinData.get("DeviceOS").getAsInt());
        this.gameVersion = parsedSkinData.get("GameVersion").getAsString();
        this.language = parsedSkinData.get("LanguageCode").getAsString();

        Skin.Builder skinBuilder = new Skin.Builder();

        skinBuilder.setSkinId(parsedSkinData.get("SkinId").getAsString())
                .setPlayFabId(parsedSkinData.has("PlayFabId") ? parsedSkinData.get("PlayFabId").getAsString() : "")
                .setSkinResourcePatch(decodeBase64(parsedSkinData.get("SkinResourcePatch").getAsString()))
                .setGeometryData(decodeBase64(parsedSkinData.get("SkinGeometryData").getAsString()))
                .setSkinData(ImageData.of(parsedSkinData.get("SkinImageWidth").getAsInt(), parsedSkinData.get("SkinImageHeight").getAsInt(), Base64.getDecoder().decode(parsedSkinData.get("SkinData").getAsString())))
                .setCapeData(ImageData.of(parsedSkinData.get("CapeImageWidth").getAsInt(), parsedSkinData.get("CapeImageHeight").getAsInt(), Base64.getDecoder().decode(parsedSkinData.get("CapeData").getAsString())))
                .setCapeId(parsedSkinData.get("CapeId").getAsString())
                .setCapeOnClassic(parsedSkinData.get("CapeOnClassicSkin").getAsBoolean())
                .setPremium(parsedSkinData.get("PremiumSkin").getAsBoolean())
                /* Parse persona specific data */
                .isPersona(parsedSkinData.get("PersonaSkin").getAsBoolean())
                .setAnimationData(decodeBase64(parsedSkinData.get("SkinAnimationData").getAsString()))
                .setArmSize(parsedSkinData.get("ArmSize").getAsString())
                .setSkinColor(parsedSkinData.get("SkinColor").getAsString());

        List<AnimationData> animations = new ArrayList<>();
        for (JsonElement element : parsedSkinData.get("AnimatedImageData").getAsJsonArray()) {
            JsonObject animation = element.getAsJsonObject();
            animations.add(parseAnimationData(animation));
        }
        skinBuilder.setAnimations(animations);

        List<PersonaPieceData> pieces = new ArrayList<>();
        for (JsonElement element : parsedSkinData.get("PersonaPieces").getAsJsonArray()) {
            JsonObject piece = element.getAsJsonObject();
            pieces.add(parsePersonaPiece(piece));
        }
        skinBuilder.setPieces(pieces);

        List<PersonaPieceTintData> tints = new ArrayList<>();
        for (JsonElement element : parsedSkinData.get("PieceTintColors").getAsJsonArray()) {
            JsonObject tint = element.getAsJsonObject();
            tints.add(parsePersonaTint(tint));
        }
        skinBuilder.setTints(tints);

        this.skin = skinBuilder.build();

    }

    // https://github.com/CloudburstMC/Server/blob/master/src/main/java/org/cloudburstmc/server/utils/ClientChainData.java#L310
    private static boolean isAuthenticated(JsonArray data) {
        // My understanding of the concept of it is that the first key is used to encode the next part, which has another key that encodes the next part
        // and then at some point, it must be verified by mojang's public key.
        boolean mojangVerified = false;
        try {
            PublicKey prevKey = null;
            for (JsonElement element : data) {

                JWSObject object = JWSObject.parse(element.getAsString());
                if (object.verify(new DefaultJWSVerifierFactory().createJWSVerifier(object.getHeader(), LoginData.MOJANG_KEY))) {
                    mojangVerified = true;
                }

                if (prevKey != null && !object.verify(new DefaultJWSVerifierFactory().createJWSVerifier(object.getHeader(), prevKey))) {
                    return false;
                }

                String identityPublicKey = object.getPayload().toJSONObject().getAsString("identityPublicKey");
                prevKey = getKey(identityPublicKey);

            }
        } catch (JOSEException | ParseException | NoSuchAlgorithmException | InvalidKeySpecException exception) {
            Server.getInstance().getLogger().error("Error occurred while authenticating login data.");
            Server.getInstance().getLogger().error(exception);
            return false;
        }
        return mojangVerified;
    }

    private static AnimationData parseAnimationData(JsonObject animation) {
        ImageData imageData = ImageData.of(animation.get("ImageWidth").getAsInt(), animation.get("ImageHeight").getAsInt(), Base64.getDecoder().decode(animation.get("Image").getAsString()));
        AnimatedTextureType textureType = AnimatedTextureType.values()[animation.get("Type").getAsInt() - 1];
        float frames = animation.get("Frames").getAsFloat();

        if (animation.has("AnimationExpression")) {
            return new AnimationData(
                    imageData,
                    textureType,
                    frames,
                    AnimationExpressionType.values()[animation.get("AnimationExpression").getAsInt() - 1]
            );
        } else {
            return new AnimationData(
                    imageData,
                    textureType,
                    frames
            );
        }
    }

    private static PublicKey getKey(String base64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(base64)));
    }

    private static PersonaPieceTintData parsePersonaTint(JsonObject tint) {
        List<String> colors = new ArrayList<>();
        tint.get("Colors").getAsJsonArray().forEach(color -> colors.add(color.getAsString()));
        return new PersonaPieceTintData(tint.get("PieceType").getAsString(), colors);
    }

    private static PersonaPieceData parsePersonaPiece(JsonObject piece) {
        return new PersonaPieceData(
                piece.get("PieceId").getAsString(),
                piece.get("PieceType").getAsString(),
                piece.get("PackId").getAsString(),
                piece.get("IsDefault").getAsBoolean(),
                piece.get("ProductId").getAsString()
        );
    }

    private static String decodeBase64(String encoded) {
        return new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
    }

    private static JsonObject decodeChain(String chain) {
        Gson gson = new Gson();
        String dataStr = decodeBase64(chain.split("\\.")[1]);
        JsonObject parsedChainData = gson.fromJson(dataStr, JsonObject.class);
        return parsedChainData;
    }

    @Override
    public String toString() {
        return "LoginData(protocol=" + this.protocol + ", xuid=" + this.xuid + ", uuid=" + this.uuid + ", gameVersion=" + this.gameVersion + ", device=" + this.device.getName() + ", language=" + this.language + ", authenticated=" + this.authenticated + ")";
    }
}
