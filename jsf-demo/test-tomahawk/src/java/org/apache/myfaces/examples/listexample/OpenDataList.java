/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.examples.listexample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

public class OpenDataList extends SortableList
{
    private DataModel data;
    private DataModel columnHeaders;

    private static final int SORT_ASCENDING = 1;
    private static final int SORT_DESCENDING = -1;

    public OpenDataList()
    {
        super(null);

        // create header info
        List headerList = new ArrayList();
        headerList.add(new ColumnHeader("Index","100",false));
        headerList.add(new ColumnHeader("Type","200",true));
        headerList.add(new ColumnHeader("Model","300",true));
        columnHeaders = new ListDataModel(headerList);

        // create list of lists (data)
        List rowList = new ArrayList();
        for (int i = 100; i <= 999; i++)
        {
            List colList = new ArrayList();
            colList.add(new Integer(i));
            colList.add("Car Type " + i);
            colList.add((i%2==0) ? "blue" : "green");
            rowList.add(colList);
        }
        data = new ListDataModel(rowList);
    }

    //==========================================================================
    // Getters
    //==========================================================================

    public DataModel getData()
    {
        sort(getSort(), isAscending());
        return data;
    }

    void setData(DataModel datamodel)
    {
    	System.out.println("preserved datamodel updated");
    	// just here to see if the datamodel is updated if preservedatamodel=true
    }

    public DataModel getColumnHeaders()
    {
        return columnHeaders;
    }

    //==========================================================================
    // Public Methods
    //==========================================================================

    public Object getColumnValue()
    {
        Object columnValue = null;
        if (data.isRowAvailable() && columnHeaders.isRowAvailable())
        {
            columnValue = ((List)data.getRowData()).get(columnHeaders.getRowIndex());
        }
        return columnValue;
    }

    public void setColumnValue(Object value)
    {
      if (data.isRowAvailable() && columnHeaders.isRowAvailable())
      {
          ((List)data.getRowData()).set(columnHeaders.getRowIndex(), value);
      }
    }

    public String getColumnWidth()
    {
        String columnWidth = null;
        if (data.isRowAvailable() && columnHeaders.isRowAvailable())
        {
            columnWidth = ((ColumnHeader)columnHeaders.getRowData()).getWidth();
        }
        return columnWidth;
    }

    public boolean isValueModifiable()
    {
        boolean valueModifiable = false;
        if (data.isRowAvailable() && columnHeaders.isRowAvailable())
        {
            valueModifiable = ((ColumnHeader)columnHeaders.getRowData()).isEditable();
        }
        return valueModifiable;
    }

    //==========================================================================
    // Protected Methods
    //==========================================================================

    protected boolean isDefaultAscending(String sortColumn)
    {
        return true;
    }

    protected void sort(final String column, final boolean ascending)
    {
        if (column != null)
        {
            int columnIndex = getColumnIndex(column);
            int direction = (ascending) ? SORT_ASCENDING : SORT_DESCENDING;
            sort(columnIndex, direction);
        }
    }

    protected void sort(final int columnIndex, final int direction)
    {
        Comparator comparator = new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                int result = 0;
                Object column1 = ((List)o1).get(columnIndex);
                Object column2 = ((List)o2).get(columnIndex);
                if (column1 == null && column2 != null)
                    result = -1;
                else if (column1 == null && column2 == null)
                    result = 0;
                else if (column1 != null && column2 == null)
                    result = 1;
                else
                    result = ((Comparable)column1).compareTo(column2) * direction;
                return result;
            }
        };
        Collections.sort((List)data.getWrappedData(), comparator);
    }

    //==========================================================================
    // Private Methods
    //==========================================================================

    private int getColumnIndex(final String columnName)
    {
        int columnIndex = -1;
        List headers = (List) columnHeaders.getWrappedData();
        for (int i=0;i<headers.size() && columnIndex==-1;i++)
        {
            ColumnHeader header = (ColumnHeader) headers.get(i);
            if (header.getLabel().equals(columnName))
            {
                columnIndex = i;
            }
        }
        return columnIndex;
    }

}
