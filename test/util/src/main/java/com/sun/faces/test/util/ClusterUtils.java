package com.sun.faces.test.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClusterUtils {

    /**
     * No instantiating me :)
     */
    private ClusterUtils() {
    }

    /**
     * A utility method that gives you an array of String that contain the base
     * URLs for the given test scenario.
     * 
     * @return the base URLs.
     */
    public static String[] getBaseUrls() {
        List<String> result = new ArrayList<String>();
        result.add(System.getProperty("integration.url"));
        for (int i = 1; i < 10; i++) {
            if (System.getProperty("integration.url" + i) != null
                    && !System.getProperty("integration.url" + i).trim().equals("")) {
                result.add(System.getProperty("integration.url" + i));
            }
        }
        return result.toArray(new String[0]);
    }
    
    /**
     * A utility method that scrambles the order of the base URLs.
     * 
     * @return the randomized base URLs.
     */
    public static String[] getRandomizedBaseUrls() {
        List<String> urls = Arrays.asList(getBaseUrls());
        Collections.shuffle(urls);
        return urls.toArray(new String[0]);
    }
}
