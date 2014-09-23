/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.context;

import javax.el.ELContext;
import javax.el.ELContextListener;
import javax.el.ELContextEvent;
import javax.faces.FactoryFinder;
import javax.faces.context.ExceptionHandler;
import javax.faces.event.PhaseId;
import javax.faces.application.*;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.context.PartialViewContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.el.ELContextImpl;
import com.sun.faces.el.ELUtils;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import com.sun.faces.renderkit.RenderKitUtils;
import javax.el.ExpressionFactory;

public class FacesContextImpl extends FacesContext {

    private static final String POST_BACK_MARKER =
          FacesContextImpl.class.getName() + "_POST_BACK";

    // Queried by InjectionFacesContextFactory
    private static final ThreadLocal<FacesContext> DEFAULT_FACES_CONTEXT =
          new ThreadLocal<FacesContext>();

    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.CONTEXT.getLogger();

    private boolean released;

    // BE SURE TO ADD NEW IVARS TO THE RELEASE METHOD
    private ResponseStream responseStream = null;
    private ResponseWriter responseWriter = null;
    private ExternalContext externalContext = null;
    private Application application = null;
    private UIViewRoot viewRoot = null;
    private ELContext elContext = null;
    private RenderKitFactory rkFactory;
    private RenderKit lastRk;
    private String lastRkId;
    private Severity maxSeverity;
    private boolean renderResponse = false;
    private boolean responseComplete = false;
    private boolean validationFailed = false;
    private Map<Object, Object> attributes;
    private List<String> resourceLibraryContracts;
    private PhaseId currentPhaseId;
    private PartialViewContext partialViewContext = null;
    private ExceptionHandler exceptionHandler = null;

    /**
     * Store mapping of clientId to ArrayList of FacesMessage instances.  The
     * null key is used to represent FacesMessage instances that are not
     * associated with a clientId instance.
     */
    private Map<String, List<FacesMessage>> componentMessageLists;


    public FacesContextImpl(ExternalContext ec, Lifecycle lifecycle) {
        Util.notNull("ec", ec);
        Util.notNull("lifecycle", lifecycle);
        this.externalContext = ec;
        setCurrentInstance(this);
        DEFAULT_FACES_CONTEXT.set(this);
        rkFactory = (RenderKitFactory)
              FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    }


    // ---------------------------------------------- Methods from FacesContext


    /**
     * @see javax.faces.context.FacesContext#getExternalContext()
     */
    @Override
    public ExternalContext getExternalContext() {
        assertNotReleased();
        return externalContext;
    }


    /**
     * @see javax.faces.context.FacesContext#getApplication()
     */
    @Override
    public Application getApplication() {
        assertNotReleased();
        if (null != application) {
            return application;
        }
        ApplicationFactory aFactory =
              (ApplicationFactory) FactoryFinder.getFactory(
                    FactoryFinder.APPLICATION_FACTORY);
        application = aFactory.getApplication();
        assert (null != application);
        return application;
    }


    /**
     * @see javax.faces.context.FacesContext#getExceptionHandler()
     */
    @Override
    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }


    /**
     * @see javax.faces.context.FacesContext#setExceptionHandler(javax.faces.context.ExceptionHandler)
     */
    @Override
    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }


    /**
     * @see javax.faces.context.FacesContext#getPartialViewContext()
     */
    @Override
    public PartialViewContext getPartialViewContext() {

        assertNotReleased();
        if (partialViewContext == null) {
            PartialViewContextFactory f = (PartialViewContextFactory)
                  FactoryFinder.getFactory(FactoryFinder.PARTIAL_VIEW_CONTEXT_FACTORY);
            partialViewContext = f.getPartialViewContext(FacesContext.getCurrentInstance());
        }
        return partialViewContext;
        
    }


    /**
     * @see javax.faces.context.FacesContext#isPostback()
     */
    @Override
    public boolean isPostback() {

        assertNotReleased();
        Boolean postback = (Boolean) this.getAttributes().get(POST_BACK_MARKER);
        if (postback == null) {
            RenderKit rk = this.getRenderKit();
            if (rk != null) {
                postback = rk.getResponseStateManager().isPostback(this);
            } else {
                // ViewRoot hasn't been set yet, so calculate the RK
                ViewHandler vh = this.getApplication().getViewHandler();
                String rkId = vh.calculateRenderKitId(this);
                postback = RenderKitUtils.getResponseStateManager(this, rkId)
                      .isPostback(this);
            }
            this.getAttributes().put(POST_BACK_MARKER, postback);
        }

        return postback;

    }

    /**
     * @see javax.faces.context.FacesContext#isReleased()
     */
    @Override
    public boolean isReleased() {
        return released;
    }

    /**
     * @see javax.faces.context.FacesContext#getAttributes()
     */
    @Override
    public Map<Object, Object> getAttributes() {

        assertNotReleased();
        if (attributes == null) {
            attributes = new HashMap<Object, Object>();
        }
        return attributes;

    }


    /**
     * @see javax.faces.context.FacesContext#getELContext()
     */
    @Override
    public ELContext getELContext() {
        assertNotReleased();
        if (elContext == null) {
            Application app = getApplication();
            elContext = new ELContextImpl(app.getELResolver());
            FacesContext facesContext = FacesContext.getCurrentInstance();
            elContext.putContext(FacesContext.class, facesContext);
            ExpressionFactory exFactory = ELUtils.getDefaultExpressionFactory(facesContext);
            if (null != exFactory) {
                elContext.putContext(ExpressionFactory.class, exFactory);
            }
            UIViewRoot root = this.getViewRoot();
            if (null != root) {
                elContext.setLocale(root.getLocale());
            }
            ELContextListener[] listeners = app.getELContextListeners();
            if (listeners.length > 0) {
                ELContextEvent event = new ELContextEvent(elContext);
                for (ELContextListener listener: listeners) {
                    listener.contextCreated(event);
                }
            }
        }
        return elContext;
        
    }


    /**
     * @see javax.faces.context.FacesContext#getClientIdsWithMessages()
     */
    @Override
    public Iterator<String> getClientIdsWithMessages() {
        assertNotReleased();
        return ((componentMessageLists == null)
                ? Collections.<String>emptyList().iterator()
                : componentMessageLists.keySet().iterator());
    }


    /**
     * @see javax.faces.context.FacesContext#getMaximumSeverity()
     */
    @Override
    public Severity getMaximumSeverity() {
        assertNotReleased();
        Severity result = null;
        if (componentMessageLists != null
            && !(componentMessageLists.isEmpty())) {
            for (Iterator<FacesMessage> i =
                  new ComponentMessagesIterator(componentMessageLists);
                 i.hasNext();) {
                Severity severity = i.next().getSeverity();
                if (result == null || severity.compareTo(result) > 0) {
                    result = severity;
                }
                if (result == FacesMessage.SEVERITY_FATAL) {
                    break;
                }
            }
        }
        return result;
    }


    /**
     * @see javax.faces.context.FacesContext#getMessageList()
     */
    @Override
    public List<FacesMessage> getMessageList() {

        assertNotReleased();

        if (null == componentMessageLists) {
            return Collections
                  .unmodifiableList(Collections.<FacesMessage>emptyList());
        } else {
            List<FacesMessage> messages = new ArrayList<FacesMessage>();
            for (List<FacesMessage> list : componentMessageLists.values()) {
                messages.addAll(list);
            }
            return Collections.unmodifiableList(messages);
        }

    }


    /**
     * @see javax.faces.context.FacesContext#getMessageList(String)
     */
    @Override
    public List<FacesMessage> getMessageList(String clientId) {

        assertNotReleased();

        if (null == componentMessageLists) {
            return Collections
                  .unmodifiableList(Collections.<FacesMessage>emptyList());
        } else {
            List<FacesMessage> list = componentMessageLists.get(clientId);
            return Collections.unmodifiableList((list != null)
                                                ? list
                                                : Collections.<FacesMessage>emptyList());
        }

    }


    /**
     * @see javax.faces.context.FacesContext#getMessages()
     */
    @Override
    public Iterator<FacesMessage> getMessages() {
        assertNotReleased();
        if (null == componentMessageLists) {
            List<FacesMessage> emptyList = Collections.emptyList();
            return (emptyList.iterator());
        }

        if (componentMessageLists.size() > 0) {
            return new ComponentMessagesIterator(componentMessageLists);
        } else {
            List<FacesMessage> emptyList = Collections.emptyList();
            return (emptyList.iterator());
        }
    }

    /**
     * @see FacesContext#getMessages(String)
     */
    @Override
    public Iterator<FacesMessage> getMessages(String clientId) {
        assertNotReleased();

        // If no messages have been enqueued at all,
        // return an empty List Iterator
        if (null == componentMessageLists) {
            List<FacesMessage> emptyList = Collections.emptyList();
            return (emptyList.iterator());
        }

        List<FacesMessage> list = componentMessageLists.get(clientId);
        if (list == null) {
            List<FacesMessage> emptyList = Collections.emptyList();
            return (emptyList.iterator());
        }
        return (list.iterator());
    }


    /**
     * @see javax.faces.context.FacesContext#getRenderKit()
     */
    @Override
    public RenderKit getRenderKit() {
        assertNotReleased();
        UIViewRoot vr = getViewRoot();
        if (vr == null) {
            return (null);
        }
        String renderKitId = vr.getRenderKitId();

        if (renderKitId == null) {
            return null;
        }

        if (renderKitId.equals(lastRkId)) {
            return lastRk;
        } else {
            lastRk = rkFactory.getRenderKit(this, renderKitId);
            if (lastRk == null) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, "Unable to locate renderkit "
                            + "instance for render-kit-id {0}.  Using {1} instead.",
                            new String[]{renderKitId,
                                RenderKitFactory.HTML_BASIC_RENDER_KIT});
                }
            }
            lastRkId = renderKitId;
            return lastRk;
        }
    }


    /**
     * @see javax.faces.context.FacesContext#getResponseStream()
     */
    @Override
    public ResponseStream getResponseStream() {
        assertNotReleased();
        return responseStream;
    }


    /**
     * @see FacesContext#setResponseStream(javax.faces.context.ResponseStream)
     */
    @Override
    public void setResponseStream(ResponseStream responseStream) {
        assertNotReleased();
        Util.notNull("responseStrean", responseStream);
        this.responseStream = responseStream;
    }


    /**
     * @see javax.faces.context.FacesContext#getViewRoot()
     */
    @Override
    public UIViewRoot getViewRoot() {
        assertNotReleased();
        return viewRoot;
    }


    /**
     * @see FacesContext#setViewRoot(javax.faces.component.UIViewRoot)
     */
    @Override
    public void setViewRoot(UIViewRoot root) {
        assertNotReleased();
        Util.notNull("root", root);

        if (viewRoot != null && !viewRoot.equals(root)) {
            Map<String, Object> viewMap = viewRoot.getViewMap(false);
            if (viewMap != null) {
                viewRoot.getViewMap().clear();
            }
        }

        viewRoot = root;
    }


    /**
     * @see javax.faces.context.FacesContext#getResponseWriter()
     */
    @Override
    public ResponseWriter getResponseWriter() {
        assertNotReleased();
        return responseWriter;
    }


    /**
     * @see FacesContext#setResponseWriter(javax.faces.context.ResponseWriter)
     */
    @Override
    public void setResponseWriter(ResponseWriter responseWriter) {
        assertNotReleased();
        Util.notNull("responseWriter", responseWriter);
        this.responseWriter = responseWriter;
    }

    /**
     * @see FacesContext#addMessage(String, javax.faces.application.FacesMessage)
     */
    @Override
    public void addMessage(String clientId, FacesMessage message) {
        assertNotReleased();
        // Validate our preconditions
        Util.notNull("message", message);

        if (maxSeverity == null) {
            maxSeverity = message.getSeverity();
        } else {
            Severity sev = message.getSeverity();
            if (sev.getOrdinal() > maxSeverity.getOrdinal()) {
                maxSeverity = sev;
            }
        }

        if (componentMessageLists == null) {
            componentMessageLists =
                  new LinkedHashMap<String, List<FacesMessage>>();
        }

        // Add this message to our internal queue
        List<FacesMessage> list = componentMessageLists.get(clientId);
        if (list == null) {
            list = new ArrayList<FacesMessage>();
            componentMessageLists.put(clientId, list);
        }
        list.add(message);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Adding Message[sourceId=" +
                        (clientId != null ? clientId : "<<NONE>>") +
                        ",summary=" + message.getSummary() + ")");
        }

    }


    /**
     * @see javax.faces.context.FacesContext#getCurrentPhaseId()
     */
    @Override
    public PhaseId getCurrentPhaseId() {

        assertNotReleased();
        return currentPhaseId;

    }
    
    


    /**
     * @see javax.faces.context.FacesContext#setCurrentPhaseId(javax.faces.event.PhaseId)
     */
    @Override
    public void setCurrentPhaseId(PhaseId currentPhaseId) {

        assertNotReleased();
        this.currentPhaseId = currentPhaseId;

    }


    /**
     * @see javax.faces.context.FacesContext#release()
     */
    @Override
    public void release() {

        released = true;
        externalContext = null;
        responseStream = null;
        responseWriter = null;
        componentMessageLists = null;
        renderResponse = false;
        responseComplete = false;
        validationFailed = false;
        viewRoot = null;
        maxSeverity = null;
        application = null;
        currentPhaseId = null;
        if (attributes != null) {
            attributes.clear();
            attributes = null;
        }
        if (null != resourceLibraryContracts) {
            resourceLibraryContracts.clear();
            resourceLibraryContracts = null;
        }
        if (partialViewContext != null) {
            partialViewContext.release();
        }
        partialViewContext = null;
        exceptionHandler = null;
        elContext = null;
        rkFactory = null;
        lastRk = null;
        lastRkId = null;

        // PENDING(edburns): write testcase that verifies that release
        // actually works.  This will be important to keep working as
        // ivars are added and removed on this class over time.

        // Make sure to clear our ThreadLocal instance.
        setCurrentInstance(null);

        // remove our private ThreadLocal instance.
        DEFAULT_FACES_CONTEXT.remove();

    }


    /**
     * @see javax.faces.context.FacesContext#renderResponse()
     */
    @Override
    public void renderResponse() {
        assertNotReleased();
        renderResponse = true;
    }


    /**
     * @see javax.faces.context.FacesContext#responseComplete()
     */
    @Override
    public void responseComplete() {
        assertNotReleased();
        responseComplete = true;
    }

    /**
     * @see javax.faces.context.FacesContext#validationFailed()
     */
    @Override
    public void validationFailed() {
        assertNotReleased();
        validationFailed = true;
    }

    /**
     * @see javax.faces.context.FacesContext#getRenderResponse()
     */
    @Override
    public boolean getRenderResponse() {
        assertNotReleased();
        return renderResponse;
    }

    @Override
    public List<String> getResourceLibraryContracts() {
        assertNotReleased();
        return (null == resourceLibraryContracts) ? Collections.EMPTY_LIST : resourceLibraryContracts;
    }

    @Override
    public void setResourceLibraryContracts(List<String> contracts) {
        assertNotReleased();
        if (null == contracts || contracts.isEmpty()) {
            if (null != resourceLibraryContracts) {
                resourceLibraryContracts.clear();
                resourceLibraryContracts = null;
            }
        } else {
            resourceLibraryContracts = new ArrayList<String>(contracts);
        }
        
    }

    

    /**
     * @see javax.faces.context.FacesContext#getResponseComplete()
     */
    @Override
    public boolean getResponseComplete() {
        assertNotReleased();
        return responseComplete;
    }

    /**
     * @see javax.faces.context.FacesContext#isValidationFailed()
     */
    @Override
    public boolean isValidationFailed() {
        assertNotReleased();
        return validationFailed;
    }

    // --------------------------------------------------------- Public Methods


    public static FacesContext getDefaultFacesContext() {

        return DEFAULT_FACES_CONTEXT.get();

    }

    // -------------------------------------------------------- Private Methods


    @SuppressWarnings({"FinalPrivateMethod"})
    private final void assertNotReleased() {
        if (released) {
            throw new IllegalStateException();
        }
    }

    
    // ---------------------------------------------------------- Inner Classes


    private static final class ComponentMessagesIterator
          implements Iterator<FacesMessage> {


        private Map<String, List<FacesMessage>> messages;
        private int outerIndex = -1;
        private int messagesSize;
        private Iterator<FacesMessage> inner;
        private Iterator<String> keys;

        // ------------------------------------------------------- Constructors


        ComponentMessagesIterator(Map<String, List<FacesMessage>> messages) {

            this.messages = messages;
            messagesSize = messages.size();
            keys = messages.keySet().iterator();

        }

        // ---------------------------------------------- Methods from Iterator


        public boolean hasNext() {

            if (outerIndex == -1) {
                // pop our first List, if any;
                outerIndex++;
                inner = messages.get(keys.next()).iterator();

            }
            while (!inner.hasNext()) {
                outerIndex++;
                if ((outerIndex) < messagesSize) {
                    inner = messages.get(keys.next()).iterator();
                } else {
                    return false;
                }
            }
            return inner.hasNext();

        }

        public FacesMessage next() {

            if (outerIndex >= messagesSize) {
                throw new NoSuchElementException();
            }
            if (inner != null && inner.hasNext()) {
                return inner.next();
            } else {
                // call this.hasNext() to properly initialize/position 'inner'
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                } else {
                    return inner.next();
                }
            }

        }

        public void remove() {

            if (outerIndex == -1) {
                throw new IllegalStateException();
            }
            inner.remove();

        }

    } // END ComponentMessagesIterator

    // The testcase for this class is TestFacesContextImpl.java
    // The testcase for this class is TestFacesContextImpl_Model.java

} // end of class FacesContextImpl
