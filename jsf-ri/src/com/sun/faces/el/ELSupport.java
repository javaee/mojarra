/*
 * $Id: ELSupport.java,v 1.1 2005/05/05 20:51:22 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * @author Jacob Hookom
 */
public class ELSupport implements ELConstants
{

    protected final static ResourceBundle bundle = ResourceBundle
            .getBundle("com.sun.faces.el.impl.Messages");

    private final static ELSupport REF = new ELSupport();

    private static Byte ZERO = new Byte((byte) 0);

    public static int checkImplicitVariable(final String var)
    {
        return Arrays.binarySearch(IMPLICIT_OBJECTS, var);
    }

    /**
     * @param obj
     * @return
     */
    public static Boolean coerceToBoolean(final Object obj)
    {
        if (obj == null || "".equals(obj))
        {
            return Boolean.FALSE;
        }
        if (obj instanceof Boolean || obj.getClass() == Boolean.TYPE)
        {
            return (Boolean) obj;
        }
        if (obj instanceof String)
        {
            return Boolean.valueOf((String) obj);
        }

        throw new IllegalArgumentException(ELSupport.msg("el.error.convert",
                obj, obj.getClass(), Boolean.class));
    }

    public static Character coerceToCharacter(final Object obj)
    {
        if (obj == null || "".equals(obj))
        {
            return new Character((char) 0);
        }
        if (obj instanceof String)
        {
            return new Character(((String) obj).charAt(0));
        }
        if (isNumber(obj))
        {
            return new Character((char) ((Number) obj).shortValue());
        }
        Class objType = obj.getClass();
        if (obj instanceof Character || objType == Character.TYPE)
        {
            return (Character) obj;
        }

        throw new IllegalArgumentException(ELSupport.msg("el.error.convert",
                obj, objType, Character.class));
    }
    
    public static Number coerceToNumber(final Object obj)
    {
        if (obj == null)
        {
            return ZERO;
        }
        else if (obj instanceof Number)
        {
            return (Number) obj;
        }
        else
        {
            String str = coerceToString(obj);
            if (isStringFloat(str))
            {
                return toFloat(str);
            }
            else
            {
                return toNumber(str);
            }
        }
    }

    protected static Number coerceToNumber(final Number number, final Class type)
    {
        if (Long.TYPE == type || Long.class.equals(type))
        {
            return new Long(number.longValue());
        }
        if (Double.TYPE == type || Double.class.equals(type))
        {
            return new Double(number.doubleValue());
        }
        if (Integer.TYPE == type || Integer.class.equals(type))
        {
            return new Integer(number.intValue());
        }
        if (BigInteger.class.equals(type))
        {
            return new BigInteger(number.toString());
        }
        if (BigDecimal.class.equals(type))
        {
            return new BigDecimal(number.toString());
        }
        if (Byte.TYPE == type || Byte.class.equals(type))
        {
            return new Byte(number.byteValue());
        }
        if (Short.TYPE == type || Short.class.equals(type))
        {
            return new Short(number.shortValue());
        }
        if (Float.TYPE == type || Float.class.equals(type))
        {
            return new Float(number.floatValue());
        }
        
        throw new IllegalArgumentException(ELSupport.msg("el.error.convert",
                number, number.getClass(), type));
    }

    public static Number coerceToNumber(final Object obj, final Class type)
    {
        if (obj == null || "".equals(obj))
        {
            return coerceToNumber(ZERO, type);
        }
        if (obj instanceof String)
        {
            return coerceToNumber((String) obj, type);
        }
        if (isNumber(obj))
        {
            return coerceToNumber((Number) obj, type);
        }

        Class objType = obj.getClass();
        if (Character.class.equals(objType) || Character.TYPE == objType)
        {
            return coerceToNumber(new Short((short) ((Character) obj)
                    .charValue()), type);
        }

        throw new IllegalArgumentException(ELSupport.msg("el.error.convert",
                obj, objType, type));
    }

    protected static Number coerceToNumber(final String val, final Class type)
    {
        if (Long.TYPE == type || Long.class.equals(type))
        {
            return Long.valueOf(val);
        }
        if (Integer.TYPE == type || Short.class.equals(type))
        {
            return Integer.valueOf(val);
        }
        if (Double.TYPE == type || Double.class.equals(type))
        {
            return Double.valueOf(val);
        }
        if (BigInteger.class.equals(type))
        {
            return new BigInteger(val);
        }
        if (BigDecimal.class.equals(type))
        {
            return new BigDecimal(val);
        }
        if (Byte.TYPE == type || Byte.class.equals(type))
        {
            return Byte.valueOf(val);
        }
        if (Short.TYPE == type || Short.class.equals(type))
        {
            return Short.valueOf(val);
        }
        if (Float.TYPE == type || Float.class.equals(type))
        {
            return Float.valueOf(val);
        }
        
        throw new IllegalArgumentException(ELSupport.msg("el.error.convert",
                val, String.class, type));
    }

    /**
     * @param obj
     * @return
     */
    public static String coerceToString(final Object obj)
    {
        if (obj == null)
        {
            return null;
        }
        else if (obj instanceof String)
        {
            return (String) obj;
        }
        else
        {
            return obj.toString();
        }
    }

