package io.superloader.plugin.lua.api;

import io.superloader.event.BasicEventHandle;
import io.superloader.plugin.lua.LuaPlugin;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class ScriptSupportEvent extends TwoArgFunction {
    LuaPlugin plugin;
    public ScriptSupportEvent(LuaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue value = LuaValue.tableOf();

        value.set("registerEvent",new registerEvent());

        env.set("ScriptSupportEvent",value);
        env.get("package").get("loaded").set("ScriptSupportEvent",value);
        return value;
    }

    class registerEvent extends TwoArgFunction{
        @Override
        public LuaValue call(LuaValue luaValue, LuaValue luaValue1) {
            if(luaValue.isfunction()){
                if(luaValue1.isstring()){
                    BasicEventHandle.addLuaScript(plugin,luaValue);
                }else throw new IllegalArgumentException("ScriptSupportEvent:registerEvent the value is not string.");
            }else throw new IllegalArgumentException("ScriptSupportEvent:registerEvent the value is not function.");
            return LuaValue.NIL;
        }
    }


}
