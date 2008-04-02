/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * $Id: ViewHandlerWrapper.java,v 1.3 2004/10/18 22:46:02 edburns Exp $
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
