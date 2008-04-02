/*
 * $Id: TextModel.java,v 1.5 2004/02/04 23:45:05 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package fruitstand;

// TextModel.java

import com.sun.faces.util.Util;



/**
 *
 *  <B>TextModel</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextModel.java,v 1.5 2004/02/04 23:45:05 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TextModel extends Object
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    private String text;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public TextModel()
{
    super();
    // Util.parameterNonNull();
    this.init();
}

protected void init()
{
    // super.init();
}

//
// Class methods
//

//
// General Methods
//

public void setText(String newText) {
    text = newText;
}

public String getText() {
    return text;
}

} // end of class TextModel
