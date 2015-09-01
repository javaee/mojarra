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
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

package com.sun.faces.composite;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.convert.Converter;

@ManagedBean
@RequestScoped
public class CompositeBean {

    public String getStateSavingMethod() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getStateManager().isSavingStateInClient(facesContext) ?
                "client" : "server";
    }

    public List<String> getTableInputValues() {
        List<String> result = new ArrayList<String>();
        result.add("a value");
        return result;
    }

    public ActionListener getActionListener() {
        return new ActionListener() {

            public void processAction(ActionEvent event)
                  throws AbortProcessingException {
                FacesContext ctx = FacesContext.getCurrentInstance();
                UIComponent source = (UIComponent) event.getSource();
                String cid = source.getClientId(ctx);
                ctx.addMessage(cid,
                               new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                "Action Invoked : " + cid,
                                                "Action Invoked : " + cid));
            }
        };

    }

    private String text = "DEFAULT VALUE";
    public String getText() {
        return text;
    }

    // for #1966
    private List<String> defaultValueList = Arrays.asList("Item 1","Item 2");

    public List<String> getDefaultValueList() {
        return this.defaultValueList;
    }

    private List<String> emptyList = Collections.emptyList();

    public List<String> getEmptyList() {
        return this.emptyList;
    }

    public Color getColor() {
        return Color.PINK;
    }
    // end #1966

    // for #1986
    private BigDecimal bigDecimalValue = null;

    public BigDecimal getBigDecimalValue() {
        return bigDecimalValue;
    }
    // end #1986

    public void setBigDecimalValue(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }


    public Validator getValidator() {

        return new TestValidator();

    }


    public Converter getConverter() {

        return new TestConverter();

    }


    public String doNav() {

        return "nestingNav";

    }


    public String action() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        UIComponent c = UIComponent.getCurrentComponent(ctx);
        ctx.addMessage(null,
                       new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "Action invoked: " + c.getClientId(ctx),
                                        "Action invoked: " + c.getClientId(ctx)));
        return "";

    }


    public String action(Object arg1, Object arg2) {

        FacesContext ctx = FacesContext.getCurrentInstance();
        UIComponent c = UIComponent.getCurrentComponent(ctx);
        String message = "Action invoked: "
                             + c.getClientId(ctx)
                             + ", arg1: " + arg1.toString()
                             + ", arg2: " + arg2.toString();

        ctx.addMessage(null,
                       new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        message,
                                        message));
        return "";

    }

    public String custom() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        UIComponent c = UIComponent.getCurrentComponent(ctx);
        ctx.addMessage(null,
                       new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "Custom action invoked: " + c.getClientId(ctx),
                                        "Custom action invoked: " + c.getClientId(ctx)));
        return "";

    }

     public String custom(Object arg1, Object arg2) {

        FacesContext ctx = FacesContext.getCurrentInstance();
        UIComponent c = UIComponent.getCurrentComponent(ctx);
        String message = "Custom action invoked: "
                             + c.getClientId(ctx)
                             + ", arg1: " + arg1.toString()
                             + ", arg2: " + arg2.toString();

        ctx.addMessage(null,
                       new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        message,
                                        message));
        return "";

    }


    public String display(String arg) {

        return "arg: " + arg;

    }


    public void actionListener(ActionEvent ae) {

        FacesContext ctx = FacesContext.getCurrentInstance();
        UIComponent c = UIComponent.getCurrentComponent(ctx);
        ctx.addMessage(null,
                       new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "ActionListener invoked: " + c.getClientId(ctx),
                                        "ActionListener invoked: " + c.getClientId(ctx)));
    }


    public void validate(FacesContext ctx, UIComponent c, Object o) {

        ctx.addMessage(null,
                       new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "validator invoked: " + c.getClientId(ctx),
                                        "validator invoked: " + c.getClientId(ctx)));

    }


    public void valueChange(ValueChangeEvent event) {

        FacesContext ctx = FacesContext.getCurrentInstance();
        UIComponent c = event.getComponent();
        ctx.addMessage(null,
                       new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "ValueChange invoked: " + c.getClientId(ctx),
                                        "ValueChange invoked: " + c.getClientId(ctx)));

    }

    public Format getFormat() {
        Format result = new DecimalFormat();
        return result;
    }


    public String getStringValue() {

        return "equalityCheck";

    }

    public String[] getStringValues() {
        return new String[] { "equalityCheck", "failedCheck" };
    }

    public String[] getItems() {
        return new String[] { "A", "B", "C" };
    }

    public List<Integer> getTestValues() {
        List<Integer> values = new ArrayList<Integer>(1);
        values.add(1);
        return values;
    }


    // ---------------------------------------------------------- Nested Classes


    public static class TestValidator implements Validator {

        public void validate(FacesContext context, UIComponent component, Object value)
              throws ValidatorException {

            String cid = component.getClientId(context);
            context.addMessage(cid,
                               new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                "Validator Invoked : " + cid,
                                                "Validator Invoked : " + cid));
        }
    }


    public static class TestConverter implements Converter {

        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            return value;
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            String cid = component.getClientId(context);
            context.addMessage(cid,
                               new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                "Converter Invoked : " + cid,
                                                "Converter Invoked : " + cid));
            return value.toString();
        }
    }

}
