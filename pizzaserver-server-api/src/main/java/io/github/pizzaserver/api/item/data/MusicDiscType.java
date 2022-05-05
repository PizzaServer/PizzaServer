package io.github.pizzaserver.api.item.data;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.ItemRegistry;

public enum MusicDiscType {
    ELEVEN(ItemID.MUSIC_DISC_11, "11"),
    THIRTEEN(ItemID.MUSIC_DISC_13, "13"),
    BLOCKS(ItemID.MUSIC_DISC_BLOCKS, "Blocks"),
    CAT(ItemID.MUSIC_DISC_CAT, "Cat"),
    CHIRP(ItemID.MUSIC_DISC_CHIRP, "Chirp"),
    FAR(ItemID.MUSIC_DISC_FAR, "Far"),
    MALL(ItemID.MUSIC_DISC_MALL, "Mall"),
    MELLOHI(ItemID.MUSIC_DISC_MELLOHI, "Mellohi"),
    OTHERSIDE(ItemID.MUSIC_DISC_OTHERSIDE, "Otherside"),
    PIGSTEP(ItemID.MUSIC_DISC_PIGSTEP, "Pigstep"),
    STAL(ItemID.MUSIC_DISC_STAL, "Stal"),
    STRAD(ItemID.MUSIC_DISC_STRAD, "Strad"),
    WAIT(ItemID.MUSIC_DISC_WAIT, "Wait"),
    WARD(ItemID.MUSIC_DISC_WARD, "Ward");

    private final String itemId;
    private final String name;

    MusicDiscType(String itemId, String name) {
        this.itemId = itemId;
        this.name = name;
    }

    public String getItemId() {
        return this.itemId;
    }

    public String getName() {
        return this.name;
    }

    public Item toItem() {
        return ItemRegistry.getInstance().getItem(this.getItemId());
    }

    public static MusicDiscType resolveByItemId(String itemId) {
        for (MusicDiscType type : MusicDiscType.values()) {
            if (type.getItemId().equals(itemId)) {
                return type;
            }
        }

        return null;
    }
}
