package io.github.willqi.pizzaserver.player.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nukkitx.protocol.bedrock.data.skin.*;
import io.netty.util.AsciiString;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * Represents the chain/skin data in the LoginPacket
 */
public class LoginData {

    private int protocol;

    private String xuid;
    private UUID uuid;
    private String username;
    private String identityPublicKey;

    private String gameVersion;
    private Device device;
    private String language;

    private Skin skin;

    public LoginData(int protocol, AsciiString chainData, AsciiString skinData) {
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

    private void parseChainData(AsciiString chainData) {
        Gson gson = new Gson();

        JsonObject chainDataObj = gson.fromJson(chainData.toString(), JsonObject.class);
        JsonArray chain = chainDataObj.get("chain").getAsJsonArray();

        String dataStr = decodeBase64(chain.get(2).getAsString().split("\\.")[1]);
        JsonObject parsedChainData = gson.fromJson(dataStr, JsonObject.class);

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
        this.identityPublicKey = parsedChainData.get("identityPublicKey")
                .getAsString();
    }

    private void parseSkinData(AsciiString skinData) {

        Gson gson = new Gson();
        String dataStr = decodeBase64(skinData.toString().split("\\.")[1]);
        JsonObject parsedSkinData = gson.fromJson(dataStr, JsonObject.class);

        this.device = Device.getPlatformByOS(parsedSkinData.get("DeviceOS").getAsInt());
        this.gameVersion = parsedSkinData.get("GameVersion").getAsString();
        this.language = parsedSkinData.get("LanguageCode").getAsString();

        Skin.Builder skinBuilder = new Skin.Builder();

        skinBuilder.setSkinId(parsedSkinData.get("SkinId").getAsString());
        skinBuilder.setPlayFabId(parsedSkinData.has("PlayFabId") ? parsedSkinData.get("PlayFabId").getAsString() : "");
        skinBuilder.setSkinResourcePatch(decodeBase64(parsedSkinData.get("SkinResourcePatch").getAsString()));
        skinBuilder.setGeometryData(decodeBase64(parsedSkinData.get("SkinGeometryData").getAsString()));
        skinBuilder.setSkinData(ImageData.of(parsedSkinData.get("SkinImageWidth").getAsInt(), parsedSkinData.get("SkinImageHeight").getAsInt(), Base64.getDecoder().decode(parsedSkinData.get("SkinData").getAsString())));
        skinBuilder.setCapeData(ImageData.of(parsedSkinData.get("CapeImageWidth").getAsInt(), parsedSkinData.get("CapeImageHeight").getAsInt(), Base64.getDecoder().decode(parsedSkinData.get("CapeData").getAsString())));
        skinBuilder.setCapeId(parsedSkinData.get("CapeId").getAsString());
        skinBuilder.setCapeOnClassic(parsedSkinData.get("CapeOnClassicSkin").getAsBoolean());
        skinBuilder.setPremium(parsedSkinData.get("PremiumSkin").getAsBoolean());

        skinBuilder.isPersona(parsedSkinData.get("PersonaSkin").getAsBoolean());
        skinBuilder.setAnimationData(decodeBase64(parsedSkinData.get("SkinAnimationData").getAsString()));
        skinBuilder.setArmSize(parsedSkinData.get("ArmSize").getAsString());
        skinBuilder.setSkinColor(parsedSkinData.get("SkinColor").getAsString());

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

}
