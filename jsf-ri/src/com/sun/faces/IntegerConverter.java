/*
 * $Id: IntegerConverter.java,v 1.1 2002/03/08 00:24:49 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

import javax.faces.Converter;
import javax.faces.ValidationException;
import javax.faces.UIComponent;
import javax.faces.RenderContext;

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
 * @version $Id: IntegerConverter.java,v 1.1 2002/03/08 00:24:49 jvisvanathan Exp $
 *
 * @see javax.faces.Converter
 *
 */

public class IntegerConverter implements Converter{

    // PENDING ( visvan ) replace with FacesContext
    public Object convertStringToObject(RenderContext ctx,
                                       UIComponent component,
                                       String componentValue)
            throws ValidationException {

        Integer intValue = null;

       // ParameterCheck.nonNull(componentValue);
        try {
            intValue = Integer.valueOf( componentValue);
        } catch ( NumberFormatException nfe ) {
            throw new ValidationException ( "Could not convert " + 
                    componentValue + " to Integer ");
        }
        return intValue;
    }

   public String convertObjectToString(RenderContext ctx,
                                       UIComponent component,
                                       Object modelValue)
           throws ValidationException {

       ParameterCheck.nonNull(modelValue); 
       return modelValue.toString();
   }

}
