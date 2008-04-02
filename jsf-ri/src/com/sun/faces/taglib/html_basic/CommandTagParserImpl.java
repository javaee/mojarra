/*
 * $Id: CommandTagParserImpl.java,v 1.2 2003/09/09 01:01:07 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import org.xml.sax.Attributes;

import com.sun.faces.taglib.FacesValidator;
import com.sun.faces.taglib.ValidatorInfo;
import com.sun.faces.taglib.TagParser;
import com.sun.faces.util.Util;

import org.mozilla.util.Assert;

/**
 * <p> Parses the command tag attributes and verifies that the required
 * attributes are present</p>
 */
public class CommandTagParserImpl implements TagParser {
    //*********************************************************************
    // Validation and configuration state (protected)

    // PENDING(edburns): Make this localizable
    private StringBuffer failureMessages;	// failureMessages
    private boolean failed;
    private ValidatorInfo validatorInfo;

    //*********************************************************************
    // Constructor and lifecycle management

    /**
     * <p>CommandTagParserImpl constructor</p>
     *
     * @param validatorInfo Bean with element property values populated
     *                      by the validator
     */
    public CommandTagParserImpl() {
        this.validatorInfo = validatorInfo;
        failed = false;
	failureMessages = new StringBuffer();
    }

    /**
     * <p>Set the validator info object that has the current tag
     * information</p>
     *
     * @param ValidatorInfo object with current tag info
     *
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
        String qn = validatorInfo.getQName();

        if (-1 != (qn.indexOf("command_button"))) {
	    handleCommandButton();
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
     * <p>set failed flag to true unless tag has a key, image, 
     * imageKey, label, or valueRef attribute</p>.
     *
     * <p>PRECONDITION: qn is a command_button</p>
     */
    private void handleCommandButton() {
        Attributes attrs = validatorInfo.getAttributes();
        String qn = validatorInfo.getQName();
        boolean hasKey = false, 
                hasImage = false, 
                hasValue = false, 
                hasValueRef = false;

        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals("key")) {
                hasKey = true;
            }
            if (attrs.getQName(i).equals("image") ||
                attrs.getQName(i).equals("imageKey")) {
                hasImage = true;
            }
            if (attrs.getQName(i).equals("value")) {
                hasValue = true;
            }
            if (attrs.getQName(i).equals("valueRef")) {
                hasValueRef = true;
            }
        }
        if (failed = ((hasKey || hasImage || hasValue || hasValueRef ) 
            != true)) {
  	    Object[] obj = new Object[1];
            obj[0] = qn;
            failureMessages.append(Util.getExceptionMessage(
                Util.VALIDATION_COMMAND_ERROR_ID, obj));
            failureMessages.append("\n");
        }

    }
 
}
