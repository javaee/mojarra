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
package org.apache.myfaces.examples.example1;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:17 $
 */
public class ValidationController
{
    public String enableValidation()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        UIInput number1 = (UIInput)facesContext.getViewRoot().findComponent("form1:number1");
        Validator[] validators = number1.getValidators();
        if (validators == null || validators.length == 0)
        {
            number1.addValidator(new LongRangeValidator(10, 1));
        }

        UIInput number2 = (UIInput)facesContext.getViewRoot().findComponent("form1:number2");
        validators = number2.getValidators();
        if (validators == null || validators.length == 0)
        {
            number2.addValidator(new LongRangeValidator(50, 20));
        }

        UIInput text = (UIInput)facesContext.getViewRoot().findComponent("form2:text");
        validators = text.getValidators();
        if (validators == null || validators.length == 0)
        {
            text.addValidator(new LengthValidator(7, 3));
        }

        return "ok";
    }

    public String disableValidation()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        UIInput number1 = (UIInput)facesContext.getViewRoot().findComponent("form1:number1");
        Validator[] validators = number1.getValidators();
        if (validators != null)
        {
            for (int i = 0; i < validators.length; i++)
            {
                Validator validator = validators[i];
                number1.removeValidator(validator);
            }
        }

        UIInput number2 = (UIInput)facesContext.getViewRoot().findComponent("form1:number2");
        validators = number2.getValidators();
        if (validators != null)
        {
            for (int i = 0; i < validators.length; i++)
            {
                Validator validator = validators[i];
                number2.removeValidator(validator);
            }
        }

        UIInput text = (UIInput)facesContext.getViewRoot().findComponent("form2:text");
        validators = text.getValidators();
        if (validators != null)
        {
            for (int i = 0; i < validators.length; i++)
            {
                Validator validator = validators[i];
                text.removeValidator(validator);
            }
        }

        return "ok";
    }



    public String getNumber1ValidationLabel()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIInput number1 = (UIInput)facesContext.getViewRoot().findComponent("form1:number1");
        Validator[] validators = number1.getValidators();
        if (validators != null && validators.length > 0)
        {
            long min = ((LongRangeValidator)validators[0]).getMinimum();
            long max = ((LongRangeValidator)validators[0]).getMaximum();
            return " (" + min + "-" + max + ")";
        }
        else
        {
            return "";
        }
    }

    public String getNumber2ValidationLabel()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIInput number1 = (UIInput)facesContext.getViewRoot().findComponent("form1:number2");
        Validator[] validators = number1.getValidators();
        if (validators != null && validators.length > 0)
        {
            long min = ((LongRangeValidator)validators[0]).getMinimum();
            long max = ((LongRangeValidator)validators[0]).getMaximum();
            return " (" + min + "-" + max + ")";
        }
        else
        {
            return "";
        }
    }

    public String getTextValidationLabel()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIInput number1 = (UIInput)facesContext.getViewRoot().findComponent("form2:text");
        Validator[] validators = number1.getValidators();
        if (validators != null && validators.length > 0)
        {
            long min = ((LengthValidator)validators[0]).getMinimum();
            long max = ((LengthValidator)validators[0]).getMaximum();
            return " (" + min + "-" + max + " chars)";
        }
        else
        {
            return "";
        }
    }

}
