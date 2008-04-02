/*
 * $Id: TestEvent.java,v 1.1 2003/07/27 00:48:29 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class TestEvent extends FacesEvent {

    public TestEvent(UIComponent component) {
        super(component);
    }

    public  boolean isAppropriateListener(FacesListener listener) {
        return (listener instanceof TestListener);
    }

    public void processListener(FacesListener listener) {
        ((TestListener) listener).processTest(this);
    }


}
