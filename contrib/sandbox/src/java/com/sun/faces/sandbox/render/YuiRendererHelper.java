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
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.sandbox.util.Util;
import com.sun.faces.sandbox.util.YuiConstants;

/**
 * I'm not a big fan of this class, but it will get me by until I can come up
 * with a better solution.  Ken Paulsen was telling me about some of the things
 * that JSFTemplating offers, and they sound pretty promising...
 * @author Jason Lee
 *
 */
public class YuiRendererHelper {
//    private static Map<String, String> cssClasses;
    protected static Map<String, String> imageVars1;
    protected static String YUI_HELPER_JS_RENDERED = "YUI_HELPER_JS";
    protected static String YUI_HELPER_MENU_JS_RENDERED = "YUI_HELPER_MENU_JS";
    protected static String YUI_HELPER_CSS_RENDERED = "YUI_HELPER_CSS";
    protected static String[] cssRules = new String[] {
        /*
        // Button
        ".yui-button  { background:#ecece3 url('%%%BASE_URL%%%/yui/button/assets/background.png') left center;}",
        ".yui-menu-button button {background-image:url('%%%BASE_URL%%%/yui/button/assets/menuarrow.gif'); }",
        ".yui-split-button button { background-image:url('%%%BASE_URL%%%/yui/button/assets/splitarrow.gif');}",
        ".yui-split-button-activeoption button { background-image:url('%%%BASE_URL%%%/yui/button/assets/splitarrow_active.gif'); }",
        ".yui-button.ie6 {filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='%%%BASE_URL%%%/yui/button/assets/background.png', sizingMethod = 'scale');}",
        
        ".yui-skin-sam .yui-button  { background: url('%%%BASE_URL%%%/yui/assets/skins/sam/sprite.png') repeat-x 0 0;}",
        ".yui-skin-sam .yui-menu-button button { background-image: url('%%%BASE_URL%%%/yui/button/assets/skins/sam/menu-button-arrow.png'); }",
        ".yui-skin-sam .yui-split-button button { background-image: url('%%%BASE_URL%%%/yui/button/assets/skins/sam/split-button-arrow.png'); }",
        ".yui-skin-sam .yui-split-button-focus button { background-image: url('%%%BASE_URL%%%/yui/button/assets/skins/sam/split-button-arrow-focus.png'); }",
        ".yui-skin-sam .yui-split-button-hover button { background-image: url('%%%BASE_URL%%%/yui/button/assets/skins/sam/split-button-arrow-hover.png');}",
        ".yui-skin-sam .yui-split-button-activeoption button { background-image: url('%%%BASE_URL%%%/yui/button/assets/skins/sam/split-button-arrow-active.png');}",
        ".yui-skin-sam .yui-menu-button-disabled button { background-image: url('%%%BASE_URL%%%/yui/button/assets/skins/sam/menu-button-arrow-disabled.png');}",
        ".yui-skin-sam .yui-split-button-disabled button { background-image: url('%%%BASE_URL%%%/yui/button/assets/skins/sam/split-button-arrow-disabled.png'); }",
        // Calendar
        ".yui-calendar .calnavleft { background: url('%%%BASE_URL%%%/yui/calendar/assets/callt.gif') no-repeat; }",
        ".yui-calendar .calnavright { background: url('%%%BASE_URL%%%/yui/calendar/assets/calrt.gif') no-repeat; }",
        ".yui-calcontainer .calclose { background: url('%%%BASE_URL%%%/yui/calendar/assets/calx.gif') no-repeat; }",
        // Editor
        ".yui-skin-sam .yui-toolbar-container .yui-toolbar-titlebar { background: url('%%%BASE_URL%%%/yui/assets/skins/sam/sprite.png') repeat-x 0 -200px;}",
        ".yui-skin-sam .yui-toolbar-container .collapse { background: url('%%%BASE_URL%%%/yui/assets/skins/sam/sprite.png') no-repeat 0 -400px; }",
        ".yui-skin-sam .yui-toolbar-container .yui-button { background:url('%%%BASE_URL%%%/yui/assets/skins/sam/sprite.png') repeat-x 0 0; }",
        ".yui-skin-sam .yui-toolbar-container .yui-button-hover{ background:url('%%%BASE_URL%%%/yui/assets/skins/sam/sprite.png') repeat-x 0 -1300px; }",
        ".yui-skin-sam .yui-toolbar-container .yui-button-selected { background:url('%%%BASE_URL%%%/yui/assets/skins/sam/sprite.png') no-repeat 0 -1700px; }",
        ".yui-skin-sam .yui-toolbar-container .yui-button-selected span.yui-toolbar-icon, .yui-skin-sam .yui-toolbar-container .yui-button-hover span.yui-toolbar-icon {background-image: url('%%%BASE_URL%%%/yui/editor/editor-sprite-active.gif'); }",
        ".yui-skin-sam .yui-toolbar-insertorderedlist-menu li { background-image: url('%%%BASE_URL%%%/yui/editor/editor-sprite.gif'); }",
        ".yui-skin-sam .yui-toolbar-container .yui-toolbar-spinbutton.yui-button a.up { background-image: url('%%%BASE_URL%%%/yui/editor/editor-sprite.gif'); }",
        ".yui-skin-sam .yui-toolbar-container .yui-toolbar-spinbutton.yui-button a.down { background-image: url('%%%BASE_URL%%%/yui/editor/editor-sprite.gif'); }",
        ".yui-skin-sam .yui-toolbar-container .yui-toolbar-select span.yui-toolbar-icon { background-image: url('%%%BASE_URL%%%/yui/editor/editor-sprite.gif'); }",
        ".yui-skin-sam .yui-editor-panel .hd h3 { background: url('%%%BASE_URL%%%/yui/assets/skins/sam/sprite.png) repeat-x 0 -200px; }",
        ".yui-skin-sam .yui-editor-panel .hd span.close { background:url('%%%BASE_URL%%%/yui/assets/skins/sam/sprite.png) no-repeat 0 -300px; }",
        ".yui-skin-sam .yui-editor-panel .ft span.tip span.icon { background-image: url('%%%BASE_URL%%%/yui/editor/editor-sprite.gif'); }",
        ".yui-skin-sam .yui-editor-panel .hd span.knob { background-image: url('%%%BASE_URL%%%/yui/editor/editor-knob.gif');}",

        // Menu
        ".yui-skin-sam .yuimenubar { background: url('%%%BASE_URL%%%/yui/assets/skins/sam/sprite.png') repeat-x 0 0; }",
        ".yui-skin-sam .yuimenubaritem a.selected { background: url('%%%BASE_URL%%%/yui/assets/skins/sam/sprite.png') repeat-x 0 -1700px;}",
        ".yui-skin-sam .yuimenubarnav a.selected .submenuindicator { background: url('%%%BASE_URL%%%/yui/menu/assets/map.gif') repeat-x -16px -856px;}",
        ".yui-skin-sam .yuimenu .topscrollbar, .yui-skin-sam .yuimenu .bottomscrollbar { background: #fff url('%%%BASE_URL%%%/yui/assets/skins/sam/sprite.png') no-repeat 0 0;}",
        ".yui-skin-sam .yuimenuitemlabel .submenuindicator,.yui-skin-sam .yuimenuitemlabel .checkedindicator, .yui-skin-sam .yuimenubaritemlabel .submenuindicator { background: url('%%%BASE_URL%%%/yui/menu/assets/map.gif') no-repeat;}",
        ".yui-skin-sam .yuimenubar .yuimenuitem a.selected .submenuindicator {background: url('%%%BASE_URL%%%/yui/menu/assets/map.gif') no-repeat 0 -906px; }",
        ".yuimenu .topscrollbar, .yuimenu .bottomscrollbar { background-image:url('%%%BASE_URL%%%/yui/menu/assets/map.gif'); }",
        ".yuimenu .topscrollbar { background-image:url('%%%BASE_URL%%%/yui/menu/assets/map.gif'); }",
        ".yuimenu .topscrollbar_disabled { background-image:url('%%%BASE_URL%%%/yui/menu/assets/map.gif'); }",
        ".yuimenu .bottomscrollbar { background-image:url('%%%BASE_URL%%%/yui/menu/assets/map.gif'); }",
        ".yuimenu .bottomscrollbar_disabled { background-image:url('%%%BASE_URL%%%/yui/menu/assets/map.gif'); }",
        ".yuimenuitemlabel .submenuindicator, .yuimenuitemlabel .checkedindicator,  .yuimenubaritemlabel .submenuindicator { background-image:url('%%%BASE_URL%%%/yui/menu/assets/map.gif'); }",

        ".yuimenu { background-color: #efefef; border:solid 1px #527B97; }",
        ".yuimenubar { background-color: #efefef; }",
        ".yuimenuitem a.selected, .yuimenubaritem a.selected { background-color:#527B97; }",


        /*
        "div.yuimenu { background-color: #efefef; border:solid 1px #527B97; }",
        "div.yuimenubar { background-color: #efefef; }",
        "div.yuimenu ul { border-color: #527B97; }",
        "div.yuimenu li.yuimenuitem { padding:2px 24px; }",
        "div.yuimenubar h6 { border-color:#527B97; }",
        "div.yuimenubar li.selected { background-color:#527B97; }",
        "div.yuimenubar li.yuimenubaritem { border-color:#527B97; }",
        "div.yuimenu li.selected, div.yuimenubar li.selected { background-color: #527B97; }",
        * /

        // TreeView
        ".ygtvtn { background: url('%%%BASE_URL%%%/yui/treeview/assets/sprite-orig.gif') 0 -5600px no-repeat; }",
        ".ygtvtm { background: url('%%%BASE_URL%%%/yui/treeview/assets/sprite-orig.gif') 0 -4000px no-repeat; }",
        ".ygtvtmh { background: url('%%%BASE_URL%%%/yui/treeview/assets/sprite-orig.gif') 0 -4800px no-repeat; }",
        ".ygtvtp { background: url('%%%BASE_URL%%%/yui/treeview/assets/sprite-orig.gif') 0 -6400px no-repeat; }",
        ".ygtvtph { background: url('%%%BASE_URL%%%/yui/treeview/assets/sprite-orig.gif') 0 -7200px no-repeat; }",
        ".ygtvln { background: url('%%%BASE_URL%%%/yui/treeview/assets/sprite-orig.gif') 0 -1600px no-repeat; }",
        ".ygtvlm { background: url('%%%BASE_URL%%%/yui/treeview/assets/sprite-orig.gif') 0 0px no-repeat; }",
        ".ygtvlmh { background: url('%%%BASE_URL%%%/yui/treeview/assets/sprite-orig.gif') 0 -800px no-repeat; }",
        ".ygtvlp { background: url('%%%BASE_URL%%%/yui/treeview/assets/sprite-orig.gif') 0 -2400px no-repeat; }",
        ".ygtvlph { background: url('%%%BASE_URL%%%/yui/treeview/assets/sprite-orig.gif') 0 -3200px no-repeat; }",
        ".ygtvloading { background: url('%%%BASE_URL%%%/yui/treeview/assets/treeview-loading.gif') 0 0 no-repeat; }",
        ".ygtvdepthcell { background: url('%%%BASE_URL%%%/yui/treeview/assets/sprite-orig.gif') 0 -8000px no-repeat; }"
        */
    };


