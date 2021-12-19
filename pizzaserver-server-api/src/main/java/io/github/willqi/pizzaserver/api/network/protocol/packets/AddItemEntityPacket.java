package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

public class AddItemEntityPacket extends BaseBedrockPacket {

    public static final int ID = 0x0f;

    private long entityUniqueId;
    private long entityRuntimeId;
    private ItemStack itemStack;

    private Vector3 position;
    private Vector3 velocity;

    private EntityMetaData metaData;
    private boolean fromFishing;


    public AddItemEntityPacket() {
        super(ID);
    }

    public long getEntityUniqueId() {
        return this.entityUniqueId;
    }

    public void setEntityUniqueId(long entityUniqueId) {
        this.entityUniqueId = entityUniqueId;
    }

    public long getEntityRuntimeId() {
        return this.entityRuntimeId;
    }

    public void setEntityRuntimeId(long entityRuntimeId) {
        this.entityRuntimeId = entityRuntimeId;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

    public EntityMetaData getMetaData() {
        return this.metaData;
    }

    public void setMetaData(EntityMetaData metaData) {
        this.metaData = metaData;
    }

    public boolean isFromFishing() {
        return this.fromFishing;
    }

    public void setFromFishing(boolean fromFishing) {
        this.fromFishing = fromFishing;
    }

}
