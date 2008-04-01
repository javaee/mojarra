/*
 * $Id: Converter.java,v 1.1 2002/03/08 00:22:07 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

public interface Converter {

    // PENDING ( visvan ) replace with FacesContext
    public Object convertStringToObject(RenderContext ctx,
                                       UIComponent component,
                                       String componentValue)
      throws ValidationException;

    public String convertObjectToString(RenderContext ctx,
                                       UIComponent component,
                                       Object modelValue)
      throws ValidationException;

}
