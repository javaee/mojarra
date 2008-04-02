/*
 * $Id: TestViewTag.java,v 1.3 2003/11/10 22:18:39 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestViewTag.java

package com.sun.faces.taglib.jsf_core;

import org.apache.cactus.WebRequest;
import org.apache.cactus.JspTestCase;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIViewRoot;
import javax.faces.validator.Validator;

import com.sun.faces.lifecycle.Phase;
import com.sun.faces.lifecycle.RenderResponsePhase;
import com.sun.faces.lifecycle.LifecycleImpl;
import com.sun.faces.JspFacesTestCase;
import com.sun.faces.FileOutputResponseWrapper;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import com.sun.faces.CompareFiles;

import com.sun.faces.TestBean;

import java.io.IOException;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.PageContext;

/**
 *
 *  <B>TestViewTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestViewTag.java,v 1.3 2003/11/10 22:18:39 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestViewTag extends JspFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI = "/TestViewTag.jsp";
public static final String TEST_URI2 = "/TestViewTag2.jsp";

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

    public TestViewTag() {
	super("TestViewTag");
    }

    public TestViewTag(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//

public void beginViewTag(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI, null);
}

public void testViewTag()
{    
    boolean result = false;    
    String value = null;
    Locale expectedLocale = new Locale("ps", "PS", "Traditional");
    Phase renderResponse = new RenderResponsePhase();    
    UIViewRoot page = new UIViewRoot();
    page.setId("root");
    page.setViewId(TEST_URI);
    page.setLocale(Locale.CANADA_FRENCH);
    getFacesContext().setViewRoot(page);

    Config.set((ServletRequest) 
	       getFacesContext().getExternalContext().getRequest(), 
	       Config.FMT_LOCALE, Locale.CANADA_FRENCH);    
    
    try {
	renderResponse.execute(getFacesContext());
    }
    catch (FacesException fe) {
	System.out.println(fe.getMessage());
	if (null != fe.getCause()) {
	    fe.getCause().printStackTrace();
	}
	else {
	    fe.printStackTrace();
	}
    }
    assertEquals("locale not as expected", expectedLocale, page.getLocale());
    assertEquals("locale not as expected", expectedLocale, 
		 Config.get((ServletRequest) 
			    getFacesContext().getExternalContext().
			    getRequest(), 
			    Config.FMT_LOCALE));
}

public void beginViewTagVB(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI2, null);
}

public void testViewTagVB()
{    
    boolean result = false;    
    String value = null;
    Locale expectedLocale = new Locale("ps", "PS", "Traditional");
    request.setAttribute("locale", expectedLocale);
    Phase renderResponse = new RenderResponsePhase();    
    UIViewRoot page = new UIViewRoot();
    page.setId("root");
    page.setViewId(TEST_URI2);
    getFacesContext().setViewRoot(page);
    
    try {
	renderResponse.execute(getFacesContext());
    }
    catch (FacesException fe) {
	System.out.println(fe.getMessage());
	if (null != fe.getCause()) {
	    fe.getCause().printStackTrace();
	}
	else {
	    fe.printStackTrace();
	}
    }
    assertEquals("locale not as expected", expectedLocale, page.getLocale());
}

} // end of class TestViewTag
