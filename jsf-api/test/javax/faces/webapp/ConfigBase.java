/*
 * $Id: ConfigBase.java,v 1.1 2003/04/07 21:45:34 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


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


    // ------------------------------------------------------------ <converter>


    private Map converters = new HashMap();
    public void addConverter(ConfigConverter converter) {
        converters.put(converter.getConverterId(), converter);
    }
    public Map getConverters() {
        return (this.converters);
    }


}
