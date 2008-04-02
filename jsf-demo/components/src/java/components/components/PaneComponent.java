/*
 * $Id: PaneComponent.java,v 1.3 2005/08/22 22:08:51 ofung Exp $
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
