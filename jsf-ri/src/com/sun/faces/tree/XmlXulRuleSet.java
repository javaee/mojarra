/*
 * $Id: XmlXulRuleSet.java,v 1.4 2002/06/18 04:56:33 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// XmlXulRuleSet.java

package com.sun.faces.tree;

import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.RuleSetBase;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;

import java.util.Map;

/**
 * <p>The set of Digester rules required to parse a Faces Xul (Xml)
 * configuration file. 
 */
public class XmlXulRuleSet extends RuleSetBase {

    private BuildComponentFromTag buildComponent = null;

    /**
     * Constructor sets Faces modules needed for building
     * UIComponent instances.
     */
    public XmlXulRuleSet(BuildComponentFromTag bc) {
        buildComponent = bc;
    }

    /**
     * <p>Add the set of Rule instances defined in this RuleSet to the
     * specified <code>Digester</code> instance.
     *
     * @param digester Digester instance to which the new Rule instances
     *  should be added.
     */
    public void addRuleInstances(Digester digester) {

        digester.addCallMethod("*/page-url", "setPageUrl", 0);

        digester.addObjectCreate("*/window", "javax.faces.component.UIForm");
        digester.addSetNext("*/window", "addChild", "javax.faces.component.UIComponent");

        digester.addObjectCreate("*/label", "javax.faces.component.UIOutput");
        digester.addSetNext("*/label", "addChild", "javax.faces.component.UIComponent");

        digester.addObjectCreate("*/textbox", "javax.faces.component.UITextEntry");
        digester.addSetNext("*/textbox", "addChild", "javax.faces.component.UIComponent");

        digester.addObjectCreate("*/checkbox", "javax.faces.component.UISelectBoolean");
        digester.addSetNext("*/checkbox", "addChild", "javax.faces.component.UIComponent");

        digester.addObjectCreate("*/radiogroup", "javax.faces.component.UISelectOne");
        digester.addSetNext("*/radiogroup", "addChild", "javax.faces.component.UIComponent");

        digester.addObjectCreate("*/menupopup", "javax.faces.component.UISelectOne");
        digester.addSetNext("*/menupopup", "addChild", "javax.faces.component.UIComponent");

        digester.addObjectCreate("*/link", "javax.faces.component.UICommand");
        digester.addSetNext("*/link", "addChild", "javax.faces.component.UIComponent");

        digester.addObjectCreate("*/button", "javax.faces.component.UICommand");
        digester.addSetNext("*/button", "addChild", "javax.faces.component.UIComponent");

        digester.addFactoryCreate("*/uicomponent", new UIComponentFactory());
        digester.addSetNext("*/uicomponent", "addChild", "javax.faces.component.UIComponent");

        ComponentRule cRule = new ComponentRule();
        cRule.setBuildComponent(buildComponent);
        digester.addRule("*/window", cRule);
        digester.addRule("*/label", cRule);
        digester.addRule("*/textbox", cRule);
        digester.addRule("*/checkbox", cRule);
        digester.addRule("*/radiogroup", cRule);
        digester.addRule("*/menupopup", cRule);
        digester.addRule("*/link", cRule);
        digester.addRule("*/button", cRule);

        ComponentNestedRule cnRule = new ComponentNestedRule();
        cnRule.setBuildComponent(buildComponent);
        digester.addRule("*/radio", cnRule);
        digester.addRule("*/menuitem", cnRule);
    }

}

final class UIComponentFactory extends AbstractObjectCreationFactory {

    public Object createObject(Attributes attributes) {
        Class cClass = null;
        UIComponent c = null;

        // Identify the name of the class to instantiate
        String className = attributes.getValue("class");
        Assert.assert_it(className != null);
        String id = attributes.getValue("id");
        Assert.assert_it(id != null);
        String value = attributes.getValue("value");
        
        // Instantiate the new object and return it
        try {
            cClass = Util.loadClass(className);
            c = (UIComponent)cClass.newInstance();
        } catch (ClassNotFoundException cnf) {
            throw new RuntimeException("Class Not Found:"+ cnf.getMessage());
        } catch (InstantiationException ie) {
            throw new RuntimeException("Class Instantiation Exception:"+
                ie.getMessage());
        } catch (IllegalAccessException ia) {
            throw new RuntimeException("Illegal Access Exception:"+
            ia.getMessage());
        }

        c.setComponentId(id);
        c.setValue(value);

        return c;
    }
}

final class ComponentRule extends Rule {

    private BuildComponentFromTag bc;

    public ComponentRule() {
        super();
    }

    /**
     * This method is invoked when the beginning of the matched
     * Xml element is encountered ;
     *
     * @param attributes The element's attribute list
     */
    public void begin(Attributes attributes) throws Exception {
        UIComponent uic = (UIComponent)digester.peek();
        AttributesImpl attrs = new AttributesImpl(attributes);
        for (int i=0; i<attrs.getLength(); i++) {
            String qName = attributes.getQName(i);
            attrs.setLocalName(i, qName);
            attrs.setValue(i, attributes.getValue(qName));
        }
        bc.applyAttributesToComponentInstance(uic, attrs);
    }
    
    public void setBuildComponent(BuildComponentFromTag bc) {
        this.bc = bc;
    }
}

/**
 * This processing rule translates nested element names and values
 * (as in Faces "SelectOne" component items.
 * The attributes are set on the UIComponent instance.  
 */
final class ComponentNestedRule extends Rule {

    private BuildComponentFromTag bc;

    public ComponentNestedRule() {
        super();
    }

    /**
     * This method is invoked when the beginning of the matched
     * Xml element is encountered (in this case "property");
     *
     * @param attributes The element's attribute list
     */
    public void begin(Attributes attributes) throws Exception {
        UIComponent uic = (UIComponent)digester.peek();
        AttributesImpl attrs = new AttributesImpl(attributes);
        for (int i=0; i<attrs.getLength(); i++) {
            String qName = attributes.getQName(i);
            attrs.setLocalName(i, qName);
            attrs.setValue(i, attributes.getValue(qName));
        }
        bc.handleNestedComponentTag(uic, "SelectOne_Option", attrs);
    }

    public void setBuildComponent(BuildComponentFromTag bc) {
        this.bc = bc;
    }
}
