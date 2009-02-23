
package com.sun.faces.systest.composite;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class LoginActionListener implements ActionListener {
 
	public void processAction(ActionEvent event)
			throws AbortProcessingException {
		FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().getRequestMap().put("message", 
                        "action listener has been called");
	}
}
