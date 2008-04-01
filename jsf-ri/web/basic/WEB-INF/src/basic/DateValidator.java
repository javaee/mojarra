/*
 * $Id: DateValidator.java,v 1.3 2002/04/05 19:41:22 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package basic;

import javax.faces.Converter;
import javax.faces.ValidationException;
import javax.faces.UIComponent;
import javax.faces.FacesContext;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;

public class DateValidator implements Converter {

    public Object convertStringToObject(FacesContext ctx,
                                       UIComponent component,
                                       String componentValue)
            throws ValidationException {

        javax.faces.MessageList msgList = ctx.getMessageList();
        Date date = null;
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        if ( componentValue == null ) {
            msgList.addMessage("MSG0002", component.getId(), componentValue);
            throw new ValidationException ( "" ); 
        }
        try {
            date = df.parse( componentValue);
        } catch ( ParseException pe ) {
             msgList.addMessage("MSG0002", component.getId(), componentValue);
            throw new ValidationException ( "" ); 
        }
        if ( date.after(new Date()) ) {
            msgList.addMessage("MSG0008", component.getId(), componentValue);
            throw new ValidationException ( "" ); 
        } 
        return date;

    }

   public String convertObjectToString(FacesContext ctx,
                                       UIComponent component,
                                       Object modelValue)
           throws ValidationException {
      return null;

   }

}
