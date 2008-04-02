/*
 * $Id: MockServlet.java,v 1.1 2003/03/13 06:06:18 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * <p>Mock <strong>Servlet</strong> for unit tests.</p>
 */

public class MockServlet implements Servlet {


    public MockServlet() {
    }


    public MockServlet(ServletConfig config) throws ServletException {
        init(config);
    }


    private ServletConfig config;


    public void destroy() {
    }


    public ServletConfig getServletConfig() {
        return (this.config);
    }


    public String getServletInfo() {
        return ("MockServlet");
    }


    public void init(ServletConfig config) throws ServletException {
        this.config = config;
    }



    public void service(ServletRequest request, ServletResponse response)
        throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }


}
