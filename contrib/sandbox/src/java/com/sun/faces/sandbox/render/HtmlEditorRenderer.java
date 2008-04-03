/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.shale.remoting.Mechanism;

import com.sun.faces.sandbox.component.HtmlEditor;
import com.sun.faces.sandbox.util.HtmlEditorResourcePhaseListener;
import com.sun.faces.sandbox.util.Util;
import com.sun.faces.sandbox.util.YuiConstants;

/**
 * @author Jason Lee
 *
 */
public class HtmlEditorRenderer extends Renderer {
    private static final String TINY_MCE = "/tinymce/tiny_mce.js";
    protected static final String scriptIds[] = { 
        YuiConstants.JS_YAHOO_DOM_EVENT
    };

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
        for (int i = 0; i < scriptIds.length; i++) {
            Util.getXhtmlHelper().linkJavascript(context, component,
                    context.getResponseWriter(), Mechanism.CLASS_RESOURCE,
                    scriptIds[i]);
        }
        
        Util.linkJavascript(writer, HtmlEditorResourcePhaseListener.generateUrl(context, TINY_MCE));
        HtmlEditorResourcePhaseListener.renderHtmlEditorJavascript(context, writer, editor);
        Util.linkJavascript(writer, HtmlEditorResourcePhaseListener.generateUrl(context, "/sandbox/tiny_mce.js"));
        
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
        writer.write("YAHOO.util.Event.onDOMReady(function() {tinyMCE.init({" + buildConfig(context, editor) + "});});");
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
            .append(",theme_advanced_toolbar_align : \"center\"")
            .append(",elements:'" + id +"'")
            .append(getThemeStyleConfigs(comp))
            .append((comp.getConfig() != null) ? "," + comp.getConfig() : "");
        
        return config.toString();
    }
    
    private String getThemeStyleConfigs(HtmlEditor comp) {
        StringBuilder config = new StringBuilder();
        // TODO:  Add this back in post-1.0 when we figure out how best to support the HTML pages in the library?
        /*
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
        } else 
        */    
        if ("simplified".equalsIgnoreCase(comp.getThemeStyle())) {
            //config.append(",theme_advanced_buttons1 : \"bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,bullist,numlist,undo,redo,link,unlink\",")
            config.append(",theme_advanced_buttons1 : \"bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright,justifyfull,bullist,numlist,undo,redo,link,unlink\",")
                .append("theme_advanced_buttons2 : \"\",")
                .append("theme_advanced_buttons3 : \"\",")
                .append("extended_valid_elements : \"a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]\"");
        } else {
//            config.append(",theme: 'advanced'");
            config.append(",theme_advanced_buttons1 : \"bold,italic,underline,strikethrough,separator,justifyleft,justifycenter,justifyright,justifyfull,separator,styleselect,formatselect,separator,bullist,numlist,separator,outdent,indent\",")
            .append("theme_advanced_buttons2 : \"undo,redo,separator,link,unlink,anchor,image,cleanup,help,code,separator,hr,removeformat,visualaid,separator,sub,sup\",")
            .append("theme_advanced_buttons3 : \"\",")
            .append("extended_valid_elements : \"a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]\"");
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