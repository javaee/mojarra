/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
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

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.ConvertibleValueHolder;
import javax.faces.component.ValueHolder;
import javax.faces.component.UISelectItems;
import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import javax.faces.model.SelectItem;

import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * <p>This bean encapsulates a car model, including pricing and package
 * choices.  The system allows the user to customize the properties of
 * this bean with the help of the {@link CarCustomizer}.</p>
 *
 * <h3>Data Access</h3>
 *
 * <p>This is the only bean in the system that has complicated access to
 * the persistent store of data.  In the present implementation, this
 * persistent store is in <code>ResourceBundle</code> instances.</p>
 * 
 * <p>There are three data source <code>ResourceBundle</code> files
 * used:</p>
 *
 * 	<ol>
 *
 * 	  <li><p><code>&lt;ModelName&gt;</code></p>
 *
 *        <p>This contains the localized content for this model.  There
 *        is a variant of this file for each supported locale, for
 *        example, <code>Jalopy_de.properties</code></p>
 *
 *         </li>
 *
 * 	  <li><p><code>&lt;Common_properties&gt;</code></p>
 *
 *        <p>This contains the localized content common to all
 *        models.</p>
 *
 *         </li>
 *
 * 	  <li><p><code>&lt;ModelName_options&gt;</code></p>
 *
 *        <p>This contains the non-localized content for this model,
 *        including the non-localized options.  There is only one
 *        variant of this file for all locales for example,
 *        <code>Jalopy_options.properties</code></p>
 *
 *         </li>
 *
 *	</ol>
 *
 * <p>All files conform to the following convention:</p>
 *
 * <code><pre>
 * key
 * key_componentType
 * key_valueType
 *</pre></code>
 *
 * <p>Where <code>key</code> is the name of an attribute of this car.
 * For example, <code>basePrice</code>, or <code>description</code>.
 * <code>key_componentType</code> is the component type of the
 * <code>UIComponent</code> subclass to be used to represent this
 * attribute in the page, for example <code>SelectManyMenu</code>.
 * <code>key_valueType</code> is the data type of the value of the
 * <code>UIComponent</code>, for example <code>java.lang.Integer</code>.
 * For all non-String valueTypes.</p>
 *
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
 *
 *
 */

public class CarBean extends Object {

    protected static final Log log = LogFactory.getLog(CarBean.class);

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
    
    /**
     * Localized labels
     */

    private ResourceBundle resources = null;

    /**
     * Price data
     */
    private ResourceBundle priceData = null;

    /**
     * Keys: String attribute name, such as engine. Values: UIComponent
     * for the attribute
     *
     */

    private Map components = null;

    /**
     * Keys: String attribute name, such as engine. Values: String value
     * of the component named by key in our components Map.
     */

    private Map attributes = null;
    
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
     *
     * <p>Create a wrapper <code>Map</code> around the components
     * <code>Map</code> that exposes the String converted value of each
     * component.</p>
     */
    
