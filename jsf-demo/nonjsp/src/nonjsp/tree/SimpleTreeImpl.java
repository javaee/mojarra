/*
 * $Id: SimpleTreeImpl.java,v 1.2 2003/02/21 23:45:58 ofung Exp $
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

// SimpleTreeImpl.java

package nonjsp.tree;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.tree.Tree;
import javax.faces.FactoryFinder;
import javax.faces.render.RenderKitFactory;
import javax.faces.context.FacesContext;

import nonjsp.util.RIConstants;

/**
 *
 * <B>SimpleTreeImpl</B> is a class ...
 *
 * Copy of com.sun.faces.tree.SimpleTreeImpl in order to remove
 * demo dependancy on RI.
 *
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SimpleTreeImpl.java,v 1.2 2003/02/21 23:45:58 ofung Exp $
 * 
 * @see	com.sun.faces.tree.SimpleTreeImpl
 * @see	javax.faces.tree.Tree
 *
 */

public class SimpleTreeImpl extends Tree 
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

protected String treeId = null;

// Relationship Instance Variables

protected String renderKitId = null;
protected UIComponent root = null;

//
// Constructors and Initializers    
//
public SimpleTreeImpl() {
    super();
}

/**

* PRECONDITION: the ServletContext has been initialized with all the
* required factories.

*/ 

public SimpleTreeImpl(FacesContext facesContext, String newTreeId) {
    this(facesContext, null, newTreeId);
}    

public SimpleTreeImpl(FacesContext context, UIComponent newRoot, 
		   String newTreeId)
{
    super();
    ParameterCheck.nonNull(context);

    if ( newRoot == null ) {
        newRoot = new UINamingContainer() {
        public String getComponentType() { return "root"; }
        };
    }
    setRoot(newRoot);
    setTreeId(newTreeId);
    setRenderKitId(RenderKitFactory.DEFAULT_RENDER_KIT);
}

//
// Class methods
//

//
// General Methods
//

void setRoot(UIComponent newRoot)
{
    ParameterCheck.nonNull(newRoot);
    root = newRoot;
}

void setTreeId(String newTreeId)
{
   ParameterCheck.nonNull(newTreeId);
   treeId = newTreeId;
}

//
// Methods from Tree
//

public String getRenderKitId()
{
    return renderKitId;
}

public void setRenderKitId(String newRenderKitId)
{
    ParameterCheck.nonNull(newRenderKitId);
    renderKitId = newRenderKitId;
}

public UIComponent getRoot()
{
    return root;
}
 
public String getTreeId()
{
    return treeId;
}

} // end of class SimpleTreeImpl
