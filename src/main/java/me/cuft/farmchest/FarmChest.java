package me.cuft.farmchest;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;


public final class FarmChest extends JavaPlugin implements Listener {

    public ItemStack WAND;
    private ArrayList<Chest> activeChests = new ArrayList<>();
    private final Util util = new Util(this);
    private int MINUTES = 0;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        saveConfig();
        try{
            initilizeConfig();
        } catch (Exception e) {}

        Commands cmds = new Commands(this);
        getCommand("farmchest").setExecutor(cmds);
        getServer().getPluginManager().registerEvents(new GUI(this, null), this);
        getServer().getPluginManager().registerEvents(new itemGUI(null, this), this);
        getServer().getPluginManager().registerEvents(this, this);
        createwand();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                MINUTES++;
                spawnItems();
            }
        }.runTaskTimer(this, 1200, 1200);
    }

    @Override
    public void onDisable() {
        saveCustomConfig();
        // Plugin shutdown logic
    }

    public void saveCustomConfig()
    {
        for(int i = 0; i < activeChests.size(); i++)
        {

            getConfig().set("AllChests." + i + "", "Chest " + i);
            getConfig().set("AllChests." + i + ".Location", activeChests.get(i).getLocation().serialize());
            int tempCount = 0;
            for(Item item : activeChests.get(i).getItems())
            {
                if(!item.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Add Item"))
                {
                    getConfig().set("AllChests." + i + ".Items.Amount", activeChests.get(i).getItems().size() - 1);
                    getConfig().set("AllChests." + i + ".Items.Item" + tempCount + ".ItemStack", item.getItem().serialize());
                    getConfig().set("AllChests." + i + ".Items.Item" + tempCount + ".Quantity", item.getQuantity());
                    getConfig().set("AllChests." + i + ".Items.Item" + tempCount + ".Rate", item.getRate());
                    getConfig().set("AllChests." + i + ".Items.Item" + tempCount + ".Radius", item.getRadius());
                    tempCount++;
                }
            }
        }
        saveConfig();
    }

    public void initilizeConfig()
    {
        for (String path : getConfig().getConfigurationSection("AllChests").getKeys(false)) {
            // s will return all different players
            int amt = getConfig().getInt("AllChests." + path + ".Items.Amount");

            ArrayList<Item> itemArrayList = new ArrayList<>();
            Location loc = null;
            for(int i = 0; i < amt; i++)
            {
                loc = Location.deserialize(getConfig().getConfigurationSection("AllChests." + path + ".Location").getValues(false));
                ItemStack itemStack = ItemStack.deserialize(getConfig().getConfigurationSection("AllChests." + path + ".Items.Item" + i + ".ItemStack").getValues(false));
                int rate = getConfig().getInt("AllChests." + path + ".Items.Item" + i + ".Rate");
                int quantity = getConfig().getInt("AllChests." + path + ".Items.Item" + i + ".Quantity");
                int radius = getConfig().getInt("AllChests." + path + ".Items.Item" + i + ".Radius");
                itemArrayList.add(new Item(loc, itemStack, quantity, rate, radius));
            }

            Chest tempChest = new Chest(loc, new Item(null, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/60b55f74681c68283a1c1ce51f1c83b52e2971c91ee34efcb598df3990a7e7"), ChatColor.GOLD + "Add Item", ChatColor.LIGHT_PURPLE + "Drag an item to add it to this chest's loot table"), 0, 1, 0));
            for (Item item : itemArrayList)
            {
                tempChest.addItem(item);
            }

            activeChests.add(tempChest);
        }
    }

    public void spawnItems()
    {
        for(Chest chest : activeChests)
        {
            org.bukkit.block.Chest c;
            try{
                c = (org.bukkit.block.Chest) chest.getLocation().getBlock().getState();
            }catch (Exception e){
                activeChests.remove(chest);
                break;
            }
            Inventory inv = c.getBlockInventory();
            for(Item item : chest.getItems())
            {
                for(Player target : Bukkit.getOnlinePlayers()){
                    if(target.getWorld().equals(chest.getLocation().getWorld()))
                    {
                        if(target.getLocation().distance(chest.getLocation()) <= item.getRadius()){
                            if((MINUTES % item.getRate()) == 0)
                            {
                                ItemStack tempItem = new ItemStack(item.getItem().getType(), item.getQuantity());
                                inv.addItem(tempItem);

                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        Action action = event.getAction();
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if(action.equals(Action.RIGHT_CLICK_BLOCK) && player.getInventory().getItemInMainHand().equals(WAND) && player.isOp() && block.getType().equals(Material.CHEST))
        {
            event.setCancelled(true);

            if(!doesChestExist(block.getLocation()))
            {
                activeChests.add(new Chest(block.getLocation(), new Item(null, util.createGuiItem(util.getSkull("http://textures.minecraft.net/texture/60b55f74681c68283a1c1ce51f1c83b52e2971c91ee34efcb598df3990a7e7"), ChatColor.GOLD + "Add Item", ChatColor.LIGHT_PURPLE + "Drag an item to add it to this chest's loot table"), 0, 1, 0)));
            }

            GUI gui = new GUI(this, block);

            gui.openInventory(player);

            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, (float) 0.5);
        }
    }

    public boolean doesChestExist(Location location)
    {
        for(Chest chest  : activeChests)
        {
            if(chest.getLocation().equals(location))
            {
                return true;
            }
        }
        return false;
    }

    public void createwand()
    {
        WAND = new ItemStack(Material.WOODEN_HOE);
        ItemMeta meta = WAND.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GREEN + "Farm Chest Wand");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.LIGHT_PURPLE + "Click on a chest!");
        meta.setLore(lore);
        WAND.setItemMeta(meta);
    }

    public ArrayList<Chest> getActiveChests() {
        return activeChests;
    }
}