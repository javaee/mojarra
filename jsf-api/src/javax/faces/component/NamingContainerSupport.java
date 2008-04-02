/*
 * $Id: NamingContainerSupport.java,v 1.7 2003/09/30 17:04:58 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import java.util.HashMap;
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

public class NamingContainerSupport implements NamingContainer {


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

    /**
     * @exception IllegalArgumentException {@inheritDoc}
     * @exception IllegalStateException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}     
     */ 
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

    /**
     * @exception IllegalArgumentException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}
     */ 
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

    /**
     * @exception IllegalArgumentException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}     
     */ 
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


}
