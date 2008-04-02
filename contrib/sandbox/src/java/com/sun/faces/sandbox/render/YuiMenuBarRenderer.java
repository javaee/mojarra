/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.sandbox.component.YuiMenuBase;

/**
 * This <code>Renderer</code> will render an application-style menu bar. For an 
 * example of this component, see the Yahoo! UI example <a target="_blank"
 * href="http://developer.yahoo.com/yui/examples/menu/topnavfrommarkup.html">page</a>.
 * @author Jason Lee
 * 
 */
public class YuiMenuBarRenderer extends YuiMenuRenderer {
    protected String cssClass = "yuimenubar";
    /*
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("style", component);
        writer.writeAttribute("type", "text/css", "type");
        writer.writeText("div #" + component.getId() + "_1 { height: 20px; }", null);
        writer.endElement("style");
    }
    */
    
    protected String getCssClass() {
        return "yuimenubar";
    }

    // TODO:  this will likely have XHTML issues
    protected void renderJavaScript(ResponseWriter writer, YuiMenuBase component) throws IOException {
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");

        String javaScript = "var oMenu_%%%JS_VAR%%% = new YAHOO.widget.MenuBar(\"%%%ID%%%\", {" +
            buildConstructorArgs(component) + "}); oMenu_%%%JS_VAR%%%.render();" ;
        javaScript = javaScript.replaceAll("%%%JS_VAR%%%",  
            YuiRendererHelper.getJavascriptVar(component) + "_1")
            .replaceAll("%%%ID%%%", component.getClientId(FacesContext.getCurrentInstance()) + "_1");
        writer.writeText(javaScript, null);
        writer.endElement("script");
    }

    protected String buildConstructorArgs(YuiMenuBase component) {
        return "width: \"" + component.getWidth() + "\", autosubmenudisplay: true, visible: true";
    }

    @Override
    protected void renderMenu(ResponseWriter writer, UIComponent component) throws IOException {
        super.renderMenu(writer, component);
    }
}