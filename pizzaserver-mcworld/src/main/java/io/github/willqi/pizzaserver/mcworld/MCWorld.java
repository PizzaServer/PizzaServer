package io.github.willqi.pizzaserver.mcworld;

import io.github.willqi.pizzaserver.commons.server.Difficulty;
import io.github.willqi.pizzaserver.commons.server.Gamemode;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.commons.world.WorldType;
import io.github.willqi.pizzaserver.mcworld.world.info.PlayerAbilities;
import io.github.willqi.pizzaserver.mcworld.world.info.WorldInfo;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.nbt.tags.NBTInteger;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.*;
import java.util.Arrays;

/**
 * Represents a Bedrock world file
 */
public class MCWorld implements Closeable {

    private final static String DB_PATH = "db";
    private final static String LEVEL_DAT_PATH = "level.dat";

    private final File mcWorldDirectory;
    private final DB database;

    private WorldInfo worldInfo = new WorldInfo();


    /**
     * Read the contents in a exported Bedrock world file
     * @param mcWorldDirectory Folder of the unzipped contents in the .mcworld file
     */
    public MCWorld(File mcWorldDirectory) throws IOException {

        if (!( mcWorldDirectory.exists() && mcWorldDirectory.isDirectory() )) {
            throw new FileNotFoundException("The directory provided could not be found.");
        }
        this.mcWorldDirectory = mcWorldDirectory;
        this.ensureDirectoryIntegrity();

        this.database = Iq80DBFactory.factory.open(
                new File(this.mcWorldDirectory.getAbsolutePath(), DB_PATH),
                new Options());

        this.parseWorldInfoFile();

    }

    /**
     * Retrieve the content in the level.dat file
     * @return
     */
    public WorldInfo getWorldInfo() {
        return this.worldInfo.clone();
    }

    public void setWorldInfo(WorldInfo worldInfo) {
        this.worldInfo = worldInfo;
        // TODO: write info to disk
    }

    /**
W      * Ensures all files required exist in the directory.
     * @throws FileNotFoundException if a file is missing
     */
    private void ensureDirectoryIntegrity() throws FileNotFoundException {
        File levelDatFile = new File(this.mcWorldDirectory.getAbsolutePath(), LEVEL_DAT_PATH);
        if (!levelDatFile.exists()) {
            throw new FileNotFoundException("Could not find level.dat file");
        }

        File dbDirectory = new File(this.mcWorldDirectory.getAbsolutePath(), DB_PATH);
        if (!(dbDirectory.exists() && dbDirectory.isDirectory())) {
            throw new FileNotFoundException("Could not find db directory");
        }
    }

