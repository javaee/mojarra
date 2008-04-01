/*
 * $Id: AttributeDescriptorImpl.java,v 1.1 2002/06/03 19:27:27 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import javax.faces.component.AttributeDescriptor;


/**
 * <p>Internal implementation of {@link AttributeDescriptor} used by classes
 * within this package.  This class is <strong>NOT</strong> part of the public
 * API of JavaServer Faces.</p>
 */

final class AttributeDescriptorImpl extends AttributeDescriptor {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct an {@link AttributeDescriptor} with the specified values.
     *
     * @param name Attribute name being described
     * @param type Valid Java type for this attribute
     * @param displayName Display name of this attribute
     * @param description Description of this attribute
     */
    public AttributeDescriptorImpl(String name, Class type,
                                   String displayName, String description) {

        super();
        this.name = name;
        this.type = type;
        this.displayName = displayName;
        this.description = description;

    }


    // ----------------------------------------------------- Instance Variables


    private String name = null;
    private Class type = null;
    private String displayName = null;
    private String description = null;


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Return a brief description of this attribute, useful when
     * rendering help text in a tool.</p>
     *
     * <p><strong>FIXME</strong> - I18N.</p>
     */
    public String getDescription() {

        return (this.description);

    }


    /**
     * <p>Return a short displayable name of this attribute, useful in
     * constructing the user interface of a tool.</p>
     *
     * <p><strong>FIXME</strong> - I18N.</p>
     */
    public String getDisplayName() {

        return (this.displayName);

    }


    /**
     * <p>Return the attribute name of the attribute described by this
     * <code>AttributeDescriptor</code>.</p>
     */
    public String getName() {

        return (this.name);

    }


    /**
     * <p>Return the Java class representing the data type of the legal
     * values for this attribute.</p>
     */
    public Class getType() {

        return (this.type);

    }


}
