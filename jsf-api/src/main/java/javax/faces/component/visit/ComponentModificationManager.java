/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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
package javax.faces.component.visit;

import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_2"><strong>ComponentModificationManager</strong>
 * enables a component instance to track the modifications 
 * made to it during any portion of a single run through the JSF request processing
 * lifecycle.  Though the state in question has nothing to do with the component 
 * state handled by {@link javax.faces.application.StateManager}.  The intended
 * use case for this class is for components that need to temporarily suspend
 * processing in order to correctly perform 
 * {@link javax.faces.component.UIComponent#invokeOnComponent}
 * or {@link javax.faces.component.UIComponent#visitTree}, and resume
 * processing when those methods return.</p>
 *
 * @since 2.2
 */
public abstract class ComponentModificationManager {
    
    public abstract void push(ComponentModification mod);
    
    public abstract ComponentModification peek();
    
    public abstract ComponentModification pop();
    
    public abstract ComponentModification suspend(FacesContext context);
    
    public abstract void resume(FacesContext context, ComponentModification mods);
    
}
