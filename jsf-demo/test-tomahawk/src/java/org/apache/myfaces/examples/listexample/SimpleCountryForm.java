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

import javax.faces.context.FacesContext;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author: edburns $)
 * @author Thomas Spiegl
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:27 $
 */
public class SimpleCountryForm
{
    private long _id;
    private String _name;
    private String _isoCode;

    public long getId()
    {
        return _id;
    }

    public void setId(long id)
    {
        _id = id;
        if (_id > 0)
        {
            SimpleCountry simpleCountry = getList().getSimpleCountry(_id);
            if (simpleCountry == null)
            {
                return;
            }
            _name = simpleCountry.getName();
            _isoCode = simpleCountry.getIsoCode();
        }
    }

    public void setIsoCode(String isoCode)
    {
        _isoCode = isoCode;
    }

    public String getIsoCode()
    {
        return _isoCode;
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    private SimpleCountry getSimpleCountry()
    {
        return new SimpleCountry(_id, _name, _isoCode, null, null);
    }

    public String save()
    {
        getList().saveSimpleCountry(getSimpleCountry());
        return "ok_next";
    }

    public String delete()
    {
        getList().deleteSimpleCountry(getSimpleCountry());
        return "ok_next";
    }

    public String apply()
    {
        getList().saveSimpleCountry(getSimpleCountry());
        return "ok";
    }

    private SimpleCountryList getList()
    {
        Object obj = FacesContext.getCurrentInstance().getApplication().getVariableResolver()
            .resolveVariable(FacesContext.getCurrentInstance(), "countryList");
        return (SimpleCountryList) obj;

    }
}
