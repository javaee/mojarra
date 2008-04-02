/*
 * $Id: TestCommandBase.java,v 1.3 2003/07/27 00:48:29 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.util.List;


/**
 * <p>Test {@link UICommandBase} subclass.</p>
 */

public class TestCommandBase extends UICommandBase {


    public TestCommandBase() {
        super();
    }

    public TestCommandBase(String id) {
        setId(id);
    }

    /* PENDING(craigmcc) - listeners is private on UIComponentBase now
    public List[] getListeners() {
        return (this.listeners);
    }
    */


}
