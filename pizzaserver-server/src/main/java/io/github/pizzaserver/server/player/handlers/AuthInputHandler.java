package io.github.pizzaserver.server.player.handlers;

import org.cloudburstmc.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.trait.LiquidTrait;
import io.github.pizzaserver.api.event.type.block.BlockBreakEvent;
import io.github.pizzaserver.api.event.type.block.BlockStartBreakEvent;
import io.github.pizzaserver.api.event.type.block.BlockStopBreakEvent;
import io.github.pizzaserver.api.event.type.player.PlayerMoveEvent;
import io.github.pizzaserver.api.event.type.player.PlayerToggleSneakingEvent;
import io.github.pizzaserver.api.event.type.player.PlayerToggleSprintingEvent;
import io.github.pizzaserver.api.event.type.player.PlayerToggleSwimEvent;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.api.utils.Location;
import io.github.pizzaserver.server.level.world.ImplWorld;
import io.github.pizzaserver.server.network.protocol.ImplPacketHandlerPipeline;
import io.github.pizzaserver.server.player.ImplPlayer;
import org.cloudburstmc.protocol.bedrock.data.PlayerAuthInputData;
import org.cloudburstmc.protocol.bedrock.data.PlayerBlockActionData;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequest;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.cloudburstmc.protocol.bedrock.packet.ItemStackRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.cloudburstmc.protocol.common.PacketSignal;

import java.util.List;
import java.util.Optional;

public class AuthInputHandler implements BedrockPacketHandler {

    private static final float ROTATION_UPDATE_THRESHOLD = 1;
    private static final float MOVEMENT_DISTANCE_THRESHOLD = 0.1f;

    private final ImplPlayer player;
    private long nextExpectedTick = -1;

    public AuthInputHandler(ImplPlayer player) {
        this.player = player;
    }

    @Override
    public PacketSignal handle(PlayerAuthInputPacket packet) {
        if (!this.isTickValid(packet.getTick())) {
            this.player.disconnect();
            return PacketSignal.HANDLED;
        }

        if (this.player.isAlive() && this.player.hasSpawned()) {
            this.handleMovement(packet.getPosition(), packet.getRotation());

            for (PlayerAuthInputData input : packet.getInputData()) {
                switch (input) {
                    case START_SNEAKING:
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
                    case STOP_SNEAKING:
                        if (this.player.isSneaking()) {
                            PlayerToggleSneakingEvent stopSneakingEvent = new PlayerToggleSneakingEvent(this.player, false);
                            this.player.getServer().getEventManager().call(stopSneakingEvent);
                            if (stopSneakingEvent.isCancelled()) {
                                this.player.getMetaData().update();
                            } else {
                                this.player.setSneaking(stopSneakingEvent.isSneaking());
                            }
                        }
                        break;
                    case START_SWIMMING:
                        Block blockSwimmingIn = this.player.getWorld().getBlock(this.player.getLocation().toVector3i());
                        if (!this.player.isSwimming() && blockSwimmingIn instanceof LiquidTrait) {
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
                        if (!this.player.isSprinting()) {
                            PlayerToggleSprintingEvent startSprintEvent = new PlayerToggleSprintingEvent(this.player, true);
                            this.player.getServer().getEventManager().call(startSprintEvent);

                            if (!startSprintEvent.isCancelled()) {
                                this.player.setSprinting(true);
                            } else {
                                this.player.getMetaData().update();
                            }
                        }
                        break;
                    case STOP_SPRINTING:
                        if (this.player.isSprinting()) {
                            PlayerToggleSprintingEvent stopSprintEvent = new PlayerToggleSprintingEvent(this.player, false);
                            this.player.getServer().getEventManager().call(stopSprintEvent);

                            if (!stopSprintEvent.isCancelled()) {
                                this.player.setSprinting(false);
                            } else {
                                this.player.getMetaData().update();
                            }
                        }
                        break;
                    case PERFORM_BLOCK_ACTIONS:
                        this.handleBlockActions(packet.getPlayerActions());
                        break;
                    case PERFORM_ITEM_STACK_REQUEST:
                        ItemStackRequestPacket requestPacket = new ItemStackRequestPacket();
                        requestPacket.getRequests().add(new ItemStackRequest(packet.getItemStackRequest().getRequestId(),
                                packet.getItemStackRequest().getActions(),
                                packet.getItemStackRequest().getFilterStrings()));

                        ((ImplPacketHandlerPipeline) this.player.getPacketHandlerPipeline()).accept(requestPacket);
                        break;
                    case UP:
                    case DOWN:
                    case LEFT:
                    case RIGHT:
                    case WANT_DOWN:
                    case WANT_UP:
                    case NORTH_JUMP:
                    case JUMP_DOWN:
                    case JUMPING:
                    case START_JUMPING:
                    case ASCEND:
                    case DESCEND:
                    case CHANGE_HEIGHT:
                    case PERSIST_SNEAK:
                    case SNEAK_DOWN:
                    case SNEAKING:
                        // For now, we don't need to handle this.
                        // However, if we ever want to implement server-side knockback using the rewind system. This may be useful.
                        break;
                    default:
                        this.player.getServer().getLogger().debug("Unknown input retrieved in AuthInputHandler: " + input);
                        break;
                }
            }

            // TODO: move inventory transaction handlers here once ALL inventory transactions all handled via the auth packet
            // at the moment, only the use inventory transaction is handled
        }
        return PacketSignal.HANDLED;
    }

    /**
     * Check if the tick sent in a player auth input packet is the next tick we are looking for.
     * @param tick the tick
     * @return if it is valid
     */
    private boolean isTickValid(long tick) {
        if (this.nextExpectedTick == -1) {
            this.nextExpectedTick = tick + 1;

            if (tick < 0) {
                this.player.getServer().getLogger().debug("Player sent invalid starting tick.");
                return false;
            }
        } else {
            if (this.nextExpectedTick != tick) {
                this.player.getServer().getLogger().debug(String.format("Player sent tick %d when expecting %d.", tick, this.nextExpectedTick));
                return false;
            }
            this.nextExpectedTick++;
        }

        return true;
    }

    private void handleBlockActions(List<PlayerBlockActionData> actions) {
        for (PlayerBlockActionData action : actions) {
            switch (action.getAction()) {
                case START_BREAK, BLOCK_CONTINUE_DESTROY -> {
                    if (this.player.isAlive()
                            && this.player.canReach(action.getBlockPosition(), this.player.isCreativeMode() ? 13 : 7)) {
                        BlockLocation blockBreakingLocation = new BlockLocation(this.player.getWorld(), action.getBlockPosition(), 0);
                        Block targetBlock = blockBreakingLocation.getBlock();

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

                            boolean canBreakNewBlock = !(targetBlock instanceof LiquidTrait)
                                    && !targetBlock.isAir()
                                    && this.player.getAdventureSettings().canMine();
                            if (canBreakNewBlock) {
                                // Special case: Fire does not trigger BLOCK_PREDICT_DESTROY
                                if (!this.playerMinedNormalBlockOrFireBlock(targetBlock, BlockFace.resolve(action.getFace()))) {
                                    break;
                                }

                                BlockStartBreakEvent blockStartBreakEvent = new BlockStartBreakEvent(this.player, targetBlock);
                                this.player.getServer().getEventManager().call(blockStartBreakEvent);
                                if (!blockStartBreakEvent.isCancelled()) {
                                    this.player.getBlockBreakingManager().startBreaking(blockBreakingLocation, BlockFace.resolve(action.getFace()));
                                } else {
                                    this.player.getWorld().sendBlock(this.player, blockBreakingLocation.toVector3i());
                                }
                            }
                        }
                    }
                }
                case BLOCK_PREDICT_DESTROY -> {
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
                        } else {
                            ((ImplWorld) this.player.getBlockBreakingManager().getBlock().get().getWorld())
                                    .sendBlock(this.player, this.player.getBlockBreakingManager().getBlock().get().getLocation().toVector3i());
                        }
                    } else {
                        this.player.getServer().getLogger().debug(String.format("%s tried to destroy a block but was not allowed.", this.player.getUsername()));

                        // Prevent malicious clients from requesting far away blocks to load unloaded chunks
                        if (action.getBlockPosition().distance(this.player.getLocation().toVector3i()) < this.player.getChunkRadius() * 16) {
                            this.player.getWorld().sendBlock(this.player, action.getBlockPosition());
                        }
                    }
                }
                case ABORT_BREAK -> {
                    if (this.player.getBlockBreakingManager().getBlock().isPresent()) {
                        BlockStopBreakEvent blockStopBreakEvent = new BlockStopBreakEvent(this.player, this.player.getBlockBreakingManager().getBlock().get());
                        this.player.getServer().getEventManager().call(blockStopBreakEvent);

                        this.player.getBlockBreakingManager().stopBreaking();
                    }
                }
                default -> this.player.getServer().getLogger().debug("Unknown block input action retrieved: " + action.getAction());
            }
        }
    }

    /**
     * Fire blocks are handled differently from other blocks when mined.
     * Returns if the player successfully broke fire block or if the player was not mining a fire block.
     * @param block the block retrieved from the auth packet
     * @param face the block face retrieved from the auth packet
     * @return if the player successfully broke fire block or if the player was not mining a fire block
     */
    private boolean playerMinedNormalBlockOrFireBlock(Block block, BlockFace face) {
        Block possibleFireBlock = block.getSide(face);

        boolean isBlockFire = possibleFireBlock.getBlockId().equals(BlockID.FIRE);
        if (isBlockFire) {
            // Special case: Fire does not fire BLOCK_PREDICT_DESTROY
            BlockStartBreakEvent startBreakEvent = new BlockStartBreakEvent(this.player, possibleFireBlock);
            this.player.getServer().getEventManager().call(startBreakEvent);
            if (startBreakEvent.isCancelled()) {
                this.player.getWorld().sendBlock(this.player, possibleFireBlock.getLocation().toVector3i());
                return false;
            }

            BlockBreakEvent blockBreakEvent = new BlockBreakEvent(this.player, possibleFireBlock);
            this.player.getServer().getEventManager().call(blockBreakEvent);
            if (!blockBreakEvent.isCancelled()) {
                this.player.getBlockBreakingManager().startBreaking(possibleFireBlock.getLocation(), face);
                this.player.getBlockBreakingManager().breakBlock();
                return true;
            } else {
                this.player.getWorld().sendBlock(this.player, possibleFireBlock.getLocation().toVector3i());
                return false;
            }
        }

        return true;
    }

    private void handleMovement(Vector3f position, Vector3f rotation) {
        boolean updateRotation = Math.abs(this.player.getPitch() - rotation.getX()) > ROTATION_UPDATE_THRESHOLD
                || Math.abs(this.player.getYaw() - rotation.getY()) > ROTATION_UPDATE_THRESHOLD
                || Math.abs(this.player.getHeadYaw() - rotation.getZ()) > ROTATION_UPDATE_THRESHOLD;
        boolean updatePosition = position.distance(this.player.getLocation().toVector3f().add(0, this.player.getEyeHeight(), 0)) > MOVEMENT_DISTANCE_THRESHOLD;

        if (updateRotation || updatePosition) {
            Location newLocation = new Location(this.player.getWorld(), position, rotation);

            PlayerMoveEvent moveEvent = new PlayerMoveEvent(this.player, this.player.getLocation(), newLocation);
            this.player.getServer().getEventManager().call(moveEvent);

            if (moveEvent.isCancelled()) {
                this.player.teleport(this.player.getLocation());
                return;
            }

            this.player.moveTo(position.getX(),
                    position.getY() - this.player.getEyeHeight(),
                    position.getZ(),
                    rotation.getX(),
                    rotation.getY(),
                    rotation.getZ());
        }
    }

}
