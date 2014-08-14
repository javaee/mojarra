/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.systest.myfaces_uidata_component_state_test;

import javax.faces.component.html.HtmlDataTable;

import javax.faces.event.ActionEvent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@ManagedBean(name="countryList")
@SessionScoped
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
        _countries.add(new SimpleCountry(1, "AUSTRIA", "AT", new BigDecimal(123L), createCities(new String[]{"Wien","Graz","Linz","Salzburg"})));
        _countries.add(new SimpleCountry(2, "AZERBAIJAN", "AZ", new BigDecimal(535L), createCities(new String[]{"Baku","Sumgait","Qabala","Agdam"})));
        _countries.add(new SimpleCountry(3, "BAHAMAS", "BS", new BigDecimal(1345623L), createCities(new String[]{"Nassau","Alice Town","Church Grove","West End"})));
        _countries.add(new SimpleCountry(4, "BAHRAIN", "BH", new BigDecimal(346L), createCities(new String[]{"Bahrain"})));
        _countries.add(new SimpleCountry(5, "BANGLADESH", "BD", new BigDecimal(456L), createCities(new String[]{"Chittagong","Chandpur","Bogra","Feni"})));
        _countries.add(new SimpleCountry(6, "BARBADOS", "BB", new BigDecimal(45645L), createCities(new String[]{"Grantley Adams"})));
    }

    /**
	 * @param names
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
