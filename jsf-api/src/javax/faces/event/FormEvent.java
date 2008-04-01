/*
 * $Id: FormEvent.java,v 1.3 2002/07/26 19:02:37 craigmcc Exp $
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

public class FormEvent extends FacesEvent {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new event object from the specified source component.</p>
     *
     * @param component Source {@link UIComponent} for this event (if any)
     * @param formName Form name of the form this event signifies
     *
     * @exception NullPointerException if any of the parameters
     *  are <code>null</code>
     */
    public FormEvent(UIComponent source, String formName) {

        this(source, formName, null);

    }


    /**
     * <p>Construct a new event object from the specified source component.</p>
     *
     * @param component Source {@link UIComponent} for this event (if any)
     * @param formName Form name of the form this event signifies
     * @param commandName Command name of the submit button that caused this
     *  form to be submitted (if any)
     *
     * @exception NullPointerException if <code>source</code> or
     *  <code>formName</code> is <code>null</code>
     */
    public FormEvent(UIComponent source, String formName, String commandName) {

        super(source);
        this.formName = formName;
        this.commandName = commandName;

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>The command name of the submit button that caused this form to be
     * submitted (if any).</p>
     */
    private String commandName = null;


    /**
     * <p>Return the command name of the {@link UICommand} that caused this
     * form to be submitted, if any.</p>
     */
    public String getCommandName() {

        return (commandName);

    }


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
