/*
 * $Id: NamingContainer.java,v 1.3 2002/12/23 22:59:34 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**

 * <p><strong>NamingContainer</strong> is implemented by any UIComponent
 * instance that wants to be a naming container.  This interface is not
 * public since it is not called by user code.  Rather, it is called
 * from UIComponent implementations.</p>

 * <p>Methods in this class must be thread-safe.</p>

 *
 */

public interface NamingContainer {


    /**

    * <p>Add the argument component to the namespace of this naming
    * container.</p>

    * <p><strong>PRECONDITION</strong>: the component identifier for the
    * argument component is valid.</p>

    * <p><strong>POSTCONDITION</strong>: The component has been added to
    * the namespace of this naming container.</p>

    * @throws IllegalArgumentException if the component identifier of
    * the argument component is not unique with in the namespace of this
    * naming container.

    */ 

    public void addComponentToNamespace(UIComponent namedComponent);

    /**

    * <p>Remove the argument component from the namespace of this naming
    * container.</p>

    * <p>If this component is not present in the namespace, or the
    * component identifier for this component is null, nothing
    * happens</p>

    * <p><strong>PRECONDITION</strong>: the component identifier for the
    * argument component is valid.</p>

    * <p><strong>POSTCONDITION</strong>: The component has been removed from
    * the namespace of this naming container, if it was present in
    * it.</p>

    */

    public void removeComponentFromNamespace(UIComponent namedComponent);

    /**

    * <p>Find a component in this namespace.</p>

    * <p>If the argument name does not contain any {@link
    * UIComponent#SEPARATOR_CHAR} characters, it is interpreted to be a
    * name in the namespace of this naming container.</p>

    * <p>If the argument name does contain {@link
    * UIComponent#SEPARATOR_CHAR} characters, each segment between
    * {@link UIComponent#SEPARATOR_CHAR} is treated as a component
    * identifier in its own namespace.</p>

    * <p>Consider the following usage:
    * <code>namingComponent.findComponent(&quot;containerOne.containerTwo.leafTwo&quot;)</code>,
    * where <code>namingComponent</code> is a <code>UIComponent</code>
    * that implements <code>NamingContainer</code>.  This call will end
    * up calling <code>findComponentInNamespace()</code>.  The first two
    * segments must be the component identifiers of NamingContainer
    * instances.  <code>containerOne</code> is in
    * <code>namingComponent</code>'s namespace,
    * <code>containerTwo</code> is in <code>containerOne</code>'s
    * namespace, and <code>leafTwo</code> is in
    * <code>containerTwo</code>'s namespace.</p>

    * @return the component stored in the namespace under the argument
    * name, or null if not found.

    * @throws IllegalArgumentException if name is malformed.

    */ 

    public UIComponent findComponentInNamespace(String name);

    /**

    * <p>Generate an id for this component.  The id returned from this
    * method must be unique in the namespace.</p>

    */

    public String generateClientId();

}
