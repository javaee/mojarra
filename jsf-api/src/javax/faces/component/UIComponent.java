/*
 * $Id: UIComponent.java,v 1.1 2002/05/07 05:18:56 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import java.util.Iterator;
import javax.faces.context.FacesContext;


/**
 * <p><strong>UIComponent</strong> is the base class for all user interface
 * components in JavaServer Faces.  The set of <code>UIComponent</code>
 * instances associated with a particular request or response are typically
 * organized into trees (with the root node, and all intervening nodes,
 * being instances of {@link UIContainer}).</p>
 *
 * <h3>Properties</h3>
 *
 * <p>Each <code>UIComponent</code> instance supports the following
 * JavaBean properties to describe its render-independent characteristics:</p>
 * <ul>
 * <li><strong>compoundId</strong> (java.lang.String) - A unique (within
 *     the component tree containing this component) identifier for the
 *     current node, consisting of the <code>id</code> of each component from
 *     the top of the tree down to (and including) the current node, separated
 *     by slash ('/') characters.  [READ-ONLY]</li>
 * <li><strong>id</strong> (java.lang.String) - An identifier for this
 *     component, which must be unique across all children of the parent
 *     {@link UIContainer}.  Identifiers may be composed of letters, digits,
 *     dashes ('-'), and underscores ('_').  To minimize the size of
 *     responses rendered by JavaServer Faces, it is recommended that
 *     identifiers be as short as possible.</li>
 * <li><strong>model</strong> (java.lang.STring) - A symbolic expression
 *     used to attach this component to <em>model</em> data in the underlying
 *     application (typically a JavaBean property).  The syntax of this
 *     expression corresponds to the expression language described in
 *     Appendix A of the <em>JavaServer Pages Standard Tag Library</em>
 *     (version 1.0) specification.</li>
 * <li><strong>parent</strong> (javax.faces.component.UIContainer) - The
 *     parent {@link UIContainer} in which this <code>UIComponent</code> is
 *     nested.  The root {@link UIContainer} will not have a parent.</li>
 * <li><strong>root</strong> (javax.faces.component.UIContainer) - The
 *     root {@link UIContainer} of the tree in which this component
 *     is nested.  [READ-ONLY]</li>
 * <li><strong>type</strong> - The canonical name of the component type
 *     represented by this <code>UIComponent</code> instance.  For all
 *     standard component types, this value is represented by a manifest
 *     constant String named <code>TYPE</code> in the implementation class.
 *     To facilitate introspection by tools, it is recommended that user
 *     defined <code>UIComponent</code> subclasses follow the same
 *     convention.</li>
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
 * characteristics of the comopnent.  The set of supported attribute names
 * (and types) for a particular <code>UIComponent</code> subclass can be
 * introspected (for example, by development tools), through a call to the
 * <code>getAttributeNames()</code> and <code>getAttributeDescriptor</code>
 * methods.</p>
 *
 * <p><strong>FIXME</strong> - Lots more about lifecycle, etc.</p>
 */

public abstract class UIComponent {


    /**
     * <p>The component type of this <code>UIComponent}</code>.</p>
     */
    public static final String TYPE = "Component";


    // ------------------------------------------------------------- Attributes


    /**
     * <p>Return the value of the attribute with the specified name,
     * which may be <code>null</code>.</p>
     *
     * @param name Name of the requested attribute
     *
     * @exception IllegalArgumentException if <code>name</code> does not
     *  identify an attribute supported by this component
     * @exception NullPointerException if <code>name</code> is
     *  <code>null</code>
     */
    public abstract Object getAttribute(String name);


    /**
     * <p>Return an {@link AttributeDescriptor} describing a
     * render-dependent attribute that is supported by this component,
     * or <code>null</code> if no attribute by this name is supported
     * by this component.</p>
     *
     * <p><strong>FIXME</strong> - The set of attributes supported by a
     * component is dependent on the RenderKit (if any) in use.</p>
     *
     * @param name Name of the requested attribute
     *
     * @exception NullPointerException if <code>name</code> is
     *  <code>null</code>
     */
    public abstract AttributeDescriptor getAttributeDescriptor(String name);


    /**
     * <p>Return an <code>Iterator</code> over the names of all
     * render-dependent attributes supported by this <code>UIComponent</code>.
     * For each such attribute, descriptive information can be retrieved
     * via <code>getAttributeDescriptor()</code>, and the current value
     * can be retrieved via <code>getAttribute()</code>.</p>
     */
    public abstract Iterator getAttributeNames();


    /**
     * <p>Set the new value of the attribute with the specified name,
     * replacing any existing value for that name.</p>
     *
     * @param name Name of the requested attribute
     * @param value New value (which may be <code>null</code>)
     *
     * @exception IllegalArgumentException if <code>name</code> does not
     *  identify an attribute supported by this component
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public abstract void setAttribute(String name, Object value);


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the <em>compound identifier</em> of this component.</p>
     */
    public abstract String getCompoundId();


    /**
     * <p>Return the identifier of this <code>UIComponent</code>.</p>
     */
    public abstract String getId();


    /**
     * <p>Set the identifier of this <code>UIComponent</code>.
     *
     * @param id The new identifier
     *
     * @exception IllegalArgumentException if <code>id</code> is zero length
     *  or contains invalid characters
     * @exception NullPointerException if <code>id</code> is <code>null</code>
     */
    public abstract void setId(String id);


    /**
     * <p>Return the symbolic model reference expression of this
     * <code>UIComponent</code>, if any.</p>
     */
    public abstract String getModel();


    /**
     * <p>Set the symbolic model reference expression of this
     * <code>UIComponent</code>.</p>
     *
     * @param model The new symbolic model reference expression, or
     *  <code>null</code> to disconnect this component from any model data
     */
    public abstract void setModel(String model);


    /**
     * <p>Return the parent {@link UIContainer} of this
     * <code>UIComponent</code>, if any.</p>
     */
    public abstract UIContainer getParent();


    /**
     * <p>Set the parent {@link UIContainer} of this
     * <code>UIComponent</code>.</p>
     *
     * @param parent The new parent, or <code>null</code> for the root node
     *  of a component tree
     */
    public abstract void setParent(UIContainer parent);


    /**
     * <p>Return the root {@link UIContainer} of the tree in which this
     * <code>UIComponent</code> is a member.</p>
     */
    public abstract UIContainer getRoot();


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public abstract String getType();


    /**
     * <p>Return the local value of this <code>UIComponent</code>.</p>
     */
    public abstract Object getValue();


    /**
     * <p>Set the local value of this <code>UIComponent</code>.</p>
     *
     * @param value The new local value
     */
    public abstract void setValue(Object value);


    // ------------------------------------------- Lifecycle Processing Methods


    /**
     * <p>Extract new values for this <code>UIComponent</code> (if any) from
     * the specified {@link FacesContext}.  This method is called during the
     * <em>Apply Request Values</em> phase of {@link Lifecycle} processing of
     * the curernt request.</p>
     *
     * <p><strong>FIXME</strong> - Specify how components can queue up
     * application events to be processed by components during the next phase.
     * </p>
     *
     * <p><strong>FIXME</strong> - Any need for exceptions here?</p>
     *
     * @param context FacesContext for the current request being processed
     */
    public abstract void applyRequestValues(FacesContext context);


    /**
     * <p>Process all events queued for this <code>UIComponent</code> during
     * the <em>Apply Request Values</em> phase that was performed previously.
     * This method is called during the <em>Handle Request Events</em>
     * phase of the {@link Lifecycle} processing of the current request.</p>
     *
     * <p>If so desired, a component can signal that lifecycle control should
     * be transferred directly to the <em>Render Response</em> phase
     * (<strong>FIXME</strong> - specify the mechanism for this), once
     * all event processing on all components has been completed.</p>
     *
     * <p><strong>FIXME</strong> - Specify how a component gets access to the
     * events that have been queued to it.</p>
     *
     * @param context FacesContext for the current request being processed
     */
    public abstract void handleRequestEvents(FacesContext context);


    /**
     * <p>Perform all validations that have been registered for this
     * <code>UIComponent</code>.  In general, component validation should
     * include an attempt to convert the local value to the data type that
     * will ultimately be required, if appropriate.</p>
     *
     * <p>If a component detects one or move validation errors, it can
     * enqueue a set of message objects to a message queue that can be used
     * in the rendered response (<p><strong>FIXME</strong> - Specify the
     * mechanism for doing this).</p>
     *
     * @param context FacesContext for the current request being processed
     */
    public abstract void processValidations(FacesContext context);


    /**
     * <p>Update any model data associated with this <code>UIComponent</code>
     * via the <code>model</code> property, and clear the local value.  If
     * there is no model data associated with this component, no action
     * is performed.</p>
     *
     * @param context FacesContext for the current request being processed
     */
    public abstract void updateModelValues(FacesContext context);


}