    /**
     * Parse the level.dat file and retrieve the world info
     * @throws IOException if file cannot be read
     */
    private void parseWorldInfoFile() throws IOException {
        try (NBTInputStream inputStream = new NBTInputStream(
                new LittleEndianDataInputStream(
                        new FileInputStream(new File(this.mcWorldDirectory.getAbsolutePath(), LEVEL_DAT_PATH))
                )
        )) {
            // the header is 8 bytes.
            inputStream.skip(8);    // TODO: These 8 bytes are important when writing the level.dat file
            NBTCompound compound = inputStream.readCompound();
            this.worldInfo
                    .setCommandsEnabled(compound.getByte("commandsEnabled").getValue() == 0x01)
                    .setCurrentTick(compound.getLong("currentTick").getValue())
                    .setHasBeenLoadedInCreative(compound.getByte("hasBeenLoadedInCreative").getValue() == 0x01)
                    .setHasLockedResourcePack(compound.getByte("hasLockedResourcePack").getValue() == 0x01)
                    .setHasLockedBehaviorPack(compound.getByte("hasLockedBehaviorPack").getValue() == 0x01)
                    .setExperiments(compound.getCompound("experiments"))
                    .setForceGamemode(compound.getByte("ForceGameType").getValue() == 0x01)
                    .setImmutable(compound.getByte("immutableWorld").getValue() == 0x01)
                    .setConfirmedPlatformLockedContent(compound.getByte("ConfirmedPlatformLockedContent").getValue() == 0x01)
                    .setFromWorldTemplate(compound.getByte("isFromWorldTemplate").getValue() == 0x01)
                    .setFromLockedTemplate(compound.getByte("isFromLockedTemplate").getValue() == 0x01)
                    .setIsMultiplayerGame(compound.getByte("MultiplayerGame").getValue() == 0x01)
                    .setIsSingleUseWorld(compound.getByte("isSingleUseWorld").getValue() == 0x01)
                    .setIsWorldTemplateOptionsLocked(compound.getByte("isWorldTemplateOptionLocked").getValue() == 0x01)
                    .setLanBroadcast(compound.getByte("LANBroadcast").getValue() == 0x01)
                    .setLanBroadcastIntent(compound.getByte("LANBroadcastIntent").getValue() == 0x01)
                    .setMultiplayerGameIntent(compound.getByte("MultiplayerGameIntent").getValue() == 0x01)
                    .setPlatformBroadcastIntent(compound.getInteger("PlatformBroadcastIntent").getValue())
                    .setRequiresCopiedPackRemovalCheck(compound.getByte("requiresCopiedPackRemovalCheck").getValue() == 0x01)
                    .setServerChunkTickRange(compound.getInteger("serverChunkTickRange").getValue())
                    .setSpawnOnlyV1Villagers(compound.getByte("SpawnV1Villagers").getValue() == 0x01)
                    .setStorageVersion(compound.getInteger("StorageVersion").getValue())
                    .setTexturePacksRequired(compound.getByte("texturePacksRequired").getValue() == 0x01)
                    .setUseMsaGamerTagsOnly(compound.getByte("useMsaGamertagsOnly").getValue() == 0x01)
                    .setWorldName(compound.getString("LevelName").getValue())
                    .setWorldStartCount(compound.getLong("worldStartCount").getValue())
                    .setXboxLiveBroadcastIntent(compound.getInteger("XBLBroadcastIntent").getValue())
                    .setEduOffer(compound.getInteger("eduOffer").getValue())
                    .setEduEnabled(compound.getByte("educationFeaturesEnabled").getValue() == 0x01)
                    .setBiomeOverride(compound.getString("BiomeOverride").getValue())
                    .setBonusChestEnabled(compound.getByte("bonusChestEnabled").getValue() == 0x01)
                    .setBonusChestSpawned(compound.getByte("bonusChestSpawned").getValue() == 0x01)
                    .setCenterMapsToOrigin(compound.getByte("CenterMapsToOrigin").getValue() == 0x01)
                    .setDefaultGamemode(Gamemode.values()[compound.getInteger("GameType").getValue()])
                    .setDifficulty(Difficulty.values()[compound.getInteger("Difficulty").getValue()])
                    .setFlatWorldLayers(compound.getString("FlatWorldLayers").getValue())
                    .setLightningLevel(compound.getFloat("lightningLevel").getValue())
                    .setLightningTime(compound.getInteger("lightningTime").getValue())
                    .setLimitedWorldCoordinates(new Vector3i(
                            compound.getInteger("LimitedWorldOriginX").getValue(),
                            compound.getInteger("LimitedWorldOriginY").getValue(),
                            compound.getInteger("LimitedWorldOriginZ").getValue()
                    ))
                    .setLimitedWorldDimensions(new Vector3i(
                            compound.getInteger("limitedWorldWidth").getValue(),
                            compound.getInteger("limitedWorldHeight").getValue(),
                            compound.getInteger("limitedWorldDepth").getValue()
                    ))
                    .setNetherScale(compound.getInteger("NetherScale").getValue())
                    .setRainLevel(compound.getFloat("rainLevel").getValue())
                    .setRainTime(compound.getInteger("rainTime").getValue())
                    .setSeed(compound.getInteger("RandomSeed").getValue())
                    .setSpawnCoordinates(new Vector3i(
                            compound.getInteger("SpawnX").getValue(),
                            compound.getInteger("SpawnY").getValue(),
                            compound.getInteger("SpawnZ").getValue()
                    ))
                    .setStartWithMapEnabled(compound.getByte("startWithMapEnabled").getValue() == 0x01)
                    .setTime(compound.getLong("Time").getValue())
                    .setWorldType(WorldType.values()[compound.getInteger("Generator").getValue()])
                    .setBaseGameVersion(compound.getString("baseGameVersion").getValue())
                    .setInventoryVersion(compound.getString("InventoryVersion").getValue())
                    .setLastPlayed(compound.getLong("LastPlayed").getValue())
                    .setMinimumCompatibleClientVersion(
                            Arrays.stream((NBTInteger[])compound.getList("MinimumCompatibleClientVersion").getContents())
                                .mapToInt(NBTInteger::getValue)
                                .toArray()
                    )
                    .setLastOpenedWithVersion(
                            Arrays.stream((NBTInteger[])compound.getList("lastOpenedWithVersion").getContents())
                                .mapToInt(NBTInteger::getValue)
                                .toArray()
                    )
                    .setPlatform(compound.getInteger("Platform").getValue())
                    .setProtocol(compound.getInteger("NetworkVersion").getValue())
                    .setPrid(compound.getString("prid").getValue());

            NBTCompound abilities = compound.getCompound("abilities");
            this.worldInfo.setPlayerAbilities(
                new PlayerAbilities()
                    .setCanAttackMobs(abilities.getByte("attackmobs").getValue() == 0x01)
                    .setCanAttackPlayers(abilities.getByte("attackplayers").getValue() == 0x01)
                    .setCanBuild(abilities.getByte("build").getValue() == 0x01)
                    .setCanFly(abilities.getByte("mayfly").getValue() == 0x01)
                    .setCanInstaBuild(abilities.getByte("instabuild").getValue() == 0x01)
                    .setCanMine(abilities.getByte("mine").getValue() == 0x01)
                    .setCanOpenContainers(abilities.getByte("opencontainers").getValue() == 0x01)
                    .setCanTeleport(abilities.getByte("teleport").getValue() == 0x01)
                    .setCanUseDoorsAndSwitches(abilities.getByte("doorsandswitches").getValue() == 0x01)
                    .setFlySpeed(abilities.getFloat("flySpeed").getValue())
                    .setIsFlying(abilities.getByte("flying").getValue() == 0x01)
                    .setIsInvulnerable(abilities.getByte("invulnerable").getValue() == 0x01)
                    .setIsOp(abilities.getByte("op").getValue() == 0x01)
                    .setIsLightning(abilities.getByte("lightning").getValue() == 0x01)
                    .setPermissionLevel(abilities.getInteger("permissionLevel").getValue())
                    .setPlayersPermissionLevel(abilities.getInteger("playerPermissionLevel").getValue())
                    .setWalkSpeed(abilities.getFloat("walkSpeed").getValue())
            );

            // TODO: gameRules

        }
    }


    @Override
    public void close() throws IOException {
        this.database.close();
    }

}
