package io.superloader.plugin;

public interface CSLBasePlugin {
    public PluginDescription getPluginDescription();
    public Type getType();
    public boolean isEnabled();

    void onEnable();
    void onDisable();

    public enum Type{
        LUA,
        PYTHON,
        JS
    }
}
