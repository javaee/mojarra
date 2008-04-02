/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.shale.remoting.Mechanism;

import com.sun.faces.sandbox.component.YuiTree;
import com.sun.faces.sandbox.component.YuiTreeNode;
import com.sun.faces.sandbox.model.HtmlNode;
import com.sun.faces.sandbox.model.MenuNode;
import com.sun.faces.sandbox.model.TextNode;
import com.sun.faces.sandbox.model.TreeNode;
import com.sun.faces.sandbox.util.Util;
import com.sun.faces.sandbox.util.YuiConstants;

/**
 * @author Jason Lee
 *
 */
public class YuiTreeRenderer extends Renderer {
    public static int level = 0;
    private static final String scriptIds[] = {
        YuiConstants.JS_YAHOO_DOM_EVENT,
        YuiConstants.JS_TREEVIEW
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
            Util.getXhtmlHelper().linkJavascript(context, component,
                    context.getResponseWriter(), Mechanism.CLASS_RESOURCE,
                    scriptIds[i]);
        }
        for (int i = 0; i < cssIds.length; i++) {
            Util.getXhtmlHelper().linkStylesheet(context, component,
                    context.getResponseWriter(), Mechanism.CLASS_RESOURCE,
                    cssIds[i]);
        }

        YuiRendererHelper.renderSandboxStylesheet(context, writer, tree);

        writer.startElement("div", tree);
        writer.writeAttribute("id", component.getId(), "id");
        Util.renderPassThruAttributes(writer, component);
        writer.endElement("div");

        String id = YuiRendererHelper.getJavascriptVar(component);
        String jsObjName = "tree" + id;

        writer.startElement("script", null);
        writer.write("var " + jsObjName +";\n(function() {\n");
        writer.write("function init_" + jsObjName + "() {\n");
        writer.write(jsObjName + " = new YAHOO.widget.TreeView('" + id + "');\n");
        writer.write("var treeNode_" + id + " = " + jsObjName + ".getRoot();\n");
    }
    
    

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String id = YuiRendererHelper.getJavascriptVar(component);
        String jsObjName = "tree" + id;

        writer.write(jsObjName + ".draw();\n}\n");
        writer.write("YAHOO.util.Event.addListener(window, \"load\", init_" + jsObjName + ");\n})();\n");

        writer.endElement("script");
    }
}