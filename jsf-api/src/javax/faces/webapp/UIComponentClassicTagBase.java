/*
 * $Id: UIComponentClassicTagBase.java,v 1.14 2005/12/06 01:58:08 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.webapp;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.tagext.JspIdConsumer;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import javax.faces.component.NamingContainer;


/**
 * <p><strong><code>UIComponentTagBase</code></strong> is the base class
 * for all JSP tags that use the "classic" JSP tag interface that
 * correspond to a {@link javax.faces.component.UIComponent} instance in
 * the view.  In Faces 1.2, all component tags are <code>BodyTag</code>
 * instances to allow for the execution of the page to build the
 * component tree, but not render it.  Rendering happens only after the
 * component tree is completely built.</p>
 *
 * <p>{@link UIComponentTag} extends
 * <code>UIComponentClassicTagBase</code> to add support for properties
 * that conform to the Faces 1.1 EL.</p>
 *
 * <p>{@link UIComponentELTag} extends
 * <code>UIComponentClassicTagBase</code> class to add support for
 * properties that conform to the EL API.</p>
 *
 * <p>The default implementation allows the proper interweaving of
 * template text, non-Faces JSP tag output, and Faces component tag
 * output in the same page, as expected by the page author.</p>
 *
 * <p>The CASE markers in the following example will be cited in the
 * method descriptions of this class.</p>
 *
 * <ul>
 *
 * <li><p>CASE 1 describes template text and/or non-component custom tag
 * output occurring as the child of a component tag, but before the
 * first component tag child of that component tag.</p></li>
 *
 * <li><p>CASE 2 describes template text and/or non-component custom tag
 * output occurring between two sibling component tags.</p></li>
 *
 * <li><p>CASE 3 describes template text and/or non-component custom tag
 * output occurring as the child content of an &lt;f:verbatim&gt;
 * tag at any point in the page.</p></li>
 *
 * <li><p>CASE 4 describes template text and/or non-component custom tag
 * output occurring between the last child component tag and its
 * enclosing parent component tag's end tag.</p></li>
 *
 * </ul>
 *
 * <code><pre>

    &lt;h:panelGrid style="color:red" border="4" columns="2"&gt;
      CASE 1
      &lt;h:outputText value="component 1"/&gt;
      CASE 2
      &lt;h:outputText value="component 2"/&gt;
      &lt;f:verbatim&gt;CASE 3&lt;/f:verbatim&gt;
      &lt;c:out value="${pageScope.CASE4}" /&gt;
    &lt;/h:panelGrid&gt;

 * </pre></code>
 *
 * <p>The preceding arrangement of faces component tags, must yield
 * markup that will render identically to the following (assuming that
 * <code>${pageScope.CASE4}</code> evaluates to "<code>CASE 4</code>"
 * without the quotes).</p>
 *
 * <code><pre>

&lt;table border="4" style="color:red"&gt;

  &lt;tbody&gt;

    &lt;tr&gt;&lt;td&gt;CASE 1&lt;/td&gt;&lt;/tr&gt; &lt;tr&gt;&lt;td&gt;component 1&lt;/td&gt;&lt;/tr&gt; 

    &lt;tr&gt;&lt;td&gt;CASE 2&lt;/td&gt; &lt;tr&gt;&lt;td&gt;component 2&lt;/td&gt;&lt;/tr&gt; 

    &lt;tr&gt;&lt;td&gt;CASE 3&lt;/td&gt; &lt;td&gt;CASE 4&lt;/td&gt;&lt;/tr&gt;

  &lt;/tbody&gt;

&lt;/table&gt;

 * </pre></code>

 *
 */

public abstract class UIComponentClassicTagBase extends UIComponentTagBase implements JspIdConsumer, BodyTag {

    // ------------------------------------------------------ Manifest Constants
    /**
     * <p>The request scope attribute under which a component tag stack
     * for the current request will be maintained.</p>
     */
    private static final String COMPONENT_TAG_STACK_ATTR =
        "javax.faces.webapp.COMPONENT_TAG_STACK";

    /**
     * <p>The {@link UIComponent} attribute under which we will store a
     * <code>List</code> of the component identifiers of child components
     * created on the previous generation of this page (if any).</p>
     */
    private static final String JSP_CREATED_COMPONENT_IDS =
        "javax.faces.webapp.COMPONENT_IDS";


    /**
     * <p>The {@link UIComponent} attribute under which we will store a
     * <code>List</code> of the facet names of facets created on the previous
     * generation of this page (if any).
     */
    private static final String JSP_CREATED_FACET_NAMES =
        "javax.faces.webapp.FACET_NAMES";
    

    /**
     * <p>The attribute name under which we will store all {@link UIComponent}
     * IDs of the current translation unit.</p>
     */ 
    private static final String GLOBAL_ID_VIEW = 
        "javax.faces.webapp.GLOBAL_ID_VIEW";

    /**
     * <p>The attribute name under which we will store the {@link FacesContext}
     * for this request.</p>
     */ 
    private static final String CURRENT_FACES_CONTEXT =
        "javax.faces.webapp.CURRENT_FACES_CONTEXT";

    /**
     * <p>The attribute name under which we will store the {@link UIViewRoot}
     * for this request.</p>
     */ 
    private static final String CURRENT_VIEW_ROOT =
        "javax.faces.webapp.CURRENT_VIEW_ROOT";
    
    /**
     * Used as the prefix for ids.  This is necessary to avoid
     * uniqueness conflicts with the transient verbatim components.
     */
    protected static final String UNIQUE_ID_PREFIX = 
	UIViewRoot.UNIQUE_ID_PREFIX + "_";

