/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */


package carstore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * <p>A helper class that customizes a CarBean for a set of options
 * in a package.</p>
 *
 * <p>This class reads its settings from a Properties file</p>
 */

public class CarCustomizer extends Object {

    protected static final Log log = LogFactory.getLog(CarCustomizer.class);

    //
    // Relationship Instance Variables
    //

    private ResourceBundle bundle = null;


    public CarCustomizer() {
        this.init(CarStore.DEFAULT_PACKAGE_PROPERTIES);
    }


    public CarCustomizer(String bundleName) {
        this.init(bundleName);
    }


    private void init(String bundleName) {
        FacesContext context = FacesContext.getCurrentInstance();

        if (log.isDebugEnabled()) {
            log.debug("Loading bundle: " + bundleName + ".");
        }
        bundle = ResourceBundle.getBundle(bundleName);
    }


    private String buttonStyle = null;


    public String getButtonStyle() {
        return buttonStyle;
    }


    public void setButtonStyle(String newButtonStyle) {
        buttonStyle = newButtonStyle;
    }


    public void customizeCar(CarBean toCustomize) {
        FacesContext context = FacesContext.getCurrentInstance();
        Enumeration keys = bundle.getKeys();
        String
            key = null,
            disabledStr = null,
            curSetting = null;
        Boolean disabled = null;
        UIComponent component = null;
        Converter converter = null;
        Object valueToSet = null;

        while (keys.hasMoreElements()) {
            key = (String) keys.nextElement();
            // skip null and secondary keys.
            if (key == null || -1 != key.indexOf("_")) {
                continue;
            }
            // skip null values
            if (null == (curSetting = bundle.getString(key))) {
                continue;
            }

            // skip null components
            if (null ==
                (component =
                (UIComponent) toCustomize.getComponents().get(key))) {
                continue;
            }

            // handle the disabled setting, if necessary
            disabled = null;
            try {
                if (null != (disabledStr = bundle.getString(key + "_disabled"))) {
                    disabled = Boolean.valueOf(disabledStr);
                }
            } catch (Throwable e) {
            }
            if (null != disabled) {
                component.getAttributes().put("disabled", disabled);
            }

            // set the value
            // If the component can and does have a converter
            if (component instanceof ValueHolder &&
                (null != (converter =
                ((ValueHolder) component).getConverter()))) {
                valueToSet = converter.getAsObject(context, component,
                                                   curSetting);
            } else {
                valueToSet = curSetting;
            }

            if (component instanceof ValueHolder) {
                ((ValueHolder) component).setValue(valueToSet);
            }
        }
    }
}
	
	
	
