/*
 * $Id: LifecycleRule.java,v 1.3 2004/02/04 23:46:22 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.rules;


import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;
import com.sun.faces.config.beans.LifecycleBean;
import com.sun.faces.config.beans.FacesConfigBean;


/**
 * <p>Digester rule for the <code>&lt;lifecycle&gt;</code> element.</p>
 */

public class LifecycleRule extends Rule {


    private static final String CLASS_NAME =
        "com.sun.faces.config.beans.LifecycleBean";


    // ------------------------------------------------------------ Rule Methods


    /**
     * <p>Create or retrieve an instance of <code>LifecycleBean</code>
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
        LifecycleBean lb = fcb.getLifecycle();
        if (lb == null) {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[LifecycleRule]{" +
                                           digester.getMatch() +
                                           "} New " + CLASS_NAME);
            }
            Class clazz =
                digester.getClassLoader().loadClass(CLASS_NAME);
            lb = (LifecycleBean) clazz.newInstance();
            fcb.setLifecycle(lb);
        } else {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[LifecycleRule]{" +
                                           digester.getMatch() +
                                           "} Old " + CLASS_NAME);
            }
        }
        digester.push(lb);

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
     * <p>Pop the <code>LifecycleBean</code> off the top of the stack.</p>
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
            digester.getLogger().debug("[LifecycleRule]{" +
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

        StringBuffer sb = new StringBuffer("LifecycleRule[className=");
        sb.append(CLASS_NAME);
        sb.append("]");
        return (sb.toString());

    }


}
