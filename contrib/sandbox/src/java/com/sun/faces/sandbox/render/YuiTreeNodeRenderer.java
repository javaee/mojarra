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
        YuiTreeNode treeNode = (YuiTreeNode)component;
        UIComponent labelFacet = treeNode.getFacet("label");
        ResponseWriter writer = context.getResponseWriter();
        
        StringWriter stringWriter = new StringWriter();
        ResponseWriter newWriter = writer.cloneWithWriter(stringWriter);
        context.setResponseWriter(newWriter);
        labelFacet.encodeAll(context);
        String output = stringWriter.toString();
        output = HTMLEntityEncode(output);
        output = output.trim();
        context.setResponseWriter(writer);
        String name =  YuiRendererHelper.getJavascriptVar(treeNode);
        writer.write("var treeNode_" + name + " = new YAHOO.widget.HTMLNode('" + 
                output + "', treeNode_" + YuiRendererHelper.getJavascriptVar(treeNode.getParent()) + ", false, true);\n");
    }
    
    private String HTMLEntityEncode( String s )
    {
        /*
        StringBuffer buf = new StringBuffer();
        for ( int i = 0; i < s.length(); i++ )
        {
            char c = s.charAt( i );
            if ( c>='a' && c<='z' || c>='A' && c<='Z' || c>='0' && c<='9' )
            {
                buf.append( c );
            }
            else
            {
                buf.append( "&#" + (int)c + ";" );
            }
        }
        return buf.toString();
        */
        return s.replaceAll("\\n", "")
            .replaceAll("'", "\\\\'");
    }
}
