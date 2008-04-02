/*
 * $Id: UINamingContainerBase.java,v 1.3 2003/07/28 22:18:46 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import java.io.IOException;


/**
 * <p><strong>UINamingContainer</strong> is a convenience base class for
 * components that wish to implement {@link NamingContainer} functionality.</p>
 */

public class UINamingContainerBase extends UIComponentBase
    implements NamingContainer {


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link NamingContainer} implementation that we delegate to
     */
    protected NamingContainerSupport namespace = new NamingContainerSupport();


    // ------------------------------------------------- NamingContainer Methods


    public void addComponentToNamespace(UIComponent namedComponent) {
	namespace.addComponentToNamespace(namedComponent);
    }


    public UIComponent findComponentInNamespace(String name) {
	return namespace.findComponentInNamespace(name);
    }


    public synchronized String generateClientId() {
	return namespace.generateClientId();
    }

    public void removeComponentFromNamespace(UIComponent namedComponent) {
	namespace.removeComponentFromNamespace(namedComponent);
    }

    // ---------------------------------------------- methods from StateHolder

    public void restoreState(FacesContext context, 
			     Object stateObj) throws IOException {
	Object [] state = (Object []) stateObj;
	namespace.restoreState(context, state[THIS_INDEX]);
	super.restoreState(context, state[SUPER_INDEX]);
    }

    public Object getState(FacesContext context) {
	// get the state of our superclasses.
	Object superState = super.getState(context);
	Object [] result = new Object[2];
	result[THIS_INDEX] = namespace.getState(context);
	// save the state of our superclass
	result[SUPER_INDEX] = superState;
	return result;
    }    


}
