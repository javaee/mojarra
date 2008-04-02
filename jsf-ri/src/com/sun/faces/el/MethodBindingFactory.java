/*
 * $Id: MethodBindingFactory.java,v 1.2 2004/11/09 04:23:09 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 
package com.sun.faces.el;

import javax.faces.el.MethodBinding;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.webapp.UIComponentTag;

import com.sun.faces.el.impl.AstValue;
import com.sun.faces.el.impl.ELSupport;
import com.sun.faces.el.impl.JsfParser;
import com.sun.faces.el.impl.Node;
import com.sun.faces.el.impl.ParseException;


/**
 * @author Jacob Hookom
 */
public class MethodBindingFactory
{

    /**
     * 
     */
    public MethodBindingFactory()
    {
        super();
    }
    
    public MethodBinding createMethodBinding(String ref, Class[] paramTypes) throws ReferenceSyntaxException
    {
        if (ref == null)
        {
            throw new NullPointerException(ELSupport.msg("el.error.factory.null"));
        }
        else if (!UIComponentTag.isValueReference(ref))
        {
            throw new ReferenceSyntaxException(ELSupport.msg("el.error.factory.syntax", ref));
        }
        
        try
        {
            Node node = JsfParser.parse(ref);
            
            if (!(node instanceof AstValue))
            {
                throw new ReferenceSyntaxException(ELSupport.msg("el.error.factory.syntax", ref));
            }
            
            return new MethodBindingImpl(ref, node, paramTypes);
        }
        catch (ParseException pe)
        {
            throw new ReferenceSyntaxException(ELSupport.msg("el.error.factory.method",ref,pe.getMessage()));
        }
    }

}
