/*
 * $Id: ValueChange.java,v 1.2 2003/02/20 22:50:39 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ValueChange.java

package basic;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;

import java.io.IOException;

/**
 *
 *  <B>ValueChange</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ValueChange.java,v 1.2 2003/02/20 22:50:39 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ValueChange implements ValueChangedListener {
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public ValueChange() {
    }

//
// Class methods
//

//
// General Methods
//

    
    // This listener will handle events after the phase specified
    // as the return value;

    public PhaseId getPhaseId() {
        return PhaseId.PROCESS_VALIDATIONS;
    }

    public void processValueChanged(ValueChangedEvent event) {
        String oldValue, newValue = null;
        System.out.println("ValueChange.processValueChanged : "+
            "source : "+event.getComponent().getComponentId());
        if (event.getOldValue() instanceof String) {
            oldValue = (String)event.getOldValue();
            System.out.println("Old Value : "+oldValue);
        }
        if (event.getNewValue() instanceof String) {
            newValue = (String)event.getNewValue();
            System.out.println("New Value : "+newValue);
        }
     
// Uncomment the following lines for a demonstration of event
// recursion;
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addFacesEvent(new ValueChangedEvent(event.getComponent(),
//            "foo", "bar"));
    }

} // end of class ValueChange
