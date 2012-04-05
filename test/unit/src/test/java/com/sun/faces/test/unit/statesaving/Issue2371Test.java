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
package com.sun.faces.test.unit.statesaving;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ApplicationStateInfo;
import com.sun.faces.application.view.StateManagementStrategyImpl;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.context.StateContext;
import com.sun.faces.facelets.Facelet;
import com.sun.faces.facelets.FaceletFactory;
import com.sun.faces.renderkit.RenderKitUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.ResponseStateManager;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;
import javax.faces.view.ViewMetadata;
import javax.servlet.ServletContext;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.*;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import static org.easymock.EasyMock.expect;
import org.junit.Ignore;
import org.powermock.api.easymock.PowerMock;

/**
 * A JUnit test for issue #2371.
 *
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FacesContext.class, FactoryFinder.class, RenderKitUtils.class})
public class Issue2371Test {

    /**
     * Test saving without a Faces context (saveView should return null).
     */
    @Test
    public void testSaveNoFacesContext() {
        FactoryFinder.setFactory(
                FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY,
                "com.sun.faces.application.view.ViewDeclarationLanguageFactoryImpl");
        StateManagementStrategyImpl strategy = new StateManagementStrategyImpl();
        assertNull(strategy.saveView(null));
    }

    /**
     * Test saving a transient root (saveView should return null).
     */
    @Test
    public void testSaveTransientRoot() {
        FactoryFinder.setFactory(
                FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY,
                "com.sun.faces.application.view.ViewDeclarationLanguageFactoryImpl");
        FacesContext facesContext = EasyMock.createMock(FacesContext.class);
        StateManagementStrategyImpl strategy = new StateManagementStrategyImpl();
        UIViewRoot viewRoot = new UIViewRoot();
        viewRoot.setTransient(true);
        expect(facesContext.getViewRoot()).andReturn(viewRoot);
        replay(facesContext);
        assertNull(strategy.saveView(facesContext));
        verify(facesContext);
    }

    /**
     * Test saving a non-transient UIViewRoot.
     */
    @Test
    public void testSaveNonTransientRoot() throws Exception {
        FactoryFinder.setFactory(
                FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY,
                "com.sun.faces.application.view.ViewDeclarationLanguageFactoryImpl");
        FactoryFinder.setFactory(
                FactoryFinder.VISIT_CONTEXT_FACTORY,
                "com.sun.faces.component.visit.VisitContextFactoryImpl");
        FacesContext facesContext = EasyMock.createMock(FacesContext.class);
        ExternalContext extContext = EasyMock.createMock(ExternalContext.class);
        HashMap<Object, Object> fcAttributes = new HashMap<Object, Object>();
        StateManagementStrategyImpl strategy = new StateManagementStrategyImpl();
        StateContext stateContext = createStateContext();
        fcAttributes.put(StateContext.class.getName() + "_KEY", stateContext);
        UIViewRoot viewRoot = new UIViewRoot();
        expect(facesContext.getViewRoot()).andReturn(viewRoot);
        expect(facesContext.getAttributes()).andReturn(fcAttributes);
        expect(facesContext.getAttributes()).andReturn(fcAttributes);
        expect(facesContext.getAttributes()).andReturn(fcAttributes);
        expect(facesContext.getExternalContext()).andReturn(extContext);
        expect(extContext.getInitParameter("javax.faces.HONOR_CURRENT_COMPONENT_ATTRIBUTES")).andReturn("false");
        expect(facesContext.getExternalContext()).andReturn(extContext);
        expect(extContext.getInitParameter("javax.faces.HONOR_CURRENT_COMPONENT_ATTRIBUTES")).andReturn("false");
        expect(facesContext.getAttributes()).andReturn(fcAttributes);
        expect(facesContext.getAttributes()).andReturn(fcAttributes);
        replay(facesContext, extContext);
        Object state = strategy.saveView(facesContext);
        assertNotNull(state);
        verify(facesContext, extContext);
    }

    /**
     * Test restoring without active state (should return null).
     */
    @Test
    public void testRestoreWithoutActiveState() throws Exception {
        FacesContext fc = EasyMock.createMock(FacesContext.class);
        ViewDeclarationLanguageFactory vdlFactory = EasyMock.createMock(ViewDeclarationLanguageFactory.class);
        ResponseStateManager rsm = EasyMock.createMock(ResponseStateManager.class);
        ViewDeclarationLanguage vdl = EasyMock.createMock(ViewDeclarationLanguage.class);
        ViewMetadata viewMetadata = EasyMock.createMock(ViewMetadata.class);

        mockStatic(FacesContext.class);
        mockStatic(FactoryFinder.class);
        mockStatic(RenderKitUtils.class);

        UIViewRoot viewRoot = PowerMock.createPartialMock(UIViewRoot.class, "getFacesContext");
        Object[] state = null;
        
        expect(FactoryFinder.getFactory("javax.faces.view.ViewDeclarationLanguageFactory")).andReturn(vdlFactory);
        expect(RenderKitUtils.getResponseStateManager(fc, "HTML_BASIC")).andReturn(rsm);
        expect(fc.isProcessingEvents()).andReturn(false);
        expect(vdlFactory.getViewDeclarationLanguage("/faces/test.xhtml")).andReturn(vdl);
        expect(vdl.getViewMetadata(fc, "/faces/test.xhtml")).andReturn(viewMetadata);
        expect(viewMetadata.createMetadataView(fc)).andReturn(viewRoot);
        fc.setViewRoot(viewRoot);
        fc.setProcessingEvents(true);
        vdl.buildView(fc, viewRoot);
        expect(rsm.getState(fc, "/faces/test.xhtml")).andReturn(state);
        replay(fc, FacesContext.class, FactoryFinder.class, RenderKitUtils.class, vdlFactory, rsm, vdl, viewMetadata, viewRoot);
        
        StateManagementStrategyImpl strategy = new StateManagementStrategyImpl();
        assertNull(strategy.restoreView(fc, "/faces/test.xhtml", "HTML_BASIC"));

        verify(fc, FacesContext.class, FactoryFinder.class, RenderKitUtils.class, vdlFactory, rsm, vdl, viewMetadata, viewRoot);
    }

    /**
     * Utility method to create a state context.
     */
    private StateContext createStateContext() {
        StateContext result = null;
        try {
            Constructor constructor = StateContext.class.getDeclaredConstructor(ApplicationStateInfo.class);
            constructor.setAccessible(true);
            result = (StateContext) constructor.newInstance((ApplicationStateInfo) null);
        } catch (Exception exception) {
        }
        return result;
    }

    /**
     * Utility method to set FacesContext.
     */
    private void setFacesContext(FacesContext fc) {
        try {
            Method method = FacesContext.class.getDeclaredMethod("setCurrentInstance", FacesContext.class);
            method.setAccessible(true);
            method.invoke(null, fc);
        } catch (Exception exception) {
        }
    }
}
