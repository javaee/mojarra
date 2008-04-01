/*
 * $Id: EventContextFactoryImpl.java,v 1.1 2002/01/11 20:05:59 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// EventContextFactoryImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.faces.EventContext;
import javax.faces.FacesException;
import javax.faces.RenderContext;

/**
 *
 *  <B>EventContextFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: EventContextFactoryImpl.java,v 1.1 2002/01/11 20:05:59 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class EventContextFactoryImpl extends EventContextFactory
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

public EventContextFactoryImpl()
{
    super();
}

//
// Methods from EventContextFactory
//

//
// Class methods
//

public EventContext newEventContext(RenderContext renderContext,
				    ServletRequest request,
				    ServletResponse response) throws FacesException {
    ParameterCheck.nonNull(renderContext);
    ParameterCheck.nonNull(request);
    ParameterCheck.nonNull(response);
    EventContext result = new EventContextImpl(renderContext, request, 
					       response);
    return result;
}

//
// General Methods
//

} // end of class EventContextFactoryImpl
