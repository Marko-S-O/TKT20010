package com.orasaari;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/** 
 * A static utility class to implement util methods and store global constants.
 */
class MapUtils {

    static final double SQRT2 = Math.sqrt(2.0);
    static final double SQRT2_1 = Math.sqrt(2.0) - 1;

    // Directories of the used files. If deploying the code to another environment, these need to be adjusted accordingly.
    static String MAP_DIRECTORY = "c:/users/ext/TKT20010/pathfinder-app/data/street-map/";
    static String SCENARIO_DIRECTORY = "c:/users/ext/TKT20010/pathfinder-app/data/test/";    

    static {
        Properties props = new Properties();
        try (InputStream in = new FileInputStream("config.properties")) {
            props.load(in);
            System.out.println(props);
            if(props.getProperty("MAP_DIRECTORY") != null) MAP_DIRECTORY = props.getProperty("MAP_DIRECTORY");
            if(props.getProperty("SCENARIO_DIRECTORY") != null) SCENARIO_DIRECTORY = props.getProperty("SCENARIO_DIRECTORY");  
            System.out.println(MAP_DIRECTORY);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
