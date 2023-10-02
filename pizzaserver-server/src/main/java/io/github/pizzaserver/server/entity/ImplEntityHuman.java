package io.github.pizzaserver.server.entity;

import com.google.common.base.Charsets;
import io.github.pizzaserver.api.entity.EntityHuman;
import io.github.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.player.PlayerList;
import io.github.pizzaserver.api.player.data.Device;
import io.github.pizzaserver.api.player.data.Skin;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.item.ItemUtils;
import org.apache.commons.io.IOUtils;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket;
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerSkinPacket;

import java.io.IOException;
import java.util.UUID;

public class ImplEntityHuman extends ImplEntity implements EntityHuman {

    public static final Skin DEFAULT_STEVE;

    static {
        try {
            String defaultGeometryString = IOUtils.toString(ImplServer.class.getResourceAsStream("/skin/steve/geometry.json"), Charsets.UTF_8);
            String defaultResourcePatchString = IOUtils.toString(ImplServer.class.getResourceAsStream("/skin/steve/resource_patch.json"), Charsets.UTF_8);
            byte[] skinData = IOUtils.toByteArray(ImplServer.class.getResourceAsStream("/skin/steve/skin_data"));

            DEFAULT_STEVE = new Skin.Builder()
                    .setSkinResourcePatch(defaultResourcePatchString)
                    .setGeometryData(defaultGeometryString)
                    .setSkinData(skinData)
                    .setSkinHeight(64)
                    .setSkinWidth(64)
                    .build();
        } catch (IOException exception) {
            throw new AssertionError("Failed to parse default skin");
        }
    }


    protected Skin skin;

    protected final UUID uuid;


    public ImplEntityHuman(EntityDefinition entityType) {
        super(entityType);
        this.uuid = UUID.randomUUID();
        this.skin = DEFAULT_STEVE;
    }

    @Override
    public Device getDevice() {
        return Device.UNKNOWN;
    }

    @Override
    public String getXUID() {
        return "";
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String getUsername() {
        return this.getDisplayName().orElse("");
    }

    @Override
    public Skin getSkin() {
        return this.skin;
    }

    @Override
    public void setSkin(Skin newSkin) {
        this.skin = newSkin;

        PlayerSkinPacket playerSkinPacket = new PlayerSkinPacket();
        playerSkinPacket.setNewSkinName("");
        playerSkinPacket.setOldSkinName("");
        playerSkinPacket.setUuid(this.getUUID());
        playerSkinPacket.setSkin(newSkin.serialize());
        playerSkinPacket.setTrustedSkin(newSkin.isTrusted());

        for (Player viewer : this.getViewers()) {
            viewer.sendPacket(playerSkinPacket);
        }

        if (this instanceof Player) {
            ((Player) this).sendPacket(playerSkinPacket);
        }
    }

    @Override
    public PlayerList.Entry getPlayerListEntry() {
        return new PlayerList.Entry.Builder()
                .setUUID(this.getUUID())
                .setXUID(this.getXUID())
                .setUsername(this.getUsername())
                .setEntityRuntimeId(this.getId())
                .setDevice(this.getDevice())
                .setSkin(this.getSkin())
                .build();
    }

    @Override
    protected void endDeathAnimation() {}

    @Override
    protected void sendMovementPacket() {
        MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
        movePlayerPacket.setRuntimeEntityId(this.getId());
        movePlayerPacket.setPosition(this.getLocation().toVector3f().add(0, this.getBaseOffset(), 0));
        movePlayerPacket.setRotation(Vector3f.from(this.getPitch(), this.getYaw(), this.getHeadYaw()));
        movePlayerPacket.setMode(MovePlayerPacket.Mode.NORMAL);
        movePlayerPacket.setOnGround(this.isOnGround());

        for (Player player : this.getViewers()) {
            player.sendPacket(movePlayerPacket);
        }
    }

    @Override
    public boolean spawnTo(Player player) {
        if (this.canBeSpawnedTo(player)) {
            this.spawnedTo.add(player);

            player.getPlayerList().addEntry(this.getPlayerListEntry());

            AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
            addPlayerPacket.setUuid(this.getUUID());
            addPlayerPacket.setUsername(this.getUsername());
            addPlayerPacket.setRuntimeEntityId(this.getId());
            addPlayerPacket.setUniqueEntityId(this.getId());
            addPlayerPacket.setPosition(Vector3f.from(this.getX(), this.getY(), this.getZ()));
            addPlayerPacket.setMotion(this.getMotion());
            addPlayerPacket.setRotation(Vector3f.from(this.getPitch(), this.getYaw(), this.getHeadYaw()));
            addPlayerPacket.getMetadata().putAll(this.getMetaData().serialize());
            addPlayerPacket.setDeviceId("");
            addPlayerPacket.setPlatformChatId("");
            addPlayerPacket.setHand(ItemUtils.serializeForNetwork(this.getInventory().getHeldItem(), player.getVersion()));
            player.sendPacket(addPlayerPacket);
            this.sendEquipmentPacket(player);

            if (!(this instanceof Player)) {
                player.getPlayerList().removeEntry(this.getPlayerListEntry());
            }

            return true;
        } else {
            return false;
        }
    }

}
