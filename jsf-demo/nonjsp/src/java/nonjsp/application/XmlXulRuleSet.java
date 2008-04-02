/*
 * $Id: XmlXulRuleSet.java,v 1.2 2005/08/22 22:09:22 ofung Exp $
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

// XmlXulRuleSet.java

package nonjsp.application;

import nonjsp.util.Util;
import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.RuleSetBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;

/**
 * <p>The set of Digester rules required to parse a Faces Xul (Xml)
 * configuration file.
 */
public class XmlXulRuleSet extends RuleSetBase {

    // Log instance for this class
    protected static Log log = LogFactory.getLog(XmlXulRuleSet.class);

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
     *                 should be added.
     */
    public void addRuleInstances(Digester digester) {

        digester.addCallMethod("*/page-url", "setPageUrl", 0);

        digester.addObjectCreate("*/window", "javax.faces.component.UIForm");

        digester.addObjectCreate("*/label", "javax.faces.component.UIOutput");

        digester.addObjectCreate("*/textbox", "javax.faces.component.UIInput");

        digester.addObjectCreate("*/checkbox",
                                 "javax.faces.component.UISelectBoolean");

        digester.addObjectCreate("*/radiogroup",
                                 "javax.faces.component.UISelectOne");

        digester.addObjectCreate("*/menupopup",
                                 "javax.faces.component.UISelectOne");

        digester.addObjectCreate("*/link", "javax.faces.component.UICommand");

        /* 
         * Button no longer needs an ActionRule that creates a default
         * ActionListener because the API's UICommandBase now installs
         * a default ActionListener
         */
        digester.addObjectCreate("*/button", "javax.faces.component.UICommand");

        digester.addObjectCreate("*/image", "javax.faces.component.UIGraphic");

        digester.addFactoryCreate("*/uicomponent", new UIComponentFactory());

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
        digester.addRule("*/image", cRule);

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
        String id = attributes.getValue("id");
        String value = attributes.getValue("value");
        
        // Instantiate the new object and return it
        try {
            cClass = Util.loadClass(className);
            c = (UIComponent) cClass.newInstance();
        } catch (ClassNotFoundException cnf) {
            throw new RuntimeException("Class Not Found:" + cnf.getMessage());
        } catch (InstantiationException ie) {
            throw new RuntimeException("Class Instantiation Exception:" +
                                       ie.getMessage());
        } catch (IllegalAccessException ia) {
            throw new RuntimeException("Illegal Access Exception:" +
                                       ia.getMessage());
        }

        c.setId(id);
        if (c instanceof UIOutput) {
            ((UIOutput) c).setValue(value);
        }
        return c;
    }
}

final class ComponentRule extends Rule {

    // Log instance for this class
    protected static Log log = LogFactory.getLog(ComponentRule.class);

    private BuildComponentFromTag bc;
    private UIComponent root;


    public ComponentRule() {
        super();
        root = null;
    }


    /**
     * This method is invoked when the beginning of the matched
     * Xml element is encountered ;
     *
     * @param attributes The element's attribute list
     */
    public void begin(Attributes attributes) throws Exception {
        UIComponent uic = (UIComponent) digester.peek();
        if (log.isTraceEnabled()) {
            log.trace("component: " + uic.getId());
        }
        AttributesImpl attrs = new AttributesImpl(attributes);
        for (int i = 0; i < attrs.getLength(); i++) {
            String qName = attributes.getQName(i);
            attrs.setLocalName(i, qName);
            attrs.setValue(i, attributes.getValue(qName));
            if (log.isTraceEnabled()) {
                log.trace(
                    "ComponentRule: qName: " + qName + " value: " +
                    attributes.getValue(qName));
            }
        }
        bc.applyAttributesToComponentInstance(uic, attrs);

        if (root == null) {
            root = (UIComponent) digester.peek(digester.getCount() - 1);
        }
        root.getChildren().add(uic);

        //If component is a form, make it the root so that children will be
        //added to it
        if (uic instanceof UIForm) {
            root = uic;
        }
    }


    /**
     * This method is invoked when the end of the matched
     * Xml element is encountered ;
     *
     * @param attributes The element's attribute list
     */
    public void end(String namespace, String name) {
        //Reset the root
        UIComponent uic = (UIComponent) digester.peek();
        if (uic instanceof UIForm) {
            root = (UIComponent) digester.peek(digester.getCount() - 1);
        }
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
        UIComponent uic = (UIComponent) digester.peek();
        AttributesImpl attrs = new AttributesImpl(attributes);
        for (int i = 0; i < attrs.getLength(); i++) {
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
