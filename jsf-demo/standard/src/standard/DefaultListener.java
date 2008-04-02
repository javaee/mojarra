/*
 * $Id: DefaultListener.java,v 1.1 2003/08/11 23:26:39 eburns Exp $
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


/**
 * Set a request parameter telling me which UIComponent was actuated
 *
 * @version $Id: DefaultListener.java,v 1.1 2003/08/11 23:26:39 eburns Exp $
 */

public class DefaultListener extends Object implements ActionListener {
    public static final String ACTUATED_COMPONENT = "standard_chosen";

    public PhaseId getPhaseId() {
	return PhaseId.PROCESS_VALIDATIONS;
    }
    
    public void processAction(ActionEvent event)
        throws AbortProcessingException {
	System.out.println("DefaultListener.processAction");
	// PENDING(edburns): make sure getComponentId() returns a copy
	// so we don't leak UIComponent instances.
	String id = event.getComponent().getComponentId();
	FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(ACTUATED_COMPONENT, id);
    }
    

}
