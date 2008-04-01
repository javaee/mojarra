/*
 * $Id: TestRequestEventHandler.java,v 1.1 2002/06/24 04:18:16 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.RequestEventHandler;


/**
 * <p>Test implementation of {@link RequestEventHandler}.</p>
 */

public class TestRequestEventHandler extends RequestEventHandler {

    public boolean processEvent(FacesContext context, UIComponent component,
                                FacesEvent event) {

        return (false); // No action taken

    }

}
