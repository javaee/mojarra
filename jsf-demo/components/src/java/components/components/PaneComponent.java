/*
 * $Id: PaneComponent.java,v 1.2.36.2 2007/04/27 21:27:12 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package components.components;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

import java.util.Iterator;

/**
 * <p>Component designed to contain child components (and possibly other
 * layout in a JSP environment) for things like a tabbed pane control.
 */
public class PaneComponent extends UIComponentBase {


    private static Log log = LogFactory.getLog(PaneComponent.class);


    // creates and adds a listener;
    public PaneComponent() {
        PaneSelectedListener listener = new PaneSelectedListener();
        addFacesListener(listener);
    }


    /**
     * <p>Return the component family for this component.
     */
    public String getFamily() {

        return ("Pane");

    }


    // Does this component render its own children?
    public boolean getRendersChildren() {
        return (true);
    }


    public void processDecodes(FacesContext context) {
        // Process all facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processDecodes(context);
        }

        // Process this component itself
        try {
            decode(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }
    }


    // Ignore update model requests
    public void updateModel(FacesContext context) {
    }


    /**
     * <p>Faces Listener implementation which sets the selected tab
     * component
     */
    public class PaneSelectedListener implements FacesListener, StateHolder {

        public PaneSelectedListener() {
        }

        // process the event..

        public void processPaneSelectedEvent(FacesEvent event) {
            UIComponent source = event.getComponent();
            PaneSelectedEvent pevent = (PaneSelectedEvent) event;
            String id = pevent.getId();

            boolean paneSelected = false;

            // Find the parent tab control so we can set all tabs
            // to "unselected";
            UIComponent tabControl = findParentForRendererType(source,
                                                               "Tabbed");
            int n = tabControl.getChildCount();
            for (int i = 0; i < n; i++) {
                PaneComponent pane = (PaneComponent) tabControl.getChildren()
                    .get(i);
                if (pane.getId().equals(id)) {
                    pane.setRendered(true);
                    paneSelected = true;
                } else {
                    pane.setRendered(false);
                }
            }

            if (!paneSelected) {
                log.warn("Cannot select pane for id=" + id + "," +
                         ", selecting first pane");
                ((PaneComponent) tabControl.getChildren().get(0)).setRendered(
                    true);
            }
        }


        // methods from StateHolder
        public Object saveState(FacesContext context) {
            return null;
        }


        public void restoreState(FacesContext context, Object state) {
        }


        public void setTransient(boolean newTransientValue) {
        }


        public boolean isTransient() {
            return true;
        }
    }


    private UIComponent findParentForRendererType(UIComponent component, String rendererType) {
        Object facetParent = null;
        UIComponent currentComponent = component;
        
        // Search for an ancestor that is the specified renderer type;
        // search includes the facets.
        while (null != (currentComponent = currentComponent.getParent())) {
            if (currentComponent.getRendererType().equals(rendererType)) {
                break;
            }
        }
        return currentComponent;
    }


}
