/*
 * $Id: ApplyRequestValuesPhase.java,v 1.5 2003/01/17 18:07:14 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ApplyRequestValuesPhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.context.FacesContextImpl;

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
 * @version $Id: ApplyRequestValuesPhase.java,v 1.5 2003/01/17 18:07:14 rkitain Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Lifecycle#APPLY_REQUEST_VALUES_PHASE
 *
 */

public class ApplyRequestValuesPhase extends GenericPhaseImpl {
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Relationship Instance Variables

//
// Constructors and Genericializers    
//

    public ApplyRequestValuesPhase( Lifecycle newDriver, int newId) {
        super(newDriver, newId);
    }

//
// Class methods
//

//
// General Methods
//

//
// Methods from Phase
//

    public int execute(FacesContext facesContext) throws FacesException {

        UIComponent component = 
            (UIComponent)facesContext.getTree().getRoot();
        Assert.assert_it(null != component);

        try {
            component.processDecodes(facesContext);
            if (((FacesContextImpl)facesContext).getRenderResponse()) {
                return Phase.GOTO_RENDER;
            }
        } catch (IOException e) {
            return Phase.GOTO_RENDER;
        }
        return Phase.GOTO_NEXT;
    }





// The testcase for this class is TestApplyRequestValuesPhase.java


} // end of class ApplyRequestValuesPhase
