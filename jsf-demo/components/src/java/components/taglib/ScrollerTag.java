/*
 * $Id: ScrollerTag.java,v 1.3 2005/08/22 22:08:58 ofung Exp $
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

package components.taglib;

import components.components.ScrollerComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.webapp.UIComponentTag;

/**
 * <p> ScrollerTag is the tag handler class for <code>ScrollerComponent.
 */
public class ScrollerTag extends UIComponentTag {

    protected String actionListener = null;
    protected String navFacetOrientation = null;
    protected String forValue = null;


    /**
     * method reference to handle an action event generated as a result of
     * clicking on a link that points a particular page in the result-set.
     */
    public void setActionListener(String actionListener) {
        this.actionListener = actionListener;
    }


    /*
     * When rendering a widget representing "page navigation" where
     * should the facet markup be rendered in relation to the page
     * navigation widget?  Values are "NORTH", "SOUTH", "EAST", "WEST".
     * Case insensitive. This can be value or a value binding 
     * reference expression.
     */
    public void setNavFacetOrientation(String navFacetOrientation) {
        this.navFacetOrientation = navFacetOrientation;
    }


    /*
     * The data grid component for which this acts as a scroller.
     * This can be value or a value binding reference expression.
     */
    public void setFor(String newForValue) {
        forValue = newForValue;
    }


    public String getComponentType() {
        return ("Scroller");
    }


    public String getRendererType() {
        return (null);
    }


    public void release() {
        super.release();
        this.navFacetOrientation = null;
    }


    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        FacesContext context = FacesContext.getCurrentInstance();
        ValueBinding vb = null;

        if (actionListener != null) {
            if (isValueReference(actionListener)) {
                Class args[] = {ActionEvent.class};
                MethodBinding mb = FacesContext.getCurrentInstance()
                    .getApplication()
                    .createMethodBinding(actionListener, args);
                ((ScrollerComponent) component).setActionListener(mb);
            } else {
                Object params [] = {actionListener};
                throw new javax.faces.FacesException();
            }
        }

        // if the attributes are values set them directly on the component, if
        // not set the ValueBinding reference so that the expressions can be
        // evaluated lazily.
        if (navFacetOrientation != null) {
            if (isValueReference(navFacetOrientation)) {
                vb =
                    context.getApplication().createValueBinding(
                        navFacetOrientation);
                component.setValueBinding("navFacetOrientation", vb);
            } else {
                component.getAttributes().put("navFacetOrientation",
                                              navFacetOrientation);
            }
        }

        if (forValue != null) {
            if (isValueReference(forValue)) {
                vb = context.getApplication().createValueBinding(forValue);
                component.setValueBinding("for", vb);
            } else {
                component.getAttributes().put("for", forValue);
            }
        }
    }
}
