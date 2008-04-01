/*
 * $Id: MessageResources.java,v 1.1 2002/06/14 00:00:50 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


/**
 * <p><strong>MessageResources</strong> represents a collection of message
 * templates, uniquely identified by message identifiers, that can be used
 * to construct localized {@link Message} instances to be added to the
 * message list in a {@link FacesContext}.</p>
 *
 * <p>An instance of <code>MessageResources</code> is created by calling the
 * <code>getMessageResources()</code> method of
 * {@link MessageResourcesFactory}, for a specified message resources
 * identifier.  Subsequent calls to <code>getMessageResources()</code>
 * will return the same instance (i.e. it is a per-web-application
 * Singleton).  Because this instance is shared across multiple requests,
 * it must be implemented in a thread-safe manner.</p>
 */

public abstract class MessageResources {


    /**
     * <p>Return a localized {@link Message} instance corresponding to the
     * specified parameters, or <code>null</code> if the specified message
     * identifier is not supported by the <code>MessageResources</code>
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
     * identifier is not supported by the <code>MessageResources</code>
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
     * identifier is not supported by the <code>MessageResources</code>
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
     * identifier is not supported by the <code>MessageResources</code>
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
     * identifier is not supported by the <code>MessageResources</code>
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
     * identifier is not supported by the <code>MessageResources</code>
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
