/*
 * $Id: ValidatorInfo.java,v 1.2 2004/02/04 23:42:03 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib;

import com.sun.faces.taglib.FacesValidator;
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
