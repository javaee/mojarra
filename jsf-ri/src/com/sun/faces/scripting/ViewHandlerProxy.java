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

package com.sun.faces.scripting;

import java.io.IOException;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * Proxy instance for a groovy-based ViewHandler.  This allows the ViewHandler
 * to remain registered with the application chain while picking up changes at runtime
 * from the associated groovy script.
 */
public class ViewHandlerProxy extends ViewHandler {

    private String scriptName;
    private ViewHandler vhDelegate;


    // ------------------------------------------------------------ Constructors
    

    public ViewHandlerProxy(String scriptName, ViewHandler vhDelegate) {

        this.scriptName = scriptName;
        this.vhDelegate = vhDelegate;

    }


    // ------------------------------------------------ Methods from ViewHandler


    @Override
    public String calculateCharacterEncoding(FacesContext context) {
        return getGroovyDelegate().calculateCharacterEncoding(context);
    }

    @Override
    public void initView(FacesContext context) throws FacesException {
        getGroovyDelegate().initView(context);
    }


    public Locale calculateLocale(FacesContext context) {
        return getGroovyDelegate().calculateLocale(context);
    }

    public String calculateRenderKitId(FacesContext context) {
        return getGroovyDelegate().calculateRenderKitId(context);
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
        return getGroovyDelegate().createView(context, viewId);
    }

    public String getActionURL(FacesContext context, String viewId) {
        return getGroovyDelegate().getActionURL(context, viewId);
    }

    public String getResourceURL(FacesContext context, String path) {
        return getGroovyDelegate().getResourceURL(context, path);
    }

    public void renderView(FacesContext context, UIViewRoot viewToRender)
    throws IOException, FacesException {
        getGroovyDelegate().renderView(context, viewToRender);
    }

    public UIViewRoot restoreView(FacesContext context, String viewId) {
        return getGroovyDelegate().restoreView(context, viewId);
    }

    public void writeState(FacesContext context) throws IOException {
        getGroovyDelegate().writeState(context);
    }


    // --------------------------------------------------------- Private Methods


    private ViewHandler getGroovyDelegate() {

        try {
            return ((ViewHandler) GroovyHelper.newInstance(scriptName,
                                                           ViewHandler.class,
                                                           vhDelegate));
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }
    
}
