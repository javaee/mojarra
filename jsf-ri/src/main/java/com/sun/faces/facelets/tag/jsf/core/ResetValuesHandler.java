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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.tag.jsf.core;


import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.view.ActionSource2AttachedObjectHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import javax.faces.component.UIViewRoot;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;

public final class ResetValuesHandler extends ActionListenerHandlerBase
      implements ActionSource2AttachedObjectHandler {
    
    private final TagAttribute render;

    // Pattern used for execute/render string splitting
    private static Pattern SPLIT_PATTERN = Pattern.compile(" ");
    
    
    private final static class LazyActionListener
          implements ActionListener, Serializable {
        Collection<String> render;

        private static final long serialVersionUID = -5676209243297546166L;

        public LazyActionListener(Collection<String> render) {
            this.render = new ArrayList<String>(render);
        }

        public void processAction(ActionEvent event)
              throws AbortProcessingException {
            FacesContext context = FacesContext.getCurrentInstance();
            UIViewRoot root = context.getViewRoot();
            root.resetValues(context, render);
        }
    }

    /**
     * @param config
     */
    public ResetValuesHandler(TagConfig config) {
        super(config);
        this.render = this.getAttribute("render");
    }


    @Override
    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        FaceletContext ctx = (FaceletContext) context.getAttributes()
              .get(FaceletContext.FACELET_CONTEXT_KEY);
        ActionSource as = (ActionSource) parent;
        String renderStr = (String) render.getObject(ctx, String.class);
        ActionListener listener = new LazyActionListener(toList(renderStr));
        as.addActionListener(listener);
    }
    
    // Converts the specified object to a List<String>
    private static List<String> toList(String strValue) {

        
        // If the value contains no spaces, we can optimize.
        // This is worthwhile, since the execute/render lists
        // will often only contain a single value.
        if (strValue.indexOf(' ') == -1) {
            return Collections.singletonList(strValue);
        }
        
        // We're stuck splitting up the string.
        String[] values = SPLIT_PATTERN.split(strValue);
        if ((values == null) || (values.length == 0)) {
            return null;
        }
        
        // Note that we could create a Set out of the values if
        // we care about removing duplicates.  However, the
        // presence of duplicates does not real harm.  They will
        // be consolidated during the partial view traversal.  So,
        // just create an list - garbage in, garbage out.
        return Collections.unmodifiableList(Arrays.asList(values));
    
    }
    

}
