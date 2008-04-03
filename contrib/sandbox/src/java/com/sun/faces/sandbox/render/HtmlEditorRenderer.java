/**
 * 
 */
package com.sun.faces.sandbox.render;

import com.sun.faces.sandbox.component.HtmlEditor;
import com.sun.faces.sandbox.component.YuiCalendar;
import com.sun.faces.sandbox.util.Util;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.shale.remoting.Mechanism;

/**
 * @author Jason Lee
 *
 */
public class HtmlEditorRenderer extends Renderer {
    private static final String TINY_MCE = "/tiny_mce/tiny_mce_src.js";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        
        if (!(component instanceof HtmlEditor)) {
            throw new IllegalStateException ("Was expecting HtmlEditor.  Found " +
                    component.getClass().getName());
        }
        
        HtmlEditor editor = (HtmlEditor)component;
        String id = editor.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        
        // Load the TinyMCE script
        Util.getXhtmlHelper().linkJavascript(context, component,
                context.getResponseWriter(), Mechanism.CLASS_RESOURCE, TINY_MCE);
        Util.getXhtmlHelper().linkJavascript(context, component,
                context.getResponseWriter(), Mechanism.CLASS_RESOURCE, "/sandbox/tiny_mce.js");
        
        // Create the textarea to use as the WYSIWYG editor
        writer.startElement("textarea", editor);
        writer.writeAttribute("id", id, "id");
        writer.writeAttribute("name", id, "name");
        writer.writeAttribute("rows", editor.getRows(), "rows");
        writer.writeAttribute("cols", editor.getCols(), "cols");
        if (editor.getValue() != null) {
            writer.write((String)editor.getValue());
        }
        writer.endElement("textarea");
        
        // Init TinyMCE
        writer.startElement("script", editor);
        writer.writeAttribute("type", "text/javascript", "type");
        writer.write("tinyMCE.init({mode:'exact',elements:'" + id +"'});");
        writer.endElement("script");
    }

    public void decode(FacesContext context, UIComponent component) {
        if (context == null) {
            throw new NullPointerException("Argument Error: Parameter 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("Argument Error: Parameter 'component' is null");
        }
        
        if (!(component instanceof HtmlEditor)) {
            // decode needs to be invoked only for components that are
            // instances or subclasses of UIInput.
            return;
        }
        HtmlEditor editor = (HtmlEditor) component; 
        
        String clientId = component.getClientId(context);
        assert(clientId != null);
        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
        if (requestMap.containsKey(clientId)) {
            String newValue = requestMap.get(clientId);
            editor.setSubmittedValue(newValue);
        }
    }        
}