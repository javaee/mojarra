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
package com.sun.faces.test.agnostic.statesaving.basic;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

@FacesComponent(value = "com.sun.faces.test.agnostic.statesaving.basic.DynamicParentComponent")
public class DynamicParentComponent extends UIComponentBase implements SystemEventListener {

    /**
     * Constructor.
     */
    public DynamicParentComponent() {
        setRendererType("com.sun.faces.test.agnostic.statesaving.basic.DynamicParentComponentRenderer");

        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();

        if (!context.isPostback()) {
            root.subscribeToViewEvent(PreRenderViewEvent.class, this);
        }
    }

    /**
     * Get the component family.
     *
     * @return the component family.
     */
    public String getFamily() {
        return "com.sun.faces.test.agnostic.statesaving.basic.DynamicParentComponent";
    }

    /**
     * Is listener for the given source.
     *
     * @param source the source object.
     * @return true if we are listening, false otherwise.
     */
    public boolean isListenerForSource(Object source) {
        return (source instanceof UIViewRoot);
    }

    /**
     * Process the system event.
     *
     * <p> Here we'll add a child in between static text. If dynamic component
     * state saving works properly then upon redisplay it should have first
     * static, then dynamic and then static text. </p>
     *
     * @param event the system event.
     * @throws AbortProcessingException when processing should be aborted.
     */
    public void processEvent(SystemEvent event)
            throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        HtmlOutputText outputText = (HtmlOutputText) context.getApplication().createComponent("javax.faces.HtmlOutputText");
        outputText.setValue("Dynamic Text");
        outputText.setEscape(false);
        getChildren().add(1, outputText);
    }
}
