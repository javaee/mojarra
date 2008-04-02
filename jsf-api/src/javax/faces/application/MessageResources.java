/*
 * $Id: MessageResources.java,v 1.1 2003/07/16 23:11:00 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;


import javax.faces.context.FacesContext;


/**
 * <p><strong>MessageResources</strong> represents a collection of message
 * templates, uniquely identified by message identifiers, that can be used
 * to construct localized {@link Message} instances to be added to the
 * message list in a {@link FacesContext}.</p>
 *
 * <p>An instance of <code>MessageResources</code> is created by calling the
 * <code>getMessageResources()</code> method of
 * {@link javax.faces.application.Application}, for a specified message
 * resources identifier.  Because this instance is shared across multiple
 * requests, it must be implemented in a thread-safe manner.</p>
 *
 * <p>The various <code>getMessage()</code> methods create a localized
 * message by consulting the <code>locale</code> property of the
 * {@link FacesContext} instance that is passed as a parameter, and using
 * that to construct (if necessary) and return a {@link Message} instance
 * whose <code>detail</code> and <code>summary</code> properties have been
 * localized for the specified locale.  If no localized text for the specified
 * locale is available, fallback text in a default language may be returned
 * instead.  If a <code>getMessage()</code> variant that includes
 * substitution parameters, the summary and detail localized text Strings are
 * used as a pattern string for <code>java.text.MessageResources()</code>,
 * whose <code>format(String,Object[])</code> method is used to perform the
 * actual substitution.</p>
 */

public abstract class MessageResources {

    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>Message resources identifier for a {@link MessageResources} instance
     * containing messages whose message identifiers are defined in the
     * JavaServer Faces specification.</p>
     */
    public static final String FACES_API_MESSAGES =
        "javax.faces.context.FACES_API_MESSAGES";


    /**
     * <p>Message resources identifier for a {@link MessageResources} instance
     * containing messages whose message identifiers are defined by the
     * JavaServer Faces implementation being used.</p>
     */
    public static final String FACES_IMPL_MESSAGES =
        "javax.faces.context.FACES_IMPL_MESSAGES";

    


    /**
     * <p>Return a localized {@link Message} instance corresponding to the
     * specified parameters, or <code>null</code> if the specified message
     * identifier is not supported by the {@link MessageResources}
     * instance.</p>
     *
     * @param context The {@link FacesContext} associated with the request
     *  being processed
     * @param messageId Message identifier of the requested message
     *
     * @exception NullPointerException if <code>context</code> or
     *  <code>messageId</code> is <code>null</code>
     */
    public abstract Message getMessage(FacesContext context, String messageId);


    /**
     * <p>Return a localized {@link Message} instance corresponding to the
     * specified parameters, or <code>null</code> if the specified message
     * identifier is not supported by the {@link MessageResources}
     * instance.</p>
     *
     * @param context The {@link FacesContext} associated with the request
     *  being processed
     * @param messageId Message identifier of the requested message
     * @param params Substitution parameters for this message
     *
     * @exception NullPointerException if <code>context</code> or
     *  <code>messageId</code> is <code>null</code>
     */
    public abstract Message getMessage(FacesContext context, String messageId,
                                       Object params[]);


    /**
     * <p>Return a localized {@link Message} instance corresponding to the
     * specified parameters, or <code>null</code> if the specified message
     * identifier is not supported by the {@link MessageResources}
     * instance.</p>
     *
     * @param context The {@link FacesContext} associated with the request
     *  being processed
     * @param messageId Message identifier of the requested message
     * @param param0 First ubstitution parameter for this message
     *
     * @exception NullPointerException if <code>context</code> or
     *  <code>messageId</code> is <code>null</code>
     */
    public abstract Message getMessage(FacesContext context, String messageId,
                                       Object param0);


    /**
     * <p>Return a localized {@link Message} instance corresponding to the
     * specified parameters, or <code>null</code> if the specified message
     * identifier is not supported by the {@link MessageResources}
     * instance.</p>
     *
     * @param context The {@link FacesContext} associated with the request
     *  being processed
     * @param messageId Message identifier of the requested message
     * @param param0 First ubstitution parameter for this message
     * @param param1 Second substitution parameter for this message
     *
     * @exception NullPointerException if <code>context</code> or
     *  <code>messageId</code> is <code>null</code>
     */
    public abstract Message getMessage(FacesContext context, String messageId,
                                       Object param0, Object param1);


    /**
     * <p>Return a localized {@link Message} instance corresponding to the
     * specified parameters, or <code>null</code> if the specified message
     * identifier is not supported by the {@link MessageResources}
     * instance.</p>
     *
     * @param context The {@link FacesContext} associated with the request
     *  being processed
     * @param messageId Message identifier of the requested message
     * @param param0 First ubstitution parameter for this message
     * @param param1 Second substitution parameter for this message
     * @param param2 Third substitution parameter for this message
     *
     * @exception NullPointerException if <code>context</code> or
     *  <code>messageId</code> is <code>null</code>
     */
    public abstract Message getMessage(FacesContext context, String messageId,
                                       Object param0, Object param1,
                                       Object param2);


    /**
     * <p>Return a localized {@link Message} instance corresponding to the
     * specified parameters, or <code>null</code> if the specified message
     * identifier is not supported by the {@link MessageResources}
     * instance.</p>
     *
     * @param context The {@link FacesContext} associated with the request
     *  being processed
     * @param messageId Message identifier of the requested message
     * @param param0 First ubstitution parameter for this message
     * @param param1 Second substitution parameter for this message
     * @param param2 Third substitution parameter for this message
     * @param param3 Fourth substitution parameter for this message
     *
     * @exception NullPointerException if <code>context</code> or
     *  <code>messageId</code> is <code>null</code>
     */
    public abstract Message getMessage(FacesContext context, String messageId,
                                       Object param0, Object param1,
                                       Object param2, Object param3);


}
