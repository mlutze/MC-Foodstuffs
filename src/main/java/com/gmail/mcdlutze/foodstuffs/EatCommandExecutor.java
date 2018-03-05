package com.gmail.mcdlutze.foodstuffs;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EatCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return false;
        }
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("eat")) {
            return tryEat(player);
        }
        return false;
    }

    private boolean tryEat(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (canEat(itemInHand)) {
            refresh(player);
            player.getInventory().setItemInMainHand(eaten(itemInHand));
            return true;
        } else {
            player.sendMessage("You are not holding an edible item.");
            return false;
        }
    }

    private void refresh(Player player) {
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        player.setHealth(maxHealth);
        player.setFoodLevel(20);
        player.setSaturation(20);
    }

    private boolean canEat(ItemStack itemStack) {
        return itemStack != null && itemStack.getType().isEdible() &&
                (itemStack.getItemMeta().hasDisplayName() || itemStack.getItemMeta().hasLore());
    }

    private ItemStack eaten(ItemStack itemStack) {
        int amount = itemStack.getAmount();
        itemStack.setAmount(amount - 1);
        return itemStack;
    }
}
