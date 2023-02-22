package io.github.pizzaserver.server.entity;

import com.nukkitx.protocol.bedrock.data.entity.EntityData;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlag;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlags;
import com.nukkitx.protocol.bedrock.packet.SetEntityDataPacket;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.commons.data.DataAction;
import io.github.pizzaserver.commons.data.key.DataKey;
import io.github.pizzaserver.commons.data.value.ValueProxy;
import it.unimi.dsi.fastutil.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class EntityMetadataHelper {

    //TODO:
    // replace this with keys mapping to an EntityData & EntityFlag
    // which can be serialised into a packet when updated.
    // Packets should be queued on a tick bus-stop so that unnecessary calculations
    // don't occur.
    // Don't generate a map and then use putAll on the packet !!!!
    // Just put each property into the packet - this is just as efficient as before then.

    //TODO!!!
    // Map all old EntityData + Flags to DataKeys
    // Flags should store booleans but should be linked to influencers (such as ON_FIRE tied to FIRE_TICKS)

    private final Entity entity;
    private final HashMap<EntityData, DataKey<?>> trackingData;
    private final EntityFlags flagCache;

    private boolean requiresUpdate;



    public EntityMetadataHelper(Entity entity) {
        this.entity = entity;
        this.trackingData = new HashMap<>();
        this.flagCache = new EntityFlags();
        this.requiresUpdate = true;
    }

    public <T> void registerInterest(EntityData dataMapping, DataKey<T> key) {
        this.registerInterest(dataMapping, key, true);
    }

    public <T> void registerInterest(EntityData dataMapping, DataKey<T> key, boolean triggerUpdates) {
        ValueProxy<T> proxy = this.entity.getProxy(key)
                .orElseThrow(() -> new IllegalArgumentException("Provided Data Key must have an assigned value!"));

        this.trackingData.put(dataMapping, key);

        if(triggerUpdates)
            proxy.listenFor(DataAction.VALUE_SET, value -> this.requiresUpdate = true);
    }

    public void registerFlagInterest(EntityFlag flag, DataKey<Boolean> flagMapping) {
        this.registerFlagInterest(flag, flagMapping, true);
    }

    public void registerFlagInterest(EntityFlag flag, DataKey<Boolean> flagMapping, boolean triggerUpdates) {
        ValueProxy<Boolean> proxy = this.entity.getProxy(flagMapping)
                .orElseThrow(() -> new IllegalArgumentException("Provided Data Key must have an assigned value!"));

        proxy.listenFor(DataAction.VALUE_SET, value -> {
            boolean includeFlag = (boolean) value;
            this.flagCache.setFlag(flag, includeFlag);

            if(triggerUpdates)
                this.requiresUpdate = true;
        });
    }

    public void tryUpdate() {
        if (this.requiresUpdate) {
            this.requiresUpdate = false;

            this.update();
        }
    }

    public void update() {
        SetEntityDataPacket entityDataPacket = new SetEntityDataPacket();
        entityDataPacket.setRuntimeEntityId(this.entity.getId());

        this.streamProperties().forEach(data ->
                entityDataPacket.getMetadata().put(data.left(), data.right())
        );

        for (Player player : this.entity.getViewers())
            player.sendPacket(entityDataPacket);

        if (this.entity instanceof Player p)
            p.sendPacket(entityDataPacket);
    }


    public Stream<Pair<EntityData, Object>> streamProperties() {
        return Stream.concat(

                // Data stored as properties
                this.trackingData.entrySet()
                .stream()
                .filter(pair -> this.entity.has(pair.getValue()))
                .map(pair -> Pair.of(
                        pair.getKey(),
                        this.entity.expect(pair.getValue())
                )),

                Stream.of(
                        Pair.of(EntityData.FLAGS, this.flagCache),
                        Pair.of(EntityData.FLAGS_2, this.flagCache)
                )
        );
    }
}
