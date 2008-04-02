/*
 * $Id: AbstractJsfParserVisitor.java,v 1.2 2004/11/09 04:23:11 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;

/**
 * @author Jacob Hookom
 */
public abstract class AbstractJsfParserVisitor extends ELSupport implements JsfParserVisitor,
        Serializable
{
    /**
     * @param obj0
     * @param obj1
     * @return
     * @throws EvaluationException
     */
    protected static int compare(final Object obj0, final Object obj1)
            throws EvaluationException
    {
        if (equals(obj0, obj1))
        {
            return 0;
        }
        if (isBigDecimalOp(obj0, obj1))
        {
            BigDecimal bd0 = (BigDecimal) coerceToNumber(obj0,
                    BigDecimal.class);
            BigDecimal bd1 = (BigDecimal) coerceToNumber(obj1,
                    BigDecimal.class);
            return bd0.compareTo(bd1);
        }
        if (isDoubleOp(obj0, obj1))
        {
            Double d0 = (Double) coerceToNumber(obj0, Double.class);
            Double d1 = (Double) coerceToNumber(obj1, Double.class);
            return d0.compareTo(d1);
        }
        if (isBigIntegerOp(obj0, obj1))
        {
            BigInteger bi0 = (BigInteger) coerceToNumber(obj0,
                    BigInteger.class);
            BigInteger bi1 = (BigInteger) coerceToNumber(obj1,
                    BigInteger.class);
            return bi0.compareTo(bi1);
        }
        if (isLongOp(obj0, obj1))
        {
            Long l0 = (Long) coerceToNumber(obj0, Long.class);
            Long l1 = (Long) coerceToNumber(obj1, Long.class);
            return l0.compareTo(l1);
        }
        if (obj0 instanceof Comparable)
        {
            return ((Comparable) obj0).compareTo(obj1);
        }
        if (obj1 instanceof Comparable)
        {
            return -((Comparable) obj1).compareTo(obj0);
        }
        throw new EvaluationException(msg("el.error.visitor.compare",
                obj0, obj1));
    }

    /**
     * @param obj0
     * @param obj1
     * @return
     * @throws EvaluationException
     */
    protected static boolean equals(final Object obj0, final Object obj1)
            throws EvaluationException
    {
        if (obj0 == obj1)
        {
            return true;
        }
        else if (obj0 == null || obj1 == null)
        {
            return false;
        }
        else if (obj0 instanceof Boolean || obj1 instanceof Boolean)
        {
            return coerceToBoolean(obj0).equals(
                    coerceToBoolean(obj1));
        }
        if (isBigDecimalOp(obj0, obj1))
        {
            BigDecimal bd0 = (BigDecimal) coerceToNumber(obj0,
                    BigDecimal.class);
            BigDecimal bd1 = (BigDecimal) coerceToNumber(obj1,
                    BigDecimal.class);
            return bd0.equals(bd1);
        }
        if (isDoubleOp(obj0, obj1))
        {
            Double d0 = (Double) coerceToNumber(obj0, Double.class);
            Double d1 = (Double) coerceToNumber(obj1, Double.class);
            return d0.equals(d1);
        }
        if (isBigIntegerOp(obj0, obj1))
        {
            BigInteger bi0 = (BigInteger) coerceToNumber(obj0,
                    BigInteger.class);
            BigInteger bi1 = (BigInteger) coerceToNumber(obj1,
                    BigInteger.class);
            return bi0.equals(bi1);
        }
        if (isLongOp(obj0, obj1))
        {
            Long l0 = (Long) coerceToNumber(obj0, Long.class);
            Long l1 = (Long) coerceToNumber(obj1, Long.class);
            return l0.equals(l1);
        }
        else
        {
            return obj0.equals(obj1);
        }
    }

    /**
     * @param var
     * @return
     */
    protected Map resolveScope(String var)
    {
        ExternalContext ext = this.getExternalContext();

        // cycle through the scopes to find a match, if no
        // match is found, then return the requestScope
        Map map = ext.getRequestMap();
        if (!map.containsKey(var))
        {
            map = ext.getSessionMap();
            if (!map.containsKey(var))
            {
                map = ext.getApplicationMap();
                if (!map.containsKey(var))
                {
                    map = ext.getRequestMap();
                }
            }
        }

        return map;
    }

    protected FacesContext context;

    private JsfParserVisitor parent;

    private PropertyResolver propertyResolver;

    private VariableResolver variableResolver;

    private ExternalContext externalContext;

    public AbstractJsfParserVisitor(FacesContext context)
    {
        this.context = context;

        if (this.context == null)
        {
            throw new NullPointerException(msg(
                    "el.error.visitor.context", this.getClass()));
        }
    }

    /**
     * @return
     */
    protected JsfParserVisitor getEvaluationVisitor()
    {
        if (this.parent == null)
        {
            this.parent = new ValueGetVisitor(this.context);
        }
        return this.parent;
    }

    /**
     * @return
     */
    protected PropertyResolver getPropertyResolver()
    {
        if (this.propertyResolver == null)
        {
            Application app = this.context.getApplication();
            if (app == null)
            {
                throw new NullPointerException(msg(
                        "el.error.visitor.application", this.context));
            }
            this.propertyResolver = app.getPropertyResolver();
            if (this.propertyResolver == null)
            {
                throw new NullPointerException(msg(
                        "el.error.visitor.property", app));
            }
        }
        return this.propertyResolver;
    }

    /**
     * @return
     */
    protected ExternalContext getExternalContext()
    {
        if (this.externalContext == null)
        {
            this.externalContext = this.context.getExternalContext();
        }
        return this.externalContext;
    }

    /**
     * @return
     */
    protected VariableResolver getVariableResolver()
    {
        if (this.variableResolver == null)
        {
            Application app = this.context.getApplication();
            if (app == null)
            {
                throw new NullPointerException(msg(
                        "el.error.visitor.application", this.context));
            }
            this.variableResolver = app.getVariableResolver();
            if (this.variableResolver == null)
            {
                throw new NullPointerException(msg(
                        "el.error.visitor.variable", app));
            }
        }
        return this.variableResolver;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstAnd, java.lang.Object)
     */
    public Object visit(AstAnd node, Object data) throws EvaluationException
    {
        Object obj = node.jjtGetChild(0).jjtAccept(this, data);
        Boolean b = coerceToBoolean(obj);
        if (!b.booleanValue())
        {
            return b;
        }
        obj = node.jjtGetChild(1).jjtAccept(this, data);
        b = coerceToBoolean(obj);
        return b;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstChoose, java.lang.Object)
     */
    public Object visit(AstChoose node, Object data) throws EvaluationException
    {
        Object obj0 = node.jjtGetChild(0).jjtAccept(this, data);
        Boolean b0 = coerceToBoolean(obj0);
        return node.jjtGetChild((b0.booleanValue() ? 1 : 2)).jjtAccept(this,
                data);
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstComplex, java.lang.Object)
     */
    public Object visit(AstComplex node, Object data)
            throws EvaluationException
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < node.jjtGetNumChildren(); i++)
        {
            sb.append(node.jjtGetChild(i).jjtAccept(this, data));
        }
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstDiv, java.lang.Object)
     */
    public Object visit(AstDiv node, Object data) throws EvaluationException
    {
        Object obj0 = node.jjtGetChild(0).jjtAccept(this, data);
        Number num0 = coerceToNumber(obj0);
        if (num0.doubleValue() == 0.0)
        {
            return num0;
        }
        Object obj1 = node.jjtGetChild(1).jjtAccept(this, data);
        return ELArithmetic.divide(obj0, obj1);
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstEmpty, java.lang.Object)
     */
    public Object visit(AstEmpty node, Object data) throws EvaluationException
    {
        Object obj = node.jjtGetChild(0).jjtAccept(this, data);
        if (obj == null)
        {
            return Boolean.TRUE;
        }
        else if (obj instanceof String)
        {
            return Boolean.valueOf(((String) obj).length() == 0);
        }
        else if (obj instanceof Object[])
        {
            return Boolean.valueOf(((Object[]) obj).length == 0);
        }
        else if (obj instanceof Collection)
        {
            return Boolean.valueOf(((Collection) obj).isEmpty());
        }
        else if (obj instanceof Map)
        {
            return Boolean.valueOf(((Map) obj).isEmpty());
        }
        return Boolean.FALSE;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstEqual, java.lang.Object)
     */
    public Object visit(AstEqual node, Object data) throws EvaluationException
    {
        Object obj0 = node.jjtGetChild(0).jjtAccept(this, data);
        Object obj1 = node.jjtGetChild(1).jjtAccept(this, data);
        return Boolean.valueOf(equals(obj0, obj1));
    } 

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstFalse, java.lang.Object)
     */
    public Object visit(AstFalse node, Object data) throws EvaluationException
    {
        return Boolean.FALSE;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstFloat, java.lang.Object)
     */
    public Object visit(AstFloat node, Object data) throws EvaluationException
    {
        return node.getFloat();
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstGreaterThan, java.lang.Object)
     */
    public Object visit(AstGreaterThan node, Object data)
            throws EvaluationException
    {
        Object obj0 = node.jjtGetChild(0).jjtAccept(this, data);
        if (obj0 == null)
        {
            return Boolean.FALSE;
        }
        Object obj1 = node.jjtGetChild(1).jjtAccept(this, data);
        if (obj1 == null)
        {
            return Boolean.FALSE;
        }
        return (compare(obj0, obj1) > 0) ? Boolean.TRUE : Boolean.FALSE;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstGreaterThanEqual, java.lang.Object)
     */
    public Object visit(AstGreaterThanEqual node, Object data)
            throws EvaluationException
    {
        Object obj0 = node.jjtGetChild(0).jjtAccept(this, data);
        Object obj1 = node.jjtGetChild(1).jjtAccept(this, data);
        if (obj0 == obj1) return Boolean.TRUE;
        if (obj0 == null || obj1 == null) return Boolean.FALSE;
        return (compare(obj0, obj1) >= 0) ? Boolean.TRUE : Boolean.FALSE;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstLessThan, java.lang.Object)
     */
    public Object visit(AstLessThan node, Object data)
            throws EvaluationException
    {
        Object obj0 = node.jjtGetChild(0).jjtAccept(this, data);
        if (obj0 == null)
        {
            return Boolean.FALSE;
        }
        Object obj1 = node.jjtGetChild(1).jjtAccept(this, data);
        if (obj1 == null)
        {
            return Boolean.FALSE;
        }
        return (compare(obj0, obj1) < 0) ? Boolean.TRUE : Boolean.FALSE;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstLessThanEqual, java.lang.Object)
     */
    public Object visit(AstLessThanEqual node, Object data)
            throws EvaluationException
    {
        Object obj0 = node.jjtGetChild(0).jjtAccept(this, data);
        Object obj1 = node.jjtGetChild(1).jjtAccept(this, data);
        if (obj0 == obj1) return Boolean.TRUE;
        if (obj0 == null || obj1 == null) return Boolean.FALSE;
        return (compare(obj0, obj1) <= 0) ? Boolean.TRUE : Boolean.FALSE;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstMinus, java.lang.Object)
     */
    public Object visit(AstMinus node, Object data) throws EvaluationException
    {
        Object obj0 = node.jjtGetChild(0).jjtAccept(this, data);
        Number num0 = coerceToNumber(obj0);
        if (num0.doubleValue() == 0.0)
        {
            return num0;
        }
        Object obj1 = node.jjtGetChild(1).jjtAccept(this, data);
        return ELArithmetic.subtract(obj0, obj1);
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstMod, java.lang.Object)
     */
    public Object visit(AstMod node, Object data) throws EvaluationException
    {
        Object obj0 = node.jjtGetChild(0).jjtAccept(this, data);
        Number num0 = coerceToNumber(obj0);
        if (num0.doubleValue() == 0.0)
        {
            return num0;
        }
        Object obj1 = node.jjtGetChild(1).jjtAccept(this, data);
        return ELArithmetic.mod(obj0, obj1);
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstMult, java.lang.Object)
     */
    public Object visit(AstMult node, Object data) throws EvaluationException
    {
        Object obj0 = node.jjtGetChild(0).jjtAccept(this, data);
        Number num0 = coerceToNumber(obj0);
        if (num0.doubleValue() == 0.0)
        {
            return num0;
        }
        Object obj1 = node.jjtGetChild(1).jjtAccept(this, data);

        if (obj0 == null && obj1 == null)
        {
            return new Long(0);
        }
        return ELArithmetic.multiply(obj0, obj1);
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNegative, java.lang.Object)
     */
    public Object visit(AstNegative node, Object data)
            throws EvaluationException
    {
        Object obj = node.jjtGetChild(0).jjtAccept(this, data);

        if (obj == null)
        {
            return new Long(0);
        }
        if (obj instanceof BigDecimal)
        {
            return ((BigDecimal) obj).negate();
        }
        if (obj instanceof BigInteger)
        {
            return ((BigInteger) obj).negate();
        }
        if (obj instanceof String)
        {
            if (isStringFloat((String) obj))
            {
                return new Double(-Double.parseDouble((String) obj));
            }
            return new Long(-Long.parseLong((String) obj));
        }
        Class type = obj.getClass();
        if (obj instanceof Long || Long.TYPE == type)
        {
            return new Long(-((Long) obj).longValue());
        }
        if (obj instanceof Double || Double.TYPE == type)
        {
            return new Double(-((Double) obj).doubleValue());
        }
        if (obj instanceof Integer || Integer.TYPE == type)
        {
            return new Integer(-((Integer) obj).intValue());
        }
        if (obj instanceof Float || Float.TYPE == type)
        {
            return new Float(-((Float) obj).floatValue());
        }
        if (obj instanceof Short || Short.TYPE == type)
        {
            return new Short((short) -((Short) obj).shortValue());
        }
        if (obj instanceof Byte || Byte.TYPE == type)
        {
            return new Byte((byte) -((Byte) obj).byteValue());
        }
        Long num = (Long) coerceToNumber(obj, Long.class);
        return new Long(-num.longValue());
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNot, java.lang.Object)
     */
    public Object visit(AstNot node, Object data) throws EvaluationException
    {
        Object obj = node.jjtGetChild(0).jjtAccept(this, data);
        Boolean b = coerceToBoolean(obj);
        return Boolean.valueOf(!b.booleanValue());
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNotEqual, java.lang.Object)
     */
    public Object visit(AstNotEqual node, Object data)
            throws EvaluationException
    {
        Object obj0 = node.jjtGetChild(0).jjtAccept(this, data);
        Object obj1 = node.jjtGetChild(1).jjtAccept(this, data);
        return Boolean.valueOf(!equals(obj0, obj1));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNull, java.lang.Object)
     */
    public Object visit(AstNull node, Object data) throws EvaluationException
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNumber, java.lang.Object)
     */
    public Object visit(AstNumber node, Object data) throws EvaluationException
    {
        return node.getNumber();
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstOr, java.lang.Object)
     */
    public Object visit(AstOr node, Object data) throws EvaluationException
    {
        Object obj = node.jjtGetChild(0).jjtAccept(this, data);
        Boolean b = coerceToBoolean(obj);
        if (b.booleanValue())
        {
            return b;
        }
        obj = node.jjtGetChild(1).jjtAccept(this, data);
        b = coerceToBoolean(obj);
        return b;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstPlus, java.lang.Object)
     */
    public Object visit(AstPlus node, Object data) throws EvaluationException
    {
        Object obj0 = node.jjtGetChild(0).jjtAccept(this, data);
        Number num0 = coerceToNumber(obj0);
        if (num0.doubleValue() == 0.0)
        {
            return num0;
        }
        Object obj1 = node.jjtGetChild(1).jjtAccept(this, data);
        return ELArithmetic.add(obj0, obj1);
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstProperty, java.lang.Object)
     */
    public Object visit(AstProperty node, Object data)
            throws EvaluationException
    {
        return node.getName(); 
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstPropertyEval, java.lang.Object)
     */
    public Object visit(AstPropertyEval node, Object data)
            throws EvaluationException
    {
        return node.jjtGetChild(0).jjtAccept(this, data);
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstString, java.lang.Object)
     */
    public Object visit(AstString node, Object data) throws EvaluationException
    {
        return node.getString();
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstText, java.lang.Object)
     */
    public Object visit(AstText node, Object data) throws EvaluationException
    {
        return node.getText();
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstTrue, java.lang.Object)
     */
    public Object visit(AstTrue node, Object data) throws EvaluationException
    {
        return Boolean.TRUE;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstValue, java.lang.Object)
     */
    public Object visit(AstValue node, Object data) throws EvaluationException
    {
        Object obj = node.jjtGetChild(0).jjtAccept(this, data);
        int propCount = node.jjtGetNumChildren();
        int i = 1;
        Object property = null;
        while (obj != null && i < propCount)
        {
            property = node.jjtGetChild(i).jjtAccept(this, obj);
            obj = this.getPropertyResolver().getValue(obj, property);
            i++;
        }
        return obj;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstVariable, java.lang.Object)
     */
    public Object visit(AstVariable node, Object data)
            throws EvaluationException
    {
        return this.getVariableResolver().resolveVariable(context,
                node.getName());
    }
}