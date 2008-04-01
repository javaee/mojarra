/*
 * $Id: MessageList.java,v 1.1 2002/05/16 18:28:48 craigmcc Exp $
 * @author Gary Karasiuk <karasiuk@ca.ibm.com>
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


import java.util.Iterator;


/**
 * <p><strong>MessageList</strong> is a collection of {@link Message} objects
 * representing validation (and other) errors that have been accumulated during
 * the processing of the current request.  A <code>MessageList</code>
 * instance is made available via the <code>getMessageList()</code> method
 * of {@link FacesContext}.</p>
 */

public abstract class MessageList {


    /**
     * <p>Create and add a new {@link Message}, not associated with a
     * particular component, to this <code>MessageList</code>.</p>
     *
     * @param messageId Message identifier of the new message
     */
    public abstract void add(String messageId);


    /**
     * <p>Create and add a new {@link Message}, not associated with a
     * particular component, to this <code>MessageList</code>.</p>
     *
     * @param messageId Message identifier of the new message
     * @param params Substitution parameters for this message
     */
    public abstract void add(String messageId, Object params[]);


    /**
     * <p>Create and add a new {@link Message} to this
     * <code>MessageList</code>.</p>
     *
     * @param messageId Message identifier of the new message
     * @param reference Reference to the component this message
     *  is to be associated with
     */
    public abstract void add(String messageId, String reference);


    /**
     * <p>Create and add a new {@link Message} to this
     * <code>MessageList</code>.</p>
     *
     * @param messageId Message identifier of the new message
     * @param reference Reference to the component this message
     *  is to be associated with
     * @param params Substitution parameters for this message
     */
    public abstract void add(String messageId, String reference,
                             Object params[]);


    /**
     * <p>Return an iterator over all of the {@link Message}s in this
     * <code>MessageList</code>, whether or not they are associated with
     * a particular component.  If there are no such {@link Message}s,
     * an empty Iterator is returned.</p>
     */
    public abstract Iterator iterator();


    /**
     * <p>Return an iterator over all of the {@link Message}s in this
     * <code>MessageList</code> that are associated with the specified
     * <code>reference</code>.  If there are no such {@link Message}s,
     * an empty Iterator is returned.</p>
     *
     * @param reference Reference to the component for which messages
     *  should be returned, or <code>null</code> to select messages that
     *  are not associated with a specific component
     */
    public abstract Iterator iterator(String reference);


}
