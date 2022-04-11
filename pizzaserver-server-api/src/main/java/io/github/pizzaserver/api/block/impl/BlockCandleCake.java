package io.github.pizzaserver.api.block.impl;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.LitType;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.utils.BoundingBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BlockCandleCake extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            this.add(NbtMap.builder()
                    .putByte("lit", (byte) 0)
                    .build());
            this.add(NbtMap.builder()
                    .putByte("lit", (byte) 1)
                    .build());
        }
    };


    public BlockCandleCake() {
        this(LitType.UNLIT);
    }

    public BlockCandleCake(LitType litType) {
        this.setLit(litType == LitType.LIT);
    }

    public boolean isLit() {
        return this.getBlockState() == 1;
    }

    public void setLit(boolean lit) {
        this.setBlockState(lit ? 1 : 0);
    }

    @Override
    public String getBlockId() {
        return BlockID.CANDLE_CAKE;
    }

    @Override
    public String getName() {
        return "Cake with Candle";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getHardness() {
        return 0.5f;
    }

    @Override
    public float getBlastResistance() {
        return 0.5f;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(Vector3f.from(0.0625f, 0, 0.0625f), Vector3f.from(0.9375f, 0.5f, 0.9375f))
                .translate(this.getLocation().toVector3f());
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public boolean canBeIgnited() {
        return false;
    }

    @Override
    public Set<Item> getDrops(Entity entity) {
        return Collections.singleton(new ItemBlock(new BlockCandle()));
    }

}
