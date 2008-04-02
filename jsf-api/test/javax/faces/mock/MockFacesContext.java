/*
 * $Id: MockFacesContext.java,v 1.6 2003/02/20 22:46:58 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ApplicationEvent;
import javax.faces.event.FacesEvent;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.ViewHandler;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FacesException;
import javax.faces.tree.Tree;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.faces.context.FacesContextImpl;

// Mock Object for FacesContext
public class MockFacesContext extends FacesContextImpl {

    public MockFacesContext() {
        setCurrentInstance(this);
    }

   public void setViewHandler(ViewHandler viewHandler) {
        viewHandler =  viewHandler;
    }    
    
    public void setApplicationHandler(ApplicationHandler applicationHandler) {
        applicationHandler =  applicationHandler;
    }

    // Mock object setter
    public void setServletContext(ServletContext servletContext) {
        servletContext = servletContext;
    }

    // Mock object setter
    public void setServletRequest(ServletRequest servletRequest) {
        servletRequest = servletRequest;
    }

    // Mock object setter
    public void setServletResponse(ServletResponse servletResponse) {
        servletResponse = servletResponse;
    }
}
