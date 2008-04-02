/*
 * $Id: ConverterTag.java,v 1.6 2007/03/01 15:51:36 rlubke Exp $
 */

/*
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
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.util.MessageUtils;

import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.servlet.jsp.JspException;

/**
 * Basic implementation of <code>ConverterELTag</code>.
 */
public class ConverterTag extends AbstractConverterTag {

    // --------------------------------------------- Methods from ConverterELTag


    @Override
    protected Converter createConverter() throws JspException {

        return new BindingConverter(converterId, binding);

    }

    // ----------------------------------------------------------- Inner Classes


    public static class BindingConverter implements Converter, StateHolder {

        ValueExpression converterId;
        ValueExpression binding;
        Converter instance;

        // -------------------------------------------------------- Constructors


        /**
         * <p>This is only used during state restoration.</p>
         */
        public BindingConverter() {
        }


        public BindingConverter(ValueExpression converterId,
                                ValueExpression binding) {

            this.converterId = converterId;
            this.binding = binding;

        }

        // ---------------------------------------------- Methods From Converter


        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            Converter delegate = getDelegate(context);
            if (delegate != null) {
                return delegate.getAsObject(context, component, value);
            } else {
                throw new ConverterException(
                     MessageUtils.getExceptionMessage(MessageUtils.CANNOT_CONVERT_ID));
            }
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            Converter delegate = getDelegate(context);
            if (delegate != null) {
                return delegate.getAsString(context, component, value);
            } else {
                throw new ConverterException(
                     MessageUtils.getExceptionMessage(MessageUtils.CANNOT_CONVERT_ID)); 
            }
        }

        // -------------------------------------------- Methods from StateHolder


        private Object[] state;
        public Object saveState(FacesContext context) {

            if (state == null) {
                state = new Object[2];
            }
            state[0] = converterId;
            state[1] = binding;

            return state;

        }

        public void restoreState(FacesContext context, Object state) {

            this.state = (Object[]) state;
            if (this.state != null) {
                this.converterId = (ValueExpression) this.state[0];
                this.binding = (ValueExpression) this.state[1];
            }

        }

        public boolean isTransient() {

            return false;

        }

        public void setTransient(boolean newTransientValue) {
            //no-op
        }

        // ----------------------------------------------------- Private Methods


        private Converter getDelegate(FacesContext context) {

            if (instance == null) {
                return createConverter(converterId, binding, context);
            }

            return instance;

        }

    }
    
}



