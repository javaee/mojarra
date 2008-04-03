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
package com.sun.faces.sandbox.util;

/**
 * This class is a holder for <code>public static final String</code>s for various
 * YUI-related file names and paths.
 * @author Jason Lee
 *
 */
public class YuiConstants {
    public static final String YUI_ROOT = "/yui/";
    public static final String SANDBOX_ROOT = "/sandbox/";
    
    public static final String CSS_CALENDAR = YUI_ROOT + "calendar/assets/calendar.css";
    
    public static final String CSS_MENU = YUI_ROOT + "menu/assets/menu.css";
    public static final String CSS_MENU_CORE = YUI_ROOT + "menu/assets/menu-core.css";
    
//    public static final String CSS_SANDBOX = YUI_ROOT + "sandbox.css";
    public static final String CSS_TABVIEW = YUI_ROOT + "tabview/assets/tabview.css";
    public static final String CSS_TABVIEW_DEFAULT = YUI_ROOT + "tabview/assets/skins/sam/tabview.css";
    public static final String CSS_TABVIEW_DEFAULT_SKIN = YUI_ROOT + "tabview/assets/skins/sam/tabview-skin.css";
    public static final String CSS_TABVIEW_BORDER_TABS = YUI_ROOT + "tabview/assets/border_tabs.css";
    public static final String CSS_TABVIEW_MODULE_TABS = "/sandbox/css/module_tabs.css";
    public static final String CSS_TABVIEW_ROUND_TABS = "/sandbox/css/round_tabs.css";
    
    public static final String CSS_TREEVIEW = YUI_ROOT + "treeview/assets/treeview.css";

    public static final String JS_AUTOCOMPLETE = YUI_ROOT + "autocomplete/autocomplete-min.js";
    
    public static final String JS_CALENDAR = YUI_ROOT + "calendar/calendar-min.js";
    
    public static final String JS_CONTAINER = YUI_ROOT + "container/container-min.js";
    
    public static final String JS_ELEMENT = YUI_ROOT + "element/element-beta-min.js";
    
    public static final String JS_LOGGER = YUI_ROOT + "logger/logger-min.js";
    
    public static final String JS_MENU = YUI_ROOT + "menu/menu-min.js";
    
    public static final String JS_SLIDER = YUI_ROOT + "slider/slider-min.js";
    
    public static final String JS_TABVIEW = YUI_ROOT + "tabview/tabview-min.js";
    
    public static final String JS_TREEVIEW = YUI_ROOT + "treeview/treeview-min.js";
    
    public static final String JS_UTILITIES = YUI_ROOT + "utilities/utilities.js";
    
    public static final String JS_YAHOO_DOM_EVENT = YUI_ROOT + "yahoo-dom-event/yahoo-dom-event.js";
    
    public static final String JS_SANDBOX_HELPER = SANDBOX_ROOT + "sandbox.js";
    public static final String JS_YUI_CALENDAR_HELPER = SANDBOX_ROOT + "calendar_helper.js";
    public static final String JS_YUI_MENU_HELPER = SANDBOX_ROOT + "menu_helper.js";
    public static final String JS_YUI_TREEVIEW_HELPER = SANDBOX_ROOT + "treeview_helper.js";
}