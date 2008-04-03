/*
 * $Id: TestUtil_messages.java,v 1.63 2007/04/27 22:02:11 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// TestUtil_messages.java

package com.sun.faces.util;

import javax.faces.component.UIViewRoot;

import java.util.Locale;

import com.sun.faces.cactus.ServletFacesTestCase;


/**
 * <B>TestUtil_messages.java</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUtil_messages.java,v 1.63 2007/04/27 22:02:11 ofung Exp $
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
        {MessageUtils.INVALID_SCOPE_LIFESPAN_ERROR_MESSAGE_ID, "4"},
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
        {MessageUtils.CYCLIC_REFERENCE_ERROR_ID, "2"},
        {MessageUtils.NO_DTD_FOUND_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_CANNOT_SET_LIST_ARRAY_PROPERTY_ID, "2"},
        {MessageUtils.MANAGED_BEAN_EXISTING_VALUE_NOT_LIST_ID, "2"},        
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
        {MessageUtils.CHILD_NOT_OF_EXPECTED_TYPE_ID, "4"},
        {MessageUtils.COMMAND_LINK_NO_FORM_MESSAGE_ID, "0"},
        {MessageUtils.FACES_CONTEXT_NOT_FOUND_ID, "0"},
        {MessageUtils.NOT_NESTED_IN_TYPE_TAG_ERROR_MESSAGE_ID, "2"},
        {MessageUtils.CANT_WRITE_ID_ATTRIBUTE_ERROR_MESSAGE_ID, "1"},
        {MessageUtils.NOT_NESTED_IN_UICOMPONENT_TAG_ERROR_MESSAGE_ID, "0"},
        {MessageUtils.NO_COMPONENT_ASSOCIATED_WITH_UICOMPONENT_TAG_MESSAGE_ID, "0"},
        {MessageUtils.FACES_SERVLET_MAPPING_INCORRECT_ID, "0"},
        {MessageUtils.JS_RESOURCE_WRITING_ERROR_ID, "0"},
        {MessageUtils.CANNOT_CONVERT_ID, "2"},
        {MessageUtils.CANNOT_VALIDATE_ID, "2"},
        {MessageUtils.ERROR_PROCESSING_CONFIG_ID, "0"},
        {MessageUtils.MANAGED_BEAN_CLASS_NOT_FOUND_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_CLASS_DEPENDENCY_NOT_FOUND_ERROR_ID, "3"},
        {MessageUtils.MANAGED_BEAN_CLASS_IS_NOT_PUBLIC_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_CLASS_IS_ABSTRACT_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_CLASS_NO_PUBLIC_NOARG_CTOR_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_INJECTION_ERROR_ID, "1"},
        {MessageUtils.MANAGED_BEAN_LIST_PROPERTY_CONFIG_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_AS_LIST_CONFIG_ERROR_ID, "1"},
        {MessageUtils.MANAGED_BEAN_AS_MAP_CONFIG_ERROR_ID, "1"},
        {MessageUtils.MANAGED_BEAN_MAP_PROPERTY_CONFIG_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_MAP_PROPERTY_INCORRECT_SETTER_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_MAP_PROPERTY_INCORRECT_GETTER_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_DEFINED_PROPERTY_CLASS_NOT_COMPATIBLE_ERROR_ID, "3"},
        {MessageUtils.MANAGED_BEAN_INTROSPECTION_ERROR_ID, "1"},
        {MessageUtils.MANAGED_BEAN_PROPERTY_DOES_NOT_EXIST_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_PROPERTY_HAS_NO_SETTER_ID, "2"},
        {MessageUtils.MANAGED_BEAN_PROPERTY_INCORRECT_ARGS_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_LIST_SETTER_DOES_NOT_ACCEPT_LIST_OR_ARRAY_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_LIST_GETTER_DOES_NOT_RETURN_LIST_OR_ARRAY_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_LIST_GETTER_ARRAY_NO_SETTER_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_UNABLE_TO_SET_PROPERTY_ERROR_ID, "2"},
        {MessageUtils.MANAGED_BEAN_PROBLEMS_ERROR_ID, "1"},
        {MessageUtils.MANAGED_BEAN_PROBLEMS_STARTUP_ERROR_ID, "1"},
        {MessageUtils.MANAGED_BEAN_UNKNOWN_PROCESSING_ERROR_ID, "1"},
        {MessageUtils.MANAGED_BEAN_PROPERTY_UNKNOWN_PROCESSING_ERROR_ID, "1"}
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
        viewRoot.setLocale(Locale.US);
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
                assertTrue(messageInfo[i][0] + " was null", message != null);
            } else if (numParams > 0) {
                Object[] params = generateParams(numParams);
                String message = MessageUtils.getExceptionMessageString(messageInfo[i][0],
                                                          params);
                assertTrue(message != null);
                for (int j = 0; j < params.length; j++) {
                    assertTrue(messageInfo[i][0] + " unable to finder marker for param " + j,message.indexOf((String) params[j]) != -1);
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
