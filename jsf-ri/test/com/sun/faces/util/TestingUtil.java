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

package com.sun.faces.util;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

/**
 * $Id: TestingUtil.java,v 1.1 2005/09/15 00:46:03 rlubke Exp $
 */
public class TestingUtil {

    public static Object invokePrivateMethod(String methodName,
                                             Class[] params,
                                             Object[] args,
                                             Class containingClass,
                                             Object invocationTarget) {
        try {
            Method method =
                containingClass.
                    getDeclaredMethod(methodName, params);
            method.setAccessible(true);
            return method.invoke(invocationTarget, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getPrivateField(String fieldName,
                                         Class containingClass,
                                         Object target) {
        try {
            Field field = containingClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    public static void setPrivateField(String fieldName,
                                       Class containingClass,
                                       Object target,
                                       Object value) {
        try {
            Field field = containingClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
