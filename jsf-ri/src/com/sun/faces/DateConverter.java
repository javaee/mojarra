/*
 * $Id: DateConverter.java,v 1.1 2002/03/08 00:24:48 jvisvanathan Exp $
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

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;

/**
 *
 *  <B>DateConverter</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: DateConverter.java,v 1.1 2002/03/08 00:24:48 jvisvanathan Exp $
 *
 * @see javax.faces.Converter
 *
 */

public class DateConverter implements Converter {

    public Object convertStringToObject(RenderContext ctx,
                                       UIComponent component,
                                       String componentValue)
            throws ValidationException {

        Date date = null;
       
        // PENDING ( visvan ) this formats the date for the default locale.
        // we should be getting the locale from the renderContext ??
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        if ( componentValue == null ) {
            throw new ValidationException ( "Value cannot be empty " ); 
        }    
        try {
            date = df.parse( componentValue);
        } catch ( ParseException pe ) {
            throw new ValidationException ( "Could not convert " + 
                    componentValue + " to date ");
        }  
        return date;
    }

   public String convertObjectToString(RenderContext ctx,
                                       UIComponent component,
                                       Object modelValue)
           throws ValidationException {

      ParameterCheck.nonNull(modelValue);
      DateFormat df = DateFormat.getDateInstance();
      return df.format( modelValue);
   }

}
