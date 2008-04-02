/*
 * $Id: PropertyResolverTestImpl.java,v 1.8 2006/03/29 22:39:34 rlubke Exp $
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

package com.sun.faces.application;

import com.sun.faces.TestPropertyResolver;
import javax.faces.el.PropertyResolver;

public class PropertyResolverTestImpl extends TestPropertyResolver{


    PropertyResolver root = null;


    // ------------------------------------------------------------ Constructors


    public PropertyResolverTestImpl(PropertyResolver root) {

       super(root);
       this.root = root;

    }


    // ---------------------------------------------------------- Public Methods


     public Object getValue(Object base, Object property) {

        if (property.equals("customPRTest2")) {
            return "PropertyResolverTestImpl";
        }
        return root.getValue(base, property);

    }

}
