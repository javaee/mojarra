/*
 * $Id: ConverterRule.java,v 1.2 2004/01/27 20:13:56 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.rules;


import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;
import com.sun.faces.config.beans.ConverterBean;
import com.sun.faces.config.beans.FacesConfigBean;


/**
 * <p>Digester rule for the <code>&lt;converter&gt;</code> element.</p>
 */

public class ConverterRule extends FeatureRule {


    private static final String CLASS_NAME =
        "com.sun.faces.config.beans.ConverterBean";


    // ------------------------------------------------------------ Rule Methods


    /**
     * <p>Create an empty instance of <code>ConverterBean</code>
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

        FacesConfigBean fcb = null;
        try {
            fcb = (FacesConfigBean) digester.peek();
        } catch (Exception e) {
            throw new IllegalStateException
                ("No parent FacesConfigBean on object stack");
        }
        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[ConverterRule]{" +
                                       digester.getMatch() +
                                       "} Push " + CLASS_NAME);
        }
        Class clazz =
            digester.getClassLoader().loadClass(CLASS_NAME);
        ConverterBean cb = (ConverterBean) clazz.newInstance();
        digester.push(cb);

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
     * <p>Pop the <code>ConverterBean</code> off the top of the stack,
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

        ConverterBean top = null;
        try {
            top = (ConverterBean) digester.pop();
        } catch (Exception e) {
            throw new IllegalStateException("Popped object is not a " +
                                            CLASS_NAME + " instance");
        }
        FacesConfigBean fcb = (FacesConfigBean) digester.peek();
        ConverterBean old = null;
        if (top.getConverterId() != null) {
            old = fcb.getConverterById(top.getConverterId());
        } else {
            old = fcb.getConverterByClass(top.getConverterForClass());
        }
        if (old == null) {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[ConverterRule]{" +
                                           digester.getMatch() +
                                           "} New(" +
                                           top.getConverterId() +
                                           "," +
                                           top.getConverterForClass() +
                                           ")");
            }
            fcb.addConverter(top);
        } else {
            if (digester.getLogger().isWarnEnabled()) {
                digester.getLogger().warn("[ConverterRule]{" +
                                          digester.getMatch() +
                                          "} Merge(" +
                                          top.getConverterId() +
                                          "," +
                                          top.getConverterForClass() +
                                          ")");
            }
            mergeConverter(top, old);
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

        StringBuffer sb = new StringBuffer("ConverterRule[className=");
        sb.append(CLASS_NAME);
        sb.append("]");
        return (sb.toString());

    }


    // --------------------------------------------------------- Package Methods


    // Merge "top" into "old"
    static void mergeConverter(ConverterBean top, ConverterBean old) {

        // Merge singleton properties
        if (top.getConverterClass() != null) {
            old.setConverterClass(top.getConverterClass());
        }

        // Merge common collections
        AttributeRule.mergeAttributes(top, old);
        mergeFeatures(top, old);
        PropertyRule.mergeProperties(top, old);

        // Merge unique collections

    }


}
