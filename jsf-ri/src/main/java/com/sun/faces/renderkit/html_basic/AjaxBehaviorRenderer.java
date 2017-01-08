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

package com.sun.faces.renderkit.html_basic;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.html.HtmlCommandScript;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.ClientBehaviorRenderer;

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.FacesLogger;
import java.util.EnumSet;
import java.util.Set;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchExpressionHandler;
import javax.faces.component.search.SearchExpressionHint;

/*
 *<b>AjaxBehaviorRenderer</b> renders Ajax behavior for a component.
 * It also  
 */

public class AjaxBehaviorRenderer extends ClientBehaviorRenderer  {
    
    // Log instance for this class
    protected static final Logger logger = FacesLogger.RENDERKIT.getLogger();


    // ------------------------------------------------------ Rendering Methods

    @Override
    public String getScript(ClientBehaviorContext behaviorContext,
                            ClientBehavior behavior) {
        if (!(behavior instanceof AjaxBehavior)) {
            // TODO: use MessageUtils for this error message?
            throw new IllegalArgumentException(
                "Instance of javax.faces.component.behavior.AjaxBehavior required: " + behavior);
        }

        if (((AjaxBehavior)behavior).isDisabled()) {
            return null;
        }
        return buildAjaxCommand(behaviorContext, (AjaxBehavior)behavior);
    }


