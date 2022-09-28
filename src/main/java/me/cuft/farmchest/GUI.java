package me.cuft.farmchest;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GUI implements Listener
{
    private final Inventory inv;
    private final FarmChest main;
    private final Block chestBlock;
    private final Util util;

    public GUI(FarmChest main, Block chestBlock)
    {
        this.main = main;
        this.chestBlock = chestBlock;
        inv = Bukkit.createInventory(null, 36, ChatColor.DARK_GREEN + "Farm Loot Rates");
        this.util = new Util(main);

        initializeItems();
    }

    public void initializeItems()
    {
        if(chestBlock == null) return;
        //add current chest's items from config
        Chest chest = util.findChestObject(chestBlock.getLocation());


        for(Item item : chest.getItems())
        {
            inv.addItem(item.getItem());
        }

        inv.setItem(27, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/a92e31ffb59c90ab08fc9dc1fe26802035a3a47c42fee63423bcdb4262ecb9b6"), ChatColor.GREEN + "Save & Exit"));
        inv.setItem(28, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7"), ChatColor.RED + "Cancel"));
        inv.setItem(35, util.createGuiItem(new ItemStack(Material.CHEST, 1), "Chest Information", "" + chestBlock.getWorld().getName(), "" + chestBlock.getLocation().getX(), "" + chestBlock.getLocation().getY(), "" + chestBlock.getLocation().getZ()));
    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if(!event.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_GREEN + "Farm Loot Rates")) return;

        final ItemStack clickedItem = event.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player player = (Player) event.getWhoClicked();

        if(event.getRawSlot() <= 35)
        {
            event.setCancelled(true);
        }

        List<String> chestItemLore = event.getView().getItem(35).getItemMeta().getLore();
        Location loc = new Location(Bukkit.getWorld(chestItemLore.get(0)), Double.parseDouble(chestItemLore.get(1)), Double.parseDouble(chestItemLore.get(2)), Double.parseDouble(chestItemLore.get(3)));

        Chest chest = util.findChestObject(loc);

        switch(event.getRawSlot()){
            case 27:
                //SAVE STUFF
                main.saveCustomConfig();
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 1);
                return;
            case 28:
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                return;
            case 35:
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                return;
        }

        if(clickedItem.getItemMeta().getDisplayName().equals(chest.getItems().get(chest.getItems().size() - 1).getItem().getItemMeta().getDisplayName()) && player.getItemOnCursor().getType() != Material.AIR)
        {
            ItemStack tempItem = player.getItemOnCursor();
            tempItem.setAmount(1);
            chest.addItem(new Item(chest.getLocation(), tempItem, 10, 5, 20));
            itemGUI itemgui = new itemGUI(chest.getItems().get(0), main);

            player.setItemOnCursor(null);
            itemgui.openInventory(player);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }
        else if(event.getRawSlot() <= 35 && !clickedItem.getItemMeta().getDisplayName().equals(chest.getItems().get(chest.getItems().size() - 1).getItem().getItemMeta().getDisplayName()))
        {
            itemGUI itemgui = new itemGUI(util.itemFromItemStack(chest, clickedItem), main);
            itemgui.openInventory(player);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }

    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if(e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_GREEN + "Farm Loot Rates")) {
            e.setCancelled(true);
        }
    }
}
