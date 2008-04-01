/*
 * $Id: FacesContext.java,v 1.2 2002/03/16 00:09:02 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import javax.servlet.ServletRequest;
import java.util.Locale;

/**
 * The class which defines an object representing all contextual
 * information required for processing a Faces request.
 * PENDING(aim): all EventContext & RenderContext params should
 * be replaced with this class in the next rev.
 */
public class FacesContext {

    protected RenderContext renderContext;
    protected EventContext eventContext;

    public FacesContext(RenderContext rc, EventContext ec) {
	renderContext = rc;
	eventContext = ec;
    }

    public RenderContext getRenderContext() {
	return renderContext;
    }

    public EventContext getEventContext() {
	return eventContext;
    }

}

