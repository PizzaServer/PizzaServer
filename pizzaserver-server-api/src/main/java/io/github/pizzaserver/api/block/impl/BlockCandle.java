package io.github.pizzaserver.api.block.impl;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.LitType;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.utils.BoundingBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BlockCandle extends Block {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int candleCount = 0; candleCount < 4; candleCount++) {
                this.add(NbtMap.builder()
                        .putInt("candles", candleCount)
                        .putByte("lit", (byte) 0)
                        .build());
                this.add(NbtMap.builder()
                        .putInt("candles", candleCount)
                        .putByte("lit", (byte) 1)
                        .build());
            }
        }
    };

    public BlockCandle() {
        this(1, LitType.UNLIT);
    }

    public BlockCandle(int candleCount, LitType litType) {
        this.setCandleCount(candleCount);
        this.setLit(litType == LitType.LIT);
    }

    public boolean isLit() {
        return this.getBlockState() % 2 == 1;
    }

    public void setLit(boolean lit) {
        this.setBlockState((this.getCandleCount() - 1) * 2 + (lit ? 1 : 0));
    }

    public int getCandleCount() {
        return (int) Math.floor(this.getBlockState() / 2d) + 1;
    }

    public int getMaxCandleCount() {
        return 4;
    }

    public void setCandleCount(int count) {
        int candleCount = Math.max(1, Math.min(count, this.getMaxCandleCount()));
        this.setBlockState((candleCount - 1) * 2 + (this.isLit() ? 1 : 0));
    }

    @Override
    public String getBlockId() {
        return BlockID.CANDLE;
    }

    @Override
    public String getName() {
        return "Candle";
    }

    @Override
    public float getHardness() {
        return 0.1f;
    }

    @Override
    public float getBlastResistance() {
        return 0.1f;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public int getLightEmission() {
        return 3;
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
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(Vector3f.from(0.1875f, 0, 0.1875f), Vector3f.from(0.8125f, 0.375f, 0.8125f))
                .translate(this.getLocation().toVector3f());
    }

    @Override
    public Set<Item> getDrops(Entity entity) {
        Item candleStack = this.toStack();
        candleStack.setCount(this.getCandleCount());

        return Collections.singleton(candleStack);
    }

}
