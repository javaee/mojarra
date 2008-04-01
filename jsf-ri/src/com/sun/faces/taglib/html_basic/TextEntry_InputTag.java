/*
 * $Id: TextEntry_InputTag.java,v 1.23 2002/03/08 00:24:50 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextEntry_InputTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.FacesTag;
//import com.sun.faces.taglib.html_basic.FormTag;
import com.sun.faces.util.Util;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.UITextEntry;
import javax.faces.UIComponent;
import javax.faces.ObjectManager;
import javax.faces.FormatValidator;
import javax.faces.RangeValidator;
import javax.faces.LengthValidator;
import javax.faces.RequiredValidator;
import javax.faces.Constants;

import javax.servlet.jsp.JspException;
import javax.servlet.ServletRequest;

/**
 *
 *  <B>TextEntry_InputTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextEntry_InputTag.java,v 1.23 2002/03/08 00:24:50 jvisvanathan Exp $
 * @author Jayashri Visvanathan
 * 
 *
 */

public class TextEntry_InputTag extends FacesTag
{
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables
    private String value = null;
    private String size = null;
    private String valueChangeListener = null;
    private String maxlength = null;
    private String modelType = null;
    private String converter = null;
    private String required = null;
    private String format = null;
    private String rangeMinimum = null;
    private String rangeMaximum = null;
    private String lengthMinimum = null;
    private String lengthMaximum = null;
    
    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public TextEntry_InputTag()
    {
        super();
    }

//
// Methods from FacesTag
//

    public UIComponent newComponentInstance() {
        return new UITextEntry();
    }

    public void setAttributes(UIComponent comp) {
	ParameterCheck.nonNull(comp);
	Assert.assert_it(comp instanceof UITextEntry);

	UITextEntry uiTextEntry = (UITextEntry) comp;
       
        uiTextEntry.setAttribute("size", size);
        uiTextEntry.setAttribute("maxlength", maxlength);
        // validate the modelType attribute before setting.
        if ( modelType != null ) {
            Class modelClass = null;
            try {
                modelClass = Class.forName(modelType);
            } catch ( ClassNotFoundException cfe ) {
                // PENDING ( visvan ) throw JSPException or just warn ??
            }
            if ( modelClass != null ) {
                uiTextEntry.setAttribute("modelType", modelClass);
            }
        }    
        uiTextEntry.setAttribute("converterReference", converter);
        // If model attribute is not found get it 
        // from parent form if it exists. If not
        // set text as an attribute so that it can be
        // used during rendering.

        // PENDING ( visvan )
        // make sure that the model object is registered
        if ( getModel() != null ) {
            uiTextEntry.setModelReference(getModel());
        } else {
            // PENDING ( visvan ) all tags should implement a common
            // interface ??
            FormTag ancestor = null;
            try {
                ancestor = (FormTag) findAncestorWithClass(this,
                    FormTag.class);
               String model_str = ancestor.getModel();
               if ( model_str != null ) {
                   setModel("$" + model_str + "." + getId());
                   uiTextEntry.setModelReference(getModel());
               } 
            } catch ( Exception e ) {
                // If form tag cannot be found then model is null
            }
        }
        if ( uiTextEntry.getValue(renderContext) == null && value != null ) {
            uiTextEntry.setText(value);
        }
    }

    public String getRendererType() {
	return "InputRenderer";
    }

    public void addListeners(UIComponent comp) throws JspException {
	ParameterCheck.nonNull(comp);
	
	if (null == valueChangeListener) {
	    return;
	}

	Assert.assert_it(comp instanceof UITextEntry);
	UITextEntry uiTextEntry = (UITextEntry) comp;
	try {
	    uiTextEntry.addValueChangeListener(valueChangeListener);    
	} catch (FacesException fe) {
	    throw new JspException("Listener + " + valueChangeListener +
				   " does not exist or does not implement valueChangeListener " + 
				   " interface" );
	}
    }    
    
