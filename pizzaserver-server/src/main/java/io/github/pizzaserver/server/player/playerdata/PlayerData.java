package io.github.pizzaserver.server.player.playerdata;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.level.Level;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.api.player.data.Gamemode;
import io.github.pizzaserver.commons.utils.Check;
import io.github.pizzaserver.server.inventory.InventoryUtils;

public class PlayerData {

    private final String levelName;
    private final Dimension dimension;
    private final Gamemode gamemode;
    private final Vector3f position;
    private final Item offHand;
    private final Item[] slots;
    private final Item[] armorSlots;
    private final float pitch;
    private final float yaw;


    private PlayerData(String levelName,
                       Dimension dimension,
                       Gamemode gamemode,
                       Vector3f position,
                       Item offHand,
                       Item[] slots,
                       Item[] armorSlots,
                       float pitch,
                       float yaw) {
        Check.nullParam(slots, "slots");
        Check.nullParam(armorSlots, "armorSlots");
        Check.inclusiveBounds(armorSlots.length, 4, 4, "armorSlots");
        Check.inclusiveBounds(slots.length, InventoryUtils.getSlotCount(ContainerType.INVENTORY), InventoryUtils.getSlotCount(ContainerType.INVENTORY), "slots");

        this.levelName = levelName;
        this.dimension = dimension;
        this.gamemode = gamemode;
        this.position = position;
        this.offHand = offHand;
        this.slots = slots;
        this.armorSlots = armorSlots;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    /**
     * Retrieve the name of the {@link Level} the player is in.
     * @return world name
     */
    public String getLevelName() {
        return this.levelName;
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public Gamemode getGamemode() {
        return this.gamemode;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Item getOffHand() {
        return Item.getAirIfNull(this.offHand);
    }

    public Item[] getSlots() {
        return this.slots;
    }

    public Item[] getArmorSlots() {
        return this.armorSlots;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }


    public static class Builder {

        private String levelName;
        private Dimension dimension;
        private Gamemode gamemode;
        private Vector3f position;
        private Item offHand;
        private Item[] slots;
        private Item[] armorSlots;
        private float pitch;
        private float yaw;


        public Builder setLevelName(String levelName) {
            this.levelName = levelName;
            return this;
        }

        public Builder setDimension(Dimension dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder setGamemode(Gamemode gamemode) {
            this.gamemode = gamemode;
            return this;
        }

        public Builder setPosition(Vector3f position) {
            this.position = position;
            return this;
        }

        public Builder setOffHand(Item offHand) {
            this.offHand = offHand;
            return this;
        }

        public Builder setSlots(Item[] slots) {
            this.slots = slots;
            return this;
        }

        public Builder setArmourSlots(Item[] slots) {
            this.armorSlots = slots;
            return this;
        }

        public Builder setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public Builder setYaw(float yaw) {
            this.yaw = yaw;
            return this;
        }

        public PlayerData build() {
            return new PlayerData(
                    Check.nullParam(this.levelName, "levelName"),
                    Check.nullParam(this.dimension, "dimension"),
                    Check.nullParam(this.gamemode, "gamemode"),
                    Check.nullParam(this.position, "position"),
                    this.offHand,
                    Check.nullParam(this.slots, "slots"),
                    Check.nullParam(this.armorSlots, "armorSlots"),
                    this.pitch,
                    this.yaw
            );
        }

    }

}
