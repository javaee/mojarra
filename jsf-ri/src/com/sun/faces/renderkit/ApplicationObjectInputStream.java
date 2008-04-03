/*
 * $Id: ApplicationObjectInputStream.java,v 1.6 2007/04/27 22:01:00 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, 
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
    
