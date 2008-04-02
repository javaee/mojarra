/*
 * $Id: CoreValidator.java,v 1.13 2004/11/30 21:36:56 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.taglib.FacesValidator;
import com.sun.faces.taglib.ValidatorInfo;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

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

    private ValidatorInfo validatorInfo;
    private IdTagParserImpl idTagParser;

    //*********************************************************************
    // Constructor and lifecycle management

    /**
     * <p>CoreValidator constructor</p>
     */
    public CoreValidator() {
        super();
        init();
    }


    /**
     * <p>Initialize state</p>
     */
    protected void init() {
        super.init();
        failed = false;
        validatorInfo = new ValidatorInfo();

        idTagParser = new IdTagParserImpl();
        idTagParser.setValidatorInfo(validatorInfo);
    }


    /**
     * <p>Release and re-initialize state</p>
     */
    public void release() {
        super.release();
        init();
    }

    //
    // Superclass overrides.
    // 

    /**
     * <p>Get the validator handler</p>
     */
    protected DefaultHandler getSAXHandler() {
        DefaultHandler h = new CoreValidatorHandler();
        return h;
    }


    /**
     * <p>Create failure message from any failed validations</p>
     *
     * @param prefix Tag library prefix
     * @param uri    Tag library uri
     */
    protected String getFailureMessage(String prefix, String uri) {
        // we should only get called if this Validator failed        
        StringBuffer result = new StringBuffer();

        if (idTagParser.hasFailed()) {
            result.append(idTagParser.getMessage());
        }
        return result.toString();
    }


    //*********************************************************************
    // SAX handler

    /**
     * <p>The handler that provides the base of the TLV implementation.</p>
     */
    private class CoreValidatorHandler extends DefaultHandler {

        /**
         * Parse the starting element. If it is a specific JSTL tag
         * make sure that the nested JSF tags have IDs.
         *
         * @param ns Element name space.
         * @param ln Element local name.
         * @param qn Element QName.
         * @param attrs  Element's Attribute list.
         */
        public void startElement(String ns,
                                 String ln,
                                 String qn,
                                 Attributes attrs) {
            maybeSnagTLPrefixes(qn, attrs);

            validatorInfo.setQName(qn);
            validatorInfo.setAttributes(attrs);
            validatorInfo.setValidator(CoreValidator.this);

            idTagParser.parseStartElement();

            if (idTagParser.hasFailed()) {
                failed = true;
            }
        }


        /**
         * <p>Parse the ending element. If it is a specific JSTL tag
         * make sure that the nested count is decreased.</p>
         *
         * @param ns Element name space.
         * @param ln Element local name.
         * @param qn Element QName.
         */
        public void endElement(String ns, String ln, String qn) {
            validatorInfo.setQName(qn);
            idTagParser.parseEndElement();
        }
    }
}
