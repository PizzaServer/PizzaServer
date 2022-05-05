package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.MusicDiscType;

public class ItemMusicDisc extends BaseItem {

    public ItemMusicDisc(MusicDiscType discType) {
        this(discType, 1);
    }

    public ItemMusicDisc(MusicDiscType discType, int count) {
        super(discType.getItemId(), count);
    }

    public MusicDiscType getDiscType() {
        return MusicDiscType.resolveByItemId(this.getItemId());
    }

    @Override
    public String getName() {
        return "Music Disc " + this.getDiscType().getName();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

}
