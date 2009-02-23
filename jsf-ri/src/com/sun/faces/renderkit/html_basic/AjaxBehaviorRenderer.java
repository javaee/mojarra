/*
 * $Id:
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

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.FacesLogger;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.Behavior;
import javax.faces.component.behavior.BehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.render.BehaviorRenderer;

import com.sun.faces.renderkit.RenderKitUtils;

/*
 *<b>AjaxBehaviorRenderer</b> renders Ajax behavior for a component.
 * It also  
 */

public class AjaxBehaviorRenderer extends BehaviorRenderer  {
    
    // Log instance for this class
    protected static final Logger logger = FacesLogger.RENDERKIT.getLogger();

    
    // ------------------------------------------------------ Rendering Methods

    @Override
    public String getScript(BehaviorContext behaviorContext,
                            Behavior behavior) {
        if (!(behavior instanceof AjaxBehavior)) {
            // TODO: use MessageUtils for this error message?
            throw new IllegalArgumentException(
                "Instance of javax.faces.component.behavior.AjaxBehavior required: " + behavior);
        }

        return buildAjaxCommand(behaviorContext, (AjaxBehavior)behavior);
    }


    @Override
    public void decode(FacesContext context,
                       UIComponent component,
                       Behavior behavior) {
        if (null == context || null == component || null == behavior) {
            throw new NullPointerException();
        }

        component.queueEvent(new AjaxBehaviorEvent(component, behavior));

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("This command resulted in form submission " +
                " AjaxBehaviorEvent queued.");
            logger.log(Level.FINE,
                "End decoding component {0}", component.getId());
        }


    }

    private static String buildAjaxCommand(BehaviorContext behaviorContext,
                                           AjaxBehavior ajaxBehavior) {

        FacesContext context = behaviorContext.getFacesContext();
        UIComponent component = behaviorContext.getComponent();
        String eventName = behaviorContext.getEventName();

        StringBuilder ajaxCommand = new StringBuilder(256);
        Collection<String> execute = ajaxBehavior.getExecute(context);
        Collection<String> render = ajaxBehavior.getRender(context);
        String onevent = ajaxBehavior.getOnEvent(context);
        String onerror = ajaxBehavior.getOnError(context);
        String sourceId = behaviorContext.getSourceId();
        Collection<Behavior.Parameter> params = behaviorContext.getParameters();

        ajaxCommand.append("mojarra.ab(");

        if (sourceId == null) {
            ajaxCommand.append("this");
        } else {
            ajaxCommand.append("'");
            ajaxCommand.append(sourceId);
            ajaxCommand.append("'");
        }

        ajaxCommand.append(",event,'");
        ajaxCommand.append(eventName);
        ajaxCommand.append("',");

        appendIds(component, ajaxCommand, execute);
        ajaxCommand.append(",");
        appendIds(component, ajaxCommand, render);

        if ((onevent != null) || (onerror != null) || !params.isEmpty())  {

            ajaxCommand.append(",{");

            // TODO: Support onerror/onevent

            if (!params.isEmpty()) {
                for (Behavior.Parameter param : params) {
                    appendOption(ajaxCommand, param.getName(), param.getValue());
                }
            }
             
            ajaxCommand.append("}");
        }

        ajaxCommand.append(")");

        return ajaxCommand.toString();
    }

    // Appends an option to the ajax command.  Assume that the 
    // options object has already been opened.
    private static void appendOption(StringBuilder builder, 
                                     String name,
                                     Object value) {


        if (null == name)
            throw new IllegalArgumentException();

        // We do null value checking in here so that callers don't have to.
        if (value == null)
            return;

        char lastChar = builder.charAt(builder.length() - 1);
        if ((lastChar != ',') && (lastChar != '{'))
            builder.append(',');

        RenderKitUtils.appendQuotedValue(builder, name);
        builder.append(":");
        RenderKitUtils.appendQuotedValue(builder, value.toString());
    }

    // Appends an ids argument to the ajax command
    private static void appendIds(UIComponent component,
                                  StringBuilder builder,
                                  Collection<String> ids) {

        if ((null == ids) || ids.isEmpty()) {
            builder.append('0');
            return;
        }

        builder.append("'");

        boolean first = true;

        for (String id : ids) {
            if (!first) {
                builder.append(' ');
            } else {
                first = false;
            }

            builder.append(getResolvedId(component, id));
        }

        builder.append("'");
    }

    // Returns the resolved (client id) for a particular id.
    private static String getResolvedId(UIComponent component, String id) {

        UIComponent resolvedComponent = findComponent(component, id);
        if (resolvedComponent == null) {
            // RELEASE_PENDING  i18n
            throw new FacesException(
                "<f:ajax> contains an unknown id '"
                + id
                + "'");
        }

        return resolvedComponent.getClientId();
    }
    /**
     * Attempt to find the component assuming the ID is relative to the
     * nearest naming container.  If not found, then search for the component
     * using an absolute component expression.
     */
    private static UIComponent findComponent(UIComponent component,
                                             String exe) {

        // RELEASE_PENDING - perhaps only enable ID validation if ProjectStage
        // is development
        UIComponent resolvedComponent = component.findComponent(exe);
        if (resolvedComponent == null) {
            // not found using a relative search, try an absolute search
            resolvedComponent = component.findComponent(':' + exe);
        }
        return resolvedComponent;

    }
}
