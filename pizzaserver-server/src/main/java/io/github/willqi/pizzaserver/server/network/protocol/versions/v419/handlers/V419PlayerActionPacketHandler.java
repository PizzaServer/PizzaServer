package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.network.protocol.data.PlayerAction;
import io.github.willqi.pizzaserver.server.network.protocol.packets.PlayerActionPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

import java.util.HashMap;

public class V419PlayerActionPacketHandler extends BaseProtocolPacketHandler<PlayerActionPacket> {


    protected final BiMap<PlayerAction, Integer> actions = HashBiMap.create(new HashMap<PlayerAction, Integer>(){
        {
            this.put(PlayerAction.START_BREAK, 0);
            this.put(PlayerAction.ABORT_BREAK, 1);
            this.put(PlayerAction.STOP_BREAK, 2);
            this.put(PlayerAction.GET_UPDATED_BLOCK, 3);
            this.put(PlayerAction.DROP_ITEM, 4);
            this.put(PlayerAction.START_SLEEPING, 5);
            this.put(PlayerAction.STOP_SLEEPING, 6);
            this.put(PlayerAction.RESPAWN, 7);
            this.put(PlayerAction.JUMP, 8);
            this.put(PlayerAction.START_SPRINT, 9);
            this.put(PlayerAction.STOP_SPRINT, 10);
            this.put(PlayerAction.START_SNEAK, 11);
            this.put(PlayerAction.STOP_SNEAK, 12);
            this.put(PlayerAction.DIMENSION_CHANGE_REQUEST, 13);
            this.put(PlayerAction.DIMENSION_CHANGE_ACK, 14);
            this.put(PlayerAction.START_GLIDE, 15);
            this.put(PlayerAction.STOP_GLIDE, 16);
            this.put(PlayerAction.BUILD_DENIED, 17);
            this.put(PlayerAction.CONTINUE_BREAK, 18);
            this.put(PlayerAction.SET_ENCHANTMENT_SEED, 20);
            this.put(PlayerAction.START_SWIMMING, 21);
            this.put(PlayerAction.STOP_SWIMMING, 22);
            this.put(PlayerAction.START_SPIN_ATTACK, 23);
            this.put(PlayerAction.STOP_SPIN_ATTACK, 24);
            this.put(PlayerAction.INTERACT_BLOCK, 25);
        }
    });

    @Override
    public PlayerActionPacket decode(BasePacketBuffer buffer) {
        PlayerActionPacket packet = new PlayerActionPacket();
        packet.setEntityRuntimeID(buffer.readUnsignedVarLong());
        int action = buffer.readVarInt();
        packet.setActionType(this.actions.inverse().get(action));
        if(packet.getActionType() == null) ImplServer.getInstance().getLogger().warn("There is an unidentified PlayerAction with an id of " + action + "!");
        packet.setVector3(buffer.readVector3i());
        packet.setFace(buffer.readVarInt());
        return packet;
    }
}
