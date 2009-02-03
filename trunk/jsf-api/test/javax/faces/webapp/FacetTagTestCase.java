/*
 * $Id: FacetTagTestCase.java,v 1.10 2007/04/27 22:00:17 ofung Exp $
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

package javax.faces.webapp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.sun.faces.mock.MockApplication;
import com.sun.faces.mock.MockExternalContext;
import com.sun.faces.mock.MockFacesContext;
import com.sun.faces.mock.MockHttpServletRequest;
import com.sun.faces.mock.MockHttpServletResponse;
import com.sun.faces.mock.MockHttpSession;
import com.sun.faces.mock.MockJspWriter;
import com.sun.faces.mock.MockLifecycle;
import com.sun.faces.mock.MockPageContext;
import com.sun.faces.mock.MockRenderKit;
import com.sun.faces.mock.MockRenderKitFactory;
import com.sun.faces.mock.MockServlet;
import com.sun.faces.mock.MockServletConfig;
import com.sun.faces.mock.MockServletContext;


/**
 * <p>Unit tests for <code>FacetTag</code>.</p>
 */

public class FacetTagTestCase extends TagTestCaseBase {


    // ------------------------------------------------------ Instance Variables


    protected UIComponentTag ctag = null; // Component tag
    protected FacetTag ftag = null;       // Facet tag
    protected UIComponentTag rtag = null; // Root tag


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public FacetTagTestCase(String name) {

        super(name);

    }


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Set up our root and component tags.
     */
    public void setUp() throws Exception {

        super.setUp();

        rtag = new TestTag("ROOT", "root") {
                protected void setProperties(UIComponent component) {
                }
            };
        rtag.setPageContext(this.pageContext);

        ftag = new FacetTag();
        ftag.setPageContext(this.pageContext);
        ftag.setParent(this.rtag);

        ctag = new TestOutputTag();
        ctag.setPageContext(this.pageContext);
        ctag.setParent(this.ftag);

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(FacetTagTestCase.class));

    }


    /**
     * Clear our root and component tags.
     */
    public void tearDown() throws Exception {

        ctag = null;
        ftag = null;
        rtag = null;

        super.tearDown();

    }


    // ------------------------------------------------- Individual Test Methods


    // Test literal facet name
    public void testLiteral() throws Exception {

        rtag.doStartTag();
        ftag.setName("foo");
        ftag.doStartTag();
        ctag.doStartTag();

        UIComponent component = rtag.getComponentInstance();
        assertNotNull(component);
        UIComponent facet = component.getFacet("foo");
        assertNotNull(facet);
        assertTrue(facet instanceof UIOutput);
        
        ctag.doEndTag();
        ftag.doEndTag();
        rtag.doEndTag();

    }


}
