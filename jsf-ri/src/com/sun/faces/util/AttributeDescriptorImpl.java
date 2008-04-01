/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.util;

import java.util.Locale;
import javax.faces.component.AttributeDescriptor;

/**
 * Concrete implementation of <strong>AttributeDescriptor</strong>
 */

public class AttributeDescriptorImpl extends AttributeDescriptor {

    private String name;
    private String displayName;
    private String description;
    private Class type;

    /**
     * Instantiate new attribute descriptor.
     * @param name String containing the name of the attribute
     * @param displayName String containing a short displayable name
     * @param description String containing brief description of attribute
     * @param type Class representing the data type of this attribute's value
     * <p><strong>FIXME</strong> - are null params allowed?</p>
     */ 
    public AttributeDescriptorImpl(String name, String displayName,
                                   String description, Class type) {
	this.name = name;
	this.displayName = displayName;
	this.description = description;
	this.type = type;
    } 


    /**
     * @return a brief description of this attribute, useful when
     * rendering help text in a tool.
     *
     * <p><strong>FIXME</strong> - I18N.</p>
     */
    public String getDescription() {
	return description;
    }


    /**
     * @return a short displayable name of this attribute, useful in
     * constructing the user interface of a tool.
     *
     * <p><strong>FIXME</strong> - I18N.</p>
     */
    public String getDisplayName() {
	return displayName;
    }


    /**
     * @return the attribute name of the attribute described by this
     * <code>AttributeDescriptor</code>.
     */
    public String getName() {
	return name;
    }


    /**
     * @return the Java class representing the data type of the legal
     * values for this attribute.
     */
    public Class getType() {
	return type;
    }

    public String getDisplayName(Locale locale) {
        if (locale == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_LOCALE_ERROR_MESSAGE_ID));
        }
        return null;
    }    
    
   
    public String getDescription(Locale locale) {
        if (locale == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_LOCALE_ERROR_MESSAGE_ID));
        }
        return null;
    }    

}
