/*
 * $Id: ResponseStateManagerImpl.java,v 1.21 2005/06/09 22:37:46 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.renderkit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.faces.application.StateManager;
import javax.faces.application.StateManager.SerializedView;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;

import com.sun.faces.util.Base64;
import com.sun.faces.util.Util;

import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * <B>RenderKitImpl</B> is a class ...
 */

public class ResponseStateManagerImpl extends ResponseStateManager {

    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Logger logger = 
            Util.getLogger(Util.FACES_LOGGER + Util.RENDERKIT_LOGGER);
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

    public ResponseStateManagerImpl() {
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

    public Object getComponentStateToRestore(FacesContext context) {

        // requestMap is a local variable so we don't need to synchronize
        Map requestMap = context.getExternalContext().getRequestMap();
        Object state = requestMap.get(FACES_VIEW_STATE);
        // null out the temporary attribute, since we don't need it anymore.
        requestMap.remove(FACES_VIEW_STATE);
        return state;
    }

    public boolean isPostback(FacesContext context) {
	boolean result = context.getExternalContext().getRequestParameterMap().
                containsKey(javax.faces.render.ResponseStateManager.VIEW_STATE_PARAM);
	return result;
    }

    public Object getTreeStructureToRestore(FacesContext context,
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
		    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("Deflating state before restoring..");
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
		logger.log(Level.SEVERE, ode.getMessage(), ode);
	    } catch (java.lang.ClassNotFoundException cnfe) {
                logger.log(Level.SEVERE,cnfe.getMessage(), cnfe);
	    } catch (java.io.IOException iox) {
		logger.log(Level.SEVERE,iox.getMessage(), iox);
	    }
	}
	else {
	    structure = viewString;
	}
	return structure;
    }

    public void writeState(FacesContext context, SerializedView view)
        throws IOException {
        String hiddenField = null;
	StateManager stateManager = Util.getStateManager(context);
        ResponseWriter writer = context.getResponseWriter();

	writer.startElement("input", context.getViewRoot());
	writer.writeAttribute("type", "hidden", null);
	writer.writeAttribute("name", javax.faces.render.ResponseStateManager.VIEW_STATE_PARAM, null);
	writer.writeAttribute("id", javax.faces.render.ResponseStateManager.VIEW_STATE_PARAM, null);
	
	
	if (stateManager.isSavingStateInClient(context)) {
	    GZIPOutputStream zos = null;
	    ObjectOutputStream oos = null;
	    boolean compress = isCompressStateSet(context);
	    
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    if (compress) {
		if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Compressing state before saving..");
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
	    String valueToWrite = (new String(Base64.encode(securedata), 
					      "ISO-8859-1"));
	    writer.writeAttribute("value", 
				  valueToWrite, null);
	}
	else {
	    writer.writeAttribute("value", view.getStructure(), null);
	}
	writer.endElement("input");

        // write this out regardless of state saving mode
        // Only write it out if there is a default render kit Identifier specified,
        // and this render kit identifier is not the default.
        String result = context.getApplication().getDefaultRenderKitId();
        if (result != null && !result.equals(RenderKitFactory.HTML_BASIC_RENDER_KIT)) {
            writer.startElement("input", context.getViewRoot());
            writer.writeAttribute("type", "hidden", "type");
            writer.writeAttribute("name", ResponseStateManager.RENDER_KIT_ID_PARAM, "name");
            writer.writeAttribute("value", RenderKitFactory.HTML_BASIC_RENDER_KIT, "value");
            writer.endElement("input");
        }
    }
    
    public boolean isCompressStateSet(FacesContext context) {
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


} // end of class ResponseStateManagerImpl

