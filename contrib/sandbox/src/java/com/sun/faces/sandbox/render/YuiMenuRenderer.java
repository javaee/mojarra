/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.sun.faces.sandbox.component.YuiMenuBase;
import com.sun.faces.sandbox.model.Menu;
import com.sun.faces.sandbox.model.MenuItem;
import com.sun.faces.sandbox.util.Util;
import com.sun.faces.sandbox.util.YuiConstants;

/**
 * @author Jason Lee
 *
 */
public class YuiMenuRenderer extends Renderer {
    protected static final String scriptIds[] = { 
         YuiConstants.JS_YAHOO_DOM_EVENT
        ,YuiConstants.JS_CONTAINER
        ,YuiConstants.JS_MENU
        ,YuiConstants.JS_YUI_MENU_HELPER
    };

    protected static final String cssIds[] = { 
        YuiConstants.CSS_MENU 
    };
    protected static final Logger logger = Logger.getLogger(YuiMenuRenderer.class.getName());
    
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        for (int i = 0; i < scriptIds.length; i++) {
            Util.linkJavascript(context.getResponseWriter(), scriptIds[i]);
        }

        for (int i = 0; i < cssIds.length; i++) {
            Util.linkStyleSheet(context.getResponseWriter(), cssIds[i]);
        }
    }
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }

        ValueHolder vh = (ValueHolder)component;
        Menu menu = (Menu)vh.getValue();
        
        ResponseWriter writer = context.getResponseWriter();
        renderMenu(writer, (YuiMenuBase)component, menu);
        renderJavaScript(writer, (YuiMenuBase)component, menu);
    }
    
    protected void renderMenu (ResponseWriter writer, YuiMenuBase component, Menu menu) throws IOException {
        writer.startElement("div", component);
        writer.writeAttribute("id", component.getId(), "id");
        writer.writeAttribute("class", "yuimenu", "class");
        
        writer.startElement("div", component);
        writer.writeAttribute("class", "bd", "class");
        
        writer.startElement("ul", component);
        writer.writeAttribute("class", "first-of-type", "class");
        
        for (MenuItem item : menu.getMenuItems()) {
            renderMenuItem(writer, component, item);
        }
        
        writer.endElement("ul");
        writer.endElement("div");
        writer.endElement("div");
    }
    
    protected void renderMenuItem (ResponseWriter writer, YuiMenuBase component, MenuItem item) throws IOException {
        writer.startElement("li", component);
        writer.writeAttribute("class", "yuimenuitem", "class");
        
        writer.startElement("a", component);
        writer.writeAttribute ("href", item.getLink(), "href");
        writer.writeText(item.getLabel(), null);
        writer.endElement("a");
        
        if (item.getSubMenu() != null) {
            renderMenu(writer, component, item.getSubMenu());
        }
        writer.endElement("li");
    }
    
    // TODO:  this will likely have XHTML issues
    protected void renderJavaScript(ResponseWriter writer, YuiMenuBase component, Menu menu) throws IOException {
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");
        
        String ctorArgs = buildConstructorArgs(component);
        writer.writeText(("var oMenu_%%%ID%%% = new YUISF.Menu(\"%%%ID%%%\", {" + ctorArgs + 
                "});").replaceAll("%%%ID%%%", component.getId()) , null);
        writer.endElement("script");
    }
    
    protected String buildConstructorArgs(YuiMenuBase component) {
        return "width: " + component.getWidth() + ", clicktohide: false, visible: false";
    }

    protected String getFullyQualifiedId(String id) {
        String fqid = null;
        
        List<UIComponent> components = FacesContext.getCurrentInstance()
            .getViewRoot().getChildren();
        // We can assume components is non-null, because if it were, the component
        // that caused this loop to execute would not exist
        for (UIComponent component : components) {
            fqid = checkChildren (component, id);
            if (fqid != null) {
                break;
            }
        }
        
        return fqid != null ? fqid : id;
    }
    
    protected String checkChildren (UIComponent component, String id) {
        String compId = component.getId(); 
        if (compId.equals(id)) {
            return component.getClientId(FacesContext.getCurrentInstance());
        }
        
        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                String result = checkChildren(child, id);
                if (result != null) {
                    return result;
                }
            }
        }
        
        return null;
    }

}