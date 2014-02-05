/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package carstore;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * <p>A helper class that customizes a CarBean for a set of options
 * in a package.</p>
 * <p/>
 * <p>This class reads its settings from a Properties file</p>
 */

public class CarCustomizer {

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
        String disabledStr;
        String curSetting;
        UIComponent component;
        Converter converter;
        Object valueToSet;

        for (Enumeration keys = bundle.getKeys(); keys.hasMoreElements();) {

            String key = (String) keys.nextElement();
            // skip null and secondary keys.
            if (key == null || -1 != key.indexOf('_')) {
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
            Boolean disabled = null;
            try {
                if (null !=
                    (disabledStr = bundle.getString(key + "_disabled"))) {
                    disabled = Boolean.valueOf(disabledStr);
                }
            } catch (Throwable e) {
                // do nothing
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
	
	
	
