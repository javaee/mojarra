/*
 * $Id: UIPage.java,v 1.1 2002/03/15 23:29:21 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * Class for representing a user-interface component which acts
 * as the root for a UI component tree represented in a page. 
 */
public class UIPage extends UIComponent /*implements NamingContainer*/{

    private static String TYPE = "Page";

    /** 
     * Returns a String representing the page's type.  
     *
     * @return a String object containing &quot;Page&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

}
