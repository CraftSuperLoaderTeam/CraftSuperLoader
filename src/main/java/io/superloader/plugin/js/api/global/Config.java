package io.superloader.plugin.js.api.global;

import io.superloader.MetaData;
import io.superloader.plugin.js.JSPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.graalvm.polyglot.proxy.ProxyObject;

import java.io.*;
import java.util.List;
import java.util.logging.Level;

public class Config implements ProxyObject {
    JSPlugin plugin;
    File configFile;
    FileConfiguration config;
    ProxyExecutable get, set, save;


    public Config(JSPlugin plugin, File folder){
        this.plugin = plugin;
        try {
            configFile = new File(folder, "config.yml");
            if (!configFile.exists()) configFile.createNewFile();
            config = YamlConfiguration.loadConfiguration(configFile);
            init();
        }catch (IOException io){
            plugin.getLogger().log(Level.SEVERE,io.getLocalizedMessage(),io);
        }
    }

    private void init() {
        get = arguments -> {
            Value o = Value.asValue(config.get(MetaData.joinString(arguments)));
            if(o == null) return MetaData.NULL;
            else return o;
        };

        set = arguments -> {
            if(arguments.length  < 2) throw new IllegalArgumentException("call function 'config.set(string,object)'.");
            if(arguments[0].isString()){
                String path = arguments[0].asString();
                Value obj = arguments[1];

                if(obj.isNumber()||obj.isString()) config.set(path,obj.asString());
                else {
                    List<?> l = obj.as(List.class);
                    config.set(path,l);
                }
            }else throw new IllegalArgumentException("call function 'config.set(string,object)'.");
            return MetaData.NULL;
        };

        save = arguments -> {
            try {
                config.save(configFile);
                return MetaData.NULL;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public Object getMember(String key) {
        switch (key) {
            case "get":
                return get;
            case "set":
                return set;
            case "save":
                return save;
            default:
                return null;
        }
    }

    @Override
    public Object getMemberKeys() {
        return new ProxyArray() {
            @Override
            public Object get(long index) {
                switch ((int) index) {
                    case 0:
                        return get;
                    case 1:
                        return set;
                    case 2:
                        return save;
                    default:
                        return null;
                }
            }

            @Override
            public void set(long index, Value value) {

            }

            @Override
            public long getSize() {
                return 4;
            }
        };
    }

    @Override
    public boolean hasMember(String key) {
        switch (key) {
            case "get":
            case "set":
            case "save":
                return true;
            default:
                return false;
        }
    }

    @Override
    public void putMember(String key, Value value) {

    }
}
