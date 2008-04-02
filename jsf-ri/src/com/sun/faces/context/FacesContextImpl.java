 /*
 * $Id: FacesContextImpl.java,v 1.86 2007/02/13 05:38:22 rlubke Exp $
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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.RIConstants;
import com.sun.faces.el.ELContextImpl;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

 public class FacesContextImpl extends FacesContext {

     //
     // Protected Constants
     //
     
     private static final String FACESCONTEXT_IMPL_ATTR_NAME = RIConstants.FACES_PREFIX + 
             "FacesContextImpl";

     //
     // Class Variables
     //

     // Log instance for this class
     private static Logger logger = Util.getLogger(Util.FACES_LOGGER
                                                   + Util.CONTEXT_LOGGER);

     //
     // Instance Variables
     //
     private boolean released;

     // Relationship Instance Variables
     private ResponseStream responseStream = null;
     private ResponseWriter responseWriter = null;
     private ExternalContext externalContext = null;
     private Application application = null;
     private UIViewRoot viewRoot = null;
     private ELContext elContext = null;
     private RenderKitFactory rkFactory;
     private RenderKit lastRk;
     private String lastRkId;

     /**
      * Store mapping of clientId to ArrayList of FacesMessage
      * instances.  The null key is used to represent FacesMessage instances
      * that are not associated with a clientId instance.
      */
     private Map<String,List<FacesMessage>> componentMessageLists;



     // Attribute Instance Variables

     private boolean renderResponse = false;
     private boolean responseComplete = false;


     //
     // Constructors and Initializers    
     // 
     public FacesContextImpl() {
     }


     public FacesContextImpl(ExternalContext ec, Lifecycle lifecycle) {
         if (null == ec) {
             throw new NullPointerException
                 (MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "ec"));
         }
         if (null == lifecycle) {
             throw new NullPointerException
                 (MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "lifecycle"));
         }
         this.externalContext = ec;
         setCurrentInstance(this);
         // Store this in request scope so jsf-api can access it.
         this.externalContext.getRequestMap().put(FACESCONTEXT_IMPL_ATTR_NAME, 
                 this);

         rkFactory = (RenderKitFactory)
             FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
     }

     //
     // Class methods
     //

     //
     // General Methods

     //
     //
     // Methods from FacesContext
     //

     public ExternalContext getExternalContext() {
         assertNotReleased();
         return externalContext;
     }


     /**
      * <p>Return the {@link Application} instance associated with this
      * web application.</p>
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

     public Iterator<String> getClientIdsWithMessages() {
         assertNotReleased();
         Iterator<String> result = null;
         if (null == componentMessageLists) {
             result = Collections.<String>emptyList().iterator();
         } else {
             result = componentMessageLists.keySet().iterator();

         }
         return result;
     }

     public Severity getMaximumSeverity() {
         assertNotReleased();
         Severity max = FacesMessage.SEVERITY_INFO;

         if (null == componentMessageLists) {
             return null;
         }
         // Get an Iterator over the ArrayList instances
         List messages = getMergedMessageLists();
         for (int i = 0, size = messages.size(); i < size; i++) {
             Severity s = ((FacesMessage) messages.get(i)).getSeverity();
             if (s.getOrdinal() > max.getOrdinal()) {
                 max = s;
             }

             if (FacesMessage.SEVERITY_FATAL.equals(max)) {
                 break;
             }
         }
         return max;
     }


     public Iterator<FacesMessage> getMessages() {
         assertNotReleased();
         if (null == componentMessageLists) {
             List<FacesMessage> emptyList = Collections.emptyList();
             return (emptyList.iterator());
         }

         //Clear set of clientIds from pending display messages list.
         Map<String,Object> requestMap = getExternalContext().getRequestMap();
         if (requestMap.containsKey(RIConstants.CLIENT_ID_MESSAGES_NOT_DISPLAYED)) {
        	 Set pendingClientIds = (Set)requestMap.get(RIConstants.CLIENT_ID_MESSAGES_NOT_DISPLAYED);
        	 pendingClientIds.clear();
         }

         // Get an Iterator over the ArrayList instances
         List<FacesMessage> messages = getMergedMessageLists();
         if (messages.size() > 0) {
             return messages.iterator();
         } else {
             List<FacesMessage> emptyList = Collections.emptyList();
             return (emptyList.iterator());
         }
     }


     public Iterator<FacesMessage> getMessages(String clientId) {
         assertNotReleased();

         //remove client id from pending display messages list.
         Map requestMap = getExternalContext().getRequestMap();
         Set pendingClientIds = (Set)
              requestMap.get(RIConstants.CLIENT_ID_MESSAGES_NOT_DISPLAYED);
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


     public ResponseStream getResponseStream() {
         assertNotReleased();
         return responseStream;
     }


     public void setResponseStream(ResponseStream newResponseStream) {
         assertNotReleased();
         if (newResponseStream == null) {
             throw new NullPointerException(
                 MessageUtils.getExceptionMessageString(
                     MessageUtils.NULL_RESPONSE_STREAM_ERROR_MESSAGE_ID));
         }
         responseStream = newResponseStream;
     }


     public UIViewRoot getViewRoot() {
         assertNotReleased();
         return viewRoot;
     }


     public void setViewRoot(UIViewRoot root) {
         assertNotReleased();

         if (root == null) {
             throw new NullPointerException
                 (MessageUtils.getExceptionMessageString(
                     MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "root"));
         }

         viewRoot = root;
     }


     public ResponseWriter getResponseWriter() {
         assertNotReleased();
         return responseWriter;
     }


     public void setResponseWriter(ResponseWriter newResponseWriter) {
         assertNotReleased();
         if (newResponseWriter == null) {
             throw new NullPointerException(
                 MessageUtils.getExceptionMessageString(
                     MessageUtils.NULL_RESPONSE_WRITER_ERROR_MESSAGE_ID));
         }
         responseWriter = newResponseWriter;
     }


     public void addMessage(String clientId, FacesMessage message) {
         assertNotReleased();
         // Validate our preconditions
         if (null == message) {
             throw new NullPointerException
                 (
                     MessageUtils.getExceptionMessageString(
                         MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "message"));
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
         if (logger.isLoggable(Level.FINE)) {
             logger.fine("Adding Message[sourceId=" +
                         (clientId != null ? clientId : "<<NONE>>") +
                         ",summary=" + message.getSummary() + ")");
         }

     }


     public void release() {
         
         this.externalContext.getRequestMap().remove(FACESCONTEXT_IMPL_ATTR_NAME);
         this.externalContext.getRequestMap().remove(RIConstants.CLIENT_ID_MESSAGES_NOT_DISPLAYED);
         
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


     public void renderResponse() {
         assertNotReleased();
         renderResponse = true;
     }


     public void responseComplete() {
         assertNotReleased();
         responseComplete = true;
     }


     public boolean getRenderResponse() {
         assertNotReleased();
         return renderResponse;
     }


     public boolean getResponseComplete() {
         assertNotReleased();
         return responseComplete;
     }


     //
     // Private methods
     //
     private void assertNotReleased() {
         if (released) {
             throw new IllegalStateException();
         }
     }


     private List<FacesMessage> getMergedMessageLists() {
         List<FacesMessage> mergedList = new ArrayList<FacesMessage>();
         if (componentMessageLists != null) {
             for (Iterator<List<FacesMessage>> i = componentMessageLists.values().iterator(); i.hasNext();) {
                 for (Iterator<FacesMessage> ii = i.next().iterator(); ii.hasNext();)
                     mergedList.add(ii.next());
             }
         }
         return mergedList;
     }

     // The testcase for this class is TestFacesContextImpl.java 
     // The testcase for this class is TestFacesContextImpl_Model.java

 } // end of class FacesContextImpl
