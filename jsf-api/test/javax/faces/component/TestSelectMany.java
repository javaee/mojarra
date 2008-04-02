/*
 * $Id: TestSelectMany.java,v 1.1 2003/09/25 07:46:05 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.List;


/**
 * <p>Test {@link UISelectMany} subclass.</p>
 */

public class TestSelectMany extends UISelectMany {


    public boolean compareValues(Object previous, Object value) {
        return (super.compareValues(previous, value));
    }

}
