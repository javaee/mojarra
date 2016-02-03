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

package components.taglib;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import java.util.TimeZone;

/**
 * <p>CalendarTag is the tag handler class for a <code>Calendar</code>
 * component associated with a <code>Calendar</code> renderer.</p>
 */

public class CalendarTag extends UIComponentTag {

    //*********************************************************************
    // Constructor and initializations
    
    public CalendarTag() {
        super();
        init();
    }


    private void init() {
        dateStyle = null;
        immediateSpecified = false;
        maxlengthSpecified = false;
        pattern = null;
        requiredSpecified = false;
        sizeSpecified = false;
        style = null;
        styleClass = null;
        value = null;
    }


    public void release() {
        super.release();
        init();
    }
    
    //*********************************************************************
    // Tag attributes and their setters
    
    private String dateStyle;


    public void setDateStyle(String dateStyle) {
        this.dateStyle = dateStyle;
    }


    private boolean immediate;
    private boolean immediateSpecified = false;


    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
        immediateSpecified = true;
    }


    private int maxlength;
    private boolean maxlengthSpecified = false;


    public void setMaxlength(int maxlength) {
        this.maxlength = maxlength;
        maxlengthSpecified = true;
    }


    private String pattern = null;


    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    private boolean required;
    private boolean requiredSpecified = false;


    public void setRequired(boolean required) {
        this.required = required;
        requiredSpecified = true;
    }


    private int size;
    private boolean sizeSpecified = false;


    public void setSize(int size) {
        this.size = size;
        sizeSpecified = true;
    }


    private String style = null;


    public void setStyle(String style) {
        this.style = style;
    }


    public String styleClass = null;


    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }


    private String value;


    public void setValue(String value) {
        this.value = value;
    }
    
    //*********************************************************************
    // URenderer/Component identification
    
    public String getRendererType() {
        return "Calendar";
    }


    public String getComponentType() {
        return "Calendar";
    }
    
    //*********************************************************************
    // Properties Setting on the associated component

    /**
     * CalendarComponent essentially is a wrapper around a UIInput
     * component on which we associate a DateTimeConverter.
     * The renderer takes care of adding a graphical representation
     * of a calendar.
     *
     * We use setProperties() to dynamically add the UIInput component to
     * the view tree. We then set its properties according to the values
     * specified in the Calendar tag.
     */
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        UIInput textComp = (UIInput) component.findComponent("date");
        if (textComp == null) {
            // We add the UIInput component to the tree only if it does not
            // already exist.
            textComp = new UIInput();
            textComp.setRendererType("javax.faces.Text");
            textComp.setId("date");
            DateTimeConverter converter = (DateTimeConverter)
                FacesContext.getCurrentInstance().getApplication().
                createConverter("javax.faces.DateTime");
            textComp.setConverter(converter);
            component.getChildren().add(textComp);
        }
        
        // Calendar attributes from tag attributes
        
        // UIInput attributes with hard coded defaults
        textComp.getAttributes().put("size", new Integer(8));
        textComp.getAttributes().put("maxlength", new Integer(8));
        
        // UIInput attributes from tag attributes (VB-enabled)
        processAttributeRef(textComp, "style", style);
        processAttributeRef(textComp, "styleClass", styleClass);
        processAttributeRef(textComp, "value", value);        

        // UIInput attributes from tag attributes (PENDING: not VB enabled)
        processAttribute(textComp, "immediate",
                         new Boolean(immediate), immediateSpecified);
        processAttribute(textComp, "maxlength",
                         new Integer(maxlength), maxlengthSpecified);
        processAttribute(textComp, "required",
                         new Boolean(required), requiredSpecified);
        processAttribute(textComp, "size",
                         new Integer(size), sizeSpecified);

        // DateTimeConverter properties with hard coded defaults
        DateTimeConverter converter = (DateTimeConverter)
            textComp.getConverter();
        converter.setDateStyle("short");
        converter.setType("date");
        TimeZone tz = TimeZone.getTimeZone("PST");
        converter.setTimeZone(tz);

        // DateTimeConverter properties from tag attributes (not VB-enabled)
        if (dateStyle != null) {
            converter.setDateStyle(dateStyle);
        }
        if (pattern != null) {
            converter.setPattern(pattern);
        }
    }        
    
    //*********************************************************************
    // Utility methods

    private void processAttribute(UIComponent component, String attrName, Object attrValue) {
        if (attrValue != null) {
            component.getAttributes().put(attrName, attrValue);
        }
    }


    private void processAttribute(UIComponent component, String attrName, Object attrValue,
                                  boolean attrSpecified) {
        if (attrSpecified) {
            component.getAttributes().put(attrName, attrValue);
        }
    }


    private void processAttributeRef(UIComponent component, String attrName, String attrValue) {
        if (attrValue != null) {
            if (isValueReference(attrValue)) {
                ValueBinding vb =
                    FacesContext.getCurrentInstance().getApplication().
                    createValueBinding(attrValue);
                component.setValueBinding(attrName, vb);
            } else {
                component.getAttributes().put(attrName, attrValue);
            }
        }
    }
}
