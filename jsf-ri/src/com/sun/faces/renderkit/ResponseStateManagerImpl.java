/*
 * $Id: ResponseStateManagerImpl.java,v 1.3 2003/09/05 18:56:57 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.renderkit;

import javax.faces.render.ResponseStateManager;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.StringReader;
import javax.faces.FacesException;

import java.io.IOException;
import java.io.Writer;
import java.io.Reader;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Base64;
import com.sun.faces.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.mozilla.util.Assert;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 *  <B>RenderKitImpl</B> is a class ...
 *
 */

public class ResponseStateManagerImpl extends ResponseStateManager {

    //
    // Protected Constants
    //
    protected static Log log = 
        LogFactory.getLog(ResponseStateManagerImpl.class);
    private static final String FACES_VIEW_STATE = 
            "com.sun.faces.FACES_VIEW_STATE";
    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    //
    // Ivars used during actual client lifetime
    //

    // Relationship Instance Variables

    
    //
    // Constructors and Initializers    
    //

    public ResponseStateManagerImpl() {
        super();
    }


    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From ResponseStateManager
    //

    public Object getComponentStateToRestore(FacesContext context) {
     
        Map requestMap = context.getExternalContext().getRequestMap();
        Object state = requestMap.get(FACES_VIEW_STATE);
        // null out the temporary attribute, since we don't need it anymore.
        requestMap.put(FACES_VIEW_STATE, null);
        return state;
    }
    
    public Object getTreeStructureToRestore(FacesContext context, 
        String treeId) {
        Object structure = null;
        Object state = null;
        
        Map requestParamMap = context.getExternalContext().getRequestParameterMap();
        
        String viewString = (String) requestParamMap.get(RIConstants.FACES_VIEW);
        if ( viewString == null ) {
            return null;
        }
        byte[] bytes  = Base64.decode(viewString.getBytes());
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(bytes));
            structure = ois.readObject();
            state = ois.readObject();
            Map requestMap = context.getExternalContext().getRequestMap();
            // store the state object temporarily in request scope until it is
            // processed by getComponentStateToRestore which resets it.
            requestMap.put(FACES_VIEW_STATE, state);
            Locale locale = (Locale) ois.readObject();
            if ( locale != null) {
                context.setLocale(locale);
            }
            ois.close();
        } catch (java.io.OptionalDataException ode) {
            log.error(ode.getMessage(), ode);
        } catch (java.lang.ClassNotFoundException cnfe) {
            log.error(cnfe.getMessage(), cnfe);
        } catch (java.io.IOException iox) {
            log.error(iox.getMessage(), iox);
        }
        return structure;
    }
    
    public Object writeState(Object content, Writer out, Object structure, 
        Object state) {
        ByteArrayOutputStream bos = null;
        FacesContext context = FacesContext.getCurrentInstance();
        String hiddenField = null; 
        
        Assert.assert_it(content != null);
        String response = content.toString();
        Assert.assert_it(structure != null);
        Assert.assert_it(state != null);
        Assert.assert_it(context.getLocale() != null);
 
        try {
            bos = new ByteArrayOutputStream();
            ObjectOutput output = new ObjectOutputStream(bos);
            output.writeObject(structure);
            output.writeObject(state);
            output.writeObject(context.getLocale());
            
            hiddenField = " <input type=\"hidden\" name=\"" 
	    + RIConstants.FACES_VIEW +  "\"" + " value=\"" +
	    (new String(Base64.encode(bos.toByteArray()), "ISO-8859-1")) + 
            "\">\n ";
        } catch (Exception se ) {
            Object [] params = { se.getMessage() };
            throw new FacesException(Util.getExceptionMessage(
                    Util.SAVING_STATE_ERROR_MESSAGE_ID, params)); 
        }
        
        response = replaceMarkers(response, 
                RIConstants.SAVESTATE_FIELD_MARKER, hiddenField);
        return response;
    }
    
    public void writeStateMarker(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null);
        // if we are saving state in page, insert a marker into buffer so that 
        // UseFaces tag can replace it state information.
        boolean isSaveStateInPage = Util.isSaveStateInPage(context);
        if ( isSaveStateInPage){
	    writer.writeText(RIConstants.SAVESTATE_FIELD_MARKER,null);
        }
    }
    
    protected String replaceMarkers(String response, String marker, 
        String hiddenField) {
       
        int markerIdx = response.indexOf(marker);
        while (markerIdx != -1 ) {
            String replacedContent = response.substring(0,markerIdx);
            int markerEnd = markerIdx + marker.length();
            String endPortion = response.substring(markerEnd, response.length());
            replacedContent = replacedContent.concat(hiddenField);
            replacedContent = replacedContent.concat(endPortion);
            response = replacedContent;
            markerIdx = response.indexOf(marker);
        }
        return response;
   }                               
    

} // end of class ResponseStateManagerImpl

