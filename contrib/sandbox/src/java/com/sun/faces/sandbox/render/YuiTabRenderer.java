/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

/**
 * @author Jason Lee
 *
 */
public class YuiTabRenderer extends Renderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", component);
        writer.startElement("p", component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();    
        writer.endElement("p");
        writer.endElement("div");
    }
}
