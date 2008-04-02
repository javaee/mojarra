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
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author edburns
 */
public class ConverterPropertyEditor extends PropertyEditorSupport {
    
    public static final String TARGET_CLASS_ATTRIBUTE_NAME = RIConstants.FACES_PREFIX + "ConverterPropertyEditorTargetClass";
    public static final String TARGET_COMPONENT_ATTRIBUTE_NAME = RIConstants.FACES_PREFIX + "ComponentForValue";
    
    /** Creates a new instance of ConverterPropertyEditor */
    public ConverterPropertyEditor() {
    }
    
    private String textValue = null;

    public void setAsText(String text) throws IllegalArgumentException {
        this.textValue = text;
    }

    public Object getValue() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        if (null == context) {
            // PENDING(edburns): I18N
            throw new FacesException("Cannot find FacesContext.");
        }
        
        Class targetClass = getTargetClass(context);
        UIComponent component = getComponent(context);
        Object retValue = null;
        
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
        
        retValue = converter.getAsObject(context, component, textValue);
        
        return retValue;
    }
    
    private Class getTargetClass(FacesContext context) {
        return (Class) 
          context.getExternalContext().getRequestMap().get(TARGET_CLASS_ATTRIBUTE_NAME);
    }
    
    private UIComponent getComponent(FacesContext context) {
        return (UIComponent) 
          context.getExternalContext().getRequestMap().get(TARGET_COMPONENT_ATTRIBUTE_NAME);
    }
}
