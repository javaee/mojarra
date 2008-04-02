/*
 * $Id: DefaultListener.java,v 1.4 2004/02/05 16:25:03 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package standard;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;


/**
 * Set a request parameter telling me which UIComponent was actuated
 *
 * @version $Id: DefaultListener.java,v 1.4 2004/02/05 16:25:03 rlubke Exp $
 */

public class DefaultListener extends Object implements ActionListener {

    public void processAction(ActionEvent event)
        throws AbortProcessingException {
        System.out.println("DefaultListener.processAction");
        // PENDING(edburns): make sure getComponentId() returns a copy
        // so we don't leak UIComponent instances.
        String id = event.getComponent().getId();
        FacesContext context = FacesContext.getCurrentInstance();
        ValueBinding vb = context.getApplication().createValueBinding(
            "#{model.hasComponent}");
        vb.setValue(context, "true");
        vb =
            context.getApplication().createValueBinding(
                "#{model.whichComponent}");
        vb.setValue(context, id);
    }


}
