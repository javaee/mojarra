/*
 * $Id: MockLifecycle.java,v 1.3 2003/10/30 23:04:59 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;


public class MockLifecycle extends Lifecycle {


    // ------------------------------------------------------------- Properties


    // --------------------------------------------------------- Public Methods


    public void addPhaseListener(PhaseListener listener) {
        throw new UnsupportedOperationException();
    }


    public void execute(FacesContext context) throws FacesException {
        throw new UnsupportedOperationException();
    }


    public PhaseListener[] getPhaseListeners() {
        throw new UnsupportedOperationException();
    }

    public void removePhaseListener(PhaseListener listener) {
        throw new UnsupportedOperationException();
    }


}
