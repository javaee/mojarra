package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ValueChangeEvent;
import javax.faces.webapp.UIComponentTag;

import com.sun.faces.sandbox.component.YuiCalendar;
import com.sun.faces.sandbox.component.YuiTree;
import com.sun.faces.sandbox.util.Util;

public class YuiCalendarTag extends UIComponentTag {
    protected String accesskey; 
    protected String converter;
    protected String converterMessage;
    protected String dir;
    protected String disabled;
    protected String hideBlankWeeks;
    protected String immediate;
    protected String label;
    protected String lang;
    protected String multiSelect;
    protected String onchange;
    protected String required;
    protected String requiredMessage;
    protected String showWeekdays;
    protected String showWeekFooter;
    protected String showWeekHeader;
    protected String startWeekday;
    protected String style;
    protected String styleClass;
    protected String tabindex;
    protected String title;
    protected String validator;
    protected String validatorMessage;
    protected String value;
    protected String valueChangeListener;
    
    public String getComponentType() {
        return YuiCalendar.COMPONENT_TYPE;
    }
    public String getRendererType() {
        return YuiCalendar.RENDERER_TYPE;
    }
    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }
    public void setConverter(String converter) {
        this.converter = converter;
    }
    
    public void setConverterMessage(String converterMessage) {
        this.converterMessage = converterMessage;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public void setHideBlankWeeks(String hideBlankWeeks) {
        this.hideBlankWeeks = hideBlankWeeks;
    }
    public void setImmediate(String immediate) {
        this.immediate = immediate;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public void setLang(String locale) {
        this.lang = locale;
    }


    public void setMultiSelect(String multiSelect) {
        this.multiSelect = multiSelect;
    }
    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }
    public void setRequired(String required) {
        this.required = required;
    }

    public void setRequiredMessage(String requiredMessage) {
        this.requiredMessage = requiredMessage;
    }
    public void setShowWeekdays(String showWeekdays) {
        this.showWeekdays = showWeekdays;
    }


    public void setShowWeekFooter(String showWeekFooter) {
        this.showWeekFooter = showWeekFooter;
    }

    public void setShowWeekHeader(String showWeekHeader) {
        this.showWeekHeader = showWeekHeader;
    }

    public void setStartWeekday(String startWeekday) {
        this.startWeekday = startWeekday;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public void setTabindex(String tabindex) {
        this.tabindex = tabindex;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public void setValidatorMessage(String validatorMessage) {
        this.validatorMessage = validatorMessage;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValueChangeListener(String valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
    
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        if (!(component instanceof YuiCalendar)) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.  Expected: com.sun.faces.sandbox.component.YuiTree.  Perhaps you're missing a tag?");
        }
        YuiCalendar cal = (YuiCalendar)component;
        if (accesskey != null) {
            if (isValueReference(accesskey)) {
                ValueBinding vb = Util.getValueBinding(accesskey);
                cal.setValueBinding(accesskey, vb);
            } else {
                // TODO:  implement or remove
//                cal.setAccesskey(accesskey);
            }
        }

        if (converter != null) {
            if (isValueReference(converter)) {
                ValueBinding vb = Util.getValueBinding(converter);
                cal.setValueBinding(converter, vb);
            } else {
                Converter _converter = FacesContext.getCurrentInstance().  
                    getApplication().createConverter(converter);  
                cal.setConverter(_converter);
            }
        }

        if (converterMessage != null) {
            if (isValueReference(converterMessage)) {
                ValueBinding vb = Util.getValueBinding(converterMessage);
                cal.setValueBinding(converterMessage, vb);
            } else {
                cal.setConverterMessage(converterMessage);
            }
        }

        if (dir != null) {
            if (isValueReference(dir)) {
                ValueBinding vb = Util.getValueBinding(dir);
                cal.setValueBinding(dir, vb);
            } else {
//                cal.setDir(dir);
            }
        }

        if (disabled != null) {
            if (isValueReference(disabled)) {
                ValueBinding vb = Util.getValueBinding(disabled);
                cal.setValueBinding(disabled, vb);
            } else {
//              TODO:  implement or remove
//                cal.setDisabled(Boolean.parseBoolean(disabled));
            }
        }

        if (hideBlankWeeks != null) {
            if (isValueReference(hideBlankWeeks)) {
                ValueBinding vb = Util.getValueBinding(hideBlankWeeks);
                cal.setValueBinding(hideBlankWeeks, vb);
            } else {
                cal.setHideBlankWeeks(Boolean.parseBoolean(hideBlankWeeks));
            }
        }

        if (immediate != null) {
            if (isValueReference(immediate)) {
                ValueBinding vb = Util.getValueBinding(immediate);
                cal.setValueBinding(immediate, vb);
            } else {
                cal.setImmediate(Boolean.parseBoolean(immediate));
            }
        }

        if (label != null) {
            if (isValueReference(label)) {
                ValueBinding vb = Util.getValueBinding(label);
                cal.setValueBinding(label, vb);
            } else {
//              TODO:  implement or remove
//                cal.setLabel(label);
            }
        }

        if (lang != null) {
            if (isValueReference(lang)) {
                ValueBinding vb = Util.getValueBinding(lang);
                cal.setValueBinding(lang, vb);
            } else {
//              TODO:  implement or remove
//                cal.setLang(lang);
            }
        }

        if (multiSelect != null) {
            if (isValueReference(multiSelect)) {
                ValueBinding vb = Util.getValueBinding(multiSelect);
                cal.setValueBinding(multiSelect, vb);
            } else {
                cal.setMultiSelect(Boolean.parseBoolean(multiSelect));
            }
        }

        if (onchange != null) {
            if (isValueReference(onchange)) {
                ValueBinding vb = Util.getValueBinding(onchange);
                cal.setValueBinding(onchange, vb);
            } else {
                cal.setOnchange(onchange);
            }
        }

        if (required != null) {
            if (isValueReference(required)) {
                ValueBinding vb = Util.getValueBinding(required);
                cal.setValueBinding(required, vb);
            } else {
                cal.setRequired(Boolean.parseBoolean(required));
            }
        }

        if (requiredMessage != null) {
            if (isValueReference(requiredMessage)) {
                ValueBinding vb = Util.getValueBinding(requiredMessage);
                cal.setValueBinding(requiredMessage, vb);
            } else {
                cal.setRequiredMessage(requiredMessage);
            }
        }

        if (showWeekdays != null) {
            if (isValueReference(showWeekdays)) {
                ValueBinding vb = Util.getValueBinding(showWeekdays);
                cal.setValueBinding(showWeekdays, vb);
            } else {
                cal.setShowWeekdays(Boolean.parseBoolean(showWeekdays));
            }
        }

        if (showWeekFooter != null) {
            if (isValueReference(showWeekFooter)) {
                ValueBinding vb = Util.getValueBinding(showWeekFooter);
                cal.setValueBinding(showWeekFooter, vb);
            } else {
                cal.setShowWeekFooter(Boolean.parseBoolean(showWeekFooter));
            }
        }

        if (showWeekHeader != null) {
            if (isValueReference(showWeekHeader)) {
                ValueBinding vb = Util.getValueBinding(showWeekHeader);
                cal.setValueBinding(showWeekHeader, vb);
            } else {
                cal.setShowWeekHeader(Boolean.parseBoolean(showWeekHeader));
            }
        }

        if (startWeekday != null) {
            if (isValueReference(startWeekday)) {
                ValueBinding vb = Util.getValueBinding(startWeekday);
                cal.setValueBinding(startWeekday, vb);
            } else {
                cal.setStartWeekday(Integer.parseInt(startWeekday));
            }
        }

        if (style != null) {
            if (isValueReference(style)) {
                ValueBinding vb = Util.getValueBinding(style);
                cal.setValueBinding(style, vb);
            } else {
//              TODO:  implement or remove
//                cal.setStyle(style);
            }
        }

        if (styleClass != null) {
            if (isValueReference(styleClass)) {
                ValueBinding vb = Util.getValueBinding(styleClass);
                cal.setValueBinding(styleClass, vb);
            } else {
//              TODO:  implement or remove
//                cal.setStyleClass(styleClass);
            }
        }

        if (tabindex != null) {
            if (isValueReference(tabindex)) {
                ValueBinding vb = Util.getValueBinding(tabindex);
                cal.setValueBinding(tabindex, vb);
            } else {
//              TODO:  implement or remove
//                cal.setTabIndex(tabindex);
            }
        }

        if (title != null) {
            if (isValueReference(title)) {
                ValueBinding vb = Util.getValueBinding(title);
                cal.setValueBinding(title, vb);
            } else {
//              TODO:  implement or remove
//                cal.setTitle(title);
            }
        }

        if (validator != null) {
            if (isValueReference(validator)) {
                Class args[] = { ValueChangeEvent.class };
                MethodBinding vb = 
                    FacesContext.getCurrentInstance().getApplication().createMethodBinding(valueChangeListener, args);
                cal.setValidator(vb);
            } else {
                throw new javax.faces.FacesException("Invalid MethodBinding expression:  " + validator);
            }
        }

        if (validatorMessage != null) {
            if (isValueReference(validatorMessage)) {
                ValueBinding vb = Util.getValueBinding(validatorMessage);
                cal.setValueBinding(validatorMessage, vb);
            } else {
                cal.setValidatorMessage(validatorMessage);
            }
        }

        if (value != null) {
            if (isValueReference(value)) {
                ValueBinding vb = Util.getValueBinding(value);
                cal.setValueBinding(value, vb);
            } else {
                cal.setValue(value);
            }
        }

        if (valueChangeListener != null) {
            if (isValueReference(valueChangeListener)) {
                Class args[] = { ValueChangeEvent.class };
                MethodBinding vb = 
                    FacesContext.getCurrentInstance().getApplication().createMethodBinding(valueChangeListener, args);
                cal.setValueChangeListener(vb);
            } else {
                throw new javax.faces.FacesException("Invalid MethodBinding expression:  " + valueChangeListener);
            }
        }

    }
    public String getAccesskey() {
        return accesskey;
    }
    public String getConverter() {
        return converter;
    }
    public String getConverterMessage() {
        return converterMessage;
    }
    public String getDir() {
        return dir;
    }
    public String getDisabled() {
        return disabled;
    }
    public String getHideBlankWeeks() {
        return hideBlankWeeks;
    }
    public String getImmediate() {
        return immediate;
    }
    public String getLabel() {
        return label;
    }
    public String getLang() {
        return lang;
    }
    public String getMultiSelect() {
        return multiSelect;
    }
    public String getOnchange() {
        return onchange;
    }
    public String getRequired() {
        return required;
    }
    public String getRequiredMessage() {
        return requiredMessage;
    }
    public String getShowWeekdays() {
        return showWeekdays;
    }
    public String getShowWeekFooter() {
        return showWeekFooter;
    }
    public String getShowWeekHeader() {
        return showWeekHeader;
    }
    public String getStartWeekday() {
        return startWeekday;
    }
    public String getStyle() {
        return style;
    }
    public String getStyleClass() {
        return styleClass;
    }
    public String getTabindex() {
        return tabindex;
    }
    public String getTitle() {
        return title;
    }
    public String getValidator() {
        return validator;
    }
    public String getValidatorMessage() {
        return validatorMessage;
    }
    public String getValue() {
        return value;
    }
    public String getValueChangeListener() {
        return valueChangeListener;
    }
}