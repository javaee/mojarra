/*
 * $Id: TextModel.java,v 1.2 2001/12/20 22:26:45 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package fruitstand;

// TextModel.java

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>TextModel</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextModel.java,v 1.2 2001/12/20 22:26:45 ofung Exp $
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
    // ParameterCheck.nonNull();
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
