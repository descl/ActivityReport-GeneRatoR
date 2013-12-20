package org.argrr.extractor.utils;


import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {
    private static Configuration configFile = null;
    private static InputStream jsonDrive = null;
    
    public static Configuration getConfigFile(){
        if(configFile == null){
            try {
                java.net.URL url = Config.class.getClassLoader().getResource("argrr.properties");
                configFile = new PropertiesConfiguration(url);
            } catch (ConfigurationException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return configFile;
    }
    
    public static String getVar(String var){
        return getConfigFile().getString(var);
    }
    
    public static String[] getArrayVar(String var){
        return getConfigFile().getStringArray(var);
    }
    
    public static InputStream getDriveApi(){
        if(jsonDrive == null){
            jsonDrive = Config.class.getClassLoader().getResourceAsStream("client_secret.json");
        }
        return jsonDrive;
    }
    
}
