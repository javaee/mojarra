/*
 * $Id: TestInputBase.java,v 1.3 2003/07/27 00:48:30 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.util.List;


/**
 * <p>Test {@link UIInputBase} subclass.</p>
 */

public class TestInputBase extends UIInputBase {


    public TestInputBase() {
        super();
    }

    public TestInputBase(String id) {
        setId(id);
    }

    /* PENDING(craigmcc) - listeners is private on UIComponentBase now
    public List[] getListeners() {
        return (this.listeners);
    }
    */

    public boolean compareValues(Object previous, Object value) {
        return (super.compareValues(previous, value));
    }


}
