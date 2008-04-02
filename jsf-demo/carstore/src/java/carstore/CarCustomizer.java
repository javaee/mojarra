/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * <p>A helper class that customizes a CarBean for a set of options
 * in a package.</p>
 *
 * <p>This class reads its settings from a Properties file</p>
 */

public class CarCustomizer extends Object {

    private static final Logger LOGGER = Logger.getLogger("carstore");

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

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Loading bundle: " + bundleName + ".");
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
                if (null !=
                    (disabledStr = bundle.getString(key + "_disabled"))) {
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
	
	
	
