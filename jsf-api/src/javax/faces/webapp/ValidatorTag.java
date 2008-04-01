/*
 * $Id: ValidatorTag.java,v 1.3 2002/08/29 16:24:38 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.component.UIComponent;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p><strong>ValidatorTag</strong> is a base class for all JSP custom actions
 * that create and register a <code>Validator</code> instance on the
 * {@link UIComponent} associated with our most immediate surrounding instance
 * of a tag whose implementation class is a subclass of {@link FacesTag}.
 * To avoid creating duplicate instances when a page is redisplayed,
 * creation and registration of a {@link Validator} occurs
 * <strong>only</strong> if the corresponding {@link UIComponent} was
 * created (by the owning {@link FacesTag}) during the execution of the
 * current page.</p>
 *
 * <p>Subclasses of this class must implement the
 * <code>createValidator()</code> method, which creates and returns a
 * {@link Validator} instance.  Any configuration properties that specify
 * the limits to be enforced by this {@link Validator} must have been
 * set by the <code>createValidator()</code> method.  Generally, this occurs
 * by copying corresponding attribute values on the tag instance.</p>
 *
 * <p>This tag creates no output to the page currently being created.  It
 * is used solely for the side effect of {@link Validator} creation.</p>
 */

public abstract class ValidatorTag extends TagSupport {


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Create a new instance of the specified {@link Validator}
     * class, and register it with the {@link UIComponent} instance associated
     * with our most immediately surrounding {@link FacesTag} instance, if
     * the {@link UIComponent} instance was created by this execution of the
     * containing JSP page.</p>
     *
     * @exception JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        // Locate our parent FacesTag
        Tag tag = getParent();
        while ((tag != null) && !(tag instanceof FacesTag)) {
            tag = tag.getParent();
        }
        if (tag == null) { // FIXME - i18n
            throw new JspException("Not nested in a FacesTag");
        }
        FacesTag facesTag = (FacesTag) tag;

        // Nothing to do unless this tag created a component
        if (!facesTag.getCreated()) {
            return (SKIP_BODY);
        }

        // Create a new instance of the specified Validator
        Validator validator = createValidator();

        // Register this instance with the appropriate component
        facesTag.getComponent().addValidator(validator);
        return (SKIP_BODY);

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Create and return a {@link Validator} instance, to be registered
     * with our surrounding {@link UIComponent}, that has been configured
     * based on the attributes used on this tag instance.</p>
     */
    protected abstract Validator createValidator();


}
