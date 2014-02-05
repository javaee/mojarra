
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
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import javax.faces.view.facelets.FaceletException;


@FacesRenderer(componentFamily="javax.faces.Output", rendererType="jsp.GetProperty")
public class GetPropertyRenderer extends Renderer {

    @Override
    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
        Map<String, Object> attrs = component.getAttributes();
        String nameVal = (String) attrs.get("name");
        String propVal = (String) attrs.get("property");
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory ef = facesContext.getApplication().getExpressionFactory();

        // Get the bean
        ValueExpression valExpression = ef.createValueExpression(elContext,
                "#{" + nameVal + "." + propVal + "}", Object.class);
        Object bean = null;
        try {
            bean = valExpression.getValue(elContext);
        } catch (Exception e) {
            throw new FaceletException("Expression " + valExpression.getExpressionString() + " not found.", e);
        }
        ResponseWriter out = facesContext.getResponseWriter();
        out.writeText(bean, component, valExpression.getExpressionString());
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    }


}
