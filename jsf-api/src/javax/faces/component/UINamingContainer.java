/*
 * $Id: UINamingContainer.java,v 1.2 2002/12/17 23:30:52 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import java.util.HashMap;


public class UINamingContainer extends UIComponentBase implements NamingContainer {


    // ------------------------------------------------------------- Attributes

    /**

    * The NamingContainer implementation

    */
    private NamingContainer namespace = null;

    public static final String TYPE = "javax.faces.component.UINamingContainer";

    public UINamingContainer() {
	namespace = new NamingContainerImpl(this);
    }

    // 
    // Methods from UIComponentBase
    //
    
    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {
	
        return (TYPE);
	
    }
    
    //
    // Methods from NamingContainer
    //

    public void addComponentToNamespace(UIComponent namedComponent) {
	namespace.addComponentToNamespace(namedComponent);
    }

    public void removeComponentFromNamespace(UIComponent namedComponent) {
	namespace.removeComponentFromNamespace(namedComponent);
    }

    public UIComponent findComponentInNamespace(String name) {
	return namespace.findComponentInNamespace(name);
    }

    public synchronized String generateClientId() {
	return namespace.generateClientId();
    }


}
