/*
 * ConverterPropertyEditor.java
 *
 * Created on August 10, 2006, 12:39 PM
 *
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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */
package com.sun.faces.application;

import java.util.Date;

/**
 * Default template class for the dynamic generation of target-class specific
 * PropertyEditor implementations.
 */
public class ConverterPropertyEditorFor_XXXX extends
    ConverterPropertyEditorBase {
    @Override
    protected Class<?> getTargetClass() {
        // Doesn't really matter what this is, since it get's replaced when the
        // concrete PropertyEditor class is generated (it can be any valid class
        // reference that is not otherwise refered to in this class -- so don't
        // make it ConverterPropertyEditorBase or
        // ConverterPropertyEditorFor_XXXX).
        return Date.class;
    }
}
