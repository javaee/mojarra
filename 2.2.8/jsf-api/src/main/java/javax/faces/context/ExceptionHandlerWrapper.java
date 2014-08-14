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

import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.SystemEvent;

/**
 * <p>Provides a simple implementation of {@link ExceptionHandler} that can
 * be subclassed by developers wishing to provide specialized behavior
 * to an existing {@link ExceptionHandler} instance.  The default
 * implementation of all methods is to call through to the wrapped
 * {@link ExceptionHandler} instance.</p>
 *
 * <p>Usage: extend this class and override {@link #getWrapped} to
 * return the instance we are wrapping.</p>
 *
 * @since 2.0
 */
public abstract class ExceptionHandlerWrapper extends ExceptionHandler implements FacesWrapper<ExceptionHandler> {


    // ----------------------------------------------- Methods from FacesWrapper


    /**
     * @return the wrapped {@link ExceptionHandler} instance
     * @see javax.faces.FacesWrapper#getWrapped() 
     */
    @Override
    public abstract ExceptionHandler getWrapped();


    // ------------------------------------------- Methods from ExceptionHandler


    /**
     * <p>The default behavior of this method is to
     * call {@link ExceptionHandler#getHandledExceptionQueuedEvent()}
     * on the wrapped {@link ExceptionHandler} object.</p>
     *
     * @see ExceptionHandler#getHandledExceptionQueuedEvent()
     */
    @Override
    public ExceptionQueuedEvent getHandledExceptionQueuedEvent() {
        return getWrapped().getHandledExceptionQueuedEvent();
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link javax.faces.context.ExceptionHandler#handle()}
     * on the wrapped {@link ExceptionHandler} object.</p>
     *
     * @see javax.faces.context.ExceptionHandler#handle()
     */
    @Override
    public void handle() throws FacesException {
        getWrapped().handle();
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link javax.faces.context.ExceptionHandler#isListenerForSource(Object)}
     * on the wrapped {@link ExceptionHandler} object.</p>
     *
     * @see javax.faces.context.ExceptionHandler#isListenerForSource(Object) ()
     */
    @Override
    public boolean isListenerForSource(Object source) {
        return getWrapped().isListenerForSource(source);
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link javax.faces.context.ExceptionHandler#processEvent(javax.faces.event.SystemEvent)}
     * on the wrapped {@link ExceptionHandler} object.</p>
     *
     * @see javax.faces.context.ExceptionHandler#processEvent(javax.faces.event.SystemEvent)
     */
    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        getWrapped().processEvent(event);
    }


    /**
     * <p>The default behavior of this method is to
     * call {@link javax.faces.context.ExceptionHandler#getRootCause(Throwable)}
     * on the wrapped {@link ExceptionHandler} object.</p>
     *
     * @see javax.faces.context.ExceptionHandler#getRootCause(Throwable)
     */
    @Override
    public Throwable getRootCause(Throwable t) {
        return getWrapped().getRootCause(t);
    }


    /**
     * <p>The default behavior of this method is to call
     * {@link ExceptionHandler#getHandledExceptionQueuedEvents()} on the wrapped
     * {@link ExceptionHandler} object.</p>
     *
     * @see ExceptionHandler#getHandledExceptionQueuedEvents()
     */
    @Override
    public Iterable<ExceptionQueuedEvent> getHandledExceptionQueuedEvents() {
        return getWrapped().getHandledExceptionQueuedEvents();
    }

    
    /**
     * <p>The default behavior of this method is to
     * call {@link ExceptionHandler#getUnhandledExceptionQueuedEvents()}
     * on the wrapped {@link ExceptionHandler} object.</p>
     *
     * @see ExceptionHandler#getUnhandledExceptionQueuedEvents()
     */
    @Override
    public Iterable<ExceptionQueuedEvent> getUnhandledExceptionQueuedEvents() {
        return getWrapped().getUnhandledExceptionQueuedEvents();
    }

}
