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

import java.util.Locale;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.faces.convert.DateTimeConverter;

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
        value = null;
        displayStyle = null;
        requiredSpecified = false;
        dateStyle = null;
        locale = null;
        pattern = null;
    }
    
    public void release() {
        super.release();
        init();
    }
    
    //*********************************************************************
    // Tag attributes and their setters
    
    private String value;
    public void setValue(String value) {
        this.value = value;
    }
    
    private String displayStyle;
    public void setDisplayStyle(String displayStyle) {
        this.displayStyle = displayStyle;
    }
    
    private boolean required;
    private boolean requiredSpecified = false;
    public void setRequired(boolean required) {
        this.required = required;
        requiredSpecified = true;
    }
    
    private String dateStyle;
    public void setDateStyle(String dateStyle) {
        this.dateStyle = dateStyle;
    }
    
    private Locale locale = null;
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    
    private String pattern = null;
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    
    //*********************************************************************
    // URenderer/Component identification
    
    public String getRendererType() { return "Calendar"; }
    public String getComponentType() { return "Calendar"; }
    
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
        
        UIInput textComp = (UIInput)component.findComponent("date");
        if (textComp == null) { 
            // We add the UIInput component to the tree only if it does not
            // already exist.
            textComp = new UIInput();
            textComp.setRendererType("Text");  
            textComp.getAttributes().put("id", "date"); //FIXME -> id
            DateTimeConverter converter = (DateTimeConverter)
            FacesContext.getCurrentInstance().getApplication().createConverter("DateTime");
            textComp.setConverter(converter);
            component.getChildren().add(textComp);
        }
        
        // Calendar
        processAttribute(component, "displayStyle", displayStyle);
        
        // UIInput from tag attributes
        processAttributeRef(textComp, "value", value);        
        processAttribute(textComp, "required", new Boolean(required), requiredSpecified);

        // UIInput properties 'hard coded'
        textComp.getAttributes().put("size", new Integer(8));
        textComp.getAttributes().put("maxlength", new Integer(8));
        
        // DateTimeConverter properties from tag attributes
        DateTimeConverter converter = (DateTimeConverter)textComp.getConverter();
        /* FIXME to allow more flexibility
        if (dateStyle != null) {
            converter.setDateStyle(dateStyle);
        }
         */
        converter.setDateStyle("short");
        TimeZone tz = TimeZone.getTimeZone("PST");
        converter.setTimeZone(tz);
        if (locale != null) {
            converter.setLocale(locale);
        }
        if (pattern != null) {
            converter.setPattern(pattern);
        }
    }        
    
    //*********************************************************************
    // Utility methods

    private void processAttribute(
    UIComponent component, String attrName, Object attrValue) {
        if (attrValue != null) {
            component.getAttributes().put(attrName, attrValue);
        }
    }
    
    private void processAttribute(
    UIComponent component, String attrName, Object attrValue, 
    boolean attrSpecified) {
        if (attrSpecified) {
            component.getAttributes().put(attrName, attrValue);
        }
    }

    private void processAttributeRef(
    UIComponent component, String attrName, String attrValue) {
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
