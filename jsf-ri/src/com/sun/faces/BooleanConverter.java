/*
 * $Id: BooleanConverter.java,v 1.2 2002/03/15 20:58:00 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.Converter;
import javax.faces.ValidationException;
import javax.faces.UIComponent;
import javax.faces.EventContext;

/**
 *
 *  <B>BooleanConverter</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: BooleanConverter.java,v 1.2 2002/03/15 20:58:00 jvisvanathan Exp $
 *
 * @see javax.faces.Converter
 *
 */

public class BooleanConverter implements Converter{

    public Object convertStringToObject(EventContext ctx,
                                       UIComponent component,
                                       String componentValue)
            throws ValidationException {
        return Boolean.valueOf(componentValue);
   }

   public String convertObjectToString(EventContext ctx,
                                       UIComponent component,
                                       Object modelValue)
           throws ValidationException {

       ParameterCheck.nonNull(modelValue);
       return modelValue.toString();
   }

}
