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

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItems;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>This bean encapsulates a car model, including pricing and package
 * choices.  The system allows the user to customize the properties of
 * this bean with the help of the {@link CarCustomizer}.</p>
 * <p/>
 * <h3>Data Access</h3>
 * <p/>
 * <p>This is the only bean in the system that has complicated access to
 * the persistent store of data.  In the present implementation, this
 * persistent store is in <code>ResourceBundle</code> instances.</p>
 * <p/>
 * <p>There are three data source <code>ResourceBundle</code> files
 * used:</p>
 * <p/>
 * <ol>
 * <p/>
 * <li><p><code>&lt;ModelName&gt;</code></p>
 * <p/>
 * <p>This contains the localized content for this model.  There
 * is a variant of this file for each supported locale, for
 * example, <code>Jalopy_de.properties</code></p>
 * <p/>
 * </li>
 * <p/>
 * <li><p><code>&lt;Common_properties&gt;</code></p>
 * <p/>
 * <p>This contains the localized content common to all
 * models.</p>
 * <p/>
 * </li>
 * <p/>
 * <li><p><code>&lt;ModelName_options&gt;</code></p>
 * <p/>
 * <p>This contains the non-localized content for this model,
 * including the non-localized options.  There is only one
 * variant of this file for all locales for example,
 * <code>Jalopy_options.properties</code></p>
 * <p/>
 * </li>
 * <p/>
 * </ol>
 * <p/>
 * <p>All files conform to the following convention:</p>
 * <p/>
 * <code><pre>
 * key
 * key_componentType
 * key_valueType
 * </pre></code>
 * <p/>
 * <p>Where <code>key</code> is the name of an attribute of this car.
 * For example, <code>basePrice</code>, or <code>description</code>.
 * <code>key_componentType</code> is the component type of the
 * <code>UIComponent</code> subclass to be used to represent this
 * attribute in the page, for example <code>SelectManyMenu</code>.
 * <code>key_valueType</code> is the data type of the value of the
 * <code>UIComponent</code>, for example <code>java.lang.Integer</code>.
 * For all non-String valueTypes.</p>
 * <p/>
 * <p>When the bean is instantiated, we load both of the above
 * properties files and iterate over the keys in each one.  For each
 * key, we look at the <code>componentType</code> and ask the
 * <code>Application</code> to create a component of that type.  We
 * store that <code>UIComponent</code> instance in our
 * <code>components</code> <code>Map</code> under the name
 * <code>key</code>.  We look at the <code>valueType</code> for the
 * <code>key</code>.  For non <code>java.lang.String</code> types, we
 * ask the <code>Application</code> for a <code>Converter</code>
 * instance for that class.  If found, we use it to convert the value
 * for the <code>key</code> to the appropriate type and store that as
 * the <code>value</code> of the <code>UIComponent</code> instance.</p>
 */

public class CarBean {

    private static final Logger LOGGER = Logger.getLogger("carstore");

    /**
     * <p>The message identifier of the Message to be created if
     * the conversion fails.  The message format string for this
     * message may optionally include a <code>{0}</code>
     * placeholder, which will be replaced by the object and value.</p>
     */
    public static final String CONVERTER_ERROR_MESSAGE_ID =
          "carstore.Converter_Error";

    //
    // Relationship Instance Variables
    //

    /** Localized labels */

    private ResourceBundle resources = null;

    /** Price data */
    private ResourceBundle priceData = null;

    /**
     * Keys: String attribute name, such as engine. Values: UIComponent
     * for the attribute
     */

    private Map<String, UIComponent> components = null;

    /**
     * Keys: String attribute name, such as engine. Values: String value
     * of the component named by key in our components Map.
     */

    private Map<String,Object> attributes = null;

    // 
    // Constructors
    //

    public CarBean() {
        this.init(CarStore.DEFAULT_MODEL_PROPERTIES);
    }


    public CarBean(String bundleName) {
        this.init(bundleName);
    }


