/*
 * $Id: DateConverter.java,v 1.3 2002/04/05 19:41:11 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces;

import javax.faces.Converter;
import javax.faces.ValidationException;
import javax.faces.UIComponent;
import javax.faces.FacesContext;
import javax.faces.MessageList;

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
 * @version $Id: DateConverter.java,v 1.3 2002/04/05 19:41:11 jvisvanathan Exp $
 *
 * @see javax.faces.Converter
 *
 */

public class DateConverter implements Converter {

    public Object convertStringToObject(FacesContext ctx,
                                       UIComponent component,
                                       String componentValue)
            throws ValidationException {

        Date date = null;
       
        // PENDING ( visvan ) this formats the date for the default locale.
        // we should be getting the locale from the renderContext ??
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        if ( componentValue == null ) {
            setErrorMessage(ctx, component.getId(), componentValue);
            throw new ValidationException (""); 
        }    
        try {
            date = df.parse( componentValue);
        } catch ( ParseException pe ) {
            setErrorMessage(ctx, component.getId(), componentValue);
            throw new ValidationException (""); 
        }  
        return date;
    }

   public String convertObjectToString(FacesContext ctx,
                                       UIComponent component,
                                       Object modelValue)
           throws ValidationException {

      ParameterCheck.nonNull(modelValue);
      DateFormat df = DateFormat.getDateInstance();
      return df.format( modelValue);
   }

   protected void setErrorMessage(FacesContext ctx, String compId, 
           String componentValue) {
       MessageList msgList = ctx.getMessageList();
       Assert.assert_it(msgList != null);
       msgList.addMessage("MSG0002", compId,componentValue);
   }

}
