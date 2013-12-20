package org.argrr.extractor.utils;

/*
 * #%L
 * ARGRR-utils
 * %%
 * Copyright (C) 2013 I3S
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author cdesclaux <christophe@desclaux.me>
 */
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
