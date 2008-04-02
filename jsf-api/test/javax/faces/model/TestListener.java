/*
 * $Id: TestListener.java,v 1.6 2005/08/22 22:08:28 ofung Exp $
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

package javax.faces.model;


/**
 * <p>Test implementation of DataModelListener.</p>
 */

public class TestListener implements DataModelListener {


    // ----------------------------------------------- DataModelListener Methods


    public void rowSelected(DataModelEvent event) {
        Object rowData = event.getRowData();
        int rowIndex = event.getRowIndex();
        trace("" + rowIndex);
        if ((rowIndex >= 0) && (rowData == null)) {
            throw new IllegalArgumentException("rowIndex=" + rowIndex +
                                               " but rowData is null");
        } else if ((rowIndex == -1) && (rowData != null)) {
            throw new IllegalArgumentException("rowIndex=" + rowIndex +
                                               " but rowData is not null");
        } else if (rowIndex < -1) {
            throw new IllegalArgumentException("rowIndex=" + rowIndex);
        }
            
    }


    // ---------------------------------------------------------- Static Methods


    private static StringBuffer trace = new StringBuffer();


    public static String trace() {
        return (trace.toString());
    }


    public static void trace(String value) {
        if (value == null) {
            trace = new StringBuffer();
        } else {
            trace.append("/");
            trace.append(value);
        }
    }


}
