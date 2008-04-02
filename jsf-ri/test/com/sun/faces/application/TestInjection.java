/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.application;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.spi.InjectionProvider;

public class TestInjection extends ServletFacesTestCase {

    public TestInjection() {
        super("TestInjection");
    }


    public TestInjection(String name) {
        super(name);
    }


    public void setUp() {
        super.setUp();
    }


    // ------------------------------------------------------------ Test Methods

    /**
     * Validate PostConstruct/PreDestroy annotations are property
     * invoked on protected, package private, and private methods.
     * @throws Exception if an error occurs
     */
    public void testInjection() throws Exception {
        ProtectedBean protectedBean = new ProtectedBean();
        PackagePrivateBean packagePrivateBean = new PackagePrivateBean();
        PrivateBean privateBean = new PrivateBean();
        ConcreteBean concreteBean = new ConcreteBean();

        ApplicationFactory aFactory =
              (ApplicationFactory) FactoryFinder.getFactory(
                    FactoryFinder.APPLICATION_FACTORY);
        aFactory.getApplication(); // bootstraps the ApplicationAssociate    
        ApplicationAssociate associate = ApplicationAssociate
              .getInstance(getFacesContext().getExternalContext());        
        assertNotNull(associate);       
        
        InjectionProvider injectionProvider = associate.getInjectionProvider();
        assertNotNull(injectionProvider);
        try {
            injectionProvider.inject(protectedBean);
            injectionProvider.invokePostConstruct(protectedBean);
            injectionProvider.invokePreDestroy(protectedBean);
            injectionProvider.inject(packagePrivateBean);
            injectionProvider.invokePostConstruct(packagePrivateBean);
            injectionProvider.invokePreDestroy(packagePrivateBean);
            injectionProvider.inject(privateBean);
            injectionProvider.invokePostConstruct(privateBean);
            injectionProvider.invokePreDestroy(privateBean);
            injectionProvider.inject(concreteBean);
            injectionProvider.invokePostConstruct(concreteBean);
            injectionProvider.invokePreDestroy(concreteBean);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            assertTrue(false);
        }
        
        assertTrue(protectedBean.getInit());
        assertTrue(protectedBean.getDestroy());
        assertTrue(packagePrivateBean.getInit());
        assertTrue(packagePrivateBean.getDestroy());
        assertTrue(privateBean.getInit());
        assertTrue(privateBean.getDestroy());
        assertTrue(concreteBean.getInit());
        assertTrue(concreteBean.getDestroy());
    }


    // ----------------------------------------------------------- Inner Classes

    private static class ProtectedBean {

        private boolean initCalled;
        private boolean destroyCalled;

        @PostConstruct void init() {
            initCalled = true;
        }

        @PreDestroy void destroy() {
            destroyCalled = true;
        }

        public boolean getInit() {
            return initCalled;
        }

        public boolean getDestroy() {
            return destroyCalled;
        }

    } // END ProtectedBean
    
    private static class PackagePrivateBean {

        private boolean initCalled;
        private boolean destroyCalled;

        @PostConstruct void init() {
            initCalled = true;
        }

        @PreDestroy void destroy() {
            destroyCalled = true;
        }

        public boolean getInit() {
            return initCalled;
        }

        public boolean getDestroy() {
            return destroyCalled;
        }

    } // END PackagePrivateBean
    
    private static class PrivateBean {

        private boolean initCalled;
        private boolean destroyCalled;

        @PostConstruct void init() {
            initCalled = true;
        }

        @PreDestroy void destroy() {
            destroyCalled = true;
        }

        public boolean getInit() {
            return initCalled;
        }

        public boolean getDestroy() {
            return destroyCalled;
        }

    } // END PrivateBean
    
    private static abstract class BaseBean {
        
        protected boolean initCalled;
        protected boolean destroyCalled;
        
        @PostConstruct void init() {
            initCalled = true;
        }
    }
    
    private static class ConcreteBean extends BaseBean {
        
        @PreDestroy void destroy() {
            destroyCalled = true;
        }
        
        public boolean getInit() {
            return initCalled;
        }

        public boolean getDestroy() {
            return destroyCalled;
        }
    }

} // END TestInjection