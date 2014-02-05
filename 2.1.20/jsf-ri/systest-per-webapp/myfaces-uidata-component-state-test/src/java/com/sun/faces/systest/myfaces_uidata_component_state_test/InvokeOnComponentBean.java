/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.systest.myfaces_uidata_component_state_test;

import com.sun.faces.util.FacesLogger;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class InvokeOnComponentBean {

    private static Logger log = FacesLogger.APPLICATION_VIEW.getLogger();
	
	private String clientId;
	
	private String currentValue;

	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	int countInvoke = 0;
	
	public void invokeSetValueOnComponent(ActionEvent evt) {
	
		ContextCallback contextCallback = new LoadInputValue(); 
		
		FacesContext faces = FacesContext.getCurrentInstance();
		UIViewRoot root = faces.getViewRoot();

		boolean found = root.invokeOnComponent(faces, clientId, contextCallback);

		if (!found)
			this.setCurrentValue(clientId + " not found!");
		
		countInvoke++;
		System.out.println("COUNT:"+countInvoke);
	}

	
	public class LoadInputValue implements ContextCallback{
		
		public void invokeContextCallback(FacesContext ctx, UIComponent c) {
			
			if (c instanceof UIInput){
				UIInput input = (UIInput) c;
				//System.out.println("The currentValue:"+input.getValue());
				Object value = input.getValue();
				c.getAttributes().put("style", "background:red"); 				
				c.getAttributes().put("styleClass", "redbackground");
				if (value instanceof SimpleCity){
					currentValue = ((SimpleCity)value).toString();
				}else{
					currentValue = (String) input.getValue();
				}
			}else{
				//System.out.println("nocurrentValue:");
			}
		}
	}
}
