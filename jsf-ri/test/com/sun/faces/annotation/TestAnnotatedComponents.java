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
 */

package com.sun.faces.annotation;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.FactoryFinder;
import javax.faces.validator.Validator;
import javax.faces.convert.Converter;
import javax.faces.component.UIComponent;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.NamedEventManager;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.mgbean.ManagedBeanInfo;
import com.sun.faces.mgbean.BeanBuilder;


public class TestAnnotatedComponents extends ServletFacesTestCase {


    // ------------------------------------------------------------ Constructors


    public TestAnnotatedComponents() {
        super("TestAnnotatedComponents");
    }


    public TestAnnotatedComponents(String name) {
        super(name);
    }


    // ------------------------------------------------------------ Test Methods


    public void testAnnotatedComponentsWebInfClasses() throws Exception {

        FacesContext ctx = getFacesContext();
        Application app = ctx.getApplication();

        UIComponent c = app.createComponent("AnnotatedComponent");
        assertNotNull(c);
        assertTrue(c instanceof AnnotatedComponent);

        Converter cv = app.createConverter("AnnotatedConverter");
        assertNotNull(cv);
        assertTrue(cv instanceof AnnotatedConverter);

        cv = app.createConverter(java.lang.CharSequence.class);
        assertNotNull(cv);
        assertTrue(cv instanceof AnnotatedConverterForClass);

        Validator v = app.createValidator("AnnotatedValidator");
        assertNotNull(v);
        assertTrue(v instanceof AnnotatedValidator);

        RenderKitFactory rkf = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit rk = rkf.getRenderKit(ctx, RenderKitFactory.HTML_BASIC_RENDER_KIT);
        assertNotNull(rk);

        Renderer r = rk.getRenderer("AnnotatedRenderer", "AnnotatedRenderer");
        assertNotNull(r);
        assertTrue(r instanceof AnnotatedRenderer);

        // validate class annotated with @ManagedBean
        ApplicationAssociate associate =
              ApplicationAssociate.getInstance(ctx.getExternalContext());
        BeanManager manager = associate.getBeanManager();
        BeanBuilder bean1 = manager.getBuilder("annotatedBean");
        assertNotNull(bean1);
        ManagedBeanInfo bean1Info = bean1.getManagedBeanInfo();
        assertEquals("annotatedBean", "annotatedBean", bean1Info.getName());
        assertEquals("request", "request", bean1Info.getScope());
        assertFalse(bean1Info.isEager());
        List<ManagedBeanInfo.ManagedProperty> managedProperties = bean1Info.getManagedProperties();
        assertNotNull(managedProperties);
        assertTrue(managedProperties.size() == 2);
        ManagedBeanInfo.ManagedProperty p1 = managedProperties.get(0);
        assertEquals("silly", "silly", p1.getPropertyName());
        assertEquals(String.class.getName(), String.class.getName(), p1.getPropertyClass());
        assertEquals("#{requestScope.name}", "#{requestScope.name}", p1.getPropertyValue());
        ManagedBeanInfo.ManagedProperty p2 = managedProperties.get(1);
        assertEquals("age", "age", p2.getPropertyName());
        assertEquals(Integer.TYPE.getName(), Integer.TYPE.getName(), p2.getPropertyClass());
        assertEquals("#{requestScope.age}", "#{requestScope.age}", p2.getPropertyValue());
        request.setAttribute("name", "Bill");
        request.setAttribute("age", 33);
        AnnotatedBean bean1Instance = (AnnotatedBean) manager.create("annotatedBean", ctx);
        assertEquals("Bill", "Bill", bean1Instance.getSilly());
        assertEquals(33, 33, bean1Instance.getAge());
        assertNotNull(request.getAttribute("annotatedBean"));
        request.removeAttribute("annotatedBean");

    }

     public void testAnnotatedComponentsWebInfLib() throws Exception {

        FacesContext ctx = getFacesContext();
        Application app = ctx.getApplication();

        UIComponent c = app.createComponent("AnnotatedComponent2");
        assertNotNull(c);
        assertTrue(c.getClass().getName().endsWith("AnnotatedComponent2"));

        Converter cv = app.createConverter("AnnotatedConverter2");
        assertNotNull(cv);
        assertTrue(cv.getClass().getName().endsWith("AnnotatedConverter2"));


        Validator v = app.createValidator("AnnotatedValidator2");
        assertNotNull(v);
        assertTrue(v.getClass().getName().endsWith("AnnotatedValidator2"));

        RenderKitFactory rkf = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit rk = rkf.getRenderKit(ctx, RenderKitFactory.HTML_BASIC_RENDER_KIT);
        assertNotNull(rk);

        Renderer r = rk.getRenderer("AnnotatedRenderer2", "AnnotatedRenderer2");
        assertNotNull(r);
        assertTrue(r.getClass().getName().endsWith("AnnotatedRenderer2"));

        // Test default naming logic
        assertNotNull(ApplicationAssociate.getInstance(ctx.getExternalContext()).getNamedEventManager().getNamedEvent("com.sun.faces.annotation.annotatedComponentSystem"));
        // Test short name
        assertNotNull(ApplicationAssociate.getInstance(ctx.getExternalContext()).getNamedEventManager().getNamedEvent("com.sun.faces.annotation.anotherAnnotatedComponentSystem"));
        assertNotNull(ApplicationAssociate.getInstance(ctx.getExternalContext()).getNamedEventManager().getNamedEvent("explicitEventName"));
    }
}