    public static Object coerceToType(final Object obj, final Class type)
            throws IllegalArgumentException
    {
        if (obj != null && type.isAssignableFrom(obj.getClass()))
        {
            return obj;
        }
        if (String.class.equals(type))
        {
            return coerceToString(obj);
        }
        if (isNumberType(type))
        {
            return coerceToNumber(obj, type);
        }
        if (Character.class.equals(type) || Character.TYPE == type)
        {
            return coerceToCharacter(obj);
        }
        if (Boolean.class.equals(type) || Boolean.TYPE == type)
        {
            return coerceToBoolean(obj);
        }
        return null;
    }

    /**
     * @param obj
     * @return
     */
    public static boolean containsNulls(final Object[] obj)
    {
        for (int i = 0; i < obj.length; i++)
        {
            if (obj[0] == null)
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isBigDecimalOp(final Object obj0, final Object obj1)
    {
        return (obj0 instanceof BigDecimal || obj1 instanceof BigDecimal);
    }

    public static boolean isBigIntegerOp(final Object obj0, final Object obj1)
    {
        return (obj0 instanceof BigInteger || obj1 instanceof BigInteger);
    }

    public static boolean isDoubleOp(final Object obj0, final Object obj1)
    {
        return (obj0 instanceof Double || obj1 instanceof Double
                || obj0 instanceof Float || obj1 instanceof Float
                || Double.TYPE == obj0.getClass()
                || Double.TYPE == obj1.getClass()
                || Float.TYPE == obj0.getClass() || Float.TYPE == obj1
                .getClass());
    }

    public static boolean isDoubleStringOp(final Object obj0, final Object obj1)
    {
        return (isDoubleOp(obj0, obj1)
                || (obj0 instanceof String && isStringFloat((String) obj0)) || (obj1 instanceof String && isStringFloat((String) obj1)));
    }

    public static boolean isImplicitVariable(final String var)
    {
        return Arrays.binarySearch(IMPLICIT_OBJECTS, var) >= 0;
    }

    public static boolean isLongOp(final Object obj0, final Object obj1)
    {
        return (obj0 instanceof Long || obj1 instanceof Long
                || obj0 instanceof Integer || obj1 instanceof Integer
                || obj0 instanceof Character || obj1 instanceof Character
                || obj0 instanceof Short || obj1 instanceof Short
                || obj0 instanceof Byte || obj1 instanceof Byte
                || Long.TYPE == obj0.getClass() || Long.TYPE == obj1.getClass()
                || Integer.TYPE == obj0.getClass()
                || Integer.TYPE == obj1.getClass()
                || Character.TYPE == obj0.getClass()
                || Character.TYPE == obj1.getClass()
                || Short.TYPE == obj0.getClass()
                || Short.TYPE == obj1.getClass()
                || Byte.TYPE == obj0.getClass() || Byte.TYPE == obj1.getClass());
    }

    public static boolean isNumber(final Object obj)
    {
        return (obj != null && isNumberType(obj.getClass()));
    }

    public static boolean isNumberType(final Class type)
    {
        return Number.class.isAssignableFrom(type) || type == Byte.TYPE
                || type == Short.TYPE || type == Integer.TYPE
                || type == Long.TYPE || type == Float.TYPE
                || type == Double.TYPE;
    }

    public static boolean isStringFloat(final String str)
    {
        int len = str.length();
        if (len > 1)
        {
            char c = 0;
            for (int i = 0; i < len; i++)
            {
                switch (c = str.charAt(i))
                {
                    case 'E':
                        return true;
                    case 'e':
                        return true;
                    case '.':
                        return true;
                }
            }
        }
        return false;
    }

    public static String msg(final String key)
    {
        return bundle.getString(key);
    }

    public static String msg(final String key, final Object obj0)
    {
        return msgArray(key, new Object[]
        { obj0 });
    }

    public static String msg(final String key, final Object obj0,
            final Object obj1)
    {
        return msgArray(key, new Object[]
        { obj0, obj1 });
    }

    public static String msg(final String key, final Object obj0,
            final Object obj1, final Object obj2)
    {
        return msgArray(key, new Object[]
        { obj0, obj1, obj2 });
    }

    public static String msg(final String key, final Object obj0,
            final Object obj1, final Object obj2, final Object obj3)
    {
        return msgArray(key, new Object[]
        { obj0, obj1, obj2, obj3 });
    }

    public static String msg(final String key, final Object obj0,
            final Object obj1, final Object obj2, final Object obj3,
            final Object obj4)
    {
        return msgArray(key, new Object[]
        { obj0, obj1, obj2, obj3, obj4 });
    }

    public static String msgArray(final String key, final Object[] objA)
    {
        return MessageFormat.format(bundle.getString(key), objA);
    }

    public static Number toFloat(final String value)
    {
        try
        {
            if (Double.parseDouble(value) > Double.MAX_VALUE)
            {
                return new BigDecimal(value);
            }
            else
            {
                return new Double(value);
            }
        }
        catch (NumberFormatException e0)
        {
            return new BigDecimal(value);
        }
    }

    public static Number toNumber(final String value)
    {
        try
        {
            return new Integer(Integer.parseInt(value));
        }
        catch (NumberFormatException e0)
        {
            try
            {
                return new Long(Long.parseLong(value));
            }
            catch (NumberFormatException e1)
            {
                return new BigInteger(value);
            }
        }
    }

    /**
     *  
     */
    public ELSupport()
    {
        super();
    }

}