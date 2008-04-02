/*
 * $Id: ValidatorMessages.java,v 1.2 2003/10/20 15:02:41 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import javax.faces.application.Application;
import javax.faces.application.Message;
import javax.faces.application.MessageResources;
import javax.faces.context.FacesContext;


/**
 * <p>Package private class for shared utility methods.</p>
 */

final class ValidatorMessages {

    /**
     * <p>The message identifier of the {@link Message} to be created if
     * the maximum or minimum value check fails, and both the maximum
     * and minim values for this validator have been set.  The message
     * format string for this message may optionally include a
     * <code>{0}</code> placeholder, which will be replaced by the
     * configured minimum value, and a <code>{1}</code> placeholder,
     * which will be replaced by the configured maximum value.</p>
     */
    public static final String NOT_IN_RANGE_MESSAGE_ID =
        "javax.faces.validator.NOT_IN_RANGE";

    /**
     * <p>Return a {@link Message} for the specified parameters.</p>
     *
     * @param context The {@link FacesContext} associated with the request
     *  being processed
     * @param messageId Message identifier of the requested message
     */
    static Message getMessage(FacesContext context, String messageId) {

        return (getMessageResources().getMessage(context, messageId));

    }


    /**
     * <p>Return a {@link Message} for the specified parameters.</p>
     *
     * @param context The {@link FacesContext} associated with the request
     *  being processed
     * @param messageId Message identifier of the requested message
     * @param params Substitution parameters for this message
     */
    static Message getMessage(FacesContext context, String messageId,
                                 Object params[]) {

        return (getMessageResources().getMessage(context, messageId, params));

    }


    /**
     * <p>Return the {@link MessageResources} instance for the message
     * resources defined by the JavaServer Faces Specification.
     */
    static synchronized MessageResources getMessageResources() {

        return (Application.getCurrentInstance().getMessageResources
                (MessageResources.FACES_API_MESSAGES));

    }


}
