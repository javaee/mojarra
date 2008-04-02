/*
 * $Id: TestHtmlUtils.java,v 1.2 2006/08/24 19:25:01 youngm Exp $
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
 * @version $Id: TestHtmlUtils.java,v 1.2 2006/08/24 19:25:01 youngm Exp $
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
		testURLEncoding("/index.jsf?joe=10&amp;fred=20", "/index.jsf?joe=10&amp%3Bfred=20", "/index.jsf?joe=10&amp;fred=20");
		//Test URL with two params and second & close to end of string
		testURLEncoding("/index.jsf?joe=10&f=20", "/index.jsf?joe=10&f=20", "/index.jsf?joe=10&amp;f=20");
		//Test URL with misplaced & expected behavior but not necissarily right.
		testURLEncoding("/index.jsf?joe=10&f=20&", "/index.jsf?joe=10&f=20&", "/index.jsf?joe=10&amp;f=20&amp;");
		//Test URL with encoded entity at end of URL expected behavior but not necissarily right.
		testURLEncoding("/index.jsf?joe=10&f=20&amp;", "/index.jsf?joe=10&f=20&amp%3B", "/index.jsf?joe=10&amp;f=20&amp;");
	}
	
	private void testURLEncoding(String urlToEncode, String expectedHTML, String expectedXML) throws UnsupportedEncodingException, IOException {
		char[] buffer = new char[1024];
		StringWriter xmlWriter = new StringWriter();
		HtmlUtils.writeURL(xmlWriter, urlToEncode, "UTF-8", RIConstants.XHTML_CONTENT_TYPE);
//		System.out.println(xmlWriter.toString());
		assertEquals(xmlWriter.toString(), expectedXML);
		StringWriter htmlWriter = new StringWriter();
		HtmlUtils.writeURL(htmlWriter, urlToEncode, "UTF-8", RIConstants.HTML_CONTENT_TYPE);
//		System.out.println(htmlWriter.toString());
		assertEquals(htmlWriter.toString(), expectedHTML);
	}

}
