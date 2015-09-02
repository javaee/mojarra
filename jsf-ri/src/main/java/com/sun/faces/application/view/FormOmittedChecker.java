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
package com.sun.faces.application.view;

import com.sun.faces.util.MessageUtils;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.component.ActionSource;
import javax.faces.component.ActionSource2;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

/**
 * A convenience class that checks for omitted forms.
 */
class FormOmittedChecker {

    /**
     * Stores the skip hint.
     */
    private static String SKIP_ITERATION_HINT = "javax.faces.visit.SKIP_ITERATION";

    /**
     * Constructor.
     */
    private FormOmittedChecker() {
    }

    /**
     * Check if omitted forms are present.
     *
     * @param context the Faces context.
     */
    public static void check(FacesContext context) {
        final FacesContext finalContext = context;
        UIViewRoot viewRoot = context.getViewRoot();
        List<UIComponent> children = viewRoot.getChildren();

        for (UIComponent child : children) {
            try {
                context.getAttributes().put(SKIP_ITERATION_HINT, true);
                Set<VisitHint> hints = EnumSet.of(VisitHint.SKIP_ITERATION);

                VisitContext visitContext = VisitContext.createVisitContext(context, null, hints);
                child.visitTree(visitContext, new VisitCallback() {

                    @Override
                    public VisitResult visit(VisitContext visitContext, UIComponent component) {
                        VisitResult result = VisitResult.ACCEPT;

                        if (isForm(component)) {
                            result = VisitResult.REJECT;
                        } else if (isInNeedOfForm(component)) {
                            addFormOmittedMessage(finalContext, component);
                        }
                        return result;
                    }
                });
            } finally {
                context.getAttributes().remove(SKIP_ITERATION_HINT);
            }
        }
    }

    /**
     * Is the component a form.
     *
     * <p> Note normally a form inherits from UIForm, but there might be some
     * component libraries out there that might not honor that. So we check the
     * component family to avoid warning in cases where 3rd party form component
     * that does not extend UIForm (eg. tr:form) is used. </p>
     *
     * @param component the UI component.
     * @return true if it is a form, false otherwise.
     */
    private static boolean isForm(UIComponent component) {
        return (component instanceof UIForm || (component.getFamily() != null && component.getFamily().endsWith("Form")));
    }

    /**
     * Is the component in need of a form.
     *
     * @param component the UI component.
     * @return true if the component is in need of a form, false otherwise.
     */
    private static boolean isInNeedOfForm(UIComponent component) {
        return (component instanceof ActionSource
                || component instanceof ActionSource2
                || component instanceof EditableValueHolder);
    }

    /**
     * Add the form omitted message.
     *
     * @param context the Faces context.
     * @param component the UI component.
     */
    private static void addFormOmittedMessage(FacesContext context, UIComponent component) {
        String key = MessageUtils.MISSING_FORM_ERROR;
        Object[] parameters = new Object[]{component.getClientId(context)};
        boolean missingFormReported = false;

        FacesMessage message = MessageUtils.getExceptionMessage(key, parameters);
        List<FacesMessage> messages = context.getMessageList();
        for (FacesMessage item : messages) {
            if (item.getDetail().equals(message.getDetail())) {
                missingFormReported = true;
                break;
            }
        }
        if (!missingFormReported) {
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            context.addMessage(null, message);
        }
    }
}
