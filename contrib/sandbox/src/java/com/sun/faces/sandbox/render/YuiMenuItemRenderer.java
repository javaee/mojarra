package com.sun.faces.sandbox.render;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.sun.faces.sandbox.component.YuiContextMenu;
import com.sun.faces.sandbox.component.YuiMenu;
import com.sun.faces.sandbox.component.YuiMenuBar;
import com.sun.faces.sandbox.component.YuiMenuItem;

public class YuiMenuItemRenderer extends Renderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
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
        YuiMenuItem menuItem = (YuiMenuItem) component;

        writer.startElement("li", component);
        writer.writeAttribute("class", "yuimenuitem", "class");
//        if (component.getParent().getParent() instanceof YuiMenu) {
//            writer.writeAttribute("class", "yuimenuitem", "class");
//        } else if (component.getParent().getParent() instanceof YuiMenuBar) {
//            writer.writeAttribute("class", "yuimenubaritem", "class");
//        } else  if (component.getParent().getParent() instanceof YuiContextMenu) {
//            writer.writeAttribute("class", "yuicontextmenuitem", "class");
//        }

        String label = (String)menuItem.getValue();
        String url = menuItem.getUrl();

        if (label != null) {
            if ((url != null) && (!"".equals(url.trim()))) { 
                writer.startElement("a", menuItem);
//                if (component.getParent().getParent() instanceof YuiMenu) {
//                    writer.writeAttribute("class", "yuimenuitemlabel", "class");
//                } else if (component.getParent().getParent() instanceof YuiMenuBar) {
//                    writer.writeAttribute("class", "yuimenubaritemlabel", "class");
//                } else  if (component.getParent().getParent() instanceof YuiContextMenu) {
//                    writer.writeAttribute("class", "yuicontextmenuitemlabel", "class");
//                }
                writer.writeAttribute("href", menuItem.getUrl(), "href");
                writer.writeText(menuItem.getValue(), null);
                writer.endElement("a");
            } else {
                writer.writeText(menuItem.getValue(), null);
            }
        }
        
        writer.endElement("li");
    }
}
