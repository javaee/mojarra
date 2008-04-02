/*
 * $Id: TestInvokeApplicationPhase.java,v 1.27 2006/03/29 22:39:44 rlubke Exp $
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

// TestInvokeApplicationPhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;
import org.apache.cactus.WebRequest;

import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;

/**
 * <B>TestInvokeApplicationPhase</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestInvokeApplicationPhase.java,v 1.27 2006/03/29 22:39:44 rlubke Exp $
 */

public class TestInvokeApplicationPhase extends ServletFacesTestCase {


    public static final String DID_COMMAND = "didCommand";
    public static final String DID_FORM = "didForm";


    // ------------------------------------------------------------ Constructors


    public TestInvokeApplicationPhase() {

        super("TestInvokeApplicationPhase");

    }


    public TestInvokeApplicationPhase(String name) {

        super(name);

    }


    // ---------------------------------------------------------- Public Methods


    public void testInvokeNoOp() {

        UIInput root = new UIInput();
        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.setViewId("default.xul");
        Phase invokeApplicationPhase = new InvokeApplicationPhase();
        getFacesContext().setViewRoot(page);

        invokeApplicationPhase.execute(getFacesContext());
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

    }


    public void testInvokeNormal() {
    }

} // end of class TestInvokeApplicationPhase
