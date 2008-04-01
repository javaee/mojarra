/*
 * $Id: EventDispatcherFactory.java,v 1.1 2001/11/28 02:32:11 aim Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */
package javax.faces;

import java.util.EventObject;

/**
 * Static factory class used to serve up an appropriate EventDispatcher
 * object given a particular target event type.
 */
public abstract class EventDispatcherFactory {
    /**
     * @return EventDispatcher object which should be used to dispatch
     *         the specified event
     */
    public static EventDispatcher newInstance(EventObject e) {
        return null;
    }
}
