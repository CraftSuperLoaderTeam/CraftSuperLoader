package io.superloader.event;

import io.superloader.plugin.CSLBasePlugin;
import org.bukkit.event.Event;

public interface CSLBaseListener {
    public CSLBasePlugin getPlugin();
    public void eventHandle(Event event);
}
