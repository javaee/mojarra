/*
 * $Id: ValueGetVisitor.java,v 1.2 2004/11/09 04:23:39 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 
package com.sun.faces.el.impl;

import javax.faces.context.FacesContext;

/**
 * @author Jacob Hookom
 */
public class ValueGetVisitor extends AbstractJsfParserVisitor
{
    /**
     * @param context
     * @param ref
     */
    public ValueGetVisitor(FacesContext context)
    {
        super(context);
    }
}
