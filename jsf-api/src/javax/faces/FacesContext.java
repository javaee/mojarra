/*
 * $Id: FacesContext.java,v 1.1 2002/03/13 17:59:32 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;


/**

 * TEMPORARY Class to ease migration to uniform FacesContext class.


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

