/*
 * $Id: FacesArraySuffix.java,v 1.1 2003/08/13 18:10:44 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el.ext;

import com.sun.faces.el.impl.ArraySuffix;
import com.sun.faces.el.impl.Expression;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.MessageUtil;
import com.sun.faces.el.impl.Constants;
import com.sun.faces.el.impl.Coercions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.el.PropertyResolver;
import javax.faces.el.PropertyNotFoundException;
import java.util.List;
import java.util.Map;

public class FacesArraySuffix extends ArraySuffix {

    //
    // Private/Protected Constants
    //
    private static Log log = LogFactory.getLog(FacesArraySuffix.class);
        
    //
    // Constructors and Initializers    
    //

    public FacesArraySuffix(Expression expression) {
        super(expression);
    }   

    //
    // General Methods
    //
    
    /**
     * Evaluates the expression in the given context, operating on the
     * given value.
     **/
    public Object evaluate(Object pValue, ExpressionInfo exprInfo) throws ElException {
        Object indexVal = null;
        Object result = null;
        int index = -1;
        boolean isIndexCase = false;

        FacesExpressionInfo facesExprInfo = (FacesExpressionInfo) exprInfo;        
        PropertyResolver propertyResolver = facesExprInfo.getPropertyResolver();        
        Object rValue = facesExprInfo.getRValue();

        if (pValue == null) {
            if (log.isWarnEnabled()) {
                log.warn(
                    MessageUtil.getMessageWithArgs(
                        Constants.CANT_GET_INDEXED_VALUE_OF_NULL,
                        getOperatorSymbol()));
            }           
            return null;
        } else if ((indexVal = evaluateIndex(exprInfo)) == null) {    // Evaluate the index
            if (log.isWarnEnabled()) {
                log.warn(
                    MessageUtil.getMessageWithArgs(
                        Constants.CANT_GET_NULL_INDEX,
                        getOperatorSymbol()));
            }
            return null;
        } 
        
        //
        // determine whether this is the index case, or the property case
        //
        if (isIndexCase = (pValue instanceof List || pValue.getClass().isArray())) {
            Integer indexObj = Coercions.coerceToInteger(indexVal);
            if (indexObj == null) {
                if (log.isErrorEnabled()) {
                    String message = MessageUtil.getMessageWithArgs(
                        Constants.BAD_INDEX_VALUE,
                        getOperatorSymbol(),
                        indexVal.getClass().getName());
                    log.error(message);
                    throw new ElException(message);
                }
                return null;
            }
            index = indexObj.intValue();
        }
        
        //
        // deal with the "get" and "put" cases separately
        //
                
        // this is the "put" case
        if (rValue != null) {
            if (isIndexCase) {
                propertyResolver.setValue(pValue, index, rValue);
            } else {
                propertyResolver.setValue(pValue, (String) indexVal, rValue);
            }
        } else {
            // this is the "get" case
            if (isIndexCase) {
                result = propertyResolver.getValue(pValue, index);
            } else {
                result = propertyResolver.getValue(pValue, (String) indexVal);
            }
        }

        return result;
    }

} // end of class FacesArraySuffix
