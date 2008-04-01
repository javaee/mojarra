/*
 * $Id: AttributeDescriptor.java,v 1.4 2002/05/23 17:45:31 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.render.Renderer;


/**
 * <p>An <strong>AttributeDescriptor</strong> describes a single characteristic
 * of a <code>UIComponent</code> that might be of interest to other classes,
 * such as a {@link Validator} or a {@link Renderer}.  This information is
 * particularly useful in tools that wish to automate the creation of user
 * interfaces based on JavaServer Pages components.</p>
 *
 * <p><strong>FIXME</strong> - Specify optional list of legal values?  Specify
 * optional default value?</p>
 */

public abstract class AttributeDescriptor {


    /**
     * <p>Return a brief description of this attribute, useful when
     * rendering help text in a tool.</p>
     *
     * <p><strong>FIXME</strong> - I18N.</p>
     */
    public abstract String getDescription();


    /**
     * <p>Return a short displayable name of this attribute, useful in
     * constructing the user interface of a tool.</p>
     *
     * <p><strong>FIXME</strong> - I18N.</p>
     */
    public abstract String getDisplayName();


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
