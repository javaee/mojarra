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

import org.apache.myfaces.component.html.ext.HtmlDataTable;

import javax.faces.event.ActionEvent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:27 $
 */
public class SimpleCountryList
{
    private List _countries = new ArrayList();
    static
    {
    }

    SimpleCountry getSimpleCountry(long id)
    {
        for (int i = 0; i < _countries.size(); i++)
        {
            SimpleCountry country = (SimpleCountry)_countries.get(i);
            if (country.getId() == id)
            {
                return country;
            }
        }
        return null;
    }

    long getNewSimpleCountryId()
    {
        long maxId = 0;
        for (int i = 0; i < _countries.size(); i++)
        {
            SimpleCountry country = (SimpleCountry)_countries.get(i);
            if (country.getId() > maxId)
            {
                maxId = country.getId();
            }
        }
        return maxId + 1;
    }

    void saveSimpleCountry(SimpleCountry simpleCountry)
    {
        if (simpleCountry.getId() == 0)
        {
            simpleCountry.setId(getNewSimpleCountryId());
        }
        boolean found = false;
        for (int i = 0; i < _countries.size(); i++)
        {
            SimpleCountry country = (SimpleCountry)_countries.get(i);
            if (country.getId() == simpleCountry.getId())
            {
                _countries.set(i, simpleCountry);
                found = true;
            }
        }
        if (!found)
        {
            _countries.add(simpleCountry);
        }
    }

    void deleteSimpleCountry(SimpleCountry simpleCountry)
    {
        for (int i = 0; i < _countries.size(); i++)
        {
            SimpleCountry country = (SimpleCountry)_countries.get(i);
            if (country.getId() == simpleCountry.getId())
            {
                _countries.remove(i);
            }
        }
    }

    public SimpleCountryList()
    {
        _countries.add(new SimpleCountry(1, "AUSTRIA", "AT", new BigDecimal(123), createCities(new String[]{"Wien","Graz","Linz","Salzburg"})));
        _countries.add(new SimpleCountry(2, "AZERBAIJAN", "AZ", new BigDecimal(535), createCities(new String[]{"Baku","Sumgait","Qabala","Agdam"})));
        _countries.add(new SimpleCountry(3, "BAHAMAS", "BS", new BigDecimal(1345623), createCities(new String[]{"Nassau","Alice Town","Church Grove","West End"})));
        _countries.add(new SimpleCountry(4, "BAHRAIN", "BH", new BigDecimal(346), createCities(new String[]{"Bahrain"})));
        _countries.add(new SimpleCountry(5, "BANGLADESH", "BD", new BigDecimal(456), createCities(new String[]{"Chittagong","Chandpur","Bogra","Feni"})));
        _countries.add(new SimpleCountry(6, "BARBADOS", "BB", new BigDecimal(45645), createCities(new String[]{"Grantley Adams"})));
    }

    /**
	 * @param strings
	 * @return
	 */
	private SimpleCity[] createCities(String[] names)
	{
		SimpleCity[] result = new SimpleCity[names.length];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = new SimpleCity(names[i]);
		}
		return result;
	}

	public List getCountries()
    {
        return _countries;
    }
    
    public Map getCountryMap()
    {
        Map map = new HashMap();

        List li = getCountries();

        for (int i = 0; i < li.size(); i++)
        {
            SimpleCountry simpleCountry = (SimpleCountry) li.get(i);
            map.put(simpleCountry.getIsoCode(),simpleCountry.getName());
        }

        return map;
    }
    
    public void setCountries(List countries)
    {
        _countries = countries;
    }

    public String addCountry()
    {
        List list = getCountries();
        list.add(new SimpleCountry(list.size() + 1, "", "", new BigDecimal(0), createCities(new String[] {})));
        return "ok";
    }

	public void deleteCountry(ActionEvent ev)
	{
		UIData datatable = findParentHtmlDataTable(ev.getComponent());
		getCountries().remove(datatable.getRowIndex() + datatable.getFirst());
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
