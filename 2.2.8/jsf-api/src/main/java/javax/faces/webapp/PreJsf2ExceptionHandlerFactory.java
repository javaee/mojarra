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

package javax.faces.webapp;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Iterator;

import javax.faces.context.ExceptionHandlerFactory;
import javax.faces.context.ExceptionHandler;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.PhaseId;
import javax.el.ELException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UpdateModelException;
import javax.faces.context.FacesContext;


/**
 * <p class="changed_added_2_0">This {@link ExceptionHandlerFactory} instance 
 * produces JSF 1.2 compatible
 * {@link ExceptionHandler} instances.  The {@link ExceptionHandler#handle} 
 * method of the <code>ExceptionHandler</code> produced by this factory must 
 * meet the following requirements</p>
 * <div class="changed_added_2_0">
 * 
 * <ul>
 * 
 * <li><p>Any
 * exceptions thrown before or after phase execution will be logged and 
 * swallowed.</p></li>
 * 
 * <li><p>The implementation must examine
 * the <code>Exception</code> within each of the unhandled exception
 * events.  If the <code>Exception</code> is an instance of
 * {@link UpdateModelException}, extract the {@link FacesMessage} from
 * the <code>UpdateModelException</code>.  Log a <code>SEVERE</code>
 * message to the log and queue the <code>FacesMessage</code> 
 * on the {@link FacesContext}, using the <code>clientId</code> of
 * the source component in a call to 
 * {@link FacesContext#addMessage(java.lang.String, javax.faces.application.FacesMessage)}</p></li>
 * 
 * </ul>
 * 
 * </div>
 *
 * @since 2.0
 */
public class PreJsf2ExceptionHandlerFactory extends ExceptionHandlerFactory {

    public PreJsf2ExceptionHandlerFactory() {
    }


    // ------------------------------------ Methods from ExceptionHandlerFactory


    /**
     * @return a new {@link ExceptionHandler} that behaves in a fashion compatible
     *  with specifications prior to JavaServerFaces 1.2
     */
    public ExceptionHandler getExceptionHandler() {

        return new PreJsf2ExceptionHandler();

    }


    // ---------------------------------------------------------- Nested Classes


    /**
     * JSF 1.2-style <code>ExceptionHandler</code> implementation.
     */
    private static final class PreJsf2ExceptionHandler extends ExceptionHandler {


        private static final Logger LOGGER =
              Logger.getLogger("javax.faces.webapp", "javax.faces.LogStrings");

        private static final String LOG_BEFORE_KEY =
              "servere.webapp.prejsf2.exception.handler.log_before";
        private static final String LOG_AFTER_KEY =
              "servere.webapp.prejsf2.exception.handler.log_after";
        private static final String LOG_KEY =
              "servere.webapp.prejsf2.exception.handler.log";


        private LinkedList<ExceptionQueuedEvent> unhandledExceptions;
        private LinkedList<ExceptionQueuedEvent> handledExceptions;
        private ExceptionQueuedEvent handled;


        // ------------------------------------------- Methods from ExceptionHandler


        /**
         * @see ExceptionHandler@getHandledExceptionQueuedEvent()
         */
        public ExceptionQueuedEvent getHandledExceptionQueuedEvent() {

            return handled;

        }


        /**
         * 
         * 
         * @since 2.0
         */
        public void handle() throws FacesException {

            for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {
                ExceptionQueuedEvent event = i.next();
                ExceptionQueuedEventContext context =
                      (ExceptionQueuedEventContext) event.getSource();
                try {
                    Throwable t = context.getException();
                    if (isRethrown(t, (context.inBeforePhase() || context.inAfterPhase()))) {
                        handled = event;
                        Throwable unwrapped = getRootCause(t);
                        if (unwrapped != null) {
                            throw new FacesException(unwrapped.getMessage(), unwrapped);
                        } else {
                            if (t instanceof FacesException) {
                                throw (FacesException) t;
                            } else {
                                throw new FacesException(t.getMessage(), t);
                            }
                        }
                    } else {
                        log(context);
                    }

                } finally {
                    if (handledExceptions == null) {
                        handledExceptions =
                              new LinkedList<ExceptionQueuedEvent>();
                    }
                    handledExceptions.add(event);
                    i.remove();
                }
            }

        }


