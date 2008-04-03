/*
 * CatchExceptionServlet.java
 *
 * Created on March 19, 2007, 2:14 PM
 */

package com.sun.faces.systest.model;

import java.io.IOException;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author edburns
 * @version
 */
public class CatchExceptionServlet implements Servlet {
    
    private FacesServlet wrapped = null;
    
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        try {
            wrapped.service(servletRequest, servletResponse);
            
        }
        catch (ServletException e) {
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            httpRequest.setAttribute("exceptionClass", e.getClass().getName());
            httpRequest.setAttribute("exceptionMessage", e.getMessage());
            throw e;
        }
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        if (null == wrapped) {
            wrapped = new FacesServlet();
        }
        wrapped.init(servletConfig);
    }

    public void destroy() {
        wrapped.destroy();
    }

    public String getServletInfo() {
        return wrapped.getServletInfo();
    }

    public ServletConfig getServletConfig() {
        return wrapped.getServletConfig();
    }
    

}
