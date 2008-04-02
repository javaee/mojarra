/*
 * $Id: ResponseStateManagerImpl.java,v 1.32 2006/05/22 20:01:26 rlubke Exp $
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


package com.sun.faces.renderkit;

import javax.faces.FacesException;
import javax.faces.application.StateManager;
import javax.faces.application.StateManager.SerializedView;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.config.WebConfiguration.WebEnvironmentEntry;
import com.sun.faces.util.Base64;
import com.sun.faces.util.Util;


/** 
 * <p>A <code>ResonseStateManager</code> implementation
 * for the default HTML render kit. 
 */
public class ResponseStateManagerImpl extends ResponseStateManager {

    // Log instance for this class
    private static final Logger logger =
          Util.getLogger(Util.FACES_LOGGER + Util.RENDERKIT_LOGGER);
    private static final String FACES_VIEW_STATE =
          "com.sun.faces.FACES_VIEW_STATE";


    private Boolean compressState = null;
    private ByteArrayGuard byteArrayGuard = null;


    public ResponseStateManagerImpl() {

        super();
        WebConfiguration webConfig = WebConfiguration.getInstance();
        assert(webConfig != null);
        byteArrayGuard = ByteArrayGuard
              .newInstance(webConfig.getEnvironmentEntry(
                    WebEnvironmentEntry.ClientStateSavingPassword));

    }

    @Override
    @SuppressWarnings("Deprecation")
    public Object getComponentStateToRestore(FacesContext context) {

        // requestMap is a local variable so we don't need to synchronize
        Map<String, Object> requestMap =
              context.getExternalContext().getRequestMap();
        Object state = requestMap.get(FACES_VIEW_STATE);
        // null out the temporary attribute, since we don't need it anymore.
        requestMap.remove(FACES_VIEW_STATE);
        return state;

    }


    @Override
    public boolean isPostback(FacesContext context) {

        return context.getExternalContext().getRequestParameterMap().
              containsKey(ResponseStateManager.VIEW_STATE_PARAM);

    }


    @Override
    public Object getState(FacesContext context, String viewId) {

        return (super.getState(context, viewId));

    }


    @Override
    @SuppressWarnings("Deprecation")
    public Object getTreeStructureToRestore(FacesContext context,
                                            String treeId) {

        StateManager stateManager = Util.getStateManager(context);

        Map<String, String> requestParamMap = context.getExternalContext()
              .getRequestParameterMap();

        String viewString = requestParamMap.get(
              ResponseStateManager.VIEW_STATE_PARAM);
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
                byte[] bytes = byteArrayGuard.decrypt(
                      (Base64.decode(viewString.getBytes())));
                bis = new ByteArrayInputStream(bytes);
                if (isCompressStateSet(context)) {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("Deflating state before restoring..");
                    }
                    gis = new GZIPInputStream(bis);
                    ois = new ApplicationObjectInputStream(gis);
                } else {
                    ois = new ApplicationObjectInputStream(bis);
                }
                structure = ois.readObject();
                state = ois.readObject();
                Map<String, Object> requestMap =
                      context.getExternalContext().getRequestMap();
                // store the state object temporarily in request scope
                // until it is processed by getComponentStateToRestore
                // which resets it.
                requestMap.put(FACES_VIEW_STATE, state);
                bis.close();
                if (compress) {
                    gis.close();
                }
                ois.close();
            } catch (java.io.OptionalDataException ode) {
                logger.log(Level.SEVERE, ode.getMessage(), ode);
                throw new FacesException(ode);
            } catch (java.lang.ClassNotFoundException cnfe) {
                logger.log(Level.SEVERE, cnfe.getMessage(), cnfe);
                throw new FacesException(cnfe);
            } catch (java.io.IOException iox) {
                logger.log(Level.SEVERE, iox.getMessage(), iox);
                throw new FacesException(iox);
            }
        } else {
            structure = viewString;
        }
        return structure;

    }


    @Override
    @SuppressWarnings("Deprecation")
    public void writeState(FacesContext context, SerializedView view)
          throws IOException {

        StateManager stateManager = Util.getStateManager(context);
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("input", context.getViewRoot());
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("name",
                              javax.faces.render.ResponseStateManager.VIEW_STATE_PARAM,
                              null);
        writer.writeAttribute("id",
                              javax.faces.render.ResponseStateManager.VIEW_STATE_PARAM,
                              null);


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
            byte[] securedata = byteArrayGuard.encrypt(bos.toByteArray());
            bos.close();
            String valueToWrite = (new String(Base64.encode(securedata),
                                              "ISO-8859-1"));
            writer.writeAttribute("value",
                                  valueToWrite, null);
        } else {
            writer.writeAttribute("value", view.getStructure(), null);
        }
        writer.endElement("input");

        // write this out regardless of state saving mode
        // Only write it out if there is a default render kit Identifier specified,
        // and this render kit identifier is not the default.
        String result = context.getApplication().getDefaultRenderKitId();
        if (result != null && !result
              .equals(RenderKitFactory.HTML_BASIC_RENDER_KIT)) {
            writer.startElement("input", context.getViewRoot());
            writer.writeAttribute("type", "hidden", "type");
            writer.writeAttribute("name",
                                  ResponseStateManager.RENDER_KIT_ID_PARAM,
                                  "name");
            writer.writeAttribute("value",
                                  RenderKitFactory.HTML_BASIC_RENDER_KIT,
                                  "value");
            writer.endElement("input");
        }

    }


    private boolean isCompressStateSet(FacesContext context) {

        if (null != compressState) {
            return compressState;
        }
        compressState = WebConfiguration
              .getInstance(context.getExternalContext())
              .getBooleanContextInitParameter(BooleanWebContextInitParameter.CompressViewState);

        return compressState;

    }

} // end of class ResponseStateManagerImpl

