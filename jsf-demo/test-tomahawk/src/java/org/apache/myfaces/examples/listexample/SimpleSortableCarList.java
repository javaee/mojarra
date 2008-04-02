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

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:28 $
 */
public class SimpleSortableCarList
    extends SortableList
{
    private List _cars;

    public SimpleSortableCarList()
    {
        super("type");

        _cars = new ArrayList();
        _cars.add(new SimpleCar(1, "car A", "red"));
        _cars.add(new SimpleCar(1, "car B", "blue"));
        _cars.add(new SimpleCar(1, "car C", "green"));
        _cars.add(new SimpleCar(1, "car D", "yellow"));
        _cars.add(new SimpleCar(1, "car E", "orange"));
    }

    public List getCars()
    {
        sort(getSort(), isAscending());
        return _cars;
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
                SimpleCar c1 = (SimpleCar)o1;
                SimpleCar c2 = (SimpleCar)o2;
                if (column == null)
                {
                    return 0;
                }
                if (column.equals("type"))
                {
                    return ascending ? c1.getType().compareTo(c2.getType()) : c2.getType().compareTo(c1.getType());
                }
                else if (column.equals("color"))
                {
                    return ascending ? c1.getColor().compareTo(c2.getColor()) : c2.getColor().compareTo(c1.getColor());
                }
                else return 0;
            }
        };
        Collections.sort(_cars, comparator);
    }
}
