/*
 * $Id: FacesTag.java,v 1.11 2002/06/10 19:12:44 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesTag.java

package com.sun.faces.taglib;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.ServletRequest;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.FacesException;

import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: FacesTag.java,v 1.11 2002/06/10 19:12:44 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public abstract class FacesTag extends javax.faces.webapp.FacesTag
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

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public FacesTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//

    /**

    * PENDING(edburns): for some VERY strange reason, sometimes the id
    * gets set with double double quotes, even though the jsp has single
    * double quotes.  WHAAAAT!

    */ 
    public void setId(String newId) { 
	int i = 0;

	if (-1 != newId.indexOf("\"")) {
	    id = newId.substring(1, newId.length() - 1);
	}
	else {
	    id = newId;
	}
    }
    
    public String getId() {
	return id;
    }



//
// General Methods
//

/**

* This is implemented by faces subclasses to allow globally turing off
* the render kit.

*/

public abstract String getLocalRendererType();

//
// Methods from Superclass
// 

public final String getRendererType()
{
    String disableRenderers =System.getProperty(RIConstants.DISABLE_RENDERERS);
    
    if (null != disableRenderers &&
	disableRenderers.equals(RIConstants.DISABLE_RENDERERS)) {
	return null;
    }
    
    return getLocalRendererType();
}
	

// 
// Methods From TagSupport
//

} // end of class FacesTag
