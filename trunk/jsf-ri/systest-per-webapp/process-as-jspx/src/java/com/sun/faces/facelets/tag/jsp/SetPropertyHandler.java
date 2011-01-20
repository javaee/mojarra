
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

package com.sun.faces.facelets.tag.jsp;

import java.io.IOException;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;




public class SetPropertyHandler extends TagHandler {

    private final TagAttribute name;

    private final TagAttribute property;

    private final TagAttribute param;

    private final TagAttribute value;

    public SetPropertyHandler(TagConfig config) {
        super(config);
        this.name = this.getRequiredAttribute("name");
        this.property = this.getRequiredAttribute("property");
        this.param = this.getAttribute("param");
        this.value = this.getAttribute("value");

    }

    public void apply(FaceletContext fc, UIComponent uic) throws IOException {
        FacesContext facesContext = fc.getFacesContext();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory ef = facesContext.getApplication().getExpressionFactory();

        // Get the bean
        String nameVal = this.name.getValue(fc);
        ValueExpression valExpression = ef.createValueExpression(elContext,
                "#{" + nameVal + "}", Object.class);
        Object bean = valExpression.getValue(elContext);
        if (null == bean) {
            throw new FaceletException("Bean " + nameVal + " not found.");
        }

        // Get the name of the property of the bean to set
        String propertyVal = this.property.getValue(fc);
        String lhs = null;
        Object rhs = null;
        if (propertyVal.equals("*")) {
            pushAllRequestParamatersToBeanProperties(facesContext, elContext, ef, bean);
        } else {
            // If both are set, it is a user error.
            if (null != this.param && null != this.value) {
                throw new FaceletException("You cannot use both the param and value attributes in a <jsp:setProperty> element.");
            }
            // if neither param nor value have values, assume the name of
            // the request parameter is equal to propertyVal
            if (null == this.param && null == this.value) {
                lhs = propertyVal;
            } else {
                // one of param or value have a value.
                if (null != this.param) {
                    lhs = this.param.getValue(fc);
                }
                if (null != this.value) {
                    lhs = propertyVal;
                    rhs = this.value.getValue(fc);
                }
            }
            if (null == rhs) {
                rhs = facesContext.getExternalContext().getRequestParameterMap().get(lhs);
            }
            ELResolver resolver = elContext.getELResolver();
            resolver.setValue(elContext, bean, lhs, rhs);
        }

        nextHandler.apply(fc, uic);

    }

    private void pushAllRequestParamatersToBeanProperties(FacesContext facesContext,
            ELContext elContext, ExpressionFactory ef, Object bean) {
        ExternalContext extContext = facesContext.getExternalContext();
        ELResolver resolver = elContext.getELResolver();
        Map<String, String []> requestParamValues =
                extContext.getRequestParameterValuesMap();
        String [] values;
        for (String cur : requestParamValues.keySet()) {
            values = requestParamValues.get(cur);
            for (String curVal : values) {
                resolver.setValue(elContext, bean, cur, curVal);
            }
        }
    }


}
