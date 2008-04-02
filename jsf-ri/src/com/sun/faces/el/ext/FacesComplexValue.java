/*
 * $Id: FacesComplexValue.java,v 1.1 2003/08/13 18:10:45 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el.ext;

import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.ComplexValue;
import com.sun.faces.el.impl.Expression;
import com.sun.faces.el.impl.ValueSuffix;

import java.util.List;

public class FacesComplexValue extends ComplexValue {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //
    
    public FacesComplexValue(Expression prefix, List suffixes) {
        super(prefix, suffixes);
    }
    
    //
    // Class methods
    //

    //
    // General Methods
    //

    public Object evaluate(ExpressionInfo exprInfo) throws ElException {
        FacesExpressionInfo facesExprInfo = (FacesExpressionInfo) exprInfo;
        Object rValue = facesExprInfo.getRValue();
        facesExprInfo.setRValue(null);
        Object ret = getPrefix().evaluate(exprInfo);
        List suffixes = getSuffixes();
        
        
        // Apply the suffixes
        for (int i = 0, size = suffixes.size(); i < size; i++) {
            ValueSuffix suffix = (ValueSuffix) suffixes.get(i);
            if ((size - 1) == i) {                
                // use the rValue to evaluate, this prevents pre-maturely setting the value
                facesExprInfo.setRValue(rValue);
                ret = suffix.evaluate(ret, exprInfo);
            } else {                
                ret = suffix.evaluate(ret, exprInfo);
            }
        }
        return ret;
    }

} // end of class FacesComplexValue
