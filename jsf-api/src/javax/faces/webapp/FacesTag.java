/*
 * $Id: FacesTag.java,v 1.40 2003/05/02 05:04:54 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.io.IOException;
import java.util.Iterator;
import java.util.HashMap;
import javax.faces.FactoryFinder;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.tree.Tree;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;


/**
 * <p><strong>FacesTag</strong> is the previous name for what is now known as
 * {@link UIComponentTag}.  Applications should migrate to using the new
 * class name immediately.</p>
 *
 * @deprecated Use {@link UIComponentTag} instead.
 */

public abstract class FacesTag extends UIComponentTag {
}
