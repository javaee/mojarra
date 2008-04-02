/*
 * $Id: TestSelectMany.java,v 1.2 2004/02/04 23:38:44 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
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
