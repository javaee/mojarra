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

public class ColumnHeader

{
    public ColumnHeader()
    {
    }

    private String _label;
    private String _width;
    private boolean _editable;

    public ColumnHeader(String label, String width, boolean editable)
    {
        _label = label;
        _width = width;
        _editable = editable;
    }

    //=========================================================================
    // Getters
    //=========================================================================

    public String getLabel()
    {
        return _label;
    }

    public String getWidth()
    {
        return _width;
    }

    public boolean isEditable()
    {
        return _editable;
    }

    //=========================================================================
    // Getters
    //=========================================================================

    public void setLabel(String label)
    {
        _label = label;
    }

    public void setWidth(String width)
    {
        _width = width;
    }

    public void setEditable(boolean editable)
    {
        _editable = editable;
    }

}
