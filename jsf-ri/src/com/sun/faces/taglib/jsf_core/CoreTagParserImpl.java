/*
 * $Id: CoreTagParserImpl.java,v 1.1 2004/12/02 18:42:24 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.RIConstants;
import com.sun.faces.taglib.TagParser;
import com.sun.faces.taglib.ValidatorInfo;
import org.xml.sax.Attributes;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * <p> Parses the command tag attributes and verifies that the required
 * attributes are present</p>
 */
public class CoreTagParserImpl implements TagParser {

    //*********************************************************************
    // Validation and configuration state (protected)

    // PENDING(edburns): Make this localizable
    private StringBuffer failureMessages;	// failureMessages
    private boolean failed;
    private ValidatorInfo validatorInfo;

    //*********************************************************************
    // Constructor and lifecycle management

    /**
     * <p>CoreTagParserImpl constructor</p>
     */
    public CoreTagParserImpl() {
        failed = false;
        failureMessages = new StringBuffer();
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
        return failureMessages.toString();
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
     * <p>Parse the starting element.  Parcel out to appropriate
     * handler method.</p>
     */
    public void parseStartElement() {
       
        String ns = validatorInfo.getNameSpace();
        String ln = validatorInfo.getLocalName();

        if (ns.equals(RIConstants.CORE_NAMESPACE)) {
            if(ln.equals("valueChangeListener")) {
                handleListener();
            } else if (ln.equals("actionListener")) {
                handleListener();
            } else if (ln.equals("converter")) {
                handleConverter();
            } else if (ln.equals("validator")) {
                handleValidator();
            }
        }
    }


    /**
     * <p>Parse the end element</p>
     */
    public void parseEndElement() {
        //no parsing required
    }


    //*********************************************************************
    // Private methods

    /**
     * <p>Listener tags must have a "type" and/or "binding" attribute.</p>
     * <p/>
     * <p>PRECONDITION: qn is an actionListener or valueChangeListener </p>
     */
    private void handleListener() {
        Attributes attrs = validatorInfo.getAttributes();
        String ln = validatorInfo.getLocalName();
	boolean hasType = false;
	boolean hasBinding = false;

        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getLocalName(i).equals("type")) {
                hasType = true;
            }
            if (attrs.getLocalName(i).equals("binding")) {
                hasBinding = true;
            }
        }
        if (failed = (!hasBinding && !hasType)) {
            Object[] obj = new Object[1];
            obj[0] = ln;
            ResourceBundle rb = ResourceBundle.getBundle(
                RIConstants.TLV_RESOURCE_LOCATION);
            failureMessages.append(
                MessageFormat.format(rb.getString("TLV_LISTENER_ERROR"), obj));
            failureMessages.append("\n");
        }
    }

    /**
     * <p>Validator tag must have a "validatorId" and/or "binding" attribute.</p>
     * <p/>
     * <p>PRECONDITION: qn is a validator</p>
     */
    private void handleValidator() {
        Attributes attrs = validatorInfo.getAttributes();
        String ln = validatorInfo.getLocalName();
        boolean hasValidatorId = false;
        boolean hasBinding = false;
                                                                                     
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getLocalName(i).equals("validatorId")) {
                hasValidatorId = true;
            }
            if (attrs.getLocalName(i).equals("binding")) {
                hasBinding = true;
            }
        }
        if (failed = (!hasBinding && !hasValidatorId)) {
            Object[] obj = new Object[1];
            obj[0] = ln;
            ResourceBundle rb = ResourceBundle.getBundle(
                RIConstants.TLV_RESOURCE_LOCATION);
            failureMessages.append(
                MessageFormat.format(rb.getString("TLV_VALIDATOR_ERROR"), obj));
            failureMessages.append("\n");
        }
    }

    /**
     * <p>Converter tag must have a "converterId" and/or "binding" attribute.</p>
     * <p/>
     * <p>PRECONDITION: qn is a converter</p>
     */
    private void handleConverter() {
        Attributes attrs = validatorInfo.getAttributes();
        String ln = validatorInfo.getLocalName();
        boolean hasConverterId = false;
        boolean hasBinding = false;
                                                                                     
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getLocalName(i).equals("converterId")) {
                hasConverterId = true;
            }
            if (attrs.getLocalName(i).equals("binding")) {
                hasBinding = true;
            }
        }
        if (failed = (!hasBinding && !hasConverterId)) {
            Object[] obj = new Object[1];
            obj[0] = ln;
            ResourceBundle rb = ResourceBundle.getBundle(
                RIConstants.TLV_RESOURCE_LOCATION);
            failureMessages.append(
                MessageFormat.format(rb.getString("TLV_CONVERTER_ERROR"), obj));
            failureMessages.append("\n");
        }
    }
}
