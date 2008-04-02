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
package org.apache.myfaces.examples.crosstable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.apache.myfaces.examples.listexample.SimpleCountry;
import org.apache.myfaces.examples.listexample.SimpleCountryList;

/**
 * @author Mathias Broekelmann (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:12 $
 */
public class DataBean extends SimpleCountryList
{
  private DataModel mColumns;
  private DataModel mCountryDataModel;
  private Map mValueMap = new HashMap();
  private boolean mEditValues;
  private String mColumnLabel;

  /**
   *
   */
  public DataBean()
  {
    super();
  }

  public boolean isEditValues()
  {
    return mEditValues;
  }

  public String editValues()
  {
    mEditValues = true;
    return null;
  }

  public String saveValues()
  {
    mEditValues = false;
    return null;
  }

  public String addColumn()
  {
    if (mColumnLabel != null)
    {
      List columns = (List) getColumnDataModel().getWrappedData();
      columns.add(mColumnLabel);
    }
    return null;
  }

  public String removeColumn()
  {
    if (mColumns != null && mColumns.isRowAvailable())
    {
      Object column = mColumns.getRowData();
      List columns = (List) getColumnDataModel().getWrappedData();
      columns.remove(column);
    }
    return null;
  }

  public String getColumnLabel()
  {
    return mColumnLabel;
  }

  public void setColumnLabel(String label)
  {
    mColumnLabel = label;
  }

  public DataModel getCountryDataModel()
  {
    if (mCountryDataModel == null)
    {
      mCountryDataModel = new ListDataModel(getCountries());
    }
    return mCountryDataModel;
  }

  public DataModel getColumnDataModel()
  {
    if (mColumns == null)
    {
      String[] result = new String[] {"2002", "2003", "2004"};
      mColumns = new ListDataModel(new ArrayList(Arrays.asList(result)));
    }
    return mColumns;
  }

  public String getColumnValue()
  {
    DataModel countryDataModel = getCountryDataModel();
    if (countryDataModel.isRowAvailable())
    {
      SimpleCountry row = (SimpleCountry) countryDataModel.getRowData();
      DataModel columnDataModel = getColumnDataModel();
      if (columnDataModel.isRowAvailable())
      {
        Object column = columnDataModel.getRowData();
        Object key = new RowColumnKey(new Long(row.getId()), column);
        if (!mValueMap.containsKey(key))
        {
          // initialize with random value
          String randomValue = String.valueOf((int) (Math.random() * 5000) + 5000);
          mValueMap.put(key, randomValue);
        }
        return (String) mValueMap.get(key);
      }
    }
    return null;
  }

  public void setColumnValue(String value)
  {
    DataModel countryDataModel = getCountryDataModel();
    if (countryDataModel.isRowAvailable())
    {
      SimpleCountry row = (SimpleCountry) countryDataModel.getRowData();
      DataModel columnDataModel = getColumnDataModel();
      if (columnDataModel.isRowAvailable())
      {
        Object column = columnDataModel.getRowData();
        Object key = new RowColumnKey(new Long(row.getId()), column);
        mValueMap.put(key, value);
      }
    }
  }

  private class RowColumnKey
  {
    private final Object mRow;
    private final Object mColumn;

    /**
     * @param row
     * @param column
     */
    public RowColumnKey(Object row, Object column)
    {
      mRow = row;
      mColumn = column;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
      if (obj == null)
      {
        return false;
      }
      if (obj == this)
      {
        return true;
      }
      if (obj instanceof RowColumnKey)
      {
        RowColumnKey other = (RowColumnKey) obj;
        return other.mRow.equals(mRow) && other.mColumn.equals(mColumn);
      }
      return super.equals(obj);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
      return (37 * 3 + mRow.hashCode()) * (37 * 3 + mColumn.hashCode());
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return mRow.toString() + "," + mColumn.toString();
    }
  }
}

