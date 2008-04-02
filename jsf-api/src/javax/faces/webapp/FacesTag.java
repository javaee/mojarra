/*
 * $Id: FacesTag.java,v 1.30 2003/01/17 02:30:16 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
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
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p><strong>FacesTag</strong> is a base class for all JSP custom actions
 * that correspond to user interface components in a page that is rendered by
 * JavaServer Faces.  Tags that need to process their tag bodies should
 * subclass {@link FacesBodyTag} instead.</p>
 *
 * <p>The <strong>id</strong> attribute of a <code>FacesTag</code> is used
 * to identify the corresponding component in the response component tree.
 * </p>
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
     * <p>Was a new component instance dynamically created when our
     * <code>findComponent()</code> method was called.</p>
     */
    protected boolean created = false;


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
     * instance during our <code>doStartTag()</code> method.</p>
     */
    public boolean getCreated() {

        return (this.created);

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
     * <p>An override for the rendered attribute associated with our
     * {@link UIComponent}, if not <code>true</code>.</p>
     */
    protected boolean rendered = true;
    
    /**
     * <p>Return the override for the rendered attribute.</p>
     */
    public boolean getRendered() {
        return this.rendered;
    }    
    
    /**
     * <p>Set an override for the rendered attribute.</p>
     *
     * @param rendered The new value for rendered attribute
     */
    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }


    /**
     * <p>Return the <code>rendererType</code> property that selects the
     * <code>Renderer</code> to be used for encoding this component, or
     * <code>null</code> to ask the component to render itself directly.
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
        try {
            if (!isSuppressed()) {
                encodeBegin();
            }
        } catch (IOException e) {
            component = null;
            context = null;
            componentStack.pop();
            componentStack = null;
            throw new JspException(e);
        }

        // Return the appropriate control value
        return (getDoStartValue());

    }


    /**
     * <p>Handle the ending of the nested body content for a tag that
     * implements <code>javax.servlet.jsp.tagext.IterationTag</code>,
     * as all subclasses of this class do.  The default implementation
     * simply calls <code>getDoAfterBodyValue()</code> to retrieve the
     * flag value to be returned.</p>
     *
     * @exception JspException if an error occurs
     */
    public int doAfterBody() throws JspException {

        return (getDoAfterBodyValue());

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
        boolean rendersChildren = component.getRendersChildren();
        try {
            if (!isSuppressed()) {
                if (rendersChildren) {
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

        // Pop the component stack, and release it if we are outermost
        componentStack.pop();
        componentStack = null;
        // Need to reset these ivars here because the release() is not
        // called right away after end tag is processed.
        this.numChildren = 0;
        this.childIndex = 0;
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
        this.created = false;
    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Create and return a new {@link UIComponent} that is acceptable
     * to this tag.  Concrete subclasses must override this method.</p>
     */
    public abstract UIComponent createComponent();


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
     * <p>Find and return the component, from the component
     * tree, that corresponds to the relative identifier defined by the
     * <code>id</code> attribute of this tag. If the value of the <code>id</code>
     * attribute is non null, see if there is a component with this
     * <code>id</code> in the namespace of the nearest ancestor to the
     * top of the stack that is a naming container.  If so, return that
     * component. If the <code>id</code> is null, generate a tagKey and use
     * it as a key into <code>tagHash</code> to locate the component. 
     * If component is found, return it. Otherwise create the component and 
     * store it in <code>tagHash</code> against the generated tagKey. 
     * <code>tagHash</code>is stored as an attribute in root component
     * so that it is available on postback. </p> 
     *
     * @exception JspException if the specified component cannot be located

     * @exception JspException if the id is non-null, and no
     * NamingContainer can be found.
     *
     */
    protected UIComponent findComponent() throws JspException {

        // Validate the requested identifier
        String tagKey = null;
        
        String id = getId();
        UIComponent root = context.getTree().getRoot();
        HashMap tagHash = (HashMap)root.getAttribute("tagHash");
        
        UIComponent parent = (UIComponent) componentStack.peek();
	UIComponent child = parent;
        if (id != null) { // FIXME - i18n
	    // find the nearest ancestor that is a naming container
	    NamingContainer closestContainer = null;
	    
	    while (!(child instanceof NamingContainer)) {
		child = child.getParent();
	    }
	    if (null == child) {
		throw new JspException("Can't find NamingContainer");
	    } 
	    closestContainer = (NamingContainer) child;
	    if (null != (child = 
			 closestContainer.findComponentInNamespace(id))) {
                created = false;
                return (child);
            }
        } else {
            // generate the tagKey and use it locate the component in tagHash
            tagKey = this.generateTagKey();
             if ( tagHash != null ) {
                 // if the component is found, then it is a postback case.
                 UIComponent component = (UIComponent)tagHash.get(tagKey);
                 if (component != null ) {
                     created = false;
                     return component;
                 }    
             } 
        }    

        // Create and return a new child component of the appropriate type
        child = createComponent();
	if (null != id) {
	    child.setComponentId(id);
	} else {
            if ( tagHash == null ) {
                // since the tagHash is null, request is processed 
                // for the first time. 
                tagHash = new HashMap();
            }
            // store this component in tagHash against the generated tagKey.
            root.setAttribute("tagHash", tagHash);
            tagHash.put(tagKey,child);
        }    
	// handle the case when we're supposed to be a facet
	Tag parentTag = null;
	if ((parentTag = this.getParent()) instanceof FacetTag) {
	    FacetTag parentFacetTag = (FacetTag)parentTag;
	    parentFacetTag.verifySingleChild();
	    parent.addFacet(parentFacetTag.getName(), child);
	}
	else {
	    parent.addChild(child);
	}
        created = true;
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
            componentStack.push(context.getTree().getRoot());
            pageContext.setAttribute(COMPONENT_STACK_ATTR,
                                     componentStack,
                                     PageContext.REQUEST_SCOPE);
        }
        return (componentStack);

    }

    /** 
     * Returns a string composed of one or more atoms, separated by ':', 
     * where an atom is: 
     * <This Tag ClassName>_<index Of This Tag In Parent's Child List>
     */
    private String generateTagKey() {
        String tagKey = "";
        Tag tagObj = this.getParent();
	FacesTag facesTag = null;
        while ( tagObj != null ) {
            if ( tagObj instanceof FacesTag) {
		facesTag = (FacesTag) tagObj;
                tagKey = 
		    facesTag.getClass().getName() + "_" + 
		    facesTag.getChildIndex() + ":" + tagKey;
            }
            tagObj = tagObj.getParent();             
        }
        tagKey = tagKey + this.getClass().getName() +"_"+ this.getChildIndex();
        return tagKey;
    }

    /**
     * This ivar is the number of children we have.
     */
    protected int numChildren = 0;

    /**
     * This ivar is the index of this child Tag in its parent's child
     * list.
     */
    protected int childIndex = 0; 

    /**
     * <p>Override this method to do three things.</p>
     * <ul>
     * <li>Call <code>super.setParent(t)</code>.</li>
     * <li>Find the closest enclosing <code>FacesTag</code> and increment its
     *     <code>numChildren</code> counter.</li>
     * <li>save our <code>childIndex</code> as
     *     <code>enclosingFacesTag.numChildren - 1</code>.</li>
     * </ul>
     */ 
    public void setParent(Tag t ) {
        
        super.setParent(t);
        // iterate until we get the parent of this tag which is a facestag and
        // increment its numchildren. Immediate parent need not be a facesTag.   
        FacesTag parentFacesTag = (FacesTag)this.getNearestEnclosingFacesTag();
        if (parentFacesTag != null ) {
            parentFacesTag.incrementNumChildren();
            this.childIndex = parentFacesTag.getNumChildren() - 1;
        }    
    } 
    
    /**
     * Returns the nearest enclosing <code>FacesTag</code>.
     */
    protected Tag getNearestEnclosingFacesTag() {
        Tag tagObj = this.getParent();
        while ( tagObj != null ) {
            if ( tagObj instanceof FacesTag) {
                tagObj = (FacesTag) tagObj;
                break;
            } 
            tagObj = tagObj.getParent(); 
        }
        if (tagObj == null ) {
            return null;
        }    
        return tagObj;
    }
    
    /** 
     * Increments current value of <code>numChildren</code>.
     */
    protected void incrementNumChildren() {
        numChildren++;
    }
    
    /** 
     * Returns current value of <code>numChildren</code>.
     */
    protected int getNumChildren() {
        return this.numChildren;
    }

    /** 
     * Returns current value of <code>childIndex</code>.
     */
    protected int getChildIndex() {
        return this.childIndex;
    }

    /**
     * <p>Return the flag value that should be returned from the
     * <code>doAfterBody()</code> method when it is called.  Subclasses
     * may override this method to return the appropriate value.</p>
     *
     * @exception JspException to cause <code>doAfterBody()</code>
     *  to throw an exception
     */
    protected int getDoAfterBodyValue() throws JspException {

        return (SKIP_BODY);

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
     * <p>Return <code>true</code> if rendering should be suppressed because
     * some parent component on the stack has been configured with
     * <code>getRendersChildreN()</code> as true.</p>
     */
    protected boolean isSuppressed() {

        int n = componentStack.size() - 1; // Skip ourself
        for (int i = (n - 1); i >= 0; i--) {
            UIComponent component = (UIComponent) componentStack.get(i);
            if (component.getRendersChildren()) {
                return (true);
            }
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
        if ((modelReference != null) &&
            (component.getModelReference() == null)) {
            component.setModelReference(modelReference);
        }

        if (component.getAttribute("rendered") == null) {
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
