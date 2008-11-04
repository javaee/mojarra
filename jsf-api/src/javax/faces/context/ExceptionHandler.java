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
 * <p>All other <code>Exception</code> cases must not be swallowed, and
 * must be allowed to flow up to the {@link
 * javax.faces.lifecycle.Lifecycle#execute} method where the individual
 * lifecycle phases are implemented.  At that point, all
 * <code>Exception</code>s are passed to the
 * <code>ExceptionHandler</code> as described in section 12.3 of the
 * specification prose document.  Any code that is not a part of the
 * core Faces implementation may leverage the
 * <code>ExceptionHandler</code> in one of two ways.</p>
 *
 * 	<ul>

	  <li><p>Ensuring that <code>Exception</code>s are not caught,
	  or are caught and re-thrown.</p>

          <p>This approach allows the <code>ExceptionHandler</code>
          facility specified in section 12.3 to operate on the
          <code>Exception</code>.</p>

          </li>

	  <li><p>Using the system event facility to publish an {@link
	  ExceptionEvent} that wraps the <code>Exception</code>.</p>

          <p>This approach requires manually publishing the {@link
          ExceptionEvent}, but allows more information about the
          <code>Exception</code>to be stored in the event.  The
          following code is an example of how to do this.</p>

<pre><code>

  //...
  } catch (Exception e) {
    ExceptionEventContext eventContext = new ExceptionEventContext(e);
    eventContext.setPhaseId(PhaseId.Whatever);
    eventContext.getAttributes().put("whateverKey", "whateverValue");
    FacesContext.getCurrentInstance().getApplication().publishEvent(
      ExceptionEvent.class, eventContext);
  }
  
</code></pre>

             <p>Because the <code>Exception</code> must not be re-thrown
             when using this approach, lifecycle processing may continue
             as normal, allowing more <code>Exception</code>s to be
             published if necessary.</p>

          </li>
	</ul>

 * <p>With either approach, any <code>ExceptionEvent</code> instances
 * that are published in this way are accessible to the {@link #handle}
 * method, which is called at the end of each lifecycle phase, as
 * specified in section 12.3.</p>

 * <p>The behavior of the default <code>ExceptionHandler</code> is
 * specified in the documentation for the methods in this class.</p>

 * </div>
 *
 * @since 2.0
 * 
 */
public abstract class ExceptionHandler implements SystemEventListener {

   /**
    * <p class="changed_added_2_0">The default implementation must take
    * the first {@link ExceptionEvent} queued from a call to {@link
    * #processEvent}, unwrap it with a call to {@link #getRootCause},
    * re-wrap it in a <code>ServletException</code> and re-throw it,
    * allowing it to be handled by any <code>&lt;error-page&gt;</code>
    * declared in the web application deployment descriptor, or by the
    * default error page, as described in section 6.1.13 ExceptionHandler.
    * The default implementation must take special action in the following
    * cases.</p>
    
    * <div class="changed_added_2_0">
    
    *
    * 	<ul>

	  <li><p>If an unchecked <code>Exception</code> occurs as a
	  result of calling a method annotated with
	  <code>PreDestroy</code> on a managed bean, the
	  <code>Exception</code> must be logged and swallowed.</p></li>

	  <li><p>If the <code>Exception</code> originates inside the
	  <code>ELContextListener.removeElContextListener</code>, the
	  <code>Exception</code> must be logged and swallowed.</p></li>

    *	</ul>
    *
    * </div>
    *
    * @since 2.0
    */
    
    public abstract void handle() throws FacesException;
    
    /** 
     * <p class="changed_added_2_0">The default implementation must return
     * the implementation specific view-id of the error page.</p>
     *
     * @since 2.0
     */

    public abstract String getDebugViewId();
    
    /**
     * <p class="changed_added_2_0">The default implementation must
     * return the first <code>ExceptionEvent</code> queued to {@link
     * #processEvent}.</p>
     *
     * @since 2.0
     */
    
    public abstract ExceptionEvent getHandledExceptionEvent();
    
    /**
     * <p class="changed_added_2_0">The default implementation must
     * return an <code>Iterable</code> over all
     * <code>ExceptionEvent</code>s that have not yet been handled by
     * the {@link #handle} method.</p>
     *
     * @since 2.0
     */

     public abstract Iterable<ExceptionEvent> getUnhandledExceptionEvents();

    /**
     * <p class="changed_added_2_0">The default implementation must
     * store the event in a strongly ordered queue for later handling</p>
     *
     * @since 2.0
     */
    public abstract void processEvent(SystemEvent exceptionEvent) throws AbortProcessingException;
    
    /**
     * <p class="changed_added_2_0">The default implementation must
     * return <code>true</code> if and only if the source argument is an
     * instance of <code>ExceptionEvent</code>.</p>
     *
     * @since 2.0
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

