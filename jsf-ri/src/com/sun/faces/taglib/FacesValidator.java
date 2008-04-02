/*
 * $Id: FacesValidator.java,v 1.16 2006/03/29 22:38:39 rlubke Exp $
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

package com.sun.faces.taglib;

import javax.servlet.jsp.tagext.PageData;
import javax.servlet.jsp.tagext.TagLibraryValidator;
import javax.servlet.jsp.tagext.ValidationMessage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.IOException;

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


    // Prefix for JSF CORE tags
    protected String JSF_CORE_PRE = null;

    // Local Name for JSF Form tag
    protected String JSF_FORM_LN = "form";

    // QName for JSF Form tag
    protected String JSF_FORM_QN = ":form";

    // Prefix for JSF HTML tags
    protected String JSF_HTML_PRE = null;

    // Local Name for JSF subview tag
    protected String JSF_SUBVIEW_LN = "subview";

    // QName for JSF subview tag
    protected String JSF_SUBVIEW_QN = ":subview";

    // Local Name for JSTL conditional tag
    protected String JSTL_CHOOSE_LN = "choose";

    // QName for JSTL conditional tag
    protected String JSTL_CHOOSE_QN = ":choose";


    // Prefix for JSTL CORE tags
    protected String JSTL_CORE_PRE = null;

    // Local Name for JSTL iterator tag
    protected String JSTL_FOREACH_LN = "forEach";

    // QName for JSTL iterator tag
    protected String JSTL_FOREACH_QN = ":forEach";

    // Local Name for JSTL iterator tag
    protected String JSTL_FORTOKENS_LN = "forTokens";

    // QName for JSTL iterator tag
    protected String JSTL_FORTOKENS_QN = ":forTokens";

    // Local Name for JSTL conditional tag
    protected String JSTL_IF_LN = "if";


    // QName for JSTL conditional tag
    protected String JSTL_IF_QN = ":if";

    //*********************************************************************
    // Constants

    //*********************************************************************
    // Validation and configuration state (protected)

    protected boolean failed;            // did the page fail?

    //*********************************************************************
    // Constants

    private static final String JSF_CORE_URI = "http://java.sun.com/jsf/core";

    private static final String JSF_HTML_URI = "http://java.sun.com/jsf/html";

    private static final String JSTL_NEW_CORE_URI =
          "http://java.sun.com/jsp/jstl/core";

    private static final String JSTL_OLD_CORE_URI =
          "http://java.sun.com/jstl/core";

    // ------------------------------------------------------------ Constructors

    //*********************************************************************
    // Constructor and lifecycle management

    public FacesValidator() {

        super();
        init();

    }

    // ---------------------------------------------------------- Public Methods


    public String getJSF_CORE_PRE() {

        return JSF_CORE_PRE;

    }


    public String getJSF_FORM_LN() {

        return JSF_FORM_LN;

    }


    public String getJSF_FORM_QN() {

        return JSF_FORM_QN;

    }


    public String getJSF_HTML_PRE() {

        return JSF_HTML_PRE;

    }


    public String getJSF_SUBVIEW_LN() {

        return JSF_SUBVIEW_LN;

    }


    public String getJSF_SUBVIEW_QN() {

        return JSF_SUBVIEW_QN;

    }


    public String getJSTL_CHOOSE_LN() {

        return JSTL_CHOOSE_LN;

    }


    public String getJSTL_CHOOSE_QN() {

        return JSTL_CHOOSE_QN;

    }


    public String getJSTL_CORE_PRE() {

        return JSTL_CORE_PRE;

    }


    public String getJSTL_FOREACH_LN() {

        return JSTL_FOREACH_LN;

    }


    public String getJSTL_FOREACH_QN() {

        return JSTL_FOREACH_QN;

    }


    public String getJSTL_FORTOKENS_LN() {

        return JSTL_FORTOKENS_LN;

    }


    public String getJSTL_FORTOKENS_QN() {

        return JSTL_FORTOKENS_QN;

    }


    public String getJSTL_IF_LN() {

        return JSTL_IF_LN;

    }


    public String getJSTL_IF_QN() {

        return JSTL_IF_QN;

    }


    public void release() {

        super.release();
        init();

    }

    //*********************************************************************
    // Validation entry point

    /**
     * Validate a JSP page. Return an an array of Validation
     * Messages if a validation failure occurs. Return null
     * on success.
     *
     * @param prefix Value of directive prefix argument.
     * @param uri    Value of directive uri argument.
     * @param page   JspData page object.
     *
     * @return ValidationMessage[] An array of Validation messages.
     */
    public synchronized ValidationMessage[] validate(String prefix, String uri,
                                                     PageData page) {

        ValidationMessage[] result = null;
        try {

// get a handler
            DefaultHandler h = getSAXHandler();

            // if the subclass doesn't want validation to ocurr
            if (null == h) {
                // don't validate
                return result;
            }

            // parse the page
            SAXParserFactory f = SAXParserFactory.newInstance();
            f.setNamespaceAware(true);
            f.setValidating(true);
            SAXParser p = f.newSAXParser();
            p.parse(page.getInputStream(), h);

//on validation failure generate error message
            if (failed) {
                result = vmFromString(getFailureMessage(prefix, uri));
            } else {
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

    // ------------------------------------------------------- Protected Methods


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


    protected abstract String getFailureMessage(String prefix, String uri);

    /**
     * <p>Subclass override.  If it returns null, the subclass is
     * telling us: do not validate.</p>
     */


    protected abstract DefaultHandler getSAXHandler();


    protected void init() {

        failed = false;

    }


    /**
     * This method provides for the ability of the TLV to use whatever
     * user defined tag lib prefix is in the page to recognize tags.
     */

    protected void maybeSnagTLPrefixes(String qName, Attributes attrs) {

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
                        JSF_SUBVIEW_QN = JSF_CORE_PRE + JSF_SUBVIEW_QN;
                    } else if (value.equals(JSF_HTML_URI)) {
                        JSF_HTML_PRE = prefix;
                        JSF_FORM_QN = JSF_HTML_PRE + JSF_FORM_QN;
                    } else if (value.equals(JSTL_OLD_CORE_URI) ||
                               value.equals(JSTL_NEW_CORE_URI)) {
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

    // --------------------------------------------------------- Private Methods

    //*********************************************************************
    // Utility functions

    /**
     * Construct a ValidationMessage[] from a single String and no ID.
     *
     * @param message Message string.
     *
     * @return ValidationMessage[] An array of Validation Messages.
     */
    private ValidationMessage[] vmFromString(String message) {

        return new ValidationMessage[]{
              new ValidationMessage(null, message)
        };

    }

}
