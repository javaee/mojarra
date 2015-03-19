/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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

// FileRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.renderkit.RenderKitUtils;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class FileRenderer extends TextRenderer {

   // ---------------------------------------------------------- Public Methods

	@Override
	public void decode(FacesContext context, UIComponent component) {

		rendererParamsNotNull(context, component);

        if (!shouldDecode(component)) {
            return;
        }

        String clientId = decodeBehaviors(context, component);

        if (clientId == null) {
            clientId = component.getClientId(context);
        }

        assert(clientId != null);
        ExternalContext externalContext = context.getExternalContext();
        Map<String, String> requestMap = externalContext.getRequestParameterMap();
        
        if (requestMap.containsKey(clientId)) {
            setSubmittedValue(component, requestMap.get(clientId));
        }

        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try {
            Collection<Part> parts = request.getParts();
            for (Part cur : parts) {
                if (clientId.equals(cur.getName())) {
                    // The cause of 3404 is here: the component should not be 
                    // transient, rather, the value should not saved as part of
                    // the state
                    // component.setTransient(true);
                    setSubmittedValue(component, cur);
                }
            }
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        } catch (ServletException se) {
            throw new FacesException(se);
        }
            
    }
    
    // If we are in Project Stage Development mode, the parent form 
    // must have an enctype of "multipart/form-data" for this component.
    // If not, produce a message.
    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {
        if (context.isProjectStage(ProjectStage.Development)) {
            boolean produceMessage = false;
            UIForm form = RenderKitUtils.getForm(component, context);
            if (null != form) {
                String encType = (String)form.getAttributes().get("enctype");
                if (null == encType || !encType.equals("multipart/form-data")) {
                    produceMessage = true;
                } 
            } else {
                produceMessage = true;
            }
            
            if (produceMessage) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "File upload component requires a form with an enctype of multipart/form-data",
                    "File upload component requires a form with an enctype of multipart/form-data");
                context.addMessage(component.getClientId(context), message);   
            }       
        }
        super.encodeBegin(context, component);
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        if (submittedValue instanceof Part) {
            Part part = (Part) submittedValue;
            if ((part.getHeader("content-disposition") == null || part.getHeader("content-disposition").endsWith("filename=\"\"")) && part.getSize() <= 0) {
                return null;
            }
        }
        return submittedValue;
    }
        
        

}
