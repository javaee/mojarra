/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.junit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * The JSF JUnit 4 Test runner.
 */
public class JsfTestRunner extends BlockJUnit4ClassRunner {

    /**
     * Stores the boolean flag indicating we skip.
     */
    private boolean skip = false;

    /**
     * Constructor.
     *
     * @param clazz the class.
     * @throws InitializationError when initialization fails.
     */
    public JsfTestRunner(Class<?> clazz) throws InitializationError {
        super(clazz);

        if (clazz.getAnnotation(JsfTest.class) != null) {
            JsfTest jsfTest = clazz.getAnnotation(JsfTest.class);

            if (System.getProperty("jsf.version") != null) {
                try {
                    JsfVersion serverVersion = JsfVersion.fromString(System.getProperty("jsf.version"));

                    if (serverVersion.ordinal() < jsfTest.value().ordinal()) {
                        this.skip = true;
                    }
                } catch (IllegalArgumentException exception) {
                    /*
                     * We could not match up the version, so we are going to 
                     * assume you still want to run the tests.
                     */
                }
            }
        }
    }

    /**
     * Compute the test methods.
     *
     * @return the test methods.
     */
    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> result = new ArrayList<FrameworkMethod>();
        if (!skip) {
            Iterator<FrameworkMethod> methods = super.computeTestMethods().iterator();

            while (methods.hasNext()) {
                FrameworkMethod method = methods.next();

                if (method.getAnnotation(JsfTest.class) != null) {
                    JsfTest jsfTest = method.getAnnotation(JsfTest.class);
                    boolean excludeFlag = false;

                    if (jsfTest.excludes().length > 0) {
                        JsfServerExclude exclude = JsfServerExclude.fromString(System.getProperty("jsf.serverString"));

                        if (exclude != null) {
                            for (JsfServerExclude current : jsfTest.excludes()) {
                                if (current.equals(exclude)) {
                                    excludeFlag = true;
                                }
                            }
                        }
                    }

                    if (!excludeFlag && System.getProperty("jsf.version") != null) {
                        try {
                            JsfVersion serverVersion = JsfVersion.fromString(System.getProperty("jsf.version"));

                            if (serverVersion.ordinal() < jsfTest.value().ordinal()) {
                            } else {
                                result.add(method);
                            }
                        } catch (IllegalArgumentException exception) {
                            /*
                             * We could not match up the version, so we are going to
                             * assume you still want to run the tests.
                             */
                            result.add(method);
                        }
                    }
                } else {
                    result.add(method);
                }
            }
        }
        return result;
    }

    /*
     * Allow for no (active) test methods on the test class.
     */
    @Override
    @SuppressWarnings({"deprecation"})
    protected void validateInstanceMethods(List<Throwable> errors) {
        validatePublicVoidNoArgMethods(After.class, false, errors);
        validatePublicVoidNoArgMethods(Before.class, false, errors);
        validateTestMethods(errors);
    }
}
