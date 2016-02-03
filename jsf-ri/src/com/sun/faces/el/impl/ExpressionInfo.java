/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

/*
 * %W% %G%
 */

package com.sun.faces.el.impl;

import javax.faces.context.FacesContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;

public class ExpressionInfo {

    private FacesContext facesContext;
    private FunctionMapper functionMapper;
    private VariableResolver variableResolver;
    private PropertyResolver propertyResolver;
    private String expressionString;
    /**
     * This is public for performance reasons.  
     */
    public final JSPExpressionString jspExpressionString = new JSPExpressionString();
    private Class expectedType;

    /**
     * <p>Reset the state of this instance to be the same as a freshly
     * instantiated instance.</p>
     */
    public void reset() {
	facesContext = null;
	functionMapper = null;
	variableResolver = null;
	propertyResolver = null;
	expressionString = null;
	jspExpressionString.set(null);
    }


    /**
     * Returns the {@see FacesContext}.
     *
     * @return the FacesContext
     */
    public FacesContext getFacesContext() {
        return facesContext;
    }


    /**
     * Sets the {@see FacesContext}.
     *
     * @param facesContext FacesContext
     */
    public void setFacesContext(FacesContext facesContext) {
        this.facesContext = facesContext;
    }


    /**
     * TODO
     *
     * @return
     */
    public FunctionMapper getFunctionMapper() {
        return functionMapper;
    }


    /**
     * TODO
     *
     * @param functionMapper
     */
    public void setFunctionMapper(FunctionMapper functionMapper) {
        this.functionMapper = functionMapper;
    }


    /**
     * TODO
     *
     * @return
     */
    public VariableResolver getVariableResolver() {
        return variableResolver;
    }


    /**
     * TODO
     *
     * @param variableResolver
     */
    public void setVariableResolver(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
    }


    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }


    public void setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }


    /**
     * Returns the expression string with the "${}" delimiters that
     * the JSP 2.0 evaluator requires. An alternative would be to
     * modify the parser, but I think that can wait until a merged
     * JSF/JSP evaluator is designed.
     *
     * @return
     */
    public String getExpressionString() {
        return "${" + expressionString + "}";
    }


    /**
     * TODO
     *
     * @param expressionString
     */
    public void setExpressionString(String expressionString) {
        int index;
        //ExpressionString may contain more than one expression.
        //If it does, change delimeters to ones recognized by the
        //JSP parser.
        while ((index = expressionString.indexOf("#{")) != -1) {
            StringBuffer buf = new StringBuffer();
            buf.append(expressionString.substring(0, index));
            buf.append("$");
            buf.append(expressionString.substring(index + 1));
            expressionString = buf.toString();
        }
        this.expressionString = expressionString;
	this.jspExpressionString.set(expressionString);
    }


    /**
     * TODO
     *
     * @return
     */
    public Class getExpectedType() {
        return expectedType;
    }


    /**
     * TODO
     *
     * @param expectedType
     */
    public void setExpectedType(Class expectedType) {
        this.expectedType = expectedType;
    }

    public static class JSPExpressionString extends Object {

    public static final int ALLOC_INCR = 256;

    public JSPExpressionString() {
	buf = new char[ALLOC_INCR];
	buf[0] = '$';
	buf[1] = '{';
    }

    public char buf[] = null;
    public int bufLen = 0;
    public int bufSize = ALLOC_INCR;
    
    public void set(String newString) {
	if (null == newString) {
	    bufLen = 0;
	    return;
	}
	int newBufSize, len;
	if (bufSize < (newBufSize = ((len = newString.length()) + 3))) {
	    bufSize = newBufSize;
	    buf = new char[bufSize];
	    buf[0] = '$';
	    buf[1] = '{';
	}
	char [] src = newString.toCharArray();
	System.arraycopy(src, 0, buf, 2, len);
	buf[len + 2] = '}'; 
	bufLen = len + 3;
    }

    public String toString() {
	return new String(buf, 0, bufLen);
    }
}
}
