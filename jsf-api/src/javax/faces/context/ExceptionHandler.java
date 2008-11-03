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
 * <p class="changed_added_2_0"><strong>ExceptionHandler</strong> is the
 * central point for handling <em>unexpected</em>
 * <code>Exception</code>s that are thrown during the Faces lifecycle.
 * The <code>ExceptionHandler</code> must not be notified of any
 * <code>Exception</code>s that occur during application startup or
 * shutdown.  <code>ExceptionHandler</code> only deals with
 * <em>unexpected</em> <code>Exception</code>s.  Several places in the
 * Faces specification require an <code>Exception</code> to be thrown as
 * a result of normal lifecycle processing.  The following
 * <em>expected</em> <code>Exception</code>cases <strong>must
 * not</strong> be handled by the <code>ExceptionHandler</code>.</p>
 *
 * <div class="changed_added_2_0">

 * 	<ul>

	  <li><p>All cases where a {@link
	  javax.faces.validator.ValidatorException} is specified to be
	  thrown or caught</p></li>

	  <li><p>All cases where a {@link
	  javax.faces.convert.ConverterException} is specified to be
	  thrown or caught</p></li>

	  <li><p>All cases where an {@link
	  javax.faces.event.AbortProcessingException} is specified to be
	  thrown or caught </p></li>

	  <li><p>The case when an <code>Exception</code> occurs during
	  processing of the {@link
	  javax.faces.component.UIInput#updateModel} method</p></li>

	  <li><p>The case when a <code>MissingResourceException</code>
	  is thrown during the processing of the <code>&lt;f:loadBundle
	  /&gt;</code> tag.</p></li>

	</ul>

 *
 * <p>All other cases must not be swallowed, and must be passed to the
 * <code>ExceptionHandler</code> as described in section 12.3 of the
 * specification prose document.  Any code that is not a part of the
 * core Faces implementation may leverage the
 * <code>ExceptionHandler</code> by ensuring that any exceptions are
 * either not caught, or are caught and re-thrown so they end up being
 * processed by the logic in section 12.3.  Alternatively, such code may
 * publish the exception manually by following this example.</p>

<pre><code>

  //...
  } catch (Exception e) {
    ExceptionEventContext eventContext = new ExceptionEventContext(e);
    eventContext.setPhaseId(PhaseId.Whatever);
    eventContext.getAttributes().put("whateverKey", "whateverValue");
    FacesContext.getCurrentInstance().getApplication().publishEvent(
      ExceptionEvent.class, eventContext);
    // Take appropriate action to alter the call flow accordingly,
    // even when the exception is not re-thrown.
  }
  
</code></pre>

 * <p>Note that this code does not re-throw the exception.  Doing so
 * will cause multiple <code>ExceptionEvent</code> instances to be
 * published for the same exception.  Therefore, code that wishes to use
 * the <code>ExceptionHandler</code> facility must either 1. not publish
 * the event from the catch block and re-throw it from there or
 * 2. publish the event from the catch block and not re-throw it from
 * there.</p>

 * </div>
 *
 * @since 2.0
 * 
 */
public abstract class ExceptionHandler implements SystemEventListener {

 /**
  * Handle all queued exceptions. The handler will process exceptions in
  * the order that they were queued.
  *
  * The exception in "getHandledExceptionEvent()" is the first exception that isn't swallowed, but 
  * rethrown as a ServletException
  */
 public abstract void handle() throws FacesException;

 /** 
   *  The view-id of the debug page to display
   */
 public abstract String getDebugViewId();

 /**
   * Information about the handled exception
   */
 public abstract ExceptionEvent getHandledExceptionEvent();

 public abstract Iterable<ExceptionEvent> getUnhandledExceptionEvents();

 /**
  * The implementation should store the event in a strongly ordered
  * queue for later handling
  */
 public abstract void processEvent(SystemEvent exceptionEvent) throws AbortProcessingException;

 /**
  * The exception handler should return true if the source object is an 
  * instance of ExceptionEvent
  */
 public abstract boolean isListenerForSource(Object source);

    /**
     * <p class="changed_added_2_0">Unwrap the argument <code>t</code>
 * until the unwrapping encounters an Object whose
 * <code>getClass()</code> is not equal to
 * <code>FacesException.class</code> or
 * <code>javax.el.ELException.class</code>.  </p>
     */

    public abstract Throwable getRootCause(Throwable t);

}

