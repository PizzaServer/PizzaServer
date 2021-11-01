package io.github.willqi.pizzaserver.api.level.world.blocks;

import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.utils.BlockLocation;
import io.github.willqi.pizzaserver.api.utils.BoundingBox;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

public class Block {

    private final BaseBlockType blockType;
    private int blockStateIndex = 0;

    private World world;
    private int x;
    private int y;
    private int z;


    public Block(BaseBlockType blockType) {
        this.blockType = blockType;
    }

    public BaseBlockType getBlockType() {
        return this.blockType;
    }

    /**
     * The block state data of the block
     * This is equivalent to a block state stored in the block_states.nbt file
     * @return {@link NBTCompound} of the block state data
     */
    public NBTCompound getBlockState() {
        return this.getBlockType().getBlockState(this.blockStateIndex);
    }

    public int getBlockStateIndex() {
        return this.blockStateIndex;
    }

    public void setBlockStateIndex(int index) {
        this.blockStateIndex = index;
    }

    public BlockLocation getLocation() {
        return new BlockLocation(this.world, this.x, this.y, this.z);
    }

    public void setLocation(BlockLocation location) {
        this.setLocation(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }

    public void setLocation(World world, Vector3i vector3i) {
        this.setLocation(world, vector3i.getX(), vector3i.getY(), vector3i.getZ());
    }

    public void setLocation(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
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
        return this.getWorld().getBlock(location.add(blockFace.getOffset()));
    }

    public boolean isAir() {
        return this.getBlockType().getBlockId().equals(BlockTypeID.AIR);
    }

    public boolean isSolid() {
        return this.getBlockType().isSolid();
    }

    public BoundingBox getBoundingBox() {
        BoundingBox boundingBox = this.getBlockType().getBoundingBox(this.getBlockStateIndex());
        boundingBox.setPosition(this.getLocation().toVector3());
        return boundingBox;
    }

}
