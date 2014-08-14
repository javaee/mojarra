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

package com.sun.faces.taglib.jsf_core;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.xml.sax.Attributes;

import com.sun.faces.RIConstants;
import com.sun.faces.taglib.FacesValidator;
import com.sun.faces.taglib.TagParser;
import com.sun.faces.taglib.ValidatorInfo;


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
        String ns = validatorInfo.getNameSpace();
        String ln = validatorInfo.getLocalName();

        String qn = validatorInfo.getQName();
        Attributes a = validatorInfo.getAttributes();
        FacesValidator validator = validatorInfo.getValidator();

        if (isNamingContainerTag(validator, ns, ln)) {
            nestedInNamingContainer = true;
        } else if ((ns.equals(RIConstants.HTML_NAMESPACE)) &&
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
            if (((ns.equals(RIConstants.HTML_NAMESPACE)) ||
                (ns.equals(RIConstants.CORE_NAMESPACE))) &&
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
        String ns = validatorInfo.getNameSpace();
        String ln = validatorInfo.getLocalName();
        FacesValidator validator = validatorInfo.getValidator();
        if (isNamingContainerTag(validator, ns, ln)) {
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
     * form tag or a subview tag.
     *
     * @param validator Parent validator
     * @param ns        The Namespace.
     * @param ln        The Local Name.
     * @return boolean True if JSF tag is form or subview
     */
    private boolean isNamingContainerTag(FacesValidator validator, String ns, String ln) {

        // PENDING (visvan) Handle custom implementations of NamingContainer.
        // This requires the compiler to look up a fake Faces environment
        // so that we can look up the component class based on what 
        // getComponentType() returns to detect customer NamingContainer
        // components.
        if (ns.equals(RIConstants.HTML_NAMESPACE)) {
            if (ln.equals(validator.getJSF_FORM_LN())) {
                return true; 
            }
        } 
        if (ns.equals(RIConstants.CORE_NAMESPACE)) {
            if (ln.equals(validator.getJSF_SUBVIEW_LN())) {
                return true;
            }
        }
        return false;
    }
}
