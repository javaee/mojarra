package com.sun.faces.sandbox.render;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.sun.faces.sandbox.component.YuiContextMenu;
import com.sun.faces.sandbox.component.YuiMenu;
import com.sun.faces.sandbox.component.YuiMenuBar;
import com.sun.faces.sandbox.component.YuiSubMenu;

public class YuiSubMenuRenderer extends Renderer {
    @Override
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
        YuiSubMenu submenu = (YuiSubMenu) component;
        
        writer.startElement("li", component);
//        writer.writeAttribute("class", "yuimenuitem", "class");
        if (component.getParent() instanceof YuiMenu) {
            writer.writeAttribute("class", "yuimenuitem", "class");
        } else if (component.getParent() instanceof YuiMenuBar) {
            writer.writeAttribute("class", "yuimenubaritem", "class");
        } else  if (component.getParent() instanceof YuiContextMenu) {
//            writer.writeAttribute("class", "yuicontextmenuitem", "class");
        }
        String label = (String)submenu.getValue();
        String url = submenu.getUrl();

        if (label != null) {
            if ((url != null) && (!"".equals(url.trim()))) { 
                writer.startElement("a", submenu);
                if (component.getParent() instanceof YuiMenu) {
                    writer.writeAttribute("class", "yuimenuitemlabel", "class");
                } else if (component.getParent() instanceof YuiMenuBar) {
                    writer.writeAttribute("class", "yuimenubaritemlabel", "class");
                } else  if (component.getParent() instanceof YuiContextMenu) {
//                    writer.writeAttribute("class", "yuicontextmenuitemlabel", "class");
                }

                writer.writeAttribute("href", submenu.getUrl(), "href");
                writer.writeText(submenu.getValue(), null);
                writer.endElement("a");
            } else {
                writer.writeText(submenu.getValue(), null);
            }
        }

        writer.startElement("div", component);
        writer.writeAttribute("id", component.getId(), "id");
//        writer.writeAttribute("style", "padding: 0px;", "style");
        if (component.getParent() instanceof YuiMenu) {
            writer.writeAttribute("class", "yuimenu", "class");
        } else if (component.getParent() instanceof YuiMenuBar) {
            writer.writeAttribute("class", "yuimenubar", "class");
        } else  if (component.getParent() instanceof YuiContextMenu) {
//            writer.writeAttribute("class", "yuicontextmenu", "class");
        }

        writer.startElement("div", component);
        writer.writeAttribute("class", "bd", "class");

        writer.startElement("ul", component);
//        writer.writeAttribute("class", "first-of-type", "class");
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        writer.endElement("ul");
        writer.endElement("div");
        writer.endElement("div");
        writer.endElement("li");
    }
    
}