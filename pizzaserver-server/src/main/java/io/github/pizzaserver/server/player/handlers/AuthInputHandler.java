package io.github.pizzaserver.server.player.handlers;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.PlayerAuthInputData;
import com.nukkitx.protocol.bedrock.data.PlayerBlockActionData;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.PlayerAuthInputPacket;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.descriptors.Liquid;
import io.github.pizzaserver.api.event.type.block.BlockBreakEvent;
import io.github.pizzaserver.api.event.type.block.BlockStartBreakEvent;
import io.github.pizzaserver.api.event.type.block.BlockStopBreakEvent;
import io.github.pizzaserver.api.event.type.player.PlayerToggleSneakingEvent;
import io.github.pizzaserver.api.event.type.player.PlayerToggleSwimEvent;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.level.world.ImplWorld;
import io.github.pizzaserver.server.player.ImplPlayer;

import java.util.List;
import java.util.Optional;

public class AuthInputHandler implements BedrockPacketHandler {

    private static final float ROTATION_UPDATE_THRESHOLD = 1;
    private static final float MOVEMENT_DISTANCE_THRESHOLD = 0.1f;

    protected final ImplPlayer player;

    public AuthInputHandler(ImplPlayer player) {
        this.player = player;
    }

    @Override
    public boolean handle(PlayerAuthInputPacket packet) {
        // TODO: tick validation
        if (this.player.isAlive() && this.player.hasSpawned()) {
            this.handleMovement(packet.getPosition(), packet.getRotation());

            for (PlayerAuthInputData input : packet.getInputData()) {
                switch (input) {
                    case SNEAK_DOWN:
                        if (!this.player.isSneaking()) {
                            PlayerToggleSneakingEvent startSneakingEvent = new PlayerToggleSneakingEvent(this.player, true);
                            this.player.getServer().getEventManager().call(startSneakingEvent);
                            if (startSneakingEvent.isCancelled()) {
                                this.player.getMetaData().update();
                            } else {
                                this.player.setSneaking(startSneakingEvent.isSneaking());
                            }
                        }
                        break;
                    case START_SWIMMING:
                        Block blockSwimmingIn = this.player.getWorld().getBlock(this.player.getLocation().toVector3i());
                        if (!this.player.isSwimming() && blockSwimmingIn instanceof Liquid) {
                            PlayerToggleSwimEvent swimEvent = new PlayerToggleSwimEvent(this.player, true);
                            this.player.getServer().getEventManager().call(swimEvent);

                            if (!swimEvent.isCancelled()) {
                                this.player.setSwimming(true);
                            }
                        }
                        break;
                    case STOP_SWIMMING:
                        if (this.player.isSwimming()) {
                            PlayerToggleSwimEvent swimEvent = new PlayerToggleSwimEvent(this.player, false);
                            this.player.getServer().getEventManager().call(swimEvent);

                            if (!swimEvent.isCancelled()) {
                                this.player.setSwimming(false);
                            }
                        }
                        break;
                    case START_SPRINTING:
                    case SPRINTING:
                    case SPRINT_DOWN:
                        this.player.sendMessage("start sprinting");
                        break;
                    case STOP_SPRINTING:
                        this.player.sendMessage("stop sprinting");
                        break;
                    case PERFORM_BLOCK_ACTIONS:
                        this.handleBlockActions(packet.getPlayerActions());
                        break;
                    case UP:
                    case DOWN:
                    case LEFT:
                    case RIGHT:
                        // For now, we don't need to handle this.
                        // However, if we ever want to implement server-side knockback using the rewind system. This may be useful.
                        break;
                    default:
                        this.player.getServer().getLogger().debug("Unknown input retrieved in AuthInputHandler: " + input);
                        break;
                }
            }

            // If the player is no longer sneaking
            if (this.player.isSneaking() && !packet.getInputData().contains(PlayerAuthInputData.SNEAK_DOWN)) {
                PlayerToggleSneakingEvent stopSneakingEvent = new PlayerToggleSneakingEvent(this.player, false);
                this.player.getServer().getEventManager().call(stopSneakingEvent);
                if (stopSneakingEvent.isCancelled()) {
                    this.player.getMetaData().update();
                } else {
                    this.player.setSneaking(stopSneakingEvent.isSneaking());
                }
            }
            // TODO: move inventory transaction handlers here once ALL inventory transactions all handled via the auth packet
            // at the moment, only the use inventory transaction is handled
        }
        return true;
    }

    protected void handleBlockActions(List<PlayerBlockActionData> actions) {
        for (PlayerBlockActionData action : actions) {
            switch (action.getAction()) {
                case START_BREAK:
                case BLOCK_CONTINUE_DESTROY:
                    if (this.player.isAlive() && this.player.canReach(action.getBlockPosition(), this.player.inCreativeMode() ? 13 : 7)) {
                        BlockLocation blockBreakingLocation = new BlockLocation(this.player.getWorld(), action.getBlockPosition(), 0);

                        boolean isAlreadyBreakingBlock = this.player.getBlockBreakingManager().getBlock().isPresent();
                        boolean breakingSameBlock = isAlreadyBreakingBlock
                                && action.getBlockPosition().equals(this.player.getBlockBreakingManager().getBlock().get().getLocation().toVector3i());
                        if (breakingSameBlock) {
                            this.player.getBlockBreakingManager().sendUpdatedBreakProgress();
                        } else {
                            if (isAlreadyBreakingBlock) {
                                BlockStopBreakEvent blockStopBreakEvent = new BlockStopBreakEvent(this.player, this.player.getBlockBreakingManager().getBlock().get());
                                this.player.getServer().getEventManager().call(blockStopBreakEvent);

                                this.player.getBlockBreakingManager().stopBreaking();
                            }

                            boolean canBreakNewBlock = !(blockBreakingLocation.getBlock() instanceof Liquid)
                                    && !blockBreakingLocation.getBlock().isAir()
                                    && this.player.getAdventureSettings().canMine();
                            if (canBreakNewBlock) {
                                BlockStartBreakEvent blockStartBreakEvent = new BlockStartBreakEvent(this.player, blockBreakingLocation.getBlock());
                                this.player.getServer().getEventManager().call(blockStartBreakEvent);
                                if (!blockStartBreakEvent.isCancelled()) {
                                    this.player.getBlockBreakingManager().startBreaking(blockBreakingLocation);
                                }
                            }
                        }
                        break;
                    }
                    break;
                case BLOCK_PREDICT_DESTROY:
                    Optional<Block> blockMining = this.player.getBlockBreakingManager().getBlock();
                    boolean isCorrectBlockCoordinates = blockMining.isPresent() && action.getBlockPosition().equals(blockMining.get().getLocation().toVector3i());
                    boolean canBreakBlock = isCorrectBlockCoordinates && this.player.getBlockBreakingManager().canBreakBlock();

                    if (!isCorrectBlockCoordinates) {
                        this.player.getServer().getLogger().debug(String.format("%s tried to destroy a block while not breaking the block.", this.player.getUsername()));
                    }

                    if (canBreakBlock) {
                        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(this.player, this.player.getWorld().getBlock(action.getBlockPosition()));
                        this.player.getServer().getEventManager().call(blockBreakEvent);

                        if (!blockBreakEvent.isCancelled()) {
                            this.player.getBlockBreakingManager().breakBlock();
                            return;
                        } else {
                            ((ImplWorld) this.player.getBlockBreakingManager().getBlock().get().getWorld())
                                    .sendBlock(this.player, this.player.getBlockBreakingManager().getBlock().get().getLocation().toVector3i());
                        }
                    } else {
                        this.player.getServer().getLogger().debug(String.format("%s tried to destroy a block but was not allowed.", this.player.getUsername()));
                    }
                    break;
                case ABORT_BREAK:
                    if (this.player.getBlockBreakingManager().getBlock().isPresent()) {
                        BlockStopBreakEvent blockStopBreakEvent = new BlockStopBreakEvent(this.player, this.player.getBlockBreakingManager().getBlock().get());
                        this.player.getServer().getEventManager().call(blockStopBreakEvent);

                        this.player.getBlockBreakingManager().stopBreaking();
                    }
                    break;
                default:
                    this.player.getServer().getLogger().debug("Unknown block input action retrieved: " + action.getAction());
                    break;
            }
        }
    }

    protected void handleMovement(Vector3f position, Vector3f rotation) {
        boolean updateRotation = Math.abs(this.player.getPitch() - rotation.getX()) > ROTATION_UPDATE_THRESHOLD
                || Math.abs(this.player.getYaw() - rotation.getY()) > ROTATION_UPDATE_THRESHOLD
                || Math.abs(this.player.getHeadYaw() - rotation.getZ()) > ROTATION_UPDATE_THRESHOLD;
        if (updateRotation) {
            this.player.setPitch(rotation.getX());
            this.player.setYaw(rotation.getY());
            this.player.setHeadYaw(rotation.getZ());
        }

        boolean updatePosition = position.distance(this.player.getLocation().toVector3f()) > MOVEMENT_DISTANCE_THRESHOLD;
        if (updatePosition) {
            this.player.moveTo(position.getX(), position.getY() - this.player.getEyeHeight(), position.getZ());
        }
    }

}