    // TODO:  This needs to be improved
    /*
    private static void renderSandboxStylesheet(FacesContext context, ResponseWriter writer, UIComponent comp) throws IOException{
        if (!Util.hasResourceBeenRendered(context, YUI_HELPER_CSS_RENDERED)) {
            writer.startElement("style", comp);
            writer.writeAttribute("type", "text/css", "type");
            for (String rule : cssRules) {
                writer.write(rule.replaceAll("%%%BASE_URL%%%", Util.generateStaticUri(""))+"\n");
            }
            
            writer.endElement("style");
            Util.setResourceAsRendered(context, YUI_HELPER_CSS_RENDERED);
        }
    }
    */
    
    public static void renderSandboxMenuJavaScript(FacesContext context, ResponseWriter writer, UIComponent comp) throws IOException{
        Map<String, String> imageVars = new HashMap<String, String>();
        imageVars.put("YAHOO.widget.MenuItem.prototype.SUBMENU_INDICATOR_IMAGE_PATH", 
                YuiConstants.SANDBOX_ROOT + "img/menuarorght8_nrm_1.gif");
        imageVars.put("YAHOO.widget.MenuItem.prototype.SELECTED_SUBMENU_INDICATOR_IMAGE_PATH", 
                YuiConstants.SANDBOX_ROOT + "img/menuarorght8_hov_1.gif");
        imageVars.put("YAHOO.widget.MenuItem.prototype.DISABLED_SUBMENU_INDICATOR_IMAGE_PATH", 
                YuiConstants.SANDBOX_ROOT + "img/menuarorght8_dim_1.gif");
        imageVars.put("YAHOO.widget.MenuItem.prototype.CHECKED_IMAGE_PATH", 
                YuiConstants.SANDBOX_ROOT + "img/menuchk8_nrm_1.gif");
        imageVars.put("YAHOO.widget.MenuItem.prototype.SELECTED_CHECKED_IMAGE_PATH", 
                YuiConstants.SANDBOX_ROOT + "img/menuchk8_hov_1.gif");
        imageVars.put("YAHOO.widget.MenuItem.prototype.DISABLED_CHECKED_IMAGE_PATH",
                YuiConstants.SANDBOX_ROOT + "img/menuchk8_dim_1.gif");
        imageVars.put("YAHOO.widget.MenuBarItem.prototype.SUBMENU_INDICATOR_IMAGE_PATH", 
                YuiConstants.SANDBOX_ROOT + "img/menuarodwn8_nrm_1.gif");
        imageVars.put("YAHOO.widget.MenuBarItem.prototype.SELECTED_SUBMENU_INDICATOR_IMAGE_PATH", 
                YuiConstants.SANDBOX_ROOT + "img/menuarodwn8_hov_1.gif");
        imageVars.put("YAHOO.widget.MenuBarItem.prototype.DISABLED_SUBMENU_INDICATOR_IMAGE_PATH", 
                YuiConstants.SANDBOX_ROOT + "img/menuarodwn8_dim_1.gif");
        renderSandboxJavaScript(context, writer, comp, YUI_HELPER_MENU_JS_RENDERED, imageVars);

    }

