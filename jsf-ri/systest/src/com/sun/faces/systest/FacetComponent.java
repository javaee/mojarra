/*
 * $Id: FacetComponent.java,v 1.10 2006/03/29 23:03:58 rlubke Exp $
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

package com.sun.faces.systest;


import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;


/**
 * <p>Test <code>UIComponent</code> for sys tests.</p>
 */

public class FacetComponent extends UIOutput {


    public FacetComponent() {
        super();
    }


    public FacetComponent(String id) {
        super();
        setId(id);
    }


    public void encodeEnd(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        UIOutput facet = (UIOutput) getFacet("header");
        if (facet != null) {
            writer.write("[" + facet.getValue() + "] ");
        } else {
            writer.write("[] ");
        }
        writer.write("[");
        writer.write((String) getValue());
        writer.write("] ");
        facet = (UIOutput) getFacet("footer");
        if (facet != null) {
            writer.write("[" + facet.getValue() + "] ");
        } else {
            writer.write("[] ");
        }
    }


}
