/*
 * $Id: TestContentTypes.java,v 1.5 2007/04/27 22:02:09 ofung Exp $
 */
                                                                                                                   
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
