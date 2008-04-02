/*
 * $Id: ViewExpiredException.java,v 1.3 2005/08/22 22:07:51 ofung Exp $
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


package javax.faces.application;

import javax.faces.FacesException;

/**
 * <p>Implementations must throw this {@link FacesException} when
 * attempting to restore the view {@link StateManager#restoreView(javax.faces.context.FacesContext, String, String)} 
 * results in failure on postback.</p>
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

    
    
