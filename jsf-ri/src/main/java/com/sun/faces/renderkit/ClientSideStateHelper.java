/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.faces.RIConstants;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.AutoCompleteOffOnViewState;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableViewStateIdRendering;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.NamespaceParameters;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.ClientStateTimeout;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.ClientStateWriteBufferSize;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.faces.FacesException;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.ResponseStateManager;

import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.io.Base64InputStream;
import com.sun.faces.io.Base64OutputStreamWriter;
import com.sun.faces.util.DebugObjectOutputStream;
import com.sun.faces.util.DebugUtil;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

/**
 * <p>
 * This <code>StateHelper</code> provides the functionality associated with client-side state saving.
 * </p>
 */
public class ClientSideStateHelper extends StateHelper {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    /**
     * <p>
     * Enabled encryption of view state.  Encryption is disabled by default.
     * </p>
     */
    private ByteArrayGuard guard;

    /**
     * <p>
     * Flag indicating whether or not client view state will be manipulated
     * for and checked against a configured timeout value.
     * </p>
     *
     * <p>
     * This flag is configured via the <code>WebContextInitParameter.ClientStateTimeout</code>
     * configuration option of <code>WebConfiguration</code> and is disabled by
     * default.
     * </p>
     *
     * @see {@link com.sun.faces.config.WebConfiguration.WebContextInitParameter#ClientStateTimeout}
     */
    private boolean stateTimeoutEnabled;

    /**
     * <p>
     * If <code>stateTimeoutEnabled</code> is <code>true</code> this value will
     * represent the time in seconds that a particular client view state is
     * valid for.
     * </p>
     *
     * @see {@link com.sun.faces.config.WebConfiguration.WebContextInitParameter#ClientStateTimeout}
     */
    private long stateTimeout;

    /**
     * <p>
     * Client state is generally large, so this allows some tuning to control
     * the buffer that's used to write the client state.
     * </p>
     *
     * <p>
     * The value specified must be divisable by two as the buffer is split
     * between character and bytes (due to how client state is written).  By
     * default, the buffer size is 8192 (per request).
     * </p>
     *
     * @see {@link com.sun.faces.config.WebConfiguration.WebContextInitParameter#ClientStateWriteBufferSize}
     */
    private int csBuffSize;
    
    
    private boolean debugSerializedState;


    /**
     * Flag determining whether or not javax.faces.ViewState should be namespaced.
     */
    protected boolean namespaceParameters;


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new <code>ClientSideStateHelper</code> instance.
     */
    public ClientSideStateHelper() {

        init();
        
    }


    // ------------------------------------------------ Methods from StateHelper


    /**
     * <p>
     * Writes the view state as a String generated by Base64 encoding the
     * Java Serialziation representation of the provided <code>state</code>
     * </p>
     *
     * <p>If <code>stateCapture</code> is <code>null</code>, the Base64 encoded
     * state will be written to the client as a hidden field using the <code>ResponseWriter</code>
     * from the provided <code>FacesContext</code>.</p>
     *
     * <p>If <code>stateCapture</code> is not <code>null</code>, the Base64 encoded
     * state will be appended to the provided <code>StringBuilder</code> without any markup
     * included or any content written to the client.
     *
     * @see StateHelper#writeState(javax.faces.context.FacesContext, java.lang.Object, java.lang.StringBuilder)
     */
    public void writeState(FacesContext ctx,
                           Object state,
                           StringBuilder stateCapture) throws IOException {

        if (stateCapture != null) {
            doWriteState(ctx, state, new StringBuilderWriter(stateCapture));
        } else {
            ResponseWriter writer = ctx.getResponseWriter();

            writer.startElement("input", null);
            writer.writeAttribute("type", "hidden", null);
            String viewStateParam = ResponseStateManager.VIEW_STATE_PARAM;
            
            if (namespaceParameters) {
                UIViewRoot viewRoot = ctx.getViewRoot();
                if (viewRoot instanceof NamingContainer) {
                    String namingContainerId = viewRoot.getContainerClientId(ctx);
                    if (namingContainerId != null) {
                        viewStateParam = namingContainerId + viewStateParam;
                    }
                }
            }
            writer.writeAttribute("name", viewStateParam, null);
            if (webConfig.isOptionEnabled(EnableViewStateIdRendering)) {
                String viewStateId = Util.getViewStateId(ctx);
                writer.writeAttribute("id", viewStateId, null);
            }
            StringBuilder stateBuilder = new StringBuilder();
            doWriteState(ctx, state, new StringBuilderWriter(stateBuilder));
            writer.writeAttribute("value", stateBuilder.toString(), null);
            if (webConfig.isOptionEnabled(AutoCompleteOffOnViewState)) {
                writer.writeAttribute("autocomplete", "off", null);
            }
            writer.endElement("input");

            writeClientWindowField(ctx, writer);
            writeRenderKitIdField(ctx, writer);
        }
    }


