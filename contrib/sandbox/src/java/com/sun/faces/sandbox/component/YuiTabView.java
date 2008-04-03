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
package com.sun.faces.sandbox.component;

import javax.faces.component.UIOutput;

/**
 * @author Jason Lee
 *
 */
public class YuiTabView extends UIOutput {
    public static String COMPONENT_TYPE = "com.sun.faces.sandbox.YuiTabView";
    public static String RENDERER_TYPE  = "com.sun.faces.sandbox.YuiTabViewRenderer";
    
    public static String TABSTYLE_BORDER = "border";
    public static String TABSTYLE_ROUND = "round";
    public static String TABSTYLE_MODULE = "module";
    
    private String minHeight = null;
    private String maxHeight = "auto"; // "dynamic", "auto", or "##px"
    protected String tabStyle = TABSTYLE_ROUND; // Currently supports "border", "round" or "module"
    protected String orientation = "top"; // top, right, bottom, or left
    
    public YuiTabView()          { setRendererType(RENDERER_TYPE); }
    public String getFamily()    { return COMPONENT_TYPE; }
    
    public String getMaxHeight()   { return ComponentHelper.getValue(this, "maxHeight", maxHeight); }
    public String getMinHeight()   { return ComponentHelper.getValue(this, "minheight", minHeight); }
    public String getOrientation() { return ComponentHelper.getValue(this, "orientation", orientation); }
    public String getTabStyle()    { return ComponentHelper.getValue(this, "tabStyle", tabStyle); }
    
    public void setMaxHeight(String maxHeight)     { this.maxHeight = maxHeight; }
    public void setMinHeight(String minHeight)     { this.minHeight = minHeight; }
    public void setOrientation(String orientation) { this.orientation = orientation; }
    public void setTabStyle(String tabStyle)       { this.tabStyle = tabStyle; }
}
