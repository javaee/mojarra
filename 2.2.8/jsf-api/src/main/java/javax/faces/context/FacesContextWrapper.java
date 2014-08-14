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

package javax.faces.context;

import java.util.Iterator;
import java.util.Map;
import java.util.List;

import javax.faces.FacesWrapper;
import javax.faces.event.PhaseId;
import javax.faces.component.UIViewRoot;
import javax.faces.render.RenderKit;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.el.ELContext;

/**
 * <p><span class="changed_modified_2_1 changed_modified_2_2">Provides</span> a simple
 * implementation of {@link FacesContext} that can be subclassed by
 * developers wishing to provide specialized behavior to an existing
 * {@link FacesContext} instance.  The default implementation of all
 * methods is to call through to the wrapped {@link FacesContext}
 * instance.</p>
 *
 * <p>Usage: extend this class and override {@link #getWrapped} to
 * return the instance being wrapping.</p>
 *
 * @since 2.0
 */
public abstract class FacesContextWrapper extends FacesContext implements FacesWrapper<FacesContext> {


    // ----------------------------------------------- Methods from FacesWrapper


    /**
     * @return the wrapped {@link FacesContext} instance
     * @see javax.faces.FacesWrapper#getWrapped()
     */
    @Override
    public abstract FacesContext getWrapped();


