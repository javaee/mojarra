/*
 * $Id: HtmlBasicValidator.java,v 1.4 2003/08/19 21:40:50 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.faces.taglib.FacesValidator;
import com.sun.faces.taglib.ValidatorInfo;


import org.mozilla.util.Assert;

/**
 * <p>Top level validator for the html_basic tld</p>
 *
 * @author Justyna Horwat
 * @author Ed Burns
 */
public class HtmlBasicValidator extends FacesValidator {
    //*********************************************************************
    // Validation and configuration state (protected)
    private ValidatorInfo validatorInfo;
    private CommandTagParserImpl commandTagParser;


    //*********************************************************************
    // Constructor and lifecycle management

    public HtmlBasicValidator() {
        super();
        init();
    }

    protected void init() {
	super.init();
        failed = false;
        validatorInfo = new ValidatorInfo();

        commandTagParser = new CommandTagParserImpl();
        commandTagParser.setValidatorInfo(validatorInfo);
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

        StringBuffer result = new StringBuffer();
        if (commandTagParser.hasFailed()) {
            result.append(commandTagParser.getMessage());
        }
	return result.toString();
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
	public void startElement(String ns, 
                                 String ln, 
                                 String qn, 
                                 Attributes attrs) {
	    maybeSnagTLPrefixes(qn, attrs);
            validatorInfo.setQName(qn);
            validatorInfo.setAttributes(attrs);
  
            commandTagParser.parseStartElement();
            if (commandTagParser.hasFailed()) {
                failed = true;
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
    }
}
