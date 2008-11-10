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

import java.util.LinkedList;
import java.util.Collection;

import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.ExceptionEventContext;
import javax.el.ELException;


/**
 * The default implementation of {@link ExceptionHandler}.
 */
public class ExceptionHandlerImpl extends ExceptionHandler {

    
    private LinkedList<ExceptionEvent> unhandledExceptions;
    private LinkedList<ExceptionEvent> handledExceptions;


    // ------------------------------------------- Methods from ExceptionHandler


    /**
     * @see ExceptionHandler@getHandledExceptionEvent()
     */
    public ExceptionEvent getHandledExceptionEvent() {

        return (isNullOrEmpty(unhandledExceptions) ? null : handledExceptions.getLast());

    }


    /**
     * @see javax.faces.context.ExceptionHandler#handle()
     */
    public void handle() throws FacesException {

        if (!isNullOrEmpty(unhandledExceptions)) {
            for (ExceptionEvent event : getUnhandledExceptionEvents()) {
                try {
                    ExceptionEventContext context = (ExceptionEventContext) event.getSource();
                    // do something
                } finally {
                    if (handledExceptions == null) {
                        handledExceptions = new LinkedList<ExceptionEvent>();
                    }
                    handledExceptions.add(event);
                }
            }
        }

    }


    /**
     * @see javax.faces.context.ExceptionHandler#isListenerForSource(Object)
     */
    public boolean isListenerForSource(Object source) {

        return (source instanceof ExceptionEvent);

    }


    /**
     * @see javax.faces.context.ExceptionHandler#processEvent(javax.faces.event.SystemEvent)
     */
    public void processEvent(SystemEvent event) throws AbortProcessingException {

        if (unhandledExceptions == null) {
            unhandledExceptions = new LinkedList<ExceptionEvent>();
        }
        unhandledExceptions.add((ExceptionEvent) event);

    }


    /**
     * @see ExceptionHandler#getRootCause(Throwable)
     */
    public Throwable getRootCause(Throwable t) {

        Class<? extends Throwable> tClass = t.getClass();
        Throwable ret = t;
        if (shouldUnwrap(tClass)) {
            ret = t.getCause();
            while (ret != null) {
                if (shouldUnwrap(tClass)) {
                    ret = ret.getCause();
                    continue;
                }
                break;
            }
        }

        return ret;
        
    }


    /**
     * @see javax.faces.context.ExceptionHandler#getUnhandledExceptionEvents()
     */
    public Iterable<ExceptionEvent> getUnhandledExceptionEvents() {

        // should we clone?
        return unhandledExceptions;

    }


    /**
     * @see javax.faces.context.ExceptionHandler#getHandledExceptionEvents()
     * @return
     */
    public Iterable<ExceptionEvent> getHandledExceptionEvents() {

        // should we clone?
        return handledExceptions;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * @param c <code>Throwable</code> implementation class
     * @return <code>true</code> if <code>c</code> is FacesException.class or
     *  ELException.class
     */
    private boolean shouldUnwrap(Class<? extends Throwable> c) {

        return (FacesException.class.equals(c) || ELException.class.equals(c));

    }


    private boolean isNullOrEmpty(Collection c) {

        return (c == null || c.isEmpty());

    }

}