    // ----------------------------------------------- Methods from FacesContext


    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getApplication()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getApplication()
     */
    @Override
    public Application getApplication() {
        return getWrapped().getApplication();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link javax.faces.context.FacesContext#getClientIdsWithMessages()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see FacesContext#getClientIdsWithMessages()
     */
    @Override
    public Iterator<String> getClientIdsWithMessages() {
        return getWrapped().getClientIdsWithMessages();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getExternalContext()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getExternalContext()
     */
    @Override
    public ExternalContext getExternalContext() {
        return getWrapped().getExternalContext();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getMaximumSeverity()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getMaximumSeverity()
     */
    @Override
    public FacesMessage.Severity getMaximumSeverity() {
        return getWrapped().getMaximumSeverity();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getMessages()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getMessages()
     */
    @Override
    public Iterator<FacesMessage> getMessages() {
        return getWrapped().getMessages();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getMessages(String)}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getMessages(String)
     */
    @Override
    public Iterator<FacesMessage> getMessages(String clientId) {
        return getWrapped().getMessages(clientId);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getRenderKit()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getRenderKit()
     */
    @Override
    public RenderKit getRenderKit() {
        return getWrapped().getRenderKit();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getRenderResponse()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getRenderResponse()
     */
    @Override
    public boolean getRenderResponse() {
        return getWrapped().getRenderResponse();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getResourceLibraryContracts}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getResourceLibraryContracts
     */
    @Override
    public List<String> getResourceLibraryContracts() {
        return getWrapped().getResourceLibraryContracts();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#setResourceLibraryContracts}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#setResourceLibraryContracts
     */
    @Override
    public void setResourceLibraryContracts(List<String> contracts) {
        getWrapped().setResourceLibraryContracts(contracts);
    }
    
    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getResponseComplete()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getResponseComplete()
     */
    @Override
    public boolean getResponseComplete() {
        return getWrapped().getResponseComplete();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getResponseStream()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getResponseStream()
     */
    @Override
    public ResponseStream getResponseStream() {
        return getWrapped().getResponseStream();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#setResponseStream(ResponseStream)}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#setResponseStream(ResponseStream)
     */
    @Override
    public void setResponseStream(ResponseStream responseStream) {
        getWrapped().setResponseStream(responseStream);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getResponseWriter()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getResponseWriter()
     */
    @Override
    public ResponseWriter getResponseWriter() {
        return getWrapped().getResponseWriter();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#setResponseWriter(ResponseWriter)}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#setResponseWriter(ResponseWriter)
     */
    @Override
    public void setResponseWriter(ResponseWriter responseWriter) {
        getWrapped().setResponseWriter(responseWriter);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getViewRoot()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getViewRoot()
     */
    @Override
    public UIViewRoot getViewRoot() {
        return getWrapped().getViewRoot();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#setViewRoot(UIViewRoot)}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#setViewRoot(UIViewRoot)
     */
    @Override
    public void setViewRoot(UIViewRoot root) {
        getWrapped().setViewRoot(root);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#addMessage(String, FacesMessage)}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#addMessage(String, FacesMessage)
     */
    @Override
    public void addMessage(String clientId, FacesMessage message) {
        getWrapped().addMessage(clientId, message);
    }

    /**
     * <p class="changed_added_2_1">The default behavior of this method
     * is to call {@link FacesContext#isReleased} on the wrapped {@link
     * FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#isReleased

     * @since 2.1
     */
    @Override
    public boolean isReleased() {
	return getWrapped().isReleased();
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#release()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#release()
     */
    @Override
    public void release() {
        getWrapped().release();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#renderResponse()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#renderResponse()
     */
    @Override
    public void renderResponse() {
        getWrapped().renderResponse();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#responseComplete()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#responseComplete()
     */
    @Override
    public void responseComplete() {
        getWrapped().responseComplete();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getAttributes()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getAttributes()
     */
    @Override
    public Map<Object, Object> getAttributes() {
        return getWrapped().getAttributes();
    }

    /**
     * <p class="changed_added_2_2">The default behavior of this method
     * is to call {@link FacesContext#getNamingContainerSeparatorChar()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getNamingContainerSeparatorChar()
     */
    @Override
    public char getNamingContainerSeparatorChar() {
        return getWrapped().getNamingContainerSeparatorChar();
    }
    
    

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getPartialViewContext()} ()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getPartialViewContext()
     */
    @Override
    public PartialViewContext getPartialViewContext() {
        return getWrapped().getPartialViewContext();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getELContext()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getELContext()
     */
    @Override
    public ELContext getELContext() {
        return getWrapped().getELContext();
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getExceptionHandler()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getExceptionHandler()
     */
    @Override
    public ExceptionHandler getExceptionHandler() {
        return getWrapped().getExceptionHandler();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#setExceptionHandler(ExceptionHandler)}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#setExceptionHandler(ExceptionHandler)
     */
    @Override
    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        getWrapped().setExceptionHandler(exceptionHandler);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getMessageList()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getMessageList()
     */
    @Override
    public List<FacesMessage> getMessageList() {
        return getWrapped().getMessageList();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getMessageList(String)}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getMessageList(String)
     */
    @Override
    public List<FacesMessage> getMessageList(String clientId) {
        return getWrapped().getMessageList(clientId);
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#isPostback()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#isPostback()
     */
    @Override
    public boolean isPostback() {
        return getWrapped().isPostback();
    }

    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#getCurrentPhaseId()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#getCurrentPhaseId()
     */
    @Override
    public PhaseId getCurrentPhaseId() {
        return getWrapped().getCurrentPhaseId();
    }
    
    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#setCurrentPhaseId(PhaseId)}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#setCurrentPhaseId(PhaseId)
     */
    @Override
    public void setCurrentPhaseId(PhaseId currentPhaseId) {
        getWrapped().setCurrentPhaseId(currentPhaseId);
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link javax.faces.context.FacesContext#isValidationFailed}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see FacesContext#isValidationFailed
     */
    @Override
    public boolean isValidationFailed() {

        return getWrapped().isValidationFailed();

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link javax.faces.context.FacesContext#validationFailed()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see FacesContext#validationFailed()
     */
    @Override
    public void validationFailed() {

        getWrapped().validationFailed();

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link FacesContext#setProcessingEvents(boolean)}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see javax.faces.context.FacesContext#setProcessingEvents(boolean)
     */
    @Override
    public void setProcessingEvents(boolean processingEvents) {
        getWrapped().setProcessingEvents(processingEvents);
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link javax.faces.context.FacesContext#isProcessingEvents()}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see FacesContext#isProcessingEvents()
     */
    @Override
    public boolean isProcessingEvents() {
        return getWrapped().isProcessingEvents();
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link javax.faces.context.FacesContext#isProjectStage(javax.faces.application.ProjectStage)}
     * on the wrapped {@link FacesContext} object.</p>
     *
     * @see FacesContext#isProjectStage(javax.faces.application.ProjectStage) 
     */
    @Override
    public boolean isProjectStage(ProjectStage stage) {
        return getWrapped().isProjectStage(stage);    
    }
}
