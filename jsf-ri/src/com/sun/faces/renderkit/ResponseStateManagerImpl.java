/*
 * $Id: ResponseStateManagerImpl.java,v 1.11 2004/02/26 20:32:52 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.renderkit;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.StateManager.SerializedView;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Map;


/**
 * <B>RenderKitImpl</B> is a class ...
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

        // requestMap is a local variable so we don't need to synchronize
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

        Map requestParamMap = context.getExternalContext()
            .getRequestParameterMap();

        String viewString = (String) requestParamMap.get(
            RIConstants.FACES_VIEW);
        if (viewString == null) {
            return null;
        }
        byte[] bytes = Base64.decode(viewString.getBytes());
        try {
            ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(bytes));
            structure = ois.readObject();
            state = ois.readObject();
            Map requestMap = context.getExternalContext().getRequestMap();
            // store the state object temporarily in request scope until it is
            // processed by getComponentStateToRestore which resets it.
            requestMap.put(FACES_VIEW_STATE, state);
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


    public void writeState(FacesContext context, SerializedView view)
        throws IOException {
        ByteArrayOutputStream bos = null;
        String hiddenField = null;

        bos = new ByteArrayOutputStream();
        ObjectOutput output = new ObjectOutputStream(bos);
        output.writeObject(view.getStructure());
        output.writeObject(view.getState());

        hiddenField = " <input type=\"hidden\" name=\""
            + RIConstants.FACES_VIEW + "\"" + " value=\"" +
            (new String(Base64.encode(bos.toByteArray()), "ISO-8859-1")) +
            "\" />\n ";
        context.getResponseWriter().write(hiddenField);
    }


    protected String replaceMarkers(String response, String marker,
                                    String hiddenField) {

        int markerIdx = response.indexOf(marker);
        while (markerIdx != -1) {
            String replacedContent = response.substring(0, markerIdx);
            int markerEnd = markerIdx + marker.length();
            String endPortion = response.substring(markerEnd,
                                                   response.length());
            replacedContent = replacedContent.concat(hiddenField);
            replacedContent = replacedContent.concat(endPortion);
            response = replacedContent;
            markerIdx = response.indexOf(marker);
        }
        return response;
    }


} // end of class ResponseStateManagerImpl

