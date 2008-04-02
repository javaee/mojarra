/*
 * $Id: ScrollerTag.java,v 1.3 2004/02/05 16:23:17 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
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

package components.taglib;

import components.components.ScrollerComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.webapp.UIComponentTag;

/**
 * ScrollerTag is the tag handler class for <code>ScrollerComponent.</code>
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
