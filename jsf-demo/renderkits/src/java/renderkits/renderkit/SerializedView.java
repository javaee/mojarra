/*
 * $Id: SerializedView.java,v 1.1 2005/08/17 18:42:42 rogerk Exp $
 */
                                                                                                                           
/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package renderkits.renderkit;

import java.io.Serializable;
 
/**
 * <p>Convenience struct for encapsulating tree structure and
 * component state.</p> 
 */
public class SerializedView extends Object implements Serializable {
    private Object structure = null;
    private Object state = null;
                                                                                                                           
    public SerializedView(Object newStructure, Object newState) {
        structure = newStructure;
        state = newState;
    }
                                                                                                                           
    public Object getStructure() {
        return structure;
    }
                                                                                                                           
    public Object getState() {
        return state;
    }
}

