package io.github.willqi.pizzaserver.server.player.playerdata.provider;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.commons.utils.Check;
import io.github.willqi.pizzaserver.commons.utils.KeyLock;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.api.level.world.data.Dimension;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTOutputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.player.playerdata.PlayerData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

/**
 * Stores {@link PlayerData} in NBT .dat files
 */
public class NBTPlayerDataProvider implements PlayerDataProvider {

    private final Server server;
    private final KeyLock<UUID> keyLock = new KeyLock<>();


    public NBTPlayerDataProvider(Server server) {
        this.server = server;
    }

    @Override
    public void save(UUID uuid, PlayerData data) throws IOException {
        Check.nullParam(uuid, "uuid");
        Check.nullParam(data, "data");

        // Make sure that we aren't concurrently reading/writing to the NBT file
        this.keyLock.lock(uuid);
        try {
            File playerFile = this.resolvePlayerNBTFile(uuid);
            if (playerFile.exists() && !playerFile.delete()) {
                throw new IOException("Failed to delete existing player data file for " + uuid);
            }

            try (NBTOutputStream outputStream = new NBTOutputStream(new FileOutputStream(playerFile))) {
                outputStream.writeCompound(getPlayerNBTDataFormat(data));
                outputStream.flush();
            }
        } finally {
            this.keyLock.unlock(uuid);
        }
    }

    @Override
    public Optional<PlayerData> load(UUID uuid) throws IOException {
        Check.nullParam(uuid, "uuid");

        // Make sure that we aren't concurrently reading/writing to the NBT file
        this.keyLock.lock(uuid);
        try {
            File playerFile = this.resolvePlayerNBTFile(uuid);

            PlayerData playerData = null;
            if (playerFile.exists()) {
                try (NBTInputStream inputStream = new NBTInputStream(new FileInputStream(playerFile))) {
                    playerData = getPlayerDataFormat(inputStream.readCompound());
                }
            }
            return Optional.ofNullable(playerData);
        } finally {
            this.keyLock.unlock(uuid);
        }

    }

    private File resolvePlayerNBTFile(UUID uuid) {
        return Paths.get(this.server.getRootDirectory(), "players", uuid.toString() + ".dat").toFile();
    }

    private static NBTCompound getPlayerNBTDataFormat(PlayerData data) {
        return new NBTCompound()
                .putString("levelName", data.getLevelName())
                .putInteger("dimension", data.getDimension().ordinal())
                .putFloat("positionX", data.getPosition().getX())
                .putFloat("positionY", data.getPosition().getY())
                .putFloat("positionZ", data.getPosition().getZ())
                .putFloat("pitch", data.getPitch())
                .putFloat("yaw", data.getYaw());
    }

    private static PlayerData getPlayerDataFormat(NBTCompound data) {
        return new PlayerData.Builder()
                .setLevelName(data.getString("levelName"))
                .setDimension(Dimension.values()[data.getInteger("dimension")])
                .setPosition(new Vector3(data.getFloat("positionX"), data.getFloat("positionY"), data.getFloat("positionZ")))
                .setPitch(data.getFloat("pitch"))
                .setYaw(data.getFloat("yaw"))
                .build();
    }

}
