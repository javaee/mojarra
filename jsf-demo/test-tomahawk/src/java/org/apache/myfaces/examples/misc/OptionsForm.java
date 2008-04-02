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
package org.apache.myfaces.examples.misc;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:34 $
 */
public class OptionsForm
{
	private static final Locale SPANISH = new Locale("es", "","");
	private static final Locale CATALAN = new Locale("ca", "","");

    private static final List AVAILABLE_LOCALES
        = Arrays.asList(new Locale[] {Locale.ENGLISH,
                                      Locale.CHINESE,
                                      Locale.GERMAN,
                                      Locale.JAPANESE,
                                      Locale.FRENCH,
									  SPANISH,
									  CATALAN});

    private Locale _locale = null;

    public String getLanguage()
    {
        return _locale != null
                ? _locale.getLanguage()
                : FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
    }

    public void setLanguage(String language)
    {
        _locale = new Locale(language);
    }

    public Locale getLocale()
    {
        return _locale;
    }

    public List getAvailableLanguages()
    {
        return new AbstractList()
        {
            public Object get(int index)
            {
                Locale locale = (Locale)AVAILABLE_LOCALES.get(index);
                String language = locale.getDisplayLanguage(locale);
                return new SelectItem(locale.getLanguage(), language, language);
            }

            public int size()
            {
                return AVAILABLE_LOCALES.size();
            }
        };
    }


}
