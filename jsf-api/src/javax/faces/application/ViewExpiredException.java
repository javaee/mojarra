/*
 * $Id: ViewExpiredException.java,v 1.2 2005/07/21 13:43:21 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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

    
    
