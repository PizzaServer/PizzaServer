package io.github.willqi.pizzaserver.nbt.tags;

/**
 * An NBT tag that has the ability to have child tags.
 * These tag types have a inherent restriction of only being allowed to go 512 children deep.
 */
public interface NBTContainer {

    int END_ID = 0;


    int getDepth();

    void setDepth(int depth);

}
