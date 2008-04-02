/*
 * $Id: TestUtil_messages.java,v 1.55 2006/07/31 23:05:00 rlubke Exp $
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

// TestUtil_messages.java

package com.sun.faces.util;

import java.util.Locale;

import javax.faces.component.UIViewRoot;

import com.sun.faces.cactus.ServletFacesTestCase;


/**
 * <B>TestUtil_messages.java</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUtil_messages.java,v 1.55 2006/07/31 23:05:00 rlubke Exp $
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
        {MessageUtils.CONVERSION_ERROR_MESSAGE_ID, "2"},
        {MessageUtils.MODEL_UPDATE_ERROR_MESSAGE_ID, "2"},
        {MessageUtils.FACES_CONTEXT_CONSTRUCTION_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.NULL_COMPONENT_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.NULL_RESPONSE_VIEW_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.REQUEST_VIEW_ALREADY_SET_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.NULL_MESSAGE_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.NAMED_OBJECT_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.NULL_RESPONSE_STREAM_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.NULL_RESPONSE_WRITER_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.NULL_EVENT_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.NULL_HANDLER_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.NULL_CONTEXT_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.NULL_LOCALE_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.SUPPORTS_COMPONENT_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.MISSING_RESOURCE_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.MISSING_CLASS_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.COMPONENT_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.LIFECYCLE_ID_ALREADY_ADDED_ID, "1"},
        {MessageUtils.LIFECYCLE_ID_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.PHASE_ID_OUT_OF_BOUNDS_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.CANT_CREATE_LIFECYCLE_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.ILLEGAL_MODEL_REFERENCE_ID, "1"},
        {MessageUtils.ATTRIBUTE_NOT_SUPORTED_ERROR_MESSAGE_ID, "2"},
        {MessageUtils.FILE_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.CANT_PARSE_FILE_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.ILLEGAL_CHARACTERS_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.NOT_NESTED_IN_FACES_TAG_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.NULL_BODY_CONTENT_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.SAVING_STATE_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.RENDERER_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.MAXIMUM_EVENTS_REACHED_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.NULL_CONFIGURATION_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.ERROR_OPENING_FILE_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.ERROR_REGISTERING_DTD_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.INVALID_INIT_PARAM_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.ERROR_SETTING_BEAN_PROPERTY_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.ERROR_GETTING_VALUE_BINDING_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.ERROR_GETTING_VALUEREF_VALUE_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.CANT_INTROSPECT_CLASS_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.CANT_CONVERT_VALUE_ERROR_MESSAGE_ID, "2"},
        {MessageUtils.INVALID_SCOPE_LIFESPAN_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.CONVERTER_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.VALIDATOR_NOT_FOUND_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.ENCODING_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.ILLEGAL_IDENTIFIER_LVALUE_MODE_ID, "1"},
        {MessageUtils.VALIDATION_ID_ERROR_ID, "1"},
        {MessageUtils.VALIDATION_EL_ERROR_ID, "1"},
        {MessageUtils.VALIDATION_COMMAND_ERROR_ID, "1"},
        {MessageUtils.CONTENT_TYPE_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.COMPONENT_NOT_FOUND_IN_VIEW_WARNING_ID, "1"},
        {MessageUtils.ILLEGAL_ATTEMPT_SETTING_VIEWHANDLER_ID, "0"},
        {MessageUtils.ILLEGAL_ATTEMPT_SETTING_STATEMANAGER_ID, "0"},
        {MessageUtils.INVALID_MESSAGE_SEVERITY_IN_CONFIG_ID, "1"},
        {MessageUtils.CANT_CLOSE_INPUT_STREAM_ID, "0"},
        {MessageUtils.DUPLICATE_COMPONENT_ID_ERROR_ID, "1"},
        {MessageUtils.FACES_SERVLET_MAPPING_CANNOT_BE_DETERMINED_ID, "1"},
        {MessageUtils.ILLEGAL_VIEW_ID_ID, "1"},
        {MessageUtils.INVALID_EXPRESSION_ID, "1"},
        {MessageUtils.NULL_FORVALUE_ID, "1"},
        {MessageUtils.EMPTY_PARAMETER_ID, "0"},
        {MessageUtils.ASSERTION_FAILED_ID, "0"},
        {MessageUtils.OBJECT_CREATION_ERROR_ID, "0"},
        {MessageUtils.CYCLIC_REFERENCE_ERROR_ID, "1"},
        {MessageUtils.NO_DTD_FOUND_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_CANNOT_SET_LIST_ARRAY_PROPERTY_ID, "2"},
        {MessageUtils.MANAGED_BEAN_EXISTING_VALUE_NOT_LIST_ID, "2"},
        {MessageUtils.MANAGED_BEAN_CANNOT_SET_MAP_PROPERTY_ID, "2"},
        {MessageUtils.MANAGED_BEAN_TYPE_CONVERSION_ERROR_ID, "4"},
        {MessageUtils.APPLICATION_ASSOCIATE_CTOR_WRONG_CALLSTACK_ID, "0"},
        {MessageUtils.APPLICATION_ASSOCIATE_EXISTS_ID, "0"},
        {MessageUtils.OBJECT_IS_READONLY, "1"},
        {MessageUtils.INCORRECT_JSP_VERSION_ID, "1"},
        {MessageUtils.EL_OUT_OF_BOUNDS_ERROR_ID, "1"},
        {MessageUtils.EL_PROPERTY_TYPE_ERROR_ID, "1"},
        {MessageUtils.EL_SIZE_OUT_OF_BOUNDS_ERROR_ID,"2"},
        {MessageUtils.EVAL_ATTR_UNEXPECTED_TYPE, "3"},
        {MessageUtils.RESTORE_VIEW_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.VALUE_NOT_SELECT_ITEM_ID, "2"},
        {MessageUtils.CHILD_NOT_OF_EXPECTED_TYPE_ID, "3"},
        {MessageUtils.COMMAND_LINK_NO_FORM_MESSAGE_ID, "0"},
        {MessageUtils.FACES_CONTEXT_NOT_FOUND_ID, "0"},
        {MessageUtils.NO_IMPLEMENTATION_CONFIG_FILE_FOUND_MESSAGE_ID, "1"},
        {MessageUtils.NOT_NESTED_IN_TYPE_TAG_ERROR_MESSAGE_ID, "2"},
        {MessageUtils.CANT_WRITE_ID_ATTRIBUTE_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.NOT_NESTED_IN_UICOMPONENT_TAG_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.NO_COMPONENT_ASSOCIATED_WITH_UICOMPONENT_TAG_MESSAGE_ID, "0"},
        {MessageUtils.FACES_SERVLET_MAPPING_INCORRECT_ID, "0"}
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
                String message = MessageUtils.getExceptionMessageString(messageInfo[i][0]);
                assertTrue(message != null);
            } else if (numParams > 0) {
                Object[] params = generateParams(numParams);
                String message = MessageUtils.getExceptionMessageString(messageInfo[i][0],
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
