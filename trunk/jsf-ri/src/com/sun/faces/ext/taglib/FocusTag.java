/*
 * $Id$
 */ 
package com.sun.faces.ext.taglib;

import javax.faces.webapp.UIComponentELTag;
import javax.faces.component.UIComponent;
import javax.el.ValueExpression;


/**
 * Tag to set focus to a given field.  Uses a single attribute, for, which is
 * the id of the field which should receive the focus.
 * 
 * @author driscoll
 */
public class FocusTag extends UIComponentELTag {
    
    private static final String COMPONENT_TYPE = "com.sun.faces.ext.focus";
    private static final String RENDERER_TYPE = "com.sun.faces.ext.render.FocusHTMLRenderer";
    
    public ValueExpression forID = null;


    // Associate the component type.
    @Override
    public String getComponentType() {
        return COMPONENT_TYPE;
    }
    
    
    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        // set forID
        if (forID != null) {
            if (!forID.isLiteralText()) {
                component.setValueExpression("for", forID);
            } else {
                component.getAttributes().put("for", forID.getExpressionString());
            }
        }
    }


    // We'll render our own content
    @Override
    public String getRendererType() {
        return RENDERER_TYPE;
    }

    @Override
    public void release() {
        super.release();
        forID = null;
    }

    public void setFor(ValueExpression forID) {
        this.forID = forID;
    }
    
}