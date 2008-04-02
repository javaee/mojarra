/*
 * $Id: HtmlBasicValidator.java,v 1.1 2003/02/03 23:04:33 edburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.faces.taglib.FacesValidator;

import org.mozilla.util.Assert;

/**
 * <p>Top level validator for the html_basic tld</p>
 *
 * @author Justyna Horwat
 * @author Ed Burns
 */
public class HtmlBasicValidator extends FacesValidator {

    //*********************************************************************
    // Constants

    // Prefix for JSF HTML tags 
    private final String JSF_HTML_PRE = "h:";

    // Prefix for JSF CORE tags 
    private final String JSF_CORE_PRE = "f:";

    // Separator character
    private final char SPACE = ' ';

    //*********************************************************************
    // Validation and configuration state (protected)

    // PENDING(edburns): Make this localizable
    private StringBuffer failureMessages;	// failureMessages

    //*********************************************************************
    // Constructor and lifecycle management

    public HtmlBasicValidator() {
        super();
        init();
    }

    protected void init() {
	super.init();
	failureMessages = new StringBuffer();
    }

    public void release() {
	super.release();
        init();
    }

    protected DefaultHandler getSAXHandler() {
	DefaultHandler h = new HtmlBasicValidatorHandler();
	return h;
    }

    protected String getFailureMessage(String prefix, String uri) {
	// we should only get called if this Validator failed
	Assert.assert_it(failed);	
	String result = failureMessages.toString();
	return result;
    }
	    
    //*********************************************************************
    // SAX handler

    /**
     * The handler that provides the base of the TLV implementation. 
     */
    private class HtmlBasicValidatorHandler extends DefaultHandler {

        /**
         * Parse the starting element.  Parcel out to appropriate
         * handler method.
         *
         * @param ns Element name space.
         * @param ln Element local name.
         * @param qn Element QName.
         * @param a Element's Attribute list.
         *
         */
	public void startElement(
                String ns, String ln, String qn, Attributes a) {
	    if (-1 != (qn.indexOf("command_button"))) {
		handleCommandButton(ns, ln, qn, a);
	    }
        }

        /**
         * Parse the ending element. If it is a specific JSTL tag
         * make sure that the nested count is decreased.
         *
         * @param ln Element local name.
         * @param qn Element QName.
         * @param a Element's Attribute list.
         *
         */
	public void endElement(String ns, String ln, String qn) {
	}

	/*

	* <p>set failed to true unless tag has a key, image, or label
	* attribute</p>.

	* <p>PRECONDITION: qn is a command_button</p>

	*/

	protected void handleCommandButton(String ns, String ln, 
					   String qn, Attributes a) {
	    boolean 
		hasKey = false,
		hasImage = false,
		hasLabel = false;
	    for (int i = 0; i < a.getLength(); i++) {
                if (a.getQName(i).equals("key")) {
		    hasKey = true;
                }
                if (a.getQName(i).equals("image")) {
		    hasImage = true;
                }
                if (a.getQName(i).equals("label")) {
		    hasLabel = true;
                }
	    }
	    if (failed = ((hasKey || hasImage || hasLabel) != true)) {
		failureMessages.append(qn + " must have either key or label attributes\n");
	    }
	    
	}
 
    }
}
