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
package com.sun.faces.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

/**
 * The JUnit tests for the ExternalContextImpl class.
 */
public class ExternalContextImplTest {

    /**
     * Test getRequestCookieMap method.
     */
    @Test
    public void testGetRequestCookieMap() {
        ServletContext servletContext = PowerMock.createNiceMock(ServletContext.class);
        HttpServletRequest request = PowerMock.createNiceMock(HttpServletRequest.class);
        HttpServletResponse response = PowerMock.createNiceMock(HttpServletResponse.class);
        replay(servletContext, request, response);
        
        ExternalContextImpl externalContext = new ExternalContextImpl(servletContext, request, response);
        verify(servletContext, request, response);
        
        assertNotNull(externalContext.getRequestCookieMap());
    }

    /**
     * Test getRequestCookieMap method (test supported methods).
     */
    @Test
    public void testGetRequestCookieMap2() {
        ServletContext servletContext = PowerMock.createNiceMock(ServletContext.class);
        HttpServletRequest request = PowerMock.createNiceMock(HttpServletRequest.class);
        HttpServletResponse response = PowerMock.createNiceMock(HttpServletResponse.class);
        Cookie cookie = new Cookie("foo", "bar");
        expect(request.getCookies()).andReturn(new Cookie[]{cookie}).anyTimes();
        replay(servletContext, request, response);
        
        ExternalContextImpl externalContext = new ExternalContextImpl(servletContext, request, response);
        verify(servletContext, request, response);
        Map<String, Object> requestCookieMap = externalContext.getRequestCookieMap();
        assertTrue(requestCookieMap.get("foo") instanceof Cookie);
        Cookie value = (Cookie) requestCookieMap.get("foo");
        
        assertTrue(value.getValue().equals("bar"));
        assertTrue(requestCookieMap.containsKey("foo"));
        assertTrue(requestCookieMap.containsValue(requestCookieMap.get("foo")));
        assertTrue(!requestCookieMap.entrySet().isEmpty());
        assertTrue(!requestCookieMap.values().isEmpty());
        assertTrue(!requestCookieMap.keySet().isEmpty());
        assertTrue(requestCookieMap.size() >= 1);
        assertTrue(!requestCookieMap.equals(new HashMap<>()));
    }

    /**
     * Test getRequestCookieMap method (test the unmodifiable nature of the
     * returned map).
     */
    @Test
    public void testGetRequestCookieMap3() {
        ServletContext servletContext = PowerMock.createNiceMock(ServletContext.class);
        HttpServletRequest request = PowerMock.createNiceMock(HttpServletRequest.class);
        HttpServletResponse response = PowerMock.createNiceMock(HttpServletResponse.class);
        Cookie cookie = new Cookie("foo", "bar");
        expect(request.getCookies()).andReturn(new Cookie[]{cookie}).anyTimes();
        replay(servletContext, request, response);
        
        ExternalContextImpl externalContext = new ExternalContextImpl(servletContext, request, response);
        verify(servletContext, request, response);
        Map<String, Object> requestCookieMap = externalContext.getRequestCookieMap();

        Iterator<Entry<String, Object>> entryIterator = requestCookieMap.entrySet().iterator();
        entryIterator.next();
        try {
            entryIterator.remove();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }

        Iterator<String> keyIterator = requestCookieMap.keySet().iterator();
        keyIterator.next();
        try {
            keyIterator.remove();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }

        Iterator<Object> valueIterator = requestCookieMap.values().iterator();
        valueIterator.next();
        try {
            valueIterator.remove();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }

        try {
            requestCookieMap.entrySet().remove("test");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }

        try {
            requestCookieMap.keySet().remove("test");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }

        try {
            requestCookieMap.values().remove("test");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    /**
     * Test getRequestCookieMap method (test the unsupported methods throw an
     * UnsupportedOperationException).
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetRequestCookieMap4() {
        ServletContext servletContext = PowerMock.createNiceMock(ServletContext.class);
        HttpServletRequest request = PowerMock.createNiceMock(HttpServletRequest.class);
        HttpServletResponse response = PowerMock.createNiceMock(HttpServletResponse.class);
        Cookie cookie = new Cookie("foo", "bar");
        expect(request.getCookies()).andReturn(new Cookie[]{cookie}).anyTimes();
        replay(servletContext, request, response);
        
        ExternalContextImpl externalContext = new ExternalContextImpl(servletContext, request, response);
        verify(servletContext, request, response);
        Map<String, Object> requestCookieMap = externalContext.getRequestCookieMap();
        boolean exceptionThrown = false;
        try {
            requestCookieMap.clear();
        } catch (UnsupportedOperationException e) {
            exceptionThrown = true;
        }
        
        assertTrue(exceptionThrown);
        verifySupplier(() -> requestCookieMap.put("foot", "bar"));
        verifyConsumer(m -> requestCookieMap.putAll((Map<? extends String, ? extends Object>) m), new HashMap<>());
        verifySupplier(() -> requestCookieMap.remove("foo"));
    }

    /**
     * Verify that the passed consumer throws an UnsupportedOperationException.
     *
     * @param consumer the consumer.
     * @param argument the argument.
     */
    private void verifyConsumer(Consumer<Object> consumer, Object argument) {
        boolean exceptionThrown = false;
        
        try {
            consumer.accept(argument);
        } catch (UnsupportedOperationException e) {
            exceptionThrown = true;
        }
        
        assertTrue(exceptionThrown);
    }

    /**
     * Verify that the passed supplier throws an UnsupportedOperationException.
     *
     * @param supplier the supplier.
     */
    private void verifySupplier(Supplier<?> supplier) {
        boolean exceptionThrown = false;
        
        try {
            supplier.get();
        } catch (UnsupportedOperationException e) {
            exceptionThrown = true;
        }
        
        assertTrue(exceptionThrown);
    }
}
