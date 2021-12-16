package io.github.pizzaserver.api.block;

import com.nukkitx.math.vector.Vector3i;
import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.api.utils.BoundingBox;

public class Block {

    private final BlockType blockType;
    private int blockStateIndex = 0;

    private World world;
    private int x;
    private int y;
    private int z;
    private int layer;


    public Block(BlockType blockType) {
        this.blockType = blockType;
    }

    public BlockType getBlockType() {
        return this.blockType;
    }

    public BlockState getBlockState() {
        return new BlockState(this.getBlockType(), this.getBlockStateIndex());
    }

    public int getBlockStateIndex() {
        return this.blockStateIndex;
    }

    public void setBlockStateIndex(int index) {
        this.blockStateIndex = index;
    }

    public int getLayer() {
        return this.layer;
    }

    public BlockLocation getLocation() {
        return new BlockLocation(this.world, this.x, this.y, this.z, this.layer);
    }

    public void setLocation(BlockLocation location) {
        this.setLocation(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getLayer());
    }

    public void setLocation(World world, Vector3i vector3i, int layer) {
        this.setLocation(world, vector3i.getX(), vector3i.getY(), vector3i.getZ(), layer);
    }

    public void setLocation(World world, int x, int y, int z, int layer) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.layer = layer;
    }

    public World getWorld() {
        return this.world;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public Block getSide(BlockFace blockFace) {
        BlockLocation location = this.getLocation();
        return this.getWorld().getBlock(location.toVector3i().add(blockFace.getOffset()));
    }

    public BlockEntity getBlockEntity() {
        BlockEntity blockEntity = this.getWorld().getBlockEntity(this.getLocation().toVector3i()).orElse(null);
        if (blockEntity == null || !blockEntity.getType().getBlockTypes().contains(this.getBlockType())) {
            return null;
        }
        return blockEntity;
    }

    public BoundingBox getBoundingBox() {
        BoundingBox boundingBox = this.getBlockState().getBoundingBox();
        boundingBox.setPosition(this.getLocation().toVector3f().add(0.5f, 0, 0.5f));
        return boundingBox;
    }

}
