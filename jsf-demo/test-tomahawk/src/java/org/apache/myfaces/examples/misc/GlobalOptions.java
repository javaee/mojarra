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

/**
 * Global (dynamically changeable) options for examples application.
 * @author Manfred Geiler (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:32 $
 */
public class GlobalOptions
{
    private String _pageLayout;

    public String getPageLayout()
    {
        return _pageLayout;
    }

    public void setPageLayout(String pageLayout)
    {
        _pageLayout = pageLayout;
    }


    public String getNumericAsString() {
        return "23";
    }

    
    public long getNumeric() {
        return 23L;
    }

}
