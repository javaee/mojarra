/*
 * $Id: UIComponent.java,v 1.8 2002/05/15 01:03:52 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.LifecycleHandler;
import javax.faces.render.AttributeDescriptor;
import javax.faces.render.Renderer;


/**
 * <p><strong>UIComponent</strong> is the base class for all user interface
 * components in JavaServer Faces.  The set of <code>UIComponent</code>
 * instances associated with a particular request or response are typically
 * organized into trees under a root <code>UIComponent</code> that represents
 * the entire request or response.</p>
 *
 * <h3>Properties</h3>
 *
 * <p>Each <code>UIComponent</code> instance supports the following
 * JavaBean properties to describe its render-independent characteristics:</p>
 * <ul>
 * <li><strong>componentId</strong> (java.lang.String) - An identifier for this
 *     component, which must be unique across all children of the parent
 *     node in a tree.  Identifiers may be composed of letters, digits,
 *     dashes ('-'), and underscores ('_').  To minimize the size of
 *     responses rendered by JavaServer Faces, it is recommended that
 *     identifiers be as short as possible.</li>
 * <li><strong>componentType</strong> - The canonical name of the component
 *     type represented by this <code>UIComponent</code> instance.  For all
 *     standard component types, this value is represented by a manifest
 *     constant String named <code>TYPE</code> in the implementation class.
 *     To facilitate introspection by tools, it is recommended that user
 *     defined <code>UIComponent</code> subclasses follow the same
 *     convention.</li>
 * <li><strong>compoundId</strong> (java.lang.String) - A unique (within
 *     the component tree containing this component) identifier for the
 *     current node, which begins with a slash character ('/'), followed by
 *     the <code>componentId</code> of each parent of the current component
 *     (from the top down) followed by a slash character ('/'), and ending
 *     with the <code>componentId</code> of this component.  [READ-ONLY]</li>
 * <li><strong>facesContext</strong> (javax.faces.context.FacesContext) -
 *     For the root component in a component tree, the {@link FacesContext}
 *     within which this component tree is registered.</p>
 * <li><strong>lifecycleHandler</strong>
 *     (javax.faces.lifecycle.LifecycleHandler) - Processing object to which
 *     we delegate the behavioral aspects of component-level lifecycle phase
 *     processing.  If not set, the corresponding methods on this
 *     component instance will be called instead.</p>
 * <li><strong>model</strong> (java.lang.STring) - A symbolic expression
 *     used to attach this component to <em>model</em> data in the underlying
 *     application (typically a JavaBean property).  The syntax of this
 *     expression corresponds to the expression language described in
 *     Appendix A of the <em>JavaServer Pages Standard Tag Library</em>
 *     (version 1.0) specification.</li>
 * <li><strong>parent</strong> (javax.faces.component.UIComponent) - The
 *     parent <code>UIComponent</code> in the tree within which this component
 *     is nested.  The root node of a component tree will not have a parent.
 *     </li>
 * <li><strong>rendererType</strong> (java.lang.String) - Logical identifier
 *     of the type of {@link Renderer} to use when rendering this component
 *     to a response.  If not specified, this component must render itself
 *     directly in the <a href="#render(javax.faces.context.FacesContext)">
 *     render()</a> method.</li>
 * <li><strong>value</strong> - The local value of this
 *     <code>UIComponent</code>, which represents a server-side cache of the
 *     value most recently entered by a user.  <strong>FIXME</strong> -
 *     discussions about when this value is cleared, how validation and
 *     caching of converted values works, and so on.</li>
 * </ul>
 *
 * <h3>Attributes</h3>
 *
 * <p>Each <code>UIComponent</code> instance supports a set of dynamically
 * defined <em>attributes</em>, normally used to describe the render-dependent
 * characteristics of the component.  In addition, properties that represent
 * the render-independent characteristics of the component MUST be gettable
 * and settable via the attributes access methods as well.</p>
 *
 * <p>Individual <code>RenderKit</code> implementations will support unique
 * sets of render-dependent attributes for a given component type.  The names
 * and characteristics of these attributes can be determined by acquiring a
 * <code>Renderer</code> of the type specified by the <code>rendererType</code>
 * property, and calling its <code>getAttributeNames()</code> method.</p>
 *
 * <h3>Component Trees and Navigation</h3>
 *
 * <p>Every <code>UIComponent</code> instance can belong to a tree of
 * components representing the current request or response.  This is
 * represented by the existence of a <code>parent</code> property to
 * represent the parent node, and a set of methods that facilitate
 * manipulation of the set of children belonging to the current node.</p>
 *
 * <p>Further, a unique (within a component tree) identifier, accessible
 * via the <code>compoundId</code> read-only property, can be calculated
 * for each component in the tree.  The syntax and semantics of compound
 * identifiers match the corresponding notions in operating system filesystems,
 * as well as URL schemes that support hierarchical identifiers (such as
 * <code>http</code>), where a leading slash character ('/') identifies
 * the root of the component tree, and subordinate nodes of the tree are
 * selected by their <code>componentId</code> property followed by a slash.</p>
 *
 * <p><code>UIComponent</code> supports navigation from one component to
 * another, within the component tree containing this component, using
 * absolute and relative path expressions.  See
 * <a href="#findComponent(java.lang.String)">findComponent()</a> for
 * more information.</p>
 *
 * <h3>Lifecyle Phase Processing</h3>
 *
 * <p>This class implements the {@link LifecycleHandler} interface, as well
 * as supporting a <code>lifecycleHandler</code> property that supports
 * optional delegation of processing to an external object.  Lifecycle phases
 * that interact with the component tree are required to call the corresponding
 * method on the delegated handler if there is one; otherwise to call the
 * corresponding method on this <code>UIComponent</code> instance.  This
 * allows custom component developers to centralize all of the state management
 * and behavior of that component into a single class, if desired.</p>
 */

