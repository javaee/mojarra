 /*
 * $Id: FacesContextImpl.java,v 1.95 2008/03/10 16:39:24 rlubke Exp $
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

package com.sun.faces.context;

import javax.el.ELContext;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.el.ELContextImpl;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.RequestStateManager;
import com.sun.faces.util.Util;

 public class FacesContextImpl extends FacesContext {

     // Log instance for this class
     private static Logger LOGGER = FacesLogger.CONTEXT.getLogger();

     private boolean released;

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

     /**
      * Store mapping of clientId to ArrayList of FacesMessage
      * instances.  The null key is used to represent FacesMessage instances
      * that are not associated with a clientId instance.
      */
     private Map<String,List<FacesMessage>> componentMessageLists;


     // ----------------------------------------------------------- Constructors


     public FacesContextImpl(ExternalContext ec, Lifecycle lifecycle) {
         Util.notNull("ec", ec);
         Util.notNull("lifecycle", lifecycle);
         this.externalContext = ec;
         setCurrentInstance(this);
         rkFactory = (RenderKitFactory)
             FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
     }


     // ---------------------------------------------- Methods from FacesContext


     /**
      * @see javax.faces.context.FacesContext#getExternalContext()
      */
     public ExternalContext getExternalContext() {
         assertNotReleased();
         return externalContext;
     }


     /**
      * @see javax.faces.context.FacesContext#getApplication()
      */
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
      * @see javax.faces.context.FacesContext#getELContext()
      */
     @Override
     public ELContext getELContext() {
         assertNotReleased();
         if (elContext == null) {
             elContext = new ELContextImpl(getApplication().getELResolver());
             elContext.putContext(FacesContext.class, this);
             UIViewRoot root = this.getViewRoot();
             if (null != root) {
                 elContext.setLocale(root.getLocale());
             }
         }
         return elContext;
     }


     /**
      * @see javax.faces.context.FacesContext#getClientIdsWithMessages()
      */
     public Iterator<String> getClientIdsWithMessages() {
         assertNotReleased();
         return ((componentMessageLists == null)
                 ? Collections.<String>emptyList().iterator()
                 : componentMessageLists.keySet().iterator());
     }


     /**
      * @see javax.faces.context.FacesContext#getMaximumSeverity()
      */
     public Severity getMaximumSeverity() {
         assertNotReleased();
         Severity result = null;
         if (componentMessageLists != null && !(componentMessageLists.isEmpty())) {
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
      * @see javax.faces.context.FacesContext#getMessages()
      */
     public Iterator<FacesMessage> getMessages() {
         assertNotReleased();
         if (null == componentMessageLists) {
             List<FacesMessage> emptyList = Collections.emptyList();
             return (emptyList.iterator());
         }

         //Clear set of clientIds from pending display messages list.
         if (RequestStateManager.containsKey(this, RequestStateManager.CLIENT_ID_MESSAGES_NOT_DISPLAYED)) {
            Set pendingClientIds = (Set)
                   RequestStateManager.get(this, RequestStateManager.CLIENT_ID_MESSAGES_NOT_DISPLAYED);
            pendingClientIds.clear();
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
     public Iterator<FacesMessage> getMessages(String clientId) {
         assertNotReleased();

         //remove client id from pending display messages list.
         Set pendingClientIds = (Set)
              RequestStateManager.get(this, RequestStateManager.CLIENT_ID_MESSAGES_NOT_DISPLAYED);
         if (pendingClientIds != null && !pendingClientIds.isEmpty()) {
            pendingClientIds.remove(clientId);
         }

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
             lastRkId = renderKitId;
             return lastRk;
         }
     }


     /**
      * @see javax.faces.context.FacesContext#getResponseStream()
      */
     public ResponseStream getResponseStream() {
         assertNotReleased();
         return responseStream;
     }


     /**
      * @see FacesContext#setResponseStream(javax.faces.context.ResponseStream)
      */
     public void setResponseStream(ResponseStream responseStream) {
         assertNotReleased();
         Util.notNull("responseStrean", responseStream);
         this.responseStream = responseStream;
     }


     /**
      * @see javax.faces.context.FacesContext#getViewRoot()
      */
     public UIViewRoot getViewRoot() {
         assertNotReleased();
         return viewRoot;
     }


     /**
      * @see FacesContext#setViewRoot(javax.faces.component.UIViewRoot)
      */
     public void setViewRoot(UIViewRoot root) {
         assertNotReleased();
         Util.notNull("root", root);
         viewRoot = root;
     }


     /**
      * @see javax.faces.context.FacesContext#getResponseWriter()
      */
     public ResponseWriter getResponseWriter() {
         assertNotReleased();
         return responseWriter;
     }


     /**
      * @see FacesContext#setResponseWriter(javax.faces.context.ResponseWriter)
      */
     public void setResponseWriter(ResponseWriter responseWriter) {
         assertNotReleased();
         Util.notNull("responseWriter", responseWriter);
         this.responseWriter = responseWriter;
     }


     /**
      * @see FacesContext#addMessage(String, javax.faces.application.FacesMessage)
      */
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
             componentMessageLists = new LinkedHashMap<String,List<FacesMessage>>();
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
      * @see javax.faces.context.FacesContext#release()
      */
     public void release() {

         RequestStateManager.remove(this, RequestStateManager.FACESCONTEXT_IMPL_ATTR_NAME);
         RequestStateManager.remove(this, RequestStateManager.CLIENT_ID_MESSAGES_NOT_DISPLAYED);

         released = true;
         externalContext = null;
         responseStream = null;
         responseWriter = null;
         componentMessageLists = null;
         renderResponse = false;
         responseComplete = false;
         viewRoot = null;

         // PENDING(edburns): write testcase that verifies that release
         // actually works.  This will be important to keep working as
         // ivars are added and removed on this class over time.

         // Make sure to clear our ThreadLocal instance.
         setCurrentInstance(null);
     }


     /**
      * @see javax.faces.context.FacesContext#renderResponse()
      */
     public void renderResponse() {
         assertNotReleased();
         renderResponse = true;
     }


     /**
      * @see javax.faces.context.FacesContext#responseComplete()
      */
     public void responseComplete() {
         assertNotReleased();
         responseComplete = true;
     }


     /**
      * @see javax.faces.context.FacesContext#getRenderResponse()
      */
     public boolean getRenderResponse() {
         assertNotReleased();
         return renderResponse;
     }


     /**
      * @see javax.faces.context.FacesContext#getResponseComplete()
      */
     public boolean getResponseComplete() {
         assertNotReleased();
         return responseComplete;
     }


     // -------------------------------------------------------- Private Methods


     private void assertNotReleased() {
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