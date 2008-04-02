/*
 * $Id: MockViewHandler.java,v 1.25 2004/02/26 20:31:55 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;

import java.util.Locale;
import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager;
import javax.faces.render.RenderKitFactory;

public class MockViewHandler extends ViewHandler {

    protected StateManager stateManager = null;

    public void renderView(FacesContext context, UIViewRoot viewToRender)
        throws IOException, FacesException {}

    public UIViewRoot restoreView(FacesContext context, String viewId) {
	return null;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
	UIViewRoot result = new UIViewRoot();
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
		    public UIViewRoot restoreView(FacesContext context, String viewId, String renderKitId) { return null; }
		    public SerializedView saveSerializedView(FacesContext context) {
			return null;
		    }

		    public void writeState(FacesContext context, 
					   SerializedView state) throws IOException {
                    }

		    protected UIViewRoot restoreTreeStructure(FacesContext context, 
							   String viewId, String renderKitId) {
			return null;
		    }
		    protected void restoreComponentState(FacesContext context, UIViewRoot root, String renderKitId) {}
		};
	}
	return stateManager;
    }

    public String getActionURL(FacesContext context, String viewId) {
        throw new UnsupportedOperationException();
    }

    public String getResourceURL(FacesContext context, String path) {
        if (path.startsWith("/")) {
            return context.getExternalContext().getRequestContextPath() + path;
        } else {
            return (path);
        }
    }

    public Locale calculateLocale(FacesContext context) {
        return Locale.getDefault();
    }

    public String calculateRenderKitId(FacesContext context) {
        return RenderKitFactory.HTML_BASIC_RENDER_KIT;
    }

}

		    