public abstract class UIComponent implements LifecycleHandler {


    /**
     * <p>The component type of this <code>UIComponent}</code>.</p>
     */
    public static final String TYPE = "Component";


    // ------------------------------------------------------------- Attributes


    /**
     * <p>The set of attribute values, keyed by the corresponding attribute
     * name.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The collection for attributes
     * must be lazily instantiated the first time that it is actually used,
     * in order to avoid wasted object creations.  This can be done by
     * calling <code>getAttributes()</code>.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - It is assumed that components
     * can only be modified in the context of a single thread.</p>
     */
    private HashMap attributes = null;


    /**
     * <p>Create (if necessary) and return the collection to be used for
     * attributes storage.</p>
     */
    private Map getAttributes() {

        if (attributes == null) {
            attributes = new HashMap();
        }
        return (attributes);

    }


    /**
     * <p>Have we allocated a collection for attributes yet?</p>
     */
    private boolean isAttributesAllocated() {

        return (attributes != null);

    }


    /**
     * <p>Return the value of the attribute with the specified name
     * (if any); otherwise, return <code>null</code>.</p>
     *
     * @param name Name of the requested attribute
     *
     * @exception NullPointerException if <code>name</code> is
     *  <code>null</code>
     */
    public Object getAttribute(String name) {

        // Validate method parameters
        if (name == null) {
            throw new NullPointerException("getAttribute");
        }

        // Special cases for read-only and special case attributes
        if ("componentType".equals(name)) {
            return (getComponentType());
        } else if ("compoundId".equals(name)) {
            return (getCompoundId());
        } else if ("value".equals(name)) {
            return (getValue());
        }

        // Return the selected attribute value
        if (!isAttributesAllocated()) {
            return (null);
        }
        return (getAttributes().get(name));

    }


