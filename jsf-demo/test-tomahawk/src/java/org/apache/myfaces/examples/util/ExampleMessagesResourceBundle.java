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
package org.apache.myfaces.examples.util;

import javax.faces.context.FacesContext;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:44 $
 */
public class ExampleMessagesResourceBundle
    extends ResourceBundle
{
    private static String BUNDLE_NAME = "org.apache.myfaces.examples.resource.example_messages";

    protected ResourceBundle getMyBundle()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return ResourceBundle.getBundle(BUNDLE_NAME, facesContext.getViewRoot().getLocale());
    }

    protected Object handleGetObject(String key)
    {
        return getMyBundle().getObject(key);
    }

    public Enumeration getKeys()
    {
        return getMyBundle().getKeys();
    }
}
