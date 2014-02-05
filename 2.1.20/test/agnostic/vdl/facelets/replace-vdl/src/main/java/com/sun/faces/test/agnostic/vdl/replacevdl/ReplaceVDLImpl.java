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
package com.sun.faces.test.agnostic.vdl.replacevdl;

import java.beans.BeanInfo;
import java.io.IOException;
import java.util.Map;
import javax.faces.FacesWrapper;
import javax.faces.application.Resource;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.StateManagementStrategy;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewMetadata;

public class ReplaceVDLImpl extends ViewDeclarationLanguage implements FacesWrapper<ViewDeclarationLanguage> {

    private ViewDeclarationLanguage toWrap;

    public ReplaceVDLImpl(ViewDeclarationLanguage toWrap) {
        this.toWrap = toWrap;
    }

    public void logMethodInvocation(String method) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        final String key = "VDLImplMessage";

        StringBuilder builder = (StringBuilder) requestMap.get(key);
        if (null == builder) {
            builder = new StringBuilder();
            requestMap.put(key, builder);
        }
        builder.append(" ").append(method);
    }

    @Override
    public void buildView(FacesContext fc, UIViewRoot uivr) throws IOException {
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        if (!params.containsKey("skipBuildView")) {
            this.logMethodInvocation("buildView");
            this.getWrapped().buildView(fc, uivr);
        }
    }

    public ViewDeclarationLanguage getWrapped() {
        return this.toWrap;
    }

    @Override
    public UIViewRoot createView(FacesContext fc, String string) {
        this.logMethodInvocation("createView");
        return this.getWrapped().createView(fc, string);
    }

    @Override
    public BeanInfo getComponentMetadata(FacesContext fc, Resource rsrc) {
        this.logMethodInvocation("getComponentMetadata");
        return this.getWrapped().getComponentMetadata(fc, rsrc);
    }

    @Override
    public Resource getScriptComponentResource(FacesContext fc, Resource rsrc) {
        this.logMethodInvocation("getScriptComponentResource");
        return this.getWrapped().getScriptComponentResource(fc, rsrc);
    }

    @Override
    public StateManagementStrategy getStateManagementStrategy(FacesContext fc, String string) {
        this.logMethodInvocation("getStateManagementStrategy");
        return this.getWrapped().getStateManagementStrategy(fc, string);
    }

    @Override
    public ViewMetadata getViewMetadata(FacesContext fc, String string) {
        this.logMethodInvocation("getViewMetadata");
        return this.getWrapped().getViewMetadata(fc, string);
    }

    @Override
    public void renderView(FacesContext fc, UIViewRoot uivr) throws IOException {
        this.logMethodInvocation("renderView");
        this.getWrapped().renderView(fc, uivr);
    }

    @Override
    public UIViewRoot restoreView(FacesContext fc, String string) {
        this.logMethodInvocation("restoreView");
        return this.getWrapped().restoreView(fc, string);
    }
}
