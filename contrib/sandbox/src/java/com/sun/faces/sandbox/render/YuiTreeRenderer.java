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
//        YuiConstants.CSS_SANDBOX
    };

    public boolean getRendersChildren() {
        return true;
    }
    
    public void encodeChildren(FacesContext context,
            UIComponent component) throws java.io.IOException {
        //
    }

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
        
        renderJavascript(context, writer, tree);
    }
    
    protected void renderJavascript(FacesContext context, ResponseWriter writer, YuiTree comp) throws IOException {
//        ResponseWriter writer = FacesContext.getCurrentInstance().getResponseWriter();
        /*
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("ID", comp.getId());
        fields.put("NODES", generateNodeScript("tree" + comp.getId() + ".getRoot()", comp.getModel()));
        
        writer.writeAttribute("type", "text/javascript", "type");
        Util.outputTemplate(this, "yui_tree_template.txt", fields);
        */
        String id = comp.getClientId(context);

        writer.startElement("script", null);
        String jsObjName = "tree" + id;
        writer.write("var " + jsObjName +";\n(function() {\n");
        writer.write("function init_" + jsObjName + "() {\n");
        writer.write(jsObjName + " = new YAHOO.widget.TreeView('" + id + "');\n");
        writer.write("var " + jsObjName + "_root = " + jsObjName + ".getRoot();\n");
        
        for (UIComponent child : comp.getChildren()) {
            if (child instanceof YuiTreeNode) {
                // render
                renderNode(writer, (YuiTreeNode)child, jsObjName+ "_root");
            } else {
                // Allow only nest treeNode components
                throw new FacesException("Component " + 
                        comp.toString() + 
                        " not expected type.  Expected: com.sun.faces.sandbox.component.YuiTreeNode.  Perhaps you're missing a tag?");
            }
        }
        
        writer.write(jsObjName + ".draw();\n}\n");
        writer.write("YAHOO.util.Event.addListener(window, \"load\", init_" + jsObjName + ");\n})();\n");
        
        writer.endElement("script");
    }
    
    protected void renderNode (ResponseWriter writer, YuiTreeNode comp, String parent) throws IOException {
        int count = 0;
        boolean contentsRendered = false; // allow only 1 non-YuiTreeNode child
        FacesContext context = FacesContext.getCurrentInstance();
        level++;
        String name = parent + "_" + level + "_" + count;
//        writer.write("var " + name + " = new YAHOO.widget.HTMLNode(null, " + parent + ", false, true);\n");
        for (UIComponent child : comp.getChildren()) {
            if (child instanceof YuiTreeNode) {
                renderNode(writer, (YuiTreeNode) child, name);
            } else {
                if (contentsRendered) {
                    throw new FacesException("More than one non-treeNode child was found.");
                }
                contentsRendered = true;
                StringWriter stringWriter = new StringWriter();
                ResponseWriter newWriter = writer.cloneWithWriter(stringWriter);
                context.setResponseWriter(newWriter);
                child.encodeAll(context);
                String output = stringWriter.toString();
                context.setResponseWriter(writer);
                writer.write("var " + name + " = new YAHOO.widget.HTMLNode('" + 
                        output + "', " + parent + ", false, true);\n");
//                writer.write(name + ".init('" + output + "', " + parent + ", false);\n");
            }
            count++;
        }
        level--;
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
}