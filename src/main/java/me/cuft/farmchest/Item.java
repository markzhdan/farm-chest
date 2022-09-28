package me.cuft.farmchest;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Item
{
    Location chestLocation;
    ItemStack item;
    int quantity;
    int rate;
    int radius;

    public Item(Location location, ItemStack item, int quantity, int rate, int radius)
    {
        this.chestLocation = location;
        this.item = item;
        this.quantity = quantity;
        this.rate = rate;
        this.radius = radius;
    }

    public ItemStack getItem(){ return item; }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRate() {
        return rate;
    }

    public void addRate(int rate) {
        this.rate += rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getRadius() {
        return radius;
    }

    public void addRadius(int radius) {
        this.radius += radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Location getChestLocation() { return chestLocation; }
}
