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

package javax.faces.webapp.pdl;

import java.beans.BeanInfo;
import java.io.IOException;
import javax.faces.application.Resource;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * <p class="changed_added_2_0">The contract that a page declaration
 * language must implement to interact with the JSF runtime. 
 * An implementation
 * of this class must be thread-safe.</p>
 *
 * <div class="changed_added_2_0">
 * 
 * <p>Instances of this class are application scoped and must be
 * obtained from the {@link PageDeclarationLanguageFactory}.</p>
 * 
 * </div>
 * 
 * @since 2.0
 * 
 */
public abstract class PageDeclarationLanguage {

    /**
     * <p class="changed_added_2_0">Return a reference to the component
     * metadata for the composite component represented by the argument
     * <code>componentResource</code>.  The implementation may share and
     * pool what it ends up returning from this method to improve
     * performance.  The implementation must ensure that no per
     * component-instance information is included in the return.  The
     * default implementation must support <code>Resource</code> being a
     * Facelet markup file that is to be interpreted as a composite
     * component as specified in section 3.6 of the spec prose document.
     * The default implementation is not required to support
     * <code>Resource</code> being a JSP markup file.  See section
     * 3.6.2.1 for the complete specification of the component metadata
     * that must be returned by the default implementation of this
     * method.</p>
     *
     * <p class="changed_added_2_0">This method is called from {@link
     * javax.faces.application.Application#createComponent(FacesContext,
     * Resource)} as a result of the PDL implementation encountering a
     * composite component within a view.</p>
     *
     *
     * </div>
     * 
     * @param context The <code>FacesContext</code> for this request.
     * @param componentResource The <code>Resource</code> that represents the component.
     * @since 2.0
     */
    public abstract BeanInfo getComponentMetadata(FacesContext context, Resource componentResource);


    /**
     * <p class="changed_added_2_0">Take implementation specific action
     * to discover a <code>Resource</code> given the argument
     * <code>componentResource</code>.  The returned
     * <code>Resource</code> if non-<code>null</code>, must point to a
     * script file that can be turned into something that extends {@link
     * javax.faces.component.UIComponent} and implements {@link
     * javax.faces.component.NamingContainer}.</p>
     *
     * <p class="changed_added_2_0">This method is called from {@link javax.faces.application.Application#createComponent(FacesContext, Resource)}.</p>
     *
     * @param context The <code>FacesContext</code> for this request.
     * @param componentResource The <code>Resource</code> that represents the component.
     * @since 2.0
     */
    public abstract Resource getScriptComponentResource(FacesContext context,
            Resource componentResource);
    
    
    public abstract UIViewRoot createView(FacesContext ctx,
                                 String viewId);
    
    public abstract UIViewRoot restoreView(FacesContext ctx, String viewId);

    /**
     * <p class="changed_added_2_0">Cause the view to be traversed for
     * rendering.  PENDING(edburns): need more specifics.  For JSP, this
     * means taking the action necessary in section 7.5.2.  For
     * Facelets, the following assertions apply.</p>

     * <p>The unique id constraint is applied.</p>

     * <p>FaceletFactory and Facelet.apply() are called.</p>
     *
     * @since 2.0
     */
    
    public abstract void renderView(FacesContext ctx,
                                    UIViewRoot view)
    throws IOException;
    

}
