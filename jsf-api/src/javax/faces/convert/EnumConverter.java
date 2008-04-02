/*
 * $Id: EnumConverter.java,v 1.1 2006/03/07 21:02:45 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.convert;


import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p>{@link Converter} implementation for <code>java.lang.Enum</code>
 * (and enum primitive) values.</p>
 * 
 * @since 1.2
 */

public class EnumConverter implements Converter, StateHolder {
    
    // for StateHolder
    public EnumConverter() {
        
    }
    
    public EnumConverter(Class targetClass) {
        this.targetClass = targetClass;
    }

    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard converter id for this converter.</p>
     */
    public static final String CONVERTER_ID = "javax.faces.Enum";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion to <code>Enum</code> fails.  The message format
     * string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the unconverted value.</li>
     * <li><code>{1}</code> replaced by one of the enum constants or the empty
     * string if none can be found.</li>
     * <li><code>{2}</code> replaced by a <code>String</code> whose value
     *   is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String ENUM_ID =
        "javax.faces.converter.EnumConverter.ENUM";
    
    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion to <code>Enum</code> fails and no target class has been
     * provided.  The message format
     * string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the unconverted value.</li>
     * <li><code>{1}</code> replaced by a <code>String</code> whose value
     *   is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String ENUM_NO_CLASS_ID =
        "javax.faces.converter.EnumConverter.ENUM_NO_CLASS";
                                                                               
    // ----------------------------------------------------- Converter Methods
    
    private Class targetClass;


    /**
     * <p>Convert the argument <code>value</code> to one of the enum
     * constants of the class provided in our constructor.  If a target
     * class argument has been provided to the constructor for this
     * instance, call its <code>getEnumConstants()</code> method.  For
     * each element in the list of constants, call
     * <code>toString()</code> and compare the string with the argument
     * <code>value</code>.  If they are equal, return the current
     * element as the <code>Object</code> value.  If none of the
     * elements yield a match in this manner, or the length of the list
     * returned from <code>getEnumConstants()</code> is zero,
     * throw a {@link ConverterException} containing the {@link
     * #ENUM_ID} message with proper parameters.  If no target class
     * argument has been provided to the constructor of this instance,
     * throw a <code>ConverterException</code> containing the {@link
     * #ENUM_NO_CLASS_ID} message with proper parameters.</p>
     *
     * @param context the <code>FacesContext</code> for this request.
     *
     * @param component the <code>UIComponent</code> to which this value
     * will be applied.
     *
     * @param value the String <code>value</code> to be converted to
     * <code>Object</code>.
     *
     * @throws ConverterException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
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
        Object result = null;
        
        if(null != targetClass) {
            Object enumConstants[] = targetClass.getEnumConstants();
            String curName = null;
            if (null != enumConstants) {
                for (Object cur : enumConstants) {
                    if (null != (curName = cur.toString())) {
                        if (curName.equals(value)) {
                            result = cur;
                            break;
                        }
                    }
                }
            }
            if (null == result) {
                curName = (null != curName) ? curName : "";
                throw new ConverterException(MessageFactory.getMessage(
                        context, ENUM_ID, new Object[] {value, curName,
                        MessageFactory.getLabel(context, component)}));
            }
        }
        return result;
    }

    /**
     * <p>Convert the enum constant given by the <code>value</code>
     * argument into a String.  If a target class argument has been
     * provided to the constructor for this instance, call its
     * <code>getEnumConstants()</code> method.  For each element in the
     * list of constants, test for equality between the current element
     * and the argument <code>value</code>.  If they are equal, call
     * <code>toString()</code> on the current element and return it.  If
     * none of the elements yield a match in this manner, or the length
     * of the list returned from <code>getEnumConstants()</code> is
     * zero, simply call <code>toString()</code> on the argument
     * <code>value</code> and return it.</p>
     *
     * @throws ConverterException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
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
        String result = null;
        
        if (null != targetClass) {
            Object enumConstants[] = targetClass.getEnumConstants();
            for (Object cur : enumConstants) {
                if(value.equals(cur)) {
                    result = cur.toString();
                }
            }
        }

	result = value.toString();
        
        return result;
    }
    
    // ----------------------------------------------------------- StateHolder

    public void restoreState(FacesContext facesContext, Object object) {
        this.targetClass = (Class) object;
    }

    public Object saveState(FacesContext facesContext) {
        return this.targetClass;
    }

    private transient boolean isTransient = false;
    
    public void setTransient(boolean b) {
        isTransient = b;
    }

    public boolean isTransient() {
        return isTransient;
    }

}