    public void addValidators(UIComponent comp) throws JspException {
        
        ParameterCheck.nonNull(comp);
	Assert.assert_it(comp instanceof UITextEntry);
	UITextEntry uiTextEntry = (UITextEntry) comp;
        Assert.assert_it( objectManager != null );
        Assert.assert_it( renderContext != null );
         
        // create validators and put it in objectManager.
        if ( required != null && required.equals("true")) {
            // create required validator
            uiTextEntry.setAttribute("required",  required);
            RequiredValidator reqValidator = new RequiredValidator();
            String reqValidatorId = Util.generateId();
            objectManager.put( pageContext.getSession(), reqValidatorId, 
                    reqValidator);
            uiTextEntry.addValidator(reqValidatorId);
        }
        if ( format != null ) {
            // create format validator
            uiTextEntry.setAttribute("format", format );
            FormatValidator formatValidator = new FormatValidator();
            String formatValidatorId = Util.generateId();
            objectManager.put( pageContext.getSession(), formatValidatorId, 
                    formatValidator);
            uiTextEntry.addValidator(formatValidatorId);
        }
        if ( rangeMinimum != null && rangeMaximum != null ) {
            // create RangeValidators only if the parameters are valid.
            try {
                uiTextEntry.setAttribute("rangeMinimum",  new Integer(rangeMinimum));
                uiTextEntry.setAttribute("rangeMaximum", new Integer(rangeMaximum)); 
                RangeValidator rangeValidator = new RangeValidator();
                String rangeValidatorId = Util.generateId();
                objectManager.put( pageContext.getSession(), rangeValidatorId, 
                     rangeValidator);
                uiTextEntry.addValidator(rangeValidatorId);
            } catch ( NumberFormatException ne ) {
                System.out.println("Invalid parameters for Range Validation");
                System.out.println("Range validator not added");
            }    
        }    
        if ( lengthMinimum != null && lengthMaximum != null ) {
            // create lengthValidator only if the parameters are valid
            try {
                uiTextEntry.setAttribute("lengthMinimum", new Integer(lengthMinimum));
                uiTextEntry.setAttribute("lengthMaximum", new Integer(lengthMaximum));
                LengthValidator lengthValidator = new LengthValidator();
                String lengthValidatorId = Util.generateId();
                objectManager.put( pageContext.getSession(), lengthValidatorId, 
                        lengthValidator);
                uiTextEntry.addValidator(lengthValidatorId);
            } catch ( NumberFormatException nfe ) {
                System.out.println("Invalid parameters for Length Validation");
                System.out.println("Length validator not added"); 
            }    
        }    
    }     
   
    //
    // Class methods
    //

    //
    // General Methods
    //

    /**
     * Tag cleanup method.
     */
    public void release() {

        super.release();

        value = null;
        size = null;
        maxlength = null;
        valueChangeListener = null;
    }

    /**
     * Returns the value of the "value" attribute
     *
     * @return String value of "value" attribute
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets "value" attribute
     * @param value value of "value" attribute
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets the size attribute
     * @param size value of size attribute
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Returns the value of size attribute
     *
     * @return String value of size attribute
     */
    public String getSize() {
        return this.size;
    }

    /**
     * Sets  maxlength attribute
     * @param  maxlength value of maxlength attribute
     */
    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

   /**
     * Returns the value of maxlength attribute
     *
     * @return String value of maxlength attribute
     */
    public String getMaxlength() {
        return this.maxlength;
    }
    
    /**
     * Returns the value of valueChangeListener attribute
     *
     * @return String value of valueChangeListener attribute
     */
    public String getValueChangeListener() {
        return this.valueChangeListener;
    }

    /**
     * Sets valueChangeListener attribute
     * @param change_listener value of formListener attribute
     */
    public void setValueChangeListener(String change_listener) {
        this.valueChangeListener = change_listener;
    }
    
    public void setModelType(String model_type) {
        modelType = model_type;
    }    
    
    public String getModelType() {
        return modelType;
    }     
    
    public void setConverter(String converter_id) {
        converter = converter_id;
    }
    
    public String getConverter() {
        return converter;
    }    
    
    public void setRequired( String required ) {
        this.required = required;
    }
    
    public String getRequired( ) {
        return required;
    }
    
    public void setFormat (String format ) {
        this.format = format;
    }
    
    public String getFormat () {
        return format;
    }
    
    public void setRangeMinimum( String minimum ) {
        this.rangeMinimum = minimum;
    }
    
    public String getRangeMinimum() {
        return rangeMinimum;
    }
    
    public void setRangeMaximum (String maximum ) {
        this.rangeMaximum = maximum;
    }
    
    public String geRangetMaximum () {
        return rangeMaximum;
    }
    
    public void setLengthMinimum(String lengthMinimum ) {
        this.lengthMinimum = lengthMinimum;
    }
    
    public String getLengthMinimum() {
        return lengthMinimum;
    }
    
    public void setLengthMaximum(String lengthMaximum ) {
        this.lengthMaximum = lengthMaximum;
    }
    
    public String getLengthMaximum() {
        return lengthMaximum;
    }
} // end of class TextEntry_InputTag
