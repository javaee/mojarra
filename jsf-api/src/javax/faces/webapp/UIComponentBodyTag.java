/*
 * $Id: UIComponentBodyTag.java,v 1.5 2005/04/21 18:55:31 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


/**
 * <p><strong>UIComponentBodyTag</strong> is a base class for all JSP custom
 * actions, related to a UIComponent, that need to process their tag bodies.
 * </p>
 *
 * @deprecated All component tags now implement <code>BodyTag</code>.
 * This class has been replaced by {@link UIComponentELTag}.
 */

public abstract class UIComponentBodyTag extends UIComponentTag {

    // remove all methods since UIComponentTag is now a body tag.

}
