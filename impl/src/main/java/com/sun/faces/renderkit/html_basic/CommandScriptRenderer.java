/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2016 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

// CommandScriptRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.regex.Pattern;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandScript;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;

import com.sun.faces.renderkit.RenderKitUtils;


/**
 * <b>CommandScriptRenderer</b> is a class that renders the current value of
 * <code>UICommand<code> as a Script that acts like an Ajax Button.
 */
public class CommandScriptRenderer extends HtmlBasicRenderer {

    private static final Pattern PATTERN_NAME = Pattern.compile("[$a-z_](\\.?[$\\w])*", Pattern.CASE_INSENSITIVE);

    @Override
    public void decode(FacesContext context, UIComponent component) {

        rendererParamsNotNull(context, component);

        if (!shouldDecode(component)) {
            return;
        }

        String clientId = component.getClientId(context);

        if (RenderKitUtils.isPartialOrBehaviorAction(context, clientId)) {
            UICommand command = (UICommand) component;
            ActionEvent event = new ActionEvent(command);
            event.setPhaseId(command.isImmediate() ? PhaseId.APPLY_REQUEST_VALUES : PhaseId.INVOKE_APPLICATION);
            command.queueEvent(event);

            if (logger.isLoggable(Level.FINE)) {
                logger.fine("This commandScript resulted in form submission ActionEvent queued.");
            }
        }
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        HtmlCommandScript commandScript = (HtmlCommandScript) component;
        String clientId = commandScript.getClientId(context);
        
        if (RenderKitUtils.getForm(commandScript, context) == null) {
            throw new IllegalArgumentException("commandScript ID " + clientId + " must be placed in UIForm");
        }
        
        String name = commandScript.getName();

        if (name == null || !PATTERN_NAME.matcher(name).matches()) {
            throw new IllegalArgumentException("commandScript ID " + clientId + " has an illegal name: '" + name + "'");
        }

        RenderKitUtils.renderJsfJsIfNecessary(context);

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
        
        writer.startElement("span", commandScript);
        writer.writeAttribute("id", clientId, "id");
        writer.startElement("script", commandScript);
        writer.writeAttribute("type", "text/javascript", "type");

        RenderKitUtils.renderFunction(context, component, getBehaviorParameters(commandScript), clientId);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        writer.endElement("script");
        writer.endElement("span");
    }

    @Override
    public boolean getRendersChildren() {
        return false;
    }

}