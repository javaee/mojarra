/*
 * $Id: TextModel.java,v 1.1 2001/12/02 01:23:38 edburns Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
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
 * @version $Id: TextModel.java,v 1.1 2001/12/02 01:23:38 edburns Exp $
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
