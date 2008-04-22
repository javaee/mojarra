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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;

/**
 * Proxy instance for a groovy-based Renderers.  This allows the Renderer
 * to remain registered with the renderkit while picking up changes at runtime
 * from the associated groovy script.
 */
public class RendererProxy extends Renderer {

    private String scriptName;
    private GroovyHelper groovyHelper;


    // ------------------------------------------------------------ Constructors


    public RendererProxy(String scriptName, GroovyHelper groovyHelper) {
        this.scriptName = scriptName;
        this.groovyHelper = groovyHelper;
    }


    // --------------------------------------------------- Methods from Renderer


    @Override
    public void decode(FacesContext context, UIComponent component) {
        getGroovyDelegate().decode(context, component);
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {
        getGroovyDelegate().encodeBegin(context, component);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
    throws IOException {
        getGroovyDelegate().encodeChildren(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
    throws IOException {
        getGroovyDelegate().encodeEnd(context, component);
    }

    @Override
    public String convertClientId(FacesContext context, String clientId) {
        return getGroovyDelegate().convertClientId(context, clientId);
    }

    @Override
    public boolean getRendersChildren() {
        return getGroovyDelegate().getRendersChildren();
    }

    @Override
    public Object getConvertedValue(FacesContext context,
                                    UIComponent component,
                                    Object submittedValue)
    throws ConverterException {
        return getGroovyDelegate().getConvertedValue(context, component, submittedValue);
    }


    // --------------------------------------------------------- Private Methods


    private Renderer getGroovyDelegate() {

        return ((Renderer) groovyHelper.newInstance(scriptName));

    }
}
