/*
 * $Id: TestUtil_messages.java,v 1.41 2005/05/18 17:35:20 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestUtil_messages.java

package com.sun.faces.util;

import java.util.Locale;

import javax.faces.component.UIViewRoot;

import com.sun.faces.ServletFacesTestCase;


/**
 * <B>TestUtil_messages.java</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUtil_messages.java,v 1.41 2005/05/18 17:35:20 jayashri Exp $
 */

public class TestUtil_messages extends ServletFacesTestCase {

//
// Protected Constants
//

// Class Variables
//

//
// Instance Variables
//


// README - Add message info to this array as {message key, "number of params"}
// If message has no parameters entry should be {message key, "0"}

    public String[][] messageInfo = {
        {Util.CONVERSION_ERROR_MESSAGE_ID, "2"},
        {Util.MODEL_UPDATE_ERROR_MESSAGE_ID, "2"},
        {Util.FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_COMPONENT_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_RESPONSE_VIEW_ERROR_MESSAGE_ID, "0"},
        {Util.REQUEST_VIEW_ALREADY_SET_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_MESSAGE_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_PARAMETERS_ERROR_MESSAGE_ID, "0"},
        {Util.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.NULL_RESPONSE_STREAM_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_RESPONSE_WRITER_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_EVENT_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_HANDLER_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_CONTEXT_ERROR_MESSAGE_ID, "0"},
        {Util.NULL_LOCALE_ERROR_MESSAGE_ID, "0"},
        {Util.SUPPORTS_COMPONENT_ERROR_MESSAGE_ID, "1"},
        {Util.MISSING_RESOURCE_ERROR_MESSAGE_ID, "0"},
        {Util.MISSING_CLASS_ERROR_MESSAGE_ID, "1"},
        {Util.COMPONENT_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.LIFECYCLE_ID_ALREADY_ADDED_ID, "1"},
        {Util.LIFECYCLE_ID_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.PHASE_ID_OUT_OF_BOUNDS_ERROR_MESSAGE_ID, "1"},
        {Util.CANT_CREATE_LIFECYCLE_ERROR_MESSAGE_ID, "1"},
        {Util.ILLEGAL_MODEL_REFERENCE_ID, "1"},
        {Util.ATTRIBUTE_NOT_SUPORTED_ERROR_MESSAGE_ID, "2"},
        {Util.FILE_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, "1"},
        {Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, "1"},
        {Util.ILLEGAL_CHARACTERS_ERROR_MESSAGE_ID, "0"},
        {Util.NOT_NESTED_IN_FACES_TAG_ERROR_MESSAGE_ID, "1"},
        {Util.NULL_BODY_CONTENT_ERROR_MESSAGE_ID, "1"},
        {Util.SAVING_STATE_ERROR_MESSAGE_ID, "0"},
        {Util.RENDERER_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.MAXIMUM_EVENTS_REACHED_ERROR_MESSAGE_ID, "1"},
        {Util.NULL_CONFIGURATION_ERROR_MESSAGE_ID, "0"},
        {Util.ERROR_OPENING_FILE_ERROR_MESSAGE_ID, "1"},
        {Util.ERROR_REGISTERING_DTD_ERROR_MESSAGE_ID, "1"},
        {Util.INVALID_INIT_PARAM_ERROR_MESSAGE_ID, "0"},
        {Util.ERROR_SETTING_BEAN_PROPERTY_ERROR_MESSAGE_ID, "1"},
        {Util.ERROR_GETTING_VALUE_BINDING_ERROR_MESSAGE_ID, "1"},
        {Util.ERROR_GETTING_VALUEREF_VALUE_ERROR_MESSAGE_ID, "1"},
        {Util.CANT_INTROSPECT_CLASS_ERROR_MESSAGE_ID, "1"},
        {Util.CANT_CONVERT_VALUE_ERROR_MESSAGE_ID, "2"},
        {Util.INVALID_SCOPE_LIFESPAN_ERROR_MESSAGE_ID, "1"},
        {Util.CONVERTER_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.VALIDATOR_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {Util.ENCODING_ERROR_MESSAGE_ID, "0"},
        {Util.ILLEGAL_IDENTIFIER_LVALUE_MODE_ID, "1"},
        {Util.VALIDATION_ID_ERROR_ID, "1"},
        {Util.VALIDATION_EL_ERROR_ID, "1"},
        {Util.VALIDATION_COMMAND_ERROR_ID, "1"},
        {Util.CONTENT_TYPE_ERROR_MESSAGE_ID, "0"},
        {Util.COMPONENT_NOT_FOUND_IN_VIEW_WARNING_ID, "1"},
        {Util.ILLEGAL_ATTEMPT_SETTING_VIEWHANDLER_ID, "0"},
        {Util.ILLEGAL_ATTEMPT_SETTING_STATEMANAGER_ID, "0"},
        {Util.INVALID_MESSAGE_SEVERITY_IN_CONFIG_ID, "1"},
        {Util.CANT_CLOSE_INPUT_STREAM_ID, "0"},
        {Util.DUPLICATE_COMPONENT_ID_ERROR_ID, "1"},
        {Util.FACES_SERVLET_MAPPING_CANNOT_BE_DETERMINED_ID, "1"},
        {Util.ILLEGAL_VIEW_ID_ID, "1"},
        {Util.INVALID_EXPRESSION_ID, "1"},
        {Util.NULL_FORVALUE_ID, "1"},
        {Util.EMPTY_PARAMETER_ID, "0"},
        {Util.ASSERTION_FAILED_ID, "0"},
        {Util.OBJECT_CREATION_ERROR_ID, "0"},
        {Util.CYCLIC_REFERENCE_ERROR_ID, "1"},
        {Util.NO_DTD_FOUND_ERROR_ID, "2"},
        {Util.MANAGED_BEAN_CANNOT_SET_LIST_ARRAY_PROPERTY_ID, "2"},
        {Util.MANAGED_BEAN_EXISTING_VALUE_NOT_LIST_ID, "2"},
        {Util.MANAGED_BEAN_CANNOT_SET_MAP_PROPERTY_ID, "2"},
        {Util.MANAGED_BEAN_TYPE_CONVERSION_ERROR_ID, "4"},
        {Util.APPLICATION_ASSOCIATE_CTOR_WRONG_CALLSTACK_ID, "0"},
        {Util.APPLICATION_ASSOCIATE_EXISTS_ID, "0"},
        {Util.OBJECT_IS_READONLY, "1"},
        {Util.INCORRECT_JSP_VERSION_ID, "1"},
        {Util.EL_OUT_OF_BOUNDS_ERROR_ID, "1"},
        {Util.EL_PROPERTY_TYPE_ERROR_ID, "1"},
        {Util.EL_SIZE_OUT_OF_BOUNDS_ERROR_ID,"2"} 
    };

    private String[][] toolsMessageInfo = {
        {ToolsUtil.MANAGED_BEAN_AS_LIST_CONFIG_ERROR_ID, "1"},
        {ToolsUtil.MANAGED_BEAN_AS_MAP_CONFIG_ERROR_ID, "1"},
        {ToolsUtil.MANAGED_BEAN_NO_MANAGED_BEAN_CLASS_ID, "1"},
        {ToolsUtil.MANAGED_BEAN_NO_MANAGED_BEAN_NAME_ID, "2"},
        {ToolsUtil.MANAGED_BEAN_NO_MANAGED_BEAN_SCOPE_ID, "1"},
        {ToolsUtil.MANAGED_BEAN_INVALID_SCOPE_ID, "2"},
        {ToolsUtil.MANAGED_BEAN_LIST_PROPERTY_CONFIG_ERROR_ID, "2"},
        {ToolsUtil.MANAGED_BEAN_MAP_PROPERTY_CONFIG_ERROR_ID, "2"},
        {ToolsUtil.MANAGED_BEAN_PROPERTY_CONFIG_ERROR_ID, "2"},
    };

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestUtil_messages() {
        super("TestUtil_messages.java");
    }


    public TestUtil_messages(String name) {
        super(name);
    }

    //
    // Methods from TestCase
    
    public void setUp() {
        super.setUp();
        UIViewRoot viewRoot = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        viewRoot.setViewId("viewId");
        getFacesContext().setViewRoot(viewRoot);

    }
    
//
// Class methods
//

//
// General Methods
//


    public void testVerifyMessages() {

        // English Language
        System.out.println("Verifying English Messages...");
        Locale locale = new Locale("en", "US");
        getFacesContext().getViewRoot().setLocale(locale);
        verifyParamsInMessages(messageInfo);

        // French Language
        System.out.println("Verifying French Messages...");
        locale = new Locale("fr", "");
        getFacesContext().getViewRoot().setLocale(locale);
        verifyParamsInMessages(messageInfo);

        // German Language
        System.out.println("Verifying German Messages...");
        locale = new Locale("de", "");
        getFacesContext().getViewRoot().setLocale(locale);
        verifyParamsInMessages(messageInfo);

        // Spanish Language
        System.out.println("Verifying Spanish Messages...");
        locale = new Locale("es", "");
        getFacesContext().getViewRoot().setLocale(locale);
        verifyParamsInMessages(messageInfo);
    }


    public void testVerifyToolsMessages() {
        System.out.println("Verifying ToolsUtil messages");
        verifyParamsInToolsMessages(toolsMessageInfo);
    }

    private void verifyParamsInToolsMessages(String[][] messageInfo) {
        int numParams = 0;

        for (int i = 0; i < messageInfo.length; i++) {
            System.out.println("Testing message: " + messageInfo[i][0]);
            try {
                numParams = Integer.parseInt(messageInfo[i][1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid param number specifier!");
                assertTrue(false);
            }
            if (numParams == 0) {
                String message = ToolsUtil.getMessage(messageInfo[i][0]);
                assertTrue(message != null);
            } else if (numParams > 0) {
                Object[] params = generateParams(numParams);
                String message = ToolsUtil.getMessage(messageInfo[i][0],
                                                      params);
                assertTrue(message != null);
                for (int j = 0; j < params.length; j++) {
                    assertTrue(message.indexOf((String) params[j]) != -1);
                }
            }
        }
    }


    private void verifyParamsInMessages(String[][] messageInfo) {
        int numParams = 0;

        for (int i = 0; i < messageInfo.length; i++) {
            System.out.println("Testing message: " + messageInfo[i][0]);
            try {
                numParams = Integer.parseInt(messageInfo[i][1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid param number specifier!");
                assertTrue(false);
            }
            if (numParams == 0) {
                String message = Util.getExceptionMessageString(messageInfo[i][0]);
                assertTrue(message != null);
            } else if (numParams > 0) {
                Object[] params = generateParams(numParams);
                String message = Util.getExceptionMessageString(messageInfo[i][0],
                                                          params);
                assertTrue(message != null);
                for (int j = 0; j < params.length; j++) {
                    assertTrue(message.indexOf((String) params[j]) != -1);
                }
            }
        }
    }


    private Object[] generateParams(int numParams) {
        Object[] params = new String[numParams];
        for (int i = 0; i < numParams; i++) {
            params[i] = "param_" + i;
        }
        return params;
    }

} // end of class TestUtil_messages
