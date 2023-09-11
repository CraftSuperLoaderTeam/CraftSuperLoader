package io.superloader;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class Util {
    public static BufferedImage getImageResource(String path, Logger logger){
        try{
            String p;
            if (path.startsWith("/")) {
                p = path;
            } else {
                p = "/" + path;
            }
            InputStream stream = Util.class.getResourceAsStream(p);
            if(stream == null)return null;
            return ImageIO.read(stream);
        }catch (IOException io){
            logger.log(Level.WARN,io.getMessage(),io);
            return null;
        }
    }

    public static URL loadURL(String path,Logger log) {
        URL resource = Util.class.getResource(path);
        if (resource == null) {
            log.error("Cannot load resource: "+path);
        }
        return resource;
    }

    public static String load(String path,Logger logger) {
        return loadURL(path,logger).toExternalForm();
    }

    public static String asString(File file){
        Validate.isTrue(file != null,"Not found file.");
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = reader.readLine())!= null) sb.append(line).append('\n');
        }catch (IOException io){
            throw new RuntimeException(io);
        }
        return sb.toString();
    }
}
