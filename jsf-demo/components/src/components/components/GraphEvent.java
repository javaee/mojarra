/*
 * $Id: GraphEvent.java,v 1.4 2003/08/28 20:11:04 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package components.components;


import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;


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
        StringBuffer sb = new StringBuffer("GraphEvent[id=");
        sb.append(getComponent().getId());
        sb.append(",path=");
        sb.append(path);
        sb.append(",only=");
        sb.append(only);
        sb.append("]");
        return (sb.toString());
    }


    public boolean isAppropriateListener(FacesListener listener) { 
	return (listener instanceof GraphComponent.GraphListener);
    }
    public void processListener(FacesListener listener) { 
	((GraphComponent.GraphListener)listener).processGraphEvent(this);
    }



}
