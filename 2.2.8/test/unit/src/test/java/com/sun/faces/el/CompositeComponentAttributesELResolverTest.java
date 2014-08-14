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
package com.sun.faces.el;

import com.sun.faces.facelets.tag.composite.CompositeComponentBeanInfo;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.el.ELContext;
import javax.el.MapELResolver;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import org.easymock.EasyMock;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

/**
 * The JUnit tests for the CompositeComponentAttributesELResolver class.
 *
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
public class CompositeComponentAttributesELResolverTest {

    /**
     * Test issue #2508.
     */
    @Test
    public void testGetValue() throws Exception {
        ELContext elContext1 = EasyMock.createNiceMock(ELContext.class);
        FacesContext facesContext1 = EasyMock.createNiceMock(FacesContext.class);
        ELContext elContext2 = EasyMock.createNiceMock(ELContext.class);
        FacesContext facesContext2 = EasyMock.createNiceMock(FacesContext.class);
        
        HashMap<Object, Object> ctxAttributes1 = new HashMap<Object, Object>();
        UIPanel composite = new UIPanel();
        CompositeComponentBeanInfo compositeBeanInfo = new CompositeComponentBeanInfo();
        BeanDescriptor beanDescriptor = new BeanDescriptor(composite.getClass());
        compositeBeanInfo.setBeanDescriptor(beanDescriptor);
        composite.getAttributes().put(Resource.COMPONENT_RESOURCE_KEY, "dummy");
        composite.getAttributes().put(UIComponent.BEANINFO_KEY, compositeBeanInfo);
        String property = "attrs";

        expect(elContext1.getContext(FacesContext.class)).andReturn(facesContext1);
        expect(facesContext1.getAttributes()).andReturn(ctxAttributes1);
        expect(elContext2.getContext(FacesContext.class)).andReturn(facesContext2);
        expect(facesContext2.getAttributes()).andReturn(ctxAttributes1);
        replay(elContext1, facesContext1, elContext2, facesContext2);
        
        CompositeComponentAttributesELResolver elResolver = new CompositeComponentAttributesELResolver();
        Map<String, Object> evalMap1 = (Map<String, Object>) elResolver.getValue(elContext1, composite, property);
        assertNotNull(evalMap1);
        Map<String, Object> evalMap2 = (Map<String, Object>) elResolver.getValue(elContext2, composite, property);
        assertNotNull(evalMap2);
        
        Field ctxField1 = evalMap1.getClass().getDeclaredField("ctx");
        ctxField1.setAccessible(true);       
        Field ctxField2 = evalMap2.getClass().getDeclaredField("ctx");
        ctxField2.setAccessible(true);

        assertTrue(evalMap1 == evalMap2);
        assertTrue(facesContext1 != ctxField1.get(evalMap1));
        assertTrue(facesContext2 == ctxField1.get(evalMap1));
        assertTrue(facesContext1 != ctxField2.get(evalMap2));
        assertTrue(facesContext2 == ctxField2.get(evalMap2));
        
        verify(elContext1, facesContext1, elContext2, facesContext2);
    }
}
