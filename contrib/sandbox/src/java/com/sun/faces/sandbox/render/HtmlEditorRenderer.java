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
    private static final String TINY_MCE = "/tinymce/tiny_mce_src.js";

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
        writer.write("tinyMCE.init({" + buildConfig(context, editor) + "});");
        writer.endElement("script");
    }
    
    private String buildConfig(FacesContext context, HtmlEditor comp) {
        StringBuilder config = new StringBuilder();
        String id = comp.getClientId(context);
        
        config.append("mode:'exact'")
//            .append(",theme_advanced_layout_manager : 'RowLayout'")
            .append(",theme: 'advanced'")
            .append(",theme_advanced_toolbar_location : '" + comp.getToolbarLocation() + "'")
            .append(",theme_advanced_path_location : \"bottom\"")
            .append(",elements:'" + id +"'")
            .append(getThemeStyleConfigs(comp))
            .append((comp.getConfig() != null) ? "," + comp.getConfig() : "");
        
        return config.toString();
    }
    
    private String getThemeStyleConfigs(HtmlEditor comp) {
        StringBuilder config = new StringBuilder();
        if ("full".equalsIgnoreCase(comp.getThemeStyle())) {
            config.append(",plugins : \"table,save,advhr,advimage,advlink,emotions,iespell,insertdatetime,preview,zoom,flash,searchreplace,print,contextmenu\",")
                .append("theme_advanced_buttons1_add_before : \"save,separator\",")
                .append("theme_advanced_buttons1_add : \"fontselect,fontsizeselect\",")
                .append("theme_advanced_buttons2_add : \"separator,insertdate,inserttime,preview,zoom,separator,forecolor,backcolor\",")
                .append("theme_advanced_buttons2_add_before: \"cut,copy,paste,separator,search,replace,separator\",")
                .append("theme_advanced_buttons3_add_before : \"tablecontrols,separator\",")
                .append("theme_advanced_buttons3_add : \"emotions,iespell,flash,advhr,separator,print\",")
                .append("theme_advanced_toolbar_location : \"top\",")
                .append("theme_advanced_toolbar_align : \"left\",")
                .append("plugin_insertdate_dateFormat : \"%Y-%m-%d\",")
                .append("plugin_insertdate_timeFormat : \"%H:%M:%S\",")
                .append("extended_valid_elements : \"a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]\",")
                .append("external_link_list_url : \"example_data/example_link_list.js\",")
                .append("external_image_list_url : \"example_data/example_image_list.js\",")
                .append("flash_external_list_url : \"example_data/example_flash_list.js\"");
        } else if ("simplified".equalsIgnoreCase(comp.getThemeStyle())) {
            config.append(",theme_advanced_buttons1 : \"bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,bullist,numlist,undo,redo,link,unlink\",")
                .append("theme_advanced_buttons2 : \"\",")
                .append("theme_advanced_buttons3 : \"\",")
                .append("theme_advanced_toolbar_align : \"left\",")
                .append("extended_valid_elements : \"a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]\"");
        } else {
            config.append(",theme: 'advanced'");
        }
        
        return config.toString();
    }

    public void decode(FacesContext context, UIComponent component) {
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