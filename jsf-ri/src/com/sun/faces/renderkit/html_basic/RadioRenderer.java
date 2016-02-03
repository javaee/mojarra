/*
 * $Id: RadioRenderer.java,v 1.68.8.2 2007/04/27 21:27:46 ofung Exp $
 */

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

// RadioRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import java.io.IOException;

/**
 * <B>ReadoRenderer</B> is a class that renders the current value of
 * <code>UISelectOne<code> or <code>UISelectMany<code> component as a list of
 * radio buttons
 */

public class RadioRenderer extends SelectManyCheckboxListRenderer {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public RadioRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //
    protected void renderOption(FacesContext context, UIComponent component,
                                SelectItem curItem, boolean alignVertical)
        throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null);

        UISelectOne selectOne = (UISelectOne) component;
        Object curValue = selectOne.getSubmittedValue();
        if (curValue == null) {
            curValue = selectOne.getValue();
        }

        if (alignVertical) {
            writer.writeText("\t", null);
            writer.startElement("tr", component);
            writer.writeText("\n", null);
        }
        
        // disable the radio button if the attribute is set.
        String labelClass = null;
	boolean componentDisabled = false;
	if (component.getAttributes().get("disabled") != null) {
            if ((component.getAttributes().get("disabled")).equals(Boolean.TRUE)) {
	        componentDisabled = true;
	    }
	}
        if (componentDisabled || curItem.isDisabled()) {
            labelClass = (String) component.
                getAttributes().get("disabledClass");
        } else {
            labelClass = (String) component.
                getAttributes().get("enabledClass");
        }
        writer.startElement("td", component);
        writer.writeText("\n", null);

        writer.startElement("label", component);
        // if enabledClass or disabledClass attributes are specified, apply
        // it on the label.
        if (labelClass != null) {
            writer.writeAttribute("class", labelClass, "labelClass");
        }

        writer.startElement("input", component);
        writer.writeAttribute("type", "radio", "type");
        if (null != curItem.getValue() &&
            curItem.getValue().equals(curValue)) {
            writer.writeAttribute("checked", Boolean.TRUE, null);
        }
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");
        writer.writeAttribute("value", (getFormattedValue(context, component,
                                                          curItem.getValue())), "value");

        if (curItem.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", "disabled");
        }
        // Apply HTML 4.x attributes specified on UISelectMany component to all 
        // items in the list except styleClass and style which are rendered as
        // attributes of outer most table.
        Util.renderPassThruAttributes(writer, component,
                                      new String[]{"style", "border"});

        Util.renderBooleanPassThruAttributes(writer, component);

        String itemLabel = curItem.getLabel();
        if (itemLabel != null) {
            writer.writeText(" ", null);
            writer.writeText(itemLabel, "label");
        }
        writer.endElement("input");
        writer.endElement("label");
        writer.endElement("td");
        writer.writeText("\n", null);
        if (alignVertical) {
            writer.writeText("\t", null);
            writer.endElement("tr");
            writer.writeText("\n", null);
        }
    }

} // end of class RadioRenderer
