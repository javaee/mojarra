/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;
import java.io.StringWriter;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.sun.faces.sandbox.component.YuiTreeNode;

/**
 * @author Jason Lee
 *
 */
public class YuiTreeNodeRenderer extends Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (context == null) {
            throw new NullPointerException("param 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("param 'component' is null");
        }
        if (!(component instanceof YuiTreeNode)) {
            throw new FacesException("Expected an instance of YuiTreeNode.  Found " +
                    component.getClass().getName() + ".");
        }

        // suppress rendering if "rendered" property on the component is false.
        if (!component.isRendered()) {
            return;
        }

        YuiTreeNode treeNode = (YuiTreeNode)component;
        UIComponent labelFacet = treeNode.getFacet("label");
        ResponseWriter writer = context.getResponseWriter();
        
        String output = YuiRendererHelper.getRenderedOutput(context, writer, labelFacet);
        String name =  YuiRendererHelper.getJavascriptVar(treeNode);
        writer.write("var treeNode_" + name + " = new YAHOO.widget.HTMLNode('" + 
                output + "', treeNode_" + YuiRendererHelper.getJavascriptVar(treeNode.getParent()) + ", false, true);\n");
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
        super.encodeEnd(context, component);
    }
}