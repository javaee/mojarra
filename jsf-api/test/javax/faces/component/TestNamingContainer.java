/*
 * $Id: TestNamingContainer.java,v 1.1 2003/11/01 00:46:08 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p>Test {@link NamingContainer} implementation with tracing.</p>
 */

public class TestNamingContainer extends UINamingContainer {


    // ------------------------------------------------------------ Constructors


    public TestNamingContainer() {
    }


    // ---------------------------------------------------------- Public Methods


    public UIComponent findComponent(String expr) {

        trace(getId());
        trace(expr);
        return (super.findComponent(expr));

    }


    // ---------------------------------------------------- Static Trace Methods


    // Accumulated trace log
    private static StringBuffer trace = new StringBuffer();

    // Append to the current trace log (or clear if null)
    public static void trace(String text) {
        if (text == null) {
            trace.setLength(0);
        } else {
            trace.append('/');
            trace.append(text);
        }
    }

    // Retrieve the current trace log
    public static String trace() {
        return (trace.toString());
    }


}
