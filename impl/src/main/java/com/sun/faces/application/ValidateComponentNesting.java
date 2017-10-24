/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.application;

import java.util.EnumSet;

import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.component.ActionSource;
import javax.faces.component.ActionSource2;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewAction;
import javax.faces.component.UIViewParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import com.sun.faces.util.MessageUtils;

/**
 * <p>
 * ValidateFormComponentNesting performs component tree validation to assure {@link ActionSource} ,
 * {@link ActionSource2} and {@link EditableValueHolder} components are placed inside a form.
 * ValidateFormComponentNesting is installed automatically if {@link ProjectStage#Development} is
 * active.
 * </p>
 * 
 * @author dueni
 * 
 */
public class ValidateComponentNesting implements SystemEventListener {

    @Override
    public boolean isListenerForSource(Object source) {
        return (source instanceof UIViewRoot);
    }

    /**
     * Process PostAddToViewEvent on UIViewRoot to validate form - action/input nesting.
     */
    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        UIComponent root = (UIComponent) event.getSource();
        FacesContext jsf = FacesContext.getCurrentInstance();
        EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_ITERATION);
        VisitContext visitContext = VisitContext.createVisitContext(jsf, null, hints);

        root.visitTree(visitContext, new ValidateFormNestingCallback());
    }

    static class ValidateFormNestingCallback implements VisitCallback {

        // report missing form problem only once
        boolean reportedOmittedFormOnce = false;

        // report missing metadata problem only once
        boolean reportedOmittedMetadataOnce = false;

        @Override
        public VisitResult visit(VisitContext context, UIComponent target) {
            // default result: continue tree walk
            VisitResult result = VisitResult.ACCEPT;

            if (target instanceof UIForm || target.getFamily().endsWith("Form") || UIViewRoot.METADATA_FACET_NAME.equals(target.getId())) {
                // stop tree walk if component is of type UIForm or component family ends on "Form"
                // or if the component is the UIPanel with id METADATA_FACET_NAME
                result = VisitResult.REJECT;
            } else if (target instanceof UIViewParameter || target instanceof UIViewAction) {
                if (reportedOmittedMetadataOnce) {
                    // report first detected problem only, then stop tree walk
                    result = VisitResult.COMPLETE;
                }
                addOmittedMessage(context.getFacesContext(), target.getClientId(context.getFacesContext()), MessageUtils.MISSING_METADATA_ERROR);
                reportedOmittedMetadataOnce = true;

            } else if (target instanceof EditableValueHolder || target instanceof ActionSource || target instanceof ActionSource2) {
                if (reportedOmittedFormOnce) {
                    // report first detected problem only, then stop tree walk
                    result = VisitResult.COMPLETE;
                }
                // if we find ActionSource, ActionSource2 or EditableValueHolder, that component
                // must be outside of a form add warning message
                addOmittedMessage(context.getFacesContext(), target.getClientId(context.getFacesContext()), MessageUtils.MISSING_FORM_ERROR);
                reportedOmittedFormOnce = true;

            }
            return result;
        }
    }

    /**
     * method for adding a message regarding missing ancestor to context
     * 
     * @param ctx
     * @param clientId
     */
    private static void addOmittedMessage(FacesContext jsf, String clientId, String key) {
        Object[] params = new Object[] {};

        FacesMessage m = MessageUtils.getExceptionMessage(key, params);
        m.setSeverity(FacesMessage.SEVERITY_WARN);
        jsf.addMessage(clientId, m);
    }

}
