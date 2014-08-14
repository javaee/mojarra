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

package com.sun.faces.test.agnostic.application.localeConfig;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.*;

@ManagedBean
@SessionScoped
public class ApplicationConfigBean {

    private String title = "Test Application Config";
    public String getTitle() {
        return title; 
    }

    public ApplicationConfigBean() {

    }

    public String getLocaleConfigPositive() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();

        Locale locale = app.getDefaultLocale();
        assertNotNull("Can't get default locale from Application", locale);
        assertEquals(Locale.US, locale);

        Iterator iter;
        int j = 0, len = 0;
        boolean found = false;
        String[][] expected = {
            {"de", "DE"},
            {"en", "US"},
            {"fr", "FR"},
            {"ps", "PS"}
        };
        len = expected.length;

        // test that the supported locales are a superset of the
        // expected locales
        for (j = 0; j < len; j++) {
            assertNotNull("Can't get supportedLocales from Application",
                          iter = app.getSupportedLocales());
            found = false;
            while (iter.hasNext()) {
                locale = (Locale) iter.next();
                if (expected[j][0].equals(locale.getLanguage()) &&
                    expected[j][1].equals(locale.getCountry())) {
                    found = true;
                }
            }
            assertTrue("Can't find expected locale " + expected[j][0] + "_" +
                       expected[j][1] + " in supported-locales list",
                       found);
        }

        return "SUCCESS";
    }



    private String status="";

    public String getStatus() {
        return status;
    }

}

