package com.sun.faces.spi;

import javax.servlet.ServletContext;
import java.net.URL;
import java.util.List;

/**
 * <p>
 * Classes that implement this interface return zero or more
 * <code>URL</code>s which refer to application configuration resources
 * (i.e. documents conforming the faces-config DTDs or Schema).
 * </p>
 */
public interface ConfigurationResourceProvider {

    /**
     * @param context the <code>ServletContext</code> for this
     *  application
     * @return a List zero or more <code>URL</code> instances
     *  representing application configuration resources
     */
    public List<URL> getResources(ServletContext context);
    
}
