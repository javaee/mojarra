/*
 * $Id: GraphEvent.java,v 1.1 2003/03/27 19:43:30 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package components.components;

import javax.faces.event.FacesEvent;
/**
 * A custom event which indicates that a node in a Graph has been
 * expanded or collapased.
 */

public class GraphEvent extends FacesEvent {

    public GraphEvent(GraphComponent component, String path, boolean only) {
        super(component);
        this.path = path;
        this.only = only;
    }


    // The path of the node to be toggled
    private String path = null;

    public String getPath() {
        return (this.path);
    }


    // Should this be the only expanded node at the current level
    private boolean only = false;

    public boolean isOnly() {
        return (this.only);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("GraphEvent[compoundId=");
        sb.append(getComponent().getComponentId());
        sb.append(",path=");
        sb.append(path);
        sb.append(",only=");
        sb.append(only);
        sb.append("]");
        return (sb.toString());
    }

}
