/*
 * $Id: ConfigParser.java,v 1.48 2003/12/17 23:26:01 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.application.ViewHandlerImpl;
import com.sun.faces.application.NavigationHandlerImpl;
import com.sun.faces.util.Util;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sun.faces.util.Util;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.ServletContext;

import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

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
            } catch (Exception e) {
                String msg = Util.getExceptionMessage(
                    Util.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID, 
                    new Object[]{TYPES[i][2]});
                if (log.isErrorEnabled()) {
                    log.error(
                        msg + ":" + TYPES[i][2] + ":exception:" +
                        e.getMessage());
                }
                throw new FacesException(msg, e);
            }            
        }        
    }

    /**
     * The Digester instance we will use to parse configuration files.
     */
    protected Digester digester = null;
    
    /**
     * <p>The <code>url-pattern</code> mappings of the FacesServlet.</p>
     */ 
    protected List mappings;

    //
    // Constructors and Initializers
    //

    /**
     * Construct a new instance of this configuration parser.
     *
     */
    public ConfigParser(ServletContext servletContext, List mappings) {

        super();
        this.mappings = mappings;
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
     *
     * @exception FileNotFoundException if input is null.
     */

    void parseConfig(InputStream input) throws FileNotFoundException {        
	if (null == input) {
	    throw new FileNotFoundException();
	}
	try {
            digester.clear();
            digester.parse(input);
        } catch (Exception e) {
            Object[] obj = new Object[1];
            obj[0] = (null != input) ? input.toString() : ""; 
	    if (log.isFatalEnabled()) {
		log.fatal(Util.getExceptionMessage(
                Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj)+e.getMessage(), e);
	    }
            throw new FacesException(Util.getExceptionMessage(
                Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj)+e.getMessage(), e);
        } finally {
	    try {
		if (input != null) {
	            input.close();
		}
	    } catch (IOException ee) {
		if (log.isFatalEnabled()) {
	            log.fatal(Util.getExceptionMessage(Util.CANT_CLOSE_INPUT_STREAM_ID), ee);
	        }
		throw new FacesException(ee);
            }
	}
    }

    /**

    * <p>Add to the configuration the
    * config information at the specified configPath.</p>
    *
    * @exception FileNotFoundException if the configPath or the stream
    * obtained from the config path is null.

    */

    // Parse the configuration file at the specified path; 
    void parseConfig(String configPath, ServletContext servletContext) throws FileNotFoundException {
        InputStream input = null;
	if (null == configPath) {
	    throw new FileNotFoundException();
	}

        try {
            input = servletContext.getResourceAsStream(configPath);
	    // Input Stream should be closed when this method completes;
	    this.parseConfig(input);
        } 
	catch (FileNotFoundException fnfe) {
	    throw fnfe;
	} 
	catch (Exception e) {
            Object[] obj = new Object[1];
            obj[0] = configPath;
            throw new FacesException(Util.getExceptionMessage(
                Util.ERROR_OPENING_FILE_ERROR_MESSAGE_ID, obj), e);
        } 
    }

    /*
     *
     * <p>Add to the configuration the
     * config information at the specified InputSource.</p>
     */
    /*
    protected void parseConfig(InputSource input) {
        try {
            digester.clear();
            digester.parse(input);
        } catch (Exception e) {
            Object[] obj = new Object[1];
            obj[0] = input.toString(); 
            throw new FacesException(Util.getExceptionMessage(
                Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj), e);
        } finally {
	    try {
	        input.getByteStream().close();
	    } catch (Exception ee) {
	        if (log.isWarnEnabled()) {
	            log.warn(Util.getExceptionMessage(Util.CANT_CLOSE_INPUT_STREAM_ID), ee);
		}
	    }
	}
    }
    */

    /**
     * @exception FileNotFoundException if the input is null
     */

    void parseConfig(InputSource input) throws FileNotFoundException {
	if (null == input) {
	    throw new FileNotFoundException();
	}
        try {
            digester.clear();
            digester.parse(input);
        } catch (Throwable t) {
            Object[] obj = new Object[1];
            obj[0] = (null != input) ? input.toString() : ""; 
            throw new FacesException(Util.getExceptionMessage(
                Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj)+t.getMessage(), t);
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
        } catch (Exception e) {
            Object[] obj = new Object[1];
            obj[0] = "/com/sun/faces/config/web-facesconfig_1_0.dtd";
            throw new FacesException(Util.getExceptionMessage(
                Util.ERROR_REGISTERING_DTD_ERROR_MESSAGE_ID, obj), e);
        }
        return (digester);

    }


    // Configure the matching rules for the specified Digester instance
    protected void configureRules(Digester digester) {

        configureRulesApplication(digester);
        configureRulesFactory(digester);
        configureRulesConverter(digester);
        configureRulesComponent(digester);
        configureRulesValidator(digester);
        configureRulesManagedBean(digester);
        configureRulesNavigationCase(digester);
        configureRulesRenderKit(digester);
	configureRulesLifecycle(digester);

    }


    // Configure the rules for an <application> element
    protected void configureRulesApplication(Digester digester) {

	String prefix = "faces-config/application";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigApplication");
        digester.addCallMethod(prefix+"/action-listener",
                               "setActionListener", 0);
        digester.addCallMethod(prefix+"/navigation-handler",
                               "setNavigationHandler", 0);
        digester.addCallMethod(prefix+"/message-bundle",
                               "setMessageBundle", 0);
        digester.addCallMethod(prefix+"/view-handler",
                               "setViewHandler", 0);
        digester.addCallMethod(prefix+"/property-resolver",
                               "setPropertyResolver", 0);
        digester.addCallMethod(prefix+"/variable-resolver",
                               "setVariableResolver", 0);
        digester.addCallMethod(prefix+"/locale-config/default-locale",
                               "setDefaultLocale", 0);
        digester.addCallMethod(prefix+"/locale-config/supported-locale",
                               "addSupportedLocale", 0);

	//
        // This custom rule will add application info to the Application instance;
        //
	ApplicationRule aRule = new ApplicationRule(mappings);
        digester.addRule(prefix, aRule);
    }

    // Configure the rules for an <factory> element
    protected void configureRulesFactory(Digester digester) {

	String prefix = "faces-config/factory";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigFactory");
        digester.addCallMethod(prefix+"/application-factory",
                               "setApplicationFactory", 0);
        digester.addCallMethod(prefix+"/faces-context-factory",
                               "setFacesContextFactory", 0);
        digester.addCallMethod(prefix+"/lifecycle-factory",
                               "setLifecycleFactory", 0);
        digester.addCallMethod(prefix+"/render-kit-factory",
                               "setRenderKitFactory", 0);
	//
        // This custom rule will set Factories into the FactoryFinder;
        //
	FactoryRule fRule = new FactoryRule();
        digester.addRule(prefix, fRule);
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

    // Configure the rules for a <managed-bean> element
    protected void configureRulesManagedBean(Digester digester) {

        String prefix = "faces-config/managed-bean";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedBean");
        digester.addCallMethod(prefix + "/managed-bean-name", "setManagedBeanId", 0);
        digester.addCallMethod(prefix + "/managed-bean-class", "setManagedBeanClass", 0);
        digester.addCallMethod(prefix + "/managed-bean-scope", "setManagedBeanScope", 0);
        digester.addCallMethod(prefix + "/managed-bean-create", "setManagedBeanCreate", 0);
	configureRulesManagedBeanListAndMap(digester, prefix);
        configureRulesManagedBeanProperty(digester, prefix + "/managed-property");

        // This custom rule will:
        //     o create managed bean factory using ConfigManagedBean;
        //     o add managed bean info to Application instance.
        //
        ManagedBeansRule mbRule = new ManagedBeansRule();
        digester.addRule(prefix, mbRule);

    }

    // Configure the rules for a <managed-bean><map-entries> and
    // <managed-bean><list-entries> element.
    protected void configureRulesManagedBeanListAndMap(Digester digester, 
						      String base) {
        // these rules set the value category of the individual property
        // value the category can be one of: VALUE, VALUE_BINDING,
        // VALUE_CLASS, NULL_VALUE
        
	ConfigManagedBeanPropertyValueRule cpvRule = 
	    new ConfigManagedBeanPropertyValueRule();
        ConfigManagedBeanPropertyValueTypeRule cpvtRule = 
	    new ConfigManagedBeanPropertyValueTypeRule();
        ConfigManagedBeanPropertyValueNullRule cpvnRule = 
	    new ConfigManagedBeanPropertyValueNullRule();
	
        // these rules set the value category of the individual property
        // map entries the category can be one of: VALUE, VALUE_BINDING,
        // NULL_VALUE.  Note that we don't have a rule for VALUE_CLASS,
        // this is because the value-class/key class thing is handled by
        // method calls directly on the ConfigManagedBeanProperty
        // instance.
	
        ConfigManagedPropertyMapValueRule cpmvRule = 
	    new ConfigManagedPropertyMapValueRule();
        ConfigManagedPropertyMapNullRule cpmnRule = 
	    new ConfigManagedPropertyMapNullRule();
	
	String prefix = base + "/map-entries";
	
	// we use the ConfigManagedBeanProperty bean here as a
	// convenience, even though what we are configuring is a bean
	// itself, not a property of a bean.
	digester.addObjectCreate(prefix, 
			     "com.sun.faces.config.ConfigManagedBeanProperty");
        digester.addSetNext(prefix, "setListOrMap", "com.sun.faces.config.ConfigManagedBeanProperty");

	//
	// for map-entries
	//

        digester.addCallMethod(prefix + "/key-class", "setMapKeyClass", 0);
        digester.addCallMethod(prefix + "/value-class", "setMapValueClass", 0);
	
        configureRulesManagedPropertyMap(digester, prefix + "/map-entry");
        digester.addRule(prefix+"/map-entry/value", cpmvRule);
        digester.addRule(prefix+"/map-entry/null-value", cpmnRule);

	//
	// for list-entries
	//
 	
	prefix = base + "/list-entries";
	// we use the ConfigManagedBeanProperty bean here as a
	// convenience, even though what we are configuring is a bean
	// itself, not a property of a bean.
	digester.addObjectCreate(prefix, 
			     "com.sun.faces.config.ConfigManagedBeanProperty");
        digester.addSetNext(prefix, "setListOrMap", "com.sun.faces.config.ConfigManagedBeanProperty");

        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/value");
        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/value-class");
        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/null-value");
        digester.addRule(prefix+"/value-class", cpvtRule);
        digester.addRule(prefix+"/value", cpvRule);
        digester.addRule(prefix+"/null-value", cpvnRule);
	
    }

    // Configure the rules for a <managed-bean><managed-property> element
    protected void configureRulesManagedBeanProperty(Digester digester, String prefix) {

        // these rules set the value category of the individual property
        // value the category can be one of: VALUE, VALUE_BINDING,
        // VALUE_CLASS, NULL_VALUE

        ConfigManagedBeanPropertyValueRule cpvRule = new ConfigManagedBeanPropertyValueRule();
        ConfigManagedBeanPropertyValueTypeRule cpvtRule = new ConfigManagedBeanPropertyValueTypeRule();
        ConfigManagedBeanPropertyValueNullRule cpvnRule = new ConfigManagedBeanPropertyValueNullRule();

        // these rules set the value category of the individual property
        // map entries the category can be one of: VALUE, VALUE_BINDING,
        // NULL_VALUE

        ConfigManagedPropertyMapValueRule cpmvRule = new ConfigManagedPropertyMapValueRule();
        ConfigManagedPropertyMapNullRule cpmnRule = new ConfigManagedPropertyMapNullRule();

        // these method calls create a property, set the property name, add
        // the property to the parent "ConfigManagedBean" object

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedBeanProperty");
        digester.addSetNext(prefix, "addProperty", "com.sun.faces.config.ConfigManagedBeanProperty");
        digester.addCallMethod(prefix + "/property-name", "setPropertyName", 0);
        digester.addCallMethod(prefix + "/map-entries/key-class", "setMapKeyClass", 0);
        digester.addCallMethod(prefix + "/map-entries/value-class", "setMapValueClass", 0);

        // for "simple" values and value bindings

        configureRulesManagedBeanPropertyValue(digester, prefix + "/value");
        digester.addRule(prefix+"/value", cpvRule);

        // for "simple" null-value

        configureRulesManagedBeanPropertyValue(digester, prefix + "/null-value");
        digester.addRule(prefix+"/null-value", cpvnRule);

        // for value arrays or Lists

        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/list-entries/value");
        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/list-entries/value-class");
        configureRulesManagedBeanPropertyValueArr(digester, prefix + "/list-entries/null-value");
        digester.addRule(prefix+"/list-entries/value-class", cpvtRule);
        digester.addRule(prefix+"/list-entries/value", cpvRule);
        digester.addRule(prefix+"/list-entries/null-value", cpvnRule);

        // for map entries
    
        configureRulesManagedPropertyMap(digester, prefix + "/map-entries/map-entry");
        digester.addRule(prefix+"/map-entries/map-entry/value", cpmvRule);
        digester.addRule(prefix+"/map-entries/map-entry/null-value", cpmnRule);
    }

    // Configure the rules for the values of a <managed-bean><property><value> element
    // This method creates property value object, sets the value, and sets it in the property object

    protected void configureRulesManagedBeanPropertyValue(Digester digester, String prefix) {
        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigManagedBeanPropertyValue");
        digester.addSetNext(prefix, "setValue", "com.sun.faces.config.ConfigManagedBeanPropertyValue");
        digester.addCallMethod(prefix, "setValue", 0);
    }

    // Configure the rules for the list-entries of
    // <managed-bean><property><list-entries><value>,
    // <managed-bean><property><list-entries><value-class>
    // <managed-bean><property><list-entries><null-value> elements.
    // This method creates property value object, sets the value, and
    // adds it to the property list-entries array in the property object
 
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
        digester.addCallMethod(prefix + "/from-action", "setFromAction", 0);
        digester.addCallMethod(prefix + "/from-outcome", "setFromOutcome", 0);
        digester.addCallMethod(prefix + "/to-view-id", "setToViewId", 0);
        digester.addCallMethod(prefix + "/redirect", "setRedirect", 0);

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

    // Configure the rules for a <lifecycle> element
    protected void configureRulesLifecycle(Digester digester) {

	String prefix = "faces-config/lifecycle";

        digester.addObjectCreate(prefix, "com.sun.faces.config.ConfigLifecycle");
        digester.addCallMethod(prefix+"/phase-listener", "setPhaseListener", 0);
	//
        // This custom rule will add a phase listener to the Lifecycle instance;
        //
        LifecycleRule lRule = new LifecycleRule();
        digester.addRule(prefix, lRule);
    }

    private boolean validateTheXml(ServletContext sc) {
        String validateXml = sc.getInitParameter(RIConstants.VALIDATE_XML);
        if (validateXml != null) {
            if (!(validateXml.equals("true")) && !(validateXml.equals("false"))) {
                Object[] obj = new Object[1];
                obj[0] = "validateXml";
                throw new FacesException(Util.getExceptionMessage(
                    Util.INVALID_INIT_PARAM_ERROR_MESSAGE_ID, obj));
            }
        } else {
            validateXml = "false";
        }
        return new Boolean(validateXml).booleanValue();
    }
}

/**
 * These specialized rules set the appropriate value category
 * (VALUE,VALUE_BINDING, NULL_VALUE,VALUE_CLASS) on the managed bean
 * property value object;
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
 * These specialized rules set the appropriate value category
 * (VALUE,VALUE_BINDING,NULL_VALUE) on the managed bean property map
 * object;
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
        Application application = aFactory.getApplication();
        application.addComponent(cc.getComponentType(), cc.getComponentClass());
    }
}

/**
 * This rule adds a <code>converterId</code>,<code>converterClass</code>
 * mapping to the <code>Application</code> instance's internal map.
 */
final class ConvertersRule extends Rule {

    protected static Log log = LogFactory.getLog(ConvertersRule.class);

    public ConvertersRule() {
       super();
    }
    public void end(String namespace, String name) throws Exception {
        ConfigConverter cc = (ConfigConverter)digester.peek();
        ApplicationFactory aFactory =
           (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = aFactory.getApplication();
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
		throw new FacesException(Util.getExceptionMessage(
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
                String message = Util.getExceptionMessage(
                    Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID, obj);
                if (log.isErrorEnabled()) {
                    log.error(message, c);
                }
		throw new FacesException(message);
	    }
	    // store by class
	    application.addConverter(theClass, cc.getConverterClass());
	    // make sure we also handle things of <WRAPPER>.TYPE, where
	    // wrapper is Byte, Boolean, Integer, etc.
	    if (null != 
		(theClass = 
		 getCorrespondingPrimitive(cc.getConverterForClass()))) {
		application.addConverter(theClass, cc.getConverterClass());
	    }
	}
    }

    /**
     * @return the appropriate primitive Class for the argument.  For
     * example, if the argument is <code>java.lang.Boolean</code>, this
     * method will return <code>Boolean.TYPE</code>.
     */

    protected Class getCorrespondingPrimitive(String converterClassName) {
	Class result = null;
	if (null == converterClassName) {
	    return null;
	}
	// an if-else chain is appropriate since the set of primitives
	// in the Java Language is never going to change.
	if (converterClassName.equals("java.lang.Boolean")) {
	    result = Boolean.TYPE;
	}
	else if (converterClassName.equals("java.lang.Byte")) {
	    result = Byte.TYPE;
	}
	else if (converterClassName.equals("java.lang.Double")) {
	    result = Double.TYPE;
	}
	else if (converterClassName.equals("java.lang.Float")) {
	    result = Float.TYPE;
	}
	else if (converterClassName.equals("java.lang.Integer")) {
	    result = Integer.TYPE;
	}
	else if (converterClassName.equals("java.lang.Character")) {
	    result = Character.TYPE;
	}
	else if (converterClassName.equals("java.lang.Short")) {
	    result = Short.TYPE;
	}
	else if (converterClassName.equals("java.lang.Long")) {
	    result = Long.TYPE;
	}
	return result;
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
        Application application = aFactory.getApplication();
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
        Application application = aFactory.getApplication();
        ManagedBeanFactory mbf = new ManagedBeanFactory(cmb);
        if (application instanceof ApplicationImpl) {
            ((ApplicationImpl) application).addManagedBeanFactory(cmb.getManagedBeanId(), mbf);
        }
    }
}

/**
 * This rule sets the Application's Action Listener / Navigation Handler /
 * PropertyResolver / VariableResolver instances;
 */
final class ApplicationRule extends Rule {

    protected static Log log = LogFactory.getLog(ConfigParser.class);
    private List mappings;

    public ApplicationRule(List mappings) {        
        this.mappings = mappings;
    }
    public void end(String namespace, String name) throws Exception {
        ConfigApplication ca = (ConfigApplication)digester.peek();
        ApplicationFactory aFactory =
            (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = aFactory.getApplication();
	Util.doAssert(null != application);
	
	Object returnObject = Util.createInstance(ca.getActionListener());
	if (returnObject != null) {
	    application.setActionListener((ActionListener)returnObject);
	}

	if (null != ca.getMessageBundle()) {
	    application.setMessageBundle(ca.getMessageBundle());
	}
	
	returnObject = Util.createInstance(ca.getNavigationHandler());
	if (returnObject != null) {
	    application.setNavigationHandler((NavigationHandler)returnObject);
	}
	
	returnObject = Util.createInstance(ca.getPropertyResolver());
	if (returnObject != null) {
	    application.setPropertyResolver((PropertyResolver)returnObject);
	}
	
	returnObject = Util.createInstance(ca.getVariableResolver());
	if (returnObject != null) {
	    application.setVariableResolver((VariableResolver)returnObject);
	}
	
	returnObject = Util.createInstance(ca.getViewHandler());
	ViewHandler viewHandler;
	if (returnObject != null) {
	    viewHandler = (ViewHandler) returnObject;
	} else {
	    viewHandler = new ViewHandlerImpl();	    
	} 
	if (viewHandler instanceof ViewHandlerImpl) {
	    ((ViewHandlerImpl) viewHandler).setFacesMapping(mappings);
	}    
	application.setViewHandler(viewHandler);

	String localeStr = null;
	Iterator iter = null;
	if (null !=  (localeStr = ca.getDefaultLocale())) {
	    application.setDefaultLocale(Util.getLocaleFromString(localeStr));
	}
	
	iter = ca.getSupportedLocales().iterator();
	ArrayList supportedLocales = 
	    new ArrayList(ca.getSupportedLocales().size());
	while (iter.hasNext()) {
	    if (null != (localeStr = (String) iter.next())) {
		supportedLocales.add(Util.getLocaleFromString(localeStr));
	    }
	}
	application.setSupportedLocales(supportedLocales);
		
    }
}

/**
 * This rule sets the Factory's Action Listener / Navigation Handler /
 * PropertyResolver / VariableResolver instances;
 */
final class FactoryRule extends Rule {

    protected static Log log = LogFactory.getLog(ConfigParser.class);

    public FactoryRule() {
        super();
    }
    public void end(String namespace, String name) throws Exception {
        ConfigFactory cf = (ConfigFactory)digester.peek();
	String implName = null;

	// PENDING(edburns): I'd rather not have hard coded factory
	// names.
	if (null != (implName = cf.getApplicationFactory())) {
	    FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
				     implName);
	}
	if (null != (implName = cf.getFacesContextFactory())) {
	    FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
				     implName);
	}
	if (null != (implName = cf.getLifecycleFactory())) {
	    FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
				     implName);
	}
	if (null != (implName = cf.getRenderKitFactory())) {
	    FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
				     implName);
	}
	
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
	if (cnr.getFromViewId() == null) {
	    cnc.setFromViewId("*");
	} else {
            cnc.setFromViewId(cnr.getFromViewId());
	}
	String fromAction = cnc.getFromAction();
	String fromOutcome = cnc.getFromOutcome();
	if (fromAction == null) {
	    fromAction = "-";
	}
	if (fromOutcome == null) {
	    fromOutcome = "-";
	}
	cnc.setKey(cnc.getFromViewId()+fromAction+fromOutcome);
        digester.push(cnc);
        ApplicationFactory aFactory =
            (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = aFactory.getApplication();
        Util.doAssert(null != application);
        if (application instanceof ApplicationImpl) {
            ((ApplicationImpl) application).addNavigationCase(cnc);
	}
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
	//
	// If the renderkit does not exist for this renderkitid,
	// create the renderkit.. Otherwise, if the renderkit exists,
	// the renderers will be added to it.
	//
	RenderKit renderKit = renderKitFactory.getRenderKit(null, renderKitId);
	if (renderKit == null) {
            String renderKitClass = cr.getRenderKitClass();
            try {
                Class renderKitClazz = Util.loadClass(renderKitClass, this);
                renderKit = (RenderKit)renderKitClazz.newInstance();
                renderKitFactory.addRenderKit(renderKitId, renderKit);
            } catch (Exception e) {
                throw new FacesException(e);
            }
	}
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
 * This rule sets the Lifecycle's Phase Listener instance;
 */
final class LifecycleRule extends Rule {

    protected static Log log = LogFactory.getLog(ConfigParser.class);

    public LifecycleRule() {
        super();
    }
    public void end(String namespace, String name) throws Exception {
        ConfigLifecycle cl = (ConfigLifecycle)digester.peek();
        LifecycleFactory lFactory =
            (LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = lFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
	Util.doAssert(null != lifecycle);
	
	Object returnObject = Util.createInstance(cl.getPhaseListener());
	if (returnObject != null) {
	    lifecycle.addPhaseListener((PhaseListener)returnObject);
	}
    }
}
