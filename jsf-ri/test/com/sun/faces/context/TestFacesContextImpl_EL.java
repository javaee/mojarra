/*
 * $Id: TestFacesContextImpl_EL.java,v 1.2 2002/10/16 22:22:53 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestFacesContextImpl_EL.java

package com.sun.faces.context;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.MessageImpl;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import com.sun.faces.context.FacesContextImpl;
import com.sun.faces.tree.XmlTreeImpl;

import javax.faces.component.UICommand;
import javax.faces.component.UIForm;

import javax.faces.event.FacesEvent;
import javax.faces.event.CommandEvent;
import javax.faces.event.FormEvent;
import javax.faces.tree.Tree;
import javax.faces.FacesException;
import javax.faces.context.ResponseWriter;
import javax.faces.webapp.ServletResponseWriter;
import java.io.PrintWriter;
import javax.faces.context.ResponseStream;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;
import com.sun.faces.RIConstants;
import javax.faces.render.RenderKit;
import java.util.Collections;
import java.util.Locale;
import java.util.Iterator;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.TestBean.InnerBean;
import com.sun.faces.TestBean.Inner2Bean;

import org.apache.cactus.WebRequest;

/**
 *
 *  <B>TestFacesContextImpl_EL</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFacesContextImpl_EL.java,v 1.2 2002/10/16 22:22:53 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestFacesContextImpl_EL extends ServletFacesTestCase
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

//
// Constructors and Initializers    
//

    public TestFacesContextImpl_EL() {super("TestFacesContext");}
    public TestFacesContextImpl_EL(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

public void beginEL(WebRequest theRequest) 
{
    theRequest.addHeader("ELHeader", "ELHeader");
    theRequest.addHeader("multiheader", "1");
    theRequest.addHeader("multiheader", "2");
    theRequest.addParameter("ELParam", "ELParam");
    theRequest.addParameter("multiparam", "one");
    theRequest.addParameter("multiparam", "two");
    theRequest.addCookie("cookie", "monster");
}

public void testEL()
{
    FacesContext facesContext = getFacesContext();
    System.out.println("Testing getModelValue() with model bean in session ");
    TestBean testBean = new TestBean();
    InnerBean newInner, oldInner = new InnerBean();
    testBean.setInner(oldInner);

    //
    // Get tests
    //

    // Simple tests, verify that bracket and dot operators work
    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
    Object result = facesContext.getModelValue("${TestBean.inner}");
    assertTrue(result == oldInner);

    result = facesContext.getModelValue("${TestBean[\"inner\"]}");
    assertTrue(result == oldInner);

    result = facesContext.getModelValue("${TestBean[\"inner\"].customers[1]}");
    assertTrue(result instanceof String);
    assertTrue(result.equals("Jerry"));

    // try out the implicit objects
    result = facesContext.getModelValue("${sessionScope.TestBean.inner}");
    assertTrue(result == oldInner);

    result = facesContext.getModelValue("${header.ELHeader}");
    assertTrue(result.equals("ELHeader"));

    result = facesContext.getModelValue("${param.ELParam}");
    assertTrue(result.equals("ELParam"));

    String multiparam[] = null;
    multiparam = (String[])
	facesContext.getModelValue("${paramValues.multiparam}");
    assertTrue(null != multiparam);
    assertTrue(2 == multiparam.length);
    assertTrue(multiparam[0].equals("one"));
    assertTrue(multiparam[1].equals("two"));

    multiparam = (String[])
	facesContext.getModelValue("${headerValues.multiheader}");
    assertTrue(null != multiparam);

    // PENDING(edburns): due to an apparent bug in cactus, multiple
    // calls to WebRequest.addHeader() still result in all the values
    // being concatenated together into a comma separated String.
    assertTrue(1 == multiparam.length);
    assertTrue(multiparam[0].equals("1,2"));

    result = facesContext.getModelValue("${initParam.saveStateInClient}");
    assertTrue(null != result);
    assertTrue(result.equals("false") || result.equals("true"));

    result = facesContext.getModelValue("${cookie.cookie}");
    assertTrue(null != result);
    assertTrue(result instanceof Cookie);
    assertTrue(((Cookie) result).getValue().equals("monster"));

    //
    // Set tests
    //
    newInner = new InnerBean();
    facesContext.setModelValue("${TestBean.inner}", newInner);
    result = facesContext.getModelValue("${TestBean.inner}");
    assertTrue(result == newInner);
    assertTrue(oldInner != newInner);

    oldInner = newInner;
    newInner = new InnerBean();
    facesContext.setModelValue("${TestBean[\"inner\"]}", newInner);
    result = facesContext.getModelValue("${TestBean[\"inner\"]}");
    assertTrue(result == newInner);
    assertTrue(oldInner != newInner);

    String 
	customer0 = (String)
	facesContext.getModelValue("${TestBean[\"inner\"].customers[0]}"), 
	customer1 = (String) 
	facesContext.getModelValue("${TestBean[\"inner\"].customers[1]}");

    facesContext.setModelValue("${TestBean[\"inner\"].customers[0]}", "Jerry");
    facesContext.setModelValue("${TestBean[\"inner\"].customers[1]}", "Mickey");
    assertTrue(((String)facesContext.getModelValue("${TestBean[\"inner\"].customers[0]}")).equals("Jerry"));
    assertTrue(((String)facesContext.getModelValue("${TestBean[\"inner\"].customers[1]}")).equals("Mickey"));
    assertTrue(facesContext.getModelValue("${TestBean[\"inner\"].customers[0]}") != customer0);
    assertTrue(facesContext.getModelValue("${TestBean[\"inner\"].customers[1]}") != customer1);

    // put in a map to the customers Collection
    Inner2Bean inner2 = new Inner2Bean();
    assertTrue(null == inner2.getNicknames().get("foo"));
    facesContext.setModelValue("${TestBean[\"inner\"].customers[2]}", inner2);
    assertTrue(facesContext.getModelValue("${TestBean[\"inner\"].customers[2]}") == inner2);    
    facesContext.setModelValue("${TestBean[\"inner\"].customers[2].nicknames.foo}", 
			       "bar");
    assertTrue(((String) inner2.getNicknames().get("foo")).equals("bar"));

    // PENDING(edburns): test set with implicit objects.

    
}


} // end of class TestFacesContextImpl_EL
