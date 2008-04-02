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
package org.apache.myfaces.examples.aliasexample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Sylvain Vieujot (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:06 $
 */
public class AliasHolder {
    private static final Log log = LogFactory.getLog(AliasHolder.class);
    
    private String name="default name";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        log.debug("set name="+name);
        this.name = name;
    }
    
    public String toUpperCase(){
        name = name.toUpperCase();
        log.debug("toUpperCase command executed");
        return null;
    }
}
