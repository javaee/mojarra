/*
 * $Id: ManagedPropertyRule.java,v 1.3 2004/02/04 23:46:22 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.rules;


import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;
import com.sun.faces.config.beans.ManagedPropertyBean;
import com.sun.faces.config.beans.ManagedBeanBean;


/**
 * <p>Digester rule for the <code>&lt;managed-property&gt;</code> element.</p>
 */

public class ManagedPropertyRule extends FeatureRule {


    private static final String CLASS_NAME =
        "com.sun.faces.config.beans.ManagedPropertyBean";


    // ------------------------------------------------------------ Rule Methods


    /**
     * <p>Create an empty instance of <code>ManagedPropertyBean</code>
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

        ManagedBeanBean mbb = null;
        try {
            mbb = (ManagedBeanBean) digester.peek();
        } catch (Exception e) {
            throw new IllegalStateException
                ("No parent ManagedBeanBean on object stack");
        }
        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[ManagedPropertyRule]{" +
                                       digester.getMatch() +
                                       "} Push " + CLASS_NAME);
        }
        Class clazz =
            digester.getClassLoader().loadClass(CLASS_NAME);
        ManagedPropertyBean mpb = (ManagedPropertyBean) clazz.newInstance();
        digester.push(mpb);

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
     * <p>Pop the <code>ManagedPropertyBean</code> off the top of the stack,
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

        ManagedPropertyBean top = null;
        try {
            top = (ManagedPropertyBean) digester.pop();
        } catch (Exception e) {
            throw new IllegalStateException("Popped object is not a " +
                                            CLASS_NAME + " instance");
        }
        ManagedBeanBean mbb = (ManagedBeanBean) digester.peek();
        ManagedPropertyBean old =
            mbb.getManagedProperty(top.getPropertyName());
        if (old == null) {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[ManagedPropertyRule]{" +
                                           digester.getMatch() +
                                           "} New(" +
                                           top.getPropertyName() +
                                           ")");
            }
            mbb.addManagedProperty(top);
        } else {
            if (digester.getLogger().isWarnEnabled()) {
                digester.getLogger().warn("[ManagedPropertyRule]{" +
                                          digester.getMatch() +
                                          "} Merge(" +
                                          top.getPropertyName() +
                                          ")");
            }
            mergeManagedProperty(top, old);
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

        StringBuffer sb = new StringBuffer("ManagedPropertyRule[className=");
        sb.append(CLASS_NAME);
        sb.append("]");
        return (sb.toString());

    }


    // --------------------------------------------------------- Package Methods


    // Merge "top" into "old"
    static void mergeManagedProperty(ManagedPropertyBean top,
                                     ManagedPropertyBean old) {

        // Merge singleton properties
        if (top.getPropertyClass() != null) {
            old.setPropertyClass(top.getPropertyClass());
        }
        if (top.isNullValue()) {
            old.setNullValue(true);
        }
        if (top.getValue() != null) {
            old.setValue(top.getValue());
        }

        // Merge common collections
        mergeFeatures(top, old);

        // Merge unique collections
        ListEntriesRule.mergeListEntries(top, old);
        MapEntriesRule.mergeMapEntries(top, old);

    }


    // Merge "top" into "old"
    static void mergeManagedProperties(ManagedBeanBean top,
                                       ManagedBeanBean old) {

        ManagedPropertyBean mpb[] = top.getManagedProperties();
        for (int i = 0; i < mpb.length; i++) {
            ManagedPropertyBean mpbo =
                old.getManagedProperty(mpb[i].getPropertyName());
            if (mpbo == null) {
                old.addManagedProperty(mpb[i]);
            } else {
                mergeManagedProperty(mpb[i], mpbo);
            }
        }

    }


}
