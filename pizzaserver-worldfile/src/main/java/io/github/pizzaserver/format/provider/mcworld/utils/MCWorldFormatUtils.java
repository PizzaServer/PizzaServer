package io.github.pizzaserver.format.provider.mcworld.utils;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.*;
import com.nukkitx.nbt.util.stream.LittleEndianDataOutputStream;
import io.github.pizzaserver.commons.utils.NumberUtils;
import io.github.pizzaserver.format.data.LevelData;
import io.github.pizzaserver.format.data.LevelGameRules;
import io.github.pizzaserver.format.data.PlayerAbilities;
import io.github.pizzaserver.format.dimension.chunks.BedrockBiomeMap;
import io.github.pizzaserver.format.dimension.chunks.BedrockHeightMap;
import io.github.pizzaserver.format.dimension.chunks.subchunk.*;
import io.github.pizzaserver.format.chunks.ChunkParseException;
import io.github.pizzaserver.format.dimension.chunks.subchunk.utils.Palette;
import io.github.pizzaserver.format.provider.mcworld.data.MCWorldChunkData;
import io.netty.buffer.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MCWorldFormatUtils {

    private MCWorldFormatUtils() {}

    public static BedrockSubChunk readSubChunk(ByteBuf buffer) throws IOException {
        BedrockSubChunk subChunk = new BedrockSubChunk();
        int subChunkVersion = buffer.readByte();

        int amountOfLayers;
        switch (subChunkVersion) {
            case 9:
                amountOfLayers = buffer.readByte();
                buffer.readByte();  // data driven heights
                break;
            case 8:
                amountOfLayers = buffer.readByte();
                break;
            case 1:
                amountOfLayers = 1;
                break;
            default:
                throw new ChunkParseException("Unknown sub chunk version: v" + subChunkVersion);
        }

        for (int layerI = 0; layerI < amountOfLayers; layerI++) {
            BlockLayer layer = readLayer(buffer);
            subChunk.addLayer(layer);
        }

        return subChunk;
    }

    public static void writeSubChunk(ByteBuf buffer, BedrockSubChunk subChunk) throws IOException {
        buffer.writeByte(8);
        buffer.writeByte(subChunk.getLayers().size());

        List<BlockLayer> layers = subChunk.getLayers();
        for (BlockLayer layer : layers) {
            writeLayer(buffer, layer);
        }
    }

    public static BlockLayer readLayer(ByteBuf buffer) throws IOException {
        float bitsPerBlock = buffer.readByte() >> 1;
        int blocksPerWord = (int) Math.floor(32 / bitsPerBlock);
        int wordsPerChunk = (4096 + blocksPerWord - 1) / blocksPerWord; // there are 4096 blocks in a chunk stored in x words

        // We want to read the palette first so that we can translate what blocks are immediately.
        int chunkBlocksIndex = buffer.readerIndex();
        buffer.setIndex(chunkBlocksIndex + (wordsPerChunk * 4), buffer.writerIndex());

        Palette<BlockPaletteEntry> palette = readPalette(buffer);
        int endPaletteIndex = buffer.readerIndex(); // we jump to this index after reading the blocks

        // Go back and parse the blocks.
        buffer.setIndex(chunkBlocksIndex, buffer.writerIndex());
        BlockLayer layer = new BlockLayer(palette);

        int pos = 0;
        for (int chunk = 0; chunk < wordsPerChunk; chunk++) {
            int word = buffer.readIntLE();  // This integer can store multiple minecraft blocks.
            for (int block = 0; block < blocksPerWord; block++) {
                if (pos >= 4096) {
                    break;
                }

                int paletteIndex = (word >> (pos % blocksPerWord) * (int) bitsPerBlock) & ((1 << (int) bitsPerBlock) - 1);
                layer.setBlockEntryAt(pos >> 8, pos & 15, (pos >> 4) & 15, palette.getEntry(paletteIndex));

                pos++;
            }
        }

        // Go back to the end of the palette to prepare for the next layer
        buffer.setIndex(endPaletteIndex, buffer.writerIndex());

        return layer;
    }

    public static void writeLayer(ByteBuf buffer, BlockLayer layer) throws IOException {
        layer.resize();

        float bitsPerBlock = NumberUtils.log2Ceil(layer.getPalette().size()) + 1;
        int blocksPerWord = (int) Math.floor(32 / bitsPerBlock);
        int wordsPerChunk = (4096 + blocksPerWord - 1) / blocksPerWord;

        buffer.writeByte(((int) bitsPerBlock << 1) | 1);

        int pos = 0;
        for (int chunk = 0; chunk < wordsPerChunk; chunk++) {
            int word = 0;
            for (int block = 0; block < blocksPerWord; block++) {
                if (pos >= 4096) {
                    break;
                }

                word |= layer.getPalette().getPaletteIndex(layer.getBlockEntryAt(pos >> 8, pos & 15, (pos >> 4) & 15)) << ((int) bitsPerBlock * block);
                pos++;
            }
            buffer.writeIntLE(word);
        }

        writePalette(buffer, layer.getPalette());
    }

    public static Palette<BlockPaletteEntry> readPalette(ByteBuf buffer) throws IOException {
        Palette<BlockPaletteEntry> palette = new Palette<>();

        int paletteLength = buffer.readIntLE();
        try (NBTInputStream inputStream = NbtUtils.createReaderLE(new ByteBufInputStream(buffer))) {
            for (int i = 0; i < paletteLength; i++) {
                NbtMap compound = (NbtMap) inputStream.readTag();

                String blockId = compound.getString("name");
                int version = compound.getInt("version");
                NbtMap states = compound.getCompound("states");

                palette.addEntry(new BlockPaletteEntry(blockId, version, states));
            }
        } catch (IOException exception) {
            throw new ChunkParseException("Failed to parse chunk palette.", exception);
        }

        return palette;
    }

    public static void writePalette(ByteBuf buffer, Palette<BlockPaletteEntry> palette) throws IOException {
        Set<BlockPaletteEntry> entries = palette.getEntries();
        buffer.writeIntLE(entries.size());
        try (NBTOutputStream outputStream = NbtUtils.createWriterLE(new ByteBufOutputStream(buffer))) {
            for (BlockPaletteEntry data : entries) {
                NbtMap compound = NbtMap.builder()
                        .putString("name", data.getId())
                        .putInt("version", data.getVersion())
                        .putCompound("states", data.getState())
                        .build();

                outputStream.writeTag(compound);
            }
        } catch (IOException exception) {
            throw new IOException("Failed to serialize chunk to disk", exception);
        }
    }

    public static MCWorldChunkData read2DChunkData(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);

        try {
            // Parse height map
            BedrockHeightMap heightMap = new BedrockHeightMap();
            for (int i = 0; i < 256; i++) {
                int z = i >> 4;
                int x = i & 15;
                heightMap.setHighestBlockAt(x, z, buffer.readUnsignedShortLE());
            }

            // Parse biome map
            BedrockBiomeMap biomeMap = new BedrockBiomeMap();

            byte[] biomeData = new byte[256];
            buffer.readBytes(biomeData);

            // Begin constructing 3D biomes...
            BedrockSubChunkBiomeMap[] subChunkBiomeMaps = new BedrockSubChunkBiomeMap[25];
            for (int i = 0; i < 25; i++) {
                subChunkBiomeMaps[i] = new BedrockSubChunkBiomeMap(new Palette<>());
            }

            // Assign every value in a column to its biome.
            for (int i = 0; i < 256; i++) {
                int z = i >> 4;
                int x = i & 15;

                for (int subChunkIndex = 0; subChunkIndex < 25; subChunkIndex++) {
                    for (int y = 0; y < 16; y++) {
                        subChunkBiomeMaps[subChunkIndex].setBiomeAt(x, y, z, biomeData[i]);
                    }
                }
            }

            // construct the biome map.
            for (int i = 0; i < 25; i++) {
                biomeMap.setSubChunk(i, subChunkBiomeMaps[i]);
            }

            return new MCWorldChunkData(heightMap, biomeMap);
        } finally {
            buffer.release();
        }
    }

    public static MCWorldChunkData read3DChunkData(byte[] data) throws IOException {
        ByteBuf buffer = Unpooled.wrappedBuffer(data);
        buffer.readerIndex(0);

        try {
            // Parse height map
            BedrockHeightMap heightMap = new BedrockHeightMap();
            for (int i = 0; i < 256; i++) {
                int z = i >> 4;
                int x = i & 15;
                heightMap.setHighestBlockAt(x, z, buffer.readUnsignedShortLE());
            }

            // Parse biome map
            BedrockBiomeMap biomeMap = new BedrockBiomeMap();
            BedrockSubChunkBiomeMap lastBiomeSubChunk = null;

            int subChunkIndex = 0;
            while (buffer.readableBytes() > 0) {
                float bitsPerBlock = buffer.readByte() >> 1;

                // if the bits is -1, that means that we should just copy the last biome map.
                if (bitsPerBlock == -1) {
                    if (lastBiomeSubChunk == null) {
                        throw new ChunkParseException("Cannot use last biome subchunk if none exists.");
                    }
                    biomeMap.setSubChunk(subChunkIndex++, lastBiomeSubChunk.clone());
                    continue;
                }

                // because the palette is written after the data, we keep a mental note of where the biome data is
                // so that we can construct the palette first.
                int biomeDataIndex = 0;
                int paletteLength = 1;  // for biomes, we assume by default that there is at least 1 biome present

                if (bitsPerBlock > 0) {
                    int blocksPerWord = (int) Math.floor(32 / bitsPerBlock);
                    int wordsPerChunk = (4096 + blocksPerWord - 1) / blocksPerWord;

                    biomeDataIndex = buffer.readerIndex();

                    buffer.skipBytes(wordsPerChunk * 4);
                    paletteLength = buffer.readIntLE();
                }

                // Parse biome palette
                Palette<Integer> palette = new Palette<>();
                for (int i = 0; i < paletteLength; i++) {
                    palette.addEntry(buffer.readIntLE());
                }

                int endOfPaletteIndex = buffer.readerIndex();

                // Begin constructing the biome map for this subchunk
                BedrockSubChunkBiomeMap subChunkBiomeMap = new BedrockSubChunkBiomeMap(palette);
                if (bitsPerBlock > 0) {
                    // Move our index back to the biome data before the palette
                    buffer.readerIndex(biomeDataIndex);

                    int blocksPerWord = (int) Math.floor(32 / bitsPerBlock);
                    int wordsPerChunk = (4096 + blocksPerWord - 1) / blocksPerWord;

                    int pos = 0;
                    for (int i = 0; i < wordsPerChunk; i++) {
                        int word = buffer.readIntLE();  // stores multiple biomes in 1 int
                        for (int block = 0; block < blocksPerWord; block++) {
                            if (pos >= 4096) {
                                break;
                            }

                            // Break apart the word into coordinates for each block's biome in the subchunk
                            int paletteIndex = (word >> (pos % blocksPerWord) * (int) bitsPerBlock) & ((1 << (int) bitsPerBlock) - 1);
                            subChunkBiomeMap.setBiomeAt(pos >> 8, pos & 15, (pos >> 4) & 15, palette.getEntry(paletteIndex));

                            pos++;
                        }
                    }
                } else {
                    // Every palette entry is the first index.
                    for (int pos = 0; pos < 4096; pos++) {
                        subChunkBiomeMap.setBiomeAt(pos >> 8, pos & 15, (pos >> 4) & 15, palette.getEntry(0));
                    }
                }

                buffer.readerIndex(endOfPaletteIndex);

                // Add the biome subchunk to our biome map.
                lastBiomeSubChunk = subChunkBiomeMap;
                biomeMap.setSubChunk(subChunkIndex++, lastBiomeSubChunk);
            }

            return new MCWorldChunkData(heightMap, biomeMap);
        } finally {
            buffer.release();
        }
    }

    public static byte[] heightMapToBytes(BedrockHeightMap heightMap) {
        byte[] data = new byte[512];
        ByteBuf buffer = Unpooled.wrappedBuffer(data);
        buffer.writerIndex(0);

        try {
            for (int height : heightMap.array()) {
                buffer.writeShortLE(height);
            }

            return data;
        } finally {
            buffer.release();
        }
    }

    public static byte[] biomeMapToBytes(BedrockBiomeMap biomeMap) throws IOException {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();
        try {
            BedrockSubChunkBiomeMap lastSubChunkBiomeMap = null;
            for (BedrockSubChunkBiomeMap subChunkBiomeMap : biomeMap.getSubChunks()) {
                float bitsPerBlock = NumberUtils.log2Ceil(subChunkBiomeMap.getPalette().size()) + 1;
                int blocksPerWord = 0;
                int wordsPerChunk = 0;

                boolean shouldCopyPreviousBiomeSubChunk = subChunkBiomeMap.equals(lastSubChunkBiomeMap)
                        || (lastSubChunkBiomeMap != null && subChunkBiomeMap.getPalette().size() == 0);
                if (shouldCopyPreviousBiomeSubChunk) {
                    buffer.writeByte(-1);
                    continue;
                }

                // The first biome subchunk NEEDS to have at least 1 entry.
                if (subChunkBiomeMap.getPalette().getEntries().size() == 0) {
                    throw new IOException("biome sub chunk has no biomes present");
                }

                if (bitsPerBlock > 0) {
                    blocksPerWord = (int) Math.floor(32 / bitsPerBlock);
                    wordsPerChunk = (4096 + blocksPerWord - 1) / blocksPerWord;
                }

                buffer.writeByte(((int) bitsPerBlock << 1) | 1);

                int pos = 0;
                for (int i = 0; i < wordsPerChunk; i++) {
                    int word = 0;
                    for (int block = 0; block < blocksPerWord; block++) {
                        if (pos >= 4096) {
                            break;
                        }

                        word |= subChunkBiomeMap.getPalette().getPaletteIndex(subChunkBiomeMap.getBiomeAt(pos >> 8, (pos >> 4) & 15, pos & 15)) << ((int) bitsPerBlock * block);
                        pos++;
                    }
                    buffer.writeIntLE(word);
                }

                if (bitsPerBlock > 0) {
                    buffer.writeIntLE(subChunkBiomeMap.getPalette().getEntries().size());
                }
                for (int i = 0; i < subChunkBiomeMap.getPalette().getEntries().size(); i++) {
                    buffer.writeIntLE(subChunkBiomeMap.getPalette().getEntry(i));
                }

                lastSubChunkBiomeMap = subChunkBiomeMap;
            }

            byte[] data = new byte[buffer.readableBytes()];
            buffer.readBytes(data);

            return data;
        } finally {
            buffer.release();
        }
    }

    public static LevelData readLevelData(File levelDataFile) throws IOException {
        LevelData levelData = new LevelData();
        try (InputStream levelDatStream = new FileInputStream(levelDataFile);
             NBTInputStream inputStream = NbtUtils.createReaderLE(levelDatStream)) {

            levelDatStream.skip(8); // useless header bytes

            NbtMap compound = (NbtMap) inputStream.readTag();

            levelData.setCommandsEnabled(compound.getBoolean("commandsEnabled"));
            levelData.setCurrentTick(compound.getLong("currentTick"));
            levelData.setHasBeenLoadedInCreative(compound.getBoolean("hasBeenLoadedInCreative"));
            levelData.setHasLockedResourcePack(compound.getBoolean("hasLockedResourcePack"));
            levelData.setHasLockedBehaviorPack(compound.getBoolean("hasLockedBehaviorPack"));
            levelData.setExperiments(compound.getCompound("experiments"));
            levelData.setForcedGamemode(compound.getBoolean("ForceGameType"));
            levelData.setImmutable(compound.getBoolean("immutableWorld"));
            levelData.setConfirmedPlatformLockedContent(compound.getBoolean("ConfirmedPlatformLockedContent"));
            levelData.setFromWorldTemplate(compound.getBoolean("isFromWorldTemplate"));
            levelData.setFromLockedTemplate(compound.getBoolean("isFromLockedTemplate"));
            levelData.setIsMultiplayerGame(compound.getBoolean("MultiplayerGame"));
            levelData.setIsSingleUseWorld(compound.getBoolean("isSingleUseWorld"));
            levelData.setIsWorldTemplateOptionsLocked(compound.getBoolean("isWorldTemplateOptionLocked"));
            levelData.setLanBroadcast(compound.getBoolean("LANBroadcast"));
            levelData.setLanBroadcastIntent(compound.getBoolean("LANBroadcastIntent"));
            levelData.setMultiplayerGameIntent(compound.getBoolean("MultiplayerGameIntent"));
            levelData.setPlatformBroadcastIntent(compound.getInt("PlatformBroadcastIntent"));
            levelData.setRequiresCopiedPackRemovalCheck(compound.getBoolean("requiresCopiedPackRemovalCheck"));
            levelData.setServerChunkTickRange(compound.getInt("serverChunkTickRange"));
            levelData.setSpawnOnlyV1Villagers(compound.getBoolean("SpawnV1Villagers"));
            levelData.setStorageVersion(compound.getInt("StorageVersion"));
            levelData.setTexturePacksRequired(compound.getBoolean("texturePacksRequired"));
            levelData.setUseMsaGamerTagsOnly(compound.getBoolean("useMsaGamertagsOnly"));
            levelData.setName(compound.getString("LevelName"));
            levelData.setWorldStartCount(compound.getLong("worldStartCount"));
            levelData.setXboxLiveBroadcastIntent(compound.getInt("XBLBroadcastIntent"));
            levelData.setEduOffer(compound.getInt("eduOffer"));
            levelData.setEduEnabled(compound.getBoolean("educationFeaturesEnabled"));
            levelData.setBiomeOverride(compound.getString("BiomeOverride"));
            levelData.setBonusChestEnabled(compound.getBoolean("bonusChestEnabled"));
            levelData.setBonusChestSpawned(compound.getBoolean("bonusChestSpawned"));
            levelData.setCenterMapsToOrigin(compound.getBoolean("CenterMapsToOrigin"));
            levelData.setDefaultGamemode(compound.getInt("GameType"));
            levelData.setDifficulty(compound.getInt("Difficulty"));
            levelData.setFlatWorldLayers(compound.getString("FlatWorldLayers"));
            levelData.setLightningLevel(compound.getFloat("lightningLevel"));
            levelData.setLightningTime(compound.getInt("lightningTime"));
            levelData.setLimitedWorldCoordinates(Vector3i.from(
                    compound.getInt("LimitedWorldOriginX"),
                    compound.getInt("LimitedWorldOriginY"),
                    compound.getInt("LimitedWorldOriginZ")));
            levelData.setLimitedWorldWidth(compound.getInt("limitedWorldWidth"));
            levelData.setNetherScale(compound.getInt("NetherScale"));
            levelData.setRainLevel(compound.getFloat("rainLevel"));
            levelData.setRainTime(compound.getInt("rainTime"));
            levelData.setSeed(compound.getLong("RandomSeed"));
            levelData.setWorldSpawn(Vector3i.from(
                    compound.getInt("SpawnX"),
                    compound.getInt("SpawnY"),
                    compound.getInt("SpawnZ")
            ));
            levelData.setStartWithMapEnabled(compound.getBoolean("startWithMapEnabled"));
            levelData.setTime(compound.getLong("Time"));
            levelData.setWorldType(compound.getInt("Generator"));
            levelData.setBaseGameVersion(compound.getString("baseGameVersion"));
            levelData.setInventoryVersion(compound.getString("InventoryVersion"));
            levelData.setLastPlayed(compound.getLong("LastPlayed"));
            levelData.setMinimumCompatibleClientVersion(compound.getList("MinimumCompatibleClientVersion", NbtType.INT).stream().mapToInt(Integer::intValue).toArray());
            levelData.setLastOpenedWithVersion(compound.getList("lastOpenedWithVersion", NbtType.INT).stream().mapToInt(Integer::intValue).toArray());
            levelData.setPlatform(compound.getInt("Platform"));
            levelData.setProtocol(compound.getInt("NetworkVersion"));
            levelData.setPrid(compound.getString("prid"));

            NbtMap abilities = compound.getCompound("abilities");
            levelData.setPlayerAbilities(
                    new PlayerAbilities()
                            .setCanAttackMobs(abilities.getBoolean("attackmobs"))
                            .setCanAttackPlayers(abilities.getBoolean("attackplayers"))
                            .setCanBuild(abilities.getBoolean("build"))
                            .setCanFly(abilities.getBoolean("mayfly"))
                            .setCanInstaBuild(abilities.getBoolean("instabuild"))
                            .setCanMine(abilities.getBoolean("mine"))
                            .setCanOpenContainers(abilities.getBoolean("opencontainers"))
                            .setCanTeleport(abilities.getBoolean("teleport"))
                            .setCanUseDoorsAndSwitches(abilities.getBoolean("doorsandswitches"))
                            .setFlySpeed(abilities.getFloat("flySpeed"))
                            .setIsFlying(abilities.getBoolean("flying"))
                            .setIsInvulnerable(abilities.getBoolean("invulnerable"))
                            .setIsOp(abilities.getBoolean("op"))
                            .setIsLightning(abilities.getBoolean("lightning"))
                            .setPermissionsLevel(abilities.getInt("permissionsLevel"))
                            .setPlayerPermissionsLevel(abilities.getInt("playerPermissionsLevel"))
                            .setWalkSpeed(abilities.getFloat("walkSpeed"))
            );

            LevelGameRules gameRules = new LevelGameRules();
            gameRules.setCommandBlockOutputEnabled(compound.getBoolean("commandblockoutput"));
            gameRules.setCommandBlocksEnabled(compound.getBoolean("commandblocksenabled"));
            gameRules.setDaylightCycle(compound.getBoolean("dodaylightcycle"));
            gameRules.setEntityDropsEnabled(compound.getBoolean("doentitydrops"));
            gameRules.setFireTickEnabled(compound.getBoolean("dofiretick"));
            gameRules.setImmediateRespawnEnabled(compound.getBoolean("doimmediaterespawn"));
            gameRules.setInsomniaEnabled(compound.getBoolean("doinsomnia"));
            gameRules.setMobLootEnabled(compound.getBoolean("domobloot"));
            gameRules.setMobSpawningEnabled(compound.getBoolean("domobspawning"));
            gameRules.setTileDropsEnabled(compound.getBoolean("dotiledrops"));
            gameRules.setWeatherCycleEnabled(compound.getBoolean("doweathercycle"));
            gameRules.setDrowningDamageEnabled(compound.getBoolean("drowningdamage"));
            gameRules.setFallDamageEnabled(compound.getBoolean("falldamage"));
            gameRules.setFireDamageEnabled(compound.getBoolean("firedamage"));
            gameRules.setKeepInventoryEnabled(compound.getBoolean("keepinventory"));
            gameRules.setMaxCommandChainLength(compound.getInt("maxcommandchainlength"));
            gameRules.setMobGriefingEnabled(compound.getBoolean("mobgriefing"));
            gameRules.setNaturalRegenerationEnabled(compound.getBoolean("naturalregeneration"));
            gameRules.setPVPEnabled(compound.getBoolean("pvp"));
            gameRules.setRandomTickSpeed(compound.getInt("randomtickspeed"));
            gameRules.setSendCommandFeedbackEnabled(compound.getBoolean("sendcommandfeedback"));
            gameRules.setShowCoordinatesEnabled(compound.getBoolean("showcoordinates"));
            gameRules.setShowDeathMessagesEnabled(compound.getBoolean("showdeathmessages"));
            gameRules.setShowItemTagsEnabled(compound.getBoolean("showtags"));
            gameRules.setSpawnRadius(compound.getInt("spawnradius"));
            gameRules.setTNTExplodesEnabled(compound.getBoolean("tntexplodes"));
            gameRules.setFreezeDamageEnabled(compound.getBoolean("freezedamage"));
            gameRules.setRespawnBlockExplosionsEnabled(compound.getBoolean("respawnblocksexplode"));
            gameRules.setShowBorderEffectsEnabled(compound.getBoolean("showbordereffect"));
            levelData.setGameRules(gameRules);
        }

        return levelData;
    }

    public static void writeLevelData(File levelDatFile, LevelData data) throws IOException {
        try (FileOutputStream fileStream = new FileOutputStream(levelDatFile);
                LittleEndianDataOutputStream leOutputStream = new LittleEndianDataOutputStream(fileStream);
                ByteArrayOutputStream payloadStream = new ByteArrayOutputStream();
                NBTOutputStream nbtStream = NbtUtils.createWriterLE(payloadStream)) {

            NbtMap payload = NbtMap.builder()
                    .putBoolean("commandsEnabled", data.isCommandsEnabled())
                    .putLong("currentTick", data.getCurrentTick())
                    .putBoolean("hasBeenLoadedInCreative", data.hasBeenLoadedInCreative())
                    .putBoolean("hasLockedResourcePack", data.hasLockedResourcePack())
                    .putBoolean("hasLockedBehaviorPack", data.hasLockedBehaviorPack())
                    .putCompound("experiments", data.getExperiments())
                    .putBoolean("ForceGameType", data.isForcedGamemode())
                    .putBoolean("immutableWorld", data.isImmutable())
                    .putBoolean("ConfirmedPlatformLockedContent", data.isConfirmedPlatformLockedContent())
                    .putBoolean("isFromWorldTemplate", data.isFromWorldTemplate())
                    .putBoolean("isFromLockedTemplate", data.isFromLockedTemplate())
                    .putBoolean("MultiplayerGame", data.isMultiplayerGame())
                    .putBoolean("isSingleUseWorld", data.isSingleUseWorld())
                    .putBoolean("isWorldTemplateOptionLocked", data.isWorldTemplateOptionsLocked())
                    .putBoolean("LANBroadcast", data.isLanBroadcast())
                    .putBoolean("LANBroadcastIntent", data.isLanBroadcastIntent())
                    .putBoolean("MultiplayerGameIntent", data.isMultiplayerGameIntent())
                    .putInt("PlatformBroadcastIntent", data.getPlatformBroadcastIntent())
                    .putBoolean("requiresCopiedPackRemovalCheck", data.requiresCopiedPackRemovalCheck())
                    .putInt("serverChunkTickRange", data.getServerChunkTickRange())
                    .putBoolean("SpawnV1Villagers", data.spawnOnlyV1Villagers())
                    .putInt("StorageVersion", data.getStorageVersion())
                    .putBoolean("texturePacksRequired", data.isTexturePacksRequired())
                    .putBoolean("useMsaGamertagsOnly", data.useMsaGamerTagsOnly())
                    .putString("LevelName", data.getName())
                    .putLong("worldStartCount", data.getWorldStartCount())
                    .putInt("XBLBroadcastIntent", data.getXboxLiveBroadcastIntent())
                    .putInt("eduOffer", data.getEduOffer())
                    .putBoolean("educationFeaturesEnabled", data.isEduEnabled())
                    .putString("BiomeOverride", data.getBiomeOverride())
                    .putBoolean("bonusChestEnabled", data.isBonusChestEnabled())
                    .putBoolean("bonusChestSpawned", data.isBonusChestSpawned())
                    .putBoolean("CenterMapsToOrigin", data.isCenterMapsToOrigin())
                    .putInt("GameType", data.getDefaultGamemode())
                    .putInt("Difficulty", data.getDifficulty())
                    .putString("FlatWorldLayers", data.getFlatWorldLayers())
                    .putFloat("lightningLevel", data.getLightningLevel())
                    .putInt("lightningTime", data.getLightningTime())
                    .putInt("LimitedWorldOriginX", data.getLimitedWorldCoordinates().getX())
                    .putInt("LimitedWorldOriginY", data.getLimitedWorldCoordinates().getY())
                    .putInt("LimitedWorldOriginZ", data.getLimitedWorldCoordinates().getZ())
                    .putInt("limitedWorldWidth", data.getLimitedWorldWidth())
                    .putInt("NetherScale", data.getNetherScale())
                    .putFloat("rainLevel", data.getRainLevel())
                    .putInt("rainTime", data.getRainTime())
                    .putLong("RandomSeed", data.getSeed())
                    .putInt("SpawnX", data.getWorldSpawn().getX())
                    .putInt("SpawnY", data.getWorldSpawn().getY())
                    .putInt("SpawnZ", data.getWorldSpawn().getZ())
                    .putBoolean("startWithMapEnabled", data.startWithMapEnabled())
                    .putLong("Time", data.getTime())
                    .putInt("Generator", data.getWorldType())
                    .putString("baseGameVersion", data.getBaseGameVersion())
                    .putString("InventoryVersion", data.getInventoryVersion())
                    .putLong("LastPlayed", data.getLastPlayed())
                    .putList("MinimumCompatibleClientVersion", NbtType.INT, Arrays.stream(data.getMinimumCompatibleClientVersion()).boxed().collect(Collectors.toList()))
                    .putList("lastOpenedWithVersion", NbtType.INT, Arrays.stream(data.getMinimumCompatibleClientVersion()).boxed().collect(Collectors.toList()))
                    .putInt("Platform", data.getPlatform())
                    .putInt("NetworkVersion", data.getProtocol())
                    .putString("prid", data.getPrid())
                    .putCompound("abilities", NbtMap.builder()
                            .putBoolean("attackmobs", data.getPlayerAbilities().canAttackMobs())
                            .putBoolean("attackplayers", data.getPlayerAbilities().canAttackPlayers())
                            .putBoolean("build", data.getPlayerAbilities().canBuild())
                            .putBoolean("mayfly", data.getPlayerAbilities().canFly())
                            .putBoolean("instabuild", data.getPlayerAbilities().canInstaBuild())
                            .putBoolean("mine", data.getPlayerAbilities().canMine())
                            .putBoolean("opencontainers", data.getPlayerAbilities().canOpenContainers())
                            .putBoolean("teleport", data.getPlayerAbilities().canTeleport())
                            .putBoolean("doorsandswitches", data.getPlayerAbilities().canUseDoorsAndSwitches())
                            .putFloat("flySpeed", data.getPlayerAbilities().getFlySpeed())
                            .putBoolean("flying", data.getPlayerAbilities().isFlying())
                            .putBoolean("invulnerable", data.getPlayerAbilities().isInvulnerable())
                            .putBoolean("op", data.getPlayerAbilities().isOp())
                            .putBoolean("lightning", data.getPlayerAbilities().isLightning())
                            .putInt("permissionsLevel", data.getPlayerAbilities().getPermissionsLevel())
                            .putInt("playerPermissionsLevel", data.getPlayerAbilities().getPlayerPermissionsLevel())
                            .putFloat("walkSpeed", data.getPlayerAbilities().getWalkSpeed())
                            .build())
                    .putBoolean("commandblockoutput", data.getGameRules().isCommandBlockOutputEnabled())
                    .putBoolean("commandblocksenabled", data.getGameRules().isCommandBlocksEnabled())
                    .putBoolean("dodaylightcycle", data.getGameRules().isDaylightCycleEnabled())
                    .putBoolean("doentitydrops", data.getGameRules().isEntityDropsEnabled())
                    .putBoolean("dofiretick", data.getGameRules().isFireTickEnabled())
                    .putBoolean("doimmediaterespawn", data.getGameRules().isImmediateRespawnEnabled())
                    .putBoolean("doinsomnia", data.getGameRules().isInsomniaEnabled())
                    .putBoolean("domobloot", data.getGameRules().isMobLootEnabled())
                    .putBoolean("domobspawning", data.getGameRules().isMobSpawningEnabled())
                    .putBoolean("dotiledrops", data.getGameRules().isTileDropsEnabled())
                    .putBoolean("doweathercycle", data.getGameRules().isWeatherCycleEnabled())
                    .putBoolean("drowningdamage", data.getGameRules().isDrowningDamageEnabled())
                    .putBoolean("falldamage", data.getGameRules().isFallDamageEnabled())
                    .putBoolean("firedamage", data.getGameRules().isFireDamageEnabled())
                    .putBoolean("keepinventory", data.getGameRules().isKeepInventoryEnabled())
                    .putInt("maxcommandchainlength", data.getGameRules().getMaxCommandChainLength())
                    .putBoolean("mobgriefing", data.getGameRules().isMobGriefingEnabled())
                    .putBoolean("naturalregeneration", data.getGameRules().isNaturalRegenerationEnabled())
                    .putBoolean("pvp", data.getGameRules().isPVPEnabled())
                    .putInt("randomtickspeed", data.getGameRules().getRandomTickSpeed())
                    .putBoolean("sendcommandfeedback", data.getGameRules().isSendCommandFeedbackEnabled())
                    .putBoolean("showcoordinates", data.getGameRules().isShowCoordinatesEnabled())
                    .putBoolean("showdeathmessages", data.getGameRules().isShowDeathMessagesEnabled())
                    .putBoolean("showtags", data.getGameRules().isShowItemTagsEnabled())
                    .putInt("spawnradius", data.getGameRules().getSpawnRadius())
                    .putBoolean("tntexplodes", data.getGameRules().isTNTExplodesEnabled())
                    .build();

            nbtStream.writeTag(payload);

            leOutputStream.writeInt(3); // version
            leOutputStream.writeInt(payloadStream.size());  // size of nbt
            leOutputStream.write(payloadStream.toByteArray());  // the nbt

            fileStream.flush();
        }
    }


}
