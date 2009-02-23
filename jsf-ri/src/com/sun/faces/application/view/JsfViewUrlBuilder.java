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

package com.sun.faces.application.view;

import java.util.List;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.webapp.pdl.PageDeclarationLanguage;

/**
 * @author Dan Allen
 */
public class JsfViewUrlBuilder extends UrlBuilder {
    private String viewId;
    private boolean includeViewParams;
    private FacesContext context;
    private ViewHandler viewHandler;

    public JsfViewUrlBuilder(FacesContext context, String url, boolean includeViewParams, String encoding) {
        super(url, encoding);
        if (context == null) {
            throw new IllegalArgumentException("The FacesContext is required");
        }
        this.context = context;
        this.viewHandler = context.getApplication().getViewHandler();
        this.includeViewParams = includeViewParams;
        this.viewId = getPath();
        convertViewToAction();
    }

    public JsfViewUrlBuilder(FacesContext context, String url, boolean includeViewParams) {
        this(context, url, includeViewParams, DEFAULT_ENCODING);
    }

    // NOTE I'm not quite comfortable with the convoluted exchange of calls here
    @Override
    public UrlBuilder setPath(String path) {
        super.setPath(path);
        this.viewId = getPath();
        convertViewToAction();
        return this;
    }

    // QUESTION should there be a createEncodedUrl? or perhaps a boolean argument?
    @Override
    public String createUrl() {
        return context.getExternalContext().encodeActionURL(super.createUrl());
    }

    @Override
    protected void appendQueryString() {
        addViewParameters();
        super.appendQueryString();
    }

    /**
     * Order of precedence for parameters:
     * 1. UIParameter
     * 2. Query string parameter
     * 3. Page parameter
     */
    protected void addViewParameters() {
        if (!includeViewParams) {
            return;
        }
        UIViewRoot currentRoot = context.getViewRoot();
        String currentViewId = currentRoot.getViewId();
        PageDeclarationLanguage pdl = null;
        List<UIViewParameter> toViewParams;
        List<UIViewParameter> currentViewParams;
        boolean currentIsSameAsNew = false;

        pdl = viewHandler.getPageDeclarationLanguage(context, currentViewId);
        currentViewParams = pdl.getViewParameters(context, currentViewId);
        
        if (currentViewId.equals(viewId)) {
            currentIsSameAsNew = true;
            toViewParams = currentViewParams;
        }
        else {
            pdl = viewHandler.getPageDeclarationLanguage(context, viewId);
            toViewParams = pdl.getViewParameters(context, viewId);
        }

        if (toViewParams.isEmpty()) {
            return;
        }

        for (UIViewParameter viewParam : toViewParams) {
            String value = null;
            // don't bother looking at page parameter if it's been overridden
            if (getParameters().containsKey(viewParam.getName())) {
                continue;
            }
            else if (paramHasValueExpression(viewParam)) {
                value = viewParam.getStringValueFromModel(context);
            }
            else {
                // Anonymous page parameter:
                // Get string value from UIViewParameter instance stored in current view
                if (currentIsSameAsNew) {
                    value = viewParam.getStringValue(context);
                }
                // ...or transfer string value from matching UIViewParameter instance stored in current view
                else {
                    value = getStringValueToTransfer(context, viewParam,
                            toViewParams);
                }
            }
            if (value != null) {
                addParameter(viewParam.getName(), value);
            }
        }
    }

    private static String getStringValueToTransfer(FacesContext context, 
            UIViewParameter param,
            List<UIViewParameter> viewParams) {
        for (UIViewParameter candidate : viewParams) {
            if ((null != candidate.getName() && null != param.getName()) &&
                candidate.getName().equals(param.getName())) {
                // QUESTION: should this be getStringValue()? That's how it is implemented in Seam
                // ANSWER: I don't know.
                return candidate.getStringValue(context);
            }
        }

        return null;
    }
    
    private static boolean paramHasValueExpression(UIViewParameter param) {
        boolean result = false;
        result = (null != param.getValueExpression("value"));
        return result;
    }

    protected void convertViewToAction() {
		if (!viewId.startsWith("/")) {
			throw new IllegalArgumentException("The viewId must begin with \"/\"");
		}
        super.setPath(viewHandler.getActionURL(context, viewId));
    }


}
