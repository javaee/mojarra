/*
 * $Id: PaneComponent.java,v 1.8 2003/09/26 20:02:00 horwat Exp $
 */

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package components.components;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.base.UIComponentBase;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Component designed to contain child components (and possibly other
 * layout in a JSP environment) for things like a tabbed pane control.
 */
public class PaneComponent extends UIComponentBase {


    private static Log log = LogFactory.getLog(PaneComponent.class);
    
    private boolean selected = false;


    // creates and adds a listener;
    public PaneComponent() {
        PaneSelectedListener listener = new PaneSelectedListener();
        addFacesListener(listener);    
    }


    // Does this component render its own children?
    public boolean getRendersChildren() {
        return (true);
    }


    // The currently selected state of this component
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    // Ignore update model requests
    public void updateModel(FacesContext context) {
    }

    /**
     * <p>Faces Listener implementation which sets the selected tab
     * component;</p>
     */
    public class PaneSelectedListener implements FacesListener, StateHolder {

        public PaneSelectedListener() {
        }

        // This listener will process events after the phase specified.

        public PhaseId getPhaseId() {
            return PhaseId.ANY_PHASE;
        }

        // process the event..

        public void processPaneSelectedEvent(FacesEvent event) {
            UIComponent source = event.getComponent();
            PaneSelectedEvent pevent = (PaneSelectedEvent)event;
            String id = pevent.getId();

            boolean paneSelected = false;

            // Find the parent tab control so we can set all tabs
            // to "unselected";
            UIComponent tabControl = findParentForRendererType(
                source, "Tabbed");
            int n = tabControl.getChildCount();
            for (int i = 0; i < n; i++) {
                PaneComponent pane = (PaneComponent)tabControl.getChildren().get(i);
                if (pane.getId().equals(id)) {
                    pane.setSelected(true);
                    paneSelected = true;
                } else {
                    pane.setSelected(false);
                }
            }
            
            if (!paneSelected) {
                log.warn("Cannot select pane for id=" + id + "," +
                     ", selecting first pane");
                ((PaneComponent) tabControl.getChildren().get(0)).setSelected(true);
            }
        }
        // methods from StateHolder
        public Object saveState(FacesContext context) {
            return null;
        }
        
        public void restoreState(FacesContext context, Object state) 
                throws IOException {
        }
        
        public void setTransient(boolean newTransientValue) {
        }
        
        public boolean isTransient() {
            return true;
        }
    }

    private UIComponent findParentForRendererType(
        UIComponent component, String rendererType) {
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
