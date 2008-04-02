/*
 * $Id: IdTagParserImpl.java,v 1.8 2004/02/26 20:33:17 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.RIConstants;
import com.sun.faces.taglib.FacesValidator;
import com.sun.faces.taglib.TagParser;
import com.sun.faces.taglib.ValidatorInfo;
import org.xml.sax.Attributes;

import java.text.MessageFormat;
import java.util.ResourceBundle;


/**
 * <p>Parses tags to verify that an id attribute is present if it is
 * determined to be required</p>
 */
public class IdTagParserImpl implements TagParser {

    //*********************************************************************
    // Constants

    //*********************************************************************
    // Validation and configuration state (protected)

    private boolean siblingSatisfied;		// is there a JSF sibling?
    private int requiresIdCount;		// nested count
    private StringBuffer requiresIdList;	// list of failing tags
    private boolean failed;
    private ValidatorInfo validatorInfo;
    private boolean nestedInNamingContainer;


    //*********************************************************************
    // Constructor and lifecycle management

    /**
     * <p>CommandTagParser constructor</p>
     */
    public IdTagParserImpl() {
        failed = false;
        siblingSatisfied = true;
        requiresIdCount = 0;
        requiresIdList = new StringBuffer();
    }


    /**
     * <p>Set the validator info object that has the current tag
     * information</p>
     *
     * @param ValidatorInfo object with current tag info
     */
    public void setValidatorInfo(ValidatorInfo validatorInfo) {
        this.validatorInfo = validatorInfo;
    }


    /**
     * <p>Get the failure message</p>
     *
     * @return String Failure message
     */
    public String getMessage() {
        Object[] obj = new Object[1];
        obj[0] = requiresIdList;
        ResourceBundle rb = ResourceBundle.getBundle(
            RIConstants.TLV_RESOURCE_LOCATION);
        return MessageFormat.format(rb.getString("TLV_ID_ERROR"), obj);
    }


    /**
     * <p>Return false if validator conditions have not been met</p>
     *
     * @return boolean false if validation conditions have not been met
     */
    public boolean hasFailed() {
        return failed;
    }


    /**
     * <p>Parse the starting element. If it is a specific JSTL tag
     * make sure that the nested JSF tags have IDs. </p>
     */
    public void parseStartElement() {
        String qn = validatorInfo.getQName();
        Attributes a = validatorInfo.getAttributes();
        FacesValidator validator = validatorInfo.getValidator();

        if (isJstlTag(validator, qn)) {
            requiresIdCount++;
        } else if (isNamingContainerTag(validator, qn)) {
            nestedInNamingContainer = true;
        } else if ((validator.getJSF_HTML_PRE() != null) &&
            (qn.startsWith(validator.getJSF_HTML_PRE())) &&
            (requiresIdCount > 0)) {
            //make sure that id is present in attributes
            if ((!(nestedInNamingContainer)) && (!hasIdAttribute(a))) {
                //add to list of jsf tags for error report
                failed = true;
                requiresIdList.append(qn).append(' ');
            }
        } else if ((requiresIdCount == 0) &&
            (!siblingSatisfied)) {
            //make sure jsf sibling has an id
            if ((validator.getJSF_HTML_PRE() != null) &&
                (qn.startsWith(validator.getJSF_HTML_PRE()) ||
                qn.startsWith(validator.getJSF_CORE_PRE())) &&
                (!hasIdAttribute(a)) && (!(nestedInNamingContainer))) {

                //add to list of jsf tags for error report
                failed = true;
                requiresIdList.append(qn).append(' ');
            }
            siblingSatisfied = true;
        } else if (requiresIdCount == 0) {
            // sibling is a non-JSF tag
            // JSF tags no longer need id's
            siblingSatisfied = true;
        }

    }


    /**
     * <p>Parse the ending element. If it is a specific JSTL tag
     * make sure that the appropriate flags are set.</p>
     */
    public void parseEndElement() {
        String qn = validatorInfo.getQName();
        FacesValidator validator = validatorInfo.getValidator();

        if (isJstlTag(validator, qn)) {
            requiresIdCount--;
            siblingSatisfied = false;
        } else if (isNamingContainerTag(validator, qn)) {
            nestedInNamingContainer = false;
        }
    }

    //*********************************************************************
    // Private methods

    /**
     * <p>Check element to make sure that the id attribute is
     * present.</p>
     *
     * @param a Attribute list
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
     * @param validator Parent validator
     * @param qn        Element to be checked.
     * @return boolean True if JSTL tag is iterator or conditional
     */
    private boolean isJstlTag(FacesValidator validator, String qn) {
        if (qn.equals(validator.getJSTL_IF_QN()) ||
            qn.equals(validator.getJSTL_CHOOSE_QN()) ||
            qn.equals(validator.getJSTL_FOREACH_QN()) ||
            qn.equals(validator.getJSTL_FORTOKENS_QN())) {
            return true;
        }
        return false;
    }


    /**
     * Check to make sure that the element is either a
     * form tag or a subview tag.
     *
     * @param validator Parent validator
     * @param qn        Element to be checked.
     * @return boolean True if JSF tag is form or subview
     */
    private boolean isNamingContainerTag(FacesValidator validator, String qn) {

        // PENDING (visvan) Handle custom implementations of NamingContainer.
        // This requires the compiler to look up a fake Faces environment
        // so that we can look up the component class based on what 
        // getComponentType() returns to detect customer NamingContainer
        // components.
        if (qn.equals(validator.getJSF_FORM_QN()) ||
            qn.equals(validator.getJSF_SUBVIEW_QN())) {
            return true;
        }
        return false;
    }
}
