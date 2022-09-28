package me.cuft.farmchest;

import org.bukkit.Location;

import java.util.ArrayList;

//CHEST ITEM. each one is unique chest
public class Chest
{
    Location location;
    ArrayList<Item> items = new ArrayList<>();

    public Chest(Location location, Item item)
    {
        this.location = location;
        this.items.add(0, item);
    }

    public Location getLocation(){
        return location;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addItem(Item item){
        this.items.add(0, item);
    }
}
