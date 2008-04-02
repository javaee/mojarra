package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.Validator;

import com.sun.faces.sandbox.component.YuiCalendar;
import com.sun.faces.sandbox.util.Util;

public class YuiCalendarTag extends UISandboxComponentTag {
    protected String accessKey; 
    protected String converter;
    protected String converterMessage;
    protected String dir;
    protected String disabled;
    protected String hideBlankWeeks;
    protected String immediate;
    protected String label;
    protected String lang;
    protected String multiSelect;
    protected String onChange;
    protected String required;
    protected String requiredMessage;
    protected String showWeekdays;
    protected String showWeekFooter;
    protected String showWeekHeader;
    protected String startWeekday;
    protected String style;
    protected String styleClass;
    protected String tabIndex;
    protected String title;
    protected String validator;
    protected String validatorMessage;
    protected String value;
    protected String valueChangeListener;

    public String getComponentType() { return YuiCalendar.COMPONENT_TYPE; }
    public String getRendererType() { return YuiCalendar.RENDERER_TYPE; }

    public void setAccesskey(String accessKey)                     { this.accessKey = accessKey; }
    public void setConverter(String converter)                     { this.converter = converter; }
    public void setConverterMessage(String converterMessage)       { this.converterMessage = converterMessage; } 
    public void setDir(String dir)                                 { this.dir = dir; }
    public void setDisabled(String disabled)                       { this.disabled = disabled; } 
    public void setHideBlankWeeks(String hideBlankWeeks)           { this.hideBlankWeeks = hideBlankWeeks; }
    public void setImmediate(String immediate)                     { this.immediate = immediate; } 
    public void setLabel(String label)                             { this.label = label; }
    public void setLang(String locale)                             { this.lang = locale; } 
    public void setMultiSelect(String multiSelect)                 { this.multiSelect = multiSelect; }
    public void setOnchange(String onChange)                       { this.onChange = onChange; }
    public void setRequired(String required)                       { this.required = required; } 
    public void setRequiredMessage(String requiredMessage)         { this.requiredMessage = requiredMessage; }
    public void setShowWeekdays(String showWeekdays)               { this.showWeekdays = showWeekdays; } 
    public void setShowWeekFooter(String showWeekFooter)           { this.showWeekFooter = showWeekFooter; } 
    public void setShowWeekHeader(String showWeekHeader)           { this.showWeekHeader = showWeekHeader; } 
    public void setStartWeekday(String startWeekday)               { this.startWeekday = startWeekday; } 
    public void setStyle(String style)                             { this.style = style; } 
    public void setStyleClass(String styleClass)                   { this.styleClass = styleClass; } 
    public void setTabIndex(String tabindex)                       { this.tabIndex = tabindex; } 
    public void setTitle(String title)                             { this.title = title; } 
    public void setValidator(String validator)                     { this.validator = validator; } 
    public void setValidatorMessage(String validatorMessage)       { this.validatorMessage = validatorMessage; } 
    public void setValue(String value)                             { this.value = value; } 
    public void setValueChangeListener(String valueChangeListener) { this.valueChangeListener = valueChangeListener; }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        if (!(component instanceof YuiCalendar)) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: com.sun.faces.sandbox.component.YuiCalendar.  Perhaps you're missing a tag?");
        }
        YuiCalendar cal = (YuiCalendar)component;

        setStringProperty(cal, "accesskey", accessKey); // TODO
        setStringProperty(cal, "converterMessage", converterMessage);
        setStringProperty(cal, "dir", dir); // TODO
        setBooleanProperty(cal, "disabled", disabled); //TODO
        setBooleanProperty(cal, "hideBlankWeeks", hideBlankWeeks);
        setBooleanProperty(cal, "immediate", immediate);
        setStringProperty(cal, "label", label); // TODO
        setStringProperty(cal, "lang", lang); //TODO
        setBooleanProperty(cal, "multiSelect", multiSelect);
        setStringProperty(cal, "onChange", onChange);
        setBooleanProperty(cal, "required", required); 
        setBooleanProperty(cal, "requiredMessage", requiredMessage);
        setBooleanProperty(cal, "showWeekdays", showWeekdays);
        setBooleanProperty(cal, "showWeekFooter", showWeekFooter);
        setBooleanProperty(cal, "showWeekHeader", showWeekHeader);
        setIntegerProperty(cal, "startWeekday", startWeekday);
        setStringProperty(cal, "style", style); // TODO
        setStringProperty(cal, "styleClass", styleClass); // TODO
        setStringProperty(cal, "tabIndex", tabIndex); // TODO
        setStringProperty(cal, "title", title); // TODO
        setStringProperty(cal, "validatorMessage", validatorMessage);
        setStringProperty(cal, "value", value);

        MethodBinding val = createMethodBinding(cal, "validator", validator, new Class[]{ Validator.class });
        if (val != null) {
            cal.setValidator(val);
        }
        MethodBinding vcl = createMethodBinding(cal, "valueChangeListener", valueChangeListener, new Class[]{ ValueChangeEvent.class });
        if (vcl != null) {
            cal.setValueChangeListener(vcl);
        }
        
        if (converter != null) {
            if (isValueReference(converter)) {
                ValueBinding vb = Util.getValueBinding(converter);
                cal.setValueBinding("converter", vb);
            } else {
                Converter _converter = FacesContext.getCurrentInstance().  
                getApplication().createConverter(converter);  
                cal.setConverter(_converter);
            }
        }

    }
}