    private static void renderSandboxJavaScript(FacesContext context, ResponseWriter writer, 
            UIComponent comp, String key, Map<String, String> imageVars) throws IOException{
        if (!Util.hasResourceBeenRendered(context, key)) {
            writer.startElement("script", comp);
            writer.writeAttribute("type", "text/javascript", "type");
//            writer.write("YAHOO.widget.MenuItem.prototype.IMG_ROOT = \"\";");
            for (Map.Entry<String, String> var : imageVars.entrySet()) {
                writer.write(var.getKey() + " = \"" + 
                        Util.generateStaticUri(var.getValue()) + "\";");
            }
            writer.endElement("script");
            Util.setResourceAsRendered(context, key);
        }
    }
    
    /**
     * Return a JavaScript-friendly variable name based on the clientId
     */
    public static String getJavascriptVar(UIComponent comp) {
        return comp.getClientId(FacesContext.getCurrentInstance()).replaceAll(":", "_");
    }
    
    public static String getRenderedOutput(FacesContext context, ResponseWriter writer, UIComponent component) throws IOException {
        String output = "";
        if (component != null) {
            StringWriter stringWriter = new StringWriter();
            ResponseWriter newWriter = writer.cloneWithWriter(stringWriter);
            context.setResponseWriter(newWriter);
            component.encodeBegin(context);
            component.encodeChildren(context);
            component.encodeEnd(context);
            output = stringWriter.toString();
            if (output != null) {
                output = output.trim();
                output = sanitizeStringForJavaScript(output);
            }
            context.setResponseWriter(writer);
        }

        return output;
    }

    private static String sanitizeStringForJavaScript( String s )
    {
        /*
        StringBuffer buf = new StringBuffer();
        for ( int i = 0; i < s.length(); i++ )
        {
            char c = s.charAt( i );
            if ( c>='a' && c<='z' || c>='A' && c<='Z' || c>='0' && c<='9' )
            {
                buf.append( c );
            }
            else
            {
                buf.append( "&#" + (int)c + ";" );
            }
        }
        return buf.toString();
        */
        return s.replaceAll("\\n", "")
            .replaceAll("'", "\\\\'");
    }
}