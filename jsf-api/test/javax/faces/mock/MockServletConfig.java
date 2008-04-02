/*
 * $Id: MockServletConfig.java,v 1.2 2004/02/04 23:39:14 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;


// Mock Object for ServletConfig (version 2.3)
public class MockServletConfig implements ServletConfig {


    public MockServletConfig() {
    }


    public MockServletConfig(ServletContext context) {
        setServletContext(context);
    }


    private Hashtable parameters = new Hashtable();
    private ServletContext context;


    // --------------------------------------------------------- Public Methods


    public void addInitParameter(String name, String value) {
        parameters.put(name, value);
    }


    public void setServletContext(ServletContext context) {
        this.context = context;
    }


    // -------------------------------------------------- ServletConfig Methods


    public String getInitParameter(String name) {
        return ((String) parameters.get(name));
    }


    public Enumeration getInitParameterNames() {
        return (parameters.keys());
    }


    public ServletContext getServletContext() {
        return (this.context);
    }


    public String getServletName() {
        return ("MockServlet");
    }


}
