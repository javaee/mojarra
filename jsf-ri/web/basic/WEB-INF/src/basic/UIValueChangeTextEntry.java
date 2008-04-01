/*
 * $Id: UIValueChangeTextEntry.java,v 1.1 2002/07/23 19:37:30 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// UIValueChangeTextEntry.java

package basic;

import javax.faces.event.FacesEvent;
import javax.faces.context.FacesContext;
import javax.faces.component.UITextEntry;

import java.io.IOException;

/**
 *
 *  <B>UIValueChangeTextEntry</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: UIValueChangeTextEntry.java,v 1.1 2002/07/23 19:37:30 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class UIValueChangeTextEntry extends UITextEntry
{
//
// Protected Constants
//

    public static final String TYPE = "basic.UIValueChangeTextEntry";



//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public UIValueChangeTextEntry()
{
    super();
}

//
// Class methods
//

//
// General Methods
//

//
// Methods from UITextEntry
//

    public String getComponentType() {

        return (TYPE);

    }

    public void decode(FacesContext context) throws IOException {
        if (context == null) {
            throw new NullPointerException();
        }
	FacesEvent valueChangeEvent = null;
	String newText, curText = this.getText();
	super.decode(context);
	newText = this.getText();

	// If the new text is different than the old text
	if ((curText != null && newText == null) ||
	    (curText == null && newText != null) ||
	    ((curText != null && !curText.equals(newText))) ||
	    ((newText != null && !newText.equals(curText)))) {
	    valueChangeEvent = new FacesEvent(this);
	    context.addRequestEvent(this, valueChangeEvent);
	}
    }



} // end of class UIValueChangeTextEntry
