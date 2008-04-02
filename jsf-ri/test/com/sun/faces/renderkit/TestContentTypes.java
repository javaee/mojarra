/*
 * $Id: TestContentTypes.java,v 1.4 2006/05/10 01:27:02 rogerk Exp $
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

package com.sun.faces.renderkit;

import com.sun.faces.cactus.ServletFacesTestCase;

import org.apache.cactus.WebRequest;

public class TestContentTypes extends ServletFacesTestCase {

//
// Constructors and Initializers
//
                                                                                                                   
    public TestContentTypes() {
        super("TestContentTypes");
    }
                                                                                                                   
    public TestContentTypes(String name) {
        super(name);
    }

    /**
     * quality test - "text/html" wins
     */
    public void beginAccept1(WebRequest theRequest) {
        theRequest.addHeader("Accept", "text/plain; q=0.5, text/html, text/x-dvi; q=0.8, text/x-c");
    }
    public void testAccept1() throws Exception {
        String clientContentType = getFacesContext().getExternalContext().getRequestHeaderMap().get("Accept");
        String serverSupportedContentTypes = "text/html, text/plain";
        String contentType = RenderKitUtils.determineContentType(
            clientContentType, serverSupportedContentTypes, null);
        assertEquals(contentType, "text/html");
    }

    /**
     * quality test - "text/x-dvi" wins
     */
    public void beginAccept2(WebRequest theRequest) {
        theRequest.addHeader("Accept", "text/plain; q=0.5, text/html, text/x-dvi; q=0.8, text/x-c");
    }
    public void testAccept2() throws Exception {
        String clientContentType = getFacesContext().getExternalContext().getRequestHeaderMap().get("Accept");
        String serverSupportedContentTypes = "text/x-dvi, text/plain";
        String contentType = RenderKitUtils.determineContentType(
            clientContentType, serverSupportedContentTypes, null);
        assertEquals(contentType, "text/x-dvi");
    }

    /**
     * "level" precedence test - "text/html;level=1" is higher than "text/html" 
     */
    public void beginAccept3(WebRequest theRequest) {
        theRequest.addHeader("Accept", "text/plain; q=0.5, text/html, text/html;level=1");
    }
    public void testAccept3() throws Exception {
        String clientContentType = getFacesContext().getExternalContext().getRequestHeaderMap().get("Accept");
        String serverSupportedContentTypes = "text/html, text/html;level=1";
        String contentType = RenderKitUtils.determineContentType(
            clientContentType, serverSupportedContentTypes, null);
        assertEquals(contentType, "text/html;level=1");
    }
 
    /**
     * "level" precedence test - "text/html;level=2" is higher than "text/html;level=1"" 
     */
    public void beginAccept4(WebRequest theRequest) {
        theRequest.addHeader("Accept", "text/plain; q=0.5, text/html, text/html;level=1, text/html;level=2");
    }
                                                                                                                           
    public void testAccept4() throws Exception {
        String clientContentType = getFacesContext().getExternalContext().getRequestHeaderMap().get("Accept");
        String serverSupportedContentTypes = "text/html, text/html;level=1, text/html;level=2";
        String contentType = RenderKitUtils.determineContentType(
            clientContentType, serverSupportedContentTypes, null);
        assertEquals(contentType, "text/html;level=2");
    }

    public void beginAccept5(WebRequest theRequest) {
        theRequest.addHeader("Accept", "text/html, application/xhtml+xml");
    }

    public void testAccept5() throws Exception {
        String clientContentType = getFacesContext().getExternalContext().getRequestHeaderMap().get("Accept");
        String serverSupportedContentTypes = "text/html, application/xhtml+xml";
        String contentType = RenderKitUtils.determineContentType(
            clientContentType, serverSupportedContentTypes, "application/xhtml+xml");
        assertEquals(contentType, "application/xhtml+xml");
    }

    public void beginAccept6(WebRequest theRequest) {
        theRequest.addHeader("Accept", "text/html, application/xhtml+xml; q=0.5");
    }

    public void testAccept6() throws Exception {
        String clientContentType = getFacesContext().getExternalContext().getRequestHeaderMap().get("Accept");
        String serverSupportedContentTypes = "text/html, application/xhtml+xml";
        String contentType = RenderKitUtils.determineContentType(
            clientContentType, serverSupportedContentTypes, "application/xhtml+xml");
        assertEquals(contentType, "text/html");
    }
}
