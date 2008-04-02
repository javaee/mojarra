/*
 * $Id: MethodAbstractVisitor.java,v 1.2 2004/11/09 04:23:32 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el.impl;

import java.lang.reflect.Method;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodNotFoundException;

/**
 * @author Jacob Hookom
 */
public abstract class MethodAbstractVisitor extends AbstractJsfParserVisitor
{
    protected Class[] paramTypes;

    /**
     * @param context
     */
    public MethodAbstractVisitor(FacesContext context, Class[] paramTypes)
    {
        super(context);
        this.paramTypes = paramTypes;
    }

    /**
     * @param base
     * @param target
     * @return
     * @throws EvaluationException
     * @throws MethodNotFoundException
     */
    protected Method findMethod(Object base, Object target)
            throws EvaluationException, MethodNotFoundException
    {
        if (base == null || target == null)
        {
            throw new MethodNotFoundException(ELSupport.msg(
                    "el.error.method.null", base, target));
        }
        String methodName = (target instanceof String) ? (String) target
                : target.toString();
        Method method = null;
        try
        {
            method = base.getClass().getMethod(methodName, this.paramTypes);
        }
        catch (NoSuchMethodException nsme)
        {
            throw new MethodNotFoundException(ELSupport.msg(
                    "el.error.method.notfound", base, method), nsme);
        }
        return method;
    }

    /**
     * @param base
     * @param method
     * @return
     * @throws EvaluationException
     * @throws MethodNotFoundException
     */
    public abstract Object handleMethod(Object base, Method method)
            throws EvaluationException, MethodNotFoundException;


    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstAnd, java.lang.Object)
     */
    public Object visit(AstAnd node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstChoose, java.lang.Object)
     */
    public Object visit(AstChoose node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstComplex, java.lang.Object)
     */
    public Object visit(AstComplex node, Object data)
            throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstDiv, java.lang.Object)
     */
    public Object visit(AstDiv node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstEmpty, java.lang.Object)
     */
    public Object visit(AstEmpty node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstEqual, java.lang.Object)
     */
    public Object visit(AstEqual node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstFalse, java.lang.Object)
     */
    public Object visit(AstFalse node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstFloat, java.lang.Object)
     */
    public Object visit(AstFloat node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstGreaterThan, java.lang.Object)
     */
    public Object visit(AstGreaterThan node, Object data)
            throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstGreaterThanEqual, java.lang.Object)
     */
    public Object visit(AstGreaterThanEqual node, Object data)
            throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstLessThan, java.lang.Object)
     */
    public Object visit(AstLessThan node, Object data)
            throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstLessThanEqual, java.lang.Object)
     */
    public Object visit(AstLessThanEqual node, Object data)
            throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstMinus, java.lang.Object)
     */
    public Object visit(AstMinus node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstMod, java.lang.Object)
     */
    public Object visit(AstMod node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstMult, java.lang.Object)
     */
    public Object visit(AstMult node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNegative, java.lang.Object)
     */
    public Object visit(AstNegative node, Object data)
            throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNot, java.lang.Object)
     */
    public Object visit(AstNot node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNotEqual, java.lang.Object)
     */
    public Object visit(AstNotEqual node, Object data)
            throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNull, java.lang.Object)
     */
    public Object visit(AstNull node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNumber, java.lang.Object)
     */
    public Object visit(AstNumber node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstOr, java.lang.Object)
     */
    public Object visit(AstOr node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstPlus, java.lang.Object)
     */
    public Object visit(AstPlus node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstProperty, java.lang.Object)
     */
    public Object visit(AstProperty node, Object data)
            throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstPropertyEval, java.lang.Object)
     */
    public Object visit(AstPropertyEval node, Object data)
            throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstString, java.lang.Object)
     */
    public Object visit(AstString node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstText, java.lang.Object)
     */
    public Object visit(AstText node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstTrue, java.lang.Object)
     */
    public Object visit(AstTrue node, Object data) throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstValue, java.lang.Object)
     */
    public Object visit(AstValue node, Object data) throws EvaluationException
    {
        // grab our inner evaluation visitor
        JsfParserVisitor parent = this.getEvaluationVisitor();

        // send in the get visitor to get our variable name
        Object obj = node.jjtGetChild(0).jjtAccept(parent, data);

        // set up our start/end
        int propCount = node.jjtGetNumChildren() - 1;
        int i = 1;

        // walk through 'middle' getters
        Object property = null;
        while (obj != null && i < propCount)
        {
            property = node.jjtGetChild(i).jjtAccept(parent, obj);
            obj = this.getPropertyResolver().getValue(obj, property);
            i++;
        }

        // grab the tail property and evaluate it as a method
        property = node.jjtGetChild(i).jjtAccept(parent, obj);
        Method method = this.findMethod(obj, property);
        return this.handleMethod(obj, method);
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstVariable, java.lang.Object)
     */
    public Object visit(AstVariable node, Object data)
            throws EvaluationException
    {
        throw new MethodNotFoundException(ELSupport
                .msg("el.error.method.invalid"));
    }
}