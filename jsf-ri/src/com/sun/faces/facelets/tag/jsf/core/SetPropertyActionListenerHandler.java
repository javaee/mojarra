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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.tag.jsf.core;

import java.io.IOException;
import java.io.Serializable;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;
import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.ActionSource2;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;
import com.sun.faces.facelets.el.LegacyValueBinding;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import com.sun.faces.facelets.tag.TagConfig;
import com.sun.faces.facelets.tag.TagException;
import com.sun.faces.facelets.tag.TagHandler;
import com.sun.faces.facelets.tag.jsf.ComponentSupport;

public class SetPropertyActionListenerHandler extends TagHandler {

    private final TagAttribute value;

    private final TagAttribute target;

    public SetPropertyActionListenerHandler(TagConfig config) {
        super(config);
        this.value = this.getRequiredAttribute("value");
        this.target = this.getRequiredAttribute("target");
    }

    public void apply(FaceletContext ctx, UIComponent parent)
            throws IOException, FacesException, FaceletException, ELException {
        if (parent instanceof ActionSource) {
            ActionSource src = (ActionSource) parent;
            if (ComponentSupport.isNew(parent)) {
                ValueExpression valueExpr = this.value.getValueExpression(ctx,
                        Object.class);
                ValueExpression targetExpr = this.target.getValueExpression(
                        ctx, Object.class);

                ActionListener listener;

                if (src instanceof ActionSource2) {
                    listener = new SetPropertyListener(valueExpr, targetExpr);
                } else {
                    listener = new LegacySetPropertyListener(
                            new LegacyValueBinding(valueExpr),
                            new LegacyValueBinding(targetExpr));
                }

                src.addActionListener(listener);
            }
        } else {
            throw new TagException(this.tag,
                    "Parent is not of type ActionSource, type is: " + parent);
        }
    }

    private static class LegacySetPropertyListener implements ActionListener,
            Serializable {

        private ValueBinding value;

        private ValueBinding target;

        public LegacySetPropertyListener() {
        };

        public LegacySetPropertyListener(ValueBinding value, ValueBinding target) {
            this.value = value;
            this.target = target;
        }

        public void processAction(ActionEvent evt)
                throws AbortProcessingException {
            FacesContext faces = FacesContext.getCurrentInstance();
            Object valueObj = this.value.getValue(faces);
            this.target.setValue(faces, valueObj);
        }

    }

    private static class SetPropertyListener implements ActionListener,
            Serializable {

        private ValueExpression value;

        private ValueExpression target;

        public SetPropertyListener() {
        };

        public SetPropertyListener(ValueExpression value, ValueExpression target) {
            this.value = value;
            this.target = target;
        }

        public void processAction(ActionEvent evt)
                throws AbortProcessingException {
            FacesContext faces = FacesContext.getCurrentInstance();
            ELContext el = faces.getELContext();
            Object valueObj = this.value.getValue(el);
             if (valueObj != null) {
                ExpressionFactory factory =
                      faces.getApplication().getExpressionFactory();
                valueObj = factory.coerceToType(value, target.getType(el));
            }
            this.target.setValue(el, valueObj);
        }

    }

}
