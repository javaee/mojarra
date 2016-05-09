/*
 * $Id: CustomResponseStateManagerImpl.java,v 1.3 2007/04/27 22:02:19 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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

    public void writeState(FacesContext context, SerializedView view)
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


} // end of class CustomResponseStateManagerImpl

