package io.github.pizzaserver.api.level.world.blocks;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.api.utils.BoundingBox;

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

    public BlockState getBlockState() {
        return new BlockState(this.getBlockType(), this.getBlockStateIndex());
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
        return this.getWorld().getBlock(location.toVector3i().add(blockFace.getOffset()));
    }

    public BoundingBox getBoundingBox() {
        BoundingBox boundingBox = this.getBlockState().getBoundingBox();
        boundingBox.setPosition(this.getLocation().toVector3f().add(0.5f, 0, 0.5f));
        return boundingBox;
    }

}
