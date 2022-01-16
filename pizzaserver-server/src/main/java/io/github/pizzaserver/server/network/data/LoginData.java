package io.github.pizzaserver.server.network.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nukkitx.protocol.bedrock.data.skin.*;
import com.nukkitx.protocol.bedrock.util.EncryptionUtils;
import io.github.pizzaserver.api.player.data.Device;
import io.github.pizzaserver.api.player.data.Skin;
import io.netty.util.AsciiString;

import java.util.*;

public class LoginData {

    private static final Gson GSON = new Gson();

    private final String xuid;
    private final String identityPublicKey;
    private final UUID uuid;
    private final String username;
    private final String languageCode;
    private final Device device;
    private final Skin skin;
    private final boolean authenticated;


    private LoginData(
            String xuid,
            String identityPublicKey,
            UUID uuid,
            String username,
            String languageCode,
            Device device,
            Skin skin,
            boolean authenticated) {
        this.xuid = xuid;
        this.identityPublicKey = identityPublicKey;
        this.uuid = uuid;
        this.username = username;
        this.languageCode = languageCode;
        this.device = device;
        this.skin = skin;
        this.authenticated = authenticated;
    }

    public String getXUID() {
        return this.xuid;
    }

    public String getIdentityPublicKey() {
        return this.identityPublicKey;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public Device getDevice() {
        return this.device;
    }

    public Skin getSkin() {
        return this.skin;
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }


    /**
     * Parses login chain/skin data or returns an empty optional if it was unable to parse.
     * @param chainData chain data
     * @param skinData skin data
     * @return login data
     */
    @SuppressWarnings("unchecked")
    public static Optional<LoginData> extract(AsciiString chainData, AsciiString skinData) {
        String xuid;
        String identityPublicKey;
        UUID uuid;
        String username;
        String languageCode;
        Device device;
        Skin skin;
        boolean authenticated;

        try {
            // Validate chain and extract data
            JsonObject chainJSON = GSON.fromJson(chainData.toString(), JsonObject.class);

            // Check if xbox authenticated
            JSONArray chainArray = new JSONArray();
            for (JsonElement chainElement : chainJSON.getAsJsonArray("chain")) {
                chainArray.add(chainElement.getAsString());
            }
            authenticated = EncryptionUtils.verifyChain(chainArray);

            // Retrieve xuid, uuid, and username
            String chainPayload = JWSObject.parse(((String) chainArray.get(chainArray.size() - 1)))
                                           .getPayload()
                                           .toString();
            JsonObject jsonPayload = GSON.fromJson(chainPayload, JsonObject.class);
            xuid = jsonPayload.getAsJsonObject("extraData").get("XUID").getAsString();
            uuid = UUID.fromString(jsonPayload.getAsJsonObject("extraData").get("identity").getAsString());
            username = jsonPayload.getAsJsonObject("extraData").get("displayName").getAsString();
            identityPublicKey = jsonPayload.get("identityPublicKey").getAsString();

            // Extract data from skin string
            JWSObject skinJWS = JWSObject.parse(skinData.toString());
            JsonObject skinJSON = GSON.fromJson(skinJWS.getPayload().toString(), JsonObject.class);

            // Retrieve device and language code
            device = Device.getPlatformByOS(skinJSON.get("DeviceOS").getAsInt());
            languageCode = skinJSON.get("LanguageCode").getAsString();

            // Retrieve skin
            skin = extractSkin(skinData);
        } catch (Exception exception) {
            return Optional.empty();
        }

        return Optional.of(new LoginData(xuid,
                                         identityPublicKey,
                                         uuid,
                                         username,
                                         languageCode,
                                         device,
                                         skin,
                                         authenticated));
    }

    private static Skin extractSkin(AsciiString skinChain) throws Exception {
        JWSObject skinJWS = JWSObject.parse(skinChain.toString());
        JsonObject skinJSON = GSON.fromJson(skinJWS.getPayload().toString(), JsonObject.class);

        List<AnimationData> animations = new ArrayList<>();
        for (JsonElement element : skinJSON.get("AnimatedImageData").getAsJsonArray()) {
            JsonObject animation = element.getAsJsonObject();
            animations.add(new AnimationData(ImageData.of(animation.get("ImageWidth").getAsInt(),
                                                          animation.get("ImageHeight").getAsInt(),
                                                          Base64.getDecoder()
                                                                .decode(animation.get("Image").getAsString())),
                                             AnimatedTextureType.values()[animation.get("Type").getAsInt()],
                                             animation.get("Frames").getAsInt()));
        }

        List<PersonaPieceData> pieces = new ArrayList<>();
        for (JsonElement element : skinJSON.get("PersonaPieces").getAsJsonArray()) {
            JsonObject piece = element.getAsJsonObject();
            pieces.add(new PersonaPieceData(piece.get("PieceId").getAsString(),
                                            piece.get("PieceType").getAsString(),
                                            piece.get("PackId").getAsString(),
                                            piece.get("IsDefault").getAsBoolean(),
                                            piece.get("ProductId").getAsString()));
        }

        List<PersonaPieceTintData> tints = new ArrayList<>();
        for (JsonElement element : skinJSON.get("PieceTintColors").getAsJsonArray()) {
            JsonObject tint = element.getAsJsonObject();
            JsonArray colorsArray = tint.get("Colors").getAsJsonArray();

            List<String> colors = new ArrayList<>(colorsArray.size());
            for (int i = 0; i < colorsArray.size(); i++) {
                colors.add(colorsArray.get(i).getAsString());
            }
            tints.add(new PersonaPieceTintData(tint.get("PieceType").getAsString(), colors));
        }

        return new Skin.Builder().setSkinId(skinJSON.get("SkinId").getAsString())
                                 .setFullSkinId(UUID.randomUUID().toString())
                                 .setPlayFabId(skinJSON.has("PlayFabId") ? skinJSON.get("PlayFabId").getAsString() : "")
                                 .setSkinResourcePatch(new String(Base64.getDecoder()
                                                                        .decode(skinJSON.get("SkinResourcePatch")
                                                                                        .getAsString())))
                                 .setGeometryData(new String(Base64.getDecoder()
                                                                   .decode(skinJSON.get("SkinGeometryData")
                                                                                   .getAsString())))
                                 .setSkinHeight(skinJSON.get("SkinImageHeight").getAsInt())
                                 .setSkinWidth(skinJSON.get("SkinImageWidth").getAsInt())
                                 .setSkinData(Base64.getDecoder().decode(skinJSON.get("SkinData").getAsString()))
                                 .setCapeHeight(skinJSON.get("CapeImageHeight").getAsInt())
                                 .setCapeWidth(skinJSON.get("CapeImageWidth").getAsInt())
                                 .setCapeData(Base64.getDecoder().decode(skinJSON.get("CapeData").getAsString()))
                                 .setCapeId(skinJSON.get("CapeId").getAsString())
                                 .setCapeOnClassic(skinJSON.get("CapeOnClassicSkin").getAsBoolean())
                                 .setPremium(skinJSON.get("PremiumSkin").getAsBoolean())
                                 /* Parse persona specific data */.setPersona(skinJSON.get("PersonaSkin")
                                                                                      .getAsBoolean())
                                 .setAnimationData(new String(Base64.getDecoder()
                                                                    .decode(skinJSON.get("SkinAnimationData")
                                                                                    .getAsString())))
                                 .setArmSize(skinJSON.get("ArmSize").getAsString())
                                 .setSkinColour(skinJSON.get("SkinColor").getAsString())
                                 .setAnimations(animations)
                                 .setPieces(pieces)
                                 .setTints(tints)
                                 .build();
    }
}
