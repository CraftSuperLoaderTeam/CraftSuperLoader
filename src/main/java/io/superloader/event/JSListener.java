package io.superloader.event;

import io.superloader.plugin.CSLBasePlugin;
import io.superloader.plugin.js.JSPlugin;
import org.bukkit.event.Event;

public class JSListener implements CSLBaseListener{
    JSPlugin plugin;
    Object function;
    public JSListener(JSPlugin plugin,Object function){
        this.plugin = plugin;
        this.function = function;
    }
    @Override
    public CSLBasePlugin getPlugin() {
        return plugin;
    }

    @Override
    public void eventHandle(Event event) {

    }
}
