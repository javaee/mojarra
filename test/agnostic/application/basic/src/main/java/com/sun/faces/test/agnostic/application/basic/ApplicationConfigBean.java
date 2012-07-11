/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.test.agnostic.application.basic;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.*;

@ManagedBean
@SessionScoped
public class ApplicationConfigBean {

    public static String standardComponentTypes[] = {
        "javax.faces.Column",
        "javax.faces.Command",
        "javax.faces.Data",
        "javax.faces.Form",
        "javax.faces.Graphic",
        "javax.faces.Input",
        "javax.faces.Message",
        "javax.faces.Messages",
        "javax.faces.NamingContainer",
        "javax.faces.Output",
        "javax.faces.Panel",
        "javax.faces.Parameter",
        "javax.faces.SelectBoolean",
        "javax.faces.SelectItem",
        "javax.faces.SelectItems",
        "javax.faces.SelectMany",
        "javax.faces.SelectOne",
        "javax.faces.ViewRoot",
        "javax.faces.HtmlCommandButton",
        "javax.faces.HtmlCommandLink",
        "javax.faces.HtmlDataTable",
        "javax.faces.HtmlForm",
        "javax.faces.HtmlGraphicImage",
        "javax.faces.HtmlInputHidden",
        "javax.faces.HtmlInputSecret",
        "javax.faces.HtmlInputText",
        "javax.faces.HtmlInputTextarea",
        "javax.faces.HtmlMessage",
        "javax.faces.HtmlMessages",
        "javax.faces.HtmlOutputFormat",
        "javax.faces.HtmlOutputLabel",
        "javax.faces.HtmlOutputLink",
        "javax.faces.HtmlOutputText",
        "javax.faces.HtmlPanelGrid",
        "javax.faces.HtmlPanelGroup",
        "javax.faces.HtmlSelectBooleanCheckbox",
        "javax.faces.HtmlSelectManyCheckbox",
        "javax.faces.HtmlSelectManyListbox",
        "javax.faces.HtmlSelectManyMenu",
        "javax.faces.HtmlSelectOneListbox",
        "javax.faces.HtmlSelectOneMenu",
        "javax.faces.HtmlSelectOneRadio"
    };

    public static Class standardComponentClasses[] = {
        javax.faces.component.UIColumn.class,
        javax.faces.component.UICommand.class,
        javax.faces.component.UIData.class,
        javax.faces.component.UIForm.class,
        javax.faces.component.UIGraphic.class,
        javax.faces.component.UIInput.class,
        javax.faces.component.UIMessage.class,
        javax.faces.component.UIMessages.class,
        javax.faces.component.UINamingContainer.class,
        javax.faces.component.UIOutput.class,
        javax.faces.component.UIPanel.class,
        javax.faces.component.UIParameter.class,
        javax.faces.component.UISelectBoolean.class,
        javax.faces.component.UISelectItem.class,
        javax.faces.component.UISelectItems.class,
        javax.faces.component.UISelectMany.class,
        javax.faces.component.UISelectOne.class,
        javax.faces.component.UIViewRoot.class,
        javax.faces.component.html.HtmlCommandButton.class,
        javax.faces.component.html.HtmlCommandLink.class,
        javax.faces.component.html.HtmlDataTable.class,
        javax.faces.component.html.HtmlForm.class,
        javax.faces.component.html.HtmlGraphicImage.class,
        javax.faces.component.html.HtmlInputHidden.class,
        javax.faces.component.html.HtmlInputSecret.class,
        javax.faces.component.html.HtmlInputText.class,
        javax.faces.component.html.HtmlInputTextarea.class,
        javax.faces.component.html.HtmlMessage.class,
        javax.faces.component.html.HtmlMessages.class,
        javax.faces.component.html.HtmlOutputFormat.class,
        javax.faces.component.html.HtmlOutputLabel.class,
        javax.faces.component.html.HtmlOutputLink.class,
        javax.faces.component.html.HtmlOutputText.class,
        javax.faces.component.html.HtmlPanelGrid.class,
        javax.faces.component.html.HtmlPanelGroup.class,
        javax.faces.component.html.HtmlSelectBooleanCheckbox.class,
        javax.faces.component.html.HtmlSelectManyCheckbox.class,
        javax.faces.component.html.HtmlSelectManyListbox.class,
        javax.faces.component.html.HtmlSelectManyMenu.class,
        javax.faces.component.html.HtmlSelectOneListbox.class,
        javax.faces.component.html.HtmlSelectOneMenu.class,
        javax.faces.component.html.HtmlSelectOneRadio.class
    };

