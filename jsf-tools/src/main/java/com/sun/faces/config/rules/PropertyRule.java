/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.config.rules;


import org.xml.sax.Attributes;

import com.sun.faces.config.beans.PropertyBean;
import com.sun.faces.config.beans.PropertyHolder;


/**
 * <p>Digester rule for the <code>&lt;property&gt;</code> element.</p>
 */

public class PropertyRule extends FeatureRule {


    private static final String CLASS_NAME =
        "com.sun.faces.config.beans.PropertyBean";


    // ------------------------------------------------------------ Rule Methods


    /**
     * <p>Create an empty instance of <code>PropertyBean</code>
     * and push it on to the object stack.</p>
     *
     * @param namespace the namespace URI of the matching element, or an 
     *   empty string if the parser is not namespace aware or the element has
     *   no namespace
     * @param name the local name if the parser is namespace aware, or just 
     *   the element name otherwise
     * @param attributes The attribute list of this element
     *
     * @exception IllegalStateException if the parent stack element is not
     *  of type FacesConfigBean
     */
    public void begin(String namespace, String name,
                      Attributes attributes) throws Exception {

        assert digester.peek() instanceof PropertyHolder
              : "Assertion Error: Expected PropertyHolder to be at the top of the stack";
        
        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[PropertyRule]{" +
                                       digester.getMatch() +
                                       "} Push " + CLASS_NAME);
        }
        Class clazz =
            digester.getClassLoader().loadClass(CLASS_NAME);
        PropertyBean pb = (PropertyBean) clazz.newInstance();
        digester.push(pb);

    }


    /**
     * <p>No body processing is required.</p>
     *
     * @param namespace the namespace URI of the matching element, or an 
     *   empty string if the parser is not namespace aware or the element has
     *   no namespace
     * @param name the local name if the parser is namespace aware, or just 
     *   the element name otherwise
     * @param text The text of the body of this element
     */
    public void body(String namespace, String name,
                     String text) throws Exception {
    }


    /**
     * <p>Pop the <code>PropertyBean</code> off the top of the stack,
     * and either add or merge it with previous information.</p>
     *
     * @param namespace the namespace URI of the matching element, or an 
     *   empty string if the parser is not namespace aware or the element has
     *   no namespace
     * @param name the local name if the parser is namespace aware, or just 
     *   the element name otherwise
     *
     * @exception IllegalStateException if the popped object is not
     *  of the correct type
     */
    public void end(String namespace, String name) throws Exception {

        PropertyBean top = null;
        try {
            top = (PropertyBean) digester.pop();
        } catch (Exception e) {
            throw new IllegalStateException("Popped object is not a " +
                                            CLASS_NAME + " instance");
        }
        PropertyHolder ah = (PropertyHolder) digester.peek();
        PropertyBean old = ah.getProperty(top.getPropertyName());
        if (old == null) {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[PropertyRule]{" +
                                           digester.getMatch() +
                                           "} New(" +
                                           top.getPropertyName() +
                                           ")");
            }
            ah.addProperty(top);
        } else {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[PropertyRule]{" +
                                          digester.getMatch() +
                                          "} Merge(" +
                                          top.getPropertyName() +
                                          ")");
            }
            mergeProperty(top, old);
        }

    }


    /**
     * <p>No finish processing is required.</p>
     *
     */
    public void finish() throws Exception {
    }


    // ---------------------------------------------------------- Public Methods


    public String toString() {

        StringBuffer sb = new StringBuffer("PropertyRule[className=");
        sb.append(CLASS_NAME);
        sb.append("]");
        return (sb.toString());

    }


    // --------------------------------------------------------- Package Methods


    // Merge "top" into "old"
    static void mergeProperty(PropertyBean top, PropertyBean old) {

        // Merge singleton properties
        if (top.getPropertyClass() != null) {
            old.setPropertyClass(top.getPropertyClass());
        }
        if (top.getDefaultValue() != null) {
            old.setDefaultValue(top.getDefaultValue());
        }
        if (top.getSuggestedValue() != null) {
            old.setSuggestedValue(top.getSuggestedValue());
        }
        if (top.isPassThrough()) {
            old.setPassThrough(true);
        }
        if (top.isReadOnly()) {
            old.setReadOnly(true);
        }
        if (top.isRequired()) {
            old.setRequired(true);
        }
        if (!top.isTagAttribute()) {
            old.setTagAttribute(false);
        }

        if(null != top.getBehaviors()){
        	old.addAllBehaviors(top.getBehaviors());
        }
        if(top.isDefaultBehavior()){
        	old.setDefaultBehavior(true);
        }
        // Merge common collections
        mergeFeatures(top, old);

        // Merge unique collections

    }


    // Merge "top" into "old"
    static void mergeProperties(PropertyHolder top, PropertyHolder old) {

        PropertyBean pb[] = top.getProperties();
        for (int i = 0; i < pb.length; i++) {
            PropertyBean pbo = old.getProperty(pb[i].getPropertyName());
            if (pbo == null) {
                old.addProperty(pb[i]);
            } else {
                mergeProperty(pb[i], pbo);
            }
        }

    }


}
