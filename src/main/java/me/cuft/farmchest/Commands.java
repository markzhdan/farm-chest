package me.cuft.farmchest;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Commands implements CommandExecutor
{
    final FarmChest main;

    public Commands(FarmChest main)
    {
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {return true;}

        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase("farmchest"))
        {
            if(player.isOp())
            {
                ItemStack wand = new ItemStack(Material.WOODEN_HOE);
                ItemMeta meta = wand.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Farm Chest Wand");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.LIGHT_PURPLE + "Click on a chest!");
                meta.setLore(lore);
                wand.setItemMeta(meta);

                player.getInventory().addItem(wand);
            }
            else
            {
                player.sendMessage(ChatColor.RED + "You do not have the required permissions");
            }
        }

        return true;
    }
}
