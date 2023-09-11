package io.superloader.plugin.lua;

import io.superloader.plugin.CSLBasePlugin;
import io.superloader.plugin.PluginDescription;
import io.superloader.plugin.lua.api.Minecraft;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LuaPlugin implements CSLBasePlugin {
    Globals globals;
    Logger logger;
    String name = "LoadingPlugin", description = "A SuperLoader lua script.", version = "0.0.1";
    boolean enable = false;
    Minecraft minecraft_api;

    public LuaPlugin(File file) {
        logger = Logger.getLogger(name);
        globals = LuaPluginManager.getGlobals(this);
        globals.load(LuaPluginManager.getData(file)).call();
    }

    public Logger getLogger() {
        return logger;
    }

    public String getName() {
        return name;
    }

    @Override
    public PluginDescription getPluginDescription() {
        return null;
    }

    public Type getType(){
        return Type.LUA;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public void onEnable() {
        try {
            minecraft_api = new Minecraft(this);
            LuaPluginManager.invoke(globals, "enable", minecraft_api);
            enable = true;
        } catch (LuaError e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void onDisable() {
        try {
            LuaPluginManager.invoke(globals, "disable", minecraft_api);
            enable = false;
        } catch (LuaError e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void load(Logger logger) {
        /*
        try {
            SuperLoader l = new SuperLoader();
            LuaPluginManager.invoke(globals, "load", l);
            name = l.getName();
            description = l.getDescription();
            version = l.getVersion();
            if (name == null) {
                name = "UnknownName";
            }
            if (description == null) {
                name = "A SuperLoader lua script.";
            }
            if (version == null) {
                name = "0.0.1";
            }
            enable = true;
            this.logger = Logger.getLogger(name);
        } catch (LuaError e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

         */
    }

    public boolean isEnabled() {
        return enable;
    }
}
