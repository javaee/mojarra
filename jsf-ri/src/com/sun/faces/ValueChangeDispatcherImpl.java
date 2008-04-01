/*
 * $Id: ValueChangeDispatcherImpl.java,v 1.8 2002/01/12 01:41:17 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.RequestDispatcher;
import javax.faces.ValueChangeDispatcher;
import java.util.EventObject;
import javax.faces.ValueChangeEvent;
import javax.faces.ValueChangeListener;
import java.util.Vector;
import javax.faces.Constants;
import javax.faces.ObjectManager;
import javax.faces.ObjectAccessor;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.UITextEntry;
import javax.faces.UISelectOne;
import javax.faces.UISelectBoolean;
import javax.faces.UIComponent;


/**
 * A class which implements the dispatching of value-change events
 * to appropriate target value-change listener objects.  
 *
 * @version $Id: ValueChangeDispatcherImpl.java,v 1.8 2002/01/12 01:41:17 edburns Exp $
 * @author Jayashri Visvanathan
 */
public class ValueChangeDispatcherImpl extends ValueChangeDispatcher {

    public ValueChangeDispatcherImpl () {
    }

    public void dispatch(ServletRequest request, ServletResponse response,
			 EventObject event) throws IOException, FacesException {

        ParameterCheck.nonNull(request);
        ParameterCheck.nonNull(response);
        ParameterCheck.nonNull(event);

        ObjectManager ot = ObjectManager.getInstance();
        Assert.assert_it( ot != null );
       
        RenderContext rc = (RenderContext)ot.get(request,
                Constants.REF_RENDERCONTEXT);
        Assert.assert_it( rc != null );
 
        ValueChangeEvent value_event = null;
        if ( event instanceof ValueChangeEvent)  {
            value_event = (ValueChangeEvent) event;
        } else {
            throw new FacesException("Invalid event type. " + 
                    "Expected ValueChangeEvent");
        }

        String new_value = (String) value_event.getNewValue();
        String srcName = value_event.getSourceName();
        String modelRef = value_event.getModelReference();

        if ( modelRef == null ) {
	    UIComponent c = (UIComponent) ot.get(request, srcName);
	    if (c instanceof UITextEntry) {
		UITextEntry te = (UITextEntry) c;
		te.setText(rc, (String)value_event.getNewValue());
	    } else if (c instanceof UISelectOne) {
		UISelectOne so = (UISelectOne) c;
		so.setSelectedValue(rc, value_event.getNewValue());
	    } else if (c instanceof UISelectBoolean) {
                UISelectBoolean se = (UISelectBoolean) c;
                boolean state = (Boolean.valueOf(new_value)).booleanValue();
                se.setSelected(rc, state);
            }	
        } else {
            rc.getObjectAccessor().setObject(rc.getRequest(), modelRef, 
					     new_value);
        }
        // components can have listeners without model
        dispatchListeners(request, value_event,ot);

    }
   
    /**
     * Dispatches all the listeners interested in ValueChangeEvent 
     *
     * @param request client information
     * @param e       event object
     * @param ot      Object pool
     */ 
    public void dispatchListeners(ServletRequest request,ValueChangeEvent e, 
            ObjectManager ot ) { 

        String srcName = e.getSourceName();
        String lis_name = srcName + Constants.REF_VALUECHANGELISTENERS;
        Vector lis_list = (Vector) ot.get(request, lis_name);
        if ( lis_list != null && lis_list.size() > 0 ) {
            for ( int i = 0; i < lis_list.size(); ++i) {
                String lis_ref_name = (String) lis_list.elementAt(i);
                // tags could have put in null listeners.
                if ( lis_ref_name != null ) {
                    ValueChangeListener listener = (ValueChangeListener) 
                        ot.get(request, lis_ref_name);
                    Assert.assert_it(listener != null );
                    listener.handleValueChange(e);
                }
            }    
        }    
    }
}
