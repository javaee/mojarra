/*
 * $Id: TestValidation.java,v 1.2 2002/04/05 19:41:21 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestValidation.java

package com.sun.faces;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;

import javax.faces.ObjectManager;
import javax.faces.UITextEntry;
import javax.faces.ObjectAccessor;
import javax.faces.FormatValidator;
import javax.faces.RangeValidator;
import javax.faces.LengthValidator;
import javax.faces.RequiredValidator;
import javax.faces.Validatible;
import com.sun.faces.util.Util;

/**
 *
 *  <B>TestValidation</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestValidation.java,v 1.2 2002/04/05 19:41:21 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestValidation extends FacesTestCase
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

    public TestValidation() {super("TestValidation");}
    public TestValidation(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

public void testValidators() 
{
    int state = 13;

    UITextEntry input = new UITextEntry();
    input.setId("source");

    // Test BooleanConverter
    System.out.println("Testing BooleanConverter");
    input.setValue("test");
    input.setAttribute("modelType", "java.lang.Boolean");
    input.doValidate(facesContext);
    state = input.getValidState();
    assertTrue(state == Validatible.VALID);
 
    // Test DateConverter
    System.out.println("Testing DateConverter");
    input.setValue("06/27/1990");
    input.setAttribute("modelType", "java.util.Date");
    input.doValidate(facesContext);
    state = input.getValidState();
    assertTrue(state == Validatible.VALID);
    

    System.out.println("Testing DateConverter");
    input.setValue("test");
    input.setAttribute("modelType", "java.util.Date");
    input.doValidate(facesContext);
    state = input.getValidState();
    assertTrue(state == Validatible.INVALID);

    // Test RequiredValidator.
    System.out.println("Testing RequiredValidator");
    input.setValue(null);
    RequiredValidator reqValidator = new RequiredValidator();
    String reqValidatorId = Util.generateId();
    objectManager.put( session, reqValidatorId, reqValidator);
    input.addValidator(reqValidatorId);
    input.doValidate(facesContext);
    state = input.getValidState();
    assertTrue(state == Validatible.INVALID);

    // Test IntegerConverter
    System.out.println("Testing IntegerConverter");
    input.setValue("test");
    input.setAttribute("modelType", "java.lang.Integer");
    input.doValidate(facesContext);
    state = input.getValidState();
    assertTrue(state == Validatible.INVALID);

    // Test IntegerConverter 
    System.out.println("Testing IntegerConverter");
    input.setValue("999");
    input.setAttribute("modelType", "java.lang.Integer");
    input.doValidate(facesContext);
    state = input.getValidState();
    assertTrue(state == Validatible.VALID);

    // Test RangeValidator
    System.out.println("Testing RangeValidator");
    input.setValue("999");
    input.setAttribute("rangeMinimum", "1");
    input.setAttribute("rangeMaximum", "1000");
    RangeValidator rangeValidator = new RangeValidator();
    String rangeValidatorId = Util.generateId();
    objectManager.put( session, rangeValidatorId, rangeValidator);
    input.addValidator(rangeValidatorId);
    input.doValidate(facesContext);
    state = input.getValidState();
    assertTrue(state == Validatible.VALID);

    // Test RangeValidator
    System.out.println("Testing RangeValidator");
    input.setValue("1001");
    input.doValidate(facesContext);
    state = input.getValidState();
    assertTrue(state == Validatible.INVALID);

    input.removeValidator(rangeValidatorId);

    // Test LengthValidator.
    System.out.println("Testing LengthValidator");
    input.setAttribute("modelType", "java.lang.String");
    input.setValue("Test LengthValidation");
    input.setAttribute("lengthMinimum", "1");
    input.setAttribute("lengthMaximum", "10");
    LengthValidator lengthValidator = new LengthValidator();
    String lengthValidatorId = Util.generateId();
    objectManager.put( session, lengthValidatorId, lengthValidator);
    input.addValidator(lengthValidatorId);
    input.doValidate(facesContext);
    state = input.getValidState();
    assertTrue(state == Validatible.INVALID);

    // Test LengthValidator
    System.out.println("Testing LengthValidator");
    input.setValue("Test");
    input.doValidate(facesContext);
    state = input.getValidState();
    assertTrue(state == Validatible.VALID);
}

} // end of class TestValidation.