    /**
     * <p>Return an <code>Iterator</code> over the names of all
     * currently defined attributes of this <code>UIComponent</code>.
     */
    public Iterator getAttributeNames() {

        if (isAttributesAllocated()) {
            return (getAttributes().keySet().iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }


    /**
     * <p>Set the new value of the attribute with the specified name,
     * replacing any existing value for that name.</p>
     *
     * @param name Name of the requested attribute
     * @param value New value (or <code>null</code> to remove
     *  any attribute value for the specified name
     *
     * @exception IllegalArgumentException if <code>name</code> represents
     *  a read-only property of this component
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public void setAttribute(String name, Object value) {

        // Validate method parameters
        if (name == null) {
            throw new NullPointerException("setAttribute");
        }
        // FIXME - validate length and contents for componentId

        // Special cases for read-only pseudo-attributes
        if ("componentType".equals(name) ||
            "compoundId".equals(name)) {
            throw new IllegalArgumentException(name);
        }

        // Set or remove the specified value
        if (value != null) {
            getAttributes().put(name, value);
        } else {
            getAttributes().remove(name);
        }

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the identifier of this <code>UIComponent</code>.</p>
     */
    public String getComponentId() {

        return ((String) getAttribute("componentId"));

    }


    /**
     * <p>Set the identifier of this <code>UIComponent</code>.
     *
     * @param componentId The new identifier
     *
     * @exception IllegalArgumentException if <code>componentId</code>
     *  is zero length or contains invalid characters
     * @exception NullPointerException if <code>componentId</code>
     *  is <code>null</code>
     */
    public void setComponentId(String componentId) {

        if (componentId == null) {
            throw new NullPointerException("setComponentId");
        }
        // FIXME - validate length>0 and valid characters
        setAttribute("componentId", componentId);

    }


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the <em>compound identifier</em> of this component.</p>
     */
    public String getCompoundId() {

        // Accumulate the component identifiers of our ancestors
        ArrayList list = new ArrayList();
        list.add(getComponentId());
        UIComponent parent = getParent();
        while (parent != null) {
            list.add(0, parent.getComponentId());
            parent = parent.getParent();
        }

        // Render the compound identifier from the top down
        StringBuffer sb = new StringBuffer();
        int n = list.size();
        for (int i = 0; i < n; i++) {
            sb.append(EXPR_SEPARATOR);
            sb.append((String) list.get(i));
        }
        return (sb.toString());

    }


    /**
     * <p>Return the {@link FacesContext} within which the component tree
     * (for which this component is the root node) is registered.</p>
     */
    public FacesContext getFacesContext() {

        return ((FacesContext) getAttribute("facesContext"));

    }


    /**
     * <p>Set the {@link FacesContext} within which the component tree
     * (for which this component is the root node) is registered.</p>
     *
     * @param context The new {@link FacesContext}, or <code>null</code>
     *  to disconnect this node from any context
     */
    public void setFacesContext(FacesContext context) {

        setAttribute("facesContext", context);

    }


    /**
     * <p>Return the lifecycle phase handler for this <code>UIComponent</code>,
     * if any.</p>
     */
    public LifecycleHandler getLifecycleHandler() {

        return ((LifecycleHandler) getAttribute("lifecycleHandler"));

    }


    /**
     * <p>Set the lifecycle phase handler for this <code>UIComponent</code>.
     *
     * @param lifecycleHandler The new lifecycle phase handler
     */
    public void setLifecycleHandler(LifecycleHandler lifecycleHandler) {

        setAttribute("lifecycleHandler", lifecycleHandler);

    }


    /**
     * <p>Return the symbolic model reference expression of this
     * <code>UIComponent</code>, if any.</p>
     */
    public String getModel() {

        return ((String) getAttribute("model"));

    }


    /**
     * <p>Set the symbolic model reference expression of this
     * <code>UIComponent</code>.</p>
     *
     * @param model The new symbolic model reference expression, or
     *  <code>null</code> to disconnect this component from any model data
     */
    public void setModel(String model) {

        setAttribute("model", model);

    }


    /**
     * <p>Return the parent <code>UIComponent</code> of this
     * <code>UIComponent</code>, if any.</p>
     */
    public UIComponent getParent() {

        return ((UIComponent) getAttribute("parent"));

    }


    /**
     * <p>Set the parent <code>UIComponent</code> of this
     * <code>UIComponent</code>.</p>
     *
     * @param parent The new parent, or <code>null</code> for the root node
     *  of a component tree
     */
    public void setParent(UIComponent parent) {

        setAttribute("parent", parent);

    }


    /**
     * <p>Return the {@link Renderer} type for this <code>UIComponent</code>
     * (if any).</p>
     */
    public String getRendererType() {

        return ((String) getAttribute("rendererType"));

    }


    /**
     * <p>Set the {@link Renderer} type for this <code>UIComponent</code>,
     * or <code>null</code> for components that render themselves.</p>
     *
     * @param rendererType Logical identifier of the type of
     *  {@link Renderer} to use, or <code>null</code> for components
     *  that render themselves
     */
    public void setRendererType(String rendererType) {

        setAttribute("rendererType", rendererType);

    }


    /**
     * <p>Return the local value of this <code>UIComponent</code>, if any.
     */
    public Object getValue() {

        return (getAttribute("value"));

    }


    /**
     * <p>Set the local value of this <code>UIComponent</code>.</p>
     *
     * @param value The new local value
     */
    public void setValue(Object value) {

        setAttribute("value", value);

    }


    /**
     * <p>Evaluate and return the current value of this component, according
     * to the following algorithm:</p>
     * <ul>
     * <li>If the <code>value</code> property has been set (corresponding
     *     to the local value for this component), return that; else</li>
     * <li>If the <code>model</code> property has been set, retrieve and
     *     return the corresponding model value, if possible; else</li>
     * <li>Return <code>null</code>.</li>
     * </ul>
     */
    public Object currentValue() {

        Object value = getAttribute("value");
        if (value != null) {
            return (value);
        }
        String model = (String) getAttribute("model");
        if (model != null) {
            FacesContext context = findComponent("/").getFacesContext();
            if (context != null) {
                return (context.getModelValue(model));
            }
        }
        return (null);

    }


    // ------------------------------------------------ Tree Management Methods


    /**
     * <p>The set of child <code>UIComponent</code>s associated with this
     * component, in the order specified by how the components were added.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The collection for children
     * must be lazily instantiated the first time that it is actually used,
     * in order to avoid wasted object creations.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - It is assumed that components
     * can only be modified in the context of a single thread.</p>
     */
    private ArrayList children = null;


    /**
     * <p>If the specified <code>componentId</code> is already present
     * in one of our children, throw an exception.</p>
     *
     * @param componentId The component identifier to check
     *
     * @exception IllegalArgumentException if this component identifier is
     *  already in use by one of our children
     */
    private void checkComponentId(String componentId) {

        if (isChildrenAllocated()) {
            Iterator kids = children.iterator();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                if (componentId.equals(kid.getComponentId())) {
                    throw new IllegalArgumentException(componentId);
                }
            }
        }

    }
            


    /**
     * <p>Create (if necessary) and return an iterator over the child
     * components of this component.</p>
     */
    private List getChildren() {

        if (children == null) {
            children = new ArrayList();
        }
        return (children);

    }


    /**
     * <p>Have we allocated a collection for children yet?</p>
     */
    private boolean isChildrenAllocated() {

        return (children != null);

    }


    /**
     * <p>Insert the specified {@link UIComponent} at the specified
     * position in the child list.</p>
     *
     * @param index Zero-relative index at which to add this
     *  {@link UIComponent}
     * @param component {@link UIComponent} to be added
     *
     * @exception IllegalArgumentException if the component identifier
     *  of the new component is not unique within the children of
     *  this component
     * @exception IndexOutOfBoundsException if the index is out of range
     *  ((index < 0) || (index &gt;= size()))
     * @exception NullPointerException if <code>component</code> is null
     */
    public void add(int index, UIComponent component) {

        checkComponentId(component.getComponentId());
        getChildren().add(index, component);

    }


    /**
     * <p>Append the specified {@link UIComponent} to the end of the
     * child list.</p>
     *
     * @param component {@link UIComponent} to be added
     *
     * @exception IllegalArgumentException if the component identifier
     *  of the new component is not unique within the children of
     *  this component
     * @exception NullPointerException if <code>component</code> is null
     */
    public void add(UIComponent component) {

        checkComponentId(component.getComponentId());
        getChildren().add(component);

    }


    /**
     * <p>Remove all child {@link UIComponent}s from the child list,
     * recursively performing this operation when a child {@link UIComponent}
     * also has children.</p>
     */
    public void clear() {

        if (!isChildrenAllocated()) {
            return;
        }
        Iterator kids = getChildren().iterator();
        while (kids.hasNext()) {
            ((UIComponent) kids.next()).clear();
        }
        children.clear();

    }


    /**
     * <p>Return <code>true</code> if the specified {@link UIComponent}
     * is a direct child of this <code>UIComponent</code>; otherwise,
     * return <code>false</code>.</p>
     *
     * @param component {@link UIComponent} to be checked
     *
     * @exception NullPointerException if <code>component</code> is null
     */
    public boolean contains(UIComponent component) {

        if (isChildrenAllocated()) {
            return (getChildren().contains(component));
        } else {
            return (false);
        }

    }


    /**
     * Segment separator in navigation expressions.
     */
    private static final String EXPR_SEPARATOR = "/";


    /**
     * "This element" selector in navigation expressions.
     */
    private static final String EXPR_CURRENT = ".";


    /**
     * "Parent element" selector in navigation expressions.
     */
    private static final String EXPR_PARENT = "..";


    /**
     * <p>Find a related component in the current component tree by evaluating
     * the specified navigation expression (which may be absolute or relative)
     * to locate the requested component, which is then returned.
     * Valid expression values are:</p>
     * <ul>
     * <li><em>Absolute Path</em> (<code>/a/b/c</code>) - Expressions that
     *     start with a slash begin at the root component of the current tree,
     *     and match exactly against the <code>compoundId</code> of the
     *     selected component.</li>
     * <li><em>Root Component</em> - (<code>/</code>) - An expression with
     *     only a slash selects the root component of the current tree.</li>
     * <li><em>Relative Path</em> - (<code>a/b</code>) - Start at the current
     *     component (rather than the root), and navigate downward.</li>
     * <li><em>Special Path Elements</em> - A path element with a single
     *     period (".") selects the current component, while a path with two
     *     periods ("..") selects the parent of the current node.</li>
     * </ul>
     *
     * @param expr Navigation expression to interpret
     *
     * @exception IllegalArgumentException if the syntax of <code>expr</code>
     *  is invalid
     * @exception IllegalArgumentException if <code>expr</code> attempts to
     *  cause navigation to a component that does not exist
     * @exception NullPointerException if <code>expr</code>
     *  is <code>null</code>
     */
    public UIComponent findComponent(String expr) {

        if (expr == null) {
            throw new NullPointerException("findChildren");
        }

        // If this is an absolute expression, start at the root node
        // Otherwise, start at the current node
        UIComponent node = this;
        if (expr.startsWith(EXPR_SEPARATOR)) {
            while (node.getParent() != null) {
                node = node.getParent();
            }
            expr = expr.substring(1);
        }

        // Parse and process each segment of the path
        while (expr.length() > 0) {

            // Identify the next segment
            String segment = null;
            int separator = expr.indexOf(EXPR_SEPARATOR);
            if (separator < 0) {
                segment = expr;
                expr = "";
            } else {
                segment = expr.substring(0, separator);
                expr = expr.substring(separator + 1);
            }

            // Process the identified segment
            if (segment.equals(EXPR_CURRENT)) {
                ; // node already points here
            } else if (segment.equals(EXPR_PARENT)) {
                node = node.getParent();
                if (node == null) {
                    throw new IllegalArgumentException(segment);
                }
            } else {
                boolean found = false;
                Iterator kids = node.iterator();
                while (kids.hasNext()) {
                    node = (UIComponent) kids.next();
                    if (segment.equals(node.getComponentId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new IllegalArgumentException(segment);
                }
            }

        }

        // Return the selected node
        return (node);

    }



    /**
     * <p>Return the {@link UIComponent} at the specified position
     * in the child list.</p>
     *
     * @param index Position of the desired component
     *
     * @exception IndexOutOfBoundsException if index is out of range
     *  ((index &lt; 0) || (index &gt;= size()))
     */
    public UIComponent get(int index) {

        return ((UIComponent) getChildren().get(index));

    }


    /**
     * <p>Return the index of the specified {@link UIComponent} in the
     * child list, or <code>-1</code> if this component is not a child.</p>
     *
     * @param component {@link UIComponent} to be checked
     *
     * @exception NullPointerException if <code>component</code> is null
     */
    public int indexOf(UIComponent component) {

        return (getChildren().indexOf(component));

    }


    /**
     * <p>Return an <code>Iterator</code> over the child {@link UIComponent}s
     * of this <code>UIComonent</code> in the proper sequence.</p>
     */
    public Iterator iterator() {

        if (isChildrenAllocated()) {
            return (getChildren().iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }


    /**
     * <p>Remove the child {@link UIComponent} at the specified position
     * in the child list.</p>
     *
     * @param index Position of the desired component
     *
     * @exception IndexOutOfBoundsException if the index is out of range
     *  ((index < 0) || (index &gt;= size()))
     */
    public void remove(int index) {

        getChildren().remove(index);

    }


    /**
     * <p>Remove the child {@link UIComponent} from the child list.</p>
     *
     * @param component {@link UIComponent} to be removed
     *
     * @exception NullPointerException if <code>component</code> is null
     */
    public void remove(UIComponent component) {

        if (component == null) {
            throw new NullPointerException("remove");
        }
        getChildren().remove(component);

    }


    /**
     * <p>Replace the child {@link UIComponent} at the specified position
     * in the child list.</p>
     *
     * @param index Position of the desired component
     * @param component The new component
     *
     * @exception IllegalArgumentException if the component identifier
     *  of the new component is not unique within the children of
     *  this component
     * @exception IndexOutOfBoundsException if the index is out of range
     *  ((index < 0) || (index &gt;= size()))
     * @exception NullPointerException if <code>component</code> is null
     */
    public void set(int index, UIComponent component) {

        UIComponent current = get(index);
        if (!component.getComponentId().equals(current.getComponentId())) {
            checkComponentId(component.getComponentId());
        }
        getChildren().set(index, component);

    }


    /**
     * <p>Return the number of {@link UIComponent}s on the child list.</p>
     */
    public int size() {

        if (isChildrenAllocated()) {
            return (getChildren().size());
        } else {
            return (0);
        }

    }


    // ------------------------------------------- Lifecycle Processing Methods


    /**
     * <p>Handler for the <em>Reconstitute Request Tree</em> phase.  Ensure
     * that the properties and attributes of the specified component
     * reflect the values that were current as of the previous response.</p>
     *
     * @param context FacesContext of the request we are processing
     * @param component Current UIComponent being processed
     */
    public void reconstituteRequestTree(FacesContext context,
                                        UIComponent component) {

        ; // Default implementation does nothing

    }


    /**
     * <p>Handler for the <em>Apply Request Values</em> phase.  Extract
     * the new value (if any) from the current request, and update the
     * local value of the specified component.  While values are being
     * extracted, optionally queue events to be processed during the
     * next phase by (<strong>FIXME</strong> - specify mechanism).</p>
     *
     * @param context FacesContext of the request we are processing
     * @param component Current UIComponent being processed
     */
    public void applyRequestValues(FacesContext context,
                                   UIComponent component) {

        ; // Default implementation does nothing

    }


    /**
     * <p>Handler for the <em>Handle Request Events</em> phase.  Process
     * the specified event, for the specified component, that was queued
     * during the previous phase.  This method will be called once per
     * event that was queued.</p>
     *
     * @param context FacesContext of the request we are processing
     * @param component Current UIComponent being processed
     * @param event <strong>FIXME</strong> - event data???
     */
    public void handleRequestEvents(FacesContext context,
                                    UIComponent component) { // FIXME - event

        ; // Default implementation does nothing

    }



    /**
     * <p>Handler for the <em>Process Validations</em> phase.  Perform
     * all registered validations for the specified component, accumulating
     * error messages by (<strong>FIXME</strong> - specify mechanism).
     *
     * @param context FacesContext of the request we are processing
     * @param component Current UIComponent being processed
     */
    public void processValidations(FacesContext context,
                                   UIComponent component) {

        ; // Default implementation does nothing

    }


    /**
     * <p>Handler for the <em>Update Model Values</em> phase.  Copy the
     * local value of this component to the corresponding model object, if
     * a model reference has been defined for this component.
     * 
     * @param context FacesContext of the request we are processing
     * @param component Current UIComponent being processed
     */
    public void updateModelValues(FacesContext context,
                                  UIComponent component) {

        ; // Default implementation does nothing

    }


    /**
     * <p>Handler for the <em>Render Response</em> phase.  If a
     * <code>rendererType</code> has been defined for this component,
     * acquire a corresponding {@link Renderer} instance
     * from our {@link RenderKit}, and use it to render the state of this
     * component to the response.  If no <code>rendererType</code> has
     * been defined for this component, render the state of this
     * component directly.</p>
     *
     * <p><strong>FIXME</strong> - Deal with the complexities of
     * conditionally rendering child components.</p>
     * 
     * @param context FacesContext of the request we are processing
     * @param component Current UIComponent being processed
     */
    public void renderResponse(FacesContext context,
                               UIComponent component) {

        ; // Default implementation does nothing

    }


}
