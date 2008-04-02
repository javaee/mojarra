/*
 * $Id: TestComponentType.java,v 1.11 2006/03/29 22:39:46 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
 * @version $Id: TestComponentType.java,v 1.11 2006/03/29 22:39:46 rlubke Exp $
 */

public class TestComponentType extends TestCase // ServletTestCase
{


    // ------------------------------------------------------------ Constructors


    public TestComponentType() {

        super("TestComponentType.java");

    }


    public TestComponentType(String name) {

        super(name);

    }


    // ---------------------------------------------------------- Public Methods
    

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
