package io.github.willqi.pizzaserver.server.player;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.BlockLocation;
import io.github.willqi.pizzaserver.server.network.protocol.data.WorldEventType;
import io.github.willqi.pizzaserver.server.network.protocol.packets.WorldEventPacket;

import java.util.Optional;

public class BreakingData {

    private final Player player;

    private BlockLocation blockMiningLocation;
    private long startBlockBreakTick;
    private long endBlockBreakTick;

    private int breakTicks;


    public BreakingData(Player player) {
        this.player = player;
    }

    public Optional<Block> getBlock() {
        if (this.blockMiningLocation != null) {
            return Optional.of(this.blockMiningLocation.getBlock());
        } else {
            return Optional.empty();
        }
    }

    public boolean isBreakingBlock() {
        return this.getBlock().isPresent();
    }

    public void breakBlock() {
        this.player.getWorld().setBlock(this.blockMiningLocation.getBlock().getBlockType().getResultBlock(), this.blockMiningLocation);
        this.resetMiningData();
    }

    public boolean canBreakBlock() {
        // Due to issues with latency, we require at least 80% of the ticks to have ticked in order to break the block.
        // We cannot go 100% because it can cause visual annoyances for the client.
        // (this is because of the delay between the start break action being retrieved and the client thinking it broke the block)
        long ticksRequiredToBreakBlock = (long) ((this.endBlockBreakTick - this.startBlockBreakTick) * 0.8) + this.startBlockBreakTick;
        boolean enoughTicksPassed = this.player.getServer().getTick() >= ticksRequiredToBreakBlock;

        return this.isBreakingBlock() && enoughTicksPassed;
    }

    public int getBreakTicks(Block block) {
        ItemStack heldItem = this.player.getInventory().getHeldItem();

        boolean isCorrectTool = heldItem.getItemType().getToolType().isCorrectTool(block);
        float breakTime = block.getBlockType().getToughness() * (isCorrectTool ? 1.5f : 5f);

        boolean isBestTool = heldItem.getItemType().getToolType().isBestTool(block);
        if (isBestTool) {
            breakTime /= this.player.getInventory().getHeldItem().getItemType().getToolStrength(block);
        }
        // TODO: haste/mining fatigue checks
        // TODO: Underwater check
        // TODO: on ground check

        return (int) Math.ceil(breakTime * 20);
    }

    public void startBreaking(BlockLocation blockLocation) {
        if (this.isBreakingBlock()) {
            this.stopBreaking();
        }

        this.breakTicks = this.getBreakTicks(blockLocation.getBlock());
        this.blockMiningLocation = blockLocation;
        this.startBlockBreakTick = this.player.getServer().getTick();
        this.endBlockBreakTick = this.player.getServer().getTick() + this.breakTicks;

        if (this.breakTicks > 0) {
            WorldEventPacket breakStartPacket = new WorldEventPacket();
            breakStartPacket.setType(WorldEventType.EVENT_BLOCK_START_BREAK);
            breakStartPacket.setPosition(this.blockMiningLocation.toVector3());
            breakStartPacket.setData(65535 / this.breakTicks);

            for (Player player : this.blockMiningLocation.getChunk().getViewers()) {
                player.sendPacket(breakStartPacket);
            }
        }
    }

    public void stopBreaking() {
        WorldEventPacket breakStopPacket = new WorldEventPacket();
        breakStopPacket.setType(WorldEventType.EVENT_BLOCK_STOP_BREAK);
        breakStopPacket.setPosition(this.blockMiningLocation.toVector3());
        for (Player player : this.blockMiningLocation.getChunk().getViewers()) {
            player.sendPacket(breakStopPacket);
        }

        this.resetMiningData();
    }

    public void sendUpdatedBreakProgress() {
        if (this.breakTicks > 0) {
            WorldEventPacket breakUpdatePacket = new WorldEventPacket();
            breakUpdatePacket.setType(WorldEventType.EVENT_BLOCK_UPDATE_BREAK);
            breakUpdatePacket.setPosition(this.blockMiningLocation.toVector3());
            breakUpdatePacket.setData(65535 / this.breakTicks);

            for (Player player : this.player.getChunk().getViewers()) {
                player.sendPacket(breakUpdatePacket);
            }
        }
    }

    public void onChangedHeldItemWhileBreaking() {
        long ticksMinedSoFar = Math.min(this.player.getServer().getTick() - this.startBlockBreakTick, this.breakTicks);
        float blockDestructionPercentage;
        if (this.breakTicks == 0) {
            blockDestructionPercentage = 1;
        } else {
            blockDestructionPercentage = (float) ticksMinedSoFar / this.breakTicks;
        }

        Block targetBlock = this.blockMiningLocation.getBlock();

        this.breakTicks = this.getBreakTicks(targetBlock);
        this.endBlockBreakTick = this.startBlockBreakTick + (int) Math.ceil(this.breakTicks * (1 - blockDestructionPercentage));

        this.sendUpdatedBreakProgress();
    }

    private void resetMiningData() {
        this.blockMiningLocation = null;
        this.startBlockBreakTick = 0;
        this.endBlockBreakTick = 0;
        this.breakTicks = 0;
    }

}
