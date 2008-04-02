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
package org.apache.myfaces.examples.calendarexample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;

/**
 * @author Martin Marinschek
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:07 $
 *          <p/>
 *          $Log: CalendarBean.java,v $
 *          Revision 1.1  2005/11/08 06:08:07  edburns
 *          import_tomahawk_test_app
 *
 */
public class CalendarBean implements Serializable
{
    private static Log log = LogFactory.getLog(CalendarBean.class);

    private List _dates;

    private String _text;
    private Date _firstDate;
    private Date _secondDate;

    public List getDates()
    {
        if(_dates == null)
        {
            _dates = new ArrayList();

            for(int i=0; i<3; i++)
                _dates.add(new DateHolder());
        }

        return _dates;
    }

    public void setDates(List dates)
    {
        _dates = dates;
    }

    public String getText()
    {
        return _text;
    }

    public void setText(String text)
    {
        _text = text;
    }

    public Date getFirstDate()
    {
        return _firstDate;
    }

    public void setFirstDate(Date firstDate)
    {
        _firstDate = firstDate;
    }

    public Date getSecondDate()
    {
        return _secondDate;
    }

    public void setSecondDate(Date secondDate)
    {
        _secondDate = secondDate;
    }

    public String submitMethod()
    {
        log.info("submit method called");

        return "submit";
    }


}
