/*
 * $Id: FacesTag.java,v 1.6 2002/06/07 20:15:52 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import javax.faces.tree.Tree;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p><strong>FacesTag</strong> is a base class for all JSP custom actions
 * that correspond to user interface components in a page that is rendered by
 * JavaServer Faces.  Tags that need to process their tag bodies should
 * subclass {@link FacesBodyTag} instead.</p>
 *
 * <p>The <strong>id</strong> attribute of a <code>FacesTag</code> is used
 * to identify the corresponding component in the response component tree.
 * It may contain either the absolute <em>compound identifier</em> (starting
 * with a '/' character) of the corresponding component, or a relative
 * expression that is resolved from the closest surrounding
 * <code>FacesTag</code> instance as if by a call to the
 * <code>findComponent()</code> method, passing
 * the specified <code>id</code> value as a paramter.</p>
 */

public abstract class FacesTag extends TagSupport {


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The {@link UIComponent} that is being encoded by this tag,
     * if any.</p>
     */
    protected UIComponent component = null;


    /**
     * <p>The {@link FacesContext} for the request being processed, if any.
     * </p>
     */
    protected FacesContext context = null;


    /**
     * <p>The {@link Renderer} to which we should delegate encoding for
     * the component associated with this tag, if any.</p>
     */
    protected Renderer renderer = null;


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the flag value that should be returned from the
     * <code>doEnd()</code> method when it is called.  Subclasses
     * May override this method to return the appropriate value.</p>
     *
     * @exception JspException to cause <code>doEnd()</code> to
     *  throw an exception
     */
    public int getDoEndValue() throws JspException {

        return (EVAL_PAGE);

    }


    /**
     * <p>Return the flag value that should be returned from the
     * <code>doStart()</code> method when it is called.  Subclasses
     * may override this method to return the appropriate value.</p>
     *
     * @exception JspException to cause <code>doStart()</code> to
     *  throw an exception
     */
    public int getDoStartValue() throws JspException {

        return (EVAL_BODY_INCLUDE);

    }


    /**
     * <p>An override for the model reference expression associated with our
     * {@link UIComponent}, if not <code>null</code>.</p>
     */
    protected String model = null;


    /**
     * <p>Return the override for the model reference expression.</p>
     */
    public String getModel() {

        return (this.model);

    }


    /**
     * <p>Set an override for the model reference expression.</p>
     *
     * @param model The new model reference expression
     */
    public void setModel(String model) {

        this.model = model;

    }


    /**
     * <p>Return the <code>rendererType</code> property that selects the
     * <code>Renderer</code> to be used for encoding this component.
     * Subclasses must override this method to return the appropriate value.
     * </p>
     */
    public abstract String getRendererType();


    // ------------------------------------------------------------ Tag Methods


    /**
     * <p>Render the beginning of the {@link UIComponent} that is associated
     * with this tag (via the <code>id</code> attribute), by following these
     * steps.</p>
     * <ul>
     * <li>Ensure that an appropriate {@link ResponseWriter} is associated
     *     with the current {@link FacesContext}.  This ensures that encoded
     *     output from the components is routed through the
     *     <code>JspWriter</code> for the current page.</li>
     * <li>Use the <code>findComponent()</code> method to acquire a reference
     *     to the {@link UIComponent} associated with this tag.  Save the
     *     acquired reference in the <code>component</code> instance variable.
     *     </li>
     * <li>If the <code>rendererType</code> property of this component is not
     *     null, acquire a reference to the corresponding {@link Renderer} from
     *     the {@link RenderKit} associated with this response.  Save the
     *     acquired reference in the <code>renderer</code> instance variable.
     *     </li>
     * <li>Call the <code>encodeBegin()</code> method of the component (if
     *     <code>rendererType</code> is <code>null</code>) or the
     *     {@link Renderer} (if <code>rendererType</code> is not
     *     <code>null</code>).</li>
     * <li>If the <code>rendersChildren</code> property of this component is
     *     <code>true</code>, call the <code>encodeChildren()</code> method
     *     of the component (if <code>rendererType</code> is <code>null</code>)
     *     or the {@link Renderer} (if <code>rendererType</code> is not
     *     <code>null</code>).</li>
     * </ul>
     *
     * <p>The flag value to be returned is acquired by calling the
     * <code>getDoStartValue()</code> method, which tag subclasses may
     * override if they do not want the default value.</p>
     *
     * @exception JspException if an error occurs
     */
    public int doStart() throws JspException {

        // Ensure that an appropriate ResponseWriter is available
        context = (FacesContext)
            pageContext.getAttribute(FacesContext.FACES_CONTEXT_ATTR,
                                     PageContext.REQUEST_SCOPE);
        if (context == null) { // FIXME - i18n
            throw new JspException("Cannot find FacesContext");
        }
        ResponseWriter writer = context.getResponseWriter();
        if ((writer == null) ||
            !(writer instanceof JspResponseWriter)) {
            writer = new JspResponseWriter(pageContext.getOut());
            context.setResponseWriter(writer);
        }

        // Locate and configure the component that corresponds to this tag
        component = findComponent();
        overrideProperties(component);
        boolean rendersChildren = component.getRendersChildren();

        // Render the beginning of the component associated with this tag
        String rendererType = component.getRendererType();
        if (rendererType == null) {
            renderer = null;
            try {
                component.encodeBegin(context);
                if (rendersChildren) {
                    component.encodeChildren(context);
                }
            } catch (IOException e) {
                component = null;
                context = null;
                throw new JspException(e);
            }
        } else {
            RenderKit renderKit = context.getResponseTree().getRenderKit();
            if (renderKit == null) { // FIXME - i18n
                throw new JspException("Cannot find RenderKit");
            }
            try {
                renderer = renderKit.getRenderer(rendererType);
            } catch (IllegalArgumentException e) { // FIXME - i18n
                component = null;
                context = null;
                throw new JspException("Cannot find Renderer '" +
                                       rendererType + "'");
            }
            try {
                renderer.encodeBegin(context, component);
                if (rendersChildren) {
                    renderer.encodeChildren(context, component);
                }
            } catch (IOException e) {
                component = null;
                context = null;
                renderer = null;
                throw new JspException(e);
            }
        }

        // Return the appropriate control value
        return (getDoStartValue());

    }


