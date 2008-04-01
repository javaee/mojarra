/*
 * $Id: RequiredValidator.java,v 1.1 2002/06/03 19:27:27 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import java.util.Collections;
import java.util.Iterator;
import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.MessageList;


/**
 * <p><strong>RequiredValidator</strong> is a {@link Validator} that checks
 * for the existence of a value for the associated component.  For the purposes
 * of this Validator, "existence" means a non-<code>null</code> value returned
 * by the <code>getValue()</code> method.</p>
 *
 * <p><strong>FIXME</strong> - In .03, a zero-length String would also fail
 * this Validator -- which behavior do we want?</p>
 */

public final class RequiredValidator extends Validator {


    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The message identifier of the {@link Message} to be created if
     * this validation fails.</p>
     */
    public static final String FAILED_MESSAGE_ID =
        "javax.faces.component.RequiredValidator.FAILED";



    // --------------------------------------------------------- Public Methods


    /**
     * <p>Return an {@link AttributeDescriptor} for the specified attribute
     * name that is supported by this <code>Validator</code>.</p>
     *
     * @param name The requested attribute name
     *
     * @exception IllegalArgumentException if this attribute is not
     *  supported by this <code>Validator</code>.
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public AttributeDescriptor getAttributeDescriptor(String name) {

        throw new IllegalArgumentException(name);

    }


    /**
     * <p>Return an <code>Iterator</code> over the names of the supported
     * attributes for this <code>Validator</code>.  If no attributes are
     * supported, an empty <code>Iterator</code> is returned.</p>
     */
    public Iterator getAttributeNames() {

        return (Collections.EMPTY_LIST.iterator());

    }


    /**
     * <p>Perform the correctness checks implemented by this
     * <code>Validator</code> against the specified {@link UIComponent}.
     * Add {@link Message}s describing any correctness violations to the
     * {@link MessageList} associated with the specified {@link FacesContext}.
     * </p>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent we are checking for correctness
     */
    public void validate(FacesContext context, UIComponent component) {

        Object value = component.getValue();
        if (value == null) {
            context.getMessageList().add(FAILED_MESSAGE_ID,
                                         component.getCompoundId());
        }

    }


}
