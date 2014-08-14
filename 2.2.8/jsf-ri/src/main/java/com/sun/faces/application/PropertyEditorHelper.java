/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package com.sun.faces.application;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.sun.faces.util.MessageFactory;
import com.sun.faces.util.RequestStateManager;

/**
 * Helper class to aid the ConverterPropertyEditorBase in converting
 * properties.
 *
 * @author Mike Youngstrom
 */
public class PropertyEditorHelper {

    private Application app;

    public PropertyEditorHelper(Application app) {
        this.app = app;
    }

    /**
     * Convert the <code>textValue</code> to an object of type targetClass by
     * delegating to a converter.
     */
    public Object convertToObject(Class<?> targetClass, String textValue) {
        UIComponent component = getComponent();
        Converter converter = app.createConverter(targetClass);
        if (null == converter) {
            // PENDING(edburns): I18N
            FacesException e = new FacesException(
                  "Cannot create Converter to convert value "
                  + textValue
                  + " to instance of target class "
                  + targetClass.getName()
                  + '.');
            throw e;
        }
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        try {
            return converter.getAsObject(currentInstance, component, textValue);
        } catch (ConverterException ce) {
            addConversionErrorMessage(currentInstance, component, ce);
            return null;
        }
    }

    /**
     * Convert an object of type targetClass to text by delegating to a converter
     * obtained from the Faces application.
     */
    public String convertToString(Class<?> targetClass, Object value) {
        UIComponent component = getComponent();
        Converter converter = app.createConverter(targetClass);
        if (null == converter) {
            // PENDING(edburns): I18N
            throw new FacesException("Cannot create Converter to convert "
                                     + targetClass.getName()
                                     + " value "
                                     + value
                                     + " to string.");
        }
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        try {
            return converter.getAsString(currentInstance, component, value);
        } catch (ConverterException ce) {
            addConversionErrorMessage(currentInstance, component, ce);
            return null;
        }
    }

    /**
     * Return the {@link javax.faces.component.UIComponent} that is currently being
     * processed.
     *
     * @return the current component, or null.
     */
    protected UIComponent getComponent() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            return ((UIComponent) RequestStateManager
                  .get(context, RequestStateManager.TARGET_COMPONENT_ATTRIBUTE_NAME));
        }
        return null;
    }

    /**
     * Add a conversion error message in the case of a PropertyEditor based
     * conversion error.
     *
     * @param context
     * @param component
     * @param ce
     */
    protected void addConversionErrorMessage(FacesContext context,
                                             UIComponent component,
                                             ConverterException ce) {
        String converterMessageString = null;
        FacesMessage message;
        UIInput input;
        if (component instanceof UIInput) {
            input = (UIInput) component;
            converterMessageString = input.getConverterMessage();
            input.setValid(false);
        }
        if (null != converterMessageString) {
            message =
                  new FacesMessage(FacesMessage.SEVERITY_ERROR, converterMessageString, converterMessageString);
        } else {
            message = ce.getFacesMessage();
            if (message == null) {
                message = MessageFactory
                      .getMessage(context, UIInput.CONVERSION_MESSAGE_ID);
                if (message.getDetail() == null) {
                    message.setDetail(ce.getMessage());
                }
            }
        }
        context.addMessage(component != null
                           ? component.getClientId(context) : null, message);
	}
}
