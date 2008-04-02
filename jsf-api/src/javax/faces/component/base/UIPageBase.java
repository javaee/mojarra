/*
 * $Id: UIPageBase.java,v 1.1 2003/07/28 22:18:46 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;

import javax.faces.component.UIPage;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class UIPageBase extends UINamingContainerBase implements UIPage {

    // ------------------------------------------------------------- Properties

    private String renderKitId;

    public String getRenderKitId() {
	return renderKitId;
    }

    public void setRenderKitId(String newRenderKitId) {
	renderKitId = newRenderKitId;
    }

    private String treeId;

    public String getTreeId() {
	return treeId;
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
    

    // -------------------------------------------------------- Constructors

    public UIPageBase(String newTreeId) {
	treeId = newTreeId;
        setRendererType(null);

    }

}



	

    

