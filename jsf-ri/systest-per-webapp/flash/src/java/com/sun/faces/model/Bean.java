
/*
 * $Id: ELFlash.java,v 1.6 2005/12/16 21:32:36 edburns Exp $
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

package com.sun.faces.model;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ComponentSystemEvent;

@RequestScoped
@ManagedBean(name="bean")
public class Bean {
    
    protected String stringVal;

    private Long selectedEventId;

    public void loadTrainingEvent(ComponentSystemEvent cse) {
        Long eventId = getSelectedEventId();
        FacesContext context = FacesContext.getCurrentInstance();
        if (null == eventId) {
            context.addMessage(null,
                    new FacesMessage("The training event you requested is invalid"));
            context.getExternalContext().getFlash().setKeepMessages(true);
            context.getApplication().getNavigationHandler().
                    handleNavigation(context, null, "/index?faces-redirect=true");
        }
    }

    public String getStringVal() {
        return stringVal;
    }

    public void setStringVal(String stringVal) {
        this.stringVal = stringVal;
        
        if (null != stringVal && stringVal.equals("addMessage")) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "test that this persists across the redirect", 
                    "This message must persist across the redirect");
            context.addMessage(null, message);
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
    }

    public Long getSelectedEventId() {
        return selectedEventId;
    }

    public void setSelectedEventId(Long selectedEventId) {
        this.selectedEventId = selectedEventId;
    }

}
