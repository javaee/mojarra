/*
 * $Id: AbstractConstantNode.java,v 1.2 2004/11/09 04:23:11 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 
package com.sun.faces.el.impl;

/**
 * @author Jacob Hookom
 */
public abstract class AbstractConstantNode extends AbstractNode
{
    /**
     * @param i
     */
    public AbstractConstantNode(int i)
    {
        super(i);
    }
    
    public abstract Object getConstantValue();
}
