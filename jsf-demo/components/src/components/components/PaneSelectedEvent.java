/*
 * $Id: PaneSelectedEvent.java,v 1.1 2003/02/15 00:56:41 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package components.components;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;

/**
 * A custom event which indicates the currently selected pane
 * in a tabbed pane control.
 */
public class PaneSelectedEvent extends FacesEvent {

    // The component id of the newly selected child pane
    private String id = null;

    public PaneSelectedEvent(UIComponent component, String id) {
        super(component);
        this.id = id;
    }

    public String getId() {
        return (this.id);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("PaneSelectedEvent[compoundId=");
        sb.append(getComponent().getComponentId());
        sb.append(",id=");
        sb.append(id);
        sb.append("]");
        return (sb.toString());
    }

}
