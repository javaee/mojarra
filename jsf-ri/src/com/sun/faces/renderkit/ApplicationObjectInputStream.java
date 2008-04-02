/*
 * $Id: ApplicationObjectInputStream.java,v 1.3 2006/03/29 22:38:35 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.renderkit;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;


/**
 * An ObjectInputStream that can deserialize objects relative to the current
 * application's class loader.  In particular, this class works around
 * deserialization problems when the JSF JARs are shared (i.e. the
 * classloader has no access to application objects).
 */
public class ApplicationObjectInputStream extends ObjectInputStream {

    // ------------------------------------------------------------ Constructors


    public ApplicationObjectInputStream() throws IOException,
          SecurityException {

        super();

    }


    public ApplicationObjectInputStream(InputStream in) throws IOException {

        super(in);

    }

    // ------------------------------------------------------- Protected Methods


    protected Class resolveClass(ObjectStreamClass desc) throws IOException,
          ClassNotFoundException {

        // When the container is about to call code associated with a 
        // particular web application, it sets the context classloader to the 
        // web app class loader. We make use of that here to locate any classes 
        // that the UIComponent may hold references to.  This won't cause a 
        // problem to locate classes in the system class loader because 
        // class loaders can look up the chain and not down the chain. 
        return Class.forName(desc.getName(), true,
                             Thread.currentThread().getContextClassLoader());

    }

} 
    
