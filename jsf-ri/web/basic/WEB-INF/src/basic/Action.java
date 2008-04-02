/*
 * $Id: Action.java,v 1.1 2003/01/21 23:23:32 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Action.java

package basic;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import java.io.IOException;

/**
 *
 *  <B>Action</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Action.java,v 1.1 2003/01/21 23:23:32 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Action implements ActionListener {
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

    public Action() {
    }

//
// Class methods
//

//
// General Methods
//

    
    // This listener will process events after the phase specified.
    
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    public void processAction(ActionEvent event) {
        System.out.println("Action.processAction : actionCommand : "+
            event.getActionCommand());

// Uncomment the following lines for a demonstration of event
// recursion;
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addFacesEvent(new ActionEvent(event.getComponent(), 
//            event.getActionCommand()));
    }

} // end of class Action
