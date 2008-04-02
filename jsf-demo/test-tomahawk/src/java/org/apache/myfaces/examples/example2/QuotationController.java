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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:18 $
 */
public class QuotationController
    implements ActionListener
{
    public void processAction(ActionEvent event) throws AbortProcessingException
    {
        if (event.getPhaseId() == PhaseId.INVOKE_APPLICATION)
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            UIComponent component = event.getComponent();

            VariableResolver vr = facesContext.getApplication().getVariableResolver();
            QuotationForm form = (QuotationForm)vr.resolveVariable(facesContext, "q_form");
            if (component.getId().equals("button1"))
            {
                form.quote();
            }
            else
            {
                form.unquote();
            }
        }

    }
    
    public PhaseId getPhaseId()
    {
        return PhaseId.UPDATE_MODEL_VALUES;
    }
}