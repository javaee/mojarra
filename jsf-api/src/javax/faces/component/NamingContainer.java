/*
 * $Id: NamingContainer.java,v 1.7 2003/09/30 22:04:37 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>NamingContainer</strong> is an interface that must be
 * implemented by any {@link UIComponent} that wants to be a naming
 * container.</p>
 */

public interface NamingContainer {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The separator character used in component identifiers to demarcate
     * navigation to a child naming container.</p>
     */
    public static final char SEPARATOR_CHAR = ':';

}
