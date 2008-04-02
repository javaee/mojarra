/*
 * $Id: NamingContainerImpl.java,v 1.3 2002/12/18 17:44:14 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import java.util.HashMap;
import java.io.Serializable;


public class NamingContainerImpl extends Object implements NamingContainer, Serializable {


    // ------------------------------------------------------------- Attributes

    /**


    * The map for the namespace of this naming container

     * <p><strong>IMPLEMENTATION NOTE</strong> - This is lazily
     * allocated, initialized, and populated.</p>

    */
    private HashMap namespace = null;
    private int serialNumber = 0;

    private UIComponent myComponent;

    public static String ID_PREFIX = "id";

    public NamingContainerImpl(UIComponent component) {
	if (!(component instanceof NamingContainer)) {
	    throw new IllegalArgumentException();
	}
	myComponent = component;
    }

    //
    // Methods from NamingContainer
    //

    public synchronized void addComponentToNamespace(UIComponent namedComponent) {
	
	if (null == namespace) {
	    namespace = new HashMap();
	}
	String key = null;
	if (null == (key = namedComponent.getComponentId())) {
	    return;
	}
	if (namespace.containsKey(key)) {
	    throw new IllegalArgumentException(key);
	}
	namespace.put(key, namedComponent);
	
    }

    public synchronized void removeComponentFromNamespace(UIComponent namedComponent) {
	if (null == namespace) {
	    return;
	}
	String key = null;
	if (null == (key = namedComponent.getComponentId())) {
	    return;
	}
	if (namespace.containsKey(key)) {
	    namespace.remove(key);
	}
	
    }

    public synchronized UIComponent findComponentInNamespace(String name) {
	UIComponent result = null;
	if (null == namespace) {
	    return null;
	}
	return (UIComponent) namespace.get(name);
    }

    public synchronized String generateClientId() {
	String result = null;

	result = ID_PREFIX + Integer.toString(serialNumber++);
	return result;
    }


}
