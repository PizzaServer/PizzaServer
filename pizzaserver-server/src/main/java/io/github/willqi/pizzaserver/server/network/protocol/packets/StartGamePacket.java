package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.data.Difficulty;
import io.github.willqi.pizzaserver.server.network.protocol.data.ItemState;
import io.github.willqi.pizzaserver.server.network.protocol.data.PlayerMovementType;
import io.github.willqi.pizzaserver.server.player.data.Gamemode;
import io.github.willqi.pizzaserver.server.player.data.PermissionLevel;
import io.github.willqi.pizzaserver.server.utils.Vector2;
import io.github.willqi.pizzaserver.server.utils.Vector3;
import io.github.willqi.pizzaserver.server.utils.Vector3f;
import io.github.willqi.pizzaserver.server.world.data.Dimension;
import io.github.willqi.pizzaserver.server.data.ServerOrigin;
import io.github.willqi.pizzaserver.server.world.data.WorldType;
import io.github.willqi.pizzaserver.server.world.gamerules.GameRule;

public class StartGamePacket extends BedrockPacket {

    public static final int ID = 0x0b;

    // Entity specific
    private Dimension dimension;
    private long entityId;
    private Gamemode gamemode;
    private PermissionLevel permissionLevel;
    private long runtimeEntityId;
    private Vector2 rotation;
    private Vector3f spawn;

    // Server
    private boolean achievementsEnabled;
    private int chunkTickRange;
    private boolean commandsEnabled;
    private long currentTick;
    private Gamemode defaultGamemode;
    private Difficulty difficulty;
    private int enchantmentSeed;
    private boolean forceExperimentalGameplay;
    private String gameVersion;
    private boolean isTrial;
    private String serverName;
    private ServerOrigin serverOrigin;
    private boolean spawnOnlyV1Villagers;

    private int movementRewindSize;
    private PlayerMovementType movementType;
    private boolean serverAuthoritativeBlockBreaking;

    private boolean hasLockedBehaviorPacks;
    private boolean hasLockedResourcePacks;
    private boolean isFromLockedWorldTemplate;
    private boolean isFromWorldTemplate;
    private boolean isWorldTemplateOptionsLocked;
    private String premiumWorldTemplateId;
    private boolean resourcePacksRequired;

    private boolean eduFeaturesEnabled;
    private String eduId;

    // TODO: block properties for custom blocks
    private ItemState[] itemStates;

    private boolean broadcastToLan;
    private boolean hasPlatformLockedContent;
    private boolean isMultiplayer;
    private String multiplayerId;
    private int platformBroadcastMode;
    private int xboxLiveBroadcastMode;
    private boolean useMsaGamerTagsOnly;

    // World
    private boolean bonusChestEnabled;
    private boolean bonusMapEnabled;
    private GameRule[] gameRules;
    private boolean isNetherType;
    private float lightingLevel;
    private int limitedWorldHeight;
    private int limitedWorldWidth;
    private float rainLevel;
    private int seed;
    private Vector3 worldSpawn;
    private String worldId;
    private int worldTime;
    private WorldType worldType;

    private short biomeType;
    private String customBiomeName;


    public StartGamePacket() {
        super(ID);
    }

}
