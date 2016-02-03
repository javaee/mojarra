/*
 * $Id: TestComponent.java,v 1.3 2007/04/27 22:02:15 ofung Exp $
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
