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
package com.sun.faces.sandbox.taglib;

import javax.faces.component.UIComponent;

import com.sun.faces.sandbox.component.YuiMenuItem;

/**
 * @author Jason Lee
 *
 */
public class YuiMenuItemTag extends UISandboxComponentTag {
    /**
     * <code>value</code> is an expression which evaulates to the Menu items.
     * @see com.sun.faces.sandbox.model.Menu
     */
    protected String value;
    /**
     * Either a string literal or expression which represents the width of the rendered
     * menu.
     */
    protected String url;
    
    @Override public String getComponentType(){ return YuiMenuItem.COMPONENT_TYPE; }
    @Override public String getRendererType() { return null; }  // Will be rendered buy the YuiMenu component

    public void setValue(String value) { this.value = value; }
    public void setUrl(String width) { this.url = width; }
    
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        YuiMenuItem menuItem = null;
        try {
            menuItem = (YuiMenuItem) component;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + 
                    " not expected type.  Expected: com.sun.faces.sandbox.component.YuiMenuItem.  Perhaps you're missing a tag?");
        }

        setStringProperty(menuItem, "value", value);
        setStringProperty(menuItem, "url", url);
    }
}
