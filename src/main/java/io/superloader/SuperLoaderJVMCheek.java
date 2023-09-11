package io.superloader;

import joptsimple.OptionSet;
import net.minecraft.server.MinecraftServer;

public class SuperLoaderJVMCheek {

    public static void main(OptionSet options){
        System.out.println("  _____            __ _    _____                       _                     _           \n" +
                " / ____|          / _| |  / ____|                     | |                   | |          \n" +
                "| |     _ __ __ _| |_| |_| (___  _   _ _ __   ___ _ __| |     ___   __ _  __| | ___ _ __ \n" +
                "| |    | '__/ _` |  _| __|\\___ \\| | | | '_ \\ / _ \\ '__| |    / _ \\ / _` |/ _` |/ _ \\ '__|\n" +
                "| |____| | | (_| | | | |_ ____) | |_| | |_) |  __/ |  | |___| (_) | (_| | (_| |  __/ |   \n" +
                " \\_____|_|  \\__,_|_|  \\__|_____/ \\__,_| .__/ \\___|_|  |______\\___/ \\__,_|\\__,_|\\___|_|   \n" +
                "                                      | |                                                \n" +
                "                                      |_|                                                \n");
        System.out.println("                         [-CraftSuperLoader-"+MetaData.version+"-]");
        bootMessage(getJavaVersion());
        bootMessage("OS Version: "+System.getProperty("os.name")+" "+System.getProperty("os.arch"));
        bootMessage("Loading net.minecraft.server."+MetaData.NMS_VERSION+".MinecraftServer");
        MinecraftServer.main(options);
    }

    public static String getJavaVersion(){
        return "Java " + System.getProperty("java.version") + '(' + System.getProperty("java.vm.name") + ')';
    }

    public static void bootMessage(String message){
        System.out.println("[ServerBoot]: "+message);
    }
}
