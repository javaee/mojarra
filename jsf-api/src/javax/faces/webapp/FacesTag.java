/*
 * $Id: FacesTag.java,v 1.36 2003/03/14 02:37:41 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.io.IOException;
import java.util.Iterator;
import java.util.HashMap;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.tree.Tree;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;


/**
 * <p><strong>FacesTag</strong> is a base class for all JSP custom actions
 * that correspond to user interface components in a page that is rendered by
 * JavaServer Faces.  Tags that need to process their tag bodies should
 * subclass {@link FacesBodyTag} instead.</p>
 *
 * <p>The <strong>id</strong> attribute of a <code>FacesTag</code> is used
 * to set the <code>componentId</code> property of the {@link UIComponent}
 * associated with this tag.</p>
 * </p>
 */

public abstract class FacesTag implements Tag {


    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The current index into the children of this tag's component.</p>
     */
    private int childIndex = 0;


    /**
     * <p>The {@link UIComponent} that is being encoded by this tag,
     * if any.</p>
     */
    private UIComponent component = null;


    /**
     * <p>The {@link FacesContext} for the request being processed, if any.
     * </p>
     */
    protected FacesContext context = null;


    /**
     * <p>Was a new component instance dynamically created when our
     * <code>findComponent()</code> method was called.</p>
     */
    private boolean created = false;


    /**
     * <p>The JSP <code>PageContext</code> for the page we are embedded in.</p>
     */
    protected PageContext pageContext = null;


    /**
     * <p>The JSP <code>Tag</code> that is the parent of this tag.</p>
     */
    private Tag parent = null;


    // ------------------------------------------------------------- Attributes


    /**
     * <p>The component identifier for the associated component.</p>
     */
    protected String id = null;


    /**
     * <p>Set the component identifier for our component.</p>
     *
     * @param id The new component identifier
     */
    public void setId(String id) {

        this.id = id;

    }


    /**
     * <p>An override for the rendered attribute associated with our
     * {@link UIComponent}, if not <code>true</code>.</p>
     */
    protected boolean rendered = true;


    /**
     * <p>Flag indicating whether the <code>rendered</code> attribute was
     * set on this tag instance.</p>
     */
    protected boolean renderedSet = false;
    

    /**
     * <p>Set an override for the rendered attribute.</p>
     *
     * @param rendered The new value for rendered attribute
     */
    public void setRendered(boolean rendered) {

        this.rendered = rendered;
        this.renderedSet = true;

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the {@link UIComponent} instance that is associated with
     * this tag instance.  This method is designed to be used by tags nested
     * within this tag, and only returns useful results between the
     * execution of <code>doStartTag()</code> and <code>doEndTag()</code>
     * on this tag instance.</p>
     */
    public UIComponent getComponent() {

        return (this.component);

    }


    /**
     * <p>Return <code>true</code> if we dynamically created a new component
     * instance during execution of this tag.  This method is designed to be
     * used by tags nested within this tag, and only returns useful results
     * between the execution of <code>doStartTag()</code> and
     * <code>doEndTag()</code> on this tag instance.</p>
     */
    public boolean getCreated() {

        return (this.created);

    }


    /**
     * <p>Return the <code>rendererType</code> property that selects the
     * <code>Renderer</code> to be used for encoding this component, or
     * <code>null</code> to ask the component to render itself directly.
     * Subclasses must override this method to return the appropriate value.
     * </p>
     */
    public abstract String getRendererType();


    // --------------------------------------------------------- Tag Properties


    /**
     * <p>Set the <code>PageContext</code> of the page containing this
     * tag instance.</p>
     *
     * @param pageContext The enclosing <code>PageContext</code>
     */
    public void setPageContext(PageContext pageContext) {

        this.pageContext = pageContext;

    }


    /**
     * <p>Return the <code>Tag</code> that is the parent of this instance.</p>
     */
    public Tag getParent() {

        return (this.parent);

    }


    /**
     * <p>Set the <code>Tag</code> that is the parent of this instance.
     * In addition, locate the closest enclosing <code>FacesTag</code> and
     * increment its <code>numChildren</code> counter.  Finally, save our
     * <code>childIndex</code> as
     * <code>(enclosingFacesTag.numChildren - 1)</code>.</p>
     *
     * @param parent The new parent <code>Tag</code>
     */
    public void setParent(Tag parent) {

        this.parent = parent;

    }


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
     * <li>Locate the component (in the component tree) corresponding
     *     to this tag, creating a new one if necesary.</li>
     * <li>Override the attributes of the associated component with values
     *     set in our custom tag attributes, if values for the corresponding
     *     attributes are <strong>NOT</strong> already set on the component.
     *     </li>
     * <li>Push this component onto the stack of components corresponding to
     *     nested component tags for the current response, creating the stack
     *     if necessary.</li>
     * <li>Call the <code>encodeBegin()</code> method of the component.</li>
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
        // PENDING(craigmcc) - Make this more efficient by doing so
        // only in the outermost tag
        context = FacesContext.getCurrentInstance();
        if (context == null) { // FIXME - i18n
            throw new JspException("Cannot find FacesContext");
        }

        // Set up the ResponseWriter as needed
        setupResponseWriter();

        // Locate and configure the component that corresponds to this tag
        childIndex = 0;
        component = findComponent();
        overrideProperties(component);

        // Render the beginning of the component associated with this tag
        try {
            if (!isSuppressed()) {
                encodeBegin();
            }
        } catch (IOException e) {
            component = null;
            context = null;
            throw new JspException(e);
        }

        // Return the appropriate control value
        return (getDoStartValue());

    }


