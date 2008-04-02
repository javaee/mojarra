/*
 * $Id: MockStateManager.java,v 1.5 2004/02/04 23:39:15 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.application.StateManager;


public class MockStateManager extends StateManager {
    protected Object getTreeStructureToSave(FacesContext context) {
        return null;
    }

    protected Object getComponentStateToSave(FacesContext context) {
        return null;
    }
  
    public UIViewRoot restoreView(FacesContext context, String viewId, 
				  String renderKitId)
    { return null; }
	
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
	
    protected void restoreComponentState(FacesContext context, UIViewRoot root, String renderKitId)
    {}

}
