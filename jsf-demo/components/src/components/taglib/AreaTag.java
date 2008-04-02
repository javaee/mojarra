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


import components.components.UIArea;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;


/**
 * This class is the tag handler that evaluates the <code>area</code>
 * custom tag.
 */

public class AreaTag extends UIComponentTag {

    
    private String onmouseout = null;
    public void setOnmouseout(String newonmouseout) {
        onmouseout = newonmouseout;
    }


    private String onmouseover = null;
    public void setOnmouseover(String newonmouseover) {
        onmouseover = newonmouseover;
    }


    private String valueRef = null;
    public void setValueRef(String newValueRef) {
	valueRef = newValueRef;
    }


    public String getComponentType() {
            return ("Area");
    }

    
    public String getRendererType() {
        return "Area";
    } 
    

    protected void overrideProperties(UIComponent component) {

        super.overrideProperties(component);

        if (onmouseover != null) {
            component.setAttribute("onmouseover", onmouseover);
        }
        if (onmouseout != null) {
            component.setAttribute("onmouseout", onmouseout);
        }
        if (valueRef != null) {
            ((UIArea) component).setValueRef(valueRef);
        }

    }


}
