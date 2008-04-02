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
package org.apache.myfaces.examples.collapsiblepanel;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * @author Martin Marinschek
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:09 $
 *          <p/>
 *          $Log: CollapsiblePanelBean.java,v $
 *          Revision 1.1  2005/11/08 06:08:09  edburns
 *          import_tomahawk_test_app
 *
 */
public class CollapsiblePanelBean implements Serializable
{
    private boolean _collapsed;

    private String _firstName="Hugo";
    private String _surName;
    private Date _birthDate;
    private List _persons;

    public boolean isCollapsed()
    {
        return _collapsed;
    }

    public void setCollapsed(boolean collapsed)
    {
        _collapsed = collapsed;
    }

    public String getFirstName()
    {
        return _firstName;
    }

    public void setFirstName(String firstName)
    {
        _firstName = firstName;
    }

    public String getSurName()
    {
        return _surName;
    }

    public void setSurName(String surName)
    {
        _surName = surName;
    }

    public Date getBirthDate()
    {
        return _birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        _birthDate = birthDate;
    }

    public List getPersons()
    {
        if(_persons == null)
        {
            _persons = new ArrayList();

            Person person = new Person();
            person.setFirstName("Hugo");
            person.setSurName("Portisch");
            _persons.add(person);
            person = new Person();
            person.setFirstName("Anja");
            person.setSurName("Kruse");
            _persons.add(person);
            person = new Person();
            person.setFirstName("Max");
            person.setSurName("Meier");
            _persons.add(person);
        }

        return _persons;

    }

    public void setPersons(List persons)
    {
        _persons = persons;
    }
}
