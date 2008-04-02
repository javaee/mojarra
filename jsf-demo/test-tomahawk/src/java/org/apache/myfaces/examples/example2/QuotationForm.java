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
package org.apache.myfaces.examples.example2;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl
 */
public class QuotationForm
{
    private String _text = "QuotationTest";
    private String _quoteChar;
    private String[] _selectManyValues;

    private SelectItem[] _selectItems = null;
    private List _selectManyItems = null;



    public QuotationForm()
    {
    }


    public String getText()
    {
        return _text;
    }

    public void setText(String text)
    {
        _text = text;
    }

    public String getQuoteChar()
    {
        return _quoteChar;
    }

    public void setQuoteChar(String value)
    {
        _quoteChar = value;
    }

    public SelectItem[] getSelectOneItems()
    {
        if (_selectItems == null)
        {
            _selectItems = new SelectItem[2];
            _selectItems[0] = new SelectItem("*", "Asterisk");
            _selectItems[1] = new SelectItem("+", "Plus");
        }
        return _selectItems;
    }




    public String[] getSelectManyValues()
    {
        return _selectManyValues;
    }

    public void setSelectManyValues(String[] value)
    {
        _selectManyValues = value;
    }

    public List getSelectManyItems()
    {
        if (_selectManyItems == null)
        {
            _selectManyItems = new ArrayList();
            _selectManyItems.add(new SelectItem("\"", "Double"));
            _selectManyItems.add(new SelectItem("'", "Single"));
            _selectManyItems.add(new SelectItem("*", "Asterisk"));
            _selectManyItems.add(new SelectItem("+", "Plus"));
            _selectManyItems.add(new SelectItem("-", "Hyphen"));
        }
        return _selectManyItems;
    }


    public void quote()
    {
        if (_quoteChar != null)
        {
            _text = _quoteChar + _text + _quoteChar;
        }
    }

    public void unquote()
    {
        if (_selectManyValues != null)
        {
            for (int i = 0; i < _selectManyValues.length; i++)
            {
                unquote(_selectManyValues[i]);
            }
        }
    }

    private void unquote(String quoteChar)
    {
        if (quoteChar != null && quoteChar.length() > 0)
        {
            if (_text.startsWith(quoteChar))
            {
                _text = _text.substring(1);
            }

            if (_text.endsWith(quoteChar))
            {
                _text = _text.substring(0, _text.length() - 1);
            }
        }
    }

}

