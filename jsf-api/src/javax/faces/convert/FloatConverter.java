/*
 * $Id: FloatConverter.java,v 1.14 2005/03/11 21:05:26 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.convert;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p>{@link Converter} implementation for <code>java.lang.Float</code>
 * (and float primitive) values.</p>
 */

public class FloatConverter implements Converter {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard converter id for this converter.</p>
     */
    public static final String CONVERTER_ID = "javax.faces.Float";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion to <code>Float</code> fails.  The message format
     * string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the unconverted value.</li>
     * <li><code>{1}</code> replaced by an example value.</li>
     * <li><code>{2}</code> replaced by a <code>String</code> whose value
     *   is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String FLOAT_ID =
        "javax.faces.converter.FloatConverter.FLOAT";
                                                                                
    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     *  the conversion of the <code>Float</code> value to
     *  <code>String</code> fails.   The message format string for this message
     *  may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> relaced by the unconverted value.</li>
     * <li><code>{1}</code> replaced by a <code>String</code> whose value
     *   is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String STRING_ID =
        "javax.faces.converter.STRING";


    // ------------------------------------------------------- Converter Methods


    /**
     * @exception ConverterException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}
     */ 
    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) {

        if (context == null || component == null) {
            throw new NullPointerException();
        }
        
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return (null);
        }
        value = value.trim();
        if (value.length() < 1) {
            return (null);
        }
        
        try {
            return (Float.valueOf(value));
        } catch (NumberFormatException nfe) {
            throw new ConverterException(MessageFactory.getMessage(
                context, FLOAT_ID, new Object[] {value, "2000000000", 
                     MessageFactory.getLabel(context, component)}));
        } catch (Exception e) {
            throw new ConverterException(e);
        }
    }

    /**
     * @exception ConverterException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}
     */ 
    public String getAsString(FacesContext context, UIComponent component,
                              Object value) {

        if (context == null || component == null) {
            throw new NullPointerException();
        }
        
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        // If the incoming value is still a string, play nice
        // and return the value unmodified
        if (value instanceof String) {
            return (String) value;
        }

        try {
            return (Float.toString(((Float) value).floatValue()));
        } catch (Exception e) {
            throw new ConverterException(MessageFactory.getMessage(
                context, STRING_ID, new Object[] {value, 
                     MessageFactory.getLabel(context, component)}), e);
        }
    }
}
