/*
 * $Id: MaxMinValidatorTag.java,v 1.1 2002/09/20 00:59:45 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// MaxMinValidatorTag.java

package com.sun.faces.taglib.jsf_core;

import javax.faces.webapp.ValidatorTag;

/**
 *
 *  <B>MaxMinValidatorTag</B> contains ivars for maximumSet and minimumSet.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: MaxMinValidatorTag.java,v 1.1 2002/09/20 00:59:45 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public abstract class MaxMinValidatorTag extends ValidatorTag
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

    /**
     * <p>Flag indicating whether a maximum limit has been set.</p>
     */
    protected boolean maximumSet = false;

    /**
     * <p>Flag indicating whether a minimum limit has been set.</p>
     */
    protected boolean minimumSet = false;


// Relationship Instance Variables

//
// Constructors and Initializers    
//

//
// Class methods
//

//
// General Methods
//

// 
// Methods from ValidatorTag
// 


} // end of class MaxMinValidatorTag
