/*
 * $Id: ValidatorTagTestCase.java,v 1.3 2004/01/08 21:21:28 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.mock.MockApplication;
import javax.faces.mock.MockExternalContext;
import javax.faces.mock.MockFacesContext;
import javax.faces.mock.MockHttpServletRequest;
import javax.faces.mock.MockHttpServletResponse;
import javax.faces.mock.MockHttpSession;
import javax.faces.mock.MockJspWriter;
import javax.faces.mock.MockLifecycle;
import javax.faces.mock.MockPageContext;
import javax.faces.mock.MockRenderKit;
import javax.faces.mock.MockRenderKitFactory;
import javax.faces.mock.MockServlet;
import javax.faces.mock.MockServletConfig;
import javax.faces.mock.MockServletContext;


/**
 * <p>Unit tests for <code>ValidatorTag</code>.</p>
 */

public class ValidatorTagTestCase extends TagTestCaseBase {


    // ------------------------------------------------------ Instance Variables


    protected UIComponentTag ctag = null; // Component tag
    protected UIComponentTag rtag = null; // Root tag


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ValidatorTagTestCase(String name) {

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
        ctag = new TestInputTag();
        ctag.setParent(this.rtag);
        ctag.setPageContext(this.pageContext);

        rtag.doStartTag();
        ctag.doStartTag();

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(ValidatorTagTestCase.class));

    }


    /**
     * Clear our root and component tags.
     */
    public void tearDown() throws Exception {

        ctag.doEndTag();
        rtag.doEndTag();

        ctag = null;
        rtag = null;

        super.tearDown();

    }


    // ------------------------------------------------- Individual Test Methods


    // Test literal validator id
    public void testLiteral() throws Exception {

        UIComponent component = ctag.getComponentInstance();
        assertNotNull(component);
        assertEquals(0, ((EditableValueHolder) component).getValidators().length);
        ValidatorTag tag = new ValidatorTag();
        tag.setValidatorId("Length");
        add(tag);
        tag.doStartTag();
        Validator validator = ((EditableValueHolder) component).getValidators()[0];
        assertNotNull(validator);
        assertTrue(validator instanceof LengthValidator);
        tag.doEndTag();
        
    }


    // Test expression validator id
    public void testExpression() throws Exception {

        UIComponent component = ctag.getComponentInstance();
        assertNotNull(component);
        assertEquals(0, ((EditableValueHolder) component).getValidators().length);
        ValidatorTag tag = new ValidatorTag();
        tag.setValidatorId("#{foo}");
        request.setAttribute("foo", "Length");
        add(tag);
        tag.doStartTag();
        Validator validator = ((EditableValueHolder) component).getValidators()[0];
        assertNotNull(validator);
        assertTrue(validator instanceof LengthValidator);
        tag.doEndTag();
        
    }


    // ------------------------------------------------------- Protected Methods


    // Add the specified ValidatorTag to our component tag
    protected void add(ValidatorTag tag) {

        tag.setParent(ctag);
        tag.setPageContext(this.pageContext);

    }


}
