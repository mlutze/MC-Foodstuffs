package com.gmail.mcdlutze.foodstuffs;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("eat").setExecutor(new EatCommandExecutor());
    }

}
