/*
 * $Id: UIViewRootBase.java,v 1.3 2003/09/15 20:17:26 eburns Exp $
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


    // -------------------------------------------------------------- Properties


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


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = renderKitId;
        values[2] = viewId;
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        renderKitId = (String) values[1];
        viewId = (String) values[2];

    }


}
