/*
 * $Id: Validator.java,v 1.1 2002/05/17 01:27:27 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.MessageList;


/**
 * <p>A <strong>Validator</strong> is a class that can perform validation
 * (correctness checks) on a {@link UIComponent}.  Zero or more
 * <code>Validator</code>s can be associated with each {@link UIComponent}
 * in the request tree, and are called during the <em>Process
 * Validations Phase</em>.</p>
 *
 * <p>Individual <code>Validator</code>s should examine the component that
 * they are passed, and add {@link Message} instances to the
 * {@link MessageList} associated with the specified {@link FacesContext}
 * for any failures to conform to the required rules.  In general, such
 * messages should be configured with the <code>compoundId</code> of the
 * specified component as the <code>reference</code> property of the message.
 * </p>
 */

public interface Validator {


    /**
     * <p>Return 


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
    public void validate(FacesContext context, UIComponent component);


}
