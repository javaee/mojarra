/*
 * $Id: CoreValidator.java,v 1.3 2003/03/13 23:01:32 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

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

import com.sun.faces.taglib.FacesValidator;

/**
 * <p>A TagLibrary Validator class to allow a TLD to mandate that
 * JSF tag must have an id if it is a child or sibling of a JSTL
 * conditional or iteration tag</p>
 *
 * @author Justyna Horwat
 */
public class CoreValidator extends FacesValidator {

    //*********************************************************************
    // Constants

    //*********************************************************************
    // Validation and configuration state (protected)

    private boolean siblingSatisfied;		// is there a JSF sibling?
    private int requiresIdCount;		// nested count
    private StringBuffer requiresIdList;	// list of failing tags

    //*********************************************************************
    // Constructor and lifecycle management

    public CoreValidator() {
        super();
        init();
    }

    protected void init() {
	super.init();
        failed = false;
        siblingSatisfied = true;
        requiresIdCount = 0;
        requiresIdList = new StringBuffer();
    }

    public void release() {
	super.release();
        init();
    }

    //
    // Superclass overrides.
    // 

    protected DefaultHandler getSAXHandler() {
	DefaultHandler h = new CoreValidatorHandler();
	return h;
    }

    protected String getFailureMessage(String prefix, String uri) {
	String result = 
	    "The following JSF tags are required to contain IDs: '" + 
	    requiresIdList.toString() +
	    "' according to the TLV in taglib prefix: '" + 
	    prefix + ":' with URI: (" + uri + ")";
	return result;
    }


    //*********************************************************************
    // SAX handler

    /**
     * The handler that provides the base of the TLV implementation. 
     */
    private class CoreValidatorHandler extends DefaultHandler {

        /**
         * Parse the starting element. If it is a specific JSTL tag
         * make sure that the nested JSF tags have IDs. 
         *
         * @param ns Element name space.
         * @param ln Element local name.
         * @param qn Element QName.
         * @param a Element's Attribute list.
         *
         */
	public void startElement(
                String ns, String ln, String qn, Attributes a) {
	    maybeSnagTLPrefixes(qn, a);

            if (isJstlTag(qn)) {
                requiresIdCount++;
            }
            else if ( qn.startsWith(JSF_HTML_PRE) &&
                       (requiresIdCount > 0) ) {
                //make sure that id is present in attributes
                if (!hasIdAttribute(a)) {
                    //add to list of jsf tags for error report
                    failed = true;
                    requiresIdList.append(qn).append(SPACE);
                }
            }
            else if ((requiresIdCount == 0) && (!siblingSatisfied)) {
                //make sure jsf sibling has an id
                if ( (qn.startsWith(JSF_HTML_PRE) ||
                      qn.startsWith(JSF_CORE_PRE)) &&
                      (!hasIdAttribute(a)) ) {
                    //add to list of jsf tags for error report
                    failed = true;
                    requiresIdList.append(qn).append(SPACE);
                }
                siblingSatisfied = true;
            }
            else if (requiresIdCount == 0) {
                // sibling is a non-JSF tag
                // JSF tags no longer need id's
                siblingSatisfied = true;
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

            if (isJstlTag(qn)) {
                requiresIdCount--;
                siblingSatisfied = false;
            }
        }

        /**
         * Check element to make sure that the id attribute is
         * present.
         *
         * @param a Attribute list
         *
         * @return boolean True if id attribute found."id"
         */
        private boolean hasIdAttribute(Attributes a) {
	    for (int i = 0; i < a.getLength(); i++) {
                if (a.getQName(i).equals("id")) {
                    return true;
                }
	    }
            return false;
        }

        /**
         * Check to make sure that the element is either a 
         * conditional or iterator JSTL tag.
         *
         * @param qn Element to be checked.
         *
         * @return boolean True if JSTL tag is iterator or conditional
         */
        private boolean isJstlTag(String qn) {
            if (qn.equals(JSTL_IF_QN) || 
                qn.equals(JSTL_CHOOSE_QN) ||
                qn.equals(JSTL_FOREACH_QN) ||
                qn.equals(JSTL_FORTOKENS_QN)) {
               return true;
            }
            return false;
        }
    }
}
