/*
 * $Id: UIPageBase.java,v 1.3 2003/07/29 16:38:04 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;

import javax.faces.component.UIPage;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import java.io.IOException;

public class UIPageBase extends UINamingContainerBase implements UIPage {

    // -------------------------------------------------------- Constructors

    public UIPageBase() { 
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

    private String treeId = null;

    public String getTreeId() {
	return treeId;
    }

    public void setTreeId(String newTreeId) {
	treeId = newTreeId;
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
	treeId = stateStr.substring(i + STATE_SEP_LEN);
	if (treeId.equals("null")) {
	    treeId = null;
	}

	super.restoreState(context, state[SUPER_INDEX]);
    }

    public Object getState(FacesContext context) {
	Object superState = super.getState(context);
	Object [] result = new Object[2];
	result[THIS_INDEX] = renderKitId + STATE_SEP + treeId;
	result[SUPER_INDEX] = superState;
	return result;
    }
    


}



	

    

