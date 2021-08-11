package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.api.player.data.AnimationAction;
import io.github.willqi.pizzaserver.server.network.protocol.packets.PlayerAnimatePacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;

public class V419PlayerAnimatePacketHandler extends BaseProtocolPacketHandler<PlayerAnimatePacket> {

    protected final BiMap<AnimationAction, Integer> actions = HashBiMap.create(new HashMap<AnimationAction, Integer>() {
        {
            this.put(AnimationAction.NO_ACTION, 0);
            this.put(AnimationAction.SWING_ARM, 1);
            this.put(AnimationAction.WAKE_UP, 3);
            this.put(AnimationAction.CRITICAL_HIT, 4);
            this.put(AnimationAction.MAGIC_CRITICAL_HIT, 5);
            this.put(AnimationAction.ROW_RIGHT, 128);
            this.put(AnimationAction.ROW_LEFT, 129);
        }
    });

    @Override
    public PlayerAnimatePacket decode(ByteBuf buffer, BasePacketHelper helper) {
        PlayerAnimatePacket playerAnimatePacket = new PlayerAnimatePacket();
        AnimationAction action = actions.inverse().get(VarInts.readInt(buffer));
        playerAnimatePacket.setAction(action);
        playerAnimatePacket.setEntityRuntimeID(VarInts.readUnsignedLong(buffer));
        if(action == AnimationAction.ROW_LEFT || action == AnimationAction.ROW_RIGHT) playerAnimatePacket.setRowingTime(buffer.readFloatLE());
        return playerAnimatePacket;
    }

    @Override
    public void encode(PlayerAnimatePacket packet, ByteBuf buffer, BasePacketHelper helper) {
        VarInts.writeInt(buffer, actions.get(packet.getAction()));
        VarInts.writeUnsignedLong(buffer, packet.getEntityRuntimeID());
        if(packet.getAction() == AnimationAction.ROW_LEFT || packet.getAction() == AnimationAction.ROW_RIGHT) buffer.writeFloatLE(packet.getRowingTime());
    }

}
