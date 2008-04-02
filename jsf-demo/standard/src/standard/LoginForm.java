/*
 * $Id: LoginForm.java,v 1.8 2005/12/14 22:27:46 rlubke Exp $
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


import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;


/** <p>Backing Bean for a username and password login form.</p> */

public class LoginForm {

    // -------------------------------------------------------------- Components


    private UIInput password = null;


    public UIInput getPassword() {
        return (this.password);
    }


    public void setPassword(UIInput password) {
        this.password = password;
    }


    private UIInput username = null;


    public UIInput getUsername() {
        return (this.username);
    }


    public void setUsername(UIInput username) {
        this.username = username;
    }

    // ---------------------------------------------------------------- Handlers


    // Validate the attempted login
    public String login() {

        String username = (String) this.username.getValue();
        String password = (String) this.password.getValue();
        if ("user".equals(username) && "pass".equals(password)) {
            return ("success");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage
                  (this.username.getClientId(context),
                   new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Invalid username or password, please retry",
                                    null));
            return (null);
        }

    }


}
