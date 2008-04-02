/*
 * $Id: CarDemoResources.java,v 1.1 2003/06/02 17:04:53 jvisvanathan Exp $
 */
/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package cardemo;

import java.util.Locale;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * CarDemoResources cache's all the resource bundles and the properties
 * file once they have been read. The lifetime of this object is the same
 * as the lifetime of the CarServer since it is created and maintained by
 * CarServer.
 */
public class CarDemoResources extends Object {
    
    private HashMap resourcesCache = null;
    
    public CarDemoResources() {
        super();
        resourcesCache = new HashMap();
    }
    
    /**
     * Returns the bundle for a particular locale. If it has already been
     * read once, it is returned from the cache. If its being loaded the first
     * time, it is cached for future requests.
     */
    public ResourceBundle getBundle(String base, Locale locale) 
            throws MissingResourceException {
        String key = base + "_" + locale.toString();
        ResourceBundle bundle = (ResourceBundle) resourcesCache.get(key);
        if ( bundle == null ) {
            bundle = ResourceBundle.getBundle(base, locale);
            resourcesCache.put(key, bundle);
        }  
        return bundle;
    }   
    
    /**
     * Returns the resource based on the filename. If it has already been
     * read once, it is returned from the cache. If its being loaded the first
     * time, it is cached for future requests. These resource files are not 
     * based on the locale. Currently this method loads the properties file
     * for different packages available.
     */
    public Properties getResource(String filename) throws IOException {
        Properties props = null;
        InputStream stream = null;
        props = (Properties)resourcesCache.get(filename);
        if ( props == null ) {
            stream = this.getClass().getClassLoader().getResourceAsStream(filename);
            if (stream != null) {
                props = new Properties();
                props.load(stream);
                stream.close();
                stream = null;
                resourcesCache.put(filename, props);
            }
        }
        return props;
    }             
}    
	