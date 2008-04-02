/*
 * $Id: UIViewRootBase.java,v 1.1 2003/08/22 14:03:15 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import java.io.IOException;

public class UIViewRootBase extends UINamingContainerBase implements UIViewRoot {

    // -------------------------------------------------------- Constructors

    public UIViewRootBase() { 
	setRendererType(null);
    }

    // ------------------------------------------------------------- Properties

    private String renderKitId = RenderKitFactory.DEFAULT_RENDER_KIT;

    public String getRenderKitId() {
	return renderKitId;
    }

    public void setRenderKitId(String newRenderKitId) {
	renderKitId = newRenderKitId;
    }

    private String viewId = null;

    public String getViewId() {
	return viewId;
    }

    public void setViewId(String newViewId) {
	viewId = newViewId;
    }

    // --------------------------------------------- methods from StateHolder

    public void restoreState(FacesContext context, 
			     Object stateObj) throws IOException {
	Object [] state = (Object []) stateObj;
	String stateStr = (String) state[THIS_INDEX];
	int i = stateStr.indexOf(STATE_SEP);
	renderKitId = stateStr.substring(0, i);
	if (renderKitId.equals("null")) {
	    renderKitId = null;
	}
	viewId = stateStr.substring(i + STATE_SEP_LEN);
	if (viewId.equals("null")) {
	    viewId = null;
	}

	super.restoreState(context, state[SUPER_INDEX]);
    }

    public Object getState(FacesContext context) {
	Object superState = super.getState(context);
	Object [] result = new Object[2];
	result[THIS_INDEX] = renderKitId + STATE_SEP + viewId;
	result[SUPER_INDEX] = superState;
	return result;
    }
    


}



	

    

