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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PagedSortableCarList extends SortableList
{
	private List cars = new ArrayList();

	private Map carMap = new HashMap();

	public PagedSortableCarList()
	{
		super("type");
		for (int i = 100; i < 900; i++)
		{
			Object car = new SimpleCar(i, "Car Type " + i, (i % 2 == 0) ? "blue" : "green");
			cars.add(car);
			carMap.put(new Integer(i), car);
		}
	}

	public List getCars()
	{
		sort(getSort(), isAscending());
		return cars;
	}

	public void setCars(List cars)
	{
		// update the cars from the provided list
		for (Iterator iter = cars.iterator(); iter.hasNext();)
		{
			SimpleCar car = (SimpleCar) iter.next();
			SimpleCar oldCar = (SimpleCar) carMap.get(new Integer(car.getId()));
			oldCar.setType(car.getType());
			oldCar.setColor(car.getColor());
		}
	}

	protected boolean isDefaultAscending(String sortColumn)
	{
		return true;
	}

	protected void sort(final String column, final boolean ascending)
	{
		Comparator comparator = new Comparator()
		{
			public int compare(Object o1, Object o2)
			{
				SimpleCar c1 = (SimpleCar) o1;
				SimpleCar c2 = (SimpleCar) o2;
				if (column == null)
				{
					return 0;
				}
				if (column.equals("type"))
				{
					return ascending ? c1.getType().compareTo(c2.getType()) : c2.getType()
									.compareTo(c1.getType());
				}
				else if (column.equals("color"))
				{
					return ascending ? c1.getColor().compareTo(c2.getColor()) : c2.getColor()
									.compareTo(c1.getColor());
				}
				else
					return 0;
			}
		};
		Collections.sort(cars, comparator);
	}
}
