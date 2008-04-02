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



/**
 * Convenient base class for sortable lists.
 * @author Thomas Spiegl (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:28 $
 */
public abstract class SortableList
{
    private String _sort;
    private boolean _ascending;

    protected SortableList(String defaultSortColumn)
    {
        _sort = defaultSortColumn;
        _ascending = isDefaultAscending(defaultSortColumn);
    }

    /**
     * Sort the list.
     */
    protected abstract void sort(String column, boolean ascending);

    /**
     * Is the default sort direction for the given column "ascending" ?
     */
    protected abstract boolean isDefaultAscending(String sortColumn);


    public void sort(String sortColumn)
    {
        if (sortColumn == null)
        {
            throw new IllegalArgumentException("Argument sortColumn must not be null.");
        }

        if (_sort.equals(sortColumn))
        {
            //current sort equals new sortColumn -> reverse sort order
            _ascending = !_ascending;
        }
        else
        {
            //sort new column in default direction
            _sort = sortColumn;
            _ascending = isDefaultAscending(_sort);
        }

        sort(_sort, _ascending);
    }

    public String getSort()
    {
        return _sort;
    }

    public void setSort(String sort)
    {
        _sort = sort;
    }

    public boolean isAscending()
    {
        return _ascending;
    }

    public void setAscending(boolean ascending)
    {
        _ascending = ascending;
    }
}