    /**
     * Used to store the previousJspId Map in requestScope
     */
    private static final String PREVIOUS_JSP_ID_MAP = 
	"javax.faces.webapp.PREVIOUS_JSP_ID_MAP";

    // ------------------------------------------------------ Instance Variables
    /**
     * <p>The <code>bodyContent</code> for this tag handler.</p>
     */
    protected BodyContent bodyContent = null;

    /**
     * <p>The {@link UIComponent} that is being encoded by this tag,
     * if any.</p>
     */
    private UIComponent component = null;


    /**
     * <p>The {@link FacesContext} for the request being processed, if any.
     * </p>
     */
    private FacesContext context = null;


    /**
     * <p>Was a new component instance dynamically created when our
     * <code>findComponent()</code> method was called.</p>
     */
    private boolean created = false;


    /**
     * <p>The <code>Lst</code> of {@link UIComponent} ids created or located
     * by nested {@link UIComponentTag}s while processing the current
     * request.</p>
     */
    private List<String> createdComponents = null;


    /**
     * <p>The <code>List</code> of facet names created or located by nested
     * {@link UIComponentTag}s while processing the current request.</p>
     */
    private List createdFacets = null;


    /**
     * <p>The JSP <code>PageContext</code> for the page we are embedded in.</p>
     */
    protected PageContext pageContext = null;


    /**
     * <p>The JSP <code>Tag</code> that is the parent of this tag.</p>
     */
    private Tag parent = null;

    /**
     * {@link #setJspId}
     */ 

    private String jspId = null;
    
    /**
     * Only consulted in setJspId to detect the iterator case.  
     * Set in {@link #release}.  Never cleared.
     */
    
    private String oldJspId = null;

    /**
     * This is simply the jspId prefixed by {@link #UNIQUE_ID_PREFIX}.
     */

    private String facesJspId = null;

    /**
     * <p>The component identifier for the associated component.</p>
     */
    private String id = null;

    /**
     * Caches the nearest enclosing {@link UIComponentClassicTagBase} of this
     * tag. This is used for duplicate id detection.
     */
    private UIComponentClassicTagBase parentTag = null;
    
    /**
     * Set to true if this component is nested inside of an iterating
     * tag
     */
    private boolean isNestedInIterator = false;

    // --------------------------------------------- Support Methods for Tag

    //
    // Simple methods to be overridden by subclasses if necessary
    // 

    /**
     * <p>Return the flag value that should be returned from the
     * <code>doStart()</code> method when it is called.  Subclasses
     * may override this method to return the appropriate value.</p>
     *
     * @throws JspException to cause <code>doStart()</code> to
     *  throw an exception
     */
    protected int getDoStartValue() throws JspException {

	int result = EVAL_BODY_BUFFERED;

	return result;
    }

    /**
     * <p>Return the flag value that should be returned from the
     * <code>doEnd()</code> method when it is called.  Subclasses
     * may override this method to return the appropriate value.</p>
     *
     * @throws JspException to cause <code>doEnd()</code> to
     *  throw an exception
     */
    protected int getDoEndValue() throws JspException {

        return (EVAL_PAGE);

    }

    /**
     * <p>Delegate to the <code>encodeBegin()</code> method of our
     * corresponding {@link UIComponent}.  This method is called from
     * <code>doStartTag()</code>.  Normally, delegation occurs unconditionally;
     * however, this method is abstracted out so that advanced tags can
     * conditionally perform this call.
     *
     * @throws IOException if an input/output error occurs
     *
     * @deprecated No encoding is done during JSP page execution.
     * Encoding is deferred until the page has completed executing to
     * allow the entire tree to be built before any encoding occurs.
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
     * @throws IOException if an input/output error occurs
     *
     * @deprecated No encoding is done during JSP page execution.
     * Encoding is deferred until the page has completed executing to
     * allow the entire tree to be built before any encoding occurs.
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
     * @throws IOException if an input/output error occurs
     *
     * @deprecated No encoding is done during JSP page execution.
     * Encoding is deferred until the page has completed executing to
     * allow the entire tree to be built before any encoding occurs.
     */
    protected void encodeEnd() throws IOException {

        component.encodeEnd(context);

    }

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




    //
    // Complex methods to support Tag
    // 

    /**
     * <p>Set up the {@link javax.faces.context.ResponseWriter} for the
     * current response, if this has not been done already.</p>
     *
     * <p>@deprecated.  {@link
     * javax.faces.application.ViewHandler#renderView} is now
     * responsible for setting up the response writer.  This method is
     * now a no-op.</p>

     */
    protected void setupResponseWriter() {
    }


    /**
     * <p>Create a new child component using <code>createComponent</code>, 
     * initialize its properties, and add it to its parent as a child.
     * </p>
     * @param context {@link FacesContext} for the current request
     * @param parent Parent {@link UIComponent} for the new child
     * @param componentId Component identifier for the new child,
     *  or <code>null</code> for no explicit identifier
     */
    private UIComponent createChild(FacesContext context, UIComponent parent,
                                    String componentId) throws JspException {

        UIComponent component = createComponent(context, componentId);
        UIComponentTagBase parentTag = getParentUIComponentClassicTagBase(pageContext);
        parent.getChildren().add(parentTag.getIndexOfNextChildTag(), component);
        created = true;
        return (component);

    }

    /**
     * <p>Create a new child component using <code>createComponent</code>, 
     * initialize its properties, and add it to its parent as a facet.
     * </p>
     * @param context {@link FacesContext} for the current request
     * @param parent Parent {@link UIComponent} of the new facet
     * @param name Name of the new facet
     * @param newId id of the new facet
     */
    private UIComponent createFacet(FacesContext context, UIComponent parent,
                                    String name, String newId) throws JspException {

        UIComponent component = createComponent(context, newId);
        parent.getFacets().put(name, component);
        created = true;
        return (component);

    }

