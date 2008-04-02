/*
 * $Id: TestComponentType.java,v 1.8 2004/02/06 18:57:07 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestComponentType.java

package com.sun.faces.renderkit.html_basic;

import junit.framework.TestCase;
import org.apache.cactus.ServletTestCase;

import javax.faces.component.UIOutput;
import javax.faces.component.UISelectMany;

/**
 * <B>TestComponentType.java</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestComponentType.java,v 1.8 2004/02/06 18:57:07 rlubke Exp $
 * @see	Blah
 * @see	Bloo
 */

public class TestComponentType extends TestCase // ServletTestCase
{

//
// Protected Constants
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

    public TestComponentType() {
        super("TestComponentType.java");
    }


    public TestComponentType(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//

    public void testComponentTypeCheck() {

        MenuRenderer mr = new MenuRenderer();

        // case 1: UISelectMany component
        
        UISelectMany many = new UISelectMany();
        String multipleText = mr.getMultipleText(many);
        assertTrue(multipleText.equals(" multiple "));

        // case 2: UISelectMany subclass component

        MyComponent myC = new MyComponent();
        multipleText = mr.getMultipleText(myC);
        assertTrue(multipleText.equals(" multiple "));

        // case 3: UIOutput component

        UIOutput output = new UIOutput();
        multipleText = mr.getMultipleText(output);
        assertTrue(!multipleText.equals(" multiple "));
        assertTrue(multipleText.equals(""));
    }


    public class MyComponent extends UISelectMany {

    }


} // end of class TestComponentType