    public static String standardConverterIds[] = {
        "javax.faces.BigDecimal",
        "javax.faces.BigInteger",
        "javax.faces.Boolean",
        "javax.faces.Byte",
        "javax.faces.Character",
        "javax.faces.DateTime",
        "javax.faces.Double",
        "javax.faces.Float",
        "javax.faces.Integer",
        "javax.faces.Long",
        "javax.faces.Number",
        "javax.faces.Short"
    };

    public static Class standardConverterClasses[] = {
        javax.faces.convert.BigDecimalConverter.class,
        javax.faces.convert.BigIntegerConverter.class,
        javax.faces.convert.BooleanConverter.class,
        javax.faces.convert.ByteConverter.class,
        javax.faces.convert.CharacterConverter.class,
        javax.faces.convert.DateTimeConverter.class,
        javax.faces.convert.DoubleConverter.class,
        javax.faces.convert.FloatConverter.class,
        javax.faces.convert.IntegerConverter.class,
        javax.faces.convert.LongConverter.class,
        javax.faces.convert.NumberConverter.class,
        javax.faces.convert.ShortConverter.class
    };

    public static Class standardConverterByIdClasses[] = {
        java.math.BigDecimal.class,
        java.math.BigInteger.class,
        java.lang.Boolean.class,
        java.lang.Byte.class,
        java.lang.Character.class,
        null,
        java.lang.Double.class,
        java.lang.Float.class,
        java.lang.Integer.class,
        java.lang.Long.class,
        null,
        java.lang.Short.class
    };

    public static Class standardConverterPrimitiveClasses[] = {
        null,
        null,
        java.lang.Boolean.TYPE,
        java.lang.Byte.TYPE,
        java.lang.Character.TYPE,
        null,
        java.lang.Double.TYPE,
        java.lang.Float.TYPE,
        java.lang.Integer.TYPE,
        java.lang.Long.TYPE,
        null,
        java.lang.Short.TYPE
    };

    private String title = "Test Application Config";
    public String getTitle() {
        return title; 
    }

    public ApplicationConfigBean() {

        componentPositive();
        componentNegative();
        getComponentTypes();
        converterPositive();
        converterNegative();
        getConverterIds();
        validatorPositive();
        validatorNegative();
        getValidatorIds();
    }

    private void componentPositive() {
        TestComponent newTestComponent = null;
        TestComponent testComponent = new TestComponent();
        UIComponent uic = null;

        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();

        app.addComponent(testComponent.getComponentType(), 
            "com.sun.faces.test.agnostic.application.basic.TestComponent");

        newTestComponent = (TestComponent)app.createComponent(testComponent.getComponentType());
        assertTrue(null != newTestComponent && newTestComponent != testComponent);

        // built-in components
        for (int i = 0, len = standardComponentTypes.length; i < len; i++) {
            uic = app.createComponent(standardComponentTypes[i]);
            assertTrue(null != uic);
            assertTrue(standardComponentClasses[i].isAssignableFrom(uic.getClass()));
        }
    }

