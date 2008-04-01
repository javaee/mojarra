/*
 * $Id: ConverterManager.java,v 1.1 2002/03/08 00:22:07 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

public abstract class ConverterManager {

    private static ConverterManager instance = null;

    public static ConverterManager getInstance() {
        return instance;
    }

    public static void setInstance(ConverterManager cm) {
        instance = cm;
    } 

    /**
     * Returns a Converter object capable to converting String objects
     * to instances of the specified class, and converting objects
     * of the specified class to java.lang.String.
     */
    public Converter getConverter(java.lang.Class c) {
        return null;
    }

    /**
     * PENDING (visvan) do we need a setConveter method ?
     * Sets the converter object which converts String objects
     * to the specified class, and objects of the specified class
     * to java.lang.String.  If a converter already exists for
     * the specified class, it is replaced.
     *
    public void setConverter(java.lang.Class c, Converter cv) {
    } */
}
