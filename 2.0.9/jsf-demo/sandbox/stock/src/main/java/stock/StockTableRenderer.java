/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package stock;


import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

/**
 * <B>StockTableRenderer</B> is a class that renders <code>UIPanel</code> component
 * as a "Grid".
 */

public class StockTableRenderer extends Renderer {


    // ---------------------------------------------------------- Public Methods


    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        // Render the beginning of this panel
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("table", component);

        String id = component.getId();
        if ((null != component.getId()) && 
            !id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX)) {
            writer.writeAttribute("id", id = component.getClientId(context), "id");
        }

        String styleClass = (String) component.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        String border = (String) component.getAttributes().get("border");
        if (border != null) {
            writer.writeAttribute("border", border, "border");
        }
        String cellspacing = (String) component.getAttributes().get("cellspacing");
        if (cellspacing != null) {
            writer.writeAttribute("cellspacing", cellspacing, "cellspacing");
        }

        writer.writeText("\n", component, null);
    }


    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        String columns = (String)component.getAttributes().get("columns");
        int count = Integer.valueOf(columns).intValue();

        boolean open = false;
        int i = 0;

        writer.startElement("tbody", component);
        writer.writeText("\n", component, null);

        for (Iterator<UIComponent> kids = getChildren(component);
            kids.hasNext();) {
            UIComponent child = kids.next();
            if (!child.isRendered()) {
                continue;
            }
            if ((i % count) == 0) {
                if (open) {
                    writer.endElement("tr");
                    writer.writeText("\n", component, null);
                    open = false;
                }
                writer.startElement("tr", component);
                writer.writeText("\n", component, null);
                open = true;
            }

            renderRow(context, component, child, writer);
            i++;
        }
        if (open) {
            writer.endElement("tr");
            writer.writeText("\n", component, null);
        }
        writer.endElement("tbody");
        writer.writeText("\n", component, null);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("table");
        writer.writeText("\n", component, null);
    }

    @Override
    public boolean getRendersChildren() {

        return true;

    }

    private void renderRow(FacesContext context,
                             UIComponent table,
                             UIComponent child,
                             ResponseWriter writer)
        throws IOException {
        writer.startElement("td", table);
        encodeRecursive(context, child);
        writer.endElement("td");
        writer.writeText("\n", table, null);
    }

    private Iterator<UIComponent> getChildren(UIComponent component) {
        int childCount = component.getChildCount();
        if (childCount > 0) {
            return component.getChildren().iterator();
        } else {
            return Collections.<UIComponent>emptyList().iterator();
        }

    }

    private void encodeRecursive(FacesContext context, UIComponent component)
        throws IOException {

        // Render this component and its children recursively
        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator<UIComponent> kids = getChildren(component);
            while (kids.hasNext()) {
                UIComponent kid = kids.next();
                encodeRecursive(context, kid);
            }
        }
        component.encodeEnd(context);
    }

}
