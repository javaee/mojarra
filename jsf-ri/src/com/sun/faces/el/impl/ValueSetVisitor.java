/*
 * $Id: ValueSetVisitor.java,v 1.2 2004/11/09 04:23:39 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el.impl;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ReferenceSyntaxException;

/**
 * @author Jacob Hookom
 */
public class ValueSetVisitor extends AbstractJsfParserVisitor
{
    protected Object value;

    /**
     * @param context
     * @param ref
     */
    public ValueSetVisitor(FacesContext context, Object value)
    {
        super(context);
        this.value = value;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstAnd, java.lang.Object)
     */
    public Object visit(AstAnd node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstChoose, java.lang.Object)
     */
    public Object visit(AstChoose node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstComplex, java.lang.Object)
     */
    public Object visit(AstComplex node, Object data)
            throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstDiv, java.lang.Object)
     */
    public Object visit(AstDiv node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstEmpty, java.lang.Object)
     */
    public Object visit(AstEmpty node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstEqual, java.lang.Object)
     */
    public Object visit(AstEqual node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstFalse, java.lang.Object)
     */
    public Object visit(AstFalse node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstFloat, java.lang.Object)
     */
    public Object visit(AstFloat node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstGreaterThan, java.lang.Object)
     */
    public Object visit(AstGreaterThan node, Object data)
            throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstGreaterThanEqual, java.lang.Object)
     */
    public Object visit(AstGreaterThanEqual node, Object data)
            throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstLessThan, java.lang.Object)
     */
    public Object visit(AstLessThan node, Object data)
            throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstLessThanEqual, java.lang.Object)
     */
    public Object visit(AstLessThanEqual node, Object data)
            throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstMinus, java.lang.Object)
     */
    public Object visit(AstMinus node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstMod, java.lang.Object)
     */
    public Object visit(AstMod node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstMult, java.lang.Object)
     */
    public Object visit(AstMult node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNegative, java.lang.Object)
     */
    public Object visit(AstNegative node, Object data)
            throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNot, java.lang.Object)
     */
    public Object visit(AstNot node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNotEqual, java.lang.Object)
     */
    public Object visit(AstNotEqual node, Object data)
            throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNull, java.lang.Object)
     */
    public Object visit(AstNull node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstNumber, java.lang.Object)
     */
    public Object visit(AstNumber node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstOr, java.lang.Object)
     */
    public Object visit(AstOr node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstPlus, java.lang.Object)
     */
    public Object visit(AstPlus node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstString, java.lang.Object)
     */
    public Object visit(AstString node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstText, java.lang.Object)
     */
    public Object visit(AstText node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstTrue, java.lang.Object)
     */
    public Object visit(AstTrue node, Object data) throws EvaluationException
    {
        throw new ReferenceSyntaxException(ELSupport
                .msg("el.error.value.set.invalid"));
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

        // if the object returned is null itself
        if (obj == null)
        {
            throw new PropertyNotFoundException(ELSupport.msg(
                    "el.error.property.walk.var", ((AbstractNode) node
                            .jjtGetChild(0)).getImage()));
        }

        // set up our start/end
        int propCount = node.jjtGetNumChildren() - 1;
        int i = 1;

        // evaluate middle getters
        Object property = null;
        while (obj != null && i < propCount)
        {
            property = node.jjtGetChild(i).jjtAccept(parent, obj);
            obj = this.getPropertyResolver().getValue(obj, property);
            i++;
        }

        // if the object to set upon is null
        if (obj == null)
                throw new PropertyNotFoundException(ELSupport.msg(
                        "el.error.property.walk.get", obj, property));

        // set the property on the object
        property = node.jjtGetChild(i).jjtAccept(parent, obj);
        this.getPropertyResolver().setValue(obj, property, this.value);
        
        return obj;
    }

    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.JsfParserVisitor#visit(com.sun.faces.el.impl.AstVariable, java.lang.Object)
     */
    public Object visit(AstVariable node, Object data)
            throws EvaluationException
    {
        String name = node.getName();
        if (ELSupport.isImplicitVariable(name))
        {
            throw new ReferenceSyntaxException(ELSupport.msg(
                    "el.error.property.set.implicit", name));
        }
        else
        {
            Map scope = this.resolveScope(name);
            scope.put(node.getName(), this.value);
        }
        return null;
    }
}