package io.superloader.plugin.lua.api;

import io.superloader.plugin.lua.LuaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public class Minecraft extends TwoArgFunction {
    LuaPlugin plugin;

    public static String motd = null;

    public Minecraft(LuaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue value = tableOf();
        value.set("version",new Version());
        value.set("setMotd",new SetMotd());
        value.set("getPlayerUid",new GetPlayerUid());
        env.set("Minecraft",value);
        env.get("package").get("loaded").set("Minecraft",value);
        return value;
    }

    static class GetPlayerUid extends OneArgFunction{

        @Override
        public LuaValue call(LuaValue luaValue) {
            if(luaValue.isstring()){
                Player player = Bukkit.getPlayer(luaValue.toString());
                if(player == null) return LuaValue.NIL;
                else LuaValue.valueOf(player.getUniqueId().toString());
            }else throw new IllegalArgumentException("Minecraft:getPlayerUid The player name type must is string.");
            return null;
        }
    }

    static class Version extends ZeroArgFunction{
        @Override
        public LuaValue call() {
            return LuaValue.valueOf(((CraftServer) Bukkit.getServer()).getServer().getVersion());
        }
    }

    static class SetMotd extends OneArgFunction{
        private static String valueof(String s){
            return s.replace('&', ChatColor.COLOR_CHAR).replace("&&","&");
        }

        @Override
        public LuaValue call(LuaValue luaValue) {
            if(luaValue.isstring()){
                motd = SetMotd.valueof(luaValue.toString());
            }else throw new IllegalArgumentException("Minecraft:setMotd This value can only be of type string.");
            return null;
        }
    }
}
