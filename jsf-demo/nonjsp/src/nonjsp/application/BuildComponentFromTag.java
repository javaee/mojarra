/*
 * $Id: BuildComponentFromTag.java,v 1.1 2003/09/08 19:31:18 horwat Exp $
 */

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

// BuildComponentFromTag.java

package nonjsp.application;

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
 * Has the same scope as the ViewEngine instance.  The ViewEngine has a
 * BuildComponentFromTag instance. <P>
 *
 * @version $Id: BuildComponentFromTag.java,v 1.1 2003/09/08 19:31:18 horwat Exp $
 *
 */

public interface BuildComponentFromTag {

    public UIComponent createComponentForTag(String shortTagName);

    public boolean tagHasComponent(String shortTagName);

    public boolean isNestedComponentTag(String shortTagName);

    public void handleNestedComponentTag(UIComponent parent, 
				         String shortTagName, Attributes attrs);
    
    public void applyAttributesToComponentInstance(UIComponent child, 
					           Attributes attrs);

} // end of interface BuildComponentFromTag

