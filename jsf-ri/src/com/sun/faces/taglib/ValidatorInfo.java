/*
 * $Id: ValidatorInfo.java,v 1.7 2006/03/29 23:03:50 rlubke Exp $
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

import org.xml.sax.Attributes;

/**
 *
 *
 */
public class ValidatorInfo {

    //*********************************************************************
    // Validation and configuration state (protected)
    private String nameSpace;
    private String localName;
    private String qName;
    private Attributes attributes;
    private FacesValidator validator;
    private String prefix;
    private String uri;


    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }


    public String getNameSpace() {
        return nameSpace;
    }


    public void setLocalName(String localName) {
        this.localName = localName;
    }


    public String getLocalName() {
        return localName;
    }


    public void setQName(String qName) {
        this.qName = qName;
    }


    public String getQName() {
        return qName;
    }


    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }


    public Attributes getAttributes() {
        return attributes;
    }


    public void setValidator(FacesValidator validator) {
        this.validator = validator;
    }


    public FacesValidator getValidator() {
        return validator;
    }


    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


    public String getPrefix() {
        return prefix;
    }


    public void setUri(String uri) {
        this.uri = uri;
    }


    public String getUri() {
        return uri;
    }


    public String toString() {
        StringBuffer mesg = new StringBuffer();

        mesg.append("\nValidatorInfo NameSpace: ");
        mesg.append(nameSpace);
        mesg.append("\nValidatorInfo LocalName: ");
        mesg.append(localName);
        mesg.append("\nValidatorInfo QName: ");
        mesg.append(qName);
        for (int i = 0; i < attributes.getLength(); i++) {
            mesg.append("\nValidatorInfo attributes.getQName(");
            mesg.append(i);
            mesg.append("): ");
            mesg.append(attributes.getQName(i));
            mesg.append("\nValidatorInfo attributes.getValue(");
            mesg.append(i);
            mesg.append("): ");
            mesg.append(attributes.getValue(i));
        }
        mesg.append("\nValidatorInfo prefix: ");
        mesg.append(prefix);
        mesg.append("\nValidatorInfo uri: ");
        mesg.append(uri);

        return mesg.toString();
    }
}
