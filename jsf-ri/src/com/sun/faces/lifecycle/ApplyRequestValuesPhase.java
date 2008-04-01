/*
 * $Id: ApplyRequestValuesPhase.java,v 1.1 2002/06/01 00:58:21 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ApplyRequestValuesPhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;

import java.io.IOException;

/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: ApplyRequestValuesPhase.java,v 1.1 2002/06/01 00:58:21 eburns Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Lifecycle#APPLY_REQUEST_VALUES_PHASE
 *
 */

public class ApplyRequestValuesPhase extends GenericPhaseImpl
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
// Constructors and Genericializers    
//

public ApplyRequestValuesPhase(Lifecycle newDriver, int newId)
{
    super(newDriver, newId, 
	  new LifecycleCallback() {
	      public int takeActionOnComponent(FacesContext context,
					       UIComponent comp) throws FacesException {
		  String rendererType = comp.getRendererType();
		  
		  if (null == rendererType) {
		      try {
			  comp.decode(context);
		      }
		      catch (IOException e) {
			  throw new FacesException("Can't decode: " + 
						   comp.getComponentId(),
						   e);
		      }
		  }
		  else {
		      RenderKit renderKit = 
			  context.getRequestTree().getRenderKit();
		      Renderer renderer = 
			  renderKit.getRenderer(rendererType);
		      if (null == renderer) {
			  throw new FacesException("Can't get renderer"+
						   " for: " + 
						   comp.getComponentId());
		      }
		      try {
			  renderer.decode(context, comp);
		      }
		      catch (IOException e) {
			  throw new FacesException("Can't decode: " + 
						   comp.getComponentId(),
						   e);
		      }
		  }
		  return Phase.GOTO_NEXT;
	      }
	  });
}

//
// Class methods
//

//
// General Methods
//





// The testcase for this class is TestApplyRequestValuesPhase.java


} // end of class ApplyRequestValuesPhase
