/*
 * $Id: AttributeDescriptorImpl.java,v 1.3 2002/08/04 20:09:42 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.component.AttributeDescriptor;


/**
 * <p>Internal implementation of {@link AttributeDescriptor} used by classes
 * within this package.  This class is <strong>NOT</strong> part of the public
 * API of JavaServer Faces.</p>
 */

final class AttributeDescriptorImpl extends AttributeDescriptor {


    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The base name of our localized resource bundle.
     */
    private static final String BUNDLE = "javax.faces.validator.Resources";


    // ----------------------------------------------------------- Constructors


    /**
     * Construct an {@link AttributeDescriptor} with the specified values.
     *
     * @param name Attribute name being described
     * @param type Valid Java type for this attribute
     * @param base Base message key for our localized description
     *  and display name lookups
     */
    public AttributeDescriptorImpl(String name, Class type, String base) {

        super();
        this.name = name;
        this.type = type;
        this.displayNameKey = base + ".displayName";
        this.descriptionKey = base + ".description";

    }


    // ----------------------------------------------------- Instance Variables


    private String descriptionKey = null;
    private String displayNameKey = null;
    private String name = null;
    private Class type = null;


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Return a brief description of this attribute, useful when
     * rendering help text in a tool, localized for the default
     * <code>Locale</code> for this instance of the Java Virtual Machine.</p>
     */
    public String getDescription() {

        return (getDescription(Locale.getDefault()));

    }


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
    public String getDescription(Locale locale) {

        if (locale == null) {
            throw new NullPointerException();
        }
        return (getMessage(descriptionKey, locale));

    }


    /**
     * <p>Return a short displayable name of this attribute, useful in
     * constructing the user interface of a tool, localized for the
     * default <code>Locale</code> for this instance of the Java Virtual
     * Machine.</p>
     */
    public String getDisplayName() {

        return (getDisplayName(Locale.getDefault()));

    }


    /**
     * <p>Return a short displayable name of this attribute, useful in
     * constructing the user interface of a tool, localized for the
     * specified <code>Locale</code>.</p>
     *
     * @param locale Locale for which to retrieve a display name
     *
     * @exception NullPointerException if <code>locale</code>
     *  is <code>null</code>
     */
    public String getDisplayName(Locale locale) {

        if (locale == null) {
            throw new NullPointerException();
        }
        return (getMessage(displayNameKey, locale));

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


    // ------------------------------------------------------- Static Variables


    /**
     * <p>The set of <code>ResourceBundle</code> instances for the
     * localized text of attribute descriptors and display names in
     * this package, keyed by <code>Locale</code>.</p>
     */
    private static HashMap bundles = new HashMap();


    // --------------------------------------------------------- Static Methods


    /**
     * <p>Return the <code>ResourceBundle</code> for the specified
     * <code>Locale</code>, if there is one; otherwise, return
     * <code>null</code>.</p>
     *
     * @param locale Locale for which to acquire a resource bundle,
     *  or <code>null</code> for the default locale of this JVM
     */
    private ResourceBundle getBundle(Locale locale) {

        // Return any cached resource bundle
        synchronized (bundles) {
            if (bundles.containsKey(locale)) {
                return ((ResourceBundle) bundles.get(locale));
            }
        }

        // Acquire a resource bundle for this locale, if possible
        ResourceBundle bundle = null;
        try {
            if (locale == null) {
                bundle = ResourceBundle.getBundle(BUNDLE);
            } else {
                bundle = ResourceBundle.getBundle(BUNDLE, locale);
            }
            synchronized (bundles) {
                bundles.put(locale, bundle);
            }
            return (bundle);
        } catch (MissingResourceException e) {
            return (null);
        }

    }


    /**
     * <p>Return the message string with the specified key, localized
     * for the specified <code>Locale</code>.  If no such message can
     * be found, return <code>null</code>.</p>
     *
     * @param key Message key of the desired message
     * @param locale Locale to localize for, or <code>null</code>
     *  for the default <code>Locale</code> for this JVM
     */
    private String getMessage(String key, Locale locale) {

        ResourceBundle bundle = getBundle(locale);
        if (bundle == null) {
            return (null);
        }
        try {
            return (bundle.getString(key));
        } catch (MissingResourceException e) {
            return (null);
        }

    }


}
