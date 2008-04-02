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
import javax.faces.el.VariableResolver;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:33 $
 */
public class OptionsController
{
    public String changeLocale()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        VariableResolver vr = facesContext.getApplication().getVariableResolver();
        OptionsForm form = (OptionsForm)vr.resolveVariable(facesContext, "optionsForm");
        facesContext.getViewRoot().setLocale(form.getLocale());
        return "ok";
    }
}
