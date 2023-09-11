package io.superloader.plugin.python;

import io.superloader.MetaData;
import jep.Interpreter;
import jep.JepConfig;
import jep.MainInterpreter;
import jep.SubInterpreter;
import py4j.GatewayServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PythonService{
    static PythonBaseAPI api = new PythonBaseAPI();
    static GatewayServer server;
    static Interpreter interpreter;
    static File main_file = null;
    static boolean started = false;
    private static final Logger log = Logger.getLogger(PythonService.class.getName());

    public static void launch(File file){
        if(file.exists()){
            if(file.isDirectory()) {
                log.log(Level.INFO, "CraftSuperLoader Python SDK:" + MetaData.PY_SDK_NETWORK);

                for(File p:file.listFiles())
                    if(p.getName().equals("main.py"))
                        main_file = p;
                if(main_file == null){
                    log.log(Level.SEVERE,"Cannot launch python sdk. Because not found main script file.");
                    return;
                }
                MainInterpreter.setJepLibraryPath(file.getPath()+"/site-package/jep/jep.dll");
                JepConfig config = new JepConfig();
                config.addIncludePaths(file.getPath());
                interpreter = new SubInterpreter(config);


                interpreter.eval(getData(main_file));

                interpreter.invoke("main");


                server = new GatewayServer(api, MetaData.py4j_port);
                server.start();
                started = true;
            }else log.log(Level.SEVERE,"Cannot launch python sdk. Because sdk file is not a folder.");
        }else file.mkdirs();
    }

    private static String getData(File file){
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while ((line= reader.readLine())!=null)sb.append(line).append('\n');
        }catch (IOException io){
            log.log(Level.SEVERE,io.getLocalizedMessage(),io);
        }
        return sb.toString();
    }

    public static void stop(){
        log.log(Level.INFO,"Stopping python sdk service...");
        if(started) {
            interpreter.close();
            server.shutdown();
        }
    }
}
