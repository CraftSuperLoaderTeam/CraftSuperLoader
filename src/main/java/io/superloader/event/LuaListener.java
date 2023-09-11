package io.superloader.event;

import io.superloader.plugin.CSLBasePlugin;
import io.superloader.plugin.lua.LuaPlugin;
import org.bukkit.event.Event;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;

import java.util.logging.Level;

public class LuaListener implements CSLBaseListener{
    LuaPlugin plugin;
    LuaValue value;
    public LuaListener(LuaPlugin plugin, LuaValue value){
        this.plugin = plugin;
        this.value = value;
    }

    @Override
    public CSLBasePlugin getPlugin() {
        return plugin;
    }

    @Override
    public void eventHandle(Event event) {
        try{
            value.invoke();
        }catch (LuaError e){
            plugin.getLogger().log(Level.SEVERE,e.getLocalizedMessage(),e);
        }
    }
}
