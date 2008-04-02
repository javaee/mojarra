/*
 * $Id: UIViewRoot.java,v 1.3 2003/09/25 07:56:14 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;


/**
 * <p><strong>UIViewRoot</strong> is the UIComponent that represents the
 * root of the UIComponent tree.  This component has no rendering, it
 * just serves as the root of the component tree.</p>
 */

public class UIViewRoot extends UIComponentBase implements NamingContainer {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIViewRoot} instance with default property
     * values.</p>
     */
    public UIViewRoot() {

        super();
        setRendererType(null);

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link NamingContainer} implementation that we delegate to
     */
    private NamingContainerSupport namespace = new NamingContainerSupport();


    // -------------------------------------------------------------- Properties


    /**
     * <p>The render kit identifier of the {@link RenderKit} associated
     * wth this view.</p>
     */
    private String renderKitId = RenderKitFactory.DEFAULT_RENDER_KIT;


    /**
     * <p>Return the render kit identifier of the {@link RenderKit}
     * associated with this view.</p>
     */
    public String getRenderKitId() {

        return (this.renderKitId);

    }


    /**
     * <p>Set the render kit identifier of the {@link RenderKit}
     * associated with this view.</p>
     *
     * @param renderKitId The new {@link RenderKit} identifier,
     *  or <code>null</code> to disassociate this view with any
     *  specific {@link RenderKit} instance
     */
    public void setRenderKitId(String renderKitId) {

        this.renderKitId = renderKitId;

    }


    /**
     * <p>The view identifier of this view.</p>
     */
    private String viewId = null;


    /**
     * <p>Return the view identifier for this view.</p>
     */
    public String getViewId() {

        return (this.viewId);

    }


    /**
     *
     * <p>Set the view identifier for this view.</p>
     *
     * @param viewId The new view identifier
     */
    public void setViewId(String viewId) {

        this.viewId = viewId;

    }


    // ------------------------------------------------- NamingContainer Methods


    public void addComponentToNamespace(UIComponent namedComponent) {

	namespace.addComponentToNamespace(namedComponent);

    }


    public UIComponent findComponentInNamespace(String name) {

	return namespace.findComponentInNamespace(name);

    }


    public synchronized String generateClientId() {

	return namespace.generateClientId();

    }


    public void removeComponentFromNamespace(UIComponent namedComponent) {

	namespace.removeComponentFromNamespace(namedComponent);

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = renderKitId;
        values[2] = viewId;
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        renderKitId = (String) values[1];
        viewId = (String) values[2];

    }


}
