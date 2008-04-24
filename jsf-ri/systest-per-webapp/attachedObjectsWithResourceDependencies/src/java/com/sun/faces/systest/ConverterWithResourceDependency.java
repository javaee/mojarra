/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.systest;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author edburns
 */
@ResourceDependency(library="resourceLib", name="validatorScript.js")
public class ConverterWithResourceDependency implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }

}
