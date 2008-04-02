/*
 * $Id: TestDataBean.java,v 1.4 2005/08/22 22:08:15 ofung Exp $
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

package javax.faces.component;


import java.io.Serializable;


// Test JavaBean for DataMode Tests
public class TestDataBean implements Serializable {

    private String command;
    public String getCommand() {
        return (this.command);
    }
    public void setCommand(String command) {
        this.command = command;
    }

    private String input;
    public String getInput() {
        return (this.input);
    }
    public void setInput(String input) {
        this.input = input;
    }

    private String output;
    public String getOutput() {
        return (this.output);
    }
    public void setOutput(String output) {
        this.output = output;
    }

}
