/*
 * $Id: MockViewHandler.java,v 1.27 2005/08/22 22:08:27 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
        result.setRenderKitId(calculateRenderKitId(context));
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

		    



