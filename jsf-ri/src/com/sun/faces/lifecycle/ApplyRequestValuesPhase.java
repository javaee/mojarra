/*
 * $Id: ApplyRequestValuesPhase.java,v 1.12 2003/09/24 23:16:24 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ApplyRequestValuesPhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;

import javax.faces.FacesException;
import javax.faces.event.PhaseId;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import java.io.IOException;

/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: ApplyRequestValuesPhase.java,v 1.12 2003/09/24 23:16:24 horwat Exp $
 * 
 */

public class ApplyRequestValuesPhase extends Phase {
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

    public ApplyRequestValuesPhase() {
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

    public PhaseId getId() {
        return PhaseId.APPLY_REQUEST_VALUES;
    }

    public void execute(FacesContext facesContext) throws FacesException {

        UIComponent component = facesContext.getViewRoot();
        Assert.assert_it(null != component);

        try {
            component.processDecodes(facesContext);
        } catch (RuntimeException re) {
            facesContext.renderResponse();
            throw re;
        }
    }

// The testcase for this class is TestApplyRequestValuesPhase.java


} // end of class ApplyRequestValuesPhase
