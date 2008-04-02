/*
 * $Id: FactoryRule.java,v 1.2 2004/01/27 20:13:58 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.rules;


import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;
import com.sun.faces.config.beans.FactoryBean;
import com.sun.faces.config.beans.FacesConfigBean;


/**
 * <p>Digester rule for the <code>&lt;factory&gt;</code> element.</p>
 */

public class FactoryRule extends Rule {


    private static final String CLASS_NAME =
        "com.sun.faces.config.beans.FactoryBean";


    // ------------------------------------------------------------ Rule Methods


    /**
     * <p>Create or retrieve an instance of <code>FactoryBean</code>
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
        FactoryBean fb = fcb.getFactory();
        if (fb == null) {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[FactoryRule]{" +
                                           digester.getMatch() +
                                           "} New " + CLASS_NAME);
            }
            Class clazz =
                digester.getClassLoader().loadClass(CLASS_NAME);
            fb = (FactoryBean) clazz.newInstance();
            fcb.setFactory(fb);
        } else {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[FactoryRule]{" +
                                           digester.getMatch() +
                                           "} Old " + CLASS_NAME);
            }
        }
        digester.push(fb);

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
     * <p>Pop the <code>FactoryBean</code> off the top of the stack.</p>
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

        Object top = digester.pop();
        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[FactoryRule]{" +
                                       digester.getMatch() +
                                       "} Pop " + top.getClass());
        }
        if (!CLASS_NAME.equals(top.getClass().getName())) {
            throw new IllegalStateException("Popped object is not a " +
                                            CLASS_NAME + " instance");
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

        StringBuffer sb = new StringBuffer("FactoryRule[className=");
        sb.append(CLASS_NAME);
        sb.append("]");
        return (sb.toString());

    }


}
