/*
 * $Id: TestListener.java,v 1.4 2004/02/04 23:39:30 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
