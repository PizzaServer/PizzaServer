package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.network.protocol.data.PlayerAction;
import io.github.willqi.pizzaserver.server.network.protocol.packets.PlayerActionPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.player.Player;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;

public class V419PlayerActionPacketHandler extends ProtocolPacketHandler<PlayerActionPacket> {


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
    public PlayerActionPacket decode(ByteBuf buffer, PacketHelper helper) {
        PlayerActionPacket packet = new PlayerActionPacket();
        packet.setEntityRuntimeID(VarInts.readUnsignedLong(buffer));
        int action = VarInts.readInt(buffer);
        packet.setActionType(actions.inverse().get(action));
        if(packet.getActionType() == null) Server.getInstance().getLogger().warn("There is an unidentified PlayerAction with an id of " + action + "!");
        packet.setVector3(new Vector3(
                VarInts.readInt(buffer),
                VarInts.readUnsignedInt(buffer),
                VarInts.readInt(buffer)
        ));
        packet.setFace(VarInts.readInt(buffer));
        return packet;
    }
}
