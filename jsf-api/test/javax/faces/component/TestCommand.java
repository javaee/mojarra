/*
 * $Id: TestCommand.java,v 1.1 2003/09/25 07:46:03 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
