/*
 * $Id: TestGenerateId.java,v 1.2 2002/04/05 19:41:21 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestGenerateId.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import org.apache.cactus.JspTestCase;

import java.io.IOException;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.ObjectManager;
import javax.faces.UIForm;
import javax.faces.FacesContext;
import javax.faces.FacesContextFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.*;

import com.sun.faces.ObjectManagerFactory;
import com.sun.faces.taglib.html_basic.FormTag;

/**
 *
 *  <B>TestGenerateId</B> is a class which tests the unique id
 *     generation used in component Faces JSP tags.
 *
 * @version $Id: TestGenerateId.java,v 1.2 2002/04/05 19:41:21 jvisvanathan Exp $
 *
 * @see setUp 
 * @see tearDown 
 *
 */

public class TestGenerateId extends JspTestCase
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

    private FormTag formTag;
    private ObjectManagerFactory omFactory;
    private FacesContextFactory rcFactory;
    private FacesContext fc = null;

    private FacesTestCase fCase = null;
//
// Constructors and Initializers    
//

    public TestGenerateId(String name) {
        super(name);
        fCase = new subCase(name); 
    }

//
// Class methods
//

//
// General Methods
//
    private class subCase extends FacesTestCase {

        public subCase(String name) {
            super(name);
        }

    }

    public void setUp() {
//uncomment for debugging
//com.sun.faces.util.DebugUtil.waitForDebugger();
        fCase.setUp();

        ObjectManager om = null;
        this.formTag = new FormTag();
/*
        try {
            omFactory = ObjectManagerFactory.newInstance();
            om = omFactory.newObjectManager(this.pageContext.getServletContext());
            rcFactory = FacesContextFactory.newInstance();
            fc = rcFactory.newFacesContext(this.pageContext.getRequest());
            om.put(pageContext.getSession(), Constants.REF_RENDERCONTEXT,
                fc);
        } catch (FacesException e) {
            System.out.println("Exception:"+ e.getMessage());
        }
        Assert.assert_it(null != omFactory);
        Assert.assert_it(null != rcFactory);

        this.pageContext.getServletContext().setAttribute(
            Constants.REF_OBJECTMANAGER, om);
*/
        this.formTag.setPageContext(this.pageContext);
    }

    /**
     *
     */
    public void testNoIdDoStartTag() {

        int result = 0;
        try {
            System.out.println("Executing doStartTag() method...");
            result = this.formTag.doStartTag();
        } catch (JspException e) {
            System.out.println("Jsp Exception:"+e.getMessage());
        }
        assertEquals(Tag.EVAL_BODY_INCLUDE, result);


        // The id should have been generated.
        //
        String id = this.formTag.getId();
        assertTrue(null != id);
        System.out.println("Generated Id:"+id);

        // The id should have been generated and set in
        // the component.
        //
        ObjectManager om = (ObjectManager) this.pageContext.getServletContext().
            getAttribute(Constants.REF_OBJECTMANAGER);
        UIForm uiForm = (UIForm)om.get(this.pageContext.getRequest(), id);
        assertTrue(null != uiForm.getId());
        System.out.println("Id Stored In Component:"+uiForm.getId());

        // Equals check..
        //
        assertEquals(this.formTag.getId(), uiForm.getId());
    }


} // end of class TestGenerateId 
