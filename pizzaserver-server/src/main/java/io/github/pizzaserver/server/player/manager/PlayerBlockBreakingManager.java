package io.github.pizzaserver.server.player.manager;

import com.nukkitx.protocol.bedrock.data.LevelEventType;
import com.nukkitx.protocol.bedrock.packet.LevelEventPacket;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.event.type.block.BlockStopBreakEvent;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.ToolItem;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

import java.util.Optional;

public class PlayerBlockBreakingManager {

    private final Player player;

    private BlockLocation blockMiningLocation;
    private BlockFace blockFaceMiningAgainst;
    private long startBlockBreakTick;
    private long endBlockBreakTick;

    private int breakTicks;


    public PlayerBlockBreakingManager(Player player) {
        this.player = player;
    }

    public void tick() {
        if (this.getBlock().isPresent()) {
            Block block = this.getBlock().get();

            for (Player viewer : block.getLocation().getChunk().getViewers()) {
                LevelEventPacket breakParticlePacket = new LevelEventPacket();
                breakParticlePacket.setType(LevelEventType.PARTICLE_CRACK_BLOCK);
                breakParticlePacket.setPosition(this.blockMiningLocation.toVector3f());
                breakParticlePacket.setData(viewer.getVersion().getBlockRuntimeId(block.getBlockId(), block.getNBTState()) | (this.blockFaceMiningAgainst.ordinal() << 24));
                viewer.sendPacket(breakParticlePacket);
            }

            // Make sure that the block we're breaking is within reach!
            boolean stopBreakingBlock = !(this.player.canReach(block)
                    && this.player.isAlive()
                    && this.player.getAdventureSettings().canMine());
            if (stopBreakingBlock) {
                BlockStopBreakEvent blockStopBreakEvent = new BlockStopBreakEvent(this.player, block);
                this.player.getServer().getEventManager().call(blockStopBreakEvent);
                this.stopBreaking();
            }
        }
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
        Block block = this.blockMiningLocation.getBlock();

        block.getWorld().getBlockEntity(block.getLocation().toVector3i())
                .ifPresent(blockEntity -> ((BaseBlockEntity<? extends Block>) blockEntity).onBreak(this.player));
        this.player.getWorld().setAndUpdateBlock(BlockID.AIR, block.getLocation().toVector3i());

        block.getBehavior().onBreak(this.player, block);
        this.player.getInventory().getHeldItem().getBehavior().onBreak(this.player, this.player.getInventory().getHeldItem(), block);

        this.resetMiningData();

        if (!block.getBlockId().equals(BlockID.FIRE)) {
            for (Player viewer : block.getLocation().getChunk().getViewers()) {
                int blockRuntimeId = viewer.getVersion().getBlockRuntimeId(block.getBlockId(), block.getNBTState());

                LevelEventPacket breakParticlePacket = new LevelEventPacket();
                breakParticlePacket.setType(LevelEventType.PARTICLE_DESTROY_BLOCK);
                breakParticlePacket.setPosition(block.getLocation().toVector3f());
                breakParticlePacket.setData(blockRuntimeId);
                viewer.sendPacket(breakParticlePacket);
            }
        }
    }

    public boolean canBreakBlock() {
        // Due to issues with latency, we require at least 80% of the ticks to have ticked in order to break the block.
        // We cannot go 100% because it can cause visual annoyances for the client.
        // (this is because of the delay between the start break action being retrieved and the client thinking it broke the block)
        long ticksRequiredToBreakBlock = (long) ((this.endBlockBreakTick - this.startBlockBreakTick) * 0.8) + this.startBlockBreakTick;
        boolean enoughTicksPassed = this.player.getServer().getTick() >= ticksRequiredToBreakBlock;

        return this.isBreakingBlock() && enoughTicksPassed;
    }

    /**
     * Retrieve the amount of ticks it would take to break the current block the player is mining.
     * @return the amount of ticks or -1 if the player is not breaking a block
     */
    public int getBreakTicks() {
        if (this.getBlock().filter(block -> block.getHardness() != -1).isPresent()) {
            Block block = this.getBlock().get();

            Item heldItem = this.player.getInventory().getHeldItem();
            boolean isCorrectTool = block.getToolTypeRequired() == ToolType.NONE;
            boolean isCorrectTier = block.getToolTierRequired() == ToolTier.NONE;
            if (heldItem instanceof ToolItem toolItemComponent) {
                isCorrectTool = block.getToolTypeRequired() == toolItemComponent.getToolType();
                isCorrectTier = toolItemComponent.getToolTier().getStrength() >= block.getToolTierRequired().getStrength();
            }

            float breakTime = block.getHardness() * ((isCorrectTool && isCorrectTier) || block.canBeMinedWithHand() ? 1.5f : 5f);
            if (isCorrectTool && heldItem instanceof ToolItem toolItemComponent) {
                breakTime /= toolItemComponent.getToolTier().getStrength();
            }
            // TODO: haste/mining fatigue checks
            // TODO: Underwater check
            // TODO: on ground check

            return Math.round(breakTime * 20);
        } else {
            return -1;
        }
    }

    public void startBreaking(BlockLocation blockLocation, BlockFace blockFaceMiningAgainst) {
        if (this.isBreakingBlock()) {
            this.stopBreaking();
        }

        this.blockMiningLocation = blockLocation;
        this.blockFaceMiningAgainst = blockFaceMiningAgainst;
        this.breakTicks = this.getBreakTicks();
        this.startBlockBreakTick = this.player.getServer().getTick();
        this.endBlockBreakTick = this.startBlockBreakTick + this.breakTicks;

        if (this.breakTicks > 0) {
            LevelEventPacket breakStartPacket = new LevelEventPacket();
            breakStartPacket.setType(LevelEventType.BLOCK_START_BREAK);
            breakStartPacket.setPosition(this.blockMiningLocation.toVector3f());
            breakStartPacket.setData(65535 / this.breakTicks);

            for (Player player : this.blockMiningLocation.getChunk().getViewers()) {
                player.sendPacket(breakStartPacket);
            }
        }
    }

    public void stopBreaking() {
        LevelEventPacket breakStopPacket = new LevelEventPacket();
        breakStopPacket.setType(LevelEventType.BLOCK_STOP_BREAK);
        breakStopPacket.setPosition(this.blockMiningLocation.toVector3f());
        for (Player player : this.blockMiningLocation.getChunk().getViewers()) {
            player.sendPacket(breakStopPacket);
        }

        this.resetMiningData();
    }

    /**
     * Send the new block break progress to all viewers of this block.
     * Additionally,
     */
    public void sendUpdatedBreakProgress() {
        if (this.breakTicks > 0) {
            Block block = this.blockMiningLocation.getBlock();

            LevelEventPacket breakUpdatePacket = new LevelEventPacket();
            breakUpdatePacket.setType(LevelEventType.BLOCK_UPDATE_BREAK);
            breakUpdatePacket.setPosition(block.getLocation().toVector3f());
            breakUpdatePacket.setData(65535 / this.breakTicks);

            for (Player player : this.blockMiningLocation.getChunk().getViewers()) {
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

        this.breakTicks = this.getBreakTicks();
        this.endBlockBreakTick = this.startBlockBreakTick + (int) Math.ceil(this.breakTicks * (1 - blockDestructionPercentage));

        this.sendUpdatedBreakProgress();
    }

    private void resetMiningData() {
        this.blockMiningLocation = null;
        this.blockFaceMiningAgainst = null;
        this.startBlockBreakTick = 0;
        this.endBlockBreakTick = 0;
        this.breakTicks = 0;
    }

}
