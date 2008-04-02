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


import components.components.MapComponent;
import components.renderkit.Util;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.el.MethodBinding;
import javax.faces.webapp.UIComponentTag;
import javax.faces.event.ActionEvent;



/**
 * <p>{@link UIComponentTag} for an image map.</p>
 */
 
public class MapTag extends UIComponentTag {


    private String current = null;
    public void setCurrent(String current) {
        this.current = current;
    }

    private String actionListener = null;
    public void setActionListener(String actionListener) {
        this.actionListener = actionListener;
    }

    private String action = null;
    public void setAction(String action) {
        this.action = action;
    }

    private String immediate = null;
    public void setImmediate(String immediate) {
        this.immediate = immediate;
    }

    private String styleClass = null;
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }


    public String getComponentType() {
        return ("DemoMap");
    }


    public String getRendererType() {
        return ("DemoMap");
    }


    public void release() {
        super.release();
        current = null;
        styleClass = null;
        actionListener = null;
        action = null;
	immediate = null;
	styleClass = null;
    }


    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        MapComponent map = (MapComponent) component;
	//        if (current != null) {
	//            map.setCurrent(current);
	//        }
        if (styleClass != null) {
            if (isValueReference(styleClass)) {
                ValueBinding vb = FacesContext.getCurrentInstance().getApplication().
                    createValueBinding(styleClass);
                map.setValueBinding("styleClass", vb);
            } else {
            map.getAttributes().put("styleClass", styleClass);
        }
        }
        if (actionListener != null) {
            if (isValueReference(actionListener)) {
                Class args[] = { ActionEvent.class };
                MethodBinding mb = FacesContext.getCurrentInstance().getApplication().createMethodBinding(actionListener, args);
                map.setActionListener(mb);
            } else {
              Object params [] = {actionListener};
              throw new javax.faces.FacesException();
            }
        }

        if (action != null) {
	    if (isValueReference(action)) {
                MethodBinding vb = FacesContext.getCurrentInstance().getApplication().createMethodBinding(action, null);
                map.setAction(vb);
            } else {
                map.setAction(Util.createConstantMethodBinding(action));
            }
        }
        if (immediate != null) {
            if (isValueReference(immediate)) {
                ValueBinding vb = FacesContext.getCurrentInstance().getApplication().
		    createValueBinding(immediate);
                map.setValueBinding("immediate", vb);
            } else {
                boolean _immediate = new Boolean(immediate).booleanValue();
                map.setImmediate(_immediate);
            }
        }
	
    }

    
}
