package com.gmail.mcdlutze.foodstuffs;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EatCommandExecutorTest {

    private EatCommandExecutor sut;

    @Mock CommandSender commandSender;
    @Mock Player player;
    @Mock PlayerInventory playerInventory;
    @Mock Command command;
    @Mock ItemStack itemStack;
    @Mock ItemMeta itemMeta;
    @Mock AttributeInstance attributeInstance;

    @Before
    public void setUp() {
        sut = new EatCommandExecutor();

        when(itemStack.getItemMeta()).thenReturn(itemMeta);

        when(player.getInventory()).thenReturn(playerInventory);
        when(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(attributeInstance);

        when(attributeInstance.getValue()).thenReturn(20.0);
    }

    @Test
    public void nonPlayerTest() {
        assertFalse(sut.onCommand(commandSender, command, "eat", new String[]{}));

        verify(commandSender).sendMessage("This command can only be executed by a player.");
    }

    @Test
    public void wrongCommandTest() {
        when(command.getName()).thenReturn("notEat");

        assertFalse(sut.onCommand(player, command, "eat", new String[]{}));
    }

    @Test
    public void inedibleTest() {
        when(command.getName()).thenReturn("eat");

        when(playerInventory.getItemInMainHand()).thenReturn(itemStack);

        when(itemMeta.hasDisplayName()).thenReturn(true);
        when(itemMeta.hasLore()).thenReturn(true);
        when(itemStack.getType()).thenReturn(Material.ANVIL);

        assertFalse(sut.onCommand(player, command, "eat", new String[]{}));

        verify(player).sendMessage("You are not holding an edible item.");
    }

    @Test
    public void emptyHandTest() {
        when(command.getName()).thenReturn("eat");

        when(playerInventory.getItemInMainHand()).thenReturn(null);

        assertFalse(sut.onCommand(player, command, "eat", new String[]{}));

        verify(player).sendMessage("You are not holding an edible item.");
    }

    @Test
    public void noLoreTest() {
        when(command.getName()).thenReturn("eat");

        when(playerInventory.getItemInMainHand()).thenReturn(itemStack);

        when(itemMeta.hasDisplayName()).thenReturn(false);
        when(itemMeta.hasLore()).thenReturn(false);
        when(itemStack.getType()).thenReturn(Material.APPLE);

        assertFalse(sut.onCommand(player, command, "eat", new String[]{}));

        verify(player).sendMessage("You are not holding an edible item.");
    }

    @Test
    public void eatTest() {
        when(command.getName()).thenReturn("eat");

        when(playerInventory.getItemInMainHand()).thenReturn(itemStack);

        when(itemMeta.hasDisplayName()).thenReturn(true);
        when(itemMeta.hasLore()).thenReturn(true);
        when(itemStack.getType()).thenReturn(Material.APPLE);

        assertTrue(sut.onCommand(player, command, "eat", new String[]{}));

        verify(player).setFoodLevel(20);
        verify(player).setSaturation(20);
        verify(player).setHealth(20);
    }

}