/*
 * $Id: FormEvent.java,v 1.1 2002/05/17 22:57:12 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


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
     */
    public FormEvent(UIComponent source, String formName) {

        super(source);
        this.formName = formName;

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>The form namd whose submitted this event signifies.</p>
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
