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

package com.sun.faces.component;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

@FacesComponent(value="dynamicAdd")
public class DynamicAdd extends UINamingContainer implements SystemEventListener {
  
  private boolean facetRequired = true;

    public boolean isFacetRequired() {
        return facetRequired;
    }

    public void setFacetRequired(boolean facetRequired) {
        this.facetRequired = facetRequired;
    }

    public DynamicAdd() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Map<String, Object> viewMap = ctx.getViewRoot().getViewMap();
        // increment the counter
        viewMap.put("dynamicAdd", null == viewMap.get("dynamicAdd") ?
            (Integer) 1 : ((Integer)viewMap.get("dynamicAdd")) + 1);
        this.setId("dynamic" + viewMap.get("dynamicAdd").toString());

        ctx.getViewRoot().subscribeToViewEvent(PreRenderViewEvent.class, (SystemEventListener) this);
    }

    public void processEvent(SystemEvent se) throws AbortProcessingException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        UIComponent source = (UIComponent) se.getSource();
        String id = source.getClientId(ctx);
        if (source.equals(ctx.getViewRoot())) {
            Map<String, Object> viewMap = ctx.getViewRoot().getViewMap();
            Integer numAddedSoFar = (Integer) viewMap.get("dynamicAdd");
            if (numAddedSoFar < 5) {
                DynamicAdd dynamic = (DynamicAdd) ctx.getApplication().createComponent("dynamicAdd");
                dynamic.setFacetRequired(this.isFacetRequired());
                this.getChildren().add(dynamic);
            }
        }
    }

    public boolean isListenerForSource(Object o) {
        return o instanceof UIViewRoot;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
      // conditionally create dynamic component facets
      if (facetRequired && null == getFacet("dynamicAddFacet")) {
        getFacets().put("dynamicAddFacet", new HtmlPanelGroup());
      }
        Map<Object, Object> contextMap = context.getAttributes();
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("ul", this);
        writer.startElement("p", this);
        writer.write("Dynamic Component " + this.getId());
        
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        Map<Object, Object> contextMap = context.getAttributes();
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("p");
        writer.endElement("ul");

    }

    @Override
    public void processDecodes(FacesContext context) {
      // conditionally recreate the dynamic component facet before process decode
      if (facetRequired && null == getFacet("dynamicAddFacet")) {
        getFacets().put("dynamicAddFacet", new HtmlPanelGroup());
      }
      
      // TODO Auto-generated method stub
      super.processDecodes(context);
    }



}
