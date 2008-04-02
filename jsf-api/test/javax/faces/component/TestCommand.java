/*
 * $Id: TestCommand.java,v 1.3 2004/02/26 20:31:26 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.List;


/**
 * <p>Test {@link UICommand} subclass.</p>
 */

public class TestCommand extends UICommand {


    public TestCommand() {
        super();
    }

    public TestCommand(String id) {
        setId(id);
    }

    /* PENDING(craigmcc) - listeners is private on UIComponentBase now
    public List[] getListeners() {
        return (this.listeners);
    }
    */


}
