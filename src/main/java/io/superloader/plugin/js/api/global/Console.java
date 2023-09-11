package io.superloader.plugin.js.api.global;

import io.superloader.MetaData;
import io.superloader.plugin.js.JSPlugin;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.graalvm.polyglot.proxy.ProxyObject;

import java.util.logging.Level;

public class Console implements ProxyObject {
    JSPlugin plugin;
    ProxyExecutable log,warn,error;
    public Console(JSPlugin plugin){
        this.plugin = plugin;
        log = arguments -> {
            plugin.getLogger().log(Level.INFO,"["+plugin.getPluginDescription().getName()+"] "+MetaData.joinString(arguments));
            return MetaData.NULL;
        };
        warn = arguments -> {
            plugin.getLogger().log(Level.WARNING,"["+plugin.getPluginDescription().getName()+"] "+MetaData.joinString(arguments));
            return MetaData.NULL;
        };
        error = arguments -> {
            plugin.getLogger().log(Level.SEVERE,"["+plugin.getPluginDescription().getName()+"] "+MetaData.joinString(arguments));
            return MetaData.NULL;
        };
    }

    @Override
    public Object getMember(String key) {
        switch (key){
            case "info":
                case "log":
                    return log;
            case "warn":
                return warn;
            case "error":
                return error;
            default:
                return null;
        }
    }

    @Override
    public Object getMemberKeys() {
        return new ProxyArray() {
            @Override
            public Object get(long index) {
                switch ((int)index){
                    case 0:
                        return log;
                    case 1:
                        return warn;
                    case 2:
                        return error;
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
        switch (key){
            case "info":
            case "log":
            case "warn":
            case "error":
                return true;
            default:
                return false;
        }
    }

    @Override
    public void putMember(String key, Value value) {

    }
}
