/*
 * $Id: FacesConfigRule.java,v 1.2 2004/01/27 20:13:57 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.rules;


import com.sun.faces.config.beans.FacesConfigBean;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;


/**
 * <p>Digester rule for the <code>&lt;faces-config&gt;</code> element.</p>
 */

public class FacesConfigRule extends Rule {


    private static final String CLASS_NAME =
        "com.sun.faces.config.beans.FacesConfigBean";


    private boolean pushed = false;


    // ------------------------------------------------------------ Rule Methods


    /**
     * <p>Create an instance of <code>FacesConfigBean</code> and push it
     * on to the object statck.</p>
     *
     * @param namespace the namespace URI of the matching element, or an 
     *   empty string if the parser is not namespace aware or the element has
     *   no namespace
     * @param name the local name if the parser is namespace aware, or just 
     *   the element name otherwise
     * @param attributes The attribute list of this element
     *
     * @exception IllegalStateException if there is anything already on the
     *  object stack
     */
    public void begin(String namespace, String name,
                      Attributes attributes) throws Exception {

        try {
            if ((FacesConfigBean) digester.peek() == null) {
                pushed = true;
};
        } catch (Exception e) {
            pushed = true;
        }

        if (pushed) {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[FacesConfigRule]{" +
                                           digester.getMatch() +
                                           "} New " + CLASS_NAME);
            }
            Class clazz = 
                digester.getClassLoader().loadClass(CLASS_NAME);
            Object instance = clazz.newInstance();
            digester.push(instance);
        } else {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[FacesConfigRule]{" +
                                           digester.getMatch() +
                                           "} Top " + CLASS_NAME);
            }
        }

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
     * <p>Pop the <code>FacesConfigBean</code> off the top of the stack.</p>
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

        if (pushed) {
            Object top = digester.pop();
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[FacesConfigRule]{" +
                                           digester.getMatch() +
                                           "} Pop " + top.getClass());
            }
            if (!CLASS_NAME.equals(top.getClass().getName())) {
                throw new IllegalStateException("Popped object is not a " +
                                                CLASS_NAME + " instance");
            }
        } else {
            if (digester.getLogger().isDebugEnabled()) {
                digester.getLogger().debug("[FacesConfigRule]{" +
                                           digester.getMatch() +
                                           "} Top " + CLASS_NAME);
            }
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

        StringBuffer sb = new StringBuffer("FacesConfigRule[className=");
        sb.append(CLASS_NAME);
        sb.append("]");
        return (sb.toString());

    }


}
