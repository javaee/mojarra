/*
 * ConverterPropertyEditor.java
 *
 * Created on August 10, 2006, 12:39 PM
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.application;

import com.sun.faces.RIConstants;
import java.beans.PropertyEditorSupport;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 *
 * @author edburns
 */
public class ConverterPropertyEditor extends PropertyEditorSupport {
    
    public static final String TARGET_CLASS_ATTRIBUTE_NAME = RIConstants.FACES_PREFIX + "ConverterPropertyEditorTargetClass";
    public static final String TARGET_COMPONENT_ATTRIBUTE_NAME = RIConstants.FACES_PREFIX + "ComponentForValue";
    private static final String CURRENT_TEXT_VALUE_NAME = RIConstants.FACES_PREFIX + "CurrentTextValue";
    
    /** Creates a new instance of ConverterPropertyEditor */
    public ConverterPropertyEditor() {
    }
    

    /**
     * <p>Store a request scoped variable under a private key.  The value
     * of this variable is the argument <code>text</code>.  This is necessary
     * because <code>PropertyEditor</code> instances are not necessarily
     * thread safe.</p>
     */
    
    public void setAsText(String text) throws IllegalArgumentException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (null == context) {
            // PENDING(edburns): I18N
            throw new FacesException("Cannot find FacesContext.");
        }
        
        context.getExternalContext().getRequestMap().put(CURRENT_TEXT_VALUE_NAME,
                text);
    }

    public Object getValue() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        if (null == context) {
            // PENDING(edburns): I18N
            throw new FacesException("Cannot find FacesContext.");
        }
        

        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Class targetClass = (Class) requestMap.get(TARGET_CLASS_ATTRIBUTE_NAME);
        UIComponent component = (UIComponent) requestMap.get(TARGET_COMPONENT_ATTRIBUTE_NAME);
        String textValue = (String) requestMap.get(CURRENT_TEXT_VALUE_NAME);
        Object retValue = null;
        
        assert(null != targetClass);
        assert(null != component);
        
        if (null == textValue) {
            return null;
        }
        
        if (null == targetClass) {
            // PENDING(edburns): I18N
            throw new FacesException("Cannot discover target class for value " +
                    textValue + ".");
        }
        
        Converter converter = context.getApplication().createConverter(targetClass);
        
        if (null == converter) {
            // PENDING(edburns): I18N
            throw new FacesException("Cannot create Converter to convert value " + textValue + 
                    " to instance of target class " + targetClass.getName() + ".");
        }
        try {
            
            retValue = converter.getAsObject(context, component, textValue);
        } catch (ConverterException ce) {
            addConversionErrorMessage(context, component, ce, textValue);
            
        }
        
        requestMap.remove(CURRENT_TEXT_VALUE_NAME);
        
        return retValue;
    }
    
    private void addConversionErrorMessage(FacesContext context, UIComponent component,
            ConverterException ce, Object value) {
        FacesMessage message = null;
        String converterMessageString = null;
        UIInput input = null;
        if (component instanceof UIInput) {
            input = (UIInput)component;
            converterMessageString = input.getConverterMessage();
            input.setValid(false);
        }
        if (null != converterMessageString) {
            message = new FacesMessage(converterMessageString, converterMessageString);
        }
        else {
            message = ce.getFacesMessage();
            if (message == null) {
                message = com.sun.faces.util.MessageFactory.getMessage(context,
                        UIInput.CONVERSION_MESSAGE_ID);
                if (message.getDetail() == null) {
                    message.setDetail(ce.getMessage());
                }
            }
        }
        
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage(component.getClientId(context), message);
    }
    
    
}
