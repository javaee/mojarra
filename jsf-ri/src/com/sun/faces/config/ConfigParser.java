/*
 * $Id: ConfigParser.java,v 1.29 2003/08/25 05:39:43 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.application.NavigationHandlerImpl;
import com.sun.faces.application.MessageCatalog;
import com.sun.faces.application.MessageResourcesImpl;
import com.sun.faces.application.MessageTemplate;
import com.sun.faces.util.Util;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.Message;
import javax.faces.application.MessageImpl;
import javax.faces.application.MessageResources;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;


import javax.servlet.ServletContext;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.mozilla.util.Assert;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;

/**
 * <p>Configuration File processing.</p>
 */

public class ConfigParser {

    //
    // Private Constants
    //
    private static final Class[][] TYPES = {
        {
            Boolean.class,
            Boolean.TYPE,
            BooleanConverter.class
        },
        {
            Byte.class,
            Byte.TYPE,
            ByteConverter.class
            
        },
        {
            Character.class,
            Character.TYPE,
            CharacterConverter.class
        },
        {
            Short.class,
            Short.TYPE,
            ShortConverter.class
        },
        {
            Integer.class,
            Integer.TYPE,
            IntegerConverter.class
        },
        {
            Long.class,
            Long.TYPE,
            LongConverter.class
        },
        {
            Float.class,
            Float.TYPE,
            FloatConverter.class
        },
        {
            Double.class,
            Double.TYPE,
            DoubleConverter.class
        }
    };
    
    //
    // Instance Variables
    //

    // Log instance for this class
    protected static Log log = LogFactory.getLog(ConfigParser.class);

    // The public identifier of our DTD
    protected String CONFIG_DTD_PUBLIC_ID =
        "-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.0//EN";
    
    static {
        for (int i = 0; i < TYPES.length; i++) {
            Converter converter = null;
            try {
                converter = (Converter) TYPES[i][2].newInstance();
                ConvertUtils.register(converter, TYPES[i][0]);
                ConvertUtils.register(converter, TYPES[i][1]);
            } catch (Throwable t) {
                String msg = Util.getExceptionMessage(
                    Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, 
                    new Object[]{TYPES[i][2]});
                if (log.isErrorEnabled()) {
                    log.error(
                        msg + ":" + TYPES[i][2] + ":exception:" +
                        t.getMessage());
                }
                throw new RuntimeException(msg);
            }            
        }        
    }

    /**
     * The Digester instance we will use to parse configuration files.
     */
    protected Digester digester = null;

    //
    // Constructors and Initializers
    //

    /**
     * Construct a new instance of this configuration parser.
     *
     */
    public ConfigParser(ServletContext servletContext) {

        super();
        boolean validateXml = validateTheXml(servletContext);
        if (log.isTraceEnabled()) {
            log.trace("Validate Xml:"+validateXml);
        }
        digester = createDigester(validateXml);
        configureRules(digester);
    }


    /**
     *
     * <p>Parse the input stream.</p>
     */

    protected void parseConfig(InputStream input) {        
	try {
            digester.clear();
            digester.parse(input);
        } catch (Throwable t) {
            Object[] obj = new Object[1];
            obj[0] = input.toString(); 
            throw new RuntimeException(Util.getExceptionMessage(
                Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj)+t.getMessage());
        }
	
        try {
            input.close();
        } catch(Throwable t) {
        }
    }

    /**

    * <p>Add to the configuration the
    * config information at the specified configPath.</p>

    */

    // Parse the configuration file at the specified path; 
    protected void parseConfig(String configPath, 
				     ServletContext servletContext) {
        InputStream input = null;

        try {
            input = servletContext.getResourceAsStream(configPath);
        } catch (Throwable t) {
            Object[] obj = new Object[1];
            obj[0] = configPath;
            throw new RuntimeException(Util.getExceptionMessage(
                Util.ERROR_OPENING_FILE_ERROR_MESSAGE_ID, obj));
        }
	this.parseConfig(input);
    }

    /*
     *
     * <p>Add to the configuration the
     * config information at the specified InputSource.</p>
     */
    protected void parseConfig(InputSource input) {
        try {
            digester.clear();
            digester.parse(input);
        } catch (Throwable t) {
            Object[] obj = new Object[1];
            obj[0] = input.toString(); 
            throw new RuntimeException(Util.getExceptionMessage(
                Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj)+t.getMessage());
        }
	
        try {
            input.getByteStream().close();
        } catch(Throwable t) {
        }
    }

    // Create a Digester instance with no rules yet
    protected Digester createDigester(boolean validateXml) {

        Digester digester = new Digester();
        digester.setUseContextClassLoader(true);
        try {
            URL url = this.getClass().getResource("/com/sun/faces/config/web-facesconfig_1_0.dtd");
            digester.register(CONFIG_DTD_PUBLIC_ID, 
			      Util.replaceOccurrences(url.toExternalForm(),
						      " ", "%20"));
            digester.setValidating(validateXml);
        } catch (Throwable t) {
            Object[] obj = new Object[1];
            obj[0] = "/com/sun/faces/config/web-facesconfig_1_0.dtd";
            throw new RuntimeException(Util.getExceptionMessage(
                Util.ERROR_REGISTERING_DTD_ERROR_MESSAGE_ID, obj)+t.getMessage());
        }
        return (digester);

    }


    // Configure the matching rules for the specified Digester instance
    protected void configureRules(Digester digester) {

        configureRulesApplication(digester);
        configureRulesConverter(digester);
        configureRulesMessageResources(digester);
        configureRulesComponent(digester);
        configureRulesValidator(digester);
        configureRulesManagedBean(digester);
        configureRulesNavigationCase(digester);
        configureRulesRenderKit(digester);

    }


    // Configure the rules for an <application> element
    protected void configureRulesApplication(Digester digester) {

	String prefix = "faces-config/application";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigApplication");
        digester.addCallMethod(prefix+"/action-listener",
                               "setActionListener", 0);
        digester.addCallMethod(prefix+"/navigation-handler",
                               "setNavigationHandler", 0);
        digester.addCallMethod(prefix+"/view-handler",
                               "setViewHandler", 0);
        digester.addCallMethod(prefix+"/property-resolver",
                               "setPropertyResolver", 0);
        digester.addCallMethod(prefix+"/variable-resolver",
                               "setVariableResolver", 0);
	//
        // This custom rule will add application info to the Application instance;
        //
	ApplicationRule aRule = new ApplicationRule();
        digester.addRule(prefix, aRule);
    }


    // Configure the rules for a <component> element
    protected void configureRulesComponent(Digester digester) {

        String prefix = "faces-config/component";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigComponent");
        digester.addCallMethod(prefix + "/component-type",
                               "setComponentType", 0);
        digester.addCallMethod(prefix + "/component-class",
                               "setComponentClass", 0);

        // This custom rule will add component info to the Application instance;
        //
        ComponentsRule cRule = new ComponentsRule();
        digester.addRule(prefix, cRule);

    }


    // Configure the rules for a <converter> element
    protected void configureRulesConverter(Digester digester) {

        String prefix = "faces-config/converter";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigConverter");
        digester.addCallMethod(prefix + "/converter-id",
                               "setConverterId", 0);
        digester.addCallMethod(prefix + "/converter-for-class",
                               "setConverterForClass", 0);
        digester.addCallMethod(prefix + "/converter-class",
                               "setConverterClass", 0);

        // This custom rule will add converter info to the Application instance;
        //
        ConvertersRule cRule = new ConvertersRule();
        digester.addRule(prefix, cRule);

    }

    // Configure the rules for a <validator> element
    protected void configureRulesValidator(Digester digester) {

        String prefix = "faces-config/validator";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigValidator");
        digester.addCallMethod(prefix + "/validator-id",
                               "setValidatorId", 0);
        digester.addCallMethod(prefix + "/validator-class",
                               "setValidatorClass", 0);

        // This custom rule will add validator info to the Application instance;
        //
        ValidatorsRule vRule = new ValidatorsRule();
        digester.addRule(prefix, vRule);
    }

    // Configure the rules for a <message-resources> element
    protected void configureRulesMessageResources(Digester digester) {
        String prefix = "faces-config/message-resources";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigMessageResources");
        digester.addCallMethod(prefix + "/message-resources-id",
                               "setMessageResourcesId", 0);
        digester.addCallMethod(prefix + "/message-resources-class",
                               "setMessageResourcesClass", 0);
	configureRulesMessage(digester);

        // This custom rule will add message resource info to the Application instance;
        //
        MessageResourceRule mrRule = new MessageResourceRule();
        digester.addRule(prefix, mrRule);
    }

    protected void configureRulesMessage(Digester digester) {
        String prefix = "faces-config/message-resources/message";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigMessage");
        digester.addSetNext(prefix, "addMessage", "com.sun.faces.config.ConfigMessage");
        digester.addCallMethod(prefix + "/message-id", "setMessageId", 0);
        digester.addCallMethod(prefix + "/message-class","setMessageClass", 0);
	digester.addCallMethod(prefix + "/summary", "addSummary", 2);
	// From this attribute
	digester.addCallParam(prefix + "/summary", 0, "xml:lang"); 
	// From this element body
	digester.addCallParam(prefix + "/summary", 1); 
	digester.addCallMethod(prefix + "/detail", "addDetail", 2);
	// From this attribute
	digester.addCallParam(prefix + "/detail", 0, "xml:lang"); 
	// From this element body
	digester.addCallParam(prefix + "/detail", 1); 
    }

    // Configure the rules for a <managed-bean> element
    protected void configureRulesManagedBean(Digester digester) {

        String prefix = "faces-config/managed-bean";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedBean");
        digester.addCallMethod(prefix + "/managed-bean-name", "setManagedBeanId", 0);
        digester.addCallMethod(prefix + "/managed-bean-class", "setManagedBeanClass", 0);
        digester.addCallMethod(prefix + "/managed-bean-scope", "setManagedBeanScope", 0);
        digester.addCallMethod(prefix + "/managed-bean-create", "setManagedBeanCreate", 0);
        configureRulesManagedBeanProperty(digester, prefix + "/managed-property");

        // This custom rule will:
        //     o create managed bean factory using ConfigManagedBean;
        //     o add managed bean info to Application instance.
        //
        ManagedBeansRule mbRule = new ManagedBeansRule();
        digester.addRule(prefix, mbRule);

    }

    // Configure the rules for a <managed-bean><managed-property> element
    protected void configureRulesManagedBeanProperty(Digester digester, String prefix) {

        // these rules set the value category of the individual property value
        // the category can be one of: value, value-ref, value-class, null-value

        ConfigManagedBeanPropertyValueRule cpvRule = new ConfigManagedBeanPropertyValueRule();
        ConfigManagedBeanPropertyValueRefRule cpvrRule = new ConfigManagedBeanPropertyValueRefRule();
        ConfigManagedBeanPropertyValueTypeRule cpvtRule = new ConfigManagedBeanPropertyValueTypeRule();
        ConfigManagedBeanPropertyValueNullRule cpvnRule = new ConfigManagedBeanPropertyValueNullRule();

        // these rules set the value category of the individual property map entries 
        // the category can be one of: value, value-ref, null-value

        ConfigManagedPropertyMapValueRule cpmvRule = new ConfigManagedPropertyMapValueRule();
        ConfigManagedPropertyMapRefRule cpmrRule = new ConfigManagedPropertyMapRefRule();
        ConfigManagedPropertyMapNullRule cpmnRule = new ConfigManagedPropertyMapNullRule();

        // these method calls create a property, set the property name, add
        // the property to the parent "ConfigManagedBean" object

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedBeanProperty");
        digester.addSetNext(prefix, "addProperty", "com.sun.faces.config.ConfigManagedBeanProperty");
        digester.addCallMethod(prefix + "/property-name", "setPropertyName", 0);
        digester.addCallMethod(prefix + "/map-entries/key-class", "setMapKeyClass", 0);
        digester.addCallMethod(prefix + "/map-entries/value-class", "setMapValueClass", 0);

        // for "simple" values

        configureRulesManagedBeanPropertyValue(digester, prefix + "/value");
        digester.addRule(prefix+"/value", cpvRule);

        // for "simple" value-ref

        configureRulesManagedBeanPropertyValueRef(digester, prefix + "/value-ref");
        digester.addRule(prefix+"/value-ref", cpvrRule);

        // for "simple" null-value

        configureRulesManagedBeanPropertyValueRef(digester, prefix + "/null-value");
        digester.addRule(prefix+"/null-value", cpvnRule);

        // for value arrays

        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/values/value");
        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/values/value-ref");
        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/values/value-class");
        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/values/null-value");
        digester.addRule(prefix+"/values/value-class", cpvtRule);
        digester.addRule(prefix+"/values/value", cpvRule);
        digester.addRule(prefix+"/values/value-ref", cpvrRule);
        digester.addRule(prefix+"/values/null-value", cpvnRule);

        // for map entries
    
        configureRulesManagedPropertyMap(digester, prefix + "/map-entries/map-entry");
        digester.addRule(prefix+"/map-entries/map-entry/value", cpmvRule);
        digester.addRule(prefix+"/map-entries/map-entry/value-ref", cpmrRule);
        digester.addRule(prefix+"/map-entries/map-entry/null-value", cpmnRule);
    }

    // Configure the rules for the values of a <managed-bean><property><value> element
    // This method creates property value object, sets the value, and sets it in the property object

    protected void configureRulesManagedBeanPropertyValue(Digester digester, String prefix) {
        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedBeanPropertyValue");
        digester.addSetNext(prefix, "setValue", "com.sun.faces.config.ConfigManagedBeanPropertyValue");
        digester.addCallMethod(prefix, "setValue", 0);
    }

    // Configure the rules for the values of a <managed-bean><property><value-ref> element
    // This method creates property value object, sets the value, and sets it in the property object

    protected void configureRulesManagedBeanPropertyValueRef(Digester digester, String prefix) {
        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedBeanPropertyValue");
        digester.addSetNext(prefix, "setValue", "com.sun.faces.config.ConfigManagedBeanPropertyValue");
        digester.addCallMethod(prefix, "setValue", 0);
    }

    // Configure the rules for the values of <managed-bean><property><values><value>,
    // <managed-bean><property><values><value-ref>,
    // <managed-bean><property><values><value-class>
    // <managed-bean><property><values><null-value> elements.
    // This method creates property value object, sets the value, and adds it to
    // the property values array in the property object
 
    protected void configureRulesManagedBeanPropertyValueArr(Digester digester, String prefix) {
        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedBeanPropertyValue");
        digester.addCallMethod(prefix, "convertValue");
        digester.addCallMethod(prefix, "setValue", 0);
        digester.addSetNext(prefix, "addValue", "com.sun.faces.config.ConfigManagedBeanPropertyValue");
    }

    protected void configureRulesManagedPropertyMap(Digester digester, String prefix) {
        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedPropertyMap");
        digester.addCallMethod(prefix+"/key", "setKey", 0);
        digester.addCallMethod(prefix+"/value", "setValue", 0);
        digester.addCallMethod(prefix+"/value-ref", "setValue", 0);
        digester.addCallMethod(prefix+"/null-value", "setValue", 0);
        digester.addSetNext(prefix, "addMapEntry", "com.sun.faces.config.ConfigManagedPropertyMap");
    }

    // Configure the rules for a <navigation-rule><navigation-case> element
    protected void configureRulesNavigationCase(Digester digester) {
	String prefix = "faces-config/navigation-rule";
        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigNavigationRule");
        digester.addCallMethod("faces-config/navigation-rule/from-view-id", "setFromViewId", 0);
        prefix = "faces-config/navigation-rule/navigation-case";
        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigNavigationCase");
        digester.addCallMethod(prefix + "/from-action-ref", "setFromActionRef", 0);
        digester.addCallMethod(prefix + "/from-outcome", "setFromOutcome", 0);
        digester.addCallMethod(prefix + "/to-view-id", "setToViewId", 0);

        // This custom rule will....
        //
        NavigationCaseRule ncRule = new NavigationCaseRule();
        digester.addRule(prefix, ncRule);

    }

