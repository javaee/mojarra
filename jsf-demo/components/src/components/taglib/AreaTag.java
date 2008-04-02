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
 
package components.taglib;


import components.components.AreaComponent;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;


/**
 * <p>{@link UIComponentTag} for an image map hotspot.</p>
 */

public class AreaTag extends UIComponentTag {

    
    private String alt = null;
    public void setAlt(String alt) {
        this.alt = alt;
    }


    private String coords = null;
    public void setCoords(String coords) {
        this.coords = coords;
    }


    private String onmouseout = null;
    public void setOnmouseout(String newonmouseout) {
        onmouseout = newonmouseout;
    }


    private String onmouseover = null;
    public void setOnmouseover(String newonmouseover) {
        onmouseover = newonmouseover;
    }


    private String shape = null;
    public void setShape(String shape) {
        this.shape = shape;
    }


    private String styleClass = null;
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }


    private String valueRef = null;
    public void setValueRef(String newValueRef) {
	valueRef = newValueRef;
    }


    public String getComponentType() {
            return ("DemoArea");
    }

    
    public String getRendererType() {
        return ("DemoArea");
    } 
    

    public void release() {
        super.release();
        this.alt = null;
        this.coords = null;
        this.onmouseout = null;
        this.onmouseover = null;
        this.shape = null;
        this.styleClass = null;
        this.valueRef = null;
    }


    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        AreaComponent area = (AreaComponent) component;
        if (alt != null) {
            area.setAlt(alt);
        }
        if (coords != null) {
            area.setCoords(coords);
        }
        if (onmouseout != null) {
            area.setAttribute("onmouseout", onmouseout);
        }
        if (onmouseover != null) {
            area.setAttribute("onmouseover", onmouseover);
        }
        if (shape != null) {
            area.setShape(shape);
        }
        if (styleClass != null) {
            area.setAttribute("styleClass", styleClass);
        }
        if (valueRef != null) {
            area.setValueRef(valueRef);
        }

    }


}