    @Override
    public void decode(FacesContext context,
                       UIComponent component,
                       ClientBehavior behavior) {
        if (null == context || null == component || null == behavior) {
            throw new NullPointerException();
        }

        if (!(behavior instanceof AjaxBehavior)) {
            // TODO: use MessageUtils for this error message?
            throw new IllegalArgumentException(
                "Instance of javax.faces.component.behavior.AjaxBehavior required: " + behavior);
        }

        AjaxBehavior ajaxBehavior = (AjaxBehavior)behavior;

        // First things first - if AjaxBehavior is disabled, we are done.
        if (ajaxBehavior.isDisabled()) {
            return;
        }        

        component.queueEvent(createEvent(context, component, ajaxBehavior));

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("This command resulted in form submission " +
                " AjaxBehaviorEvent queued.");
            logger.log(Level.FINE,
                "End decoding component {0}", component.getId());
        }


    }

    // Creates an AjaxBehaviorEvent for the specified component/behavior
    private static AjaxBehaviorEvent createEvent(
            FacesContext facesContext, UIComponent component, AjaxBehavior ajaxBehavior) {

        AjaxBehaviorEvent event = new AjaxBehaviorEvent(facesContext, component, ajaxBehavior);

        PhaseId phaseId = isImmediate(component, ajaxBehavior) ?
                              PhaseId.APPLY_REQUEST_VALUES :
                              PhaseId.INVOKE_APPLICATION;

        event.setPhaseId(phaseId);

        return event;
    }


    // Tests whether we should perform immediate processing.  Note
    // that we "inherit" immediate from the parent if not specified
    // on the behavior.
    private static boolean isImmediate(UIComponent component,
                                       AjaxBehavior ajaxBehavior) {

        boolean immediate = false;

        if (ajaxBehavior.isImmediateSet()) {
            immediate = ajaxBehavior.isImmediate();
        } else if (component instanceof EditableValueHolder) {
            immediate = ((EditableValueHolder)component).isImmediate();
        } else if (component instanceof ActionSource) {
            immediate = ((ActionSource)component).isImmediate();
        }

        return immediate;
    }

    private static String buildAjaxCommand(ClientBehaviorContext behaviorContext,
                                           AjaxBehavior ajaxBehavior) {

        // First things first - if AjaxBehavior is disabled, we are done.
        if (ajaxBehavior.isDisabled()) {
            return null;
        }        

        UIComponent component = behaviorContext.getComponent();
        String eventName = behaviorContext.getEventName();

        StringBuilder ajaxCommand = new StringBuilder(256);
        Collection<String> execute = ajaxBehavior.getExecute();
        Collection<String> render = ajaxBehavior.getRender();
        String onevent = ajaxBehavior.getOnevent();
        String onerror = ajaxBehavior.getOnerror();
        String sourceId = behaviorContext.getSourceId();
        String delay = ajaxBehavior.getDelay();
        Boolean resetValues = null;
        if (ajaxBehavior.isResetValuesSet()) {
            resetValues = ajaxBehavior.isResetValues();
        }
        Collection<ClientBehaviorContext.Parameter> params = behaviorContext.getParameters();

        // Needed workaround for SelectManyCheckbox - if execute doesn't have sourceId,
        // we need to add it - otherwise, we use the default, which is sourceId:child, which
        // won't work.
        ClientBehaviorContext.Parameter foundparam = null;
        for (ClientBehaviorContext.Parameter param : params) {
            if (param.getName().equals("incExec") && (Boolean)param.getValue()) {
                foundparam = param;
            }
        }
        if (foundparam != null && !execute.contains(sourceId)) {
                execute = new LinkedList<>(execute);
                execute.add(component.getClientId());
        }
        if (foundparam != null) {
            try {
                // And since this is a hack, we now try to remove the param
                params.remove(foundparam);
            } catch (UnsupportedOperationException uoe) {
                if (logger.isLoggable(Level.FINEST)) {
                    logger.log(Level.FINEST, "Unsupported operation", uoe);
                }
            }
        }

        HtmlCommandScript commandScript = (component instanceof HtmlCommandScript) ? (HtmlCommandScript) component : null;
        
        if (commandScript != null) {
            String name = commandScript.getName();

            if (!name.contains(".")) {
                ajaxCommand.append("var ");
            }

            ajaxCommand.append(name).append('=').append("function(o){var o=(typeof o==='object')&&o?o:{};");
            
            for (ClientBehaviorContext.Parameter param : params) {
                ajaxCommand.append("o[");
                RenderKitUtils.appendQuotedValue(ajaxCommand, param.getName());
                ajaxCommand.append("]=");
                Object paramValue = param.getValue();

                if (paramValue == null) {
                    ajaxCommand.append("null");
                }
                else {
                    RenderKitUtils.appendQuotedValue(ajaxCommand, paramValue.toString());
                }

                ajaxCommand.append(";");
            }
            
            params = Collections.singleton(new ClientBehaviorContext.Parameter("o", null));
        }

        ajaxCommand.append("mojarra.ab(");

        if (sourceId == null) {
            ajaxCommand.append("this");
        } else {
            ajaxCommand.append("'");
            ajaxCommand.append(sourceId);
            ajaxCommand.append("'");
        }

        ajaxCommand.append(",");
        ajaxCommand.append(commandScript == null ? "event" : "null");
        ajaxCommand.append(",'");
        ajaxCommand.append(eventName);
        ajaxCommand.append("',");

        appendIds(behaviorContext.getFacesContext(), component, ajaxCommand, execute);
        ajaxCommand.append(",");
        appendIds(behaviorContext.getFacesContext(), component, ajaxCommand, render);
        
        if ((onevent != null) || (onerror != null) || (delay != null) ||
                (resetValues != null) || !params.isEmpty())  {

            ajaxCommand.append(",{");

            if (onevent != null) {
                RenderKitUtils.appendProperty(ajaxCommand, "onevent", onevent, false);
            }

            if (onerror != null) {
                RenderKitUtils.appendProperty(ajaxCommand, "onerror", onerror, false);
            }
            
            if (delay != null) {
                RenderKitUtils.appendProperty(ajaxCommand, "delay", delay, true);
            }
            
            if (resetValues != null) {
                RenderKitUtils.appendProperty(ajaxCommand, "resetValues", resetValues, false);
            }

            if (!params.isEmpty()) {
                if (commandScript != null) {
                    RenderKitUtils.appendProperty(ajaxCommand, "params", params.iterator().next().getName(), false);
                }
                else {
                    RenderKitUtils.appendProperty(ajaxCommand, "params", "{", false);
                    
                    for (ClientBehaviorContext.Parameter param : params) {
                        RenderKitUtils.appendProperty(ajaxCommand, 
                                param.getName(),
                                param.getValue());
                    }
                    
                    ajaxCommand.append("}");
                }
                
            }
             
            ajaxCommand.append("}");
        }

        ajaxCommand.append(")");

        if (commandScript != null) {
            ajaxCommand.append("}");

            if (commandScript.isAutorun()) {
                ajaxCommand.append(";mojarra.l(").append(commandScript.getName()).append(")");
            }
        }

        return ajaxCommand.toString();
    }

    private static final Set<SearchExpressionHint> EXPRESSION_HINTS =
            EnumSet.of(SearchExpressionHint.RESOLVE_CLIENT_SIDE, SearchExpressionHint.RESOLVE_SINGLE_COMPONENT);
    
    // Appends an ids argument to the ajax command
    private static void appendIds(FacesContext facesContext,
                                  UIComponent component,
                                  StringBuilder builder,
                                  Collection<String> ids) {

        if ((null == ids) || ids.isEmpty()) {
            builder.append('0');
            return;
        }

        builder.append("'");

        SearchExpressionHandler handler = null;
        SearchExpressionContext searchExpressionContext = null;
        
        boolean first = true;

        for (String id : ids) {
            if (id.trim().length() == 0) {
                continue;
            }
            if (!first) {
                builder.append(' ');
            } else {
                first = false;
            }

            if (id.equals("@all") || id.equals("@none") ||
                id.equals("@form") || id.equals("@this")) {
                builder.append(id);
            } else {
                if (searchExpressionContext == null)  {
                    searchExpressionContext = SearchExpressionContext.createSearchExpressionContext(
                            facesContext, component, EXPRESSION_HINTS, null);
                }
                if (handler == null) {
                    handler = facesContext.getApplication().getSearchExpressionHandler();
                }
                
                builder.append(handler.resolveClientId(searchExpressionContext, id));
            }
        }

        builder.append("'");
    }
}
