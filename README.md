# Farm Chest
>One of my big projects...

Minecraft Spigot plugin for Odium SMP that introduced farm chests / loot boxes into the game.

# Purpose
Adds chests that spawn loot in order to provide extra resources to players. Spawns assigned amount of loot inside the chest at a given rate when a player 
is in the spawn radius.


# Features
1. Stores data in a YML file.
    - Stored data regarding:
        - Chest items
        - Chest location / information
        - Item spawn rate
        - Item spawn amount
        - Spawn radius
        

2. Dynamic and responsive custom GUI.
      - Displayed a different GUI based on a normal player and an admin.
      - Add, remove, and update (amount, rate, radius) items within chest.
      - Multiple tabs and elements.

3. Supports unlimited amount of chests in the world.
      - Each chest can store and spawn 27 items with all unique data (rates, amount, radius).

4. Player QOL.
      - Simple to use plugin with little to no interactions (keeps plugin simple with high output impact).

5. Admin control.
      - Allowed admins to change item data through a custom GUI.
      - Sped up admin productivity and management.
      

![Screenshot (97)](https://user-images.githubusercontent.com/51387320/192681617-5008ff75-6ec5-4bbe-8992-afbb84937c3f.png)


# Commands
**/farmchest**
- Must have admin permissions
- Gives the player a "wand" that can be used to click on chests to open GUI and update items


## To-Do
Edit values directly by clicking the anvil and typing it out. Only works with "slider" buttons.

Add custom hover text/display above chest to display chest data.