    /**
     * <p>Render the ending of the {@link UIComponent} that is associated
     * with this tag (via the <code>id</code> attribute).  This is accomplished
     * by calling the <code>encodeEnd()</code> method of the
     * {@link UIComponent} (if its <code>rendererType</code> property is not
     * set) or the associated {@link Renderer} (if <code>rendererType</code>
     * was set).  After rendering is complete, release any references to the
     * {@link UIComponent} and {@link Renderer} saved during execution of
     * <code>doStart()</code>.</p>
     *
     * <p>The flag value to be returned is acquired by calling the
     * <code>getDoEndValue()</code> method, which tag subclasses may
     * override if they do not want the default value.</p>
     *
     * @exception JspException if an error occurs
     */
    public int doEnd() throws JspException {

        // Render the ending of the component associated with this tag
        try {
            if (renderer == null) {
                component.encodeEnd(context);
            } else {
                renderer.encodeEnd(context, component);
            }
        } catch (IOException e) {
            throw new JspException(e);
        } finally {
            component = null;
            context = null;
            renderer = null;
        }

        // Return the appropriate control value
        return (getDoEndValue());

    }


    /**
     * <p>Release any resources allocated during the execution of this
     * tag handler.</p>
     */
    public void release() {

        super.release();
        this.id = null;
        this.model = null;

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Find and return the component, from the response component tree,
     * that corresponds to the absolute or relative identifier defined in the
     * <code>id</code> attribute of this tag.</p>
     *
     * @exception JspException if the specified component cannot be located
     */
    protected UIComponent findComponent() throws JspException {

        // Acquire a reference to the response component tree
        FacesContext context = (FacesContext)
            pageContext.getAttribute(FacesContext.FACES_CONTEXT_ATTR,
                                     PageContext.REQUEST_SCOPE);
        if (context == null) { // FIXME - i18n
            throw new JspException("Cannot locate FacesContext");
        }
        Tree tree = context.getResponseTree();
        if (tree == null) { // FIXME - i18n
            throw new JspException("Cannot locate Tree");
        }

        // Process an absolute identifier
        String id = getId();
        if (id == null) { // FIXME - i18n
            throw new JspException("No id attribute specified");
        } else if (id.length() < 1) { // FIXME - i18n
            throw new JspException("Zero-length id attribute specified");
        } else if (id.startsWith("/")) {
            UIComponent root = tree.getRoot();
            if (root == null) { // FIXME - i18n
                throw new JspException("Cannot locate root node");
            }
            try {
                return (root.findComponent(id));
            } catch (IllegalArgumentException e) {
                throw new JspException(e);
            }
        }

        // Process a relative identifier
        Tag tag = getParent();
        while (true) {
            if (tag == null) { // FIXME - i18n
                throw new JspException("Cannot find parent FacesTag");
            }
            if (tag instanceof FacesTag) {
                break;
            }
            tag = tag.getParent();
        }
        FacesTag parent = (FacesTag) tag;
        try {
            UIComponent component = parent.findComponent();
            return (component.findComponent(id));
        } catch (IllegalArgumentException e) { // FIXME - i18n
            throw new JspException("Cannot find parent FacesTag component");
        }

    }


    /**
     * <p>Override properties of the specified component if the corresponding
     * properties of this tag handler were explicitly set.</p>
     *
     * <p>Tag subclasses that want to support additional override properties
     * must ensure that the base class <code>overrideProperties()</code>
     * method is still called.  A typical implementation that supports
     * extra properties <code>foo</code> and <code>bar</code> would look
     * something like this:</p>
     * <pre>
     * protected void overrideProperties(UIComponent component) {
     *   super.overrideProperties(component);
     *   if (foo != null) {
     *     component.setAttribute("foo", foo);
     *   }
     *   if (bar != null) {
     *     component.setAttribute("bar", bar);
     *   }
     * }
     * </pre>
     */
    protected void overrideProperties(UIComponent component) {

        // The rendererType property is always overridden
        component.setRendererType(getRendererType());

        // Override other properties as required
        if (model != null) {
            component.setModel(model);
        }

    }


}
