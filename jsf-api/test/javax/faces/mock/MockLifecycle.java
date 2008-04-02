/*
 * $Id: MockLifecycle.java,v 1.5 2004/01/27 20:30:20 craigmcc Exp $
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


    public void render(FacesContext context) throws FacesException {
        throw new UnsupportedOperationException();
    }


}
