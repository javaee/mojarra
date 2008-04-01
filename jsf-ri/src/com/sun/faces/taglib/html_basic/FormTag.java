/*
 * $Id: FormTag.java,v 1.2 2001/11/07 00:18:34 rogerk Exp $
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

// FormTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>FormTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FormTag.java,v 1.2 2001/11/07 00:18:34 rogerk Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class FormTag extends TagSupport
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

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public FormTag()
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

// ----VERTIGO_TEST_START

//
// Test methods
//

public static void main(String [] args)
{
    Assert.setEnabled(true);
    FormTag me = new FormTag();
    Log.setApplicationName("FormTag");
    Log.setApplicationVersion("0.0");
    Log.setApplicationVersionDate("$Id: FormTag.java,v 1.2 2001/11/07 00:18:34 rogerk Exp $");
    
}

// ----VERTIGO_TEST_END

} // end of class FormTag