    protected void configureRulesRenderKit(Digester digester) {
        String prefix = "faces-config/render-kit";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigRenderKit");
        digester.addCallMethod(prefix + "/render-kit-id",
                               "setRenderKitId", 0);
        digester.addCallMethod(prefix + "/render-kit-class",
                               "setRenderKitClass", 0);
	configureRulesRenderer(digester);

	// This custom rule will use RenderKitFactory to create RenderKit
	// instance;
	//
	RenderKitRule rRule = new RenderKitRule();
	digester.addRule(prefix, rRule);
    }

    protected void configureRulesRenderer(Digester digester) {
        String prefix = "faces-config/render-kit/renderer";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigRenderer");
        digester.addSetNext(prefix, "addRenderer", "com.sun.faces.config.ConfigRenderer");
        digester.addCallMethod(prefix + "/renderer-type",
                               "setRendererType", 0);
        digester.addCallMethod(prefix + "/renderer-class",
                               "setRendererClass", 0);
    }


    private boolean validateTheXml(ServletContext sc) {
        String validateXml = sc.getInitParameter(RIConstants.VALIDATE_XML);
        if (validateXml != null) {
            if (!(validateXml.equals("true")) && !(validateXml.equals("false"))) {
                Object[] obj = new Object[1];
                obj[0] = "validateXml";
                throw new RuntimeException(Util.getExceptionMessage(
                    Util.INVALID_INIT_PARAM_ERROR_MESSAGE_ID, obj));
            }
        } else {
            validateXml = "false";
        }
        return new Boolean(validateXml).booleanValue();
    }
   
}

/**
 * These specialized rules set the appropriate value category (value,value-ref,
 * null-value,value-class)  on the managed bean property value object;
 */
final class ConfigManagedBeanPropertyValueRule extends Rule {
    public ConfigManagedBeanPropertyValueRule() {
        super();
    }
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        ConfigManagedBeanPropertyValue cpv = (ConfigManagedBeanPropertyValue)digester.peek();
        cpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE);
    }
}

final class ConfigManagedBeanPropertyValueRefRule extends Rule {
    public ConfigManagedBeanPropertyValueRefRule() {
        super();
    }
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        ConfigManagedBeanPropertyValue cpv = (ConfigManagedBeanPropertyValue)digester.peek();
        cpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE_REF);
    }
}

final class ConfigManagedBeanPropertyValueTypeRule extends Rule {
    public ConfigManagedBeanPropertyValueTypeRule() {
        super();
    }
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        ConfigManagedBeanPropertyValue cpv = (ConfigManagedBeanPropertyValue)digester.peek();
        cpv.setValueCategory(ConfigManagedBeanPropertyValue.VALUE_CLASS);
    }
}

final class ConfigManagedBeanPropertyValueNullRule extends Rule {
    public ConfigManagedBeanPropertyValueNullRule() {
        super();
    }
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        ConfigManagedBeanPropertyValue cpv = (ConfigManagedBeanPropertyValue)digester.peek();
        cpv.setValueCategory(ConfigManagedBeanPropertyValue.NULL_VALUE);
    }
}

/**
 * These specialized rules set the appropriate value category (value,value-ref,null-value)
 * on the managed bean property map object;
 */

final class ConfigManagedPropertyMapValueRule extends Rule {
    public ConfigManagedPropertyMapValueRule() {
        super();
    }
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        ConfigManagedPropertyMap cpm = (ConfigManagedPropertyMap)digester.peek();
        cpm.setValueCategory(ConfigManagedPropertyMap.VALUE);
    }
}

final class ConfigManagedPropertyMapRefRule extends Rule {
    public ConfigManagedPropertyMapRefRule() {
        super();
    }
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        ConfigManagedPropertyMap cpm = (ConfigManagedPropertyMap)digester.peek();
        cpm.setValueCategory(ConfigManagedPropertyMap.VALUE_REF);
    }
}

final class ConfigManagedPropertyMapNullRule extends Rule {
    public ConfigManagedPropertyMapNullRule() {
        super();
    }
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        ConfigManagedPropertyMap cpm = (ConfigManagedPropertyMap)digester.peek();
        cpm.setValueCategory(ConfigManagedPropertyMap.NULL_VALUE);
    }
}

