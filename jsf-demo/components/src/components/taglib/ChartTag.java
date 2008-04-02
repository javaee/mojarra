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

import components.components.ChartComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;


/**
 * This class is the tag handler that processes the <code>chart</code> 
 * custom tag.
 */

public class ChartTag extends UIComponentTag {


    private String width = null;
    public void setWidth(String width) {
        this.width = width;
    }

    private String height = null;
    public void setHeight(String height) {
        this.height = height;
    }

    private String orientation = null;
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    
    private String value = null;
    public void setValue(String value) {
        this.value = value;
    }
    
    private String type = null;
    public void setType(String type) {
        this.type = type;
    }

    public String getComponentType() {
        return ("Chart");
    }


    public String getRendererType() {
        return (null);
    }


    public void release() {
        super.release();
        width = null;
        height = null;
        orientation = null;
    }


    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        ChartComponent chart = (ChartComponent) component;
       
        if (width != null) {
            if (isValueReference(width)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding(width);
                chart.setValueBinding("width", vb);
            } else {
                chart.setWidth(width);
            }
        }
        
        if (height != null) {
            if (isValueReference(height)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding(height);
                chart.setValueBinding("height", vb);
            } else {
                chart.setHeight(height);
            }
        }
        
        if (orientation != null) {
            if (isValueReference(orientation)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding(orientation);
                chart.setValueBinding("orientation", vb);
            } else {
                chart.setOrientation(orientation);
            }
        }
        
        if (type != null) {
            if (isValueReference(type)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding(type);
                chart.setValueBinding("type", vb);
            } else {
                chart.setType(type);
            }
        }
        
        if (value != null) {
            if (isValueReference(value)) {
                ValueBinding vb = FacesContext.getCurrentInstance()
                    .getApplication().createValueBinding(value);
                chart.setValueBinding("value", vb);
            } else {
                chart.setValue(value);
            }
        }
    }


}
