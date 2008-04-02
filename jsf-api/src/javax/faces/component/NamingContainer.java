/*
 * $Id: NamingContainer.java,v 1.5 2003/02/20 22:46:10 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>NamingContainer</strong> is an interface that must be
 * implemented by any {@link UIComponent} that wants to be a naming
 * container.</p>
 */

public interface NamingContainer {


    /**
     * <p>Add the specified {@link UIComponent} to the namespace of this
     * naming container.</p>
     *
     * @param component The {@link UIComponent} to be added
     *
     * @exception IllegalArgumentException if the specified component
     *  does not have a <code>componentId</code>
     * @exception IllegalStateException if the component identifier of
     *  the specified component is not unique within the namespace of this
     *  naming container
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public void addComponentToNamespace(UIComponent component);


    /**
     * <p>Remove the specified {@link UIComponent} from the namespace of
     * this naming container, if it is present.</p>
     *
     * @param component The {@link UIComponent} to be removed
     *
     * @exception IllegalArgumentException if the specified component
     *  does not have a <code>componentId</code>
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public void removeComponentFromNamespace(UIComponent component);


    /**
     * <p>Find and return a {@link UIComponent} in this namespace, if it
     * is present; otherwise return <code>null</code>.</p>
     *
     * <p>If the argument name does not contain any {@link
     * UIComponent#SEPARATOR_CHAR} characters, it is interpreted to be a
     * name in the namespace of this naming container.</p>
     *
     * <p>If the argument name does contain {@link
     * UIComponent#SEPARATOR_CHAR} characters, each segment between
     * {@link UIComponent#SEPARATOR_CHAR} is treated as a component
     * identifier in its own namespace, which are searched for in
     * child naming containers of this naming container, from left
     * to right.</p>
     *
     * @param name Identifier of the desired component
     *
     * @exception IllegalArgumentException if <code>name</code> is malformed
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */ 
    public UIComponent findComponentInNamespace(String name);


    /**
     * <p>Generate an identifier for a component, suitable for communication
     * to a client.  The identifier returned from this method must be unique
     * within this namespace.</p>
     */
    public String generateClientId();


}
