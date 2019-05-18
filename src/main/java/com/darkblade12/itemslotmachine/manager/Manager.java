package com.darkblade12.itemslotmachine.manager;

import com.darkblade12.itemslotmachine.ItemSlotMachine;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Manager implements Listener {

    protected final ItemSlotMachine plugin;

    protected Manager(ItemSlotMachine plugin) {
        this.plugin = plugin;
    }

    protected abstract boolean onInitialize();

    protected abstract void onDisable();

    public boolean onReload() {
        onDisable();
        return onInitialize();
    }

    protected final void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    protected final void unregisterAll() {
        HandlerList.unregisterAll(this);
    }
}
