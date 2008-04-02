/*
 * $Id: UpdateModelValuesPhase.java,v 1.19 2003/03/19 21:16:30 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// UpdateModelValuesPhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.context.FacesContextImpl;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.context.MessageResources;
import javax.faces.context.Message;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

import java.util.Iterator;


/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: UpdateModelValuesPhase.java,v 1.19 2003/03/19 21:16:30 jvisvanathan Exp $
 * 
 */

public class UpdateModelValuesPhase extends Phase {
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
// Constructors and Genericializers    
//

public UpdateModelValuesPhase() {
}

//
// Class methods
//

//
// General Methods
//

//
// Methods from Phase
//

public int getId() {
    return Phase.UPDATE_MODEL_VALUES;
}

public void execute(FacesContext facesContext) throws FacesException
{
    Iterator messageIter = null;

    UIComponent component = 
        (UIComponent)facesContext.getTree().getRoot();
    Assert.assert_it(null != component);
    
    try {
        component.processUpdates(facesContext);
    } catch (Throwable e) {
        Object[] params = new Object[3];
        UIOutput uiOutput = null;
        if ( component instanceof UIOutput) {
            uiOutput= (UIOutput) component;
            params[0] = uiOutput.getValue();
            params[1] = uiOutput.getValueRef();
        }  
        params[2] = e.getMessage();
        MessageResources resources = Util.getMessageResources();
        Assert.assert_it( resources != null );
        Message msg = resources.getMessage(facesContext,
            Util.MODEL_UPDATE_ERROR_MESSAGE_ID,params);
        facesContext.addMessage(component, msg);
    }

    messageIter = facesContext.getMessages();
    Assert.assert_it(null != messageIter);

    if (messageIter.hasNext()) {
        // Proceed based on the number of errors present
        facesContext.renderResponse();
    }

}



// The testcase for this class is TestUpdateModelValuesPhase.java


} // end of class UpdateModelValuesPhase
