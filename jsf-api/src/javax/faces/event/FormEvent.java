/*
 * $Id: FormEvent.java,v 1.4 2002/07/31 00:48:13 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;


/**
 * <p><strong>FormEvent</strong> is a subclass of {@link FacesEvent} that
 * indicates that a particular {@link UIForm} was submitted by the user.
 * It is queued to the application, for processing during the
 * <em>Invoke Application</em> phase of the request processing lifecycle.</p>
 */

public class FormEvent extends CommandEvent {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new event object from the specified source component.</p>
     *
     * @param component Source {@link UIComponent} for this event
     * @param formName Form name of the form this event signifies
     * @param commandName Command name of the submit button that caused this
     *  form to be submitted (if any)
     *
     * @exception NullPointerException if any of the parameters
     *  are <code>null</code>
     */
    public FormEvent(UIComponent source, String formName, String commandName) {

        super(source, commandName);
        if (formName == null) {
            throw new NullPointerException();
        }
        this.formName = formName;

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>The form name whose submitted this event signifies.</p>
     */
    private String formName = null;


    /**
     * <p>Return the form name of the {@link UIForm} whose submission
     * this event signifies.</p>
     */
    public String getFormName() {

        return (formName);

    }


}
