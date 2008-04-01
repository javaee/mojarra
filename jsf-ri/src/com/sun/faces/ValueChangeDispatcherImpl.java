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
import javax.faces.ObjectTable;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.WTextEntry;
import javax.faces.ModelAccessor;

/**
 * A class which implements the dispatching of value-change events
 * to appropriate target value-change listener objects.  
 *
 * @version $Id: ValueChangeDispatcherImpl.java,v 1.2 2001/12/10 18:18:00 visvan Exp $
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

        ObjectTable ot = ObjectTable.getInstance();
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
            WTextEntry c = (WTextEntry) ot.get(request, srcName);
            if ( c != null ) {
                c.setText(rc, (String)value_event.getNewValue());
            }
        } else {
            ModelAccessor.setModelObject(rc, modelRef, new_value);
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
            ObjectTable ot ) { 

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
