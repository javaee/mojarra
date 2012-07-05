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

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.Attribute;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.NavigationCase;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class OutcomeTargetLinkRenderer extends OutcomeTargetRenderer {

    private static final Attribute[] ATTRIBUTES =
        AttributeManager.getAttributes(AttributeManager.Key.OUTCOMETARGETLINK);

    private static final String NO_NAV_CASE =
          OutcomeTargetLinkRenderer.class.getName() + "_NO_NAV_CASE";

    //Attributes that are to excluded from rendering for this renderer.
    private static final List<String> EXCLUDED_ATTRIBUTES = Arrays.asList("disabled");

    // --------------------------------------------------- Methods from Renderer


    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        NavigationCase navCase = null;
        boolean failedToResolveNavigationCase = false;
        boolean disabled = Util.componentIsDisabled(component);

        if (!disabled) {
            navCase = getNavigationCase(context, component);
            if (navCase == null) {
                failedToResolveNavigationCase = true;
                context.getAttributes().put(NO_NAV_CASE, true);
            }

        }

        if (disabled || navCase == null) {
            renderAsDisabled(context, component, failedToResolveNavigationCase);
        } else {
            renderAsActive(context, navCase, component);
        }

    }


    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);
        String endElement = ((Util.componentIsDisabled(component) || context.getAttributes().remove(NO_NAV_CASE) != null) 
                                ? "span"
                                : "a");
        writer.endElement(endElement);

    }


    // ------------------------------------------------------- Protected Methods


    protected void renderAsDisabled(FacesContext context,
                                    UIComponent component,
                                    boolean failedToResolveNavigationCase)
    throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        writer.startElement("span", component);
        writeIdAndNameAttributes(context, writer, component);
        renderLinkCommonAttributes(writer, component);
        renderPassThruAttributes(context, writer, component, ATTRIBUTES, EXCLUDED_ATTRIBUTES);
        writeValue(writer, component);

        // shame that we can't put this in encodeEnd, but then we have to attempt to resolve the navigation case again
        if (failedToResolveNavigationCase) {
            if (!context.isProjectStage(ProjectStage.Production)) {
                writer.write(MessageUtils.getExceptionMessageString(MessageUtils.OUTCOME_TARGET_LINK_NO_MATCH));
            }
        }

    }

    protected void renderAsActive(FacesContext context,
                                  NavigationCase navCase,
                                  UIComponent component)
    throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        writer.startElement("a", component);
        writeIdAndNameAttributes(context, writer, component);

        String hrefVal = getEncodedTargetURL(context, component, navCase);
        hrefVal += getFragment(component);
        writer.writeURIAttribute("href", hrefVal, "outcome");

        renderLinkCommonAttributes(writer, component);
        renderPassThruAttributes(context, writer, component, ATTRIBUTES, null);
        writeValue(writer, component);

    }

    protected void writeIdAndNameAttributes(FacesContext context,
                                            ResponseWriter writer,
                                            UIComponent component)
    throws IOException {

        String writtenId = writeIdAttributeIfNecessary(context, writer, component);
        if (null != writtenId) {
            writer.writeAttribute("name", writtenId, "name");
        }

    }

    protected void writeValue(ResponseWriter writer, UIComponent component)
    throws IOException {

        writer.writeText(getLabel(component), component, null);
        writer.flush();
        
    }


    protected void renderLinkCommonAttributes(ResponseWriter writer,
                                              UIComponent component)
    throws IOException {

        // this is common to both link and button target renderers
        String styleClass = (String) component.getAttributes().get("styleClass");
        if (styleClass != null && styleClass.length() > 0) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

        // target/onclick should be pass through, but right now, due to command Link,
        // they all share the same base properties file which marks them as non
        // pass-through
        String target = (String) component.getAttributes().get("target");
        if (target != null && target.length() > 0) {
            writer.writeAttribute("target", target, "target");
        }

        String onclick = (String) component.getAttributes().get("onclick");
        if (onclick != null && onclick.length() > 0) {
            writer.writeAttribute("onclick", onclick, "onclick");
        }
    }

}
