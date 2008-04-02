/*
 * $Id: TestSelectManyBase.java,v 1.2 2003/07/26 17:55:20 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.util.List;


/**
 * <p>Test {@link UISelectManyBase} subclass.</p>
 */

public class TestSelectManyBase extends UISelectManyBase {


    public boolean compareValues(Object previous, Object value) {
        return (super.compareValues(previous, value));
    }

}
