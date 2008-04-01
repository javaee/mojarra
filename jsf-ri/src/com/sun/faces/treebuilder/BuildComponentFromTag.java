/*
 * $Id: BuildComponentFromTag.java,v 1.1 2002/03/19 19:25:01 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// BuildComponentFromTag.java

package com.sun.faces.treebuilder;

import javax.faces.UIComponent;
import javax.faces.RenderContext;
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
 * @version $Id: BuildComponentFromTag.java,v 1.1 2002/03/19 19:25:01 eburns Exp $
 * 
 * @see	com.sun.faces.treebuilder.TreeEngine#getTreeForURI
 *
 */

public interface BuildComponentFromTag
{

public UIComponent createComponentForTag(String shortTagName);

public boolean tagHasComponent(String shortTagName);

public boolean isNestedComponentTag(String shortTagName);

public void handleNestedComponentTag(RenderContext renderContext,
				     UIComponent parent, 
				     String shortTagName, Attributes attrs);
    
public void applyAttributesToComponentInstance(RenderContext renderContext,
					       UIComponent child, 
					       Attributes attrs);

} // end of interface BuildComponentFromTag
