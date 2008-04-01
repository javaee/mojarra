/*
 * $Id: TestParamBlockingRequestWrapper.java,v 1.3 2002/01/15 02:17:32 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestParamBlockingRequestWrapper.java

package com.sun.faces;

import junit.framework.TestCase;

import junit.framework.*;
import org.apache.cactus.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import javax.faces.Constants;

import com.sun.faces.ParamBlockingRequestWrapper;
import com.sun.faces.util.Util;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

/**
 *
 *  <B>TestParamBlockingRequestWrapper</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestParamBlockingRequestWrapper.java,v 1.3 2002/01/15 02:17:32 rogerk Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestParamBlockingRequestWrapper extends FilterTestCase {
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

    public TestParamBlockingRequestWrapper() {super("TestParamBlockingRequestWrapper");}
    public TestParamBlockingRequestWrapper(String name) {super(name);}

    //
    // Class methods
    //

    //
    // Methods from TestCase
    //

    public void setUp() {
    }

    public void tearDown() {
    }

    //
    // General Methods
    //

    public void beginWrapper(WebRequest theRequest) {
	theRequest.addParameter("one", "two");
	theRequest.addParameter("three", "four");
	theRequest.addParameter("five", "six");
    }

    public void testWrapper() {
        boolean result;
	
	System.out.print("Testing the initial request has params: ");
	result = Util.hasParameters(request);
	assertTrue(result);
	System.out.println(result);
	
	System.out.print("Testing a ParamBlockingRequestWrapper around this request has NO params: ");
	ParamBlockingRequestWrapper wrapped = 
	    new ParamBlockingRequestWrapper(request);
	result = !Util.hasParameters(wrapped);
	assertTrue(result);
	System.out.println(result);

	System.out.print("Testing wrapped request has no params: ");
	result = (null == wrapped.getParameter("one"));
	assertTrue(result);
	System.out.println(result);
	
	System.out.print("Testing wrapped paramNames enum is empty: ");
	Enumeration paramNames = wrapped.getParameterNames();
	result = !paramNames.hasMoreElements();
	assertTrue(result);
	System.out.println(result);
	
	System.out.print("Testing wrapped paramValues has is null: ");
	result = (null == wrapped.getParameterValues("one"));
	assertTrue(result);
	System.out.println(result);
	
	System.out.print("Testing wrapped request map is empty: ");
	Map paramMap = wrapped.getParameterMap();
	Set entrySet = paramMap.entrySet();
	result = 0 == entrySet.size();
	assertTrue(result);
	System.out.println(result);
	
    }

}

