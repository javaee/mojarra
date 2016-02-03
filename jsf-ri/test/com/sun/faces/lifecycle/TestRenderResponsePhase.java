/*
 * $Id: TestRenderResponsePhase.java,v 1.84 2007/04/27 22:02:08 ofung Exp $
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

// TestRenderResponsePhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.cactus.JspFacesTestCase;
import com.sun.faces.util.Util;
import org.apache.cactus.JspTestCase;
import org.apache.cactus.WebRequest;

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import java.util.Locale;

/**
 * <B>TestRenderResponsePhase</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderResponsePhase.java,v 1.84 2007/04/27 22:02:08 ofung Exp $
 */

public class TestRenderResponsePhase extends JspFacesTestCase {

//
// Protected Constants
//

    public static final String TEST_URI = "/TestRenderResponsePhase.jsp";


    public String getExpectedOutputFilename() {
        return "RenderResponse_correct";
    }


    public static final String ignore[] = {
    };


    public String[] getLinesToIgnore() {
        return ignore;
    }


    public boolean sendResponseToFile() {
        return true;
    }


//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestRenderResponsePhase() {
        super("TestRenderResponsePhase");
    }


    public TestRenderResponsePhase(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//


    public void beginHtmlBasicRenderKit(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI, null);
    }


    public void testHtmlBasicRenderKit() {


        boolean result = false;
        String value = null;
        LifecycleImpl lifecycle = new LifecycleImpl();
        Phase renderResponse = new RenderResponsePhase();
        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.setId("root");
        page.setViewId(TEST_URI);
        page.setLocale(Locale.US);
        getFacesContext().setViewRoot(page);

        try {
            renderResponse.execute(getFacesContext());
        } catch (FacesException fe) {
            System.out.println(fe.getMessage());
            if (null != fe.getCause()) {
                fe.getCause().printStackTrace();
            } else {
                fe.printStackTrace();
            }
        }
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        assertTrue(verifyExpectedOutput());
    }

} // end of class TestRenderResponsePhase
