/*
 * $Id: CommandListener.java,v 1.4 2002/01/25 18:35:06 visvan Exp $
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
}
