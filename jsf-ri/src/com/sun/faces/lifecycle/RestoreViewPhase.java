/*
 * $Id: RestoreViewPhase.java,v 1.4 2003/09/15 22:11:46 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RestoreViewPhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.event.PhaseId;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;

import javax.faces.el.ValueBinding;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.component.UIInput;
import javax.faces.application.ApplicationFactory;
import javax.faces.event.ActionListener;
import javax.faces.application.ViewHandler;

import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Base64;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.sun.faces.util.DebugUtil;
import com.sun.faces.util.Util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: RestoreViewPhase.java,v 1.4 2003/09/15 22:11:46 eburns Exp $
 * 
 */

public class RestoreViewPhase extends Phase {
    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(RestoreViewPhase.class);

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    private ActionListener actionListener = null;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Genericializers    
    //

    public RestoreViewPhase() {

        ApplicationFactory aFactory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        if (aFactory != null) {
            actionListener = aFactory.getApplication().getActionListener();
        }
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    // 
    // Methods from Phase
    //


    public PhaseId getId() {
        return PhaseId.RESTORE_VIEW;
    }

    /**

    * PRECONDITION: the necessary factories have been installed in the
    * ServletContext attr set. <P>

    * POSTCONDITION: The facesContext has been initialized with a tree. 

    */

    public void execute(FacesContext facesContext) throws FacesException
    {
        if (null == facesContext) {
            throw new FacesException(Util.getExceptionMessage(
                     Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }

        // If an app had explicitely set the tree in the context, use that;
        //
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Locale locale = null;
        if (viewRoot != null) {
            locale = facesContext.getExternalContext().getRequestLocale();
            facesContext.setLocale(locale);
            doPerComponentActions(facesContext, viewRoot);
            return;
        }

        // Reconstitute or create the request tree
        Map requestMap = facesContext.getExternalContext().getRequestMap();
        String viewId = (String) 
                   requestMap.get("javax.servlet.include.path_info");
        if (viewId == null) {
            viewId = facesContext.getExternalContext().getRequestPathInfo();
        }
        if (viewId == null) {
            throw new FacesException(Util.getExceptionMessage(
                    Util.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID));
        }
        viewRoot = (Util.getViewHandler(facesContext)).
                restoreView(facesContext, viewId);
        Assert.assert_it(viewRoot != null);
        facesContext.setViewRoot(viewRoot);
        doPerComponentActions(facesContext, viewRoot);
    }    

    /**
      * <p>Do any per-component actions necessary during reconstitute</p>
      */
    protected void doPerComponentActions(FacesContext context, UIComponent uic) {
        Iterator kids = uic.getFacetsAndChildren();
        String componentRef = null;
        while (kids.hasNext()) {
            doPerComponentActions(context, (UIComponent) kids.next());
        }
        if (uic instanceof UIInput) {
            ((UIInput)uic).setValid(true);
        }
      
        // if this component has a componentRef, make sure to populate the
        // ValueBinding for it.
        if (null != (componentRef = uic.getComponentRef())) {
            ValueBinding valueBinding = 
                context.getApplication().getValueBinding(componentRef);
            valueBinding.setValue(context, uic);
        }
    }

    // The testcase for this class is TestRestoreViewPhase.java


} // end of class RestoreViewPhase
