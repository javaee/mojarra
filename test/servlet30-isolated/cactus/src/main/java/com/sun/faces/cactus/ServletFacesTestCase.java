/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id: ServletFacesTestCase.java,v 1.1 2005/10/18 16:41:33 edburns Exp $
 */

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

// ServletFacesTestCase.java

package com.sun.faces.cactus;

import org.apache.cactus.ServletTestCase;

import javax.faces.context.FacesContext;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import java.util.Iterator;

/**
 * <B>ServletFacesTestCase</B> is a base class that leverages
 * FacesTestCaseService to add Faces specific behavior to that provided
 * by cactus.  This class just delegates all method calls to
 * facesService.
 *
 * @version $Id: ServletFacesTestCase.java,v 1.1 2005/10/18 16:41:33 edburns Exp $
 * @see	#facesService
 */

public abstract class ServletFacesTestCase extends ServletTestCase
    implements FacesTestCase {

//
// Protected Constants
//

    public static final String ENTER_CALLED = FacesTestCaseService.ENTER_CALLED;
    public static final String EXIT_CALLED = FacesTestCaseService.EXIT_CALLED;
    public static final String EMPTY = FacesTestCaseService.EMPTY;


//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

/*

* This is the thing you use to get the facesContext and other
* Faces Objects.

*/

    protected FacesTestCaseService facesService = null;

//
// Constructors and Initializers    
//

    public ServletFacesTestCase() {
        super("ServletFacesTestCase");
        init();
    }


    public ServletFacesTestCase(String name) {
        super(name);
        init();
    }


    protected void init() {
        facesService = new FacesTestCaseService(this);
    }

//
// Class methods
//

//
// Methods from FacesTestCase
//

    public ServletConfig getConfig() {
        return config;
    }


    public HttpServletRequest getRequest() {
        return request;
    }


    public HttpServletResponse getResponse() {
        return response;
    }


    public PageContext getPageContext() {
        return null;
    }


    public boolean sendResponseToFile() {
        return false;
    }


    public String getExpectedOutputFilename() {
        return null;
    }


    public String[] getLinesToIgnore() {
        return null;
    }


    public boolean sendWriterToFile() {
        return false;
    }

//
// General Methods
//


    public void setUp() {
        facesService.setUp();
    }


    public void tearDown() {
        facesService.tearDown();
    }


    public FacesContext getFacesContext() {
        return facesService.getFacesContext();
    }


    public boolean verifyExpectedOutput() {
        return facesService.verifyExpectedOutput();
    }

    public void verifyEqualsContractPositive(Object x, Object y, Object z) {
        facesService.verifyEqualsContractPositive(x, y, z);
    }

    public boolean isMember(String toTest, String[] set) {
        return facesService.isMember(toTest, set);
    }


    public boolean isSubset(String[] subset, Iterator superset) {
        return facesService.isSubset(subset, superset);
    }


    public boolean requestsHaveSameAttributeSet(HttpServletRequest request1,
                                                HttpServletRequest request2) {
        return facesService.requestsHaveSameAttributeSet(request1, request2);
    }


    public void loadFromInitParam(String paramValue) {
        facesService.loadFromInitParam(paramValue);
    }
    
    private String testRootDir = null;
    public String getTestRootDir() {
        return testRootDir;
    }
    
    public void setTestRootDir(String rootDir) {
        this.testRootDir = rootDir;
    }


} // end of class ServletFacesTestCase
