/*
 * $Id: BuildComponentFromTag.java,v 1.1 2003/02/13 23:34:26 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// BuildComponentFromTag.java

package nonjsp.tree;

import javax.faces.component.UIComponent;
import org.xml.sax.Attributes;

/**
 *
 *  An instance of this class knows how to build a UIComponent instance
 *  from a JSP tag.  This allows locating this knowledge near the tag
 *  handlers.  <P>
 *
 * The implementation must be modified if the tags change. <P>
 *
 * Copy of com.sun.faces.tree.BuildComponentFromTag in order to remove
 * demo dependancy on RI.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * Has the same scope as the TreeEngine instance.  The TreeEngine has a
 * BuildComponentFromTag instance. <P>
 *
 * @version $Id: BuildComponentFromTag.java,v 1.1 2003/02/13 23:34:26 horwat Exp $
 * 
 * @see	com.sun.faces.tree.BuildComponentFromTag
 * @see	com.sun.faces.treebuilder.TreeEngine#getTreeForURI
 *
 */

public interface BuildComponentFromTag
{

public UIComponent createComponentForTag(String shortTagName);

public boolean tagHasComponent(String shortTagName);

public boolean isNestedComponentTag(String shortTagName);

public void handleNestedComponentTag(UIComponent parent, 
				     String shortTagName, Attributes attrs);
    
public void applyAttributesToComponentInstance(UIComponent child, 
					       Attributes attrs);

} // end of interface BuildComponentFromTag

