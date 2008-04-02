/*
 * $Id: ConfigBase.java,v 1.5 2005/08/22 22:08:30 ofung Exp $
 */

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

package javax.faces.webapp;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>Base bean for parsing configuration information.</p>
 */
public class ConfigBase {


    // ---------------------------------------------------------- <application>


    private String actionListener = null;
    public String getActionListener() {
        return (this.actionListener);
    }
    public void setActionListener(String actionListener) {
        this.actionListener = actionListener;
    }

    private String navigationHandler = null;
    public String getNavigationHandler() {
        return (this.navigationHandler);
    }
    public void setNavigationHandler(String navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    private String propertyResolver = null;
    public String getPropertyResolver() {
        return (this.propertyResolver);
    }
    public void setPropertyResolver(String propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    private String variableResolver = null;
    public String getVariableResolver() {
        return (this.variableResolver);
    }
    public void setVariableResolver(String variableResolver) {
        this.variableResolver = variableResolver;
    }


    // ------------------------------------------------------------ <component>


    private Map components = null;
    public void addComponent(ConfigComponent component) {
        if (components == null) {
            components = new HashMap();
        }
        components.put(component.getComponentType(), component);
    }
    public Map getComponents() {
        if (components == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.components);
        }
    }


    // ------------------------------------------------------------ <converter>


    private Map converters = null;
    public void addConverter(ConfigConverter converter) {
        if (converters == null) {
            converters = new HashMap();
        }
        converters.put(converter.getConverterId(), converter);
    }
    public Map getConverters() {
        if (converters == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.converters);
        }
    }


    // ------------------------------------------------------------ <validator>


    private Map validators = null;
    public void addValidator(ConfigValidator validator) {
        if (validators == null) {
            validators = new HashMap();
        }
        validators.put(validator.getValidatorId(), validator);
    }
    public Map getValidators() {
        if (validators == null) {
            return (Collections.EMPTY_MAP);
        } else {
            return (this.validators);
        }
    }


}
