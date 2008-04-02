/*
 * $Id: MockMessageResources.java,v 1.1 2003/08/28 21:08:59 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.util.HashMap;
import java.util.Map;
import javax.faces.application.Message;
import javax.faces.application.MessageImpl;
import javax.faces.application.MessageResources;
import javax.faces.context.FacesContext;


public class MockMessageResources extends MessageResources {


    // ------------------------------------------------------ Instance Variables


    // Message instances for all locales, keyed by message id
    private Map messages = new HashMap();


    // ---------------------------------------------------------- Public Methods


    public void addMessage(String key, String message) {
        messages.put(key,
                     new MessageImpl(Message.SEVERITY_ERROR, message, null));
    }


    // ------------------------------------------------ MessageResources Methods


    public Message getMessage(FacesContext context, String messageId) {
        return ((Message) messages.get(messageId));
    }


    public Message getMessage(FacesContext context, String messageId,
                              Object params[]) {
        throw new UnsupportedOperationException();
    }


    public Message getMessage(FacesContext context, String messageId,
                              Object param0) {
        return (getMessage(context, messageId,
                           new Object[] { param0 }));
    }


    public Message getMessage(FacesContext context, String messageId,
                              Object param0, Object param1) {
        return (getMessage(context, messageId,
                           new Object[] { param0, param1 }));
    }


    public Message getMessage(FacesContext context, String messageId,
                              Object param0, Object param1,
                              Object param2) {
        return (getMessage(context, messageId,
                           new Object[] { param0, param1, param2 }));
    }


    public Message getMessage(FacesContext context, String messageId,
                              Object param0, Object param1,
                              Object param2, Object param3) {
        return (getMessage(context, messageId,
                           new Object[] { param0, param1, param2, param3 }));
    }



}
