package io.superloader.plugin;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

public class PluginDescription {

    private String name;
    private String main;
    private List<String> depend = new ArrayList<>();
    private List<String> softDepend = new ArrayList<>();
    private List<String> loadBefore = new ArrayList<>();
    private String version;
    private String description;
    private final List<String> authors = new ArrayList<>();
    private String website = "No have webside";

    public PluginDescription(Map<String, Object> yamlMap) {
        this.loadMap(yamlMap);
    }

    public PluginDescription(String yamlString) {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(dumperOptions);
        this.loadMap(yaml.loadAs(yamlString, LinkedHashMap.class));
    }

    private void loadMap(Map<String, Object> plugin){
        this.name = ((String) plugin.get("name")).replaceAll("[^A-Za-z0-9 _.-]", "");
        if (this.name.isEmpty()) {
            throw new IllegalArgumentException("Invalid PluginDescription name");
        }
        this.name = this.name.replace(" ", "_");
        this.version = String.valueOf(plugin.get("version"));

        if(version == null) version = "0.0.1";

        this.main = (String) plugin.get("main");

        if (this.name.equals("CraftSuperLoader")) {
            throw new IllegalArgumentException("Invalid PluginDescription main, cannot start within the CraftSuperLoader");
        }

        if (plugin.containsKey("website")) {
            this.website = (String) plugin.get("website");
        }

        if (plugin.containsKey("description")) {
            this.description = (String) plugin.get("description");
        }


        if (plugin.containsKey("author")) {
            this.authors.add((String) plugin.get("author"));
        }

        if (plugin.containsKey("authors")) {
            this.authors.addAll((Collection<? extends String>) plugin.get("authors"));
        }

        if(description == null) description = "A CSL JS script plugin.";
    }

    public String getFullName() {
        return this.name + " v" + this.version;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getDepend() {
        return depend;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getLoadBefore() {
        return loadBefore;
    }

    public String getMain() {
        return main;
    }

    public String getName() {
        return name;
    }

    public List<String> getSoftDepend() {
        return softDepend;
    }

    public String getVersion() {
        return version;
    }

    public String getWebsite() {
        return website;
    }
}
