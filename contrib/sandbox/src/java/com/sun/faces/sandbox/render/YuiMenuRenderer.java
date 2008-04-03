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

import com.sun.faces.sandbox.component.YuiMenuBase;
import com.sun.faces.sandbox.util.Util;
import com.sun.faces.sandbox.util.YuiConstants;

/**
 * This class is the base <code>Renderer</code> for all of the YUI menu components.
 * It is also the <code>Renderer</code> for the YUI menu object.  For an 
 * example of this component, see the Yahoo! UI example <a target="_blank"
 * href="http://developer.yahoo.com/yui/examples/menu/example07.html">page</a>.
 * 
 * @author Jason Lee
 *
 */
public class YuiMenuRenderer extends Renderer {
    /**
     * This String array lists all of the JavaScript files needed by this component.
     */
    protected static final String scriptIds[] = { 
        YuiConstants.JS_YAHOO_DOM_EVENT
        ,YuiConstants.JS_CONTAINER
        ,YuiConstants.JS_MENU
        ,YuiConstants.JS_SANDBOX_HELPER
        ,YuiConstants.JS_YUI_MENU_HELPER
    };
    protected String cssClass = "yuimenu";

    int idCount = 0;

    /**
     * This String array lists all of the CSS files needed by this component.
     */
    protected static final String cssIds[] = { 
        YuiConstants.CSS_MENU,
        YuiConstants.CSS_SANDBOX
    };

//    public boolean getRendersChildren() {
//        return true;
//    }

    protected String getCssClass() {
        return "yuimenu";
    }

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

        ResponseWriter writer = context.getResponseWriter();

        for (int i = 0; i < scriptIds.length; i++) {
            Util.linkJavascript(writer, scriptIds[i], true);
        }
        for (int i = 0; i < cssIds.length; i++) {
            Util.linkStyleSheet(writer, cssIds[i]);
        }
        
        writer.startElement("script",component);
        writer.writeAttribute("type", "text/javascript", "type");
        writer.write("YAHOO.widget.MenuItem.prototype.IMG_ROOT = \"" + 
                Util.generateStaticUri("/yui/menu/assets/") +"\";");
        writer.endElement("script");

//        YuiRendererHelper.renderSandboxMenuJavaScript(context, context.getResponseWriter(), component);
//        YuiRendererHelper.renderSandboxStylesheet(context, context.getResponseWriter(), component);

//        writer.startElement("span", component);
//        writer.writeAttribute("class", "yui-skin-sam1", "class");
        
        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(context), "id");
        writer.writeAttribute("class", getCssClass(), "class");
        writer.startElement("div", component);
        writer.writeAttribute("class", "bd", "class");

        writer.startElement("ul", component);
        writer.writeAttribute("class", "first-of-type", "class");

//        encodeChildren(context, component);
    }
    
    

//    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
//        for (UIComponent child : component.getChildren()) {
//            if (child instanceof YuiMenuItem) {
//                ((YuiMenuItem)child).setCssClass(getCssClass());
//            }
//            child.encodeAll(context);
//        }
//    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("ul");
        writer.endElement("div");
        writer.endElement("div");
//        writer.endElement("span");
        renderJavaScript(context, writer, (YuiMenuBase)component);
    }

    /**
     * This will render the JavaScript needed to instantiate the YUI menu object
     */
    // TODO:  this will likely have XHTML issues
    protected void renderJavaScript(FacesContext context, ResponseWriter writer, YuiMenuBase component) throws IOException {
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");

        String ctorArgs = buildConstructorArgs(component);
        String javaScript =
            "YAHOO.util.Event.onDOMReady(function() {" +
            "var oMenu_%%%JS_VAR%%% = new YAHOO.widget.Menu(\"%%%ID%%%\", {" + ctorArgs + "}); oMenu_%%%JS_VAR%%%.render(); oMenu_%%%JS_VAR%%%.show();});";
        javaScript = javaScript
            .replaceAll("%%%ID%%%",     component.getClientId(context))
            .replaceAll("%%%JS_VAR%%%", YuiRendererHelper.getJavascriptVar(component));
        writer.writeText(javaScript , null);
        writer.endElement("script");
    }

    /**
     * A helper method to create the constructor arguments for the JavaScript 
     * object instantiation.
     * @return the JavaScript associative array text (minus the curly braces) representing the desired arguments
     */
    protected String buildConstructorArgs(YuiMenuBase component) {
        return "width: \"" + component.getWidth() + 
        "\", autosubmenudisplay: " + component.getAutoShow() + 
        ", clicktohide: false, visible: true";
    }

/*
    public void encodeBegin1(FacesContext context, UIComponent component) throws IOException {
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

        ResponseWriter writer = context.getResponseWriter();

        for (int i = 0; i < scriptIds.length; i++) {
            Util.linkJavascript(writer, scriptIds[i], true);
        }
        for (int i = 0; i < cssIds.length; i++) {
            Util.linkStyleSheet(writer, cssIds[i]);
        }

        YuiRendererHelper.renderSandboxMenuJavaScript(context, context.getResponseWriter(), component);
        YuiRendererHelper.renderSandboxStylesheet(context, context.getResponseWriter(), component);
    }

    public void encodeEnd2(FacesContext context, UIComponent component) throws IOException {
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

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", component);
        writer.writeAttribute("class", "yui-skin-sam", "class");
        idCount = 0;
        renderMenu(writer, (YuiMenuBase)component);
        writer.endElement("div");
        renderJavaScript(context, writer, (YuiMenuBase)component);
    }
//  public void encodeEnd(FacesContext context, UIComponent component)
//  throws IOException {
//  ResponseWriter writer = context.getResponseWriter();
////writer.endElement("div");
//  }

    protected void renderMenu (ResponseWriter writer, UIComponent component) throws IOException {
        idCount++;
        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(FacesContext.getCurrentInstance()) + "_" + idCount, "id");
        writer.writeAttribute("style", "padding: 0px;", "style");
        writer.writeAttribute("class", getCssClass(), "class");

        writer.startElement("div", component);
        writer.writeAttribute("class", "bd", "class");

        writer.startElement("ul", component);
        writer.writeAttribute("class", "first-of-type", "class");

        for (UIComponent child : component.getChildren()) {
            if ((child instanceof YuiMenuItem) && child.isRendered()) {
                renderMenuItem(writer, (YuiMenuItem)child);
            } else {
                child.encodeAll(FacesContext.getCurrentInstance());
            }
        }


        writer.endElement("ul");
        writer.endElement("div");
        writer.endElement("div");
    }

    protected void renderMenuItem (ResponseWriter writer, YuiMenuItem menuItem) throws IOException {
        idCount++;
        writer.startElement("li", menuItem);
        writer.writeAttribute("id", menuItem.getClientId(FacesContext.getCurrentInstance()) + "_" + idCount, "id");
        writer.writeAttribute("class", "yuimenuitem", "class");
        String label = (String)menuItem.getValue();
        String url = menuItem.getUrl();

        if (label != null) {
            if ((url != null) && (!"".equals(url.trim()))) { 
                writer.startElement("a", menuItem);
                writer.writeAttribute("href", menuItem.getUrl(), "href");
                writer.writeText(menuItem.getValue(), null);
                writer.endElement("a");
            } else {
                writer.writeText(menuItem.getValue(), null);
            }
        }
        if (menuItem.getChildCount() > 0) {
            for (UIComponent child: menuItem.getChildren()) {
                if (child instanceof YuiMenuItem) {
                    renderMenu(writer, child);
                } else {
                    // 1.1 doesn't support encodeAll()
                    child.encodeBegin(FacesContext.getCurrentInstance());
                    child.encodeChildren(FacesContext.getCurrentInstance());
                    child.encodeEnd(FacesContext.getCurrentInstance());
                }
            }
        }

        writer.endElement("li");
    }
*/
}