/*
 * $Id: TestExternalContextImpl.java,v 1.1 2003/03/21 23:26:44 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestExternalContextImpl.java

package com.sun.faces.context;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.MessageImpl;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import com.sun.faces.context.FacesContextImpl;
import com.sun.faces.tree.SimpleTreeImpl;

import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;

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

/**
 *
 *  <B>TestExternalContextImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestExternalContextImpl.java,v 1.1 2003/03/21 23:26:44 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestExternalContextImpl extends ServletFacesTestCase
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

    public TestExternalContextImpl() {super("TestExternalContext");}
    public TestExternalContextImpl(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

//PENDING(rogerk) the unit test for ExternalContext should cast the Object instances 
// to the expected type.  It should test put and get.  It should test the
// UnsupportedOperationException is thrown when expected, etc.

public void testAccessors() {

}


// Unit tests to update and retrieve values from model objects
// are in TestExternalContextImpl_Model.java
} // end of class TestExternalContextImpl
