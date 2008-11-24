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

package javax.faces.context;

import javax.faces.FacesException;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;


/**
 * RELEASE_PENDING (edburns,rogerk) I wonder if it might make more
 *  sense to have the default behavior language in the spec pdf instead
 *  of cluttering the javadocs of an abstract class.  People who extend
 *  this class don't care about the default implementation.  It should define
 *  the general contract that all implementations of ExceptionHandler should
 *  follow in order to work proprely in the runtime.
 *  Also, note that the spec should state that the implementation, should queue
 *  AbortProcessingExceptions caught during standard runtime processing, but
 *  when handle() is called, these Exceptions are logged and not thrown.
 *
 * <ul>
 *
 * <li><p>Ensuring that <code>Exception</code>s are not caught,
 * or are caught and re-thrown.</p>
 *
 *         <p>This approach allows the <code>ExceptionHandler</code>
 *         facility specified in section 12.3 to operate on the
 *         <code>Exception</code>.</p>
 *
 *         </li>
 *
 * <li><p>Using the system event facility to publish an {@link
 * ExceptionEvent} that wraps the <code>Exception</code>.</p>
 *
 *         <p>This approach requires manually publishing the {@link
 *         ExceptionEvent}, but allows more information about the
 *         <code>Exception</code>to be stored in the event.  The
 *         following code is an example of how to do this.</p>
 *
 * <pre><code>
 *
 * //...
 * } catch (Exception e) {
 *   FacesContext ctx = FacesContext.getCurrentInstance();
 *   ExceptionEventContext eventContext = new ExceptionEventContext(ctx, e);
 *   eventContext.getAttributes().put("key", "value");
 *   ctx.getApplication().publishEvent(ExceptionEvent.class, eventContext);
 * }
 *
 * </code></pre>
 *
 *            <p>Because the <code>Exception</code> must not be re-thrown
 *            when using this approach, lifecycle processing may continue
 *            as normal, allowing more <code>Exception</code>s to be
 *            published if necessary.</p>
 *
 *         </li>
 * </ul>
 *
 * <p>With either approach, any <code>ExceptionEvent</code> instances
 * that are published in this way are accessible to the {@link #handle}
 * method, which is called at the end of each lifecycle phase, as
 * specified in section 12.3.</p>
 *
 * <p>The behavior of the default <code>ExceptionHandler</code> is
 * specified in the documentation for the methods in this class.</p>
 *
 * </div>
 *
 * @since 2.0
 */
public abstract class ExceptionHandler implements SystemEventListener {

   /**
    * RELEASE_PENDING (edburns,roger) This needs further clarification.
    *  the handle method will, when called, process *all* unhandled
    *  ExceptionEvents in the order they were queued.  The docs should
    *  probably state thus instead of "must take the first ExceptionEvent
    *  queued"  The "handled" exception will be the first exception that isn't
    *  swallowed.  The first queued exception, in the default implementation
    *  may not be thrown (i.e. AbortProcessingExceptions are queued but not
    *  thrown giving developers a chance to handle them)
    *
    * <p class="changed_added_2_0">The default implementation must take
    * the first {@link ExceptionEvent} queued from a call to {@link
    * #processEvent}, unwrap it with a call to {@link #getRootCause},
    * re-wrap it in a <code>ServletException</code> and re-throw it,
    * allowing it to be handled by any <code>&lt;error-page&gt;</code>
    * declared in the web application deployment descriptor, or by the
    * default error page, as described in section 6.1.13 ExceptionHandler.
    * The default implementation must take special action in the following
    * cases.</p>
    *
    * <div class="changed_added_2_0">
    *
    *
    * <ul>
    *
    * <li><p>If an unchecked <code>Exception</code> occurs as a
    * result of calling a method annotated with
    * <code>PreDestroy</code> on a managed bean, the
    * <code>Exception</code> must be logged and swallowed.</p></li>
    *
    * <li><p>If the <code>Exception</code> originates inside the
    * <code>ELContextListener.removeElContextListener</code>, the
    * <code>Exception</code> must be logged and swallowed.</p></li>
    *
    * </ul>
    *
    * </div>
    */
    public abstract void handle() throws FacesException;


    /**
     * RELEASE_PENDING (edburns,rogerk) this should return the
     * first "handled" (i.e. thrown) ExceptionEvent.
     *
     * <p class="changed_added_2_0">The default implementation must
     * return the first <code>ExceptionEvent</code> queued to {@link
     * #processEvent}.</p>
     */
    public abstract ExceptionEvent getHandledExceptionEvent();


    /**
     * <p class="changed_added_2_0">The default implementation must return an
     * <code>Iterable</code> over all <code>ExceptionEvent</code>s that have
     * not yet been handled by the {@link #handle} method.</p>
     */
    public abstract Iterable<ExceptionEvent> getUnhandledExceptionEvents();


    /**
     * <p class="changed_added_2_0">The default implementation must
     * return an <code>Iterable</code> over all
     * <code>ExceptionEvent</code>s that have been handled by the {@link
     * #handle} method.</p>
     */
    public abstract Iterable<ExceptionEvent> getHandledExceptionEvents();


    /**
     * <p class="changed_added_2_0">The default implementation must
     * store the event in a strongly ordered queue for later handling</p>
     */
    public abstract void processEvent(SystemEvent exceptionEvent) throws AbortProcessingException;


    /**
     * RELEASE_PENDING (edburns, rogerk) The source will be ExceptionEventContext,
     *  so it should only return true in that case.
     *
     * <p class="changed_added_2_0">The default implementation must
     * return <code>true</code> if and only if the source argument is an
     * instance of <code>ExceptionEvent</code>.</p>
     */
    public abstract boolean isListenerForSource(Object source);

    
    /**
     * <p class="changed_added_2_0">Unwrap the argument <code>t</code>
     * until the unwrapping encounters an Object whose
     * <code>getClass()</code> is not equal to
     * <code>FacesException.class</code> or
     * <code>javax.el.ELException.class</code>.  </p>
     *
     * RELEASE_PENDING (edburns, rogerk) should specify that if there is no root
     *  cause, null is returned
     */
    public abstract Throwable getRootCause(Throwable t);

}

