/*
 * $Id: ResponseStateManagerImpl.java,v 1.13.24.1.2.4 2007/04/27 21:27:44 ofung Exp $
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


package com.sun.faces.renderkit;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Base64;
import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.StateManager.SerializedView;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.Map;
import javax.faces.application.StateManager;
import javax.faces.context.ResponseWriter;


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
    
     private static final String COMPRESS_STATE_PARAM =
        "com.sun.faces.COMPRESS_STATE";
    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private Boolean compressStateSet = null;
    
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
	StateManager stateManager = Util.getStateManager(context);
        
        Object structure = null;
        Object state = null;
        ByteArrayInputStream bis = null;
        GZIPInputStream gis = null;
        ObjectInputStream ois = null;
        boolean compress = isCompressStateSet(context);
        
        Map requestParamMap = context.getExternalContext()
            .getRequestParameterMap();

        String viewString = (String) requestParamMap.get(
            RIConstants.FACES_VIEW);
        if (viewString == null) {
            return null;
        }
	if (stateManager.isSavingStateInClient(context)) {
	    byte[] bytes = Base64.decode(viewString.getBytes());
	    try {
		bis = new ByteArrayInputStream(bytes);
		if (isCompressStateSet(context)) {
		    if (log.isDebugEnabled()) {
			log.debug("Deflating state before restoring..");
		    }
		    gis = new GZIPInputStream(bis);
		    ois = new ApplicationObjectInputStream(gis);
		} else {
		    ois = new ApplicationObjectInputStream(bis);
		}
		structure = ois.readObject();
		state = ois.readObject();
		Map requestMap = context.getExternalContext().getRequestMap();
		// store the state object temporarily in request scope until it is
		// processed by getComponentStateToRestore which resets it.
		requestMap.put(FACES_VIEW_STATE, state);
		bis.close();
		if ( compress) {
		    gis.close();
		}
		ois.close();
	    } catch (java.io.OptionalDataException ode) {
		log.error(ode.getMessage(), ode);
		throw new FacesException(ode);
	    } catch (java.lang.ClassNotFoundException cnfe) {
		log.error(cnfe.getMessage(), cnfe);
		throw new FacesException(cnfe);
	    } catch (java.io.IOException iox) {
		log.error(iox.getMessage(), iox);
		throw new FacesException(iox);
	    }
	}
	else {
	    structure = viewString;
	}
	return structure;
    }


    public void writeState(FacesContext context, SerializedView view)
        throws IOException {

	StateManager stateManager = Util.getStateManager(context);
        ResponseWriter writer = context.getResponseWriter();

	writer.startElement("input", context.getViewRoot());
	writer.writeAttribute("type", "hidden", null);
	writer.writeAttribute("name", RIConstants.FACES_VIEW, null);
	writer.writeAttribute("id", RIConstants.FACES_VIEW, null);
        
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
	    bos.close();
	    
	    String valueToWrite = (new String(Base64.encode(bos.toByteArray()), "ISO-8859-1"));
	    writer.writeAttribute("value", 
				  valueToWrite, null);
	}
	else {
	    writer.writeAttribute("value", view.getStructure(), null);
	}
	writer.endElement("input");

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

