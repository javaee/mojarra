/*
 * $Id: DefaultListener.java,v 1.3 2003/12/17 15:19:57 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package standard;

import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;
import javax.faces.event.AbortProcessingException;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;


/**
 * Set a request parameter telling me which UIComponent was actuated
 *
 * @version $Id: DefaultListener.java,v 1.3 2003/12/17 15:19:57 rkitain Exp $
 */

public class DefaultListener extends Object implements ActionListener {

    public void processAction(ActionEvent event)
        throws AbortProcessingException {
	System.out.println("DefaultListener.processAction");
	// PENDING(edburns): make sure getComponentId() returns a copy
	// so we don't leak UIComponent instances.
	String id = event.getComponent().getId();
	FacesContext context = FacesContext.getCurrentInstance();
	ValueBinding vb = context.getApplication().createValueBinding("#{model.hasComponent}");
	vb.setValue(context, "true");
	vb = context.getApplication().createValueBinding("#{model.whichComponent}");
	vb.setValue(context, id);
    }
    

}
