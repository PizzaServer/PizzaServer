package io.github.pizzaserver.server.block.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.behavior.impl.BaseBlockBehavior;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.impl.BlockSlab;
import io.github.pizzaserver.api.entity.Entity;

public class SlabBlockBehavior extends BaseBlockBehavior<BlockSlab> {

    @Override
    public boolean prepareForPlacement(Entity entity, BlockSlab slab, BlockFace face, Vector3f clickPosition) {
        boolean isUpperSlab = face == BlockFace.BOTTOM || (clickPosition.getY() >= 0.5f && face != BlockFace.TOP);
        slab.setUpperSlab(isUpperSlab);

        Block originBlock = slab.getWorld().getBlock(slab.getLocation().toVector3i().add(face.opposite().getOffset()));
        boolean shouldTryCombiningSlabsBasedOnOriginSlab = originBlock.toItem().hasSameDataAs(slab.toItem())
                && originBlock instanceof BlockSlab
                && !slab.isDouble();

        if (shouldTryCombiningSlabsBasedOnOriginSlab) {
            boolean isOriginBlockUpperSlab = ((BlockSlab) originBlock).isUpperSlab();

            boolean shouldMakeDoubleSlabs = (face == BlockFace.TOP && !isOriginBlockUpperSlab)
                    || (face == BlockFace.BOTTOM && isOriginBlockUpperSlab);
            if (shouldMakeDoubleSlabs) {
                slab.setDouble(true);
                slab.setLocation(slab.getWorld(), slab.getLocation().toVector3i().add(face.opposite().getOffset()), slab.getLayer());

                return !this.collideWithEntities(entity, slab);
            }
        }


        Block replacedBlock = slab.getWorld().getBlock(slab.getLocation().toVector3i());
        boolean shouldTryCombiningSlabsBasedOnReplacedSlab = replacedBlock.toItem().hasSameDataAs(slab.toItem())
                && replacedBlock instanceof BlockSlab
                && !slab.isDouble();
        if (shouldTryCombiningSlabsBasedOnReplacedSlab) {
            boolean isReplacedBlockUpperSlab = ((BlockSlab) replacedBlock).isUpperSlab();

            boolean shouldMakeDoubleSlabs = face != BlockFace.TOP && face != BlockFace.BOTTOM
                    && ((slab.isUpperSlab() && !isReplacedBlockUpperSlab)
                            || (!slab.isUpperSlab() && isReplacedBlockUpperSlab));
            if (shouldMakeDoubleSlabs) {
                slab.setDouble(true);
                return !this.collideWithEntities(entity, slab);
            }
        }

        return super.prepareForPlacement(entity, slab, face, clickPosition);
    }

}
