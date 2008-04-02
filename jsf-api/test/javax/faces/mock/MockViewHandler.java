/*
 * $Id: MockViewHandler.java,v 1.13 2003/09/15 22:09:40 eburns Exp $
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
import javax.faces.component.base.UIViewRootBase;
import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager;


public class MockViewHandler extends Object implements ViewHandler {

    protected StateManager stateManager = null;

    public void renderView(FacesContext context, UIViewRoot viewToRender)
        throws IOException, FacesException {}

    public UIViewRoot restoreView(FacesContext context, String viewId) {
	return null;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
	UIViewRoot result = new UIViewRootBase();
	result.setViewId(viewId);
	return result;
    }

    public void writeState(FacesContext context) {}

    public StateManager getStateManager() {
	if (null == stateManager) {
	    stateManager = new StateManager() {
		    protected Object getTreeStructureToSave(FacesContext context) {
			return null;
		    }
		    protected Object getComponentStateToSave(FacesContext context) {
			return null;
		    }
		    public UIViewRoot restoreView(FacesContext context, String viewId) { return null; }
		    public SerializedView saveSerializedView(FacesContext context) {
			return null;
		    }

		    public void writeState(FacesContext context, 
					   SerializedView state) throws IOException {
                    }

		    protected UIViewRoot restoreTreeStructure(FacesContext context, 
							   String viewId) {
			return null;
		    }
		    protected void restoreComponentState(FacesContext context, UIViewRoot root) throws IOException {}
		};
	}
	return stateManager;
    }

}

		    



