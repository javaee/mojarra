/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.sun.faces.sandbox.component.YuiTree;
import com.sun.faces.sandbox.model.HtmlNode;
import com.sun.faces.sandbox.model.MenuNode;
import com.sun.faces.sandbox.model.TextNode;
import com.sun.faces.sandbox.model.TreeNode;
import com.sun.faces.sandbox.util.Util;
import com.sun.faces.sandbox.util.YuiConstants;

/**
 * @author lee
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

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        
        YuiTree tree = (YuiTree) component;
        // Render the div that will hold the tree
        ResponseWriter writer = context.getResponseWriter();

        for (int i = 0; i < scriptIds.length; i++) {
            Util.linkJavascript(writer, scriptIds[i]);
        }
        for (int i = 0; i < cssIds.length; i++) {
            Util.linkStyleSheet(writer, cssIds[i]);
        }
        
        writer.startElement("div", tree);
        writer.writeAttribute("id", component.getId(), "id");
        Util.renderPassThruAttributes(writer, component);
        writer.endElement("div");
        
        renderJavascript(tree);
    }
    
    protected String generateNodeScript(String parentNode, TreeNode node) {
        StringBuilder ret = new StringBuilder();
        int count = 0;
        level++;
        for (TreeNode child : node.getChildren()) {
            String nodeName = "node_" + level + "_" + count;
            ret.append("var ")
                .append(nodeName)
                .append(" = new ");
            if (child instanceof TextNode) {
                ret.append(buildTextNodeCtor(child, parentNode));
            } else if (child instanceof HtmlNode) {
                ret.append(this.buildHtmlNodeCtor(child, parentNode));
            } else if (child instanceof MenuNode) {
                ret.append(this.buildMenuNodeCtor(child, parentNode));
            }
            ret.append (";\n");
            if (child.hasChildren()) {
                ret.append(generateNodeScript(nodeName, child));
            }
            count++;
        }
        level--;
        
        return ret.toString();
    }
    
    protected String buildTextNodeCtor(TreeNode node, String parentNode) {
        return "YAHOO.widget.TextNode(\"" + node.getLabel() + "\", " +
            parentNode + ",false)";
    }
    
    protected String buildMenuNodeCtor(TreeNode node, String parentNode) {
        return "YAHOO.widget.MenuNode({label: \"" + node.getLabel() + "\", href: \"" +
            node.getContent() + "\"}, " + parentNode + ",false)";
    }

    protected String buildHtmlNodeCtor(TreeNode node, String parentNode) {
        return "YAHOO.widget.HTMLNode(\"" + node.getContent() + "\", " +
            parentNode + ",false, true)";
    }

    protected void renderJavascript(YuiTree comp) throws IOException {
        ResponseWriter writer = FacesContext.getCurrentInstance().getResponseWriter();
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("ID", comp.getId());
        fields.put("NODES", generateNodeScript("tree" + comp.getId() + ".getRoot()", comp.getModel()));
        
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", "type");
        Util.outputTemplate(this, "yui_tree_template.txt", fields);
        
        writer.endElement("script");
    }
}