/*
 * $Id: TestEvent.java,v 1.3 2004/02/04 23:38:43 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class TestEvent extends FacesEvent {

    public TestEvent(UIComponent component) {
        this(component, null);
    }

    public TestEvent(UIComponent component, String id) {
        super(component);
        this.id = id;
    }

    private String id;

    public String getId() {
        return (this.id);
    }

    public  boolean isAppropriateListener(FacesListener listener) {
        return (listener instanceof TestListener);
    }

    public void processListener(FacesListener listener) {
        ((TestListener) listener).processTest(this);
    }


}
