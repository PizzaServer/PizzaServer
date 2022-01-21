package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.behavior.impl.DefaultBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockSlab;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.player.Player;

import java.util.Set;

public class SlabBlockBehavior extends DefaultBlockBehavior {

    @Override
    public boolean prepareForPlacement(Entity entity, Block block, BlockFace face, Vector3f clickPosition) {
        BlockSlab slabBlock = (BlockSlab) block;
        boolean isUpperSlab = face == BlockFace.BOTTOM || (clickPosition.getY() >= 0.5f && face != BlockFace.TOP);
        slabBlock.setUpperSlab(isUpperSlab);

        Block originBlock = block.getWorld().getBlock(block.getLocation().toVector3i().add(face.opposite().getOffset()));
        boolean shouldTryCombiningSlabsBasedOnOriginSlab = originBlock.toStack().hasSameDataAs(block.toStack())
                && originBlock instanceof BlockSlab
                && !slabBlock.isDouble();

        if (shouldTryCombiningSlabsBasedOnOriginSlab) {
            boolean isOriginBlockUpperSlab = ((BlockSlab) originBlock).isUpperSlab();

            boolean shouldMakeDoubleSlabs = (face == BlockFace.TOP && !isOriginBlockUpperSlab)
                    || (face == BlockFace.BOTTOM && isOriginBlockUpperSlab);
            if (shouldMakeDoubleSlabs) {
                slabBlock.setDouble(true);
                slabBlock.setLocation(block.getWorld(), block.getLocation().toVector3i().add(face.opposite().getOffset()), block.getLayer());

                return !this.collideWithEntities(entity, slabBlock);
            }
        }


        Block replacedBlock = block.getWorld().getBlock(block.getLocation().toVector3i());
        boolean shouldTryCombiningSlabsBasedOnReplacedSlab = replacedBlock.toStack().hasSameDataAs(block.toStack())
                && replacedBlock instanceof BlockSlab
                && !slabBlock.isDouble();
        if (shouldTryCombiningSlabsBasedOnReplacedSlab) {
            boolean isReplacedBlockUpperSlab = ((BlockSlab) replacedBlock).isUpperSlab();

            boolean shouldMakeDoubleSlabs = face != BlockFace.TOP && face != BlockFace.BOTTOM
                    && ((slabBlock.isUpperSlab() && !isReplacedBlockUpperSlab)
                            || (!slabBlock.isUpperSlab() && isReplacedBlockUpperSlab));
            if (shouldMakeDoubleSlabs) {
                slabBlock.setDouble(true);
                return !this.collideWithEntities(entity, slabBlock);
            }
        }

        return super.prepareForPlacement(entity, slabBlock, face, clickPosition);
    }

}
