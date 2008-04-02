/*
 * $Id: NamingContainerImpl.java,v 1.4 2002/12/23 22:59:34 eburns Exp $
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
	int i = 0;
	if (null == namespace) {
	    return null;
	}
	// If this is a simple name
	if (-1 == (i = name.indexOf(UIComponent.SEPARATOR_CHAR))) {
	    result = (UIComponent) namespace.get(name);
	}
	else {
	    // Make sure the SEPARATOR_CHAR is not the last char in name
	    if (name.length() == (i+1)) {
		throw new IllegalArgumentException(name);
	    }

	    String 
		first = name.substring(0, i),
		rest = name.substring(i+1);
	    NamingContainer namingContainerChild = null;
	    try {
		if (null != (namingContainerChild = (NamingContainer)
			     this.findComponentInNamespace(first))) {
		    result = 
			namingContainerChild.findComponentInNamespace(rest);
		}
	    }
	    catch (ClassCastException e) {
		throw new IllegalArgumentException(e.getMessage());
	    }
	}
	
	return result;
    }

    public synchronized String generateClientId() {
	String result = null;

	result = ID_PREFIX + Integer.toString(serialNumber++);
	return result;
    }


}
