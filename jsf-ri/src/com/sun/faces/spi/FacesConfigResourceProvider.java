package com.sun.faces.spi;

/**
 * <p> Classes that implement this interface return zero or more
 * <code>URL</code>s which refer to application configuration resources (i.e.
 * documents conforming the faces-config DTD or Schema). </p>
 *
 * <p>
 * Implementations of this interface are made known to the runtime using
 * service discovery.
 * </p>
 *
 * <p>For example:</p>
 *
 * <pre>
 *     META-INF/services/com.sun.faces.spi.FacesConfigResourceProvider
 * </pre>
 *
 * <p>
 * The file, <code>com.sun.faces.spi.FacesConfigResourceProvider</code>,
 * contains a single line which represents the fully qualified class name
 * of the concrete <code>FacesConfigResourceProvider</code>.
 * </p>
 *
 * <p>
 * The <code>FacesConfigResourceProvider</code> instances that are found
 * will be inserted into a List of existing <code>ConfigurationResourceProviders</code>
 * <em>after</em> those that process <code>faces-config.xml</code> files in <code>META-INF</code>
 * but <em>before</em> those that process <code>faces-config.xml</code> files in the
 * web application.  If the documents returned by this <code>ConfigurationResourceProvider</code>
 * instance require specific ordering semantics, then use the partial or absolute ordering
 * feature provided by the JavaServer Faces 2.0 specification.
 * </p>
 */
public interface FacesConfigResourceProvider extends ConfigurationResourceProvider {

    public static final String SERVICES_KEY = "com.sun.faces.spi.FacesConfigResourceProvider";
    
}
