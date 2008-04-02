
/*
 * $Id: SpecialConverter.java,v 1.1 2006/01/18 15:52:54 rlubke Exp $
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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.systest.model;

import javax.faces.context.*;
import javax.faces.component.*;
import javax.faces.convert.*;
public class SpecialConverter implements Converter {
        public Object getAsObject(FacesContext context,
                                  UIComponent component,
                                  String value)
        throws ConverterException {            
            if (value == null) {
               throw new ConverterException();
            } else {
                return new SpecialBean(value);
            }
        }

        public String getAsString(FacesContext context,
                                  UIComponent component,
                                  Object value)
        throws ConverterException {           
            if (!(value instanceof SpecialBean)) {
                throw new ConverterException();
            } else {
                return ((SpecialBean) value).getString();
            }
        }
    }

