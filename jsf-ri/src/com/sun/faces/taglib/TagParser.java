/*
 * $Id: TagParser.java,v 1.1 2003/08/19 21:40:48 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib;

/**
 *
 * <p>Interface defining the Validator Tag Parser implementation methods</p>
 *
 */
public interface TagParser {

    /**
     * <p>Return the failure message for a failed validation</p>
     *
     * @return String Failure message
     */
    public String getMessage();

    /**
     * <p>Return false if validator conditions have not been met</p>
     *
     * @return boolean false if validation conditions have not been met
     */
    public boolean hasFailed();

    /**
     * <p>Set the Validator Info Bean</p>
     */
    public void setValidatorInfo(ValidatorInfo validatorInfo);

    /**
     * <p>Parse the starting element.  Parcel out to appropriate
     * handler method.</p>
     */
    public void parseStartElement();

    /**
     * <p>Parse the ending element.  Parcel out to appropriate
     * handler method.</p>
     */
    public void parseEndElement();
}
