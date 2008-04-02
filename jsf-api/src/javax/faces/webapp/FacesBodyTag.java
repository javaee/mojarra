/*
 * $Id: FacesBodyTag.java,v 1.7 2003/05/02 05:04:54 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;


/**
 * <p><strong>FacesBodyTag</strong> is the previous name for what is now known
 * as {@link UIComponentBodyTag}.  Applications should migrate to using the new
 * class name immediately.</p>
 *
 * @deprecated Use {@link UIComponentBodyTag} instead.
 */

public abstract class FacesBodyTag extends UIComponentBodyTag {
}
