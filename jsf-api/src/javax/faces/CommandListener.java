/*
 * $Id: CommandListener.java,v 1.5 2002/03/08 00:22:07 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.EventListener;

/**
 * The listener interface for handling command events.
 * An object should implement this interface if it needs
 * to respond to a command event.
 *
 * @see UICommand#addCommandListener
 * @see UITextEntry#addCommandListener
 */
public interface CommandListener extends EventListener {
    /**
     * Invoked when a command event occurs.
     * @param event the CommandEvent object describing the
     *          command event
     * @param nh NavigationHandler object which should be configured by
     *           the command listener to describe the outcome and
     *           possible navigational result of executing the command
     */
    void doCommand(CommandEvent event, NavigationHandler nh) throws
            CommandFailedException;
    
    /**
     * Returns whether or not the form hierarchy of the component
     * which generated the associated command event should be
     * validated before <code>doCommand</code> is invoked on this
     * listener.
     * @see Validator
     * @see Validatible
     * @return boolean value indicating whether or not the form should
     *         be validated before the command is invoked
     */
    boolean requiresValidation(CommandEvent event);
}
