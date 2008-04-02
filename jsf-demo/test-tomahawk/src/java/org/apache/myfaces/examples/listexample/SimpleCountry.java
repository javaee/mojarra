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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:26 $
 */
public class SimpleCountry implements Serializable
{
	private long _id;
	private String _name;
	private String _isoCode;
	private BigDecimal _size;
	private boolean _remove = false;
	private List _cities;
	private String mSortCitiesColumn;
	private boolean mIsSortCitiesAscending;
	
	public SimpleCountry(long id, String name, String isoCode, BigDecimal size, SimpleCity[] cities)
	{
		_id = id;
		_name = name;
		_isoCode = isoCode;
		_size = size;

		if (cities != null)
			_cities = new ArrayList(Arrays.asList(cities));
		else
			_cities = new ArrayList();
	}

	public long getId()
	{
		return _id;
	}

	public String getName()
	{
		return _name;
	}

	public String getIsoCode()
	{
		return _isoCode;
	}

	public BigDecimal getSize()
	{
		return _size;
	}

	public List getCities()
	{
		if (mSortCitiesColumn != null)
		{
			Collections.sort(_cities, new Comparator()
			{
				public int compare(Object arg0, Object arg1)
				{
					SimpleCity lhs;
					SimpleCity rhs;
					if (isSortCitiesAscending())
					{
						lhs = (SimpleCity) arg0;
						rhs = (SimpleCity) arg1;
					}
					else
					{
						rhs = (SimpleCity) arg0;
						lhs = (SimpleCity) arg1;
					}
					String lhsName = lhs.getName();
					String rhsName = rhs.getName();
					if (lhsName != null)
					{
						if(rhsName != null)
						{
							return lhsName.compareToIgnoreCase(rhsName);
						}
						return -1;
					}
					else if (rhsName != null)
					{
						return 1;
					}
					return 0;
				}
			});
		}
		return _cities;
	}

	public void setId(long id)
	{
		_id = id;
	}

	public void setIsoCode(String isoCode)
	{
		_isoCode = isoCode;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public void setSize(BigDecimal size)
	{
		_size = size;
	}

	public boolean isRemove()
	{
		return _remove;
	}

	public void setRemove(boolean remove)
	{
		_remove = remove;
	}

	public String addCity()
	{
		getCities().add(new SimpleCity());
		return null;
	}

	public void deleteCity(ActionEvent ev)
	{
		UIData datatable = findParentHtmlDataTable(ev.getComponent());
		getCities().remove(datatable.getRowIndex() + datatable.getFirst());
	}

	public void setSortCitiesColumn(String columnName)
	{
		mSortCitiesColumn = columnName;
	}

	/**
	 * @return Returns the sortCitiesColumn.
	 */
	public String getSortCitiesColumn()
	{
		return mSortCitiesColumn;
	}

	public boolean isSortCitiesAscending()
	{
		return mIsSortCitiesAscending;
	}

	/**
	 * @param isSortCitiesAscending The isSortCitiesAscending to set.
	 */
	public void setSortCitiesAscending(boolean isSortCitiesAscending)
	{
		mIsSortCitiesAscending = isSortCitiesAscending;
	}

	/**
	 * @param component
	 * @return
	 */
	private HtmlDataTable findParentHtmlDataTable(UIComponent component)
	{
		if (component == null)
		{
			return null;
		}
		if (component instanceof HtmlDataTable)
		{
			return (HtmlDataTable) component;
		}
		return findParentHtmlDataTable(component.getParent());
	}
}
