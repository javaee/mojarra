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
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:25 $
 */
public class SimpleCar
        implements Serializable
{
    private int _id;
    private String _type;
    private String _color;

    public SimpleCar(int id, String type, String color)
    {
        _id = id;
        _type = type;
        _color = color;
    }

    public int getId()
    {
        return _id;
    }

    public void setId(int id)
    {
        _id = id;
    }

    public String getType()
    {
        return _type;
    }

    public void setType(String type)
    {
        _type = type;
    }

    public String getColor()
    {
        return _color;
    }

    public void setColor(String color)
    {
        _color = color;
    }
}
