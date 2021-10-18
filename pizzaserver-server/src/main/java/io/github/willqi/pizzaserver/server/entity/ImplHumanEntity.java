package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.entity.HumanEntity;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlag;
import io.github.willqi.pizzaserver.api.entity.meta.flags.EntityMetaFlagCategory;
import io.github.willqi.pizzaserver.api.entity.definition.impl.HumanEntityDefinition;
import io.github.willqi.pizzaserver.api.network.protocol.data.MovementMode;
import io.github.willqi.pizzaserver.api.network.protocol.packets.AddPlayerPacket;
import io.github.willqi.pizzaserver.api.network.protocol.packets.MovePlayerPacket;
import io.github.willqi.pizzaserver.api.network.protocol.packets.PlayerSkinPacket;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.player.PlayerList;
import io.github.willqi.pizzaserver.api.player.data.Device;
import io.github.willqi.pizzaserver.api.player.skin.Skin;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.ImplServer;

import java.io.*;
import java.util.UUID;
import java.util.stream.Collectors;

public class ImplHumanEntity extends ImplLivingEntity implements HumanEntity {

    public static Skin DEFAULT_STEVE;

    static {
        try {
            InputStream defaultGeometryStream = ImplServer.class.getResourceAsStream("/skin/steve/geometry.json");
            String defaultGeometryString = new BufferedReader(new InputStreamReader(defaultGeometryStream))
                    .lines().collect(Collectors.joining("\n"));

            InputStream defaultResourcePatchStream = ImplServer.class.getResourceAsStream("/skin/steve/resource_patch.json");
            String defaultResourcePatchString = new BufferedReader(new InputStreamReader(defaultResourcePatchStream))
                    .lines().collect(Collectors.joining("\n"));

            InputStream defaultSkinDataStream = ImplServer.class.getResourceAsStream("/skin/steve/skin_data");
            ByteArrayOutputStream skinDataBufferStream = new ByteArrayOutputStream();
            int b;
            while ((b = defaultSkinDataStream.read()) != -1) {
                skinDataBufferStream.write(b);
            }
            byte[] skinData = skinDataBufferStream.toByteArray();

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


    public ImplHumanEntity(HumanEntityDefinition entityType) {
        super(entityType);
        this.uuid = UUID.randomUUID();
        this.skin = DEFAULT_STEVE;
    }

    @Override
    public float getHeight() {
        return 1.8f;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getEyeHeight() {
        return 1.62f;
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
        return this.getDisplayName();
    }

    @Override
    public Skin getSkin() {
        return this.skin;
    }

    @Override
    public void setSkin(Skin newSkin) {
        this.skin = newSkin;

        PlayerSkinPacket playerSkinPacket = new PlayerSkinPacket();
        playerSkinPacket.setPlayerUUID(this.getUUID());
        playerSkinPacket.setSkin(newSkin);
        playerSkinPacket.setTrusted(newSkin.isTrusted());

        for (Player viewer : this.getViewers()) {
            viewer.sendPacket(playerSkinPacket);
        }

        if (this instanceof Player) {
            ((Player) this).sendPacket(playerSkinPacket);
        }
    }

    @Override
    public boolean isSneaking() {
        return this.getMetaData().hasFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.IS_SNEAKING);
    }

    @Override
    public void setSneaking(boolean sneaking) {
        this.getMetaData().setFlag(EntityMetaFlagCategory.DATA_FLAG, EntityMetaFlag.IS_SNEAKING, sneaking);
        this.setMetaData(this.getMetaData());
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
    public void tick() {
        if (this.moveUpdate) {
            MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
            movePlayerPacket.setEntityRuntimeId(this.getId());
            movePlayerPacket.setPosition(new Vector3(this.getX(), this.getY() + this.getEyeHeight(), this.getZ()));
            movePlayerPacket.setPitch(this.getPitch());
            movePlayerPacket.setYaw(this.getYaw());
            movePlayerPacket.setHeadYaw(this.getHeadYaw());
            movePlayerPacket.setMode(MovementMode.NORMAL);
            movePlayerPacket.setOnGround(false);

            for (Player player : this.getViewers()) {
                player.sendPacket(movePlayerPacket);
            }
        }

        super.tick();
    }

    @Override
    public boolean spawnTo(Player player) {
        if (this.canBeSpawnedTo(player)) {
            this.spawnedTo.add(player);

            player.getPlayerList().addEntry(this.getPlayerListEntry());

            AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
            addPlayerPacket.setUUID(this.getUUID());
            addPlayerPacket.setUsername(this.getUsername());
            addPlayerPacket.setEntityRuntimeId(this.getId());
            addPlayerPacket.setEntityUniqueId(this.getId());
            addPlayerPacket.setPosition(new Vector3(this.getX(), this.getY(), this.getZ()));
            addPlayerPacket.setVelocity(new Vector3(0, 0, 0));
            addPlayerPacket.setPitch(this.getPitch());
            addPlayerPacket.setYaw(this.getYaw());
            addPlayerPacket.setHeadYaw(this.getHeadYaw());
            addPlayerPacket.setMetaData(this.getMetaData());
            addPlayerPacket.setDevice(this.getDevice());
            addPlayerPacket.setHeldItem(this.getInventory().getHeldItem());
            player.sendPacket(addPlayerPacket);

            if (!(this instanceof Player)) {
                player.getPlayerList().removeEntry(this.getPlayerListEntry());
            }

            return true;
        } else {
            return false;
        }
    }

}
