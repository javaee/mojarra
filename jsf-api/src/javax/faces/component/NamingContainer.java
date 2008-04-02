/*
 * $Id: NamingContainer.java,v 1.12 2004/02/26 20:30:29 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>NamingContainer</strong> is an interface that must be
 * implemented by any {@link UIComponent} that wants to be a naming
 * container.  Naming containers affect the behavior of the
 * {@link UIComponent#findComponent} and {@link UIComponent#getClientId}
 * methods;  see those methods for further information.</p>
 */

public interface NamingContainer {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The separator character used in component identifiers to demarcate
     * navigation to a child naming container.</p>
     */
    public static final char SEPARATOR_CHAR = ':';

}
