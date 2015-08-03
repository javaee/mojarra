/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDLGPL_1_1.html
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
package com.sun.faces.cdi;

import static com.sun.faces.component.CompositeComponentStackManager.getManager;
import static javax.faces.component.UIComponent.getCurrentCompositeComponent;
import static javax.faces.context.FacesContext.getCurrentInstance;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_3">
 * The Composite Component producer is the CDI producer that allows EL resolving of
 * <code>#{cc}</code>
 * </p>
 *
 * @since 2.3
 */
public class CompositeComponentProducer extends CdiProducer<Object> {

    /**
     * Serialization version
     */
    private static final long serialVersionUID = 1L;
    
    public CompositeComponentProducer() {
        super.name("cc")
             .beanClassAndType(UIComponent.class)
             .create(e -> {
              
                 FacesContext context = getCurrentInstance();
                 
                 // The following five lines violate the specification.
                 // The specification states that the 'cc' implicit object
                 // always evaluates to the current composite component,
                 // however, this isn't desirable behavior when passing
                 // attributes between nested composite components, so we
                 // need to alter the behavior so that the components behave
                 // as the user would expect.
                 /* BEGIN DEVIATION */
                 UIComponent component = getManager(context).peek();
                 
                 /* END DEVIATION */
                 if (component == null) {
                     component = getCurrentCompositeComponent(context);
                 }
                 
                 return component;
                 
             });
             
    }

}
