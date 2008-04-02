/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.sun.faces.el.impl;

import javax.servlet.jsp.PageContext;


/**
 *
 * <p>This is the JSTL-specific implementation of VariableResolver.
 * It looks up variable references in the PageContext, and also
 * recognizes references to implicit objects.
 * 
 * @author Nathan Abramson - Art Technology Group
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: ofung $
 **/

public class JspVariableResolver extends
    com.sun.faces.el.impl.VariableResolver {
    //-------------------------------------
    // Member variables
    //-------------------------------------

    private PageContext mCtx;

    //-------------------------------------
    /**
     *
     * Constructor
     **/
    public JspVariableResolver(PageContext pCtx) {
        mCtx = pCtx;
    }
  
    //-------------------------------------
    
    public Object resolveVariable(String pName) throws ElException {
        try {
            return resolve(pName);
        } catch (ElException e) {
            throw new ElException(e.getMessage());
        }
    }

    /**
     *
     * Resolves the specified variable within the given context.
     * Returns null if the variable is not found.
     **/
    public Object resolve(String pName)
        throws ElException {
        // Check for implicit objects
        if ("pageContext".equals(pName)) {
            return mCtx;
        } else if ("pageScope".equals(pName)) {
            return ImplicitObjects.
                getImplicitObjects(mCtx).
                getPageScopeMap();
        } else if ("requestScope".equals(pName)) {
            return ImplicitObjects.
                getImplicitObjects(mCtx).
                getRequestScopeMap();
        } else if ("sessionScope".equals(pName)) {
            return ImplicitObjects.
                getImplicitObjects(mCtx).
                getSessionScopeMap();
        } else if ("applicationScope".equals(pName)) {
            return ImplicitObjects.
                getImplicitObjects(mCtx).
                getApplicationScopeMap();
        } else if ("param".equals(pName)) {
            return ImplicitObjects.
                getImplicitObjects(mCtx).
                getParamMap();
        } else if ("paramValues".equals(pName)) {
            return ImplicitObjects.
                getImplicitObjects(mCtx).
                getParamsMap();
        } else if ("header".equals(pName)) {
            return ImplicitObjects.
                getImplicitObjects(mCtx).
                getHeaderMap();
        } else if ("headerValues".equals(pName)) {
            return ImplicitObjects.
                getImplicitObjects(mCtx).
                getHeadersMap();
        } else if ("initParam".equals(pName)) {
            return ImplicitObjects.
                getImplicitObjects(mCtx).
                getInitParamMap();
        } else if ("cookie".equals(pName)) {
            return ImplicitObjects.
                getImplicitObjects(mCtx).
                getCookieMap();
        }

        // Otherwise, just look it up in the page context
        else {
            return mCtx.findAttribute(pName);
        }
    }
					
    //-------------------------------------
}
