/*
 * $Id: CommandLinkRenderer.java,v 1.62 2007/08/30 19:29:12 rlubke Exp $
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

// CommandLinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;


/**
 * <B>CommandLinkRenderer</B> is a class that renders the current value of
 * <code>UICommand<code> as a HyperLink that acts like a Button.
 */

public class CommandLinkRenderer extends LinkRenderer {

    private static final Attribute[] ATTRIBUTES =
          AttributeManager.getAttributes(AttributeManager.Key.COMMANDLINK);


    private static final String SCRIPT_STATE = RIConstants.FACES_PREFIX +
                                               "scriptState";

    // ---------------------------------------------------------- Public Methods


    @Override
    public void decode(FacesContext context, UIComponent component) {

        rendererParamsNotNull(context, component);

        if (!shouldDecode(component)) {
            return;
        }

        if (wasClicked(context, component)) {
            component.queueEvent(new ActionEvent(component));
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("This commandLink resulted in form submission " +
                            " ActionEvent queued.");

            }
        }

    }


    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        boolean componentDisabled =
              Boolean.TRUE.equals(component.getAttributes().get("disabled"));

        String formClientId = RenderKitUtils.getFormClientId(component, context);
        if (formClientId == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING,
                           "Component {0} must be enclosed inside a form",
                           component.getId());
            }
        }

        if (componentDisabled || formClientId == null) {
            renderAsDisabled(context, component);
        } else {
            RenderKitUtils.renderJsfJs(context);
            renderAsActive(context, component);
        }

    }


    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncodeChildren(component)) {
            return;
        }

        if (component.getChildCount() > 0) {
            for (UIComponent kid : component.getChildren()) {
                encodeRecursive(context, kid);
            }
        }
        
    }


    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);
        String formClientId = RenderKitUtils.getFormClientId(component, context);
        if (formClientId == null) {
            writer.write(MessageUtils.getExceptionMessageString(
                  MessageUtils.COMMAND_LINK_NO_FORM_MESSAGE_ID));
            writer.endElement("span");
            return;
        }

        if (Boolean.TRUE.equals(component.getAttributes().get("disabled"))) {
            writer.endElement("span");
        } else {
            writer.endElement("a");
        }

    }


    @Override
    public boolean getRendersChildren() {

        return true;

    }

    // ------------------------------------------------------- Protected Methods


    @Override
    protected Object getValue(UIComponent component) {

        return ((UICommand) component).getValue();       

    }

    /*
     * Render the necessary Javascript for the link.
     * Note that much of this code is shared with CommandButtonRenderer.renderOnClick
     * RELEASE_PENDING: Consolidate this code into a utility method, if possible.
     */
    protected void renderAsActive(FacesContext context, UIComponent command)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);
        String formClientId = RenderKitUtils.getFormClientId(command, context);
        if (formClientId == null) {
            return;
        }

        //make link act as if it's a button using javascript        
        writer.startElement("a", command);
        writeIdAttributeIfNecessary(context, writer, command);
        writer.writeAttribute("href", "#", "href");
        RenderKitUtils.renderPassThruAttributes(context,
                                                writer,
                                                command,
                                                ATTRIBUTES,
                                                getNonOnClickBehaviors(command));

        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, command);

        String target = (String) command.getAttributes().get("target");
        if (target != null) {
            target = target.trim();
        } else {
            target = "";
        }

        Collection<Behavior.Parameter> params = getBehaviorParameters(command);
        RenderKitUtils.renderOnclick(context, 
                                     command,
                                     params,
                                     target,
                                     true);

        writeCommonLinkAttributes(writer, command);

        // render the current value as link text.
        writeValue(command, writer);
        writer.flush();

    }

    // --------------------------------------------------------- Private Methods

    private static boolean wasClicked(FacesContext context,
                                      UIComponent component) {

        Map<String,String> requestParamMap =
              context.getExternalContext().getRequestParameterMap();
        return (requestParamMap.containsKey(component.getClientId(context)));

    }

    // Returns the Behaviors map, but only if it contains some entry other
    // than those handled by renderOnclick().  This helps us optimize
    // renderPassThruAttributes() in the very common case where the
    // link only contains an "action" (or "click") Behavior.  In that
    // we pass a null Behaviors map into renderPassThruAttributes(),
    // which allows us to take a more optimized code path.
    private static Map<String, List<Behavior>> getNonOnClickBehaviors(UIComponent component) {

        return getPassThruBehaviors(component, "click", "action");
    }


} // end of class CommandLinkRenderer
