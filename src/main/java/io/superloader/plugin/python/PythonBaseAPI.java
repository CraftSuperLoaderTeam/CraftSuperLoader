package io.superloader.plugin.python;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class PythonBaseAPI {

    public Server getServer(){
        return Bukkit.getServer();
    }

    public Player getPlayer(String name){
        return Bukkit.getPlayer(name);
    }
}
