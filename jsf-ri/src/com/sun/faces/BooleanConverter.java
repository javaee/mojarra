/*
 * $Id: BooleanConverter.java,v 1.1 2002/03/08 00:24:47 jvisvanathan Exp $
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
import javax.faces.RenderContext;

/**
 *
 *  <B>BooleanConverter</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: BooleanConverter.java,v 1.1 2002/03/08 00:24:47 jvisvanathan Exp $
 *
 * @see javax.faces.Converter
 *
 */

public class BooleanConverter implements Converter{

    public Object convertStringToObject(RenderContext ctx,
                                       UIComponent component,
                                       String componentValue)
            throws ValidationException {
        return Boolean.valueOf(componentValue);
   }

   public String convertObjectToString(RenderContext ctx,
                                       UIComponent component,
                                       Object modelValue)
           throws ValidationException {

       ParameterCheck.nonNull(modelValue);
       return modelValue.toString();
   }

}
