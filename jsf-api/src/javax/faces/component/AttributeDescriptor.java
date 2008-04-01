/*
 * $Id: AttributeDescriptor.java,v 1.6 2002/07/12 00:30:01 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.Locale;
import javax.faces.render.Renderer;
import javax.faces.validator.Validator;


/**
 * <p>An <strong>AttributeDescriptor</strong> describes a single characteristic
 * of a <code>UIComponent</code> that might be of interest to other classes,
 * such as a {@link Validator} or a {@link Renderer}.  This information is
 * particularly useful in tools that wish to automate the creation of user
 * interfaces based on JavaServer Faces components.</p>
 */

public abstract class AttributeDescriptor {


    /**
     * <p>Return a brief description of this attribute, useful when
     * rendering help text in a tool, localized for the default
     * <code>Locale</code> for this instance of the Java Virtual Machine.</p>
     */
    public abstract String getDescription();


    /**
     * <p>Return a brief description of this attribute, useful when
     * rendering help text in a tool, localized for the specified
     * <code>Locale</code>.</p>
     *
     * @param locale Locale for which to retrieve a localized description
     *
     * @exception NullPointerException if <code>locale</code>
     *  is <code>null</code>
     */
    public abstract String getDescription(Locale locale);


    /**
     * <p>Return a short displayable name of this attribute, useful in
     * constructing the user interface of a tool, localized for the
     * default <code>Locale</code> for this instance of the Java Virtual
     * Machine.</p>
     */
    public abstract String getDisplayName();


    /**
     * <p>Return a short displayable name of this attribute, useful in
     * constructing the user interface of a tool, localized for the
     * specified <code>Locale</code>.</p>
     *
     * @param locale Locale for which to retrieve a display name
     */
    public abstract String getDisplayName(Locale locale);


    /**
     * <p>Return the attribute name of the attribute described by this
     * <code>AttributeDescriptor</code>.</p>
     */
    public abstract String getName();


    /**
     * <p>Return the Java class representing the data type of the legal
     * values for this attribute.</p>
     */
    public abstract Class getType();


}
