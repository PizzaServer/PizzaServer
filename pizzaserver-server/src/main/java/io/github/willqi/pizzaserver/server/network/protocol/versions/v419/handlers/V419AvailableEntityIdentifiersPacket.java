package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.AvailableEntityIdentifiersPacket;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.nbt.tags.NBTList;
import io.github.willqi.pizzaserver.nbt.tags.NBTTag;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

import java.util.HashSet;
import java.util.Set;

public class V419AvailableEntityIdentifiersPacket extends BaseProtocolPacketHandler<AvailableEntityIdentifiersPacket> {

    @Override
    public void encode(AvailableEntityIdentifiersPacket packet, BasePacketBuffer buffer) {
        NBTList<NBTCompound> idList = new NBTList<>(NBTTag.COMPOUND_TAG_ID);
        Set<NBTCompound> nbtEntries = new HashSet<>();
        int rId = 0;    // what is the purpose of this?
        for (AvailableEntityIdentifiersPacket.Entry entry : packet.getEntries()) {
            nbtEntries.add(new NBTCompound()
                    .putString("bid", "")
                    .putBoolean("hasspawnegg", entry.hasSpawnEgg())
                    .putString("id", entry.getId())
                    .putInteger("rid", rId++)
                    .putBoolean("summonable", entry.isSummonable()));
        }
        idList.setContents(nbtEntries.toArray(new NBTCompound[0]));

        buffer.writeNBTCompound(new NBTCompound().putList("idlist", idList));
    }

}