/**
 * This rule adds a <code>componentType</code>,<code>componentClass</code>
 * mapping to the <code>Application</code> instance's internal map.
 */
final class ComponentsRule extends Rule {
    public ComponentsRule() {
        super();
    }
    public void end(String namespace, String name) throws Exception {
        ConfigComponent cc = (ConfigComponent)digester.peek();
        ApplicationFactory aFactory =
           (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application =
           (ApplicationImpl)aFactory.getApplication();
        application.addComponent(cc.getComponentType(), cc.getComponentClass());
    }
}

/**
 * This rule adds a <code>converterId</code>,<code>converterClass</code>
 * mapping to the <code>Application</code> instance's internal map.
 */
final class ConvertersRule extends Rule {
    public ConvertersRule() {
       super();
    }
    public void end(String namespace, String name) throws Exception {
        ConfigConverter cc = (ConfigConverter)digester.peek();
        ApplicationFactory aFactory =
           (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application =
           (ApplicationImpl)aFactory.getApplication();
	String idOrClassName = null;
	// the DTD states that converter-id and converter-for-class are
	// mutually exclusive, so we're safe here.

	// If we have a converter-id
	if (null != (idOrClassName = cc.getConverterId())) {
	    // store by id
	    application.addConverter(cc.getConverterId(), cc.getConverterClass());
	}
	else {
	    // do we have a converter-for-class?
	    if (null == (idOrClassName = cc.getConverterForClass())) {
		Object[] obj = new Object[1];
		obj[0] = "converter: " + cc.getConverterClass();
		throw new RuntimeException(Util.getExceptionMessage(
								    Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj));
	    }
	    Class theClass = null;
	    // is it valid?
	    try {
		theClass = Util.loadClass(idOrClassName, this);
	    }
	    catch (ClassNotFoundException c) {
		Object[] obj = new Object[1];
		obj[0] = "converter: " + cc.getConverterClass() + " " + 
		    idOrClassName;
		throw new RuntimeException(Util.getExceptionMessage(
								    Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj), c);
	    }
	    // store by class
	    application.addConverter(theClass, cc.getConverterClass());
	}
    }
}

/**
 * This rule adds a <code>validatorId</code>,<code>validatorClass</code>
 * mapping to the <code>Application</code> instance's internal map.
 */
final class ValidatorsRule extends Rule {
    public ValidatorsRule() {
       super();
    }
    public void end(String namespace, String name) throws Exception {
        ConfigValidator cc = (ConfigValidator)digester.peek();
        ApplicationFactory aFactory =
           (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application =
           (ApplicationImpl)aFactory.getApplication();
       application.addValidator(cc.getValidatorId(), cc.getValidatorClass());
    }
}

/**
 * This rule creates a <code>ManagedBeanFactory</code> instance
 * and adds it to the <code>Application</code> implementation.
 */
final class ManagedBeansRule extends Rule {
    public ManagedBeansRule() {
        super();
    }
    public void end(String namespace, String name) throws Exception {
        ConfigManagedBean cmb = (ConfigManagedBean)digester.peek();
        ApplicationFactory aFactory =
            (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application =
            (ApplicationImpl)aFactory.getApplication();
        ManagedBeanFactory mbf = new ManagedBeanFactory(cmb);
        application.addManagedBeanFactory(cmb.getManagedBeanId(), mbf);
    }
}

/**
 * This rule sets the Application's Action Listener / Navigation Handler /
 * PropertyResolver / VariableResolver instances;
 */
final class ApplicationRule extends Rule {

    protected static Log log = LogFactory.getLog(ConfigParser.class);

