/*
 * $Id: ConverterTag.java,v 1.4 2006/03/29 23:03:51 rlubke Exp $
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

import javax.el.ValueExpression;
import javax.el.ELContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.webapp.ConverterELTag;
import javax.servlet.jsp.JspException;

/**
 * Basic implementation of <code>ConverterELTag</code>.
 */
public class ConverterTag extends ConverterELTag {


    // -------------------------------------------------------------- Attributes


    /**
     * <p>The identifier of the {@link Converter} instance to be created.</p>
     */
    private ValueExpression converterId = null;

    /**
     * <p>Set the identifer of the {@link Converter} instance to be created.
     *
     * @param converterId The identifier of the converter instance to be
     * created.
     */
    public void setConverterId(ValueExpression converterId) {

        this.converterId = converterId;

    } // END setConverterId


    /**
     * <p>The {@link ValueExpression} that evaluates to an object that
     * implements {@link Converter}.</p>
     */
    private ValueExpression binding = null;

    /**
     * <p>Set the expression that will be used to create a {@link ValueExpression}
     * that references a backing bean property of the {@link Converter} instance to
     * be created.</p>
     *
     * @param binding The new expression
     */
    public void setBinding(ValueExpression binding) {

        this.binding = binding;

    } // END setBinding


    // -------------------------------------------- Methods from ConverterELTag


    protected Converter createConverter()
    throws JspException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        Converter converter = null;

        // If "binding" is set, use it to create a converter instance.
        if (binding != null) {
            try {
                converter = (Converter) binding.getValue(elContext);
                if (converter != null) {
                    return converter;
                }
            } catch (Exception e) {
                throw new JspException(e);
            }
        }

        // If "converterId" is set, use it to create the converter
        // instance.  If "converterId" and "binding" are both set, store the
        // converter instance in the value of the property represented by
        // the ValueExpression 'binding'.
        if (converterId != null) {
            try {
                String converterIdVal = (String)
                    converterId.getValue(elContext);
                converter = facesContext.getApplication()
                                .createConverter(converterIdVal);
                if (converter != null && binding != null) {
                    binding.setValue(elContext, converter);
                }
            } catch (Exception e) {
                throw new JspException(e);
            }
        }

        return converter;

    } // END createConverter


}
