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

package javax.faces.application;

import java.util.Set;
import javax.faces.FacesWrapper;
import javax.faces.context.ExternalContext;

/**
 * <p class="changed_added_2_0">Provides a simple implementation of
 * {@link FacesAnnotationHandler} that can be subclassed by developers
 * wishing to provide specialized behavior to an existing {@link
 * FacesAnnotationHandler} instance.  The default implementation of all
 * methods is to call through to the wrapped {@link
 * FacesAnnotationHandler}.</p>
 *
 * <div class="changed_added_2_0">
 *
 * <p>Usage: extend this class and override {@link #getWrapped} to
 * return the instance we are wrapping.</p>
 *
 * </div>
 *
 * @since 2.0
 */
public abstract class FacesAnnotationHandlerWrapper extends FacesAnnotationHandler implements FacesWrapper<FacesAnnotationHandler> {

    /**
     * @return the instance that we are wrapping.
     */ 
    public abstract FacesAnnotationHandler getWrapped();

    /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link
     * FacesAnnotationHandler#getClassNamesWithFacesAnnotations}
     * on the wrapped {@link FacesAnnotationHandler} object.</p>
     */

    @Override
    public Set<String> getClassNamesWithFacesAnnotations(ExternalContext extContext) {
        return getWrapped().getClassNamesWithFacesAnnotations(extContext);
    }

   /**
     * <p class="changed_added_2_0">The default behavior of this method
     * is to call {@link FacesAnnotationHandler#processAnnotatedClasses}
     * on the wrapped {@link FacesAnnotationHandler} object.</p>
     */
    @Override
    public void processAnnotatedClasses(ExternalContext extContext, Set<String> annotatedClasses) {
        getWrapped().processAnnotatedClasses(extContext, annotatedClasses);
    }


    // -------------------------------------------- Methods from FacesAnnotationHandler

    
}
