/*
 * $Id: TestComponent.java,v 1.2 2005/08/22 22:11:36 ofung Exp $
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


import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import java.io.IOException;


/**
 * <p>Test <code>UIComponent</code> for sys tests.</p>
 */

public class TestComponent extends UIInput {


    public TestComponent() {
        this("test");
    }


    public TestComponent(String componentId) {
        super();
        setId(componentId);
    }


    public String getComponentType() {
        return ("TestComponent");
    }


    // -------------------------------------------------- Trace-Enabled Methods


    public void decode(FacesContext context) {
        trace("d-" + getId());
        super.decode(context);
    }


    public void encodeBegin(FacesContext context) throws IOException {
        trace("eB-" + getId());
        super.encodeBegin(context);
    }


    public void encodeChildren(FacesContext context) throws IOException {
        trace("eC-" + getId());
        super.encodeChildren(context);
    }


    public void encodeEnd(FacesContext context) throws IOException {
        trace("eE-" + getId());
        super.encodeEnd(context);
    }


    public void updateModel(FacesContext context) {
        trace("u-" + getId());
        super.updateModel(context);
    }


    public void validate(FacesContext context) {
        trace("v-" + getId());
        super.validate(context);
    }


    public void processDecodes(FacesContext context) {
        trace("pD-" + getId());
        super.processDecodes(context);
    }


    public void processValidators(FacesContext context) {
        trace("pV-" + getId());
        super.processValidators(context);
    }


    public void processUpdates(FacesContext context) {
        trace("pU-" + getId());
        super.processUpdates(context);
    }


    // --------------------------------------------------- Static Trace Methods


    // Accumulated trace log
    private static StringBuffer trace = new StringBuffer();


    // Append to the current trace log (or clear if null)
    public static void trace(String text) {
        if (text == null) {
            trace.setLength(0);
        } else {
            trace.append('/');
            trace.append(text);
        }
    }


    // Retrieve the current trace log
    public static String trace() {
        return (trace.toString());
    }


}
