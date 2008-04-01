/*
 * $Id: BuildComponentFromTag.java,v 1.2 2002/04/05 19:41:20 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// BuildComponentFromTag.java

package com.sun.faces.treebuilder;

import javax.faces.UIComponent;
import javax.faces.FacesContext;
import org.xml.sax.Attributes;

/**
 *
 *  An instance of this class knows how to build a UIComponent instance
 *  from a JSP tag.  This allows locating this knowledge near the tag
 *  handlers.  <P>

 * The implementation must be modified if the tags change. <P>
 *
 * <B>Lifetime And Scope</B> <P>

 * Has the same scope as the TreeEngine instance.  The TreeEngine has a
 * BuildComponentFromTag instance. <P>
 *
 * @version $Id: BuildComponentFromTag.java,v 1.2 2002/04/05 19:41:20 jvisvanathan Exp $
 * 
 * @see	com.sun.faces.treebuilder.TreeEngine#getTreeForURI
 *
 */

public interface BuildComponentFromTag
{

public UIComponent createComponentForTag(String shortTagName);

public boolean tagHasComponent(String shortTagName);

public boolean isNestedComponentTag(String shortTagName);

public void handleNestedComponentTag(FacesContext facesContext,
				     UIComponent parent, 
				     String shortTagName, Attributes attrs);
    
public void applyAttributesToComponentInstance(FacesContext facesContext,
					       UIComponent child, 
					       Attributes attrs);

} // end of interface BuildComponentFromTag
