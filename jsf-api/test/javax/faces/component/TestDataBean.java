/*
 * $Id: TestDataBean.java,v 1.1 2003/10/21 23:58:19 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