    private void init(String bundleName) {
	FacesContext context = FacesContext.getCurrentInstance();
	ResourceBundle data = null;
	Enumeration keys = null;
	components = new HashMap();

	// load the labels
	resources = ResourceBundle.getBundle(CarStore.CARSTORE_PREFIX +
					     ".bundles.Resources", 
				     context.getViewRoot().getLocale());

	// load the prices
	priceData = ResourceBundle.getBundle(CarStore.CARSTORE_PREFIX +
					     ".bundles.OptionPrices");
	
	// populate the locale-specific information
	if (log.isDebugEnabled()) {
	    log.debug("Loading bundle: " + bundleName + ".");
	}
	data = ResourceBundle.getBundle(bundleName, 
					context.getViewRoot().getLocale());
	if (log.isDebugEnabled()) {
	    log.debug("Bundle " + bundleName + 
		      " loaded. Reading properties...");
	}
	initComponentsFromProperties(context, data);
	if (log.isDebugEnabled()) {
	    log.debug("done.");
	}

	// populate the non-locale-specific information common to all cars
	if (log.isDebugEnabled()) {
	    log.debug("Loading bundle: Common_options.");
	}
	data = ResourceBundle.getBundle(CarStore.CARSTORE_PREFIX + 
					".bundles.Common_options");
	if (log.isDebugEnabled()) {
	    log.debug("Bundle Common_options loaded. Reading properties...");
	}
	initComponentsFromProperties(context, data);
	if (log.isDebugEnabled()) {
	    log.debug("done.");
	}

	// populate the non-locale-specific information specific to each car
	if (log.isDebugEnabled()) {
	    log.debug("Loading bundle: " + bundleName + "_options.");
	}
	data = ResourceBundle.getBundle(bundleName + "_options");
	if (log.isDebugEnabled()) {
	    log.debug("Bundle " + bundleName + 
		      "_options loaded. Reading properties...");
	}
	initComponentsFromProperties(context, data);
	if (log.isDebugEnabled()) {
	    log.debug("done.");
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
		public java.util.Set entrySet() { 
		    throw new UnsupportedOperationException();
		}
		public boolean equals(Object o) {
		    throw new UnsupportedOperationException();
		}
		public Object get(Object key) {
		    UIComponent component = null;
		    Converter converter = null;
		    Object result = null;
		    if (null == key) {
			return null;
		    }
		    if (null != (component = (UIComponent) 
				 CarBean.this.components.get(key))) {
			// if this component can have a Converter
			if (component instanceof ConvertibleValueHolder) {
			    // try to get it
			    converter = ((ConvertibleValueHolder)component).
				getConverter();
			}
			// if this component can have a Value
			if (component instanceof ValueHolder) {
			    result = ((ValueHolder)component).getValue();
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
		public java.util.Set keySet() { 
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
		public java.util.Collection values() {
		    ArrayList result = new ArrayList();
		    Iterator keys = keySet().iterator();
		    while (keys.hasNext()) {
			result.add(get(keys.next()));
		    }
		    return result;
		}
	    };
	
    }

    /**
     * <p>For each entry in data, create component and cause it to be
     * populated with values.</p>
     *  
     */

    private void initComponentsFromProperties(FacesContext context,
					      ResourceBundle data) {
	Application application = context.getApplication();
	Enumeration keys = data.getKeys();
	String 
	    key = null,
	    value = null,
	    componentType = null,
	    valueType = null;
	UIComponent component = null;

	// populate the map
	while (keys.hasMoreElements()) {
	    key = (String) keys.nextElement();
	    if (key == null) {
		continue;
	    }
	    // skip secondary keys.
	    if (-1 != key.indexOf("_")) {
		continue;
	    }
	    value = data.getString(key);
	    componentType = data.getString(key + "_componentType");
	    valueType = data.getString(key + "_valueType");
	    if (log.isDebugEnabled()) {
		log.debug("populating map for " + key + "\n" + 
			  "\n\tvalue: " + value +
			  "\n\tcomponentType: " + componentType + 
			  "\n\tvalueType: " + valueType);
	    }
	    // create the component for this componentType
	    component = application.createComponent(componentType);
	    populateComponentWithValue(context, component, componentType,
				       value, valueType);
	    components.put(key, component);
	}
    }

    /**
     * <p>populate the argument component with values, being sensitive
     * to the possible multi-nature of the values, and to the type of
     * the values.</p>
     * 
     */

    private void populateComponentWithValue(FacesContext context,
					    UIComponent component, 
					    String componentType,
					    String value, String valueType) {
	Application application = context.getApplication();
	ValueHolder valueHolder = null;
	Converter converter = null;
	UISelectItems items = null;

	// make sure our component is a ValueHolder
	if (component instanceof ValueHolder) {
	    valueHolder = (ValueHolder) component;
	}
	else {
	    // the component must be a ValueHolder
	    return;
	}

	// if we need a converter, and can have a converter
	if (!valueType.equals("java.lang.String") && 
	    component instanceof ConvertibleValueHolder) {
	    // if so create it,
	    try {
		converter = 
		    application.createConverter(CarStore.loadClass(valueType,
								   this));
	    }
	    catch (ClassNotFoundException cne) {
                FacesMessage errMsg = MessageFactory.getMessage(
                    CONVERTER_ERROR_MESSAGE_ID,
                    (new Object[] { valueType }));
		throw new IllegalStateException(errMsg.getSummary());
	    }
	    // add it to our component,
	    ((ConvertibleValueHolder)component).setConverter(converter);
	}
	
	// if this component is a SelectOne or SelectMany, take special action
	if (isMultiValue(componentType)) {
	    // create a UISelectItems instance
	    items = new UISelectItems();
	    items.setValue(parseStringIntoArrayList(context, component, 
						    value, valueType, 
						    converter));
	    // add it to the component
	    component.getChildren().add(items);
	}
	else {
	    // we have a single value
	    if (null != converter) {
		valueHolder.setValue(converter.getAsObject(context, component, 
							   value));
	    }
	    else {
		valueHolder.setValue(value);
	    }
	}
    }

    /**
     * @return true if componentType starts with SelectMany or SelectOne
     *
     */
    private boolean isMultiValue(String componentType) {
	if (null == componentType) {
	    return false;
	}
	return (componentType.startsWith("SelectMany") || 
		componentType.startsWith("SelectOne"));
    }

    /**
     * Tokenizes the passed in String which is a comma separated string of 
     * option values that serve as keys into the main resources file.
     * For example, optionStr could be "Disc,Drum", which corresponds to
     * brake options available for the chosen car. This String is tokenized
     * and used as key into the main resource file to get the localized option
     * values and stored in the passed in ArrayList.
     */
    public ArrayList parseStringIntoArrayList(FacesContext context,
					      UIComponent component,
					      String value, 
					      String valueType, 
					      Converter converter) {
        ArrayList optionsList = null;
        int i = 0;
	Object optionValue = null;
	String 
	    optionKey = null,
	    optionLabel = null;
	Map nonLocalizedOptionValues = null;
        
        if ( value == null ) {
            return null;
        }     
        StringTokenizer st = new StringTokenizer(value, ",");
        optionsList = new ArrayList((st.countTokens())+1);
        while (st.hasMoreTokens()) {
            optionKey = st.nextToken();
	    try {
                optionLabel = resources.getString(optionKey);
            } 
	    catch (MissingResourceException e) {
		// if we can't find a hit, the key is the label
		optionLabel = optionKey;
	    }

	    if (null != converter) {
		// PENDING deal with the converter case
	    }
	    else {
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
	int sum = ((Integer)((ValueHolder)getComponents().get("basePrice")).
		   getValue()).intValue();
	Iterator iter = getComponents().keySet().iterator();
	String key = null;
	Object value = null;
	UIComponent component = null;
	while (iter.hasNext()) {
	    key = (String) iter.next();
	    component = (UIComponent) getComponents().get(key);
	    value = ((ValueHolder)component).getValue();
	    if (null == value || (!(component instanceof UIInput))) {
		continue;
	    }
	    
	    // if the value is a String, see if we have priceData for it
	    if (value instanceof String) {
		try {
		    sum += 
			Integer.valueOf(priceData.getString((String)value)).intValue();
		}
		catch (NumberFormatException e) {
		}
	    }
	    // if the value is a Boolean, look up the price by name
	    else if (value instanceof Boolean && 
		     ((Boolean)value).booleanValue()) {
		try {
		    sum += 
			Integer.valueOf(priceData.getString(key)).intValue();
		}
		catch (NumberFormatException e) {
		}
	    }
	    else if (value instanceof Number) {
		sum += ((Number)value).intValue();
	    }
	}
	Integer result = new Integer(sum);
	// store the new price into the component for currentPrice
	((ValueHolder)getComponents().get("currentPrice")).
	    setValue(result);
	return result;
    }
	

    public Map getComponents() {
	return components;
    }

    public Map getAttributes() {
	return attributes;
    }


}
