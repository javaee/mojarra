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

package javax.faces.application;

import javax.faces.FacesException;

/**
 * <p>Implementations must throw this {@link FacesException} when
 * attempting to restore the view {@link StateManager#restoreView(javax.faces.context.FacesContext, String, String)} 
 * results in failure on postback.</p>
 *
 * @since 1.2
 */

public class ViewExpiredException extends FacesException {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new exception with no detail message or root cause.</p>
     */
    public ViewExpiredException() {

        super();

    }

    /**
     *<p>Construct a new exception with the specified view identifier.</p>
     *
     * @param viewId The view identifier for this exception
     */
    public ViewExpiredException(String viewId) {
        
        this.viewId = viewId;
        
    }
    
    /**
     * <p>Construct a new exception with the specified detail message and
     * no root cause.</p>
     *
     * @param message The detail message for this exception
     * @param viewId The view identifier for this exception
     */
    public ViewExpiredException(String message, String viewId) {

        super(message);
        this.viewId = viewId;

    }


    /**
     * <p>Construct a new exception with the specified root cause.  The detail
     * message will be set to <code>(cause == null ? null :
     * cause.toString()</code>
     *
     * @param cause The root cause for this exception
     * @param viewId The view identifier for this exception
     */
    public ViewExpiredException(Throwable cause, String viewId) {

        super(cause);
        this.viewId = viewId;

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     * @param viewId The view identifier for this exception
     */
    public ViewExpiredException(String message, Throwable cause, String viewId) {

        super(message, cause);
        this.viewId = viewId;

    }

    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The view identifier of the view that could not be restored.</p>
     */
    private String viewId = null;
    
    
    // --------------------------------------------------------- Public Methods


    /**
     * <p>Return the view identifier of this exception, or <code>null</code> if the
     * view identifier is nonexistent or unknown.</p>
     */
    public String getViewId() {

        return (this.viewId);

    }

    /**
     * <p>Return the message for this exception prepended with the view identifier
     * if the view identifier is not <code>null</code>, otherwise, return the 
     * message.</p>
     */
    public String getMessage() {

        if (viewId != null) {
            return "viewId:" + viewId + " - " + super.getMessage();
        }
        return super.getMessage();

    }
    
}

    
    
