/*
 * $Id: UIValueChangeTextEntry.java,v 1.4 2002/10/07 20:39:54 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// UIValueChangeTextEntry.java

package basic;

import javax.faces.event.RequestEvent;
import javax.faces.context.FacesContext;
import javax.faces.component.UIInput;

import java.io.IOException;

/**
 *
 *  <B>UIValueChangeTextEntry</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: UIValueChangeTextEntry.java,v 1.4 2002/10/07 20:39:54 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class UIValueChangeTextEntry extends UIInput
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

    public boolean decode(FacesContext context) throws IOException {
        if (context == null) {
            throw new NullPointerException();
        }
	RequestEvent valueChangeEvent = null;
	String newText, curText = (String) this.getValue();
	super.decode(context);
	newText = (String) this.getValue();

	// If the new text is different than the old text
	if ((curText != null && newText == null) ||
	    (curText == null && newText != null) ||
	    ((curText != null && !curText.equals(newText))) ||
	    ((newText != null && !newText.equals(curText)))) {
	    valueChangeEvent = new RequestEvent(this);
	    context.addRequestEvent(this, valueChangeEvent);
	}
        return true;
    }



} // end of class UIValueChangeTextEntry
