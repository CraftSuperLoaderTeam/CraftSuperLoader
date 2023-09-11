package io.superloader.plugin.js;

import io.superloader.MetaData;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSPluginManager {
    static ArrayList<JSPlugin> plugins = new ArrayList<>();
    private static final Logger log = Logger.getLogger(JSPluginManager.class.getName());

    public static void loadJSScripts(File file){
        log.log(Level.INFO,"CraftSuperLoader js script engine: "+ MetaData.JS_SCRIPT_ENGINE);
        if(file.exists()){
            if(file.isDirectory()){
                for(File plu:file.listFiles()){
                    if(!plu.isDirectory())continue;
                    try {
                        JSPlugin plugin = new JSPlugin(plu);
                        plugin.load();
                        plugins.add(plugin);
                    }catch (Exception e){
                        log.log(Level.SEVERE,e.getLocalizedMessage(),e);
                    }
                }
            }else log.log(Level.WARNING,"Cannot load js script. Because the 'jsscripts' is not a folder");
        }else file.mkdirs();
    }

    public static ArrayList<JSPlugin> getPlugins() {
        return plugins;
    }

    public static JSPlugin getPlugin(String name){
        for(JSPlugin plugin:plugins){
            if(name.equals(plugin.description.getName())){
                return plugin;
            }
        }
        return null;
    }

    public static void disablePlugin(JSPlugin plugin){
        plugin.onDisable();
        plugins.remove(plugin);
    }

    public static void enablePlugin(JSPlugin plugin){
        File f = plugin.folder;
        plugins.remove(plugin);
        JSPlugin p = new JSPlugin(f);
        plugins.add(p);
        p.load();
        p.onEnable();
    }

    public static void enableJSScripts(){
        for(JSPlugin plugin:plugins){
            try {
                if (!plugin.enable) plugin.onEnable();
            }catch (Exception e){
                log.log(Level.SEVERE,e.getLocalizedMessage(),e);
            }
        }
    }

    public static void disableJSScripts(){
        for(JSPlugin plugin:plugins){
            if(plugin.enable) plugin.onDisable();
        }
    }
}
