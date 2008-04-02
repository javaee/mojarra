/*
 * $Id: MockLifecycle.java,v 1.1 2003/07/20 00:41:45 craigmcc Exp $
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
import javax.faces.lifecycle.ViewHandler;


public class MockLifecycle extends Lifecycle {


    // ------------------------------------------------------------- Properties


    private ViewHandler viewHandler = null;


    public ViewHandler getViewHandler() {
        return (this.viewHandler);
    }


    public void setViewHandler(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
    }


    // --------------------------------------------------------- Public Methods


    public void addPhaseListener(PhaseListener listener) {
        throw new UnsupportedOperationException();
    }


    public void execute(FacesContext context) throws FacesException {
        throw new UnsupportedOperationException();
    }


    public void removePhaseListener(PhaseListener listener) {
        throw new UnsupportedOperationException();
    }


}
