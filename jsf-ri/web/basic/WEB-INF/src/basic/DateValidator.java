/*
 * $Id: DateValidator.java,v 1.1 2002/03/08 00:24:50 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package basic;

import javax.faces.Converter;
import javax.faces.ValidationException;
import javax.faces.UIComponent;
import javax.faces.RenderContext;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;

public class DateValidator implements Converter {

    public Object convertStringToObject(RenderContext ctx,
                                       UIComponent component,
                                       String componentValue)
            throws ValidationException {

        Date date = null;
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
        if ( date.after(new Date()) ) {
            throw new ValidationException ( "Date cannot be before Today's date " ); 
        } 
        return date;

    }

   public String convertObjectToString(RenderContext ctx,
                                       UIComponent component,
                                       Object modelValue)
           throws ValidationException {
      return null;

   }

}
