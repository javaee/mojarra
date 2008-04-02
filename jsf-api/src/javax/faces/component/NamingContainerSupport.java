/*
 * $Id: NamingContainerSupport.java,v 1.3 2003/02/20 22:46:10 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;


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
 *       public void removeComponentFromNamespace(UIComponent component) {
 *           ncs.removeComponentFromNamespace(component);
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
 *   }
 * </pre>
 */

public class NamingContainerSupport implements NamingContainer, Serializable {


    // ----------------------------------------------------- Instance Variables


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
    private Map namespace = null;


    /**
     * <p>Serial number used to generate unique client identifiers.</p>
     */
    private int serialNumber = 0;


    // ------------------------------------------------ NamingContainer Methods


    public void addComponentToNamespace(UIComponent component) {

        // Validate our parameter
        if (component == null) {
            throw new NullPointerException();
        }
        String key = component.getComponentId();
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


    public void removeComponentFromNamespace(UIComponent component) {

        // Validate our parameter
        if (component == null) {
            throw new NullPointerException();
        }
        String key = component.getComponentId();
        if (key == null) {
            throw new IllegalArgumentException();
        }

        // Remove this component if it is present
        if (namespace != null) {
            namespace.remove(key);
        }

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


}
