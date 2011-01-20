/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package com.sun.faces.taglib.jsf_core;

import java.util.Stack;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;

import java.lang.reflect.Method;

import com.sun.faces.util.RequestStateManager;
import com.sun.faces.util.ReflectionUtils;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.RIConstants;
import java.util.Map;

public class SubviewTag extends UIComponentELTag {


    private static final Logger LOGGER = FacesLogger.TAGLIB.getLogger();


    // ------------------------------------------------------------ Constructors


    public SubviewTag() {

        super();

    }


    // ---------------------------------------------------------- Public Methods


    public String getComponentType() {

        return "javax.faces.NamingContainer";

    }

   
    public String getRendererType() {

        return null;

    }


    // ------------------------------------------------------- Protected Methods


    protected UIComponent createVerbatimComponentFromBodyContent() {

        UIOutput verbatim = (UIOutput)
              super.createVerbatimComponentFromBodyContent();
        String value = null;

        FacesContext ctx = getFacesContext();
        Object response = ctx.getExternalContext().getResponse();
        // flush out any content above the view tag
        Method customFlush = ReflectionUtils.lookupMethod(response.getClass(),
                                                          "flushContentToWrappedResponse",
                                                          RIConstants.EMPTY_CLASS_ARGS);
        Method isBytes = ReflectionUtils.lookupMethod(response.getClass(),
                                                      "isBytes",
                                                      RIConstants.EMPTY_CLASS_ARGS);
        Method isChars = ReflectionUtils.lookupMethod(response.getClass(),
                                                      "isChars",
                                                      RIConstants.EMPTY_CLASS_ARGS);
        Method resetBuffers = ReflectionUtils.lookupMethod(response.getClass(),
                                                           "resetBuffers",
                                                           RIConstants.EMPTY_CLASS_ARGS);
        Method getChars = ReflectionUtils.lookupMethod(response.getClass(),
                                                       "getChars",
                                                       RIConstants.EMPTY_CLASS_ARGS);
        boolean cont = true;
        if (isBytes == null) {
            cont = false;
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "jsf.core.taglib.subviewtag.interweaving_failed_isbytes");
            }
        }
        if (isChars == null) {
            cont = false;
             if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "jsf.core.taglib.subviewtag.interweaving_failed_ischars");
            }
        }
        if (resetBuffers == null) {
            cont = false;
             if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "jsf.core.taglib.subviewtag.interweaving_failed_resetbuffers");
            }
        }
        if (getChars == null) {
            cont = false;
             if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "jsf.core.taglib.subviewtag.interweaving_failed_getchars");
            }
        }
        if (customFlush == null) {
            cont = false;
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "jsf.core.taglib.viewtag.interweaving_failed");
            }
        }

        if (cont) {
            try {
                if ((Boolean) isBytes.invoke(response)) {
                    customFlush.invoke(response);
                } else if ((Boolean) isChars.invoke(response)) {
                    char[] chars = (char[]) getChars.invoke(response);
                    if (null != chars && 0 < chars.length) {
                        if (null != verbatim) {
                            value = (String) verbatim.getValue();
                        }
                        verbatim = super.createVerbatimComponent();
                        if (null != value) {
                            verbatim.setValue(value + new String(chars));
                        } else {
                            verbatim.setValue(new String(chars));
                        }
                    }
                }
                resetBuffers.invoke(response);

            } catch (Exception e) {
                throw new FacesException("Response interweaving failed!", e);
            }
        }

        return verbatim;

    }

    public int doEndTag() throws JspException {
        int retValue;

        getViewTagStack().pop();
        retValue = super.doEndTag();
        return retValue;
    }

    public int doStartTag() throws JspException {
        int retValue;
        
        retValue = super.doStartTag();
        getViewTagStack().push(this);
        
        return retValue;
    }

    /** 
     *  @return Stack of UIComponentClassicTagBase instances, each of
     *  which is a "view" tag.  The bottom most element on the stack is
     *  the ViewTag itself.  Subsequent instances are SubviewTag
     *  instances.
     */
    static Stack<UIComponentClassicTagBase> getViewTagStack() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        Map<String,Object> stateMap = RequestStateManager.getStateMap(ctx);

        //noinspection unchecked
        Stack<UIComponentClassicTagBase> result = (Stack<UIComponentClassicTagBase>)
              stateMap.get(RequestStateManager.VIEWTAG_STACK_ATTR_NAME);
        if (result == null) {
            result = new Stack<UIComponentClassicTagBase>();
            stateMap.put(RequestStateManager.VIEWTAG_STACK_ATTR_NAME, result);
        }
        
        return result;
    }
    

}
