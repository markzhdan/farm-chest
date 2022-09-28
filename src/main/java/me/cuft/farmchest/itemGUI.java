package me.cuft.farmchest;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class itemGUI implements Listener
{
    private final Inventory inv;
    private final FarmChest main;
    private final Util util;
    private final Item item;

    public itemGUI(Item item, FarmChest main)
    {
        this.item = item;
        this.main = main;
        this.util = new Util(main);

        inv = Bukkit.createInventory(null, 45, ChatColor.DARK_GREEN + "Item Rates");

        initializeItems();
    }

    public void initializeItems()
    {
        if(item == null) return;

        inv.setItem(0, util.createGuiItem(new ItemStack(Material.CHEST, 1), "Chest Information", "" + item.getChestLocation().getWorld().getName(), "" + item.getChestLocation().getX(), "" + item.getChestLocation().getY(), "" + item.getChestLocation().getZ()));

        inv.setItem(4, util.createGuiItem(item.getItem(), item.getItem().getType() + " Information"));

        inv.setItem(11, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/c4e490e1658bfde4d4ef1ea7cd646c5353377905a1369b86ee966746ae25ca7"), ChatColor.RED + "-10"));
        inv.setItem(12, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/ad73cf66d31b83cd8b8644c15958c1b73c8d97323b801170c1d8864bb6a846d"), ChatColor.RED + "-1"));
        inv.setItem(13, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/b7dc3e29a0923e52ecee6b4c9d533a79e74bb6bed541b495a13abd359627653"), ChatColor.WHITE + "Quantity:", ChatColor.GREEN + "" + item.getQuantity()));
        inv.setItem(14, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/c86185b1d519ade585f184c34f3f3e20bb641deb879e81378e4eaf209287"), ChatColor.GREEN + "+1"));
        inv.setItem(15, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/98f293f294980d732f523321c34a4cdcc3e6f9e36c9320e150f1cce31aa5"), ChatColor.GREEN + "+10"));
        inv.setItem(17, util.createGuiItem(new ItemStack(Material.ANVIL, 1), ChatColor.WHITE + "Edit Quantity"));

        inv.setItem(20, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/c4e490e1658bfde4d4ef1ea7cd646c5353377905a1369b86ee966746ae25ca7"), ChatColor.RED + "-10"));
        inv.setItem(21, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/ad73cf66d31b83cd8b8644c15958c1b73c8d97323b801170c1d8864bb6a846d"), ChatColor.RED + "-1"));
        inv.setItem(22, util.createGuiItem(new ItemStack(Material.CLOCK, 1), "Rate:", ChatColor.GREEN + "Every " + item.getRate() + " Minutes"));
        inv.setItem(23, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/c86185b1d519ade585f184c34f3f3e20bb641deb879e81378e4eaf209287"), ChatColor.GREEN + "+1"));
        inv.setItem(24, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/98f293f294980d732f523321c34a4cdcc3e6f9e36c9320e150f1cce31aa5"), ChatColor.GREEN + "+10"));
        inv.setItem(26, util.createGuiItem(new ItemStack(Material.ANVIL, 1), ChatColor.WHITE + "Edit Rate"));

        inv.setItem(29, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/c4e490e1658bfde4d4ef1ea7cd646c5353377905a1369b86ee966746ae25ca7"), ChatColor.RED + "-10"));
        inv.setItem(30, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/ad73cf66d31b83cd8b8644c15958c1b73c8d97323b801170c1d8864bb6a846d"), ChatColor.RED + "-1"));
        inv.setItem(31, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/647e2e5d55b6d04943519bed2557c6329e33b60b909dee8923cd88b115210"),ChatColor.WHITE + "Radius:", ChatColor.GREEN + "" + item.getRadius() + " Blocks"));
        inv.setItem(32, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/c86185b1d519ade585f184c34f3f3e20bb641deb879e81378e4eaf209287"), ChatColor.GREEN + "+1"));
        inv.setItem(33, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/98f293f294980d732f523321c34a4cdcc3e6f9e36c9320e150f1cce31aa5"), ChatColor.GREEN + "+10"));
        inv.setItem(35, util.createGuiItem(new ItemStack(Material.ANVIL, 1), ChatColor.WHITE + "Edit Radius"));


        inv.setItem(36, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/a92e31ffb59c90ab08fc9dc1fe26802035a3a47c42fee63423bcdb4262ecb9b6"), ChatColor.GREEN + "Save & Exit"));
        inv.setItem(37, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7"), ChatColor.RED + "Cancel"));

        inv.setItem(44, util.createGuiItem(new ItemStack(Material.BARRIER, 1), ChatColor.DARK_RED + "Delete"));
    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if(!event.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_GREEN + "Item Rates")) return;

        final ItemStack clickedItem = event.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        List<String> chestItemLore = event.getView().getItem(0).getItemMeta().getLore();
        Location loc = new Location(Bukkit.getWorld(chestItemLore.get(0)), Double.parseDouble(chestItemLore.get(1)), Double.parseDouble(chestItemLore.get(2)), Double.parseDouble(chestItemLore.get(3)));

        Chest chest = util.findChestObject(loc);

        Item itemStack = util.itemFromItemStack(chest, event.getView().getItem(4));

        // Using slots click is a best option for your inventory click's
        switch(event.getRawSlot()){
            case 11:
                itemStack.addQuantity(-10);
                player.getOpenInventory().setItem(13, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/b7dc3e29a0923e52ecee6b4c9d533a79e74bb6bed541b495a13abd359627653"), ChatColor.WHITE + "Quantity:", ChatColor.GREEN + "" + itemStack.getQuantity()));
                break;
            case 12:
                itemStack.addQuantity(-1);
                player.getOpenInventory().setItem(13, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/b7dc3e29a0923e52ecee6b4c9d533a79e74bb6bed541b495a13abd359627653"), ChatColor.WHITE + "Quantity:", ChatColor.GREEN + "" + itemStack.getQuantity()));
                break;
            case 14:
                itemStack.addQuantity(1);
                player.getOpenInventory().setItem(13, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/b7dc3e29a0923e52ecee6b4c9d533a79e74bb6bed541b495a13abd359627653"), ChatColor.WHITE + "Quantity:", ChatColor.GREEN + "" + itemStack.getQuantity()));
                break;
            case 15:
                itemStack.addQuantity(10);
                player.getOpenInventory().setItem(13, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/b7dc3e29a0923e52ecee6b4c9d533a79e74bb6bed541b495a13abd359627653"), ChatColor.WHITE + "Quantity:", ChatColor.GREEN + "" + itemStack.getQuantity()));
                break;
            case 20:
                itemStack.addRate(-10);
                player.getOpenInventory().setItem(22, util.createGuiItem(new ItemStack(Material.CLOCK, 1), "Rate:", ChatColor.GREEN + "Every " + itemStack.getRate() + " Minutes"));
                break;
            case 21:
                itemStack.addRate(-1);
                player.getOpenInventory().setItem(22, util.createGuiItem(new ItemStack(Material.CLOCK, 1), "Rate:", ChatColor.GREEN + "Every " + itemStack.getRate() + " Minutes"));
                break;
            case 23:
                itemStack.addRate(1);
                player.getOpenInventory().setItem(22, util.createGuiItem(new ItemStack(Material.CLOCK, 1), "Rate:", ChatColor.GREEN + "Every " + itemStack.getRate() + " Minutes"));
                break;
            case 24:
                itemStack.addRate(10);
                player.getOpenInventory().setItem(22, util.createGuiItem(new ItemStack(Material.CLOCK, 1), "Rate:", ChatColor.GREEN + "Every " + itemStack.getRate() + " Minutes"));
                break;
            case 29:
                itemStack.addRadius(-10);
                player.getOpenInventory().setItem(31, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/647e2e5d55b6d04943519bed2557c6329e33b60b909dee8923cd88b115210"),ChatColor.WHITE + "Radius:", ChatColor.GREEN + "" + itemStack.getRadius() + " Blocks"));
                break;
            case 30:
                itemStack.addRadius(-1);
                player.getOpenInventory().setItem(31, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/647e2e5d55b6d04943519bed2557c6329e33b60b909dee8923cd88b115210"),ChatColor.WHITE + "Radius:", ChatColor.GREEN + "" + itemStack.getRadius() + " Blocks"));
                break;
            case 32:
                itemStack.addRadius(1);
                player.getOpenInventory().setItem(31, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/647e2e5d55b6d04943519bed2557c6329e33b60b909dee8923cd88b115210"),ChatColor.WHITE + "Radius:", ChatColor.GREEN + "" + itemStack.getRadius() + " Blocks"));
                break;
            case 33:
                itemStack.addRadius(10);
                player.getOpenInventory().setItem(31, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/647e2e5d55b6d04943519bed2557c6329e33b60b909dee8923cd88b115210"),ChatColor.WHITE + "Radius:", ChatColor.GREEN + "" + itemStack.getRadius() + " Blocks"));
                break;
            case 17:
                new AnvilGUI.Builder()
                        .onComplete((player1, text) -> {                                    //called when the inventory output slot is clicked
                            if(util.numberOrNot(text)) {
                                itemStack.setQuantity(Integer.parseInt(text));
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                return AnvilGUI.Response.openInventory(new itemGUI(itemStack, main).inv);
                            } else {
                                return AnvilGUI.Response.text("Enter a valid number");
                            }
                        })                                                  //prevents the inventory from being closed
                        .text("" + itemStack.getQuantity())                              //sets the text the GUI should start with
                        .itemLeft(new ItemStack(Material.PAPER))                      //use a custom item for the second slot
                        .onLeftInputClick(player1 -> player.sendMessage("Rename the paper to set the value"))     //called when the left input slot is clicked
                        .title("Enter a value")                                       //set the title of the GUI (only works in 1.14+)
                        .plugin(main)                                          //set the plugin instance
                        .open(player);
                break;
            case 26:
                new AnvilGUI.Builder()
                        .onComplete((player1, text) -> {                                    //called when the inventory output slot is clicked
                            if(util.numberOrNot(text)) {
                                itemStack.setRate(Integer.parseInt(text));
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                return AnvilGUI.Response.openInventory(new itemGUI(itemStack, main).inv);
                            } else {
                                return AnvilGUI.Response.text("Enter a valid number");
                            }
                        })                                                  //prevents the inventory from being closed
                        .text("" + itemStack.getRate())                              //sets the text the GUI should start with
                        .itemLeft(new ItemStack(Material.PAPER))                      //use a custom item for the second slot
                        .onLeftInputClick(player1 -> player.sendMessage("Rename the paper to set the value"))     //called when the left input slot is clicked
                        .title("Enter a value")                                       //set the title of the GUI (only works in 1.14+)
                        .plugin(main)                                          //set the plugin instance
                        .open(player);
                break;
            case 35:
                new AnvilGUI.Builder()
                        .onComplete((player1, text) -> {                                    //called when the inventory output slot is clicked
                            if(util.numberOrNot(text)) {
                                itemStack.setRadius(Integer.parseInt(text));
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                return AnvilGUI.Response.openInventory(new itemGUI(itemStack, main).inv);
                            } else {
                                return AnvilGUI.Response.text("Enter a valid number");
                            }
                        })                                                  //prevents the inventory from being closed
                        .text("" + itemStack.getRadius())                              //sets the text the GUI should start with
                        .itemLeft(new ItemStack(Material.PAPER))                      //use a custom item for the second slot
                        .onLeftInputClick(player1 -> player.sendMessage("Rename the paper to set the value"))     //called when the left input slot is clicked
                        .title("Enter a value")                                       //set the title of the GUI (only works in 1.14+)
                        .plugin(main)                                          //set the plugin instance
                        .open(player);
                break;
            case 36:
                main.saveCustomConfig();
                player.closeInventory();
                break;
            case 37:
                //SAVE STUFF
                player.closeInventory();
                break;
            case 44:
                chest.getItems().remove(itemStack);
                player.closeInventory();
                break;
        }
        if(event.getRawSlot() <= 44)
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if(e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_GREEN + "Item Rates")) {
            e.setCancelled(true);
        }
    }
}