    /**
     * <p>Inspects the incoming request parameters for the standardized state
     * parameter name.  In this case, the parameter value will be a Base64 encoded
     * string previously encoded by ServerSideStateHelper#writeState(FacesContext, Object, StringBuilder).</p>
     *
     * <p>The string will be Base64-decoded and the state reconstructed using standard
     * Java serialization.</p>
     *
     * @see StateHelper#getState(javax.faces.context.FacesContext, java.lang.String)
     */
    public Object getState(FacesContext ctx, String viewId) throws IOException {

        String stateString = getStateParamValue(ctx);
        
        if (stateString == null) {
            return null;
        }
        
        if ("stateless".equals(stateString)) {
            return "stateless";
        }

        return doGetState(ctx, stateString);
    }


    // ------------------------------------------------------- Protected Methods


    /**
     * Rebuilds the view state from the Base64 included String included
     * with the request.
     *
     * @param stateString the Base64 encoded view state
     * @return the view state reconstructed from <code>stateString</code>
     */
    protected Object doGetState(FacesContext ctx, String stateString) {
        
        if ("stateless".equals(stateString)) {
            return null;
        }
        
        ObjectInputStream ois = null;
        InputStream bis = new Base64InputStream(stateString);
        try {
            if (guard != null) {
                byte[] bytes = stateString.getBytes(RIConstants.CHAR_ENCODING);
                int numRead = bis.read(bytes, 0, bytes.length);
                byte[] decodedBytes = new byte[numRead];
                bis.reset();
                bis.read(decodedBytes, 0, decodedBytes.length);

                bytes = guard.decrypt(ctx, decodedBytes);
                if (bytes == null) return null;
                bis = new ByteArrayInputStream(bytes);
            }


            if (compressViewState) {
                bis = new GZIPInputStream(bis);
            }
            
            ois = serialProvider.createObjectInputStream(bis);

            long stateTime = 0;
            if (stateTimeoutEnabled) {
                try {
                    stateTime = ois.readLong();
                } catch (IOException ioe) {
                    // we've caught an exception trying to read the time
                    // marker.  This most likely means a view that has been
                    // around before upgrading to the release that included
                    // this feature.  So, no marker, return null now to
                    // cause a ViewExpiredException
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Client state timeout is enabled, but unable to find the "
                              + "time marker in the serialized state.  Assuming state "
                              + "to be old and returning null.");
                    }
                    return null;
                }
            }
            Object structure = ois.readObject();
            Object state = ois.readObject();
            if (stateTime != 0 && hasStateExpired(stateTime)) {
                // return null if state has expired.  This should cause
                // a ViewExpiredException to be thrown
                return null;
            }

            return new Object[] { structure, state };

        } catch (java.io.OptionalDataException ode) {
        	if (LOGGER.isLoggable(Level.SEVERE)) {
        		LOGGER.log(Level.SEVERE, ode.getMessage(), ode);
        	}
            throw new FacesException(ode);
        } catch (ClassNotFoundException cnfe) {
        	if (LOGGER.isLoggable(Level.SEVERE)) {
        		LOGGER.log(Level.SEVERE, cnfe.getMessage(), cnfe);
        	}
            throw new FacesException(cnfe);
        } catch (InvalidClassException ice) {
            /*
             * Thrown when the JSF runtime is trying to deserialize a client-side
             * state that has been saved with a previous version of Mojarra. Instead
             * of blowing up, force a ViewExpiredException.
             */
            return null;
        } catch (IOException iox) {
        	if (LOGGER.isLoggable(Level.SEVERE)) {
        		LOGGER.log(Level.SEVERE, iox.getMessage(), iox);
        	}
            throw new FacesException(iox);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ioe) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST, "Closing stream", ioe);
                    }
                }
            }
        }
    }


    /**
     * Serializes and Base64 encodes the provided <code>state</code> to the
     * provided <code>writer</code>/
     *
     * @param facesContext the Faces context.
     * @param state view state
     * @param writer the <code>Writer</code> to write the content to
     * @throws IOException if an error occurs writing the state to the client
     */
    protected void doWriteState(FacesContext facesContext, Object state, Writer writer)
    throws IOException {
        
        if (facesContext.getViewRoot().isTransient()) {
            writer.write("stateless");
            writer.flush();
            return;
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream base = null;
        if (compressViewState) {
            base = new GZIPOutputStream(baos, csBuffSize);
        } else {
            base = baos;
        }

        ObjectOutputStream oos = null;

        try {
            oos = serialProvider
                .createObjectOutputStream(new BufferedOutputStream(base));

            if (stateTimeoutEnabled) {
                oos.writeLong(System.currentTimeMillis());

            }

            Object[] stateToWrite = (Object[]) state;


            if (debugSerializedState) {
                ByteArrayOutputStream discard = new ByteArrayOutputStream();
                DebugObjectOutputStream out =
                        new DebugObjectOutputStream(discard);
                try {
                        out.writeObject(stateToWrite[0]);
                } catch (Exception e) {
                    throw new FacesException(
                            "Serialization error. Path to offending instance: " 
                            + out.getStack(), e);
                }            

            }

            //noinspection NonSerializableObjectPassedToObjectStream
                oos.writeObject(stateToWrite[0]);

            if (debugSerializedState) {
                ByteArrayOutputStream discard = new ByteArrayOutputStream();

                DebugObjectOutputStream out =
                        new DebugObjectOutputStream(discard);
                try {
                        out.writeObject(stateToWrite[1]);
                } catch (Exception e) {
                    DebugUtil.printState((Map)stateToWrite[1], LOGGER);
                    throw new FacesException(
                            "Serialization error. Path to offending instance: " 
                            + out.getStack(), e);
                }            

            }

            //noinspection NonSerializableObjectPassedToObjectStream
                oos.writeObject(stateToWrite[1]);

            oos.flush();
            oos.close();
            oos = null;

            // get bytes for encrypting
            byte[] bytes = baos.toByteArray();

            if (guard != null) {
                // this will MAC
                bytes = guard.encrypt(facesContext, bytes);
            }

            // Base 64 encode
            Base64OutputStreamWriter bos =
                new Base64OutputStreamWriter(bytes.length, writer);
            bos.write(bytes, 0, bytes.length);
            bos.finish();

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "Client State: total number of characters written: {0}",
                           bos.getTotalCharsWritten());
            }
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ioe) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST, "Closing stream", ioe);
                    }
                }
            }
        }
    }

    /**
     * <p>If the {@link com.sun.faces.config.WebConfiguration.WebContextInitParameter#ClientStateTimeout} init parameter
     * is set, calculate the elapsed time between the time the client state was
     * written and the time this method was invoked during restore.  If the client
     * state has expired, return <code>true</code>.  If the client state hasn't expired,
     * or the init parameter wasn't set, return <code>false</code>.
     * @param stateTime the time in milliseconds that the state was written
     *  to the client
     * @return <code>false</code> if the client state hasn't timed out, otherwise
     *  return <code>true</code>
     */
    protected boolean hasStateExpired(long stateTime) {

        if (stateTimeoutEnabled) {
            long elapsed = (System.currentTimeMillis() - stateTime) / 60000;
            return (elapsed > stateTimeout);
        } else {
            return false;
        }

    }


    /**
     * <p>
     * Initialze the various configuration options for client-side
     * sate saving.
     * </p>
     */
    protected void init() {

        if (webConfig.canProcessJndiEntries() &&
        		!webConfig.isSet(BooleanWebContextInitParameter.DisableClientStateEncryption)) {
            guard = new ByteArrayGuard();
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "jsf.config.webconfig.enventry.clientencrypt");
        }

        }

        stateTimeoutEnabled = webConfig.isSet(ClientStateTimeout);
        if (stateTimeoutEnabled) {
            String timeout = webConfig.getOptionValue(ClientStateTimeout);
            try {
                stateTimeout = Long.parseLong(timeout);
            } catch (NumberFormatException nfe) {
                stateTimeout = Long.parseLong(ClientStateTimeout.getDefaultValue());
            }
        }


        String size = webConfig.getOptionValue(
              ClientStateWriteBufferSize);
        String defaultSize =
              ClientStateWriteBufferSize.getDefaultValue();
        try {
            csBuffSize = Integer.parseInt(size);
            if (csBuffSize % 2 != 0) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING,
                               "jsf.renderkit.resstatemgr.clientbuf_div_two",
                               new Object[]{
                                     ClientStateWriteBufferSize.getQualifiedName(),
                                     size,
                                     defaultSize});
                }
                csBuffSize = Integer.parseInt(defaultSize);
            } else {
                csBuffSize /= 2;
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Using client state buffer size of "
                                + csBuffSize);
                }
            }
        } catch (NumberFormatException nfe) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING,
                           "jsf.renderkit.resstatemgr.clientbuf_not_integer",
                           new Object[]{
                                 ClientStateWriteBufferSize.getQualifiedName(),
                                 size,
                                 defaultSize});
            }
            csBuffSize = Integer.parseInt(defaultSize);
        }

        debugSerializedState = webConfig.isOptionEnabled(BooleanWebContextInitParameter.EnableClientStateDebugging);

        namespaceParameters = webConfig.isOptionEnabled(NamespaceParameters);

    }

    /**
     * Is stateless.
     * 
     * @param facesContext the Faces context.
     * @param viewId the view id.
     * @return true if stateless, false otherwise.
     * @throws IllegalStateException when the request was not a postback.
     */
    @Override
    public boolean isStateless(FacesContext facesContext, String viewId) throws IllegalStateException {
        if (facesContext.isPostback()) {
            Object stateObject;

            try {
                stateObject = getState(facesContext, viewId);
            } catch(IOException ioe) {
                throw new IllegalStateException("Cannot determine whether or not the request is stateless", ioe);
            }
            if (stateObject instanceof String && "stateless".equals((String) stateObject)) {
                return true;
            }

            return false;
        }
        
        throw new IllegalStateException("Cannot determine whether or not the request is stateless");
    }

    // ----------------------------------------------------------- Inner Classes


    /**
     * A simple <code>Writer</code> implementation to encapsulate a
     * <code>StringBuilder</code> instance.
     */
    protected static final class StringBuilderWriter extends Writer {

        private StringBuilder sb;


        // -------------------------------------------------------- Constructors


        protected StringBuilderWriter(StringBuilder sb) {

            this.sb = sb;

        }


        // ------------------------------------------------- Methods from Writer


        @Override
        public void write(int c) throws IOException {

            sb.append((char) c);

        }


        @Override
        public void write(char cbuf[]) throws IOException {

            sb.append(cbuf);

        }


        @Override
        public void write(String str) throws IOException {

            sb.append(str);

        }


        @Override
        public void write(String str, int off, int len) throws IOException {

            sb.append(str.toCharArray(), off, len);

        }


        @Override
        public Writer append(CharSequence csq) throws IOException {

            sb.append(csq);
            return this;

        }


        @Override
        public Writer append(CharSequence csq, int start, int end)
        throws IOException {

            sb.append(csq, start, end);
            return this;

        }

        @Override
        public Writer append(char c) throws IOException {

            sb.append(c);
            return this;

        }

        public void write(char cbuf[], int off, int len) throws IOException {

            sb.append(cbuf, off, len);

        }

        public void flush() throws IOException {

            //no-op

        }

        public void close() throws IOException {

            //no-op

        }

    } // END StringBuilderWriter
}
