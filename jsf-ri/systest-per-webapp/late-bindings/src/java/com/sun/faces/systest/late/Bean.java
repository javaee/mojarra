/*
 * $Id: Bean.java,v 1.1 2007/03/01 18:13:37 rlubke Exp $
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

package com.sun.faces.systest.late;

import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;


public class Bean {

    private boolean switchit;


    private Validator v1 = new CustomValidator1();
    private Validator v2 = new CustomValidator2();
    private Validator vret = v1;
    private Validator vnext = v2;
    public Validator getValidator() {
        if (switchit) {
            Validator tmp = vret;
            vret = vnext;
            vnext = tmp;
            switchit = false;
        }
        return vret;
    }

    private Converter c1 = new CustomConverter1();
    private Converter c2 = new CustomConverter2();
    private Converter cret = c1;
    private Converter cnext = c2;
    public Converter getConverter() {
        if (switchit) {
            Converter tmp = cret;
            cret = cnext;
            cnext = tmp;
            switchit = false;
        }
        return cret;
    }

    // ----------------------------------------------------------- Inner Classes

    private class CustomValidator1 implements Validator {

        public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
            switchit = true;
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                          "CustomValidator1 invoked",
                                                          "CustomValidator1 invoked"));
        }
    }

    private class CustomValidator2 implements Validator {

        public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
            switchit = true;
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                          "CustomValidator2 invoked",
                                                          "CustomValidator2 invoked"));
        }
    }

    private class CustomConverter1 implements Converter {

        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            switchit = true;
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                          "CustomConverter1 invoked",
                                                          "customConverter1 invoked"));
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            switchit = true; 
            return value.toString();
        }

    }

    private class CustomConverter2 implements Converter {

        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            switchit = true;
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                          "CustomConverter2 invoked",
                                                          "customConverter2 invoked"));
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            switchit = true;
            return value.toString();
        }

    }
}
