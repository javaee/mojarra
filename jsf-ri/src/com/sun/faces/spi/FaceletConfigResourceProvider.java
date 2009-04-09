package com.sun.faces.spi;

/**
 * <p> Classes that implement this interface return zero or more
 * <code>URL</code>s which refer to application configuration resources (i.e.
 * documents conforming the Facelet taglib DTD or Schema). </p>
 *
 * <p>
 * Implementations of this interface are made known to the runtime using
 * service discovery.
 * </p>
 *
 * <p>For example:</p>
 *
 * <pre>
 *     META-INF/services/com.sun.faces.spi.FaceletConfigResourceProvider
 * </pre>
 *
 * <p>
 * The file, <code>com.sun.faces.spi.FaceletConfigResourceProvider</code>,
 * contains a single line which represents the fully qualified class name
 * of the concrete <code>FacesConfigResourceProvider</code>.
 * </p>
 */
public interface FaceletConfigResourceProvider {

    public static final String SERVICES_KEY = "com.sun.faces.spi.FaceletConfigResourceProvider";
    
}
