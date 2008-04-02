/*
 * $Id: NullValueRule.java,v 1.3 2004/02/04 23:46:23 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config.rules;


import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;
import com.sun.faces.config.beans.NullValueHolder;
import com.sun.faces.config.beans.FacesConfigBean;


/**
 * <p>Digester rule for the <code>&lt;null-value&gt;</code> element.</p>
 */

public class NullValueRule extends Rule {


    // ------------------------------------------------------------ Rule Methods


    /**
     * <p>Set the <code>nullValue</code> property to null on the
     * <code>NullValueHolder</code> at the top of the stack.</p>
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

        NullValueHolder nvh = null;
        try {
            nvh = (NullValueHolder) digester.peek();
        } catch (Exception e) {
            throw new IllegalStateException
                ("No parent NullValueHolder on object stack");
        }
        nvh.setNullValue(true);
        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[NullValueRule]{" +
                                       digester.getMatch() +
                                       "} Set");
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
     * <p>No end processing is required.</p>
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
    }


    /**
     * <p>No finish processing is required.</p>
     *
     */
    public void finish() throws Exception {
    }


    // ---------------------------------------------------------- Public Methods


    public String toString() {

        StringBuffer sb = new StringBuffer("NullValueRule[]");
        return (sb.toString());

    }


}
