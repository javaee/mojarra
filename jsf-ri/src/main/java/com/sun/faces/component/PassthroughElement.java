/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
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
package com.sun.faces.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.el.MethodExpression;
import javax.el.ValueExpression;


/**
 * <p>Causes all child components of this component
 * to be rendered.  This is useful in scenarios
 * where a parent component is expecting a single
 * component to be present, but the application
 * wishes to render more than one.</p>
 * <p>By default, the <code>rendererType</code> property must be set to "<code>javax.faces.passthrough.Element</code>".
 * This value can be changed by calling the <code>setRendererType()</code> method.</p>
 */
public class PassthroughElement extends javax.faces.component.UIPanel 
    implements ClientBehaviorHolder {

    private static final String OPTIMIZED_PACKAGE = "javax.faces.component.";

    public PassthroughElement() {
        super();
        setRendererType("javax.faces.passthrough.Element");
    }


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Panel";


    protected enum PropertyKeys {
;
        String toString;
        PropertyKeys(String toString) { this.toString = toString; }
        PropertyKeys() { }
        public String toString() {
            return ((toString != null) ? toString : super.toString());
        }
    }

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(
        Arrays.asList("click","dblclick","keydown","keypress","keyup","mousedown","mousemove",
        "mouseout","mouseover","mouseup"));

    public Collection<String> getEventNames() {
        return EVENT_NAMES;    
    }


    public String getDefaultEventName() {
        return "click";    
    }


    private void handleAttribute(String name, Object value) {
        List<String> setAttributes = (List<String>) this.getAttributes().get(
            "javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(OPTIMIZED_PACKAGE)) {
                setAttributes = new ArrayList<String>(6);
                this.getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }
        if (setAttributes != null) {
            if (value == null) {
                ValueExpression ve = getValueExpression(name);
                if (ve == null) {
                    setAttributes.remove(name);
                }
            } else if (!setAttributes.contains(name)) {
                setAttributes.add(name);
            }
        }
    }

}
