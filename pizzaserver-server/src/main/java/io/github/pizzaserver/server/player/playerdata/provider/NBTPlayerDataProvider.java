package io.github.pizzaserver.server.player.playerdata.provider;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.nbt.*;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.api.player.data.Gamemode;
import io.github.pizzaserver.commons.utils.Check;
import io.github.pizzaserver.commons.utils.KeyLock;
import io.github.pizzaserver.server.item.ItemUtils;
import io.github.pizzaserver.server.player.playerdata.PlayerData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Check.notNull(uuid, "uuid");
        Check.notNull(data, "data");

        // Make sure that we aren't concurrently reading/writing to the NBT file
        this.keyLock.lock(uuid);
        try {
            File playerFile = this.resolvePlayerNBTFile(uuid);
            if (playerFile.exists() && !playerFile.delete()) {
                throw new IOException("Failed to delete existing player data file for " + uuid);
            }

            try (NBTOutputStream outputStream = NbtUtils.createWriter(new FileOutputStream(playerFile))) {
                outputStream.writeTag(getPlayerNBTDataFormat(data));
            }
        } finally {
            this.keyLock.unlock(uuid);
        }
    }

    @Override
    public Optional<PlayerData> load(UUID uuid) throws IOException {
        Check.notNull(uuid, "uuid");

        // Make sure that we aren't concurrently reading/writing to the NBT file
        this.keyLock.lock(uuid);
        try {
            File playerFile = this.resolvePlayerNBTFile(uuid);

            PlayerData playerData = null;
            if (playerFile.exists()) {
                try (NBTInputStream inputStream = NbtUtils.createReader(new FileInputStream(playerFile))) {
                    playerData = getPlayerDataFormat((NbtMap) inputStream.readTag());
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

    private static NbtMap getPlayerNBTDataFormat(PlayerData data) {
        return NbtMap.builder()
                .putString("levelName", data.getLevelName())
                .putInt("dimension", data.getDimension().ordinal())
                .putInt("gamemode", data.getGamemode().ordinal())
                .putFloat("positionX", data.getPosition().getX())
                .putFloat("positionY", data.getPosition().getY())
                .putFloat("positionZ", data.getPosition().getZ())
                .putCompound("offHand", ItemUtils.serializeForDisk(data.getOffHand()))
                .putList("slots", NbtType.COMPOUND, Arrays.stream(data.getSlots())
                        .map(ItemUtils::serializeForDisk)
                        .collect(Collectors.toList()))
                .putList("armorSlots", NbtType.COMPOUND, Arrays.stream(data.getArmorSlots())
                        .map(ItemUtils::serializeForDisk)
                        .collect(Collectors.toList()))
                .putFloat("pitch", data.getPitch())
                .putFloat("yaw", data.getYaw())
                .build();
    }

    private static PlayerData getPlayerDataFormat(NbtMap data) {
        return new PlayerData.Builder()
                .setLevelName(data.getString("levelName"))
                .setDimension(Dimension.values()[data.getInt("dimension")])
                .setGamemode(Gamemode.values()[data.getInt("gamemode")])
                .setPosition(Vector3f.from(data.getFloat("positionX"), data.getFloat("positionY"), data.getFloat("positionZ")))
                .setOffHand(ItemUtils.deserializeDiskNBTItem(data.getCompound("offHand")))
                .setSlots(data.getList("slots", NbtType.COMPOUND).stream()
                        .map(ItemUtils::deserializeDiskNBTItem)
                        .toArray(Item[]::new))
                .setArmourSlots(data.getList("armorSlots", NbtType.COMPOUND).stream()
                        .map(ItemUtils::deserializeDiskNBTItem)
                        .toArray(Item[]::new))
                .setPitch(data.getFloat("pitch"))
                .setYaw(data.getFloat("yaw"))
                .build();
    }

}
