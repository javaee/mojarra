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

/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.sun.faces.sandbox.component.YuiTree;
import com.sun.faces.sandbox.util.Util;
import com.sun.faces.sandbox.util.YuiConstants;

/**
 * @author Jason Lee
 *
 */
public class YuiTreeRenderer extends Renderer {
    //public static int level = 0;
    private static final String scriptIds[] = {
        YuiConstants.JS_YAHOO_DOM_EVENT,
        YuiConstants.JS_TREEVIEW,
        YuiConstants.JS_YUI_TREEVIEW_HELPER
    };

    private static final String cssIds[] = { 
        YuiConstants.CSS_TREEVIEW
    };

    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {

        if (context == null) {
            throw new NullPointerException("param 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("param 'component' is null");
        }

        // suppress rendering if "rendered" property on the component is false.
        if (!component.isRendered()) {
            return;
        }

        YuiTree tree = (YuiTree) component;
        // Render the div that will hold the tree
        ResponseWriter writer = context.getResponseWriter();

        for (int i = 0; i < scriptIds.length; i++) {
            Util.linkJavascript(writer, scriptIds[i], true);
        }
        for (int i = 0; i < cssIds.length; i++) {
            Util.linkStyleSheet(writer, cssIds[i]);
        }

        YuiRendererHelper.renderSandboxStylesheet(context, writer, tree);

        writer.startElement("div", tree);
        writer.writeAttribute("id", component.getId(), "id");
        Util.renderPassThruAttributes(writer, component);

        writer.startElement("ul", null);
//        tree.encodeChildren(context);
    }
    
    

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("param 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("param 'component' is null");
        }

        // suppress rendering if "rendered" property on the component is false.
        if (!component.isRendered()) {
            return;
        }

        YuiTree tree = (YuiTree) component;
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("ul");
        writer.endElement("div");

        String id = YuiRendererHelper.getJavascriptVar(component);
        String jsObjName = "tree" + id;

        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", "type");
        writer.write("var " + jsObjName +";");
        writer.write("(function() {");
        writer.write("function init_" + jsObjName + "() {");
        writer.write(jsObjName + " = new YAHOO.widget.TreeView('" + component.getId() + "');");
        writer.write(jsObjName + ".readList();");
        writer.write(jsObjName + ".draw();");
        if (tree.getShowExpanded() == true) {
            writer.write(jsObjName + ".expandAll();");
        }
        writer.write("} YAHOO.util.Event.addListener(window, \"load\", init_" + jsObjName + ");})();");

        writer.endElement("script");
    }

    @Override
    public boolean getRendersChildren() {
        return false;
    }
}