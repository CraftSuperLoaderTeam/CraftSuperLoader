package io.superloader.command;

import io.superloader.plugin.PluginDescription;
import io.superloader.plugin.js.JSPlugin;
import io.superloader.plugin.js.JSPluginManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class JSCommand extends Command {
    public JSCommand(String name) {
        super(name);
        this.description = "JS script plugin manage command.";
        this.usageMessage = "/js [help|list|load|disable|info]";
        this.setPermission( "superloader.command.js" );
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        try {
            if (!testPermission(sender)) return true;
            if (args.length == 0) {
                sender.sendMessage("Scripts " + getPluginList());
            } else {
                switch (args[0]) {
                    case "load":
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "Usage: /js load <file_name>");
                            return false;
                        }
                        JSPlugin plugin = JSPluginManager.getPlugin(args[1]);
                        if (plugin == null) {
                            sender.sendMessage(ChatColor.RED + "Not found script.");
                            break;
                        }
                        if (plugin.isEnabled()) {
                            sender.sendMessage(ChatColor.GOLD + "This script is enabled!");
                            break;
                        }
                        JSPluginManager.enablePlugin(plugin);
                        break;
                    case "disable":
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "Usage: /js disable <plugin_name>");
                            return false;
                        }
                        JSPlugin plugin_d = JSPluginManager.getPlugin(args[1]);
                        if (plugin_d == null) {
                            sender.sendMessage(ChatColor.RED + "Not found script.");
                            break;
                        }
                        if (!plugin_d.isEnabled()) {
                            sender.sendMessage(ChatColor.GOLD + "This script is disabled!");
                            break;
                        }
                        JSPluginManager.disablePlugin(plugin_d);
                        break;
                    case "list":
                        sender.sendMessage("Scripts " + getPluginList());
                        break;
                    case "help":
                        sender.sendMessage(ChatColor.DARK_AQUA+"[CSL-JS-CommandHelp]:\n" +
                                "/js help -Get command help info.\n" +
                                "/js list -List all loaded plugins.\n" +
                                "/js load <file_name> -Load a dir to js plugin.\n" +
                                "/js disable <plugin_name> -Disable a enabled js plugin.\n" +
                                "/js info <plugin_name> -List a js plugin all info.");
                        break;
                    case "info":
                        if (args.length < 2) {
                            sender.sendMessage(ChatColor.RED + "Usage: /js info <plugin_name>");
                            return false;
                        }
                        JSPlugin plugin_dd = JSPluginManager.getPlugin(args[1]);
                        if (plugin_dd == null) {
                            sender.sendMessage(ChatColor.RED + "Not found script.");
                            break;
                        }
                        if (!plugin_dd.isEnabled()) {
                            sender.sendMessage(ChatColor.GOLD + "This script is disabled!");
                            break;
                        }

                        PluginDescription description1 = plugin_dd.getPluginDescription();
                        sender.sendMessage(ChatColor.GOLD+"["+description1.getName()+"]: MainScript<"+description1.getMain()+">" +
                                "\nAuthors: "+description1.getAuthors() +
                                "\nVersion: "+description1.getVersion() +
                                "\nWebside: "+description1.getWebsite() +
                                "\nDescription: "+description1.getDescription());
                        break;
                    default:
                        sender.sendMessage("Usage: /js [help|list|load|disable|info]");
                        break;
                }
            }
        }catch (Exception e){
            Bukkit.getLogger().warning(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return false;
    }
    private String getPluginList() {
        StringBuilder pluginList = new StringBuilder();

        for (JSPlugin plugin : JSPluginManager.getPlugins()) {
            if (pluginList.length() > 0) {
                pluginList.append(ChatColor.WHITE);
                pluginList.append(", ");
            }

            pluginList.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED);
            pluginList.append(plugin.getPluginDescription().getName());
        }

        return "(" + JSPluginManager.getPlugins().size() + "): " + pluginList.toString();
    }
}
