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
package com.sun.faces.test.htmlunit;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;

public class DebugHelper extends WebConnectionWrapper {

    private final String urlFragmentToMonitor;
    private String rawRequestBody;
    private String rawResponse;
    private WebClient webClient;
    

    public DebugHelper(WebClient webClient, String urlFragmentToMonitor) {
        super(webClient);
        this.urlFragmentToMonitor = urlFragmentToMonitor;
        this.webClient = webClient;
    }

    @Override
    public WebResponse getResponse(WebRequest request) throws IOException {

        if (urlFragmentToMonitor == null || request.getUrl().toString().contains(urlFragmentToMonitor)) {
            rawRequestBody = request.getRequestBody();

            WebResponse response = super.getResponse(request);

            rawResponse = response.getContentAsString();
            
            return response;
        }

        return super.getResponse(request);
    }
    
    public void sleep() {
        try {
            Thread.sleep(4000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void print(HtmlPage page, String description) {
        System.out.println("\n\n\n ************* " + description + " ********************");
        print(page);
    }
    
    public void print(HtmlPage page) {
        System.out.println("\n\n\n RAW REQUEST BODY \n" + getRawRequestBody());
        System.out.println("\n\n\n RAW RESPONSE \n" + getRawResponse());
        
        System.out.println("Response Headers: " + page.getWebResponse().getResponseHeaders());
        
        System.out.println("\n\n\n ********************************* \n" + page.asXml());
        
        System.out.println("\n\n\n **************************** \n" + webClient.getCookieManager().getCookies());
        
        System.out.println("\n\n\n *********************************");
    }
    
    public String getRawRequestBody() {
        return rawRequestBody;
    }

    public String getRawResponse() {
        return rawResponse;
    }
   
}