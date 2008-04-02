/*
 * $Id: SelectManyUnregistered.java,v 1.2 2004/02/06 18:39:58 craigmcc Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package standard;


import java.io.Serializable;


/**
 * <p>Test bean for valid options of a SelectMany that are not strings,
 * and for which no converter has been registered.</p>
 */

public class SelectManyUnregistered implements Serializable {

    public SelectManyUnregistered() {
        this("NONE");
    }

    public SelectManyUnregistered(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return (this.name);
    }

    public boolean equals(Object o) {
        if (o instanceof SelectManyUnregistered) {
            return (getName().equals(((SelectManyUnregistered) o).getName()));
        } else {
            return (false);
        }
    }

    public String toString() {
        return (getName());
    }


}