        /**
         * @see javax.faces.context.ExceptionHandler#isListenerForSource(Object)
         */
        public boolean isListenerForSource(Object source) {

            return (source instanceof ExceptionQueuedEventContext);

        }


        /**
         * @see javax.faces.context.ExceptionHandler#processEvent(javax.faces.event.SystemEvent)
         */
        public void processEvent(SystemEvent event)
              throws AbortProcessingException {

            if (event != null) {
                if (unhandledExceptions == null) {
                    unhandledExceptions = new LinkedList<ExceptionQueuedEvent>();
                }
                unhandledExceptions.add((ExceptionQueuedEvent) event);
            }

        }


        /**
         * @see ExceptionHandler#getRootCause(Throwable)
         */
        public Throwable getRootCause(Throwable t) {

            if (t == null) {
                return null;
            }
            if (shouldUnwrap(t.getClass())) {
                Throwable root = t.getCause();
                if (root != null) {
                    Throwable tmp = getRootCause(root);
                    if (tmp == null) {
                        return root;
                    } else {
                        return tmp;
                    }
                } else {
                    return t;
                }
            }
            return t;

        }


        /**
         * @see javax.faces.context.ExceptionHandler#getUnhandledExceptionQueuedEvents()
         */
        public Iterable<ExceptionQueuedEvent> getUnhandledExceptionQueuedEvents() {

            return ((unhandledExceptions != null)
                    ? unhandledExceptions
                    : Collections.<ExceptionQueuedEvent>emptyList());

        }


        /**
         * @return
         *
         * @see javax.faces.context.ExceptionHandler#getHandledExceptionQueuedEvents()
         */
        public Iterable<ExceptionQueuedEvent> getHandledExceptionQueuedEvents() {

            return ((handledExceptions != null)
                    ? handledExceptions
                    : Collections.<ExceptionQueuedEvent>emptyList());

        }


        // --------------------------------------------------------- Private Methods


        /**
         * @param c <code>Throwable</code> implementation class
         *
         * @return <code>true</code> if <code>c</code> is FacesException.class or
         *         ELException.class
         */
        private boolean shouldUnwrap(Class<? extends Throwable> c) {

            return (FacesException.class.equals(c) || ELException.class.equals(c));

        }


        private boolean isRethrown(Throwable t, boolean isBeforeOrAfterPhase) {

            return (!isBeforeOrAfterPhase &&
                    !(t instanceof AbortProcessingException) &&
                    !(t instanceof UpdateModelException));

        }

        
        private void log(ExceptionQueuedEventContext exceptionContext) {

            Throwable t = exceptionContext.getException();
            UIComponent c = exceptionContext.getComponent();
            if (t instanceof UpdateModelException) {
                FacesContext context = FacesContext.getCurrentInstance();
                FacesMessage message = ((UpdateModelException)t).getFacesMessage();
                LOGGER.log(Level.SEVERE, message.getSummary(), t.getCause());
                context.addMessage(c.getClientId(context), message);
            } else {
                boolean beforePhase = exceptionContext.inBeforePhase();
                boolean afterPhase = exceptionContext.inAfterPhase();
                PhaseId phaseId = exceptionContext.getPhaseId();
                String key = getLoggingKey(beforePhase, afterPhase);
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                            key,
                            new Object[]{t.getClass().getName(),
                                        phaseId.toString(),
                                        ((c != null)
                                         ? c.getClientId(exceptionContext.getContext())
                                         : ""),
                                        t.getMessage()});
                    LOGGER.log(Level.SEVERE, t.getMessage(), t);
                }
            }

        }

        private String getLoggingKey(boolean beforePhase, boolean afterPhase) {
            if (beforePhase) {
                return LOG_BEFORE_KEY;
            } else if (afterPhase) {
                return LOG_AFTER_KEY;
            } else {
                return LOG_KEY;
            }
        }

    } // END PreJsf2ExceptionHandler
    
}
