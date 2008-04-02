/*
 * $Id: PrependIdTestCase.java,v 1.5 2006/03/29 23:03:56 rlubke Exp $
 */

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
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.jsptest;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.faces.component.NamingContainer;


/**
 * <p>Verify that we have an intelligent error message when the user
 * forgets the view tag.</p>
 */

public class PrependIdTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public PrependIdTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


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
        return (new TestSuite(PrependIdTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------ Instance Variables



    // ------------------------------------------------- Individual Test Methods

    public void testMissingView() throws Exception {
	client.setThrowExceptionOnFailingStatusCode(false);
	HtmlPage page = getPage("/faces/jsp/prependId.jsp");
        String pageText = page.asXml();
        // Literal ids with prependId literal
        assertTrue(-1 != pageText.indexOf("span id=\"case1prependIdFalse\""));
        assertTrue(-1 != pageText.indexOf("span id=\"form2:case1prependIdTrue\""));
        assertTrue(-1 != pageText.indexOf("span id=\"form3:case1prependIdUnspecified\""));
	
        // Literal ids with prependId from expression
        assertTrue(-1 != pageText.indexOf("span id=\"case2prependIdFalse\""));
        assertTrue(-1 != pageText.indexOf("span id=\"form5:case2prependIdTrue\""));
        assertTrue(-1 != pageText.indexOf("span id=\"form6:case2prependIdUnspecified\""));
        
        // Auto-generated ids with prependId literal
	assertTrue(-1 != pageText.indexOf("input value=\"prependIdFalse\" type=\"text\" name=\"j_id_id54\""));
        assertTrue(-1 != pageText.indexOf("input value=\"prependIdTrue\" type=\"text\" name=\"j_id_id57:j_id_id59\""));
        assertTrue(-1 != pageText.indexOf("input value=\"prependIdUnspecified\" type=\"text\" name=\"j_id_id62:j_id_id64\""));
        
        // Auto-generated ids with prependId from expression
        assertTrue(-1 != pageText.indexOf("input value=\"prependIdFalse\" type=\"text\" name=\"j_id_id71\""));
        assertTrue(-1 != pageText.indexOf("input value=\"prependIdTrue\" type=\"text\" name=\"j_id_id74:j_id_id76\""));
        assertTrue(-1 != pageText.indexOf("input value=\"prependIdUnspecified\" type=\"text\" name=\"j_id_id79:j_id_id81\""));
       
    }

}
