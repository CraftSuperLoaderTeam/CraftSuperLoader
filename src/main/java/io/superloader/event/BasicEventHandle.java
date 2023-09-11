package io.superloader.event;

import io.superloader.plugin.lua.LuaPlugin;
import io.superloader.plugin.lua.api.Minecraft;
import org.bukkit.event.Event;
import org.bukkit.event.server.ServerListPingEvent;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;

public class BasicEventHandle {

    static ArrayList<CSLBaseListener> listeners = new ArrayList<>();

    public static void onEvent(Event event){
        if("ServerListPingEvent".equals(event.getEventName())){
            onPing((ServerListPingEvent) event);
            return;
        }
        for(CSLBaseListener listener:listeners){
            listener.eventHandle(event);
        }
    }

    public static void addLuaScript(LuaPlugin plugin, LuaValue function){
        listeners.add(new LuaListener(plugin,function));
    }

    public static void onPing(ServerListPingEvent event){
        if(Minecraft.motd==null)return;
        event.setMotd(Minecraft.motd);
    }
}
