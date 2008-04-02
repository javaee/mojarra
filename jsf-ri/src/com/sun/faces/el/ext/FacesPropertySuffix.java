/*
 * $Id: FacesPropertySuffix.java,v 1.1 2003/08/13 18:10:47 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el.ext;

import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.StringLiteral;

public class FacesPropertySuffix extends FacesArraySuffix {

    //
    // Instance Variables
    //

    String mName;
        
    //
    // Constructors and Initializers    
    //

    public FacesPropertySuffix(String pName) {
        super(null);
        mName = pName;
    }

    //
    // General Methods
    //

    public String getName() {
        return mName;
    }

    public void setName(String pName) {
        mName = pName;
    }
    
    /**
     * Gets the value of the index
     */
    protected Object evaluateIndex(ExpressionInfo exprInfo)
        throws ElException {
        return mName;
    }

    /**
     * Returns the operator symbol
     */
    protected String getOperatorSymbol() {
        return ".";
    }

    //-------------------------------------
    // ValueSuffix methods
    //-------------------------------------
    
    /**
     * Returns the expression in the expression language syntax
     */
    public String getExpressionString() {
        return "." + StringLiteral.toIdentifierToken(mName);
    }

} // end of class FacesPropertySuffix
