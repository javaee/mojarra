/*
 * $Id: IntegerConverter.java,v 1.2 2002/03/15 20:58:00 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

import javax.faces.Converter;
import javax.faces.ValidationException;
import javax.faces.UIComponent;
import javax.faces.EventContext;
import javax.faces.MessageList;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;
/**
 *
 *  <B>IntegerConverter</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: IntegerConverter.java,v 1.2 2002/03/15 20:58:00 jvisvanathan Exp $
 *
 * @see javax.faces.Converter
 *
 */

public class IntegerConverter implements Converter{

    // PENDING ( visvan ) replace with FacesContext
    public Object convertStringToObject(EventContext ctx,
                                       UIComponent component,
                                       String componentValue)
            throws ValidationException {

        Integer intValue = null;

       // ParameterCheck.nonNull(componentValue);
        try {
            intValue = Integer.valueOf( componentValue);
        } catch ( NumberFormatException nfe ) {
            MessageList msgList = ctx.getMessageList();
            Assert.assert_it(msgList != null);
            msgList.addMessage("MSG0001", component.getId(),
                    componentValue);
            throw new ValidationException("");
        }
        return intValue;
    }

   public String convertObjectToString(EventContext ctx,
                                       UIComponent component,
                                       Object modelValue)
           throws ValidationException {

       ParameterCheck.nonNull(modelValue); 
       return modelValue.toString();
   }

}
