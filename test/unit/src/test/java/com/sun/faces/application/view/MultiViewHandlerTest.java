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
package com.sun.faces.application.view;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import javax.faces.context.ExternalContext;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import org.junit.Before;
import org.easymock.EasyMock;
import org.junit.Test;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class) 
@PrepareForTest(MultiViewHandler.class) 
public class MultiViewHandlerTest {

    private FacesContext facesContext;
    private Application application;
    private ExternalContext externalContext;

    @Before
    public void setUp() {
        facesContext = EasyMock.createMock(FacesContext.class);
        application = EasyMock.createMock(Application.class);
        externalContext = EasyMock.createMock(ExternalContext.class);
    }

    @Test
    public void testBCP47Support() throws Exception {

        Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("Accept-Language", "zh-Hans-CN");
        List<Locale> requestLocales = new ArrayList<Locale>();
        requestLocales.add(new Locale("en-US"));
        List<Locale> supportedLocales = new ArrayList<Locale>();
        supportedLocales.add(new Locale("zh", "CN"));
        supportedLocales.add(new Locale("en", "US"));
        supportedLocales.add(new Locale("de"));
        supportedLocales.add(new Locale("no", "NO"));        

        /*
         * Since we want to just test calculateLocale we need to make it so
         * that we only mock a method that will not get called by 
         * calculateLocale, that way we can avoid calling the default 
         * constructor.
         */
        MultiViewHandler viewHandler = PowerMock.createNicePartialMock(MultiViewHandler.class, new String[] { "initView" });
        
        expect(facesContext.getExternalContext()).andReturn(externalContext).anyTimes();
        expect(externalContext.getRequestHeaderMap()).andReturn(requestHeaders).anyTimes();
        expect(externalContext.getRequestLocales()).andReturn(requestLocales.iterator()).anyTimes();
        expect(facesContext.getApplication()).andReturn(application).anyTimes();
        expect(application.getSupportedLocales()).andReturn(supportedLocales.iterator()).anyTimes();
        expect(application.getDefaultLocale()).andReturn(null).anyTimes();
        
        PowerMock.replay(facesContext, application, externalContext, viewHandler);

        Locale result = viewHandler.calculateLocale(facesContext);
        
        PowerMock.verify(facesContext, application, externalContext, viewHandler);

        assertEquals("CN", result.getCountry());
        assertEquals("zh", result.getLanguage());
    }
}
