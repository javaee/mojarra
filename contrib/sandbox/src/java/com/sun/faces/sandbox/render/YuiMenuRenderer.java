/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.shale.remoting.Mechanism;

import com.sun.faces.sandbox.component.YuiMenuBase;
import com.sun.faces.sandbox.component.YuiMenuItem;
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
        ,YuiConstants.JS_YUI_MENU_HELPER
    };
    
    int idCount = 0;

    /**
     * This String array lists all of the CSS files needed by this component.
     */
    protected static final String cssIds[] = { 
        YuiConstants.CSS_MENU 
    };

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext context,
            UIComponent component) throws java.io.IOException {
        //
    }


    /**
     * This method will output the necessary JavaScript and CSS references to enable the 
     * JavaScript object creation.
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
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
        
        YuiRendererHelper.renderSandboxJavaScript(context, context.getResponseWriter(), component);
    }

    /**
     * All component rendering will be done in this method.
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }

        ResponseWriter writer = context.getResponseWriter();
        idCount = 0;
        renderMenu(writer, (YuiMenuBase)component);
        renderJavaScript(writer, (YuiMenuBase)component);
    }

    /**
     * This will render the menu and its subitems.
     * @throws IOException
     * @see com.sun.faces.sandbox.model.Menu
     */
    protected void renderMenu (ResponseWriter writer, UIComponent component) throws IOException {
        idCount++;
        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(FacesContext.getCurrentInstance()) + "_" + idCount, "id");
        writer.writeAttribute("style", "padding: 0px;", "style");
        writer.writeAttribute("class", "yuimenu", "class");

        writer.startElement("div", component);
        writer.writeAttribute("class", "bd", "class");

        writer.startElement("ul", component);
        writer.writeAttribute("class", "first-of-type", "class");

        for (UIComponent child : component.getChildren()) {
            if (child instanceof YuiMenuItem) {
                renderMenuItem(writer, (YuiMenuItem)child);
            } else {
                child.encodeAll(FacesContext.getCurrentInstance());
            }
        }


        writer.endElement("ul");
        writer.endElement("div");
        writer.endElement("div");
    }

    /**
     * This will render the given <code>MenuItem</code>.  If the <code>MenuItem</code>
     * has a nested <code>Menu</code>, <code>renderMenu</code> is called to handle it.  
     * @throws IOException
     * @see com.sun.faces.sandbox.model.Menu
     * @see com.sun.faces.sandbox.model.MenuItem
     */
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
                    renderMenu(writer, menuItem);
                } else {
                    child.encodeAll(FacesContext.getCurrentInstance());
                }
            }
        }

        writer.endElement("li");
    }

    /**
     * This will render the JavaScript needed to instantiate the YUI menu object
     */
    // TODO:  this will likely have XHTML issues
    protected void renderJavaScript(ResponseWriter writer, YuiMenuBase component) throws IOException {
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");

        String ctorArgs = buildConstructorArgs(component);
        String javaScript = "var oMenu_%%%JS_VAR%%% = new YUISF.Menu(\"%%%ID%%%\", {" + ctorArgs + "});";
        javaScript = javaScript.replaceAll("%%%ID%%%", component.getClientId(FacesContext.getCurrentInstance()) + "_1")
            .replaceAll("%%%JS_VAR%%%", getJavascriptVar(component.getClientId(FacesContext.getCurrentInstance()) + "_1"));
        writer.writeText(javaScript , null);
        writer.endElement("script");
    }
    
    // Return a JavaScript-friendly variable name
    protected String getJavascriptVar(String name) {
        return name.replaceAll(":", "_");
    }

    /**
     * A helper method to create the constructor arguments for the JavaScript 
     * object instantiation.
     * @return the JavaScript associative array text (minus the curly braces) representing the desired arguments
     */
    protected String buildConstructorArgs(YuiMenuBase component) {
        return "width: \"" + component.getWidth() + "\", clicktohide: false, visible: true";
    }
}
