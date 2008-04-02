/*
 * $Id: LogValueChangedListener.java,v 1.7 2005/12/14 22:27:46 rlubke Exp $
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
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;


/** <p>Log the occurrence of this event.</p> */

public class LogValueChangedListener implements ValueChangeListener {

    // --------------------------------------------- ValueChangeListener Methods

    /**
     * <p>Log the event.</p>
     *
     * @param event {@link ValueChangeEvent} that is being processed
     */
    public void processValueChange(ValueChangeEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        append(context, "ValueChangeEvent(" +
                        event.getComponent().getClientId(context) + "," +
                        event.getOldValue() + "," + event.getNewValue() + ")");

    }


    private void append(FacesContext context, String value) {

        String message = (String)
              context.getExternalContext().getRequestMap().get("message");
        if (message == null) {
            message = "";
        }
        message += "<li>" + value + "</li>";
        context.getExternalContext().getRequestMap().put("message", message);

    }


}
