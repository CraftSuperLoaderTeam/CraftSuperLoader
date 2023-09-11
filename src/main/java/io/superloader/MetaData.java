package io.superloader;

import org.bukkit.craftbukkit.CraftServer;
import org.graalvm.polyglot.Value;

public class MetaData {
    public static final String server_name = "CraftSuperLoader";
    public static final String NMS_VERSION = CraftServer.class.getPackage().getName().substring(23);
    public static final String version = NMS_VERSION+"/v0.0.9";
    public static final String LUA_SCRIPT_ENGINE = "LuaJ-v3.0.1";
    public static final String JS_SCRIPT_ENGINE = "GraalVM-v21.3.5";
    public static final String PY_SDK_NETWORK = "Py4j-v0.10.9.7 + JEP-v4.1.1";
    public static int py4j_port = 25333;
    public static final Value NULL = Value.asValue(null);

    public static String joinString(Value... values){
        StringBuilder sb = new StringBuilder();
        for (Value value : values) {
            sb.append(value.toString());
        }
        return sb.toString();
    }
}
