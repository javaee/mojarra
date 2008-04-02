/*
 * $Id: DefaultListener.java,v 1.6 2005/08/22 22:09:42 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
 * @version $Id: DefaultListener.java,v 1.6 2005/08/22 22:09:42 ofung Exp $
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
