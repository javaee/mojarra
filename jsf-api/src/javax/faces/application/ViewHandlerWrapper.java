/*
 * $Id: ViewHandlerWrapper.java,v 1.6 2007/04/27 22:00:02 ofung Exp $
 */

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

import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.FacesException;

import java.util.Locale;
import java.io.IOException;

/**
 * <p>Provides a simple implementation of {@link ViewHandler} that can
 * be subclassed by developers wishing to provide specialized behavior
 * to an existing {@link ViewHandler} instance.  The default
 * implementation of all methods is to call through to the wrapped
 * {@link ViewHandler}.</p>
 *
 * <p>Usage: extend this class and override {@link #getWrapped} to
 * return the instance we are wrapping.</p>
 *
 * @since 1.2
 */
public abstract class ViewHandlerWrapper extends ViewHandler {


    /**
     * @return the instance that we are wrapping.
     */ 

    abstract protected ViewHandler getWrapped();


    // ------------------------ Methods from javax.faces.application.ViewHandler


    /**
     * 
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#calculateCharacterEncoding(javax.faces.context.FacesContext)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#calculateCharacterEncoding(javax.faces.context.FacesContext)
     * @since 1.2
     */

    public String calculateCharacterEncoding(FacesContext context) {

        return getWrapped().calculateCharacterEncoding(context);

    }
        
    /**
     * 
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#calculateLocale(javax.faces.context.FacesContext)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#calculateLocale(javax.faces.context.FacesContext)
     * @since 1.2
     */
    public Locale calculateLocale(FacesContext context) {

        return getWrapped().calculateLocale(context);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#calculateRenderKitId(javax.faces.context.FacesContext)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#calculateRenderKitId(javax.faces.context.FacesContext)
     * @since 1.2
     */
    public String calculateRenderKitId(FacesContext context) {

        return getWrapped().calculateRenderKitId(context);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#createView(javax.faces.context.FacesContext, String)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#createView(javax.faces.context.FacesContext, String)
     * @since 1.2
     */
    public UIViewRoot createView(FacesContext context, String viewId) {

        return getWrapped().createView(context, viewId);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#getActionURL(javax.faces.context.FacesContext, String)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#getActionURL(javax.faces.context.FacesContext, String)
     * @since 1.2
     */
    public String getActionURL(FacesContext context, String viewId) {

        return getWrapped().getActionURL(context, viewId);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#getResourceURL(javax.faces.context.FacesContext, String)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#getResourceURL(javax.faces.context.FacesContext, String)
     * @since 1.2
     */
    public String getResourceURL(FacesContext context, String path) {

        return getWrapped().getResourceURL(context, path);
    }
    
    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#initView}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#initView
     * @since 1.2
     */
    public void initView(FacesContext context) throws FacesException {
        
        getWrapped().initView(context);
    }
    
    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#renderView(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)
     * @since 1.2
     */
    public void renderView(FacesContext context, UIViewRoot viewToRender)
    throws IOException, FacesException {

        getWrapped().renderView(context, viewToRender);

    }


    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#restoreView(javax.faces.context.FacesContext, String)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#restoreView(javax.faces.context.FacesContext, String)
     * @since 1.2
     */
    public UIViewRoot restoreView(FacesContext context, String viewId) {

        return getWrapped().restoreView(context, viewId);

    }

    /**
     * <p>The default behavior of this method is to
     * call {@link ViewHandler#writeState(javax.faces.context.FacesContext)}
     * on the wrapped {@link ViewHandler} object.</p>
     *
     * @see ViewHandler#writeState(javax.faces.context.FacesContext)
     * @since 1.2
     */
    public void writeState(FacesContext context) throws IOException {
	getWrapped().writeState(context);

    }

}
