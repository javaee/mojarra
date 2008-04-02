/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;

import javax.faces.context.ResponseWriter;

import com.sun.faces.sandbox.component.YuiMenuBase;
import com.sun.faces.sandbox.model.Menu;

/**
 * This <code>Renderer</code> will render an application-style menu bar. For an 
 * example of this component, see the Yahoo! UI example <a target="_blank"
 * href="http://developer.yahoo.com/yui/examples/menu/topnavfrommarkup.html">page</a>.
 * @author Jason Lee
 * 
 */
public class YuiMenuBarRenderer extends YuiMenuRenderer {
    // TODO:  this will likely have XHTML issues
    protected void renderJavaScript(ResponseWriter writer, YuiMenuBase component, Menu menu) throws IOException {
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");

        writer.writeText(("var oMenu_%%%ID%%% = new YAHOO.widget.MenuBar(\"%%%ID%%%\", {" +
                buildConstructorArgs(component) + "});")
                .replaceAll("%%%ID%%%", component.getId() + "_1"), null);
        writer.writeText("oMenu_%%%ID%%%.render();"
                .replaceAll("%%%ID%%%", component.getId() + "_1"), null);
        writer.endElement("script");
    }

    protected String buildConstructorArgs(YuiMenuBase component) {
        return "width: \"" + component.getWidth() + "\"";
    }
}