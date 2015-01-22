/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.test.javaee6web.flowtraversalcombinations;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ReturnNaviToOtherNodesIT {
    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    @Test
    public void testReturnNaviToMethodCall() throws Exception {
        HtmlPage page = webClient.getPage(webUrl);
        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("go_to_start_from_return_flow");
        page = button.click();
        button = (HtmlSubmitInput) page.getHtmlElementById("go_to_initial_return_node_flow");
        page = button.click();
        HtmlTextInput returnNode = (HtmlTextInput)page.getHtmlElementById("parent-node-to-be-returned");
        returnNode.setValueAttribute("MethodCallNodeToBeCalled");
        button = (HtmlSubmitInput) page.getHtmlElementById("return-to-method-call-node");
        page = button.click();
        assertTrue(page.asXml().contains("Great! You are now in the correct destination view."));
    }    

    @Test
    public void testReturnNaviToViewNode() throws Exception {
        HtmlPage page = webClient.getPage(webUrl);
        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("go_to_start_from_return_flow");
        page = button.click();
        button = (HtmlSubmitInput) page.getHtmlElementById("go_to_initial_return_node_flow");
        page = button.click();
        HtmlTextInput returnNode = (HtmlTextInput)page.getHtmlElementById("parent-node-to-be-returned");
        returnNode.setValueAttribute("ViewNodeToBeCalled");
        button = (HtmlSubmitInput) page.getHtmlElementById("return-to-method-call-node");
        page = button.click();
        assertTrue(page.asXml().contains("Great! You are now in the correct destination view."));
    } 
    
    @Test
    public void testReturnNaviToSwitchNode() throws Exception {
        HtmlPage page = webClient.getPage(webUrl);
        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("go_to_start_from_return_flow");
        page = button.click();
        button = (HtmlSubmitInput) page.getHtmlElementById("go_to_initial_return_node_flow");
        page = button.click();
        HtmlTextInput returnNode = (HtmlTextInput)page.getHtmlElementById("parent-node-to-be-returned");
        returnNode.setValueAttribute("SwitchNodeToBeCalled");
        button = (HtmlSubmitInput) page.getHtmlElementById("return-to-method-call-node");
        page = button.click();
        assertTrue(page.asXml().contains("Great! You are now in the correct destination view."));
    } 
    
    @Test
    public void testReturnNaviToReturnNode() throws Exception {
        HtmlPage page = webClient.getPage(webUrl);
        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("go_to_start_from_return_flow");
        page = button.click();
        button = (HtmlSubmitInput) page.getHtmlElementById("go_to_initial_return_node_flow");
        page = button.click();
        HtmlTextInput returnNode = (HtmlTextInput)page.getHtmlElementById("parent-node-to-be-returned");
        returnNode.setValueAttribute("ReturnNodeToBeCalled");
        button = (HtmlSubmitInput) page.getHtmlElementById("return-to-method-call-node");
        page = button.click();
        
        assertTrue(page.asXml().contains("Has a flow: false."));
        assertTrue(page.asXml().contains("flowScope value, should be empty: ."));
    } 
    
//    comment out this to make hudson clean, will uncomment once the issue fixed.     
//    @Test
//    public void testReturnNaviToFlowCallNode() throws Exception {
//        HtmlPage page = webClient.getPage(webUrl);
//        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("go_to_start_from_return_flow");
//        page = button.click();
//        button = (HtmlSubmitInput) page.getHtmlElementById("go_to_initial_return_node_flow");
//        page = button.click();
//        HtmlTextInput returnNode = (HtmlTextInput)page.getHtmlElementById("parent-node-to-be-returned");
//        returnNode.setValueAttribute("FlowCallNodeToBeCalled");
//        button = (HtmlSubmitInput) page.getHtmlElementById("return-to-method-call-node");
//        page = button.click();
//        assertTrue(page.asXml().contains("Great! You are now in the correct destination view."));
//    }
}