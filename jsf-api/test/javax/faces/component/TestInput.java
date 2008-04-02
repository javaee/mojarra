/*
 * $Id: TestInput.java,v 1.1 2003/09/25 07:46:04 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.List;


/**
 * <p>Test {@link UIInput} subclass.</p>
 */

public class TestInput extends UIInput {


    public TestInput() {
        super();
    }

    public TestInput(String id) {
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
