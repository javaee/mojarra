/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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


package com.sun.faces.el;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.mock.MockELContext;
import com.sun.faces.mock.MockExternalContext;
import com.sun.faces.mock.MockFacesContext;
import com.sun.faces.mock.MockHttpServletRequest;
import com.sun.faces.mock.MockHttpServletResponse;
import com.sun.faces.mock.MockHttpSession;
import com.sun.faces.mock.MockServletContext;

/**
 * Tests needs to be run with assertions enabled (-ea:com.sun.faces.el.ChainAwareVariableResolver)
 */
public class TestNestedELResolver extends TestCase {

    private static final String UNPREFIXED_VALUE = "unprefixedValue";

    private static final String UNPREFIXED_KEY = "unprefixedKey";

    private static final String PREFIXED_VALUE = "prefixedValue";

    private static final String PREFIX = "test:";

    private static final String PREFIXED_KEY = PREFIX + "value";

    private ELContext elContext;

    public void setUp() throws Exception {
        FacesContext facesContext = createStubbedFacesContext();
        this.elContext = facesContext.getELContext();
        facesContext.getExternalContext().getApplicationMap()
              .put(PREFIXED_KEY, PREFIXED_VALUE);
        facesContext.getExternalContext().getApplicationMap()
              .put(UNPREFIXED_KEY, UNPREFIXED_VALUE);
    }

    public void testShouldResolveVariableWhenNestedELResolverCallCanNotResolve()
          throws Exception {
        assertEquals(UNPREFIXED_VALUE,
                     this.elContext.getELResolver().getValue(this.elContext,
                                                             null,
                                                             UNPREFIXED_KEY));
    }

    public void testShouldResolveVariableViaNestedELResolverCall()
          throws Exception {
        assertEquals(PREFIXED_VALUE,
                     this.elContext.getELResolver().getValue(this.elContext,
                                                             null,
                                                             PREFIXED_KEY));
    }

    ///wish I could use FacesTester ;-)

    private FacesContext createStubbedFacesContext() throws Exception {
        ServletContext context = new MockServletContext();
        ((MockServletContext) context).addInitParameter("javax.faces.DISABLE_FACELET_JSF_VIEWHANDLER", "true");
        HttpSession session = new MockHttpSession(context);
        ServletRequest request = new MockHttpServletRequest(session);
        ServletResponse response = new MockHttpServletResponse();
        ExternalContext externalContect = new MockExternalContext(context,
                                                                  request,
                                                                  response);
        MockFacesContext mockFacesContext = new MockFacesContext(externalContect);
        mockFacesContext.setApplication(new ApplicationImpl());
        ApplicationAssociate associate = getApplicationAssociate(
              mockFacesContext);
        associate
              .setELResolversFromFacesConfig(Collections.<ELResolver>singletonList(
                    new NestedELResolver(PREFIX)));
        associate.setLegacyVariableResolver(new ChainAwareVariableResolver());
        FacesCompositeELResolver facesCompositeELResolver = 
      new DemuxCompositeELResolver(
           FacesCompositeELResolver.ELResolverChainType.Faces);
        ELUtils.buildFacesResolver(facesCompositeELResolver, associate);
        ELContext elContext = mockFacesContext.getELContext();
        setELResolverOnElContext(facesCompositeELResolver, elContext);
        return mockFacesContext;
    }


    private ApplicationAssociate getApplicationAssociate(MockFacesContext mockFacesContext) {
        return (ApplicationAssociate) mockFacesContext.getExternalContext()
              .getApplicationMap()
              .get(RIConstants.FACES_PREFIX + "ApplicationAssociate");
    }

    //there should be a better way for doing this when using mocks/stubs
    private void setELResolverOnElContext(FacesCompositeELResolver facesCompositeELResolver, ELContext elContext)
          throws Exception {
        Field field = elContext.getClass().getDeclaredField("resolver");
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(elContext, facesCompositeELResolver);
        } finally {
            field.setAccessible(accessible);
        }
    }

    /*
      * Uses ElContext.getElResolver().getValue() inside its own getValue()
      */
    public static class NestedELResolver extends ELResolver {

        private final String prefix;

        public NestedELResolver(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Class<?> getCommonPropertyType(ELContext arg0, Object arg1) {
            return null;
        }

        @Override
        public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext arg0, Object arg1) {
            return null;
        }

        @Override
        public Class<?> getType(ELContext arg0, Object arg1, Object arg2) {
            return null;
        }

        @Override
        public Object getValue(ELContext context, Object base, Object property) {
            if (context.getContext(NestedELResolver.class) != Boolean.TRUE) {
                context.putContext(NestedELResolver.class, Boolean.TRUE);
                try {
                    Object value = context.getELResolver()
                          .getValue(context, base, this.prefix + property);
                    context.setPropertyResolved(value != null);
                    return value;
                } finally {
                    context.putContext(NestedELResolver.class, Boolean.FALSE);
                }
            }
            return null;
        }

        @Override
        public boolean isReadOnly(ELContext arg0, Object arg1, Object arg2) {
            return false;
        }

        @Override
        public void setValue(ELContext arg0, Object arg1, Object arg2, Object arg3) {

        }

    }

}
