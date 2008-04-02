/*
 * $Id: FacesValidator.java,v 1.2 2003/02/03 23:04:33 edburns Exp $
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

}
