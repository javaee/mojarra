/*
 * $Id: ResponseStateManagerImpl.java,v 1.34 2006/05/22 23:35:52 rlubke Exp $
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
    private static final Logger LOGGER =
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

    
    /**
     * @see {@link ResponseStateManager#getComponentStateToRestore(javax.faces.context.FacesContext)}       
     */
    @Override
    @SuppressWarnings("Deprecation")
    public Object getComponentStateToRestore(FacesContext context) {

        // requestMap is a local variable so we don't need to synchronize        
        return context.getExternalContext().getRequestMap()
              .remove(FACES_VIEW_STATE);

    }


    /**
     * @see {@link ResponseStateManager#isPostback(javax.faces.context.FacesContext)}     
     */
    @Override   
    public boolean isPostback(FacesContext context) {

        return context.getExternalContext().getRequestParameterMap().
              containsKey(ResponseStateManager.VIEW_STATE_PARAM);

    }   


    /**
     * @see {@link ResponseStateManager#getTreeStructureToRestore(javax.faces.context.FacesContext, String)}      
     */
    @Override
    @SuppressWarnings("Deprecation")
    public Object getTreeStructureToRestore(FacesContext context,
                                            String treeId) {

        StateManager stateManager = Util.getStateManager(context);

        String viewString = getStateParam(context);
        Object structure;
        if (viewString == null) {
            return null;
        }

        if (stateManager.isSavingStateInClient(context)) {
            Object state;
            ByteArrayInputStream bis;
            GZIPInputStream gis = null;
            ObjectInputStream ois;
            boolean compress = isCompressStateSet(context);

            try {
                byte[] bytes = byteArrayGuard.decrypt(
                      (Base64.decode(viewString.getBytes())));
                bis = new ByteArrayInputStream(bytes);
                if (isCompressStateSet(context)) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Deflating state before restoring..");
                    }
                    gis = new GZIPInputStream(bis);
                    ois = new ApplicationObjectInputStream(gis);
                } else {
                    ois = new ApplicationObjectInputStream(bis);
                }
                structure = ois.readObject();
                state = ois.readObject();
               
                bis.close();
                if (compress) {
                    if (gis != null) {
                        gis.close();
                    }
                }
                ois.close();
                
                storeStateInRequest(context, state);
                
            } catch (java.io.OptionalDataException ode) {
                LOGGER.log(Level.SEVERE, ode.getMessage(), ode);
                throw new FacesException(ode);
            } catch (java.lang.ClassNotFoundException cnfe) {
                LOGGER.log(Level.SEVERE, cnfe.getMessage(), cnfe);
                throw new FacesException(cnfe);
            } catch (java.io.IOException iox) {
                LOGGER.log(Level.SEVERE, iox.getMessage(), iox);
                throw new FacesException(iox);
            }
        } else {
            structure = viewString;
        }
        return structure;

    }
    

    /**
     * @see {@link ResponseStateManager#writeState(javax.faces.context.FacesContext, javax.faces.application.StateManager.SerializedView)}       
     */
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
            ObjectOutputStream oos;
            boolean compress = isCompressStateSet(context);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            if (compress) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Compressing state before saving..");
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

        writeRenderKitIdField(context, writer);

    }


    /**
     * <p>Store the state for this request into a temporary attribute
     * within the same request.</p>
     * @param context the <code>FacesContext</code> of the current request
     * @param state the view state
     */
     private void storeStateInRequest(FacesContext context, Object state) {

        // store the state object temporarily in request scope
        // until it is processed by getComponentStateToRestore
        // which resets it.
        context.getExternalContext().getRequestMap()
              .put(FACES_VIEW_STATE, state);
       
    }
    

    /**
     * <p>Write a hidden field if the default render kit ID is not
     * RenderKitFactory.HTML_BASIC_RENDER_KIT.</p>
     *
     * @param context the <code>FacesContext</code> for the current request
     * @param writer  the target writer
     *
     * @throws IOException if an error occurs
     */
    private void writeRenderKitIdField(FacesContext context,
                                       ResponseWriter writer)
          throws IOException {
        String result = context.getApplication().getDefaultRenderKitId();        
        if (result != null && 
            !RenderKitFactory.HTML_BASIC_RENDER_KIT.equals(result)) {
            writer.startElement("input", context.getViewRoot());
            writer.writeAttribute("type", "hidden", "type");
            writer.writeAttribute("name",
                                  ResponseStateManager.RENDER_KIT_ID_PARAM,
                                  "name");
            writer.writeAttribute("value",
                                  result,
                                  "value");
            writer.endElement("input");
        }
    }

    /**
     * <p>Get our view state from this request</p>
     * @param context the <code>FacesContext</code> for the current request
     * @return the view state from this request
     */
    private String getStateParam(FacesContext context) {       

        return context.getExternalContext().getRequestParameterMap().get(
              ResponseStateManager.VIEW_STATE_PARAM);       
    }


    /**
     * <p>Determines if client state should or should not be
     * compressed.</p>
     * @param context the <code>FacesContext</code> for the current request
     * @return <code>true</code> if the state should be compressed before 
     *  writing to the response, otherwise <code>false</code>
     */
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

