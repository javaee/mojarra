package com.sun.faces.spi;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

import java.util.List;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Factory class for creating <code>ConfigurationResourceProvider</code> instances
 * using the Java services discovery mechanism.
 */
public class ConfigurationResourceProviderFactory {

    private static final String[] EMPTY_ARRAY = new String[0];
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();


    public enum ProviderType {

        /**
         * ConfigurationResourceProvider type for configuration resources
         * that follow the faces-config DTD/Schema.
         */
        FacesConfig(FacesConfigResourceProvider.SERVICES_KEY),

        /**
         * ConfigurationResourceProvider type for configuration resources
         * that follow the Facelet taglib DTD/Schema.
         */
        FaceletConfig(FaceletConfigResourceProvider.SERVICES_KEY);

        String servicesKey;

        ProviderType(String servicesKey) {
            this.servicesKey = servicesKey;
        }
        
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * @param providerType the type of providers that should be discovered and instantiated.
     *
     * @return an array of all <code>ConfigurationResourceProviders discovered that
     *  match the specified <code>ProviderType</code>.
     */
    public static ConfigurationResourceProvider[] createProviders(ProviderType providerType) {

        String[] serviceEntries = getServiceEntries(providerType.servicesKey);
        List<ConfigurationResourceProvider> providers = new ArrayList<ConfigurationResourceProvider>();
        if (serviceEntries.length > 0) {
            for (String serviceEntry : serviceEntries) {
                ConfigurationResourceProvider provider =
                      getProviderFromEntry(serviceEntry);
                if (provider != null) {
                    providers.add(provider);
                }
            }
            return providers.toArray(new ConfigurationResourceProvider[providers.size()]);
        } else {
            return new ConfigurationResourceProvider[0];
        }
        
    }


    // --------------------------------------------------------- Private Methods

     private static ConfigurationResourceProvider getProviderFromEntry(String entry) {

        if (entry == null) {
            return null;
        }

        try {
            Class<?> clazz = Util.loadClass(entry, null);
            ConfigurationResourceProvider provider = (ConfigurationResourceProvider) clazz.newInstance();
            if (!(provider instanceof FaceletConfigResourceProvider)
                  || !(provider instanceof FacesConfigResourceProvider)) {
                throw new IllegalStateException("ConfigurationResourceProvider must be either of type FaceletConfigResourceProvider or FacesConfigResourceProvider.");
            }
            return provider;
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
            return null;
        }

    }


    private static String[] getServiceEntries(String key) {

        List<String> results = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            return EMPTY_ARRAY;
        }

        Enumeration<URL> urls = null;
        String serviceName = "META-INF/services/" + key;
        try {
            urls = loader.getResources(serviceName);
        } catch (IOException ioe) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           ioe.toString(),
                           ioe);
            }
        }

        if (urls != null) {
            InputStream input = null;
            BufferedReader reader = null;
            while (urls.hasMoreElements()) {
                try {
                    if (results == null) {
                        results = new ArrayList<String>();
                    }
                    URL url = urls.nextElement();
                    URLConnection conn = url.openConnection();
                    conn.setUseCaches(false);
                    input = conn.getInputStream();
                    if (input != null) {
                        try {
                            reader =
                                  new BufferedReader(new InputStreamReader(input, "UTF-8"));
                        } catch (Exception e) {
                            reader =
                                  new BufferedReader(new InputStreamReader(input));
                        }
                        for (String line = reader.readLine();
                             line != null;
                             line = reader.readLine()) {
                            results.add(line.trim());
                        }
                    }
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "jsf.spi.provider.cannot_read_service",
                                   new Object[]{ serviceName });
                        LOGGER.log(Level.SEVERE,
                                   e.toString(),
                                   e);
                    }
                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (Exception ignored) {
                        }
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        }

        return ((results != null && !results.isEmpty())
                ? results.toArray(new String[results.size()])
                : EMPTY_ARRAY);

    }

    
}
