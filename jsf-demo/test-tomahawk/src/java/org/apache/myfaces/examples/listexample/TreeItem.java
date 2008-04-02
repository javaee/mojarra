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

/**
 * <p>
 * Bean class holding a tree item.
 * </p>
 * 
 * @author <a href="mailto:dlestrat@apache.org">David Le Strat</a>
 * */
public class TreeItem implements Serializable
{
    private int id;

    private String name;

    private String isoCode;

    private String description;

    
    /**
     * @param id The id.
     * @param name The name.
     * @param isoCode The isoCode.
     * @param description The description.
     */
    public TreeItem(int id, String name, String isoCode, String description)
    {
        this.id = id;
        this.name = name;
        this.isoCode = isoCode;
        this.description = description;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return Returns the id.
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return Returns the isoCode.
     */
    public String getIsoCode()
    {
        return isoCode;
    }

    /**
     * @param isoCode The isoCode to set.
     */
    public void setIsoCode(String isoCode)
    {
        this.isoCode = isoCode;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
}
