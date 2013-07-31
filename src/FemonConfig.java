/*
 * FemonConfig.java
 *
 * Created on December 1, 2006, 2:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author VisokayA
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.awt.*;
import javax.swing.*;
import java.nio.channels.SocketChannel;
import java.nio.charset.*;
import java.nio.*;

public class FemonConfig {
    
    public static String sProdReaders;
    public static String sQAReaders;
    public static String sDevReaders;
    public static String sFontPref;
    public static String sFontSize;
    private final static Properties cfgProperties = System.getProperties();
    private static String   PROD_READER_NAMES = "Prod_Reader_Names";
    private static String   QA_READER_NAMES = "QA_Reader_Names";
    private static String   DEV_READER_NAMES = "Dev_Reader_Names";
    private static String   FONT_PREFERENCE = "Font_Preference";
    private static String   FONT_SIZE = "Font_Size";
    private static Properties properties;
    
    public static void ReadConfigData()
    throws ConfigException {
        URL url = null;
        String configFile = (String)cfgProperties.get("CONFIG_FILE");
        properties = new Properties();
        if (jmainFrame.bIsApplet){
            String path = FemonApplet.codeBase+jmainFrame.CONFIG_FILE;
            configFile = path;
            
            try {
                url = new URL(path);
                try {
                    InputStream is = url.openStream();
                    properties.load(is);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        } else if (configFile == null){
            configFile = new StringBuffer(System.getProperty("user.dir"))
            .append(File.separator)
            .append("femonproperties.txt").toString();
            File f = new File(configFile);
            try{
                properties.load(new FileInputStream(configFile));
            } catch (java.io.IOException ioe){
                throw new ConfigException("Cannot read femonproperties.  Program exiting.");
            }
        }
        sFontPref = (String)cfgProperties.getProperty(FONT_PREFERENCE);
        if (sFontPref == null){
            sFontPref = (String)properties.getProperty(FONT_PREFERENCE);
        }
        sFontSize = (String)cfgProperties.getProperty(FONT_SIZE);
        if(sFontSize == null)
            sFontSize = (String)properties.getProperty(FONT_SIZE);
        
        sProdReaders = (String)cfgProperties.getProperty(PROD_READER_NAMES);
        if (sProdReaders == null)
            sProdReaders = (String)properties.getProperty(PROD_READER_NAMES);
        if (sProdReaders == null)
            throw new ConfigException("PROD_READER_NAMES value missing from femon.properties.  Program exiting.");
        
        sQAReaders = (String)cfgProperties.getProperty(QA_READER_NAMES);
        if (sQAReaders == null)
            sQAReaders = (String)properties.getProperty(QA_READER_NAMES);
        if (sQAReaders == null)
            throw new ConfigException("QA_READER_NAMES value missing from femon.properties.  Program exiting.");
        
        sDevReaders = (String)cfgProperties.getProperty(DEV_READER_NAMES);
        if (sDevReaders == null)
            sDevReaders = (String)properties.getProperty(DEV_READER_NAMES);
        if (sDevReaders == null)
            throw new ConfigException("DEV_READER_NAMES value missing from femon.properties.  Program exiting.");
    }
    
    public static void saveFont(String font){
        cfgProperties.setProperty(FONT_PREFERENCE, font);
        properties.setProperty(FONT_PREFERENCE, font);
        try{
            String configFile = new StringBuffer(System.getProperty("user.dir"))
            .append(File.separator)
            .append("femonproperties.txt").toString();
            properties.store(new FileOutputStream(configFile), "Now Also Saving Font Pref");
        }catch(IOException e){
            
        }
    }
    
}
