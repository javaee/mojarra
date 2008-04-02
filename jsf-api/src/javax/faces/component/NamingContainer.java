/*
 * $Id: NamingContainer.java,v 1.2 2002/12/17 23:30:50 eburns Exp $
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

    * @return the component stored in the namespace under the argument
    * name, or null if not found.

    */ 

    public UIComponent findComponentInNamespace(String name);

    /**

    * <p>Generate an id for this component.  The id returned from this
    * method must be unique in the namespace.</p>

    */

    public String generateClientId();

}
