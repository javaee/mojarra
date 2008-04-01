/*
 * $Id: FormListener.java,v 1.4 2002/01/25 18:35:07 visvan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.EventListener;

/**
 * The listener interface for handling form events.
 * An object should implement this interface if it needs
 * to respond to lifecycle events on a UIForm component.
 *
 * @see UIForm#addFormListener
 */
public interface FormListener extends EventListener {
    /**
     * Invoked when a form INIT event occurs.
     * @param event the FormEvent object describing the form event
     */
    void formInitialized(FormEvent event);

   /**
     * Invoked when a form CONTROL_EXITED event occurs.
     * @param event the FormEvent object describing the form event
     */
    void formExited(FormEvent event);
}
