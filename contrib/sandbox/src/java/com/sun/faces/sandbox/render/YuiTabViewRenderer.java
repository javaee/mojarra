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

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.sun.faces.sandbox.component.YuiTab;
import com.sun.faces.sandbox.component.YuiTabView;
import com.sun.faces.sandbox.util.Util;
import com.sun.faces.sandbox.util.YuiConstants;

/**
 * @author Jason Lee
 *
 */
// TODO
// http://tech.groups.yahoo.com/group/ydn-javascript/message/7472
public class YuiTabViewRenderer extends Renderer {
    private static final String scriptIds[] = {
        YuiConstants.JS_UTILITIES,
        YuiConstants.JS_ELEMENT,
        YuiConstants.JS_TABVIEW
    };

    private static final String cssIds[] = { 
        YuiConstants.CSS_TABVIEW,
        YuiConstants.CSS_SANDBOX
    };

    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {
        String extraClass = "";
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

        YuiTabView tabView = (YuiTabView) component;
        // Render the div that will hold the tree
        ResponseWriter writer = context.getResponseWriter();
        for (int i = 0; i < scriptIds.length; i++) {
            Util.linkJavascript(writer, scriptIds[i], true);
        }
        
        for (int i = 0; i < cssIds.length; i++) {
            Util.linkStyleSheet(writer, cssIds[i]);
        }

//        YuiRendererHelper.renderSandboxStylesheet(context, writer, tabView);

        if (YuiTabView.TABSTYLE_BORDER.equalsIgnoreCase(tabView.getTabStyle())) {
            Util.linkStyleSheet(writer, YuiConstants.CSS_TABVIEW_BORDER_TABS);
        } else if (YuiTabView.TABSTYLE_MODULE.equalsIgnoreCase(tabView.getTabStyle())) {
            Util.linkStyleSheet(writer, YuiConstants.CSS_TABVIEW_MODULE_TABS);
            writer.startElement("style", tabView);
            writer.writeAttribute("type", "text/css", "type");
            writer.write(".yui-navset .yui-nav {background-image:url(" + 
                    Util.generateStaticUri("/sandbox/img/newcats_bkgd.gif") + ");}");
            writer.write(".yui-navset .yui-nav .selected a {background-image:url(" + 
                    Util.generateStaticUri("/sandbox/img/tab_right.gif") + ");}");
            writer.write(".yui-navset .yui-nav .selected {background-image:url(" + 
                    Util.generateStaticUri("/sandbox/img/ptr.gif") + ");}");
            writer.write(".yui-navset .yui-nav .selected em {background-image:url(" + 
                    Util.generateStaticUri("/sandbox/img/tab_left.gif") + ");}");
            writer.endElement("style");
        } else  if (YuiTabView.TABSTYLE_ROUND.equalsIgnoreCase(tabView.getTabStyle())) {
            Util.linkStyleSheet(writer, YuiConstants.CSS_TABVIEW_ROUND_TABS);
            writer.startElement("style", tabView);
            writer.writeAttribute("type", "text/css", "type");
            writer.write(".yui-navset .yui-nav li a {background-image:url(" + 
                    Util.generateStaticUri("/sandbox/img/round_4px_trans_gray.gif") +
                ");}");
            writer.write(".yui-navset .yui-nav li a em {background-image:url(" + 
                    Util.generateStaticUri("/sandbox/img/round_4px_trans_gray.gif") +
                ");}");
            writer.endElement("style");
        } else {
            Util.linkStyleSheet(writer, YuiConstants.CSS_TABVIEW_DEFAULT);
//            Util.linkStyleSheet(writer, YuiConstants.CSS_TABVIEW_DEFAULT_SKIN);
            writer.startElement("style", tabView);
            writer.writeAttribute("type", "text/css", "type");
            writer.write(".yui-skin-sam .yui-navset .yui-nav a { background:#dadbdb url(" +
                    Util.generateStaticUri(YuiConstants.YUI_ROOT + "assets/skins/sam/sprite.png") + ") repeat-x;}");
            writer.write(".yui-skin-sam .yui-navset .yui-nav .selected a, .yui-skin-sam .yui-navset .yui-nav a:focus, .yui-skin-sam .yui-navset .yui-nav a:hover { background:#214197 url(" +
                    Util.generateStaticUri(YuiConstants.YUI_ROOT + "assets/skins/sam/sprite.png") + ") repeat-x left -1400px;}");
            writer.write(".yui-skin-sam .yui-navset .yui-content div { border: 0px solid #808080;}");
            writer.write(".yui-skin-sam .yui-navset .yui-content { border-left: 1px solid #808080; border-right: 1px solid #808080; border-bottom: 1px solid #808080; background:  #FFFFFF none repeat scroll 0%; }");
            writer.endElement("style");
            extraClass="yui-skin-sam";
        }
        
        if (tabView.getMinHeight() != null) {
            writer.startElement("style", tabView);
            writer.writeAttribute("type", "text/css", "type");
            writer.write(".yui-content { min-height: " + tabView.getMinHeight() + "}");
            writer.endElement("style");
        }
        
        // Added to support the new default tab style, which requires an enclosing container with a certain class
        writer.startElement("div", tabView);
        writer.writeAttribute("id", "tabView_" + component.getClientId(context), "id");
        writer.writeAttribute("class", extraClass, "class");
                
        writer.startElement("div", tabView);
        writer.writeAttribute("id", "tabView_" + component.getClientId(context), "id");
        writer.writeAttribute("class", "yui-navset", "class");
        Util.renderPassThruAttributes(writer, component);

        writer.startElement("ul", tabView);
        writer.writeAttribute("class", "yui-nav", "class");
        // Loop through child tabs and get the tab labels
        for (UIComponent child : tabView.getChildren()) {
            if (!(child instanceof YuiTab)) {
                throw new FacesException ("Was expecting a YuiTab child, but found " + 
                        child.getClass().getName());
            }
            YuiTab tab = (YuiTab) child;
            UIComponent labelFacet = tab.getFacet("label");
            String labelText;
            // If a facet is found, it overrides any value in the label attribute
            if (labelFacet != null) {
                labelText = YuiRendererHelper.getRenderedOutput(context, writer, labelFacet);
            } else {
                labelText = tab.getLabel();
            }
            
            if (labelText == null) {
                throw new FacesException ("YuiTab requires either the 'label' attribute or a 'label' facet, but neither were found:  " +
                        tab.getClientId(context));
            }
            
            writer.startElement("li", tab);
            if (Boolean.TRUE.equals(tab.getActive())) {
                writer.writeAttribute("class", "selected", "class");
            } else if (Boolean.TRUE.equals(tab.getDisabled())) {
                writer.writeAttribute("class", "disabled", "class");
            }
            writer.startElement("a", tab);
            writer.startElement("em", tab);
            writer.writeText(labelText, null);
            writer.endElement("em");
            writer.endElement("a");
            writer.endElement("li");
            
        }
        writer.endElement("ul");
        writer.startElement("div", tabView);
        writer.writeAttribute("class", "yui-content", "class");
//        String maxHeight = tabView.getMaxHeight();
//        if ((maxHeight != null) && !"auto".equalsIgnoreCase(maxHeight)) {
//            if (!"dynamic".equalsIgnoreCase(maxHeight)) {
//                writer.writeAttribute("style", "height:  " + maxHeight + "; maxHeight:  " + maxHeight, "style");
//            }
//        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
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
        YuiTabView tabView = (YuiTabView) component;
        
        writer.endElement("div"); // content div
        writer.endElement("div"); // containing div
        writer.endElement("div"); // containing div
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");
        String jsName = "tabView_" + YuiRendererHelper.getJavascriptVar(component);
        writer.write ("var " + jsName + " = new YAHOO.widget.TabView('tabView_" + component.getClientId(context) + "', " +
                "{ orientation: '" + tabView.getOrientation() + "' });");
        //writer.writeText(text, property)
        if ("auto".equalsIgnoreCase(tabView.getMaxHeight())) {
            writer.write(AUTO_HEIGHT_JS.replaceAll("%%%TABVIEW%%%", jsName));
        }
        writer.endElement("script");
    }
    
    private final static String AUTO_HEIGHT_JS =
        "YAHOO.util.Event.onContentReady('%%%TABVIEW%%%', function() { " +
        "var tabs = %%%TABVIEW%%%.get('tabs'); " +
        "var height = %%%TABVIEW%%%.get('activeTab').get('contentEl').offsetHeight;" +  /* seed with visible tab */ 
      
        "for (var i = 0, len = tabs.length; i < len; i++) { " +
        "    if ( tabs[i] == %%%TABVIEW%%%.get('activeTab') ) { " +
        "        continue; " +  /* skip active tab */
        "    } " +
          
        "    tabs[i].set('contentVisible', true); " + /* so we can measure */ 
        "    height = Math.max(tabs[i].get('contentEl').offsetHeight, height); " +
        "    tabs[i].set('contentVisible', false); " +
        "} " +
      
        "%%%TABVIEW%%%.getElementsByClassName('yui-content')[0].style.height = height + 'px'; " +
  
        "});";
}