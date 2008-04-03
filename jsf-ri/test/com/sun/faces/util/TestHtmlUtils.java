/*
 * $Id: TestHtmlUtils.java,v 1.4 2007/04/27 22:02:11 ofung Exp $
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

// TestHtmlUtils.java
package com.sun.faces.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import com.sun.faces.RIConstants;

import junit.framework.TestCase;

/**
 * <B>TestHtmlUtils</B> is a class ...
 *
 * @version $Id: TestHtmlUtils.java,v 1.4 2007/04/27 22:02:11 ofung Exp $
 */
public class TestHtmlUtils extends TestCase {
	
	public void testWriteURL() throws IOException {
		//Test url with no params
		testURLEncoding("http://www.google.com", "http://www.google.com", "http://www.google.com");
		//Test URL with one param
		testURLEncoding("http://www.google.com?joe=10", "http://www.google.com?joe=10", "http://www.google.com?joe=10");
		//Test URL with two params
		testURLEncoding("http://www.google.com?joe=10&fred=20", "http://www.google.com?joe=10&fred=20", "http://www.google.com?joe=10&amp;fred=20");
		//Test URL with & entity encoded
		testURLEncoding("/index.jsf?joe=10&amp;fred=20", "/index.jsf?joe=10&amp;fred=20", "/index.jsf?joe=10&amp;fred=20");
		//Test URL with two params and second & close to end of string
		testURLEncoding("/index.jsf?joe=10&f=20", "/index.jsf?joe=10&f=20", "/index.jsf?joe=10&amp;f=20");
		//Test URL with misplaced & expected behavior but not necissarily right.
		testURLEncoding("/index.jsf?joe=10&f=20&", "/index.jsf?joe=10&f=20&", "/index.jsf?joe=10&amp;f=20&amp;");
		//Test URL with encoded entity at end of URL expected behavior but not necissarily right.
		testURLEncoding("/index.jsf?joe=10&f=20&amp;", "/index.jsf?joe=10&f=20&amp;", "/index.jsf?joe=10&amp;f=20&amp;");
	}
	
	private void testURLEncoding(String urlToEncode, String expectedHTML, String expectedXML) throws UnsupportedEncodingException, IOException {
		char[] buffer = new char[1024];
		StringWriter xmlWriter = new StringWriter();
		HtmlUtils.writeURL(xmlWriter, urlToEncode, "UTF-8", RIConstants.XHTML_CONTENT_TYPE);
      System.out.println("XML: " + xmlWriter.toString());
		assertEquals(xmlWriter.toString(), expectedXML);
		StringWriter htmlWriter = new StringWriter();
		HtmlUtils.writeURL(htmlWriter, urlToEncode, "UTF-8", RIConstants.HTML_CONTENT_TYPE);
      System.out.println("HTML: " + htmlWriter.toString());
		assertEquals(htmlWriter.toString(), expectedHTML);
	}

}
