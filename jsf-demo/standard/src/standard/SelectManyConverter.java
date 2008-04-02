/*
 * $Id: SelectManyConverter.java,v 1.2 2004/02/06 18:39:57 craigmcc Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package standard;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


public class SelectManyConverter implements Converter {


    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) {

        return (new SelectManyRegistered(value));

    }


    public String getAsString(FacesContext context, UIComponent component,
                              Object value) {

        return (((SelectManyRegistered) value).getName());

    }


}


