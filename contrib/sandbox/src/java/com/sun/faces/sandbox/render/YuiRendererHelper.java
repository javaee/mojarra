/**
 * 
 */
package com.sun.faces.sandbox.render;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletRequest;

import org.apache.shale.remoting.Mechanism;

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
    protected static Map<String, String> cssClasses;
    protected static Map<String, String> imageVars;
    
    protected static Map<String, String> getCssClasses() {
        if (cssClasses == null) {
            cssClasses = new HashMap<String, String>();
            cssClasses.put(".ygtvtn", YuiConstants.YUI_ROOT + "assets/tn.gif");
            cssClasses.put(".ygtvtm", YuiConstants.YUI_ROOT + "assets/tm.gif");
            cssClasses.put(".ygtvtmh", YuiConstants.YUI_ROOT + "assets/tmh.gif");
            cssClasses.put(".ygtvtp", YuiConstants.YUI_ROOT + "assets/tp.gif");
            cssClasses.put(".ygtvtph", YuiConstants.YUI_ROOT + "assets/tph.gif");
            cssClasses.put(".ygtvln", YuiConstants.YUI_ROOT + "assets/ln.gif");
            cssClasses.put(".ygtvlm", YuiConstants.YUI_ROOT + "assets/lm.gif");
            cssClasses.put(".ygtvlmh", YuiConstants.YUI_ROOT + "assets/lmh.gif");
            cssClasses.put(".ygtvlp", YuiConstants.YUI_ROOT + "assets/lp.gif");
            cssClasses.put(".ygtvlph", YuiConstants.YUI_ROOT + "assets/lph.gif");
            cssClasses.put(".ygtvloading", YuiConstants.YUI_ROOT + "assets/loading.gif");
            cssClasses.put(".ygtvdepthcell", YuiConstants.YUI_ROOT + "assets/vline.gif");
            
            cssClasses.put("div.yuimenu div.topscrollbar, div.yuimenu div.bottomscrollbar", YuiConstants.YUI_ROOT + "assets/map.gif");
            cssClasses.put("div.yuimenu div.topscrollbar", YuiConstants.YUI_ROOT + "assets/map.gif");
            cssClasses.put("div.yuimenu div.topscrollbar_disabled", YuiConstants.YUI_ROOT + "assets/map.gif");
            cssClasses.put("div.yuimenu div.bottomscrollbar", YuiConstants.YUI_ROOT + "assets/map.gif");
            cssClasses.put("div.yuimenu div.bottomscrollbar_disabled", YuiConstants.YUI_ROOT + "assets/map.gif");
            cssClasses.put("div.yuimenu li.hassubmenu em.submenuindicator, div.yuimenubar li.hassubmenu em.submenuindicator", YuiConstants.YUI_ROOT + "assets/map.gif");
            cssClasses.put("div.yuimenu li.checked em.checkedindicator", YuiConstants.YUI_ROOT + "assets/map.gif");
            
            cssClasses.put(".yui-calendar .calnavleft", YuiConstants.YUI_ROOT + "assets/callt.gif");
            cssClasses.put(".yui-calendar .calnavright", YuiConstants.YUI_ROOT + "assets/calrt.gif");
        }
        
        return cssClasses;
    }
    
    protected static Map<String, String> getImageVars() {
        if (imageVars == null) {
            imageVars = new HashMap<String, String>();
//            imageVars.put("YAHOO.widget.MenuItem.prototype.IMG_ROOT", "");
            imageVars.put("YAHOO.widget.MenuItem.prototype.SUBMENU_INDICATOR_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuarorght8_nrm_1.gif");
            imageVars.put("YAHOO.widget.MenuItem.prototype.SELECTED_SUBMENU_INDICATOR_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuarorght8_hov_1.gif");
            imageVars.put("YAHOO.widget.MenuItem.prototype.DISABLED_SUBMENU_INDICATOR_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuarorght8_dim_1.gif");
            imageVars.put("YAHOO.widget.MenuItem.prototype.CHECKED_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuchk8_nrm_1.gif");
            imageVars.put("YAHOO.widget.MenuItem.prototype.SELECTED_CHECKED_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuchk8_hov_1.gif");
            imageVars.put("YAHOO.widget.MenuItem.prototype.DISABLED_CHECKED_IMAGE_PATH",
                    YuiConstants.YUI_ROOT + "assets/menuchk8_dim_1.gif");
            imageVars.put("YAHOO.widget.MenuBarItem.prototype.SUBMENU_INDICATOR_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuarodwn8_nrm_1.gif");
            imageVars.put("YAHOO.widget.MenuBarItem.prototype.SELECTED_SUBMENU_INDICATOR_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuarodwn8_hov_1.gif");
            imageVars.put("YAHOO.widget.MenuBarItem.prototype.DISABLED_SUBMENU_INDICATOR_IMAGE_PATH", 
                    YuiConstants.YUI_ROOT + "assets/menuarodwn8_dim_1.gif");
            imageVars.put("YAHOO.widget.Calendar.prototype.IMG_ROOT", 
                    YuiConstants.YUI_ROOT + "");
            imageVars.put("YAHOO.widget.Calendar.prototype.NAV_ARROW_LEFT", 
                    YuiConstants.YUI_ROOT + "assets/callt.gif");
            imageVars.put("YAHOO.widget.Calendar.prototype.NAV_ARROW_RIGHT", 
                    YuiConstants.YUI_ROOT + "assets/calrt.gif");



        }
        return imageVars;
    }
    
    public static void renderSandboxStylesheet(FacesContext context, ResponseWriter writer, UIComponent comp) throws IOException{
        writer.startElement("style", comp);
        writer.writeAttribute("type", "text/css", "type");
        for (Map.Entry<String, String> cssClass : getCssClasses().entrySet()) {
            writer.write(cssClass.getKey() + " {background-image:url(" + 
                    Util.getXhtmlHelper().mapResourceId(context, Mechanism.CLASS_RESOURCE, cssClass.getValue()) +
                    ");}\n");
        }
        writer.endElement("style");
    }
    
    public static void renderSandboxJavaScript(FacesContext context, ResponseWriter writer, UIComponent comp) throws IOException{
        writer.startElement("script", comp);
        writer.writeAttribute("type", "text/javascript", "type");
        writer.write("YAHOO.widget.MenuItem.prototype.IMG_ROOT = \"\";");
        for (Map.Entry<String, String> var : getImageVars().entrySet()) {
            writer.write(var.getKey() + " = \"" + 
                    Util.getXhtmlHelper().mapResourceId(context, Mechanism.CLASS_RESOURCE, var.getValue()) +
                    "\";");
        }
        writer.endElement("script");
    }

}