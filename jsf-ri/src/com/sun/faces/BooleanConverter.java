/*
 * $Id: BooleanConverter.java,v 1.3 2002/04/05 19:41:11 jvisvanathan Exp $
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
import javax.faces.FacesContext;

/**
 *
 *  <B>BooleanConverter</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: BooleanConverter.java,v 1.3 2002/04/05 19:41:11 jvisvanathan Exp $
 *
 * @see javax.faces.Converter
 *
 */

public class BooleanConverter implements Converter{

    public Object convertStringToObject(FacesContext ctx,
                                       UIComponent component,
                                       String componentValue)
            throws ValidationException {
        return Boolean.valueOf(componentValue);
   }

   public String convertObjectToString(FacesContext ctx,
                                       UIComponent component,
                                       Object modelValue)
           throws ValidationException {

       ParameterCheck.nonNull(modelValue);
       return modelValue.toString();
   }

}
