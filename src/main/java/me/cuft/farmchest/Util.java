package me.cuft.farmchest;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public class Util
{
    private final FarmChest main;

    public Util(FarmChest main){
        this.main = main;
    }

    protected ItemStack createGuiItem(final ItemStack skull, final String name, final String... lore) {
        final ItemStack item = skull;
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getSkull(String url) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        if(url.isEmpty())return item;


        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures",new Property("textures", new String(encodedData)));
        Field profileField = null;
        try
        {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        }
        catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    public Chest findChestObject(Location location)
    {
        for(Chest chest : main.getActiveChests())
        {
            Location chestBlockLocation = chest.getLocation();
            if(chestBlockLocation.equals(location))
            {
                return chest;
            }
        }
        return null;
    }

    public Item itemFromItemStack(Chest chest, ItemStack itemStack)
    {
        for(Item item : chest.getItems())
        {
            if(item.getItem().getType().equals(itemStack.getType()))
            {
                return item;
            }
        }
        return null;
    }

    public boolean numberOrNot(String input)
    {
        try
        {
            Integer.parseInt(input);
        }
        catch(NumberFormatException ex)
        {
            return false;
        }
        return true;
    }
}
