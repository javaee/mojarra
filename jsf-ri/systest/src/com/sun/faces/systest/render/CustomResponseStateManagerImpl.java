/*
 * $Id: CustomResponseStateManagerImpl.java,v 1.4 2005/08/22 22:10:44 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */


package com.sun.faces.systest.render;

import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.ByteArrayGuard;
import com.sun.faces.util.Base64;
import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.StateManager.SerializedView;
import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.Map;


/**
 * <B>RenderKitImpl</B> is a class ...
 */

public class CustomResponseStateManagerImpl extends ResponseStateManager {

    //
    // Protected Constants
    //
    protected static Log log =
        LogFactory.getLog(CustomResponseStateManagerImpl.class);
    private static final String FACES_VIEW_STATE =
        "com.sun.faces.FACES_VIEW_STATE";
    
     private static final String COMPRESS_STATE_PARAM =
        "com.sun.faces.COMPRESS_STATE";
    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private Boolean compressStateSet = null;
    private ByteArrayGuard byteArrayGuard = null;
    
    //
    // Ivars used during actual client lifetime
    //

    // Relationship Instance Variables

    
    //
    // Constructors and Initializers    
    //

    public CustomResponseStateManagerImpl() {
        super();
        byteArrayGuard = new ByteArrayGuard();
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

    public Object getState(FacesContext context, String viewId) {
        Object stateArray[] = { getTreeStructure(context, viewId),
                                getComponentState(context) };
        return stateArray;
    }

    public boolean isPostback(FacesContext context) {
	boolean result = context.getExternalContext().getRequestParameterMap().
                containsKey(javax.faces.render.ResponseStateManager.VIEW_STATE_PARAM);
	return result;
    }

    private Object getComponentState(FacesContext context) {

        // requestMap is a local variable so we don't need to synchronize
        Map requestMap = context.getExternalContext().getRequestMap();
        Object state = requestMap.get(FACES_VIEW_STATE);
        // null out the temporary attribute, since we don't need it anymore.
        requestMap.remove(FACES_VIEW_STATE);
        return state;
    }

    private Object getTreeStructure(FacesContext context,
                                            String treeId) {
	StateManager stateManager = Util.getStateManager(context);
        
	Map requestParamMap = context.getExternalContext()
	    .getRequestParameterMap();
	
	String viewString = (String) requestParamMap.get(
							 javax.faces.render.ResponseStateManager.VIEW_STATE_PARAM);
	Object structure = null;
	if (viewString == null) {
	    return null;
	}
	
	if (stateManager.isSavingStateInClient(context)) {
	    Object state = null;
	    ByteArrayInputStream bis = null;
	    GZIPInputStream gis = null;
	    ObjectInputStream ois = null;
	    boolean compress = isCompressStateSet(context);
	   
	    try {
                 byte[] bytes = byteArrayGuard.decrypt(context,
                    (Base64.decode(viewString.getBytes())));
		bis = new ByteArrayInputStream(bytes);
		if (isCompressStateSet(context)) {
		    if (log.isDebugEnabled()) {
			log.debug("Deflating state before restoring..");
		    }
		    gis = new GZIPInputStream(bis);
		    ois = new ObjectInputStream(gis);
		} else {
		    ois = new ObjectInputStream(bis);
		}
		structure = ois.readObject();
		state = ois.readObject();
		Map requestMap = context.getExternalContext().getRequestMap();
		// store the state object temporarily in request scope
		// until it is processed by getComponentStateToRestore
		// which resets it.
		requestMap.put(FACES_VIEW_STATE, state);
		bis.close();
		if ( compress) {
		    gis.close();
		}
		ois.close();
	    } catch (java.io.OptionalDataException ode) {
		log.error(ode.getMessage(), ode);
	    } catch (java.lang.ClassNotFoundException cnfe) {
            log.error(cnfe.getMessage(), cnfe);
	    } catch (java.io.IOException iox) {
		log.error(iox.getMessage(), iox);
	    }
	}
	else {
	    structure = viewString;
	}
	return structure;
    }

    public void writeState(FacesContext context,
                           Object state) throws IOException {
        SerializedView view = null;
        if (state instanceof SerializedView) {
            view = (SerializedView) state;
        }
        else {
            Object[] stateArray = (Object[])state;
            StateManager stateManager =
                context.getApplication().getStateManager();
            view = stateManager.new SerializedView(stateArray[0], null);
        }
        writeSerializedState(context, view);
    }

    private void writeSerializedState(FacesContext context, SerializedView view)
        throws IOException {
        String hiddenField = null;
	StateManager stateManager = Util.getStateManager(context);
	
	if (stateManager.isSavingStateInClient(context)) {
	    GZIPOutputStream zos = null;
	    ObjectOutputStream oos = null;
	    boolean compress = isCompressStateSet(context);
	    
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    if (compress) {
		if (log.isDebugEnabled()) {
		    log.debug("Compressing state before saving..");
		}
		zos = new GZIPOutputStream(bos);
		oos = new ObjectOutputStream(zos);
	    } else {
		oos = new ObjectOutputStream(bos);    
	    }
	    oos.writeObject(view.getStructure());
	    oos.writeObject(view.getState());
	    oos.close();
	    if (compress) {
		zos.close();
	    }
            byte[] securedata = byteArrayGuard.encrypt(context, 
                    bos.toByteArray());
	    bos.close();
	    
	    hiddenField = " <input type=\"hidden\" name=\""
		+ javax.faces.render.ResponseStateManager.VIEW_STATE_PARAM + "\"" + " value=\"" +
                    (new String(Base64.encode(securedata), "ISO-8859-1"))
		+ "\" />\n ";
	}
	else {
	    hiddenField = " <input type=\"hidden\" name=\""
		+ javax.faces.render.ResponseStateManager.VIEW_STATE_PARAM + "\"" + " value=\"" +
		view.getStructure() +
		"\" />\n ";
	    
	}
        context.getResponseWriter().write(hiddenField);

        // write this out regardless of state saving mode
        // Only write it out if there is a default specified, and 
        // this render kit identifier is not the default.
        String result = context.getApplication().getDefaultRenderKitId();
        if ((null != result && !result.equals("CUSTOM")) || result == null) {
            hiddenField = " <input type=\"hidden\" name=\""
                + ResponseStateManager.RENDER_KIT_ID_PARAM + "\"" + " value=\"CUSTOM\"" +
                "\" />\n ";
            context.getResponseWriter().write(hiddenField);
        }
    }
    
    private boolean isCompressStateSet(FacesContext context) {
	if (null != compressStateSet) {
	    return compressStateSet.booleanValue();
	}
	compressStateSet = Boolean.TRUE;

        String compressStateParam = context.getExternalContext().
            getInitParameter(COMPRESS_STATE_PARAM);
        if (compressStateParam != null){
	    compressStateSet = Boolean.valueOf(compressStateParam);
        }
	return compressStateSet.booleanValue();
    }


} // end of class CustomResponseStateManagerImpl

