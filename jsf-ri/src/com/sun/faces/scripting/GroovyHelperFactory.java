package com.sun.faces.scripting;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.faces.util.Util;
import com.sun.faces.util.FacesLogger;

/**
 * This class exists to avoid having to have Groovy available at runtime.
 */
public class GroovyHelperFactory {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    private static final String GROOVY_HELPER_IMPL =
          "com.sun.faces.scripting.GroovyHelperImpl";

    public static GroovyHelper createHelper() {
        try {
            if (Util.loadClass("groovy.util.GroovyScriptEngine", GroovyHelperFactory.class) != null) {
                try {
                    Class<?> c =
                          Util.loadClass(GROOVY_HELPER_IMPL, GroovyHelperFactory.class);
                    return (GroovyHelper) c.newInstance();
                } catch (UnsupportedOperationException ignored) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Groovy runtime available, but WEB-INF/groovy directory not present."
                                    + "  Groovy support will not be enabled.");
                    }
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "Groovy support not available",
                                   e);
                    }
                }
            }
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }

}
