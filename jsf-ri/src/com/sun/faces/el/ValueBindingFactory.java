/*
 * $Id: ValueBindingFactory.java,v 1.2 2004/11/09 04:23:10 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 
package com.sun.faces.el;

import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;

import com.sun.faces.el.impl.AbstractConstantNode;
import com.sun.faces.el.impl.JsfParser;
import com.sun.faces.el.impl.ELSupport;
import com.sun.faces.el.impl.Node;
import com.sun.faces.el.impl.ParseException;


/**
 * @author Jacob Hookom
 */
public class ValueBindingFactory
{   
    /**
     * 
     */
    public ValueBindingFactory()
    {
        super();
    }
    
    public ValueBinding createValueBinding(String ref) throws ReferenceSyntaxException
    {
        if (ref == null)
        {
            throw new NullPointerException(ELSupport.msg("el.error.factory.null"));
        }
        
        try
        {
            Node node = JsfParser.parse(ref);
            if (node instanceof AbstractConstantNode)
            {
                return new ValueBindingConstant(ref, ((AbstractConstantNode) node).getConstantValue());
            }
            return new ValueBindingImpl(ref, node);
        }
        catch (ParseException pe)
        {
            throw new ReferenceSyntaxException(ELSupport.msg("el.error.factory.value",ref,pe.getMessage()));
        }
    }
}
