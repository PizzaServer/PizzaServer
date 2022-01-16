package io.github.pizzaserver.server.player;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.impl.BlockAir;
import io.github.pizzaserver.api.block.impl.BlockDirt;
import io.github.pizzaserver.api.block.impl.BlockIronOre;
import io.github.pizzaserver.api.block.impl.BlockStone;
import io.github.pizzaserver.api.entity.inventory.PlayerInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.item.impl.ItemShears;
import io.github.pizzaserver.api.item.impl.ItemStonePickaxe;
import io.github.pizzaserver.api.item.impl.ItemWoodenPickaxe;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.player.manager.PlayerBlockBreakingManager;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerBlockBreakingManagerTests {

    @Test
    public void shouldNotLetPlayerBreakBlockTooEarly() {
        float minimumBreakPercentage = 0.8f;

        Server mockServer = mock(Server.class);
        when(mockServer.getTick()).thenReturn(0L,
                                              // starting tick
                                              (long) Math.ceil(15L * minimumBreakPercentage) - 1,
                                              // tick of server before block can be broken
                                              (long) Math.ceil(15L
                                                                       * minimumBreakPercentage));    // tick of server after block can be broken


        World mockWorld = mock(World.class);
        Chunk mockChunk = mock(Chunk.class);

        Player mockPlayer = mock(Player.class);
        PlayerInventory mockPlayerInventory = mock(PlayerInventory.class);
        when(mockPlayer.getInventory()).thenReturn(mockPlayerInventory);
        when(mockPlayerInventory.getHeldItem()).thenReturn(new ItemShears());

        PlayerBlockBreakingManager blockBreakingManager = new PlayerBlockBreakingManager(mockPlayer);
        BlockLocation blockLocation = new BlockLocation(mockWorld, 0, 0, 0, 0);
        Block blockMining = new BlockDirt();
        blockMining.setLocation(blockLocation);

        when(mockWorld.getBlock(0, 0, 0, 0)).thenReturn(blockMining);
        when(mockWorld.getChunk(0, 0)).thenReturn(mockChunk);
        when(mockChunk.getViewers()).thenReturn(Collections.emptySet());
        when(mockPlayer.getServer()).thenReturn(mockServer);


        blockBreakingManager.startBreaking(blockLocation);
        assertFalse(blockBreakingManager.canBreakBlock(), "player was able to break a dirt block too quick");
        assertTrue(blockBreakingManager.canBreakBlock(),
                   "player was not able to break a dirt block despite enough ticks passing.");
    }

    @Test
    public void breakingShouldBeQuickerAndCorrectWithCorrectTool() {
        Item correctToolAndTier = new ItemStonePickaxe();
        Item correctTool = new ItemWoodenPickaxe();
        int expectedTicksWithTool = 150;
        int expectedTicksWithoutTool = 300;


        Server mockServer = mock(Server.class);
        when(mockServer.getTick()).thenReturn(0L);

        World mockWorld = mock(World.class);
        Chunk mockChunk = mock(Chunk.class);
        Player mockPlayer = mock(Player.class);
        PlayerInventory mockPlayerInventory = mock(PlayerInventory.class);
        when(mockPlayer.getInventory()).thenReturn(mockPlayerInventory);
        when(mockPlayerInventory.getHeldItem()).thenReturn(new ItemBlock(new BlockAir()),
                                                           new ItemBlock(new BlockAir()),
                                                           correctTool,
                                                           correctToolAndTier);

        PlayerBlockBreakingManager blockBreakingManager = new PlayerBlockBreakingManager(mockPlayer);
        BlockLocation blockLocation = new BlockLocation(mockWorld, 0, 0, 0, 0);
        Block blockMining = new BlockIronOre();
        blockMining.setLocation(blockLocation);

        when(mockWorld.getBlock(0, 0, 0, 0)).thenReturn(blockMining);
        when(mockWorld.getChunk(0, 0)).thenReturn(mockChunk);
        when(mockChunk.getViewers()).thenReturn(Collections.emptySet());
        when(mockPlayer.getServer()).thenReturn(mockServer);


        blockBreakingManager.startBreaking(blockLocation);

        int resultTicksWithoutTool = blockBreakingManager.getBreakTicks();
        int resultTicksWithTool = blockBreakingManager.getBreakTicks();
        int resultTicksWithToolAndTier = blockBreakingManager.getBreakTicks();
        assertEquals(expectedTicksWithoutTool, resultTicksWithoutTool, "break ticks without a tool was incorrect");
        assertEquals(expectedTicksWithTool, resultTicksWithTool, "break ticks with correct tool was incorrect");
        assertTrue(resultTicksWithToolAndTier < resultTicksWithTool,
                   "break ticks with best tool was slower than with the correct tool");
    }

    @Test
    public void breakTimeShouldChangeIfHandItemChanges() {
        Item originalItem = new ItemBlock(new BlockAir());
        Item switchingToItem = new ItemWoodenPickaxe();

        Server mockServer = mock(Server.class);
        when(mockServer.getTick()).thenReturn(0L);

        World mockWorld = mock(World.class);
        Chunk mockChunk = mock(Chunk.class);
        Player mockPlayer = mock(Player.class);
        PlayerInventory mockPlayerInventory = mock(PlayerInventory.class);
        when(mockPlayer.getInventory()).thenReturn(mockPlayerInventory);
        when(mockPlayerInventory.getHeldItem()).thenReturn(originalItem, originalItem, switchingToItem);

        PlayerBlockBreakingManager blockBreakingManager = new PlayerBlockBreakingManager(mockPlayer);
        BlockLocation blockLocation = new BlockLocation(mockWorld, 0, 0, 0, 0);
        Block blockMining = new BlockStone();
        blockMining.setLocation(blockLocation);

        when(mockWorld.getBlock(0, 0, 0, 0)).thenReturn(blockMining);
        when(mockWorld.getChunk(0, 0)).thenReturn(mockChunk);
        when(mockChunk.getViewers()).thenReturn(Collections.emptySet());
        when(mockPlayer.getServer()).thenReturn(mockServer);


        blockBreakingManager.startBreaking(blockLocation);
        int originalTicksLeft = blockBreakingManager.getBreakTicks();

        blockBreakingManager.onChangedHeldItemWhileBreaking();
        assertTrue(blockBreakingManager.getBreakTicks() != originalTicksLeft,
                   "there was no change in break ticks when switching items");
    }
}