    /**
     * <p>Return a child with the specified component id from the specified
     * component, if any; otherwise, return <code>null</code>.</p>
     *
     * @param component {@link UIComponent} to be searched
     * @param componentId Component id to search for
     */
    private UIComponent getChild(UIComponent component, String componentId) {

        Iterator<UIComponent> kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            if (componentId.equals(kid.getId())) {
                return (kid);
            }
        }
        return (null);

    }

    /**
     * <p>Find and return the {@link UIComponent}, from the component
     * tree, that corresponds to this tag handler instance.  If there
     * is no such {@link UIComponent}, create one 
     * and add it as a child or facet of the {@link UIComponent} associated
     * with our nearest enclosing {@link UIComponentTag}.  The process for
     * locating or creating the component is:</p>
     * <ol>
     * <li>If we have previously located this component, return it.</li>
     * <li>Locate the parent component by looking for a parent
     *     {@link UIComponentTag} instance, and ask it for its component.
     *     If there is no parent {@link UIComponentTag} instance, this tag
     *     represents the root component, so get it from the current
     *     <code>Tree</code> and return it.</li>
     * <li>If this {@link UIComponentTag} instance has the
     *     <code>facetName</code> attribute set, ask the parent
     *     {@link UIComponent} for a facet with this name.  If not found,
     *     create one, call <code>setProperties()</code> with the new
     *     component as a parameter, and register it under this name.
     *     Return the found or created facet {@link UIComponent}.</li>
     * <li>Determine the component id to be assigned to the new
     *     component, as follows:  if this {@link UIComponentTag} has
     *     an <code>id</code> attribute set, use that value; otherwise,
     *     generate an identifier that is guaranteed to be the same for
     *     this {@link UIComponent} every time this page is processed
     *     (i.e. one based on the location of all {@link UIComponentTag}
     *     instances without an <code>id</code> attribute set).</li>
     * <li>Ask the parent {@link UIComponent} for a child with this identifier.
     *     If not found, create one, call <code>setProperties()</code>
     *     with the new component as a parameter, and register it as a child
     *     with this identifier.  Return the found or created
     *     child {@link UIComponent}.</li>
     * </ol>
     * <p>When creating a component, the process is:</p>
     * <ol>
     * <li>Retrieve the component type by calling
     * {@link UIComponentTag#getComponentType}</li>
     * <li>If the component has a <code>binding</code> attribute,
     * create an expression from it, and call
     * {@link Application#createComponent} with that expression,
     * the {@link FacesContext}, and the component type.  Store the
     * expression using the key <code>"binding"</code>.</li>
     * <li>Otherwise, call {@link Application#createComponent} with
     * only the component type.
     * <li>Call <code>setProperties()</code>.
     * <li>Add the new component as a child or facet of its parent</li>
     * </ol>
     */
    protected UIComponent findComponent(FacesContext context)
	throws JspException {

        // Step 1 -- Have we already found the relevant component?
        if (component != null) {
            return (component);
        }

        // Step 2 -- Identify the component that is, or will be, our parent
        UIComponentClassicTagBase parentTag = 
	    getParentUIComponentClassicTagBase(pageContext);
        UIComponent parentComponent = null;
        if (parentTag != null) {
            parentComponent = parentTag.getComponentInstance();
        } else {
	    // Special case.  The component to be found is the
	    // UIViewRoot.
	    // see if this is the first time this tag instance is trying
	    // to be bound to the UIViewRoot
	    parentComponent = context.getViewRoot();
            // Has this UIViewRoot instance had a tag bound to it
            // before?
            if (null == 
                parentComponent.getAttributes().get(CURRENT_VIEW_ROOT)) {
                // No it hasn't.

                // make sure setProperties() and setId() are called
                // once per UIViewRoot instance.
		try {
		    setProperties(parentComponent);
		}
		catch (FacesException e) {
		    if (e.getCause() instanceof JspException) {
			throw ((JspException)e.getCause());
		    }
		    throw e;
		}

                if (null != this.id) {
                    parentComponent.setId(this.id);
                }
		else {
		    assert(null != getFacesJspId());
		    parentComponent.setId(getFacesJspId());
		}
                parentComponent.getAttributes().put(CURRENT_VIEW_ROOT, 
                                                    CURRENT_VIEW_ROOT);
                created = true;
            }
            else if (hasBinding()) {
		try {
		    setProperties(parentComponent);
		}
		catch (FacesException e) {
		    if (e.getCause() instanceof JspException) {
			throw ((JspException)e.getCause());
		    }
		    throw e;
		}
            }

	    // this is not the first time this tag instance is trying to
	    // be bound to this UIViewRoot, take no extra action.
		
            component = parentComponent;
            return (component);
        }

        // Step 3 -- Calculate the component identifier for this component
        String newId = createId();
        
        // Step 4 -- Create or return a facet with the specified name (if any)
        String facetName = getFacetName();
        if (facetName != null) {
            component = (UIComponent)
                parentComponent.getFacets().get(facetName);
            if (component == null) {
                component = createFacet(context, parentComponent, facetName,
                        newId);
            }
            return (component);
        }

        // Step 5 -- Create or return a child with the specified id
        component = getChild(parentComponent, newId);
        if (component == null) {
            component = createChild(context, parentComponent, newId);
        }
        return (component);

    }

    //
    // Tag tree navigation
    // 

    /**
     * <p>Locate and return the nearest enclosing {@link UIComponentClassicTagBase}
     * if any; otherwise, return <code>null</code>.</p>
     *
     * @param context <code>PageContext</code> for the current page
     */
    public static UIComponentClassicTagBase getParentUIComponentClassicTagBase(PageContext context) {

        FacesContext facesContext = (FacesContext)
              context.getAttribute(CURRENT_FACES_CONTEXT);
        List list = (List) facesContext.getExternalContext().getRequestMap()
              .get(COMPONENT_TAG_STACK_ATTR);
       
        if (list != null) {
            return ((UIComponentClassicTagBase) list.get(list.size() - 1));
        } else {
            return (null);
        }

    }

    //
    // Methods related to the createdComponents and createdFacets lists.
    // 

    protected int getIndexOfNextChildTag() {

        if (createdComponents != null) {
            return (createdComponents.size());
        } else {
            return (0);
        }

    }

    protected void addChild(UIComponent child) {

        if (createdComponents == null) {
            createdComponents = new ArrayList<String>();
        }
        createdComponents.add(child.getId());
    }

    protected void addFacet(String name) {

        if (createdFacets == null) {
            createdFacets = new ArrayList();
        }
        createdFacets.add(name);

    }

    /**
     * <p>Pop the top {@link UIComponentTag} instance off of our component tag
     * stack, deleting the stack if this was the last entry.</p>
     */
    private void popUIComponentClassicTagBase() {
        Map<String,Object> requestMap = 
              context.getExternalContext().getRequestMap();
        List list = (List) requestMap.get(COMPONENT_TAG_STACK_ATTR);
        if (list != null) {
            list.remove(list.size() - 1);
            if (list.size() < 1) {
                requestMap.remove(COMPONENT_TAG_STACK_ATTR);
            }
        }

    }


    /**
     * <p>Push the specified {@link UIComponentTag} instance onto our component
     * tag stack, creating a stack if necessary.</p>
     */
    private void pushUIComponentClassicTagBase() {

        Map<String,Object> requestMap = 
              context.getExternalContext().getRequestMap();
        List<UIComponentClassicTagBase> list = (List<UIComponentClassicTagBase>) 
              requestMap.get(COMPONENT_TAG_STACK_ATTR);
        if (list == null) {
            list = new ArrayList<UIComponentClassicTagBase>();
            requestMap.put(COMPONENT_TAG_STACK_ATTR, list);
        }
        list.add(this);

    }


    /**
     * <p>Retrieve from the {@link UIComponent} corresponding to this tag
     * handler the list of all child component ids created by
     * {@link UIComponentTag} instances the previous time this tree was
     * rendered.  Compare it to the list of children created during this
     * page processing pass, and remove all children present on the old list
     * but not in the new list.  Save the list as a {@link UIComponent}
     * attribute so that it gets saved as part of the component's state.</p>
     */
    private void removeOldChildren() {

        // Remove old children that are no longer present
        List oldList =
            (List) component.getAttributes().get(JSP_CREATED_COMPONENT_IDS);
        if (oldList != null) {

            if (createdComponents != null) {

                // Components not in the new list need to be removed
                Iterator<String> olds = oldList.iterator();
                while (olds.hasNext()) {
                    String old = olds.next();
                    if (!createdComponents.contains(old)) {
                        UIComponent child = component.findComponent(old);
                        // if a component is marked transient, it would have 
                        // been already removed from the child list, but the
                        // oldList would still have it. So,unless findComponent
                        // is successful, we don't have to call remove child.
                        if ( child != null) {
                            component.getChildren().remove(child);
                        }
                    }
                }

            } else {

                // All old components need to be removed
                Iterator<String> olds = oldList.iterator();
                while (olds.hasNext()) {
                    String old = olds.next();
                    UIComponent child = component.findComponent(old);
                    component.getChildren().remove(child);
                }

            }

        }

        // Save the current list as a component attribute
        if (createdComponents != null) {
            component.getAttributes().put(JSP_CREATED_COMPONENT_IDS,
                                          createdComponents);
        } else {
            component.getAttributes().remove(JSP_CREATED_COMPONENT_IDS);
        }
        createdComponents = null;

    }


    /**
     * <p>Retrieve from the {@link UIComponent} corresponding to this tag
     * handler the list of all facet names created by {@link UIComponentTag}
     * instances the previous time this tree was rendered.  Compare it to the
     * list of facets created during this page processing pass, and remove
     * all facets present on the old list but not in the new list.  Save the
     * list as a {@link UIComponent} attribute so that it gets saved as part
     * of the component's state.</p>
     */
    private void removeOldFacets() {

        // Remove old facets that are no longer present
        List oldList =
            (List) component.getAttributes().get(JSP_CREATED_FACET_NAMES);
        if (oldList != null) {

            if (createdFacets != null) {

                // Facets not in the new list need to be removed
                Iterator<String> olds = oldList.iterator();
                while (olds.hasNext()) {
                    String old = olds.next();
                    if (!createdFacets.contains(old)) {
                        component.getFacets().remove(old);
                    }
                }

            } else {

                // All old facets need to be removed
                Iterator<String> olds = oldList.iterator();
                while (olds.hasNext()) {
                    String old = olds.next();
                    component.getFacets().remove(old);
                }

            }

        }

        // Save the current list as a component attribute
        if (createdFacets != null) {
            component.getAttributes().put(JSP_CREATED_FACET_NAMES,
                                          createdFacets);
        } else {
            component.getAttributes().remove(JSP_CREATED_FACET_NAMES);
        }
        createdFacets = null;

    }

    //
    // Methods to support content interweaving
    //

    /**
     *
     * <p>Create a transient UIOutput component from the body content,
     * of this tag instance or return null if there is no body content,
     * the body content is whitespace, or the body content is a
     * comment.</p>
     */

    protected UIComponent createVerbatimComponentFromBodyContent() {
	UIOutput verbatim = null;
	String bodyContentString = null;
	String trimString = null;
	if (null != bodyContent && 
	    null != (bodyContentString = bodyContent.getString()) &&
	    0 < (trimString = bodyContent.getString().trim()).length()) {
	    if (!(trimString.startsWith("<!--") && 
		  trimString.endsWith("-->"))) {
		verbatim = createVerbatimComponent();
		verbatim.setValue(bodyContentString);
		bodyContent.clearBody();
	    }  else {
                // clear body if bodyContent is just comments.
                bodyContent.clearBody();    
            }
	}
	return verbatim;
    }

    /**
     * <p>Use the {@link Application} instance to create a new component
     * with the following characteristics.</p>
     *
     * <p><code>componentType</code> is
     * <code>javax.faces.HtmlOutputText</code>.</p>
     *
     * <p><code>transient</code> is <code>true</code>.</p>
     *
     * <p><code>escape</code> is <code>false</code>.</p>
     *
     * <p><code>id</code> is
     * <code>FacesContext.getViewRoot().createUniqueId()</code></p>
     *
     */

    protected UIOutput createVerbatimComponent() {
	assert(null != getFacesContext());
	UIOutput verbatim = null;
	Application application = getFacesContext().getApplication();
	verbatim = (UIOutput)
	    application.createComponent("javax.faces.HtmlOutputText");
	verbatim.setTransient(true);
	verbatim.getAttributes().put("escape", Boolean.FALSE);
	verbatim.setId(getFacesContext().getViewRoot().createUniqueId());
	return verbatim;
    }

    /**
     * <p>Add <i>verbatim</i> as a sibling of <i>component</i> in
     * <i>component</i> in the parent's child list.  <i>verbatim</i> is
     * added to the list at the position immediatly preceding
     * <i>component</i>.</p>
     */

    protected void addVerbatimBeforeComponent(UIComponentClassicTagBase parentTag,
					    UIComponent verbatim,
					   UIComponent component) {
	int indexOfComponentInParent = 0;
	UIComponent parent = component.getParent();
	List children = parent.getChildren();
	if (null == parent) {
	    return;
	}
	indexOfComponentInParent = children.indexOf(component);
	children.add(indexOfComponentInParent, verbatim);
	parentTag.addChild(verbatim);
    }

    /**
     * <p>Add <i>verbatim</i> as a sibling of <i>component</i> in
     * <i>component</i> in the parent's child list.  <i>verbatim</i> is
     * added to the list at the position immediatly following
     * <i>component</i>.</p>
     */
					   
    protected void addVerbatimAfterComponent(UIComponentClassicTagBase parentTag,
					   UIComponent verbatim,
					   UIComponent component) {
	int indexOfComponentInParent = 0;
	UIComponent parent = component.getParent();
	List children = parent.getChildren();
	if (null == parent) {
	    return;
	}
	indexOfComponentInParent = children.indexOf(component);
	if (children.size() - 1 == indexOfComponentInParent) {
	    children.add(verbatim);
	}
	else {
	    children.add(indexOfComponentInParent + 1, verbatim);
	}
	parentTag.addChild(verbatim);
    }

    // ------------------------------------------------------------ Tag Methods

    /**
     *
     * <p>Perform any processing necessary to find (or create) the
     * {@link UIComponent} instance in the view corresponding to this
     * tag instance in the page and, if and only if a component was
     * created, insert it into the tree at the proper location as
     * expected by the page author.  Secondarily, cause a transient
     * {@link UIOutput} component to be created and placed in the tree
     * <b>before</b> the <code>UIComponent</code> instance for
     * <b>this</b> tag.  The value of this <code>UIOutput</code>
     * component must include anything covered by <code>CASE 1</code> or
     * <code>CASE 2</code> in the class description.</p>
     *
     * <p>The default implementation, which is intended to be sufficient
     * for most components, implements this secondary requirement by
     * calling {@link #getParentUIComponentClassicTagBase}, and calling
     * {@link #createVerbatimComponentFromBodyContent} on the result.
     * It then adds the returned component to the tree <b>before</b> the
     * actual component for <b>this</b> tag instance instance by calling
     * {@link #addVerbatimBeforeComponent}.</p>
     *
     * <p>Before returning, the component is pushed onto the component
     * stack for this response to enable the {@link
     * #getParentUIComponentClassicTagBase} method to work properly.</p>
     *
     * <p>The flag value to be returned is acquired by calling the
     * <code>getDoStartValue()</code> method, which tag subclasses may
     * override if they do not want the default value.</p>
     *
     * @throws JspException if an error occurs
     */
    public int doStartTag() throws JspException {
	// make sure that these ivars are reset at the beginning of the
	// lifecycle for this tag.
	createdComponents = null;
	createdFacets = null;
	UIComponent verbatim = null;
        
        context = getFacesContext();
	if (null == context) {
	    // PENDING(edburns): I18N
	    throw new JspException("Can't find FacesContext");
	}

        parentTag = getParentUIComponentClassicTagBase(pageContext);
        Map requestMap = context.getExternalContext().getRequestMap();
        Map componentIds = null;
        if (parentTag == null) {
            // create the map if we're the top level UIComponentTag
            componentIds = new HashMap();
            requestMap.put(GLOBAL_ID_VIEW, componentIds); 
        } else {
            componentIds = (Map) requestMap.get(GLOBAL_ID_VIEW);
        }

	// If we're not inside of a facet, and if we are inside of a
	// rendersChildren==true component, stuff any template text or
	// custom tag output into a transient component.
        if (null == getFacetName() &&
	    null != parentTag) {
            // If we're not inside a JSP tag, flush the buffer 
            // to our wrapped response
            if (null == this.getParent()) {
        	JspWriter out = pageContext.getOut();
		try {
		    out.flush();
		}
		catch (IOException ioe) {
		    throw new JspException(ioe);
		}
	    }
	    verbatim = parentTag.createVerbatimComponentFromBodyContent();
	}

        // Locate the UIComponent associated with this UIComponentTag,
        // creating one if necessary
        component = findComponent(context);
        
	// if we have a verbatim component, add it after this component.
	if (null != verbatim) {
	    addVerbatimBeforeComponent(parentTag,
				       verbatim, component);
	}

        Object tagInstance = null;
        String clientId = null;
        if (this.id != null) {
            clientId = component.getClientId(context);
            tagInstance = (componentIds.get(clientId) == this ? this : null);
        }

        // If we have a tag instance, then, most likely, a tag handler
        // returned EVAL_BODY_AGAIN somewhere.  Make sure the instance
        // returned is the same as the current instance and if this is the case,
        // do not perform ID validation as it has already been done.
        if (tagInstance == null) {

            // only check for id uniqueness if the author has manually given
            // us an id.
            if (null != this.id) {

                // assert component ID uniqueness
                if (clientId != null) {
                    if (componentIds.containsKey(clientId)) {
                        // PENDING i18n
                        throw new JspException(new IllegalStateException("Duplicate component id: '" +
                            clientId +
                            "', first used in tag: '" +
                            componentIds.get(clientId).getClass().getName() +
                            "'"));
                    } else {                        
                        componentIds.put(clientId, this);
                    }
                }
            }

            // Add to parent's list of created components or facets if needed

            if (parentTag != null) {
                if (getFacetName() == null) {
                    parentTag.addChild(component);
                } else {
                    parentTag.addFacet(getFacetName());
                }
            }
        }

	// Rendering is deferred until after the tree is completely
	// created

        // Return the appropriate control value
        pushUIComponentClassicTagBase();
        return (getDoStartValue());

    }


    /**
     *
     * <p>Perform any processing necessary to handle the content
     * implications of CASE 3 in the class description.</p>
     *
     * <p>The default implementation, which is intended to be sufficient
     * for most components, calls {@link
     * #createVerbatimComponentFromBodyContent} on <b>this</b> instance
     * and adds it as a child of the component for this tag's component
     * at the <b>end</b> of the child list.  In addition, the following
     * housekeeping steps are taken.</p>
     * 
     * <ul>
     *
     * <li>Retrieve from the {@link UIComponent} the set of component
     * ids of child components created by {@link UIComponentTag}
     * instances the last time this page was processed (if any).
     * Compare it to the list of children created during this page
     * processing pass, and remove all children present in the old list
     * but not the new.  Save the new list as a component attribute so
     * that it gets saved as part of the component's state.</li>
     *
     * <li>Retrieve from the {@link UIComponent} the set of facet names
     * of facets created by {@link UIComponentTag} instances the last
     * time this page was processed (if any).  Compare it to the list of
     * facets created during this page processing pass, and remove all
     * facets present in the old list but not the new.  Save the new
     * list as a component attribute so that it gets saved as part of
     * the component's state.</li>
     *
     * <li>Release all references to the component, and pop it from the
     * component stack for this response, removing the stack if this was
     * the outermost component.</li> </ul>
     *
     * <p>The flag value to be returned is acquired by calling the
     * <code>getDoEndValue()</code> method, which tag subclasses may
     * override if they do not want the default value.</p>
     *
     * @throws JspException if an error occurs
     */
    public int doEndTag() throws JspException {

        // Remove old children and facets as needed
        popUIComponentClassicTagBase();
        removeOldChildren();
        removeOldFacets();

        // Render the children (if needed) and  end of the component
        // associated with this tag
        try {
	    UIComponent verbatim = null;
	    UIComponentClassicTagBase parentTag = 
		getParentUIComponentClassicTagBase(pageContext);

	    if (null != (verbatim = this.createVerbatimComponentFromBodyContent())) {
                component.getChildren().add(verbatim);
		if (null != parentTag) {
		    parentTag.addChild(verbatim);
		}
	    }

	    // else, we don't render rendersChildren==true
	    // components here

        } catch (Throwable e) {
            throw new JspException(e);
        } finally {
            component = null;
            context = null;
        }

        // Return the appropriate control value
        created = false;

	this.release();
        return (getDoEndValue());

    }

    /**
     * <p>Release any resources allocated during the execution of this
     * tag handler.</p>
     */
    public void release() {
        
        this.parent = null;

        this.id = null;
        this.jspId = null;
        this.facesJspId = null;
        this.created = false;
	this.bodyContent = null;
        this.isNestedInIterator = false;
    }

    // -------------------------------------------- Support methods for BodyTag

    /**
     * <p>Return the flag value that should be returned from the
     * <code>doAfterBody()</code> method when it is called.  Subclasses
     * may override this method to return the appropriate value.</p>
     */
    protected int getDoAfterBodyValue() throws JspException {

        return (SKIP_BODY);

    }

    // -------------------------------------------------------- BodyTag Methods

    /**
     * <p>Set the <code>bodyContent</code> for this tag handler.  This method
     * is invoked by the JSP page implementation object at most once per
     * action invocation, before <code>doInitiBody()</code>.  This method
     * will not be invoked for empty tags or for non-empty tags whose
     * <code>doStartTag()</code> method returns <code>SKIP_BODY</code> or
     * <code>EVAL_BODY_INCLUDE</code>.</p>
     *
     * @param bodyContent The new <code>BodyContent</code> for this tag
     */
    public void setBodyContent(BodyContent bodyContent) {

        this.bodyContent = bodyContent;

    }

    /**
     * <p>Get the <code>JspWriter</code> from our <code>BodyContent</code>.
     * </p>
     */
    public JspWriter getPreviousOut() {

        return (this.bodyContent.getEnclosingWriter());

    }

    public BodyContent getBodyContent() {
	
        return (this.bodyContent);
	
    }



    /**
     * <p>Prepare for evaluation of the body.  This method is invoked by the
     * JSP page implementation object after <code>setBodyContent()</code>
     * and before the first time the body is to be evaluated.  This method
     * will not be invoked for empty tags or for non-empty tags whose
     * <code>doStartTag()</code> method returns <code>SKIP_BODY</code>
     * or <code>EVAL_BODY_INCLUDE</code>.</p>
     *
     * @throws JspException if an error is encountered
     */
    public void doInitBody() throws JspException {

        ; // Default implementation does nothing

    }

    /**
     *
     * <p>Perform any processing necessary to handle the content
     * implications of CASE 4 in the class description.</p>
     *
     * <p>Return result from {@link #getDoAfterBodyValue}</p>
     * @throws JspException if an error is encountered
     */
    public int doAfterBody() throws JspException {
	
	UIComponent verbatim = null;
	UIComponentClassicTagBase parentTag = 
	    getParentUIComponentClassicTagBase(pageContext);
	
	// if we are the root tag, or if we are inside of a
	// rendersChildren==true component
	if (this == parentTag || 
	    (null != parentTag && 
	     parentTag.getComponentInstance().getRendersChildren())) {
	    // stuff the template text or custom tag output into a
	    // transient component
	    if (null != (verbatim = this.createVerbatimComponentFromBodyContent())) {
		component.getChildren().add(verbatim);
		parentTag.addChild(verbatim);
	    }
	}
	
        return (getDoAfterBodyValue());

    }

    // ----------------------------------------------- Methods relating to Id

    /**
     * <p>Set the component identifier for our component.  If the
     * argument begins with {@link
     * UIViewRoot#UNIQUE_ID_PREFIX} throw an
     * <code>IllegalArgumentException</code></p>
     *
     * @param id The new component identifier.  This may not start with
     * {@link UIViewRoot#UNIQUE_ID_PREFIX}.
     *
     * @throws IllegalArgumentException if the argument is
     * non-<code>null</code> and starts with {@link
     * UIViewRoot#UNIQUE_ID_PREFIX}.
     */
    public void setId(String id) {
	if (null != id && id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX)) {
	    throw new IllegalArgumentException();
	}

        this.id = id;

    }

    /**
     * <p>Return the <code>id</code> value assigned by the page author.</p>
     */
    protected String getId() {

	return (id);

    }

    /**
     * <p>If this method has been called before on this tag's useful
     * lifetime (before {@link #release} was called), return the
     * previously returned value.  Otherwise, if {@link #getJspId}
     * returns non-<code>null</code>, prepend {@link #UNIQUE_ID_PREFIX}
     * to the <code>jspId</code> and return the result.</p>
     */

    protected String getFacesJspId() {
	if (null == facesJspId) {
	    if (null != jspId) {
		facesJspId = UNIQUE_ID_PREFIX + jspId;
                // if this tag happens to be nested within <c:forEach>, jspId
                // will be the same for each iteration. So it is
                // transformed into a unique "id" by appending a counter which 
                // gets stored in request scope with jspId as the key for use
                // during the next iteration.
                if (isDuplicateId(facesJspId)) {
                    facesJspId = generateIdForIteratorChild(facesJspId);
                } 
            }
            else {
                // jspId will be null if we're running in a container
                // that doesn't support JspIdConsumer
                facesJspId = context.getViewRoot().createUniqueId();
            }
	}
	return facesJspId;
    }
    
    /**
     * Returns true if a component already exists with the same 
     * <code>id</code>. This will be the case if this tag is 
     * nested within <code><c:forEach></code> tag or any other JSTL loop tag 
     * or if the page has components with the same <code>Id</code>.
     *
     * @param componentId <code>id</code> to be looked up for possible 
     * duplication.
     * @return true if this nested with <code>facesJspId</code> is duplicate.
     */
    private boolean isDuplicateId(String componentId) {
        boolean result = false;
        if (parentTag != null) {
            List childComponents = parentTag.getCreatedComponents();
            // PENDING: Need to analyze the impact of this look up on pages
            // with several levels of nesting.
            if (childComponents != null) {
                result = childComponents.contains(componentId);
            }
            else {
                result = parentTag.isNestedInIterator;
            }
        }
       
        return result;
    }
    
    /*
     * Appends a counter to the passed in <code>id</code> and stores the 
     * <code>id</code> and counter information in request scope.
     *
     * @return String <code>id</code> with a counter appended to it.
     */
    private String generateIdForIteratorChild (String componentId) {
        Map requestMap = getFacesContext().getExternalContext().getRequestMap();
        Integer serialNum = (Integer) requestMap.get(componentId);
        if (null == serialNum) {
            serialNum = new Integer(1);
        } else {
            serialNum = new Integer(serialNum.intValue() + 1);            
        }   
        requestMap.put(componentId, serialNum);
        componentId = componentId + UNIQUE_ID_PREFIX + serialNum.intValue();
        return componentId;
    }
    
    /**
     * Returns the <code>List</code> of {@link UIComponent} ids created or 
     * located by nested {@link UIComponentTag}s while processing the current
     * request.</p>
     */
    protected List<String> getCreatedComponents() {
        return createdComponents;
    }
    
    /**
     * <p>Create the component identifier to be used for this component.</p>
     */
    private String createId() {

	if (this.id == null) {
	    return getFacesJspId();
	} else {
            // if this tag happens to be nested within <c:forEach>, jspId
            // will be the same for each iteration. So it is
            // transformed into a unique "id" by appending a counter which gets
            // stored in request scope with jspId as the key for use during next
            // iteration.
            if (isDuplicateId(this.id)) {
                this.id = generateIdForIteratorChild(this.id);
            }
	    return (this.id);
        }

    }

    // ------------------------------------------------   JspIdConsumer Methods


    /**
     * <p>Defined on {@link JspIdConsumer}.  This method is called by
     * the container before {@link #doStartTag}.  The argument is
     * guaranteed to be unique within the page.</p>
     *
     * @param id the container generated id for this tag, guaranteed to
     * be unique within the page.
     */

    public void setJspId(String id) {
	facesJspId = null;
        updatePreviousJspIdAndIteratorStatus(id);
	jspId = id;
    }

    /**
     * <p>Called from {@link #setJspId} to update the value saved for
     * the previous call to {@link #setJspId} for this component <b>on
     * this request</b>.  If this method is presented with the same
     * argument <code>id</code> for the same tag instance more than once
     * on the same request, then we know that the tag instance lies
     * inside an iterator tag, such as <code>c:forEach</code>.  If so,
     * we set the <code>isNestedInIterator</code> ivar to
     * <code>true</code> otherwise, we set it to <code>false</code>.</p>
     *
     * <p>The implementation for this method stores a Map from tag
     * instance to id String as a request scoped attribute.  This map
     * contains the value used as the previousJspId and compared with
     * the argument <code>id</code>.
     *
     * @param id the id to be compared with the previous id, if any, for
     * this tag instance on this request.
     */
    
    private void updatePreviousJspIdAndIteratorStatus(String id) {
        Map<Object,String> previousJspIdMap = null;
        String oldJspId = null;
       
        if (null == (previousJspIdMap = (Map<Object, String>)
            pageContext.getRequest().getAttribute(PREVIOUS_JSP_ID_MAP))) {
            pageContext.getRequest().setAttribute(PREVIOUS_JSP_ID_MAP, 
                    previousJspIdMap = new HashMap<Object,String>());
        }
        assert(null != previousJspIdMap);
        oldJspId = previousJspIdMap.get(this);
        // detect the iterator case
        if (null != oldJspId && oldJspId.equals(id)) {
            isNestedInIterator = true;
        }
        else {
            isNestedInIterator = false;
            previousJspIdMap.put(this, id);
        }
    }

    public String getJspId() {
	return jspId;
    }

    // ------------------------------------------------------- Abstract methods

    /**
     * <p>Override properties and attributes of the specified component,
     * if the corresponding properties of this tag handler instance were
     * explicitly set.  This method must be called <strong>ONLY</strong>
     * if the specified {@link UIComponent} was in fact created during
     * the execution of this tag handler instance, and this call will occur
     * <strong>BEFORE</strong> the {@link UIComponent} is added to
     * the view.</p>
     *
     * <p>Tag subclasses that want to support additional set properties
     * must ensure that the base class <code>setProperties()</code>
     * method is still called.  A typical implementation that supports
     * extra properties <code>foo</code> and <code>bar</code> would look
     * something like this:</p>
     * <pre>
     * protected void setProperties(UIComponent component) {
     *   super.setProperties(component);
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
     * <li><code>rendered</code> - Set if a value for the
     *     <code>rendered</code> property is specified for
     *     this tag handler instance.</li>
     * <li><code>rendererType</code> - Set if the <code>getRendererType()</code>
     *     method returns a non-null value.</li>
     * </ul>
     *
     * @param component {@link UIComponent} whose properties are to be
     *  overridden
     */
    protected abstract void setProperties(UIComponent component);


    /**
     * <p>Create and return a new child component of the type returned
     * by calling <code>getComponentType()</code>.  If this {@link
     * UIComponentTag} has a non-null <code>binding</code> attribute,
     * this is done by call {@link Application#createComponent} with the
     * expression created for the <code>binding</code> attribute, and
     * the expression will be stored on the component.  Otherwise,
     * {@link Application#createComponent} is called with only the
     * component type.  Finally, initialize the components id and other
     * properties.  </p>
     * @param context {@link FacesContext} for the current request
     * @param newId id of the component
     */

    protected abstract UIComponent createComponent(FacesContext context, 
						   String newId) throws JspException;

    /**
     * <p>Return <code>true</code> if this component has a
     * non-<code>null</code> binding attribute.  This method is
     * necessary to allow subclasses that expose the
     * <code>binding</code> property as an Faces 1.1 style EL property
     * as well as subclasses that expose it as an EL API property.</p>
     */

    protected abstract boolean hasBinding();

    // --------------------------------------------------------- Properties

    /**
     * <p>Return the {@link UIComponent} instance that is associated with
     * this tag instance.  This method is designed to be used by tags nested
     * within this tag, and only returns useful results between the
     * execution of <code>doStartTag()</code> and <code>doEndTag()</code>
     * on this tag instance.</p>
     */
    public UIComponent getComponentInstance() {

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


    protected FacesContext getFacesContext() {
	
	if (context == null) {
            if (null == (context = (FacesContext)
                    pageContext.getAttribute(CURRENT_FACES_CONTEXT))) {
                context = FacesContext.getCurrentInstance();
	    
                if (context == null) { // PENDING - i18n
                    throw new RuntimeException("Cannot find FacesContext");
                }
	    
                // store the current FacesContext for use by other
                // UIComponentTags in the same page
                pageContext.setAttribute(CURRENT_FACES_CONTEXT, context);
            }
	}
	
	return (context);
	
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


}
