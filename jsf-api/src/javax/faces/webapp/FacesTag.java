/*
 * $Id: FacesTag.java,v 1.12 2002/06/27 21:47:28 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
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
     * <p>The request attribute under which our component stack is stored.</p>
     */
    protected static final String COMPONENT_STACK_ATTR =
        "javax.faces.webapp.FacesTag.COMPONENT_STACK";


    /**
     * <p>The {@link UIComponent} that is being encoded by this tag,
     * if any.</p>
     */
    protected UIComponent component = null;


    /**
     * <p>The {@link UIComponent} stack representing the current nesting
     * of components for the current response.</p>
     */
    protected Stack componentStack = null;


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

        if (component.getRendersChildren()) {
            return (SKIP_BODY);
        } else {
            return (EVAL_BODY_INCLUDE);
        }

    }


    /**
     * <p>An override for the model reference expression associated with our
     * {@link UIComponent}, if not <code>null</code>.</p>
     */
    protected String modelReference = null;


    /**
     * <p>Return the override for the model reference expression.</p>
     */
    public String getModelReference() {

        return (this.modelReference);

    }


    /**
     * <p>Set an override for the model reference expression.</p>
     *
     * @param modelReference The new model reference expression
     */
    public void setModelReference(String modelReference) {

        this.modelReference = modelReference;

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
     * <li>FIXME - update the look-then-create description.</li>
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
    public int doStartTag() throws JspException {

        // Look up the FacesContext instance for this request
        context = (FacesContext)
            pageContext.getAttribute(FacesContext.FACES_CONTEXT_ATTR,
                                     PageContext.REQUEST_SCOPE);
        if (context == null) { // FIXME - i18n
            throw new JspException("Cannot find FacesContext");
        }

        // Set up the ResponseWriter as needed
        setupResponseWriter();

        // Locate and configure the component that corresponds to this tag
        componentStack = findComponentStack();
        component = findComponent();
        overrideProperties(component);
        boolean rendersChildren = component.getRendersChildren();
        componentStack.push(component);

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
                componentStack.pop();
                componentStack = null;
                throw new JspException(e);
            }
        } else {
            RenderKit renderKit = context.getResponseTree().getRenderKit();
            if (renderKit == null) { // FIXME - i18n
                throw new JspException("Cannot find RenderKit");
            }
            // FIXME - deal with direct output components!
            try {
                renderer = renderKit.getRenderer(rendererType);
            } catch (IllegalArgumentException e) { // FIXME - i18n
                component = null;
                context = null;
                componentStack.pop();
                componentStack = null;
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
                componentStack.pop();
                componentStack = null;
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
    public int doEndTag() throws JspException {

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

        // Pop the component stack
        componentStack.pop();
        componentStack = null;

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
        this.modelReference = null;

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Create and return a new {@link UIComponent} that is acceptable
     * to this tag.  Concrete subclasses must override this method.</p>
     */
    public abstract UIComponent createComponent();


    /**
     * <p>Find and return the component, from the response component tree,
     * that corresponds to the relative identifier defined by the
     * <code>id</code> attribute of this tag.  If no such component can
     * be found, create an appropriate instance.</p>
     *
     * @exception JspException if the specified component cannot be located
     */
    protected UIComponent findComponent() throws JspException {

        // Validate the requested identifier
        String id = getId();
        if (id == null) { // FIXME - i18n
            throw new JspException("No id attribute specified");
        } else if (id.length() < 1) { // FIXME - i18n
            throw new JspException("Zero-length id attribute specified");
        } else if (id.startsWith("/") || id.startsWith(".")) { // FIXME - i18n
            throw new JspException("Only simple id values allowed");
        }

        // Ask the top component on the stack if it has a child of this id
        UIComponent parent = (UIComponent) componentStack.peek();
        UIComponent child = null;
        Iterator children = parent.getChildren();
        while (children.hasNext()) {
            child = (UIComponent) children.next();
            if (id.equals(child.getComponentId())) {
                return (child);
            }
        }

        // Create and return a new child component of the appropriate type
        child = createComponent();
        child.setComponentId(id);
        parent.addChild(child);
        return (child);

    }


    /**
     * <p>Locate and return the component stack for this response,
     * creating one if this has not been done already.</p>
     */
    protected Stack findComponentStack() {

        Stack componentStack = (Stack)
            pageContext.getAttribute(COMPONENT_STACK_ATTR,
                                     PageContext.REQUEST_SCOPE);
        if (componentStack == null) {
            componentStack = new Stack();
            componentStack.push(context.getResponseTree().getRoot());
            pageContext.setAttribute(COMPONENT_STACK_ATTR,
                                     componentStack,
                                     PageContext.REQUEST_SCOPE);
        }
        return (componentStack);

    }


    /**
     * <p>Override properties of the specified component if the corresponding
     * properties of this tag handler were explicitly set, and the
     * corresponding attribute of the component is not set.</p>
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
        if ((modelReference != null) &&
            (component.getModelReference() == null)) {
            component.setModelReference(modelReference);
        }

    }


    /**
     * <p>Set up the {@link ResponseWriter} for the current response,
     * if this has not been done already.</p>
     */
    protected void setupResponseWriter() {

        ResponseWriter writer = context.getResponseWriter();
        if ((writer == null) ||
            !(writer instanceof JspResponseWriter)) {
            writer = new JspResponseWriter(pageContext.getOut());
            context.setResponseWriter(writer);
        }

    }


}