    public ApplicationRule() {
        super();
    }
    public void end(String namespace, String name) throws Exception {
        ConfigApplication ca = (ConfigApplication)digester.peek();
        ApplicationFactory aFactory =
            (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application =
            (ApplicationImpl)aFactory.getApplication();
	Assert.assert_it(null != application);
	
	Object returnObject = createInstance(ca.getActionListener());
	if (returnObject != null) {
	    application.setActionListener((ActionListener)returnObject);
	}

	returnObject = createInstance(ca.getNavigationHandler());
	if (returnObject != null) {
	    application.setNavigationHandler((NavigationHandler)returnObject);
	}

	returnObject = createInstance(ca.getPropertyResolver());
	if (returnObject != null) {
	    application.setPropertyResolver((PropertyResolver)returnObject);
	}

	returnObject = createInstance(ca.getVariableResolver());
	if (returnObject != null) {
	    application.setVariableResolver((VariableResolver)returnObject);
	}

	returnObject = createInstance(ca.getViewHandler());
	if (returnObject != null) {
	    application.setViewHandler((ViewHandler)returnObject);
	}
    }

    protected Object createInstance(String className) {
	Class clazz = null;
	Object returnObject = null;
	if (className != null) {
            try {
	        clazz = Util.loadClass(className, this);
	        if (clazz != null) {
	            returnObject = clazz.newInstance();
	        }
	    } catch (Throwable t) {
	        Object[] params = new Object[1];
	        params[0] = className;
	        String msg = Util.getExceptionMessage(
		    Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, params);
	        if (log.isErrorEnabled()) {
	            log.error(msg + ":" + className + ":exception:"+
		        t.getMessage());
                }
	    }
        }
	return returnObject;
    }
}

/**
 * This rule gets the Navigation Handler instance from the Application instance.
 * Then it sets Navigation Case info in the Navigation Handler instance...
 */
final class NavigationCaseRule extends Rule {

    protected static Log log = LogFactory.getLog(ConfigParser.class);

    public NavigationCaseRule() {
        super();
    }
    public void end(String namespace, String name) throws Exception {
        ConfigNavigationCase cnc = (ConfigNavigationCase)digester.pop();
        ConfigNavigationRule cnr = (ConfigNavigationRule)digester.peek();
        cnc.setFromViewId(cnr.getFromViewId());
        digester.push(cnc);
        ApplicationFactory aFactory =
            (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application =
            (ApplicationImpl)aFactory.getApplication();
        Assert.assert_it(null != application);
        NavigationHandlerImpl navHandler = (NavigationHandlerImpl)application.
            getNavigationHandler();
        Assert.assert_it(null != navHandler);
        navHandler.addNavigationCase(cnc);
    }
}

/**
 *  This rule gets a <code>RenderKit</code> instance using the
 *  <code>RenderKitFactory</code> with the <code>RenderKitId</code>
 *  from the <code>ConfigRenderKit</code> instance.  It extracts
 *  <code>ConfigRenderer</code> instances and uses the information
 *  to create <code>Renderer</code> instances.  The <code>Renderer</code>
 *  instances are added to the <code>RenderKit</code>.
 *  This rule executes when the ending <code></render-kit></faces-config></code>
 *  XML element is encountered.
 */
final class RenderKitRule extends Rule {

    protected static Log log = LogFactory.getLog(ConfigParser.class);

    public RenderKitRule() {
        super();
    }
    public void end(String namespace, String name) throws Exception {
        ConfigRenderKit cr = (ConfigRenderKit)digester.peek();
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
	String renderKitId = cr.getRenderKitId();
        RenderKit renderKit =
            renderKitFactory.getRenderKit(renderKitId);
        Map renderersMap = cr.getRenderers();
        Iterator rendererIds = renderersMap.keySet().iterator();
        while (rendererIds.hasNext()) {
            String rendererId = (String)rendererIds.next();
            if (log.isTraceEnabled()) {
                log.trace("  Adding Renderer " + rendererId);
            }
            ConfigRenderer configRenderer = (ConfigRenderer)
                renderersMap.get(rendererId);
            String rendererClass = configRenderer.getRendererClass();
            try {
                Class rendererClazz = Util.loadClass(rendererClass, this);
                Renderer renderer = (Renderer)rendererClazz.newInstance();
                renderKit.addRenderer(rendererId, renderer);
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }
    }
}

/**
 * This rule performs the following functionality for 
 * <code>MessageResource</code> handling:
 *     1. gets the <code>ConfigMessageResources</code> instance from the
 *        Digester stack.
 *     2. Uses the <code>messageResourcesId</code> to get a 
 *        <code>MessageResources</code> instance from the <code>Application</code>
 *        implementation.
 *     3. Iterates through each of the <code>ConfigMessage</code> instances
 *        (contained in <code>ConfigMessageResources</code>, and creates a 
 *        <code>MessageTemplate</code> instance.
 *     4. Each <code>MessageTemplate</code> is added to a <code>MessageCatalog</code>
 *        based on <code>Locale</code>.
 *  This rule executes when the ending <code></message-resources></faces-config></code>
 *  XML element is encountered.
 */
final class MessageResourceRule extends Rule {