    /**
     * <p>Render the ending of the {@link UIComponent} that is associated
     * with this tag (via the <code>id</code> attribute), by following these
     * steps.</p>
     * <ul>
     * <li>If the <code>rendersChildren</code> property of this component is
     *     <code>true</code>, call the <code>encodeChildren()</code> method
     *     of the component.</li>
     * <li>Call the <code>encodeEnd()</code> method of the component.</li>
     * <li>Release all references to the component, and pop it from
     *     the component stack for this response, removing the stack
     *     if this was the outermost component.</li>
     * </ul>
     *
     * <p>The flag value to be returned is acquired by calling the
     * <code>getDoEndValue()</code> method, which tag subclasses may
     * override if they do not want the default value.</p>
     *
     * @exception JspException if an error occurs
     */
    public int doEndTag() throws JspException {

        // Render the children (if needed) and  end of the component
        // associated with this tag
        try {
            if (!isSuppressed()) {
                if (component.getRendersChildren()) {
                    encodeChildren();
                }
                encodeEnd();
            }
        } catch (IOException e) {
            throw new JspException(e);
        } finally {
            component = null;
            context = null;
        }

        // Return the appropriate control value
        childIndex = 0;
        created = false;
        return (getDoEndValue());

    }


    /**
     * <p>Release any resources allocated during the execution of this
     * tag handler.</p>
     */
    public void release() {

        this.parent = null;

        this.id = null;
        this.created = false;
        this.rendered = true;
        this.renderedSet = false;
    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Create and return a new {@link UIComponent} that is acceptable
     * to this tag.  Concrete subclasses must override this method.</p>
     */
    protected abstract UIComponent createComponent();


    /**
     * <p>Delegate to the <code>encodeBegin()</code> method of our
     * corresponding {@link UIComponent}.  This method is called from
     * <code>doStartTag()</code>.  Normally, delegation occurs unconditionally;
     * however, this method is abstracted out so that advanced tags can
     * conditionally perform this call.
     *
     * @exception IOException if an input/output error occurs
     */
    protected void encodeBegin() throws IOException {

        component.encodeBegin(context);

    }


    /**
     * <p>Delegate to the <code>encodeChildren()</code> method of our
     * corresponding {@link UIComponent}.  This method is called from
     * <code>doStartTag()</code>.  Normally, delegation occurs unconditionally;
     * however, this method is abstracted out so that advanced tags can
     * conditionally perform this call.
     *
     * @exception IOException if an input/output error occurs
     */
    protected void encodeChildren() throws IOException {

        component.encodeChildren(context);

    }


    /**
     * <p>Delegate to the <code>encodeEnd()</code> method of our
     * corresponding {@link UIComponent}.  This method is called from
     * <code>doStartTag()</code>.  Normally, delegation occurs unconditionally;
     * however, this method is abstracted out so that advanced tags can
     * conditionally perform this call.
     *
     * @exception IOException if an input/output error occurs
     */
    protected void encodeEnd() throws IOException {

        component.encodeEnd(context);

    }


    /**
     * <p>Find and return the {@link UIComponent}, from the component
     * tree, that corresponds to this tag handler instance.  If there
     * is no such {@link UIComponent}, create one by calling
     * <code>createComponent()</code>, and add it is a child or facet
     * of the {@link UIComponent} associated with our nearest enclosing
     * {@link FacesTag}.</p>
     *
     */
    protected UIComponent findComponent() throws JspException {

        // Have we already found the relevant component?
        if (component != null) {
            return (component);
        }

        // Identify the component that is, or will be, our parent
        FacesTag parentTag = getParentFacesTag();
        UIComponent parentComponent = null;
        boolean parentCreated = false;
        if (parentTag != null) {
            parentComponent = parentTag.getComponent();
            parentCreated = parentTag.getCreated();
        } else {
            parentComponent = context.getTree().getRoot();
            parentCreated = parentComponent.getChildCount() < 1;
        }

        // Case 1 -- Our parent was just created, so we must do so also
        if (parentCreated) {

            // Create a new component instance
            component = createComponent();
            if (id != null) {
                component.setComponentId(id);
            }
            created = true;

            // Add it as a facet or a child
            String facetName = getFacetName();
            if (facetName != null) {
                parentComponent.addFacet(facetName, component);
            } else {
                parentComponent.addChild(component);
            }

            // Return the newly created component
            return (component);

        }

        // Case 2 -- Our parent was not created, so locate ourself
        String facetName = getFacetName();
        if (facetName != null) {

            // Case 2A -- Look up facet by name
            component = parentComponent.getFacet(facetName);
            // PENDING - what if it's not there?

        } else {

            // Case 2B -- Look up child by position
            if (parentTag != null) {
                component =
                    parentComponent.getChild(parentTag.getChildIndex());
                parentTag.incrementChildIndex();
            } else {
                component = parentComponent.getChild(0);
            }
            // PENDING - what if it's not there?

        }

        // Return the located component
        return (component);

    }


    /**
     * <p>Return the current index into the children of this tag's
     * corresponding {@link UIComponent}.</p>
     */
    protected int getChildIndex() {

        return (this.childIndex);

    }


    /**
     * <p>Return the flag value that should be returned from the
     * <code>doEnd()</code> method when it is called.  Subclasses
     * may override this method to return the appropriate value.</p>
     *
     * @exception JspException to cause <code>doEnd()</code> to
     *  throw an exception
     */
    protected int getDoEndValue() throws JspException {

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
    protected int getDoStartValue() throws JspException {

        return (EVAL_BODY_INCLUDE);

    }


    /**
     * <p>Return the facet name that we should be stored under, if any;
     * otherwise, return null (indicating that we will be a child component).
     * </p>
     */
    protected String getFacetName() {

        Tag parent = getParent();
        if (parent instanceof FacetTag) {
            return (((FacetTag) parent).getName());
        } else {
            return (null);
        }

    }


    /**
     * <p>Locate and return the nearest enclosing {@link FacesTag} if any;
     * otherwise, return <code>null</code>.</p>
     */
    protected FacesTag getParentFacesTag() {

        Tag tag = getParent();
        while (tag != null) {
            if (tag instanceof FacesTag) {
                return ((FacesTag) tag);
            }
            tag = tag.getParent();
        }
        return (null);

    }
    

    /**
     * <p>Increment the index into the children of this tag's component.</p>
     */
    protected void incrementChildIndex() {

        childIndex++;

    }


    /**
     * <p>Return <code>true</code> if rendering should be suppressed because
     * our component is a facet, or some parent component has been configured
     * with <code>getRendersChildren()</code> as true.</p>
     */
    protected boolean isSuppressed() {

        if (getFacetName() != null) {
            return (true);
        }
        UIComponent component = this.component.getParent();
        while (component != null) {
            if (component.getRendersChildren()) {
                return (true);
            }
            component = component.getParent();
        }
        return (false);

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
        if (renderedSet) {
            component.setRendered(rendered);
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
            writer = new JspResponseWriter(pageContext);
            context.setResponseWriter(writer);
        }

    }


}
