/*
 * $Id: Converter.java,v 1.2 2002/03/15 20:49:21 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * The interface for implementing an object which performs
 * type conversion (in both directions) between java.lang.String
 * and a particular Java type.  
 * <p>
 * A typical converter class would be geared towards a particular 
 * Java type.  For example, a class named <code>DateConverter</code>
 * would convert values between java.lang.String and java.util.Date.
 * <p>
 * This type conversion is required when the UI component contains
 * a local value which is stored as a java.lang.String, but the 
 * application data object (model) associated with that component
 * requires the value in a different Java representation (typically
 * a different type, but could be a different java.lang.String
 * representation).
 * <p>
 * A converter should be implemented as a stateless object which
 * serves type conversion requests based on the faces context,
 * UI component instance, and value passed in as parameters to
 * the conversion methods.   A converter may use information
 * contained in the context, such as Locale, or UI component to
 * perform the conversion.
 * <p>
 * The process of converting java.lang.String UI component values
 * to the UI component's model type occurs as the first step in
 * component-level validation.  The converse, converting the UI
 * component's model type into a java.lang.String local value
 * occurs when the UI component 'pulls' the model value into its
 * local value (this often happens lazily, at render time).
 * <p>
 * At the time conversion is performed (in either direction), the
 * converter is obtained in the following way:
 *     
 *
 * @see UIComponent#getConverter
 */ 
public interface Converter {

    /**
     * Converts the String value of the specified component
     * to an object of the type specified by this converter.
     * @param 
     *   ctx the EventContext object used to process the current request
     * @param component 
     *   the UIComponent whose value is being converted
     * @param componentValue
     *   the String containing the value of the component
     * @throws ValidationException 
     *   if the converter was unable to convert the value  
     * @return Object containing the converted value
     */
    Object convertStringToObject(EventContext ctx,
                                       UIComponent component,
                                       String componentValue)
      throws ValidationException;

    /**
     * Converts the object value of this converter's type
     * to a String value for the specified component.
     * @param ctx 
     *   the EventContext object used to process the current request
     * @param component 
     *   the UIComponent whose value is being converted
     * @param modelValue
     *   the Object containing the value
     * @throws ValidationException 
     *   if the converter was unable to convert the value  
     * @return String containing the converted value
     */
    String convertObjectToString(EventContext ctx,
                                       UIComponent component,
                                       Object modelValue)
      throws ValidationException;

}
