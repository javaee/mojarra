/*
 * $Id: FacesValidator.java,v 1.3 2003/03/13 23:01:30 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib;

import java.io.IOException;
import javax.servlet.jsp.tagext.PageData;
import javax.servlet.jsp.tagext.TagLibraryValidator;
import javax.servlet.jsp.tagext.ValidationMessage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.mozilla.util.Assert;

/**
 * <p>Base class for all faces TLVs</p>
 *
 * @author Justyna Horwat
 * @author Ed Burns
 */
public abstract class FacesValidator extends TagLibraryValidator {

    //*********************************************************************
    // Constants

    //*********************************************************************
    // Validation and configuration state (protected)

    protected boolean failed;			// did the page fail?


    //*********************************************************************
    // Constants

    final String JSF_CORE_URI = "http://java.sun.com/jsf/core";

    final String JSF_HTML_URI = "http://java.sun.com/jsf/html";

    final String JSTL_CORE_URI = "http://java.sun.com/jstl/core";
    // Prefix for JSF HTML tags 
    protected String JSF_HTML_PRE = null;
    public String getJSF_HTML_PRE() { return JSF_HTML_PRE; }

    // Prefix for JSF CORE tags 
    protected String JSF_CORE_PRE = null;
    public String getJSF_CORE_PRE() { return JSF_CORE_PRE; }

    // Prefix for JSTL CORE tags 
    protected String JSTL_CORE_PRE = null;
    public String getJSTL_CORE_PRE() { return JSTL_CORE_PRE; }

    // Separator character
    protected final char SPACE = ' ';

    // QName for JSTL conditional tag
    protected String JSTL_IF_QN = ":if";
    public String getJSTL_IF_QN() { return JSTL_IF_QN; }

    // QName for JSTL conditional tag
    protected String JSTL_CHOOSE_QN = ":choose";
    public String getJSTL_CHOOSE_QN() { return JSTL_CHOOSE_QN; }

    // QName for JSTL iterator tag
    protected String JSTL_FOREACH_QN = ":forEach";
    public String getJSTL_FOREACH_QN() { return JSTL_FOREACH_QN; }

    // QName for JSTL iterator tag
    protected String JSTL_FORTOKENS_QN = ":forTokens";
    public String getJSTL_FORTOKENS_QN() { return JSTL_FORTOKENS_QN; }



    //*********************************************************************
    // Constructor and lifecycle management

    public FacesValidator() {
        super();
        init();
    }

    protected void init() {
        failed = false;
    }

    public void release() {
	super.release();
        init();
    }

    protected abstract DefaultHandler getSAXHandler();
    protected abstract String getFailureMessage(String prefix, String uri);

    //*********************************************************************
    // Validation entry point

    /**
     * Validate a JSP page. Return an an array of Validation 
     * Messages if a validation failure occurs. Return null 
     * on success.
     *
     * @param prefix Value of directive prefix argument.
     * @param uri Value of directive uri argument.
     * @param page JspData page object.
     *
     * @returns ValidationMessage[] An array of Validation messages.
     */
    public synchronized ValidationMessage[] validate(
            String prefix, String uri, PageData page) {
	ValidationMessage[] result = null;
	try {

            // get a handler
            DefaultHandler h = getSAXHandler();

	    // parse the page
	    SAXParserFactory f = SAXParserFactory.newInstance();
	    f.setValidating(true);
	    SAXParser p = f.newSAXParser();
	    p.parse(page.getInputStream(), h);

            //on validation failure generate error message
	    if (failed) {
 	        result = vmFromString(getFailureMessage(prefix, uri));
            }
            else {
                //success
                result = null;
            }

	} catch (SAXException ex) {
            result = vmFromString(ex.toString());
	} catch (ParserConfigurationException ex) {
            result = vmFromString(ex.toString());
	} catch (IOException ex) {
            result = vmFromString(ex.toString());
	}
	// Make sure all resources are released
	this.release();
	return result;
    }


    //*********************************************************************
    // Utility functions

    /**
     * Construct a ValidationMessage[] from a single String and no ID.
     *
     * @param message Message string.
     *
     * @returns ValidationMessage[] An array of Validation Messages.
     */
    private ValidationMessage[] vmFromString(String message) {
	return new ValidationMessage[] {
            new ValidationMessage(null, message)
	};
    }

    protected void debugPrintTagData(String ns, String ln, String qn, 
				     Attributes attrs) {
	int 
	    i = 0, 
	    len = attrs.getLength();
	System.out.println("nameSpace: " + ns + " localName: " + ln + 
			   " QName: " + qn);
	for (i = 0; i < len; i++) {
	    System.out.println("\tlocalName: " + attrs.getLocalName(i));
	    System.out.println("\tQName: " + attrs.getQName(i));
	    System.out.println("\tvalue: " + attrs.getValue(i) + "\n");
	}
    }


    /**

    * This method provides for the ability of the TLV to use whatever
    * user defined tag lib prefix is in the page to recognize tags.

    */

    protected void maybeSnagTLPrefixes(String qName, Attributes attrs) {

	// for testing purposes
	if (Assert.enabled) {
	    String thisName;
	    // Only need one TLV instance
	    if (null == System.getProperty(thisName = 
					   this.getClass().getName())) {
		// Save this so the test can access it.
		System.getProperties().put(thisName, this);
	    }
	}
		
	if (!qName.equals("jsp:root")) {
	    return;
	}
	int 
	    colon,
	    i = 0, 
	    len = attrs.getLength();
	String 
	    prefix = null,
	    value = null;
	for (i = 0; i < len; i++) {
	    if (null != (value = attrs.getValue(i)) &&
		null != (qName = attrs.getQName(i))) {
		if (qName.startsWith("xmlns:") && 7 <= qName.length()) {
		    prefix = qName.substring(6);
		    if (value.equals(JSF_CORE_URI)) {
			JSF_CORE_PRE = prefix;
		    }
		    else if (value.equals(JSF_HTML_URI)) {
			JSF_HTML_PRE = prefix;
		    }
		    else if (value.equals(JSTL_CORE_URI)) {
			JSTL_CORE_PRE = prefix;
			JSTL_IF_QN = JSTL_CORE_PRE + JSTL_IF_QN;
			JSTL_CHOOSE_QN = JSTL_CORE_PRE + JSTL_CHOOSE_QN;
			JSTL_FOREACH_QN = JSTL_CORE_PRE + JSTL_FOREACH_QN;
			JSTL_FORTOKENS_QN = JSTL_CORE_PRE + JSTL_FORTOKENS_QN;
		    }
		}
	    }
	}
    }    

}
