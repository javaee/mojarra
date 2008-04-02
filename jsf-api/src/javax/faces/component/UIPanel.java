/*
 * $Id: UIPanel.java,v 1.8 2003/01/16 20:47:57 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UIPanel</strong> is a {@link UIComponent} that manages the
 * layout of its child components.</p>
 */

public class UIPanel extends UIOutput implements NamingContainer {


    // ------------------------------------------------------- attributes

    /**

    * The NamingContainer implementation

    */
    private NamingContainer namespace = null;

    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UIPanel";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return a flag indicating whether this component is responsible
     * for rendering its child components.</p>
     */
    public boolean getRendersChildren() {

        return (true);

    }


    // ------------------------------------------- Lifecycle Processing Methods

    // ------------------------------------------- Constructors

    public UIPanel() {
	namespace = new NamingContainerImpl(this);
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
