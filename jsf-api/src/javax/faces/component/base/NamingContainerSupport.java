/*
 * $Id: NamingContainerSupport.java,v 1.4 2003/07/31 12:22:20 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;

import javax.faces.context.FacesContext;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.StateHolder;
import java.io.IOException;
import java.util.Iterator;


/**
 * <p><strong>NamingContainerSupport</strong> is a utility class that may be
 * utilized by {@link UIComponent}s that implement {@link NamingContainer} to
 * delegate naming container management methods.</p>
 *
 * <p>Typical usage in a {@link UIComponent} implementation
 * class would be:</p>
 * <pre>
 *   public class MyComponent extends UIComponentBase
 *     implements NamingContainer {
 *
 *       private NamingContainerSupport ncs = new NamingContainerSupport();
 *
 *       ...
 *
 *       public void addComponentToNamespace(UIComponent component) {
 *           ncs.addComponentToNamespace(component);
 *       }
 *
 *       public UIComponent findComponentInNamespace(String name) {
 *           return ncs.findComponentInNamespace(name);
 *       }
 *
 *       public String generateClientId() {
 *           return ncs.generateClientId();
 *       }
 *
 *       public void removeComponentFromNamespace(UIComponent component) {
 *           ncs.removeComponentFromNamespace(component);
 *       }
 *
 *   }
 * </pre>
 */

public class NamingContainerSupport implements NamingContainer, StateHolder, Serializable {


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Base string for generating unique client identifiers.</p>
     */
    private static String ID_PREFIX = "id";


    /**
     * <p>The map for the namespace of this naming container.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - This is lazily
     * allocated, initialized, and populated.</p>
     */
    private HashMap namespace = null;


    /**
     * <p>Serial number used to generate unique client identifiers.</p>
     */
    private int serialNumber = 0;


    // ------------------------------------------------- NamingContainer Methods


    public void addComponentToNamespace(UIComponent component) {

        // Validate our parameter
        if (component == null) {
            throw new NullPointerException();
        }
        String key = component.getId();
        if (key == null) {
            throw new IllegalArgumentException();
        }

        // Allocate our local storage if necessary
	if (null == namespace) {
	    namespace = new HashMap();
	}

        // Add this component if the key is unique
	if (namespace.containsKey(key)) {
	    throw new IllegalStateException(key);
	}
	namespace.put(key, component);

    }


    public UIComponent findComponentInNamespace(String name) {

        if ((name == null) || (name.length() < 1)) {
            throw new NullPointerException();
        }
        if (namespace == null) {
            return (null);
        }

        int i = name.indexOf(UIComponent.SEPARATOR_CHAR);
        if ((i == 0) || (i == (name.length() - 1))) {
            throw new IllegalArgumentException(name);
        }
        if (i < 0) {
            return ((UIComponent) namespace.get(name));
        } else {
            UIComponent child =
                (UIComponent) namespace.get(name.substring(0, i));
            if (child == null) {
                return (null);
            }
            if (!(child instanceof NamingContainer)) {
                throw new IllegalArgumentException(name);
            }
            String rest = name.substring(i + 1);
            return (((NamingContainer) child).findComponentInNamespace(rest));
        }

    }


    public String generateClientId() {

        while (true) {
            String result = ID_PREFIX + (serialNumber++);
            if ((namespace == null) ||
                !namespace.containsKey(result)) {
                return (result);
            }
        }

    }

    public boolean equals(Object otherObj) {
	if (!(otherObj instanceof NamingContainerSupport)) {
	    return false;
	}
	NamingContainerSupport other = (NamingContainerSupport) otherObj;
	if (other.serialNumber != serialNumber) {
	    return false;
	}
	Iterator iter = other.namespace.keySet().iterator();
	Object 
	    val1 = null,
	    val2 = null,
	    cur = null;
	
	while (iter.hasNext()) {
	    cur = iter.next();
	    val1 = namespace.get(cur);
	    val2 = other.namespace.get(cur);
	    if ((val1 == null && val2 != null) || 
		(val1 != null && val2 == null)) {
		return false;
	    }
	    if (!val1.equals(val2)) {
		return false;
	    }
	}
	iter = namespace.keySet().iterator();
	while (iter.hasNext()) {
	    cur = iter.next();
	    val1 = namespace.get(cur);
	    val2 = other.namespace.get(cur);
	    if ((val1 == null && val2 != null) || 
		(val1 != null && val2 == null)) {
		return false;
	    }
	    if (!val1.equals(val2)) {
		return false;
	    }
	}
	return true;
    }

    public void removeComponentFromNamespace(UIComponent component) {

        // Validate our parameter
        if (component == null) {
            throw new NullPointerException();
        }
        String key = component.getId();
        if (key == null) {
            throw new IllegalArgumentException();
        }

        // Remove this component if it is present
        if (namespace != null) {
            namespace.remove(key);
        }

    }

    // ---------------------------------------------- methods from StateHolder

    public Object getState(FacesContext context) {
	Object [] state = new Object[2];
	state[SERIAL_INDEX] = "" + serialNumber;
	if (null != namespace) {
	    state[NAMESPACE_INDEX] = namespace.clone();
	}
	return state;
    }

    private static final int SERIAL_INDEX = 0;
    private static final int NAMESPACE_INDEX = 1;

    public void restoreState(FacesContext context, Object stateObj) {
	Object [] state = (Object []) stateObj;
	serialNumber = Integer.valueOf((String)state[SERIAL_INDEX]).intValue();
	if (null != namespace) {
	    namespace.clear();
	}
	if (null != state[NAMESPACE_INDEX]) {
	    if (null == namespace) {
		namespace = new HashMap();
	    }
	    namespace.putAll((Map) state[NAMESPACE_INDEX]);
	}
    }

    public boolean isTransient() { return false;
    }

    public void setTransient(boolean newT) {}

}
