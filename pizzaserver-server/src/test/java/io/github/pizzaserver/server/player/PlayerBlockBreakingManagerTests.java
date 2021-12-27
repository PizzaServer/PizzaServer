package io.github.pizzaserver.server.player;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.block.types.impl.BlockTypeAir;
import io.github.pizzaserver.api.block.types.impl.BlockTypeDirt;
import io.github.pizzaserver.api.block.types.impl.BlockTypeIronOre;
import io.github.pizzaserver.api.block.types.impl.BlockTypeStone;
import io.github.pizzaserver.api.entity.inventory.PlayerInventory;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.item.types.impl.ItemTypeShears;
import io.github.pizzaserver.api.item.types.impl.ItemTypeStonePickaxe;
import io.github.pizzaserver.api.item.types.impl.ItemTypeWoodenPickaxe;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.item.type.ImplBlockItemType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerBlockBreakingManagerTests {

    @Test
    public void shouldNotLetPlayerBreakBlockTooEarly() {
        float minimumBreakPercentage = 0.8f;

        Server mockServer = mock(Server.class);
        when(mockServer.getTick()).thenReturn(0L,   // starting tick
                (long) Math.ceil(15L * minimumBreakPercentage) - 1, // tick of server before block can be broken
                (long) Math.ceil(15L * minimumBreakPercentage));    // tick of server after block can be broken


        World mockWorld = mock(World.class);
        Chunk mockChunk = mock(Chunk.class);

        Player mockPlayer = mock(Player.class);
        PlayerInventory mockPlayerInventory = mock(PlayerInventory.class);
        when(mockPlayer.getInventory()).thenReturn(mockPlayerInventory);
        when(mockPlayerInventory.getHeldItem()).thenReturn(new ItemStack(new ItemTypeShears()));

        PlayerBlockBreakingManager blockBreakingManager = new PlayerBlockBreakingManager(mockPlayer);
        BlockLocation blockLocation = new BlockLocation(mockWorld, 0, 0, 0, 0);
        Block blockMining = new Block(new BlockTypeDirt());
        blockMining.setLocation(blockLocation);

        when(mockWorld.getBlock(0, 0, 0, 0)).thenReturn(blockMining);
        when(mockWorld.getChunk(0, 0)).thenReturn(mockChunk);
        when(mockChunk.getViewers()).thenReturn(Collections.emptySet());
        when(mockPlayer.getServer()).thenReturn(mockServer);


        blockBreakingManager.startBreaking(blockLocation);
        assertFalse(blockBreakingManager.canBreakBlock(), "player was able to break a dirt block too quick");
        assertTrue(blockBreakingManager.canBreakBlock(), "player was not able to break a dirt block despite enough ticks passing.");
    }

    @Test
    public void breakingShouldBeQuickerAndCorrectWithCorrectTool() {
        BlockType blockType = new BlockTypeIronOre();
        ItemStack correctToolAndTier = new ItemStack(new ItemTypeStonePickaxe());
        ItemStack correctTool = new ItemStack(new ItemTypeWoodenPickaxe());
        int expectedTicksWithTool = 150;
        int expectedTicksWithoutTool = 300;


        Server mockServer = mock(Server.class);
        when(mockServer.getTick()).thenReturn(0L);

        World mockWorld = mock(World.class);
        Chunk mockChunk = mock(Chunk.class);
        Player mockPlayer = mock(Player.class);
        PlayerInventory mockPlayerInventory = mock(PlayerInventory.class);
        when(mockPlayer.getInventory()).thenReturn(mockPlayerInventory);
        when(mockPlayerInventory.getHeldItem()).thenReturn(
                new ItemStack(new ImplBlockItemType(new BlockTypeAir())),
                new ItemStack(new ImplBlockItemType(new BlockTypeAir())),
                correctTool,
                correctToolAndTier);

        PlayerBlockBreakingManager blockBreakingManager = new PlayerBlockBreakingManager(mockPlayer);
        BlockLocation blockLocation = new BlockLocation(mockWorld, 0, 0, 0, 0);
        Block blockMining = new Block(blockType);
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
        assertTrue(resultTicksWithToolAndTier < resultTicksWithTool, "break ticks with best tool was slower than with the correct tool");
    }

    @Test
    public void breakTimeShouldChangeIfHandItemChanges() {
        ItemStack originalItem = new ItemStack(new ImplBlockItemType(new BlockTypeAir()));
        ItemStack switchingToItem = new ItemStack(new ItemTypeWoodenPickaxe());

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
        Block blockMining = new Block(new BlockTypeStone());
        blockMining.setLocation(blockLocation);

        when(mockWorld.getBlock(0, 0, 0, 0)).thenReturn(blockMining);
        when(mockWorld.getChunk(0, 0)).thenReturn(mockChunk);
        when(mockChunk.getViewers()).thenReturn(Collections.emptySet());
        when(mockPlayer.getServer()).thenReturn(mockServer);


        blockBreakingManager.startBreaking(blockLocation);
        int originalTicksLeft = blockBreakingManager.getBreakTicks();

        blockBreakingManager.onChangedHeldItemWhileBreaking();
        assertTrue(blockBreakingManager.getBreakTicks() != originalTicksLeft, "there was no change in break ticks when switching items");
    }

}
