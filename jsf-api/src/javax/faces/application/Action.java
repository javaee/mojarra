/*
 * $Id: Action.java,v 1.2 2003/03/13 01:11:51 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;


/**
 * <p>An <strong>Action</strong> is an object that performs a task,
 * and returns a String value that describes the result of performing
 * that task.  {@link Action}s are typically invoked via
 * {@link javax.faces.component.UIComponent}s that have an
 * <code>actionRef</code> attribute, when that component is activated
 * by the user.</p>
 *
 * <p>An object that implements {@link Action} is typically made available for
 * use by JavaServer Faces components by exposing a read-only JavaBeans
 * property that returns an {@link Action} instance, often implemented as
 * an anonymous inner class.  For example:</p>
 * <pre>
 *   public class MyNavigationHandler {
 *     ...
 *     public Action getMenuPickAction() {
 *       return new Action() {
 *         public String invoke() { return (doMenuPick()); }
 *       }
 *     }
 *     ...
 *     public String doMenuPick() {
 *       ... actual behavior goes here ...
 *     }
 *     ...
 *   }
 * </pre>
 * <p>Using this technique, an application object can expose behavior both
 * directly to other application objects (the public <code>doMenuPick()</code>
 * method) and indirectly to JavaServer Faces components who might access it
 * via an <code>actionRef</code> property value like
 * "<code>handler.menuPickAction</code>".
 *
 * <p>If needed, an {@link Action} can gain access to state information
 * about the current JavaServer Faces request by calling the static
 * method <code>FacesContext.getCurrentInstance()</code>.</p>
 */

public abstract class Action {


    /**
     * <p>Perform the task that is encapsulated in this {@link Action}
     * instance, and return a String value that is a logical description
     * of the outcome of performing that task.  Exceptions encountered
     * during the performance of the task should be encapsulated into
     * appropriate return values.</p>
     */
    public abstract String invoke();


}