    protected static Log log = LogFactory.getLog(ConfigParser.class);

    public MessageResourceRule() {
        super();
    }
    public void end(String namespace, String name) throws Exception {
        ApplicationFactory aFactory =
            (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application =
            (ApplicationImpl)aFactory.getApplication();
	ConfigMessageResources mr = (ConfigMessageResources)digester.peek();
	String messageResourcesId = mr.getMessageResourcesId();
	Assert.assert_it(null != messageResourcesId);
        MessageResources messageResources = application.getMessageResources(messageResourcesId);
	if (messageResources instanceof MessageResourcesImpl) {
	    //
	    // Iterate through each ConfigMessage and create a MessageTemplate;
	    // For each ConfigMessage, determine the language for summary and detail,
	    // and catalog the message by language.
	    //
	    Map messages = mr.getMessages();
	    Iterator iter = messages.keySet().iterator();
	    while (iter.hasNext()) {
                ConfigMessage configMessage = (ConfigMessage)messages.get(iter.next());
		String messageClazz = configMessage.getMessageClass();
		if (messageClazz ==null) {
		    messageClazz = MessageImpl.class.getName();
		}
		MessageTemplate messageTemplate = null;
		if (messageClazz.equals(MessageImpl.class.getName())) {
		    Map summaries = configMessage.getSummaries();
		    Map details = configMessage.getDetails();
	            Iterator langIter = summaries.keySet().iterator();
		    Locale locale = null;
		    //
		    // Determine the Locale..  If no langage is specified in the config
		    // file, attempt to get it from FacesContext;
		    //
		    while (langIter.hasNext()) {
			String xmlLocale = (String)langIter.next();
			Assert.assert_it(null != xmlLocale);
			locale = getLocale(xmlLocale);
		        messageTemplate = new MessageTemplate();
			messageTemplate.setMessageId(configMessage.getMessageId());
			messageTemplate.setLocale(locale);
			messageTemplate.setSummary((String)summaries.get(xmlLocale));
			messageTemplate.setDetail((String)details.get(xmlLocale));
			//
			// Default to ERROR if not in config file..
			//
			if (configMessage.getSeverity() > 0) { 
			    messageTemplate.setSeverity(configMessage.getSeverity());
			} else {
			    messageTemplate.setSeverity(Message.SEVERITY_ERROR);
			}
			//
			// See if a catalog exists for the locale, and if it does,
			// use it to add the message - otherwise, create the catalog 
			// first.
			//
                        MessageCatalog catalog = 
			    ((MessageResourcesImpl)messageResources).findCatalog(locale);
			if (catalog == null) {
			    catalog = new MessageCatalog(locale);
			    catalog.addMessage(messageTemplate);
			    ((MessageResourcesImpl)messageResources).addCatalog(locale, catalog);
			} else {
			    catalog.addMessage(messageTemplate);
			}
		    }
		}
            }
	}
    }
    
    // W3C XML specification refers to IETF RFC 1766 for language code structure,
    // therefore the value for the xml:lang attribute should be in the form of
    // language or language-country or language-country-variant.
    private static Locale getLocale(String locale) {       
        String language = null;
        String country = null;
        String variant = null;
        int dash = locale.indexOf('-');
        if (dash < 0) {
            language = locale;
            country = "";
            variant = "";
        } else {
            language = locale.substring(0, dash);
            country = locale.substring(dash + 1);
            int vDash = country.indexOf('-');
            if (vDash > 0) {
                String cTemp = country.substring(0, vDash);
                variant = country.substring(vDash + 1);
                country = cTemp;
            } else {
                variant = "";
            }
        }
        return new Locale(language, country, variant);
    }
}
