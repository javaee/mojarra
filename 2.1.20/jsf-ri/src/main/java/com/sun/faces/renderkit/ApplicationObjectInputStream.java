/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.renderkit;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashMap;
import java.util.Map;


/**
 * An ObjectInputStream that can deserialize objects relative to the current
 * application's class loader.  In particular, this class works around 
 * deserialization problems when the JSF JARs are shared (i.e. the 
 * classloader has no access to application objects).
 */
public class ApplicationObjectInputStream extends ObjectInputStream {

    // Taken from ObjectInputStream to resolve primitive types
    private static final Map<String,Class<?>> PRIMITIVE_CLASSES =
          new HashMap<String,Class<?>>(9, 1.0F);

    static {
        PRIMITIVE_CLASSES.put("boolean", boolean.class);
        PRIMITIVE_CLASSES.put("byte", byte.class);
        PRIMITIVE_CLASSES.put("char", char.class);
        PRIMITIVE_CLASSES.put("short", short.class);
        PRIMITIVE_CLASSES.put("int", int.class);
        PRIMITIVE_CLASSES.put("long", long.class);
        PRIMITIVE_CLASSES.put("float", float.class);
        PRIMITIVE_CLASSES.put("double", double.class);
        PRIMITIVE_CLASSES.put("void", void.class);
    }
   
    public ApplicationObjectInputStream() throws IOException, 
            SecurityException {
        super();
    }
    
    public ApplicationObjectInputStream(InputStream in) throws IOException {
        super(in);
    } 

    protected Class<?> resolveClass(ObjectStreamClass desc)
    throws IOException, ClassNotFoundException {
        
        // When the container is about to call code associated with a 
        // particular web application, it sets the context classloader to the 
        // web app class loader. We make use of that here to locate any classes 
        // that the UIComponent may hold references to.  This won't cause a 
        // problem to locate classes in the system class loader because 
        // class loaders can look up the chain and not down the chain.
        String name = desc.getName();
        try {
            return Class.forName(name,
                                 true,
                                 Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException cnfe) {
            Class<?> c = PRIMITIVE_CLASSES.get(name);
            if (c != null) {
                return c;
            }
            throw cnfe;
        }

    }
} 
    