    private void componentNegative() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        boolean exceptionThrown = false;
        try {
            app.addComponent("William", "BillyBoy");
            app.createComponent("William");
        } catch (FacesException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        // non-existent mapping
        exceptionThrown = false;
        try {
            app.createComponent("Joebob");
        } catch (FacesException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    private void getComponentTypes() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();

        Iterator iter = app.getComponentTypes();
        assertTrue(null != iter);
        assertTrue(isSubset(standardComponentTypes, iter));
    }

    private void converterPositive() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();

        TestConverter newTestConverter = null;
        TestConverter testConverter = new TestConverter();
        Converter conv = null;

        // runtime addition

        app.addConverter(testConverter.getConverterId(),
            "com.sun.faces.test.agnostic.application.basic.TestConverter");
        newTestConverter = (TestConverter)app.createConverter(testConverter.getConverterId());
        assertTrue(null != newTestConverter && newTestConverter != testConverter);

        // built-in converters

        // by-id
        for (int i = 0, len = standardConverterIds.length; i < len; i++) {
            conv = app.createConverter(standardConverterIds[i]);
            assertTrue(null != conv);
            assertTrue(standardConverterClasses[i].isAssignableFrom(conv.getClass()));
        }

        // by-class
        for (int i = 0, len = standardConverterByIdClasses.length; i < len; i++) {
            // skip entries for which by-class registration doesn't make sense.
            if (null == standardConverterByIdClasses[i]) {
                continue;
            }
            conv = app.createConverter(standardConverterByIdClasses[i]);
            assertTrue("null != " + standardConverterByIdClasses[i].toString(),
                       null != conv);
            assertTrue(standardConverterClasses[i].isAssignableFrom(conv.getClass()));
        }

        // primitive classes
        for (int i = 0, len = standardConverterPrimitiveClasses.length; i <
            len; i++) {
            if (null == standardConverterPrimitiveClasses[i]) {
                continue;
            }
            conv = app.createConverter(standardConverterPrimitiveClasses[i]);
            assertTrue("null != " + standardConverterPrimitiveClasses[i].toString(), null != conv);
            assertTrue(standardConverterClasses[i].isAssignableFrom(conv.getClass()));
        }
    }

    private void converterNegative() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        boolean exceptionThrown = false;
        try {
            app.addConverter("William", "BillyBoy");
            app.createConverter("William");
        } catch (FacesException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        // non-existent mapping
        exceptionThrown = false;
        try {
            app.createConverter("Joebob");
        } catch (FacesException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    private void getConverterIds() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();

        Iterator iter = app.getConverterIds();
        assertTrue(null != iter);

        assertTrue(isSubset(standardConverterIds, iter));
    }

    private void validatorPositive() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();

        Validator newTestValidator = null;
        Validator testValidator = new LengthValidator();
        Validator val = null;

        // runtime addition

        app.addValidator("Billybob", "javax.faces.validator.LengthValidator");
        newTestValidator = app.createValidator("Billybob");
        assertTrue(null != newTestValidator && newTestValidator != testValidator);

        // test standard validators
        val = app.createValidator("javax.faces.DoubleRange");
        assertTrue(null != val && val instanceof Validator);
        val = app.createValidator("javax.faces.Length");
        assertTrue(null != val && val instanceof Validator);
        val = app.createValidator("javax.faces.LongRange");
        assertTrue(null != val && val instanceof Validator);
    }

    private void validatorNegative() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        boolean exceptionThrown = false;
        try {
            app.addValidator("William", "BillyBoy");
            app.createValidator("William");
        } catch (FacesException e) {
            exceptionThrown = true;
        }

        // non-existent mapping
        exceptionThrown = false;
        try {
            app.createValidator("Joebob");
        } catch (FacesException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    private void getValidatorIds() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();

        Iterator iter = app.getValidatorIds();
        assertTrue(null != iter);

        String standardValidatorIds[] = {
            "javax.faces.DoubleRange",
            "javax.faces.Length",
            "javax.faces.LongRange"
        };
        assertTrue(isSubset(standardValidatorIds, iter));
    }

    private String status="";

    public String getStatus() {
        return status;
    }

    public boolean isSubset(String[] subset, Iterator superset) {
        int i, len = subset.length;
        boolean[] hits = new boolean[len];
        String cur = null;
        for (i = 0; i < len; i++) {
            hits[i] = false;
        }


        // for each element in the superset, go through the entire subset,
        // marking our "hits" array if there is a match.
        while (superset.hasNext()) {
            cur = (String) superset.next();
            for (i = 0; i < len; i++) {
                if (cur.equals(subset[i])) {
                    hits[i] = true;
                }
            }
        }

        // if any of the hits array is false, return false;
        for (i = 0; i < len; i++) {
            if (!hits[i]) {
                return false;
            }
        }
        return true;
    }


}

