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

package com.sun.faces.composite;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Unit tests for Composite Components.
 */
public class CompositeComponentsTestCaseWithEE6Dependencies extends AbstractTestCase {


    @SuppressWarnings({"UnusedDeclaration"})
    public CompositeComponentsTestCaseWithEE6Dependencies() {
        this("CompositeComponentsTestCaseWithEE6Dependencies");
    }

    public CompositeComponentsTestCaseWithEE6Dependencies(String name) {
        super(name);
    }


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        super.setUp();
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(CompositeComponentsTestCaseWithEE6Dependencies.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }
    

    // -------------------------------------------------------------- Test Cases

    /**
     * Added for issue 1318.
     */
    public void testIssue1318() throws Exception {

        HtmlPage page = getPage("/faces/composite/issue1318.xhtml");
        HtmlSubmitInput button = (HtmlSubmitInput) getInputContainingGivenId(page, "form:arg:n1:n2:command");
        assertNotNull(button);
        page = button.click();
        String message = "Action invoked: form:arg:n1:n2:command, arg1: Hello, arg2: World!";
        assertTrue(page.asText().contains(message));
        
    }

    public void testCompositeComponentActionWithArgs() throws Exception {

        HtmlPage page = getPage("/faces/composite/compActionWithArgs.xhtml");
        HtmlSubmitInput button = (HtmlSubmitInput) getInputContainingGivenId(page, "n:form:command");
        assertNotNull(button);
        page = button.click();
        String message = "Custom action invoked: c:n:form:command, arg1: arg1, arg2: arg2";
        assertTrue(page.asText().contains(message));

    }


    public void testCompositeComponentAttributeWithArgs() throws Exception {

        HtmlPage page = getPage("/faces/composite/compAttributeWithArgs.xhtml");
        String message = "arg: arg1";
        assertTrue(page.asText().contains(message));

    }

    public void testCompositeComponentAttributeRequired() throws Exception {

        HtmlPage page = getPage("/faces/composite/compAttributeRequired.xhtml");
        String message = "xx1:0xx";
        assertTrue(page.asText().contains(message));

        page = getPage("/faces/composite/compAttributeRequiredNullValue.xhtml");
        message = "xx:0xx";
        assertTrue(page.asText().contains(message));

        page = getPage("/faces/composite/compAttributeRequiredLiteral.xhtml");
        message = "xx2:0xx";
        assertTrue(page.asText().contains(message));
    }

    public void testInvalidArgsToCCExpression() throws Exception {

        client.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = getPage("/faces/composite/invalidMeArgs.xhtml");
        assertTrue(page.asText().contains("value=\"#{cc.attrs.custom(cc.attrs.arg1)}\" Illegal attempt to pass arguments to a composite component lookup expression"));

        page = getPage("/faces/composite/invalidVeArgs.xhtml");
        assertTrue(page.asText().contains("value=\"#{cc.attrs.bean(cc.attrs.arg1)}\" Illegal attempt to pass arguments to a composite component lookup expression"));
    }




}
