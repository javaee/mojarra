/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.taglib.html_basic;

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
     */
    public CommandTagParserImpl() {
        failed = false;
        failureMessages = new StringBuffer();
    }


    /**
     * <p>Set the validator info object that has the current tag
     * information</p>
     *
     * @param validatorInfo object with current tag info
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

        if (ns.equals(RIConstants.HTML_NAMESPACE)) {
            if (ln.equals("commandButton")) {
                handleCommandButton();
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
     * <p>set failed flag to true unless tag has a value attribute</p>.
     * <p/>
     * <p>PRECONDITION: qn is a commandButton</p>
     */
    private void handleCommandButton() {
        Attributes attrs = validatorInfo.getAttributes();
        String ln = validatorInfo.getLocalName();
        boolean hasValue = false;
        boolean hasImage = false;
	boolean hasBinding = false;

        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getLocalName(i).equals("value")) {
                hasValue = true;
            }
            if (attrs.getLocalName(i).equals("image")) {
                hasImage = true;
            }
            if (attrs.getLocalName(i).equals("binding")) {
                hasBinding = true;
            }
        }
        if (failed = (!hasBinding && !(hasValue || hasImage))) {
            Object[] obj = new Object[1];
            obj[0] = ln;
            ResourceBundle rb = ResourceBundle.getBundle(
                RIConstants.TLV_RESOURCE_LOCATION);
            failureMessages.append(
                MessageFormat.format(rb.getString("TLV_COMMAND_ERROR"), obj));
            failureMessages.append("\n");
        }

    }

}
