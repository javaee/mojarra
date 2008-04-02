/*
 * $Id: TestCommandBase.java,v 1.2 2003/07/26 17:55:19 craigmcc Exp $
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

    public List[] getListeners() {
        return (this.listeners);
    }


}