    /**
     * <p>Initialize our components <code>Map</code> as described in the
     * class documentation.</p>
     * <p/>
     * <p>Create a wrapper <code>Map</code> around the components
     * <code>Map</code> that exposes the String converted value of each
     * component.</p>
     * 
     * @param bundleName the resource bundle name
     */
    private void init(String bundleName) {
        FacesContext context = FacesContext.getCurrentInstance();       

        components = new HashMap<String, UIComponent>();

        // load the labels
        resources =
              ResourceBundle.getBundle(CarStore.CARSTORE_PREFIX +
                                       ".bundles.Resources",
                                       context.getViewRoot().getLocale());

        // load the prices
        priceData = ResourceBundle.getBundle(CarStore.CARSTORE_PREFIX +
                                             ".bundles.OptionPrices");

        // populate the locale-specific information
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Loading bundle: " + bundleName + ".");
        }
        ResourceBundle data = ResourceBundle.getBundle(bundleName,
                                        context.getViewRoot().getLocale());
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Bundle " + bundleName +
                        " loaded. Reading properties...");
        }
        initComponentsFromProperties(context, data);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("done.");
        }

        // populate the non-locale-specific information common to all cars
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Loading bundle: Common_options.");
        }
        data = ResourceBundle.getBundle(CarStore.CARSTORE_PREFIX +
                                        ".bundles.Common_options");
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Bundle Common_options loaded. Reading properties...");
        }
        initComponentsFromProperties(context, data);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("done.");
        }

        // populate the non-locale-specific information specific to each car
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Loading bundle: " + bundleName + "_options.");
        }
        data = ResourceBundle.getBundle(bundleName + "_options");
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Bundle " + bundleName +
                        "_options loaded. Reading properties...");
        }
        initComponentsFromProperties(context, data);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("done.");
        }

        // create a read-only Map exposing the values of all of our
        // components.
        attributes =
            new Map() {
                public void clear() {
                    CarBean.this.components.clear();
                }


                public boolean containsKey(Object key) {
                    return CarBean.this.components.containsKey(key);
                }


                public boolean containsValue(Object value) {
                    throw new UnsupportedOperationException();
                }


                public java.util.Set<Map.Entry<String,Object>> entrySet() {
                    throw new UnsupportedOperationException();
                }


                public boolean equals(Object o) {
                    throw new UnsupportedOperationException();
                }


                public Object get(Object key) {
                    UIComponent component;
                    Converter converter = null;
                    Object result = null;
                    if (null == key) {
                        return null;
                    }
                    if (null != (component = 
                        CarBean.this.components.get(key))) {
                        // if this component can have a Converter
                        if (component instanceof ValueHolder) {
                            // try to get it
                            converter = ((ValueHolder) component).
                                getConverter();
                            result = ((ValueHolder) component).getValue();
                        }

                        // if we do have a value
                        if (null != result) {
                            // and we do have a converter
                            if (null != converter) {
                                // convert the value to String
                                result = converter.
                                    getAsString(FacesContext.
                                                getCurrentInstance(),
                                                component, result);
                            }
                        }
                    }
                    return result;
                }


                public int hashCode() {
                    return CarBean.this.components.hashCode();
                }


                public boolean isEmpty() {
                    return CarBean.this.components.isEmpty();
                }


                public java.util.Set<String> keySet() {
                    return CarBean.this.components.keySet();
                }


                public Object put(Object k, Object v) {
                    throw new UnsupportedOperationException();
                }


                public void putAll(Map t) {
                    throw new UnsupportedOperationException();
                }


                public Object remove(Object k) {
                    throw new UnsupportedOperationException();
                }


                public int size() {
                    return CarBean.this.components.size();
                }


                public Collection<Object> values() {
                    ArrayList<Object> result = 
                          new ArrayList<Object>(this.size());
                    for (Object o : keySet()) {
                        result.add(get(o));
                    }
                    return result;
                }
            };


    }


    /**
     * <p>For each entry in data, create component and cause it to be
     * populated with values.</p>
     * @param context the <code>FacesContext</code> for the current request
     * @param data a ResourceBundle
     */
    private void initComponentsFromProperties(FacesContext context,
                                              ResourceBundle data) {

        // populate the map
        for (Enumeration<String> keys = data.getKeys(); keys.hasMoreElements();) {

            String key = keys.nextElement();
            if (key == null) {
                continue;
            }
            // skip secondary keys.
            if (key.contains("_")) {
                continue;
            }
            String value = data.getString(key);
            String componentType = data.getString(key + "_componentType");
            String valueType = data.getString(key + "_valueType");
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("populating map for " + key + "\n" +
                            "\n\tvalue: " + value +
                            "\n\tcomponentType: " + componentType +
                            "\n\tvalueType: " + valueType);
            }
            // create the component for this componentType
            UIComponent component =
                  context.getApplication().createComponent(componentType);
            populateComponentWithValue(context, component, componentType,
                                       value, valueType);
            components.put(key, component);
        }
    }


    /**
     * <p>populate the argument component with values, being sensitive
     * to the possible multi-nature of the values, and to the type of
     * the values.</p>
     * @param context the <code>FacesContext</code> for the current request
     * @param component the <code>UIComponent</code> to populate
     * @param componentType the component type
     * @param value the value
     * @param valueType the value type
     */
    private void populateComponentWithValue(FacesContext context,
                                            UIComponent component,
                                            String componentType,
                                            String value, 
                                            String valueType) {
        Application application = context.getApplication();
        Converter converter = null;

        // if we need a converter, and can have a converter
        if (!"java.lang.String".equals(valueType) &&
            component instanceof ValueHolder) {
            // if so create it,
            try {
                converter =
                      application.createConverter(CarStore.loadClass(valueType,
                                                                     this));
                // add it to our component,
                ((ValueHolder) component).setConverter(converter);
            } catch (ClassNotFoundException cne) {
                FacesMessage errMsg = MessageFactory.getMessage(
                      CONVERTER_ERROR_MESSAGE_ID,
                      valueType);
                throw new IllegalStateException(errMsg.getSummary());
            }

        }

        // if this component is a SelectOne or SelectMany, take special action
        if (isMultiValue(componentType)) {
            // create a UISelectItems instance
            UISelectItems items = new UISelectItems();
            items.setValue(parseStringIntoArrayList(value, converter));
            // add it to the component
            component.getChildren().add(items);
        } else {
            // we have a single value
            if (null != converter) {
                component.getAttributes().put("value",
                                              converter.getAsObject(context,
                                                                    component,
                                                                    value));
            } else {
                component.getAttributes().put("value", value);
            }
        }
    }   

    /**
     * Determines if the component type is a SelectMany or SelectOne.
     * @param componentType the component type
     * @return true of the componentType starts with SelectMany or SelectOne
     */
    private boolean isMultiValue(String componentType) {
        if (null == componentType) {
            return false;
        }
        return (componentType.startsWith("javax.faces.SelectMany") ||
                componentType.startsWith("javax.faces.SelectOne"));
    }


    /*
     * Tokenizes the passed in String which is a comma separated string of
     * option values that serve as keys into the main resources file.
     * For example, optionStr could be "Disc,Drum", which corresponds to
     * brake options available for the chosen car. This String is tokenized
     * and used as key into the main resource file to get the localized option
     * values and stored in the passed in ArrayList.
     */

    /**
     * <p>Tokenizes the passed in String which is a comma separated string of
     * option values that serve as keys into the main resources file.
     * For example, optionStr could be "Disc,Drum", which corresponds to
     * brake options available for the chosen car. This String is tokenized
     * and used as key into the main resource file to get the localized option
     * values and stored in the passed in ArrayList.</p>
     *
     * @param value a comma separated String of values
     * @param converter currently ignored
     * @return a <code>List</code> of <code>SelectItem</code> instances
     *  parsed from <code>value</code>
     */
    public List<SelectItem> parseStringIntoArrayList(String value,
                                         Converter converter) {

        if (value == null) {
            return null;
        }

        String[] splitOptions = value.split(",");
        ArrayList<SelectItem> optionsList =
              new ArrayList<SelectItem>((splitOptions.length) + 1);
        for (String optionKey : splitOptions) {
            String optionLabel;
            try {
                optionLabel = resources.getString(optionKey);
            } catch (MissingResourceException e) {
                // if we can't find a hit, the key is the label
                optionLabel = optionKey;
            }

            if (null != converter) {
                // PENDING deal with the converter case
            } else {
                optionsList.add(new SelectItem(optionKey, optionLabel));
            }
        }
        return optionsList;
    }


    public String updatePricing() {
        getCurrentPrice();
        return null;
    }


    public Integer getCurrentPrice() {
        // go through our options and try to get the prices
        int sum = (Integer) ((ValueHolder) getComponents().get("basePrice")).
              getValue();

        for (Object o : getComponents().keySet()) {

            String key = (String) o;
            UIComponent component = (UIComponent) getComponents().get(key);
            Object value = component.getAttributes().get("value");
            if (null == value || (!(component instanceof UIInput))) {
                continue;
            }

            // if the value is a String, see if we have priceData for it
            if (value instanceof String) {
                try {
                    sum +=
                          Integer.valueOf(priceData.getString((String) value));
                } catch (NumberFormatException e) {
                    // do nothing
                }
            }
            // if the value is a Boolean, look up the price by name
            else if (value instanceof Boolean &&
                     (Boolean) value) {
                try {
                    sum +=
                          Integer.valueOf(priceData.getString(key));
                } catch (NumberFormatException e) {
                    // do nothing
                }
            } else if (value instanceof Number) {
                sum += ((Number) value).intValue();
            }
        }
        Integer result = sum;
        // store the new price into the component for currentPrice
        ((ValueHolder) getComponents().get("currentPrice")).
              setValue(result);
        return result;
    }


    public Map getComponents() {
        return components;
    }


    public Map<String,Object> getAttributes() {
        return attributes;
    }


}
