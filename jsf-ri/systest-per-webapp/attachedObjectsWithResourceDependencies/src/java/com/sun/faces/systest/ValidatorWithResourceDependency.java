/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.systest;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author edburns
 */
@ResourceDependency(name="converterScript.js")
public class ValidatorWithResourceDependency implements Validator {

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    }
    
    

}
