/*
 * $Id: TestRequestEventHandler.java,v 1.2 2002/09/21 22:24:36 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.RequestEvent;
import javax.faces.event.RequestEventHandler;


/**
 * <p>Test implementation of {@link RequestEventHandler}.</p>
 */

public class TestRequestEventHandler extends RequestEventHandler {

    public boolean processEvent(FacesContext context, UIComponent component,
                                RequestEvent event) {

        return (true); // No action taken

    }

}
