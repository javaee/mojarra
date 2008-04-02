/*
 * $Id: ApplicationObjectInputStream.java,v 1.1 2005/07/22 16:58:21 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.renderkit;

import com.sun.faces.RIConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Map;
import javax.faces.context.FacesContext;


/**
 * An ObjectInputStream that can deserialize objects relative to the current
 * application's class loader.  In particular, this class works around 
 * deserialization problems when the JSF JARs are shared (i.e. the 
 * classloader has no access to application objects).
 */
public class ApplicationObjectInputStream extends ObjectInputStream {
   
    public ApplicationObjectInputStream() throws IOException, 
            SecurityException {
        super();
    }
    
    public ApplicationObjectInputStream(InputStream in) throws IOException {
        super(in);
    } 

    protected Class resolveClass(ObjectStreamClass desc) throws IOException, 
            ClassNotFoundException {
        // When the container is about to call code associated with a 
        // particular web application, it sets the context classloader to the 
        // web app class loader. We make use of that here to locate any classes 
        // that the UIComponent may hold references to.  This won't cause a 
        // problem to locate classes in the system class loader because 
        // class loaders can look up the chain and not down the chain. 
        return Class.forName(desc.getName(),true, 
                Thread.currentThread().getContextClassLoader());
    }
} 
    
