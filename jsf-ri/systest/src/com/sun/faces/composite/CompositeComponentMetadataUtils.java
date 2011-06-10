/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.composite;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.view.AttachedObjectTarget;

public class CompositeComponentMetadataUtils {

    /**
     * <p>Use the composite component metadata specification
     * in section JSF.3.6.2.1 to print out the metadata to
     * the argument writer.</p>
     * @throws IOException
     */

    public static void writeMetadata(BeanInfo metadata, 
            ResponseWriter writer) throws IOException{

        // Print out the top level BeanDescriptor stuff.
        BeanDescriptor descriptor = metadata.getBeanDescriptor();
        writeFeatureDescriptor("composite-component-BeanDescriptor", descriptor,
                writer);
        writeFeatureDescriptorValues(
                "composite-component-BeanDescriptor", descriptor,
                writer);
        PropertyDescriptor attributes[] = metadata.getPropertyDescriptors();
        for (PropertyDescriptor cur : attributes) {
            writeFeatureDescriptor("composite-component-attribute", cur,
                    writer);
            writeFeatureDescriptorValues("composite-component-attribute", cur,
                    writer);
        }
    }

    public static void writeFeatureDescriptor(String prefix,
            FeatureDescriptor fd, ResponseWriter writer) throws IOException {

        writer.write(prefix + "-name:" +
                fd.getName() + "\n");
        writer.write(prefix + "-displayName:" +
                fd.getDisplayName() + "\n");
        writer.write(prefix + "-shortDescription:" +
                fd.getShortDescription() + "\n");
        writer.write(prefix + "-expert:" +
                fd.isExpert() + "\n");
        writer.write(prefix + "-hidden:" +
                fd.isHidden() + "\n");
        writer.write(prefix + "-preferred:" +
                fd.isPreferred() + "\n");

    }

    public static void writeFeatureDescriptorValues(String prefix,
            FeatureDescriptor fd, ResponseWriter writer) throws IOException {

        Enumeration<String> extraValues = fd.attributeNames();
        String curName;
        while (extraValues.hasMoreElements()) {
            curName = extraValues.nextElement();
            if (curName.equals(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY)) {
                List<AttachedObjectTarget> attachedObjects =
                        (List<AttachedObjectTarget>) fd.getValue(curName);
                for (AttachedObjectTarget curTarget : attachedObjects) {
                    writer.write(prefix + "-attached-object-" + curTarget.getName() + "\n");
                }
            } else if (curName.equals(UIComponent.FACETS_KEY)) {
                Map<String, PropertyDescriptor> facets =
                        (Map<String, PropertyDescriptor>) fd.getValue(curName);
                for (String cur : facets.keySet()) {
                    String facetPrefix = prefix + "-facet-" + cur;
                    writeFeatureDescriptor(facetPrefix, facets.get(cur),
                            writer);
                    writeFeatureDescriptorValues(facetPrefix,
                            facets.get(cur), writer);
                }
            } else {
                ValueExpression ve = (ValueExpression) fd.getValue(curName);
                writer.write(prefix + "-extra-attribute-" + curName + ": " +
                        ve.getValue(FacesContext.getCurrentInstance().getELContext())
                        + "\n");
            }
        }
    }


}
