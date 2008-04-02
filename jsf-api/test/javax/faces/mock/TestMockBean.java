/*
 * $Id: TestMockBean.java,v 1.3 2004/02/26 20:31:55 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.io.Serializable;


// Test JavaBean for Mock Tests
public class TestMockBean implements Serializable {

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

    public String combine() {
        return ((command == null ? "" : command) + ":" +
                (input == null ? "" : input) + ":" +
                (output == null ? "" : output));
    }

}
