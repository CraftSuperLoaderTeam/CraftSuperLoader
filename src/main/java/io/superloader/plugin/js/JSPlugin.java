package io.superloader.plugin.js;

import io.superloader.Util;
import io.superloader.plugin.CSLBasePlugin;
import io.superloader.plugin.PluginDescription;
import io.superloader.plugin.js.api.global.Config;
import io.superloader.plugin.js.api.global.Console;
import io.superloader.plugin.js.external.JSExternal;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.apache.commons.lang3.Validate;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSPlugin implements CSLBasePlugin {

    public static final Int2ObjectOpenHashMap<JSPlugin> jsPluginIdMap = new Int2ObjectOpenHashMap<>();
    public static final ConcurrentHashMap<String, JSExternal> jsExternalMap = new ConcurrentHashMap<>();
    private Logger log;
    boolean enable = false;
    PluginDescription description;
    Context context;
    File mainJSFile,folder;
    ArrayList<File> loaded_files;
    protected Value jsExports = null;


    public JSPlugin(File folder){
        Validate.notNull(folder,"The folder is null.");
        Validate.isTrue(folder.isDirectory(),"The js plugin file must a folder.");
        this.folder = folder;
        context = Context.newBuilder("js")
                .allowAllAccess(true)
                .allowHostAccess(HostAccess.newBuilder(HostAccess.ALL).targetTypeMapping(Double.class, Float.class, null, Double::floatValue).build())
                .allowHostClassLoading(true)
                .allowHostClassLookup(className -> true)
                .allowIO(true)
                .allowExperimentalOptions(true)
                .option("js.syntax-extensions","true")
                .option("js.esm-eval-returns-exports","true")
                .option("engine.WarnInterpreterOnly","false")
                .option("js.shared-array-buffer", "true")
                .option("js.foreign-object-prototype", "true")
                .option("js.nashorn-compat", "true")
                .option("js.ecmascript-version", "13")
                .build();
        description = new PluginDescription(Util.asString(new File(folder,"plugin.yml")));

        log = Logger.getLogger(description.getName());

        context.getBindings("js").putMember("console",new Console(this));
        context.getBindings("js").putMember("config",new Config(this,folder));

        mainJSFile = new File(folder,description.getMain());
        loaded_files = new ArrayList<>();
    }

    public void load() {
        try {
            for(File f:folder.listFiles()){
                if(f.getName().split("\\.")[1].equals("js")){
                    context.eval(Source.newBuilder("js",f)
                            .name("@" + description.getName() + "/" + f.getName())
                            .mimeType("application/javascript+module")
                            .build());
                    loaded_files.add(f);
                }
            }
            jsExports = context.eval(Source.newBuilder("js", mainJSFile)
                    .name("@" + description.getName() + "/" + mainJSFile.getName())
                    .mimeType("application/javascript+module").build());
        }catch (Exception io){
            log.log(Level.SEVERE,io.getMessage(),io);
        }
    }

    public Logger getLogger() {
        return log;
    }

    public boolean isEnabled() {
        return enable;
    }

    public void onEnable(){
        try {
            Value mainFunc = jsExports.getMember("enable");
            if (mainFunc != null && mainFunc.canExecute()) {
                synchronized (context) {
                    mainFunc.executeVoid();
                }
            }else log.log(Level.WARNING,"Not found function 'enable' in ["+description.getName()+"]");
        }catch (Exception e){
            log.log(Level.SEVERE,e.getMessage(),e);
            return;
        }
        enable = true;
    }

    @Override
    public void onDisable() {
        enable = false;
        try {
            Value value = jsExports.getMember("disable");
            if (value != null &&value.canExecute()) {
                value.execute();
            } else {
                log.log(Level.SEVERE, "Disable js plugin throw exception [" + description.getName() + "].");
                context.close();
                throw new RuntimeException(new NoSuchMethodException("not found 'disable'."));
            }
            context.close();
        }catch (Exception e){
            log.log(Level.SEVERE,e.getMessage(),e);
        }
    }

    @Override
    public PluginDescription getPluginDescription() {
        return description;
    }

    @Override
    public Type getType() {
        return Type.JS;
    }
}
