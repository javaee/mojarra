/*
 * $Id: CommandListener.java,v 1.3 2002/01/10 22:32:21 edburns Exp $
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
 */
public interface CommandListener extends EventListener {
    /**
     * Invoked when a command event occurs.
     * @param event the CommandEvent object describing the
     *          command event
     * @throws CommandFailedException if the command could not
     *         be performed successfully
     */
    public void doCommand(CommandEvent event) throws CommandFailedException;
}
