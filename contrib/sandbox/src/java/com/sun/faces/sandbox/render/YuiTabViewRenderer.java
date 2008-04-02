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

import org.apache.shale.remoting.Mechanism;

import com.sun.faces.sandbox.component.YuiTab;
import com.sun.faces.sandbox.component.YuiTabView;
import com.sun.faces.sandbox.util.Util;
import com.sun.faces.sandbox.util.YuiConstants;

/**
 * @author Jason Lee
 *
 */
public class YuiTabViewRenderer extends Renderer {
    private static final String scriptIds[] = {
        YuiConstants.JS_UTILITIES,
        YuiConstants.JS_ELEMENT,
        YuiConstants.JS_TABVIEW
    };

    private static final String cssIds[] = { 
        YuiConstants.CSS_TABVIEW
    };

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

        YuiTabView tabView = (YuiTabView) component;
        // Render the div that will hold the tree
        ResponseWriter writer = context.getResponseWriter();

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

        YuiRendererHelper.renderSandboxStylesheet(context, writer, tabView);

        if (YuiTabView.TABSTYLE_BORDER.equalsIgnoreCase(tabView.getTabStyle())) {
            Util.getXhtmlHelper().linkStylesheet(context, component,
                    context.getResponseWriter(), Mechanism.CLASS_RESOURCE,
                    YuiConstants.CSS_TABVIEW_BORDER_TABS);
        } else if (YuiTabView.TABSTYLE_MODULE.equalsIgnoreCase(tabView.getTabStyle())) {
            Util.getXhtmlHelper().linkStylesheet(context, component,
                    context.getResponseWriter(), Mechanism.CLASS_RESOURCE,
                    YuiConstants.CSS_TABVIEW_MODULE_TABS);
            writer.startElement("style", tabView);
            writer.writeAttribute("type", "text/css", "type");
            writer.write(".yui-navset .yui-nav {background-image:url(" + 
                    Util.getXhtmlHelper().mapResourceId(context, Mechanism.CLASS_RESOURCE, YuiConstants.YUI_ROOT + "assets/newcats_bkgd.gif") + ");}\n");
            writer.write(".yui-navset .yui-nav .selected a {background-image:url(" + 
                    Util.getXhtmlHelper().mapResourceId(context, Mechanism.CLASS_RESOURCE, YuiConstants.YUI_ROOT + "assets/tab_right.gif") + ");}\n");
            writer.write(".yui-navset .yui-nav .selected {background-image:url(" + 
                    Util.getXhtmlHelper().mapResourceId(context, Mechanism.CLASS_RESOURCE, YuiConstants.YUI_ROOT + "assets/ptr.gif") + ");}\n");
            writer.write(".yui-navset .yui-nav .selected em {background-image:url(" + 
                    Util.getXhtmlHelper().mapResourceId(context, Mechanism.CLASS_RESOURCE, YuiConstants.YUI_ROOT + "assets/tab_left.gif") + ");}\n");
            writer.endElement("style");
        } else  if (YuiTabView.TABSTYLE_ROUND.equalsIgnoreCase(tabView.getTabStyle())) {
            Util.getXhtmlHelper().linkStylesheet(context, component,
                    context.getResponseWriter(), Mechanism.CLASS_RESOURCE,
                    YuiConstants.CSS_TABVIEW_ROUND_TABS);
            writer.startElement("style", tabView);
            writer.writeAttribute("type", "text/css", "type");
            writer.write(".yui-navset .yui-nav li a {background-image:url(" + 
                    Util.getXhtmlHelper().mapResourceId(context, Mechanism.CLASS_RESOURCE, YuiConstants.YUI_ROOT + "assets/round_4px_trans_gray.gif") +
                ");}\n");
            writer.write(".yui-navset .yui-nav li a em {background-image:url(" + 
                    Util.getXhtmlHelper().mapResourceId(context, Mechanism.CLASS_RESOURCE, YuiConstants.YUI_ROOT + "assets/round_4px_trans_gray.gif") +
                ");}\n");
            writer.endElement("style");
        }
        
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
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div"); // content div
        writer.endElement("div"); // containing div
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");
        String jsName = "tabView_" + YuiRendererHelper.getJavascriptVar(component);
        writer.write ("var " + jsName + " = new YAHOO.widget.TabView('tabView_" + component.getClientId(context) + "');");
        //writer.writeText(text, property)
        writer.endElement("script");
    }
}