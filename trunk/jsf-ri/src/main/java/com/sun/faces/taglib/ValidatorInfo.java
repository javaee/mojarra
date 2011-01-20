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
