/*
 * $Id: Bean.java,v 1.3 2007/09/07 12:02:30 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

    private Validator val;
    public void setValidator2(Validator val) {
        System.out.println("setValidator2() -> " + val.getClass().getName());
        if (!(val instanceof LBValidator)) {
            throw new IllegalArgumentException("Expected LBValidator, received: " + val.getClass().getName());
        }
        this.val = val;
    }

    public Validator getValidator2() {
        return val;
    }

    private Converter con;
    public void setConverter2(Converter con) {
        System.out.println("setConverter2() -> " + con.getClass().getName());
        if (!(con instanceof LBConverter)) {
            throw new IllegalArgumentException("Expected LBConverter, received: " + con.getClass().getName());
        }
        this.con = con;
    }

    public Converter getConverter2() {
        return con;
    }
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
