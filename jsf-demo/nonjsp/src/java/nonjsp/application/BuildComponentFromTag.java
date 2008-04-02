/*
 * $Id: BuildComponentFromTag.java,v 1.3 2005/12/14 22:27:32 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// BuildComponentFromTag.java

package nonjsp.application;

import javax.faces.component.UIComponent;

import org.xml.sax.Attributes;

/**
 * An instance of this class knows how to build a UIComponent instance
 * from a JSP tag.  This allows locating this knowledge near the tag
 * handlers.  <P>
 * <p/>
 * The implementation must be modified if the tags change. <P>
 * <p/>
 * Copy of com.sun.faces.tree.BuildComponentFromTag in order to remove
 * demo dependancy on RI.
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 * <p/>
 * Has the same scope as the ViewEngine instance.  The ViewEngine has a
 * BuildComponentFromTag instance. <P>
 *
 * @version $Id: BuildComponentFromTag.java,v 1.3 2005/12/14 22:27:32 rlubke Exp $
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

