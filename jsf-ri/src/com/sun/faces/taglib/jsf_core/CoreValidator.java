/*
 * $Id: CoreValidator.java,v 1.18 2006/03/29 22:38:41 rlubke Exp $
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

package com.sun.faces.taglib.jsf_core;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.faces.taglib.FacesValidator;
import com.sun.faces.taglib.ValidatorInfo;
import com.sun.faces.util.Util;

/**
 * <p>A TagLibrary Validator class to allow a TLD to mandate that
 * JSF tag must have an id if it is a child or sibling of a JSTL
 * conditional or iteration tag</p>
 *
 * @author Justyna Horwat
 */
public class CoreValidator extends FacesValidator {


    private CoreTagParserImpl coreTagParser;
    private IdTagParserImpl idTagParser;

    //*********************************************************************
    // Constants

    //*********************************************************************
    // Validation and configuration state (protected)

    private ValidatorInfo validatorInfo;

    // ------------------------------------------------------------ Constructors

    //*********************************************************************
    // Constructor and lifecycle management

    /** <p>CoreValidator constructor</p> */
    public CoreValidator() {

        super();
        init();

    }

    // ---------------------------------------------------------- Public Methods


    /** <p>Release and re-initialize state</p> */
    public void release() {

        super.release();
        init();

    }

    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Create failure message from any failed validations</p>
     *
     * @param prefix Tag library prefix
     * @param uri    Tag library uri
     */
    protected String getFailureMessage(String prefix, String uri) {

        // we should only get called if this Validator failed        
        StringBuffer result = new StringBuffer();

        if (idTagParser.getMessage() != null) {
            result.append(idTagParser.getMessage());
        }
        if (coreTagParser.getMessage() != null) {
            result.append(coreTagParser.getMessage());
        }

        return result.toString();

    }

    //
    // Superclass overrides.
    // 

    /** <p>Get the validator handler</p> */
    protected DefaultHandler getSAXHandler() {

        if (java.beans.Beans.isDesignTime() ||
            !Util.isCoreTLVActive()) {
            return null;
        }
        DefaultHandler h = new CoreValidatorHandler();
        return h;

    }


    /** <p>Initialize state</p> */
    protected void init() {

        super.init();
        failed = false;
        validatorInfo = new ValidatorInfo();

        idTagParser = new IdTagParserImpl();
        idTagParser.setValidatorInfo(validatorInfo);

        coreTagParser = new CoreTagParserImpl();
        coreTagParser.setValidatorInfo(validatorInfo);

    }

    //*********************************************************************
    // SAX handler

    /** <p>The handler that provides the base of the TLV implementation.</p> */
    private class CoreValidatorHandler extends DefaultHandler {

        // ---------------------------------------------------------- Public Methods


        /**
         * <p>Parse the ending element. If it is a specific JSTL tag
         * make sure that the nested count is decreased.</p>
         *
         * @param ns Element name space.
         * @param ln Element local name.
         * @param qn Element QName.
         */
        public void endElement(String ns, String ln, String qn) {

            validatorInfo.setNameSpace(ns);
            validatorInfo.setLocalName(ln);
            validatorInfo.setQName(qn);
            idTagParser.parseEndElement();
            coreTagParser.parseEndElement();

        }


        /**
         * Parse the starting element. If it is a specific JSTL tag
         * make sure that the nested JSF tags have IDs.
         *
         * @param ns    Element name space.
         * @param ln    Element local name.
         * @param qn    Element QName.
         * @param attrs Element's Attribute list.
         */
        public void startElement(String ns,
                                 String ln,
                                 String qn,
                                 Attributes attrs) {

            maybeSnagTLPrefixes(qn, attrs);

            validatorInfo.setNameSpace(ns);
            validatorInfo.setLocalName(ln);
            validatorInfo.setQName(qn);
            validatorInfo.setAttributes(attrs);
            validatorInfo.setValidator(CoreValidator.this);

            idTagParser.parseStartElement();

            if (idTagParser.hasFailed()) {
                failed = true;
            }

            coreTagParser.parseStartElement();

            if (coreTagParser.hasFailed()) {
                failed = true;
            }

        }

    }

}
