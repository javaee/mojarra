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

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.Enumeration;
import javax.el.ValueExpression;
import javax.faces.application.Resource;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.view.ViewDeclarationLanguage;

@FacesComponent(value="systest.BootstrapComponent")
public class BootstrapCompositeComponent extends UIOutput {

    @Override
    public void encodeAll(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        ViewDeclarationLanguage vdl = context.getApplication().
                getViewHandler().getViewDeclarationLanguage(context,
                "/composite/boostrapCompositeComponentMetadata.xhtml");
        Resource compositeComponentResource = context.getApplication().getResourceHandler().createResource("componentWithMetadata.xhtml", "composite");

        long
                beforeFirstCall = System.currentTimeMillis(),
                afterFirstCall, beforeSecondCall, afterSecondCall,
                firstCallDuration, secondCallDuration;
        BeanInfo metadata = vdl.getComponentMetadata(context, compositeComponentResource);
        afterFirstCall = System.currentTimeMillis();
        firstCallDuration = afterFirstCall - beforeFirstCall;

        CompositeComponentMetadataUtils.writeMetadata(metadata, writer);

        beforeSecondCall = System.currentTimeMillis();
        metadata = vdl.getComponentMetadata(context, compositeComponentResource);
        afterSecondCall = System.currentTimeMillis();
        
        secondCallDuration = afterSecondCall - beforeSecondCall;

        CompositeComponentMetadataUtils.writeMetadata(metadata, writer);

        writer.write("firstCallDuration: " + firstCallDuration +
                " secondCallDuration: " + secondCallDuration + "\n");
        if (firstCallDuration > secondCallDuration) {
            writer.write("First call longer than second call by " +
                    (firstCallDuration - secondCallDuration));
        } else {
            writer.write("Cache did not work!");
        }


    }
}
