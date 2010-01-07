/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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
package com.sun.faces.composite;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

@FacesComponent(value="JavaBackedComponentDefaultAttrValues")
public class JavaBackedComponentDefaultAttrValues extends UINamingContainer {

    @Override
    public String getFamily() {
        return "javax.faces.NamingContainer";
    }

    @Override
    public void encodeAll(FacesContext context) throws IOException {
        super.encodeAll(context);
        ResponseWriter responseWriter = context.getResponseWriter();
        Map<String, Object> attrs = this.getAttributes();
        String clientId = this.getClientId(context);
        responseWriter.startElement("p", this);
        responseWriter.writeAttribute("id", clientId, "clientId");
        responseWriter.writeText("attr1 value is " + attrs.get("attr1").toString() + ". ", "attr1");
        responseWriter.writeText("attr2 value is " + attrs.get("attr2").toString() + ". ", "attr2");
        responseWriter.writeText("attr3 value is " + attrs.get("attr3").toString() + ". ", "attr3");
        responseWriter.writeText("action value is " + attrs.get("action").toString() + ". ", "action");
        responseWriter.writeText("actionListener value is " + attrs.get("action").toString() + ". ", "action");
        responseWriter.writeText("validator value is " + attrs.get("validator").toString() + ". ", "validator");
        responseWriter.writeText("valueChangeListener value is " + attrs.get("valueChangeListener").toString() + ". ", "valueChangeListener");
        responseWriter.endElement("p");
    }



}
