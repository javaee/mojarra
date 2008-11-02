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
 * Interface an exception handler should implement 
 * 
 **/
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

 /**
  * Information about all queued exceptions, handled and not handled.
  * 
  * OPEN ISSUE: Should we included already handled exceptions, or only exceptions not yet handled? Should there be two methods for this?
  * /
 public Iterable<ExceptionEvent> getExceptions();

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

}

