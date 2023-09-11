package io.superloader.plugin.lua;

import io.superloader.MetaData;
import io.superloader.plugin.lua.api.Minecraft;
import io.superloader.plugin.lua.api.ScriptSupportEvent;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LuaPluginManager {
    private static final Logger log = Logger.getLogger(LuaPluginManager.class.getName());
    private static ArrayList<LuaPlugin> plugins = new ArrayList<>();

    public static ArrayList<LuaPlugin> getPlugins() {
        return plugins;
    }

    public static Object invoke(Globals globals, String func, Object... parameters) {
        if (parameters != null && parameters.length > 0) {
            LuaValue[] values = new LuaValue[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                values[i] = CoerceJavaToLua.coerce(parameters[i]);
            }
            return globals.get(func).call(LuaValue.listOf(values));
        } else {
            return globals.get(func).call();
        }
    }

    protected static Globals getGlobals(LuaPlugin plugin){
        Globals globals = new Globals();
        globals.load(new JseBaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new CoroutineLib());
        globals.load(new JseMathLib());
        globals.load(new JseIoLib());
        globals.load(new JseOsLib());
        globals.load(new LuajavaLib());
        globals.load(new Minecraft(plugin));
        globals.load(new ScriptSupportEvent(plugin));
        LoadState.install(globals);
        LuaC.install(globals);
        return globals;
    }

    public static LuaPlugin getPlugin(String name){
        for(LuaPlugin plugin:plugins){
            if(plugin.name.equals(name))return plugin;
        }
        return null;
    }

    public static void loadLuaScripts(File file){
        log.log(Level.INFO,"CraftSuperLoader lua script engine: "+ MetaData.LUA_SCRIPT_ENGINE);
        if(file.exists()){
            if(file.isDirectory()){
                for(File plu:file.listFiles()){
                    try {
                        LuaPlugin plugin = new LuaPlugin(plu);
                        plugin.load(log);
                        plugins.add(plugin);
                    }catch (Exception e){
                        log.log(Level.SEVERE,e.getLocalizedMessage(),e);
                    }
                }
            }else log.log(Level.WARNING,"Cannot load lua script. Because the 'luascripts' is not a folder");
        }else {
            file.mkdirs();
        }
    }

    public static void enableLuaScripts(){
        for(LuaPlugin plugin:plugins){
            if(!plugin.enable) plugin.onEnable();
        }
    }

    public static void disableLuaScripts(){

    }

    protected static String getData(File file){
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = reader.readLine())!=null)sb.append(line).append('\n');
            return sb.toString();
        }catch (IOException io){
            throw new IllegalArgumentException();
        }
    }
}
