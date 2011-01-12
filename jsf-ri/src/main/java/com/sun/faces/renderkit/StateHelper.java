/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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


import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.ResponseStateManager;
import javax.faces.render.RenderKitFactory;

import com.sun.faces.spi.SerializationProviderFactory;
import com.sun.faces.spi.SerializationProvider;
import com.sun.faces.config.WebConfiguration;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.CompressViewState;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableViewStateIdRendering;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.AutoCompleteOffOnViewState;


/**
 * Common code for the default <code>StateHelper</code> implementations.
 */
public abstract class StateHelper {

    /**
     * <p>
     * State hidden field start.  Content after this is the actual
     * state representation followed by the {@link #STATE_FIELD_END}.
     * </p>
     *
     * @see {@link com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter#EnableViewStateIdRendering}
     */
    protected static final char[] STATE_FIELD_START =
          ("<input type=\"hidden\" name=\""
           + ResponseStateManager.VIEW_STATE_PARAM
           + "\" id=\""
           + ResponseStateManager.VIEW_STATE_PARAM
           + "\" value=\"").toCharArray();

    /**
     * <p>
     * Alternate hidden field to generate W3C valid XHTML.  This version
     * includes no id attribute.  This version will be used when the
     * <code>EnableViewStateIdRendering</code> is disabled.
     * </p>
     *
     * @see {@link com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter#EnableViewStateIdRendering}
     */
    protected static final char[] STATE_FIELD_START_NO_ID =
          ("<input type=\"hidden\" name=\""
           + ResponseStateManager.VIEW_STATE_PARAM
           + "\" value=\"").toCharArray();


    /**
     * <p>
     * The end of the hidden state field.
     * </p>
     */
    protected static final char[] STATE_FIELD_END =
          "\" />".toCharArray();

    /**
     * <p>
     * The end of the hidden state field.
     * </p>
     */
    protected static final char[] STATE_FIELD_AUTOCOMPLETE_END =
          "\" autocomplete=\"off\" />".toCharArray();

    /**
     * <p>
     * Factory for serialization streams.  These are pluggable via
     * the {@link com.sun.faces.config.WebConfiguration.WebContextInitParameter#SerializationProviderClass}.
     * </p>
     *
     * @see {@link com.sun.faces.config.WebConfiguration.WebContextInitParameter#SerializationProviderClass}
     */
    protected SerializationProvider serialProvider;

    /**
     * <p>
     * Access to the context init parameters that configure this application.
     * </p>
     */
    protected WebConfiguration webConfig;


    /**
     * <p>
     * Flag indicating whether or not view state should be compressed to reduce
     * the memory/bandwidth footprint.  This option is common to both types
     * of state saving.
     * </p>
     *
     * @see {@link com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter#CompressViewState}
     */
    protected boolean compressViewState;


    /**
     * This will be used the by the different <code>StateHelper</code> implementations
     * when writing the start of the state field.  This value of this field is
     * determined by the value of the {@link com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter#EnableViewStateIdRendering}<code>
     */
    protected char[] stateFieldStart;


    /**
     * This will be used the by the different <code>StateHelper</code> implementations
     * when writing the end of the state field.  This value of this field is
     * determined by the value of the {@link com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter#AutoCompleteOffOnViewState}<code>
     */
    protected char[] stateFieldEnd;


    // ------------------------------------------------------------ Constructors


    /**
     * Constructs a new <code>StateHelper</code> instance.
     */
    public StateHelper() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        serialProvider = SerializationProviderFactory
              .createInstance(ctx.getExternalContext());
        webConfig = WebConfiguration.getInstance(ctx.getExternalContext());
        compressViewState = webConfig.isOptionEnabled(CompressViewState);
        stateFieldStart = (webConfig.isOptionEnabled(EnableViewStateIdRendering)
                           ? STATE_FIELD_START
                           : STATE_FIELD_START_NO_ID);
        stateFieldEnd = (webConfig.isOptionEnabled(AutoCompleteOffOnViewState)
                           ? STATE_FIELD_AUTOCOMPLETE_END
                           : STATE_FIELD_END);


        if (serialProvider == null) {
            serialProvider = SerializationProviderFactory
                  .createInstance(FacesContext
                        .getCurrentInstance().getExternalContext());
        }

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>
     * Functionally similar to {@link ResponseStateManager#writeState(javax.faces.context.FacesContext, Object)}
     * with an option to write the state directly to the provided <code>StringBuilder</code>
     * without sending any markup to the client.
     * </p>
     *
     * @see {@link javax.faces.render.ResponseStateManager#writeState(javax.faces.context.FacesContext, Object)}
     */
    public abstract void writeState(FacesContext ctx,
                                    Object state,
                                    StringBuilder stateCapture)
    throws IOException;


    /**
     * @see javax.faces.render.ResponseStateManager#getState(javax.faces.context.FacesContext, String)
     */
    public abstract Object getState(FacesContext ctx, String viewId)
    throws IOException;


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Get our view state from this request</p>
     *
     * @param context the <code>FacesContext</code> for the current request
     *
     * @return the view state from this request
     */
    protected static String getStateParamValue(FacesContext context) {

        String pValue = context.getExternalContext().getRequestParameterMap().
              get(ResponseStateManager.VIEW_STATE_PARAM);
        if (pValue != null && pValue.length() == 0) {
            pValue = null;
        }
        return pValue;

    }


    /**
     * <p>
     * If a custom <code>RenderKit</code> is used, write out the ID
     * of the <code>RenderKit</code> out as a hidden field.  This will be used
     * when restoring the view state.
     * </p>
     * @param context the <code>FacesContext</code> for the current request
     * @param writer the <code>ResponseWriter</code> to write to
     * @throws IOException if an error occurs writing to the client
     */
    protected void writeRenderKitIdField(FacesContext context,
                                         ResponseWriter writer)
    throws IOException {

        String result = context.getApplication().getDefaultRenderKitId();
        if (result != null
            && !RenderKitFactory.HTML_BASIC_RENDER_KIT.equals(result)) {
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

}
