/*
 * $Id: TabRenderer.java,v 1.2 2005/08/22 22:08:54 ofung Exp $
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

package components.renderkit;


import components.components.PaneComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import java.io.IOException;
import java.util.Iterator;


/**
 * <p>Render the individual {@link PaneComponent} and its children, but
 * <strong>only</strong> if this {@link PaneComponent} is currently
 * selected.  Otherwise, no output at all is sent.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - Because of the fact that we
 * want standard JSP text (not nested inside Faces components) to be usable
 * on a pane, this Renderer needs to know whether it is being used in a JSP
 * environment (where the rendering of the child components writes to the
 * local value of our Pane component) or not (where we must do it ourselves).
 * This is resolved by having the <code>Pane_Tab</code> tag set a
 * render dependent attribute named "demo.renderer.TabRenderer.JSP" when it
 * creates the corresponding component, so that we can tell what is going on.
 * </p>
 */

public class TabRenderer extends BaseRenderer {


    private static Log log = LogFactory.getLog(TabRenderer.class);


    public void decode(FacesContext context, UIComponent component) {
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("encodeBegin(" + component.getId() + ")");
        }

    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("encodeChildren(" + component.getId() + ")");
        }

    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("encodeEnd(" + component.getId() + ")");
        }

        // Render our children only -- our parent has rendered ourself
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            encodeRecursive(context, kid);
        }

    }


}
