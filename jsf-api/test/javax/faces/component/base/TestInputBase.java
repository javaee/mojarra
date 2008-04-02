/*
 * $Id: TestInputBase.java,v 1.2 2003/07/26 17:55:20 craigmcc Exp $
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

    public List[] getListeners() {
        return (this.listeners);
    }


    public boolean compareValues(Object previous, Object value) {
        return (super.compareValues(previous, value));
    }


}
