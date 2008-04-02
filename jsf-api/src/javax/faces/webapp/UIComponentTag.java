/*
 * $Id: UIComponentTag.java,v 1.8 2003/07/19 04:52:59 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;
import javax.faces.FactoryFinder;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.el.ValueBinding;
import javax.faces.tree.Tree;
import javax.faces.application.Application;
import javax.servlet.jsp.JspException;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;


/**
 * <p><strong>UIComponentTag</strong> is the base class for all JSP custom
 * actions that correspond to user interface components in a page that is
 * rendered by JavaServer Faces.  Tags that need to process their tag bodies
 * should subclass {@link UIComponentBodyTag} instead.</p>
 */

public abstract class UIComponentTag implements Tag {


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
     * <p>The component reference (if any) used to wire up this component
     * to a {@link UIComponent} property of a JavaBean class.</p>
     */
    private String componentRef = null;


    /**
     * <p>Set the component reference for our component.</p>
     *
     * @param componentRef The new component reference
     */
    public void setComponentRef(String componentRef) {

	this.componentRef = componentRef;

    }


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
     * {@link UIComponent}.</p>
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
     * <p>Return the component type for the component that is or will be
     * bound to this tag.  This value can be passed to
     * {@link javax.faces.application.Application#getComponent} to create
     * the {@link UIComponent} instance for this tag.  Subclasses must
     * override this method to return the appropriate value.</p>
     */
    protected abstract String getComponentType();


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
     * <p>Set the <code>Tag</code> that is the parent of this instance.</p>
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
     * <li>Call the <code>encodeBegin()</code> method of the component,
     *     unless rendering is suppressed or our component renders its
     *     own children.</li>
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
        if (context == null) { // PENDING - i18n
            throw new JspException("Cannot find FacesContext");
        }

        // Set up the ResponseWriter as needed
        setupResponseWriter();

        // Locate and configure the component that corresponds to this tag
        childIndex = 0;
        component = findComponent(context);

        // Render the beginning of the component associated with this tag
        try {
            if (!isSuppressed() && !component.getRendersChildren()) {
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
     *     <code>true</code>, call the <code>encodeBegin()</code> method
     *     of this component.
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
                    encodeBegin();
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

	this.componentRef = null;
        this.id = null;
        this.created = false;
        this.rendered = true;
        this.renderedSet = false;
    }


    // ------------------------------------------------------ Protected Methods


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
     * is no such {@link UIComponent}, perform the following algorithm
     * to create one that can be returned:</p>
     * <ul>
     * <li>If this tag has no <code>componentRef</code> attribute value,
     *     call <code>Application.getComponent(String)</code>,
     *     passing the result of calling <code>getComponentType()</code> on
     *     this tag instance.</li>
     * <li>If this tag has a <code>componentRef</code> attribute value,
     *     call <code>Application.getComponent(String, FacesContext,
     *     String)</code>, passing a {@link ValueBinding} based on the
     *     <code>componentRef</code> attribute value, the {@link FacesContext}
     *     for the current request, and the result of calling
     *     <code>getComponentType() on this tag instance.</li>
     * <li>After the component instance has been created by either of the
     *     above mechanisms, call <code>overrideProperties()</code> to copy
     *     values from the attributes of this tag instance to the corresponding
     *     attributes and properties of the component instance.</li>
     * </ul>
     */
    protected UIComponent findComponent(FacesContext context)
	throws JspException {

        // Have we already found the relevant component?
        if (component != null) {
            return (component);
        }

        // Identify the component that is, or will be, our parent
        UIComponentTag parentTag = getParentUIComponentTag();
        UIComponent parentComponent = null;
        boolean thisTagIsRoot = false;
	boolean parentCreated = false;
        if (parentTag != null) {
            parentComponent = parentTag.getComponent();
            parentCreated = parentTag.getCreated();
        } else {
	    // If there is no parent tag, this tag must be the root.
	    thisTagIsRoot = true;
            parentComponent = context.getTree().getRoot();
            parentCreated = parentComponent.getChildCount() < 1;
        }

        // Case 1 -- Our parent was just created, so we must do so also
        if (parentCreated) {

	    if (thisTagIsRoot) {
		component = parentComponent;
	    }
	    else {
		// Create a new component instance
		try {
		    Application application = context.getApplication();
		    ValueBinding binding = null;
		    if (this.componentRef != null) {
			binding = application.getValueBinding(componentRef);
			component = application.getComponent
			    (binding, context, getComponentType());
			overrideProperties(component);
			binding.setValue(context, component);
		    } else {
			component =
			    application.getComponent(getComponentType());
			overrideProperties(component);
		    }
		}
		catch (FacesException e) {
		    throw new JspException(e);
		}
	    }
            created = true;

            // Add it as a facet or a child
            String facetName = getFacetName();
	    // protect from adding us to ourself
	    if (parentComponent != component) {
		if (facetName != null) {
		    parentComponent.addFacet(facetName, component);
		} else {
		    parentComponent.addChild(component);
		}
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
		// The only case where parentTag == null is the root
		// tag, therefore, the component is the rootComponent.
                component = context.getTree().getRoot();
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
     * <p>Locate and return the nearest enclosing {@link UIComponentTag} if any;
     * otherwise, return <code>null</code>.</p>
     */
    protected UIComponentTag getParentUIComponentTag() {

        Tag tag = getParent();
        while (tag != null) {
            if (tag instanceof UIComponentTag) {
                return ((UIComponentTag) tag);
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
     * <p>Override properties and attributes of the specified component,
     * if the corresponding properties of this tag handler instance were
     * explicitly set.  This method will be called <strong>ONLY</strong>
     * if the specified {@link UIComponent} was in fact created during
     * the execution of this tag handler instance, and this call will occur
     * <strong>BEFORE</strong> the {@link UIComponent} is added to
     * the component tree.</p>
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
     *
     * <p>The default implementation overrides the following properties:</p>
     * <ul>
     * <li><code>componentId</code> - Set if a value for the
     *     <code>id</code> property is specified for
     *     this tag handler instance.</li>
     * <li><code>componentRef</code> - Set if a value for the
     *     <code>componentRef</code> property is specified for
     *     this tag handler instance.</li>
     * <li><code>rendered</code> - Set if a value for the
     *     <code>rendererd</code> property is specified for
     *     this tag handler instance.</li>
     * <li><code>rendererType</code> - Set if the <code>getRendererType()</code>
     *     method returns a non-null value.</li>
     * </ul>
     *
     * @param component {@link UIComponent} whose properties are to be
     *  overridden
     */
    protected void overrideProperties(UIComponent component) {

	if (componentRef != null) {
	    component.setComponentRef(componentRef);
	}
	if (id != null) {
	    component.setComponentId(id);
	}
        if (renderedSet) {
            component.setRendered(rendered);
        }
	if (getRendererType() != null) {
	    component.setRendererType(getRendererType());
	}

    }


    /**
     * <p>Set up the {@link ResponseWriter} for the current response,
     * if this has not been done already.</p>
     */
    protected void setupResponseWriter() {

        ResponseWriter writer = context.getResponseWriter();
        if (writer == null) {
	    RenderKitFactory renderFactory = (RenderKitFactory)
		FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	    RenderKit renderKit = 
		renderFactory.getRenderKit(context.getTree().getRenderKitId());
	    ServletResponse response = (ServletResponse)
		context.getExternalContext().getResponse();
            writer = 
		renderKit.getResponseWriter(new Writer() {
		    public void close() throws IOException {
			pageContext.getOut().close();
		    }
		    public void flush() throws IOException {
			pageContext.getOut().flush();
		    }
		    public void write(char cbuf) throws IOException {
			pageContext.getOut().write(cbuf);
		    }
		    public void write(char[] cbuf, int off, 
				      int len) throws IOException {
			pageContext.getOut().write(cbuf, off, len);
		    }
		    public void write(int c) throws IOException {
			pageContext.getOut().write(c);
		    }
		    public void write(String str) throws IOException {
			pageContext.getOut().write(str);
		    }
		    public void write(String str, int off, 
				      int len) throws IOException {
			pageContext.getOut().write(str, off, len);
		    }
		},
					    response.getCharacterEncoding());
            context.setResponseWriter(writer);
        }

    }


}
