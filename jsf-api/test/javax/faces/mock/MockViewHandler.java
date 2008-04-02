/*
 * $Id: MockViewHandler.java,v 1.5 2003/08/22 14:03:30 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;

import java.io.IOException;
import java.io.Reader;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager;


public class MockViewHandler extends Object implements ViewHandler {

    protected StateManager stateManager = null;

    public void renderView(FacesContext context)
        throws IOException, FacesException {}

    public UIViewRoot restoreView(FacesContext context, String viewId) {
	return null;
    }

    public StateManager getStateManager() {
	if (null == stateManager) {
	    stateManager = new StateManager() {
		    protected Object getTreeStructureToSave(FacesContext context) {
			return null;
		    }
		    protected Object getComponentStateToSave(FacesContext context) {
			return null;
		    }
		    public boolean getView(FacesContext context, String viewId) throws IOException { return true; }
		    public void writeStateMarker(FacesContext context) throws IOException {}
		    public void saveView(FacesContext context, Reader content, 
			SerializedView state) {}
		    protected boolean restoreTreeStructure(FacesContext context, 
							   String viewId) {
			return false;
		    }
		    protected void restoreComponentState(FacesContext context) throws IOException {}
		};
	}
	return stateManager;
    }

}

		    



