package com.sun.faces.test.util;

/**
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
public class SeleniumUtils {
    
    /**
     * No instantiating me :)
     */
    private SeleniumUtils() {
    }

    /**
     * A utility method that gives you a web driver instance.
     * 
     * @return the base URLs.
     */
    public static Object createWebDriver(String browser) throws Exception {
        Object result = null;

        if ("Firefox".equals(browser)) {
            Class clazz = Class.forName("org.openqa.selenium.firefox.FirefoxDriver");
            result = clazz.newInstance();
        }
        
        if ("IE".equalsIgnoreCase(browser) || "Internet Explorer".equalsIgnoreCase(browser)) {
            Class clazz = Class.forName("org.openqa.selenium.ie.InternetExplorerDriver");
            result = clazz.newInstance();
        }
        
        if (result == null) {
            throw new Exception("Unable to create WebDriver for: " + browser);
        }
        
        return result;
    }
}
