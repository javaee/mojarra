/*
 * $Id: XmlXulRuleSet.java,v 1.4 2003/05/01 20:53:01 eburns Exp $
 */

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

// XmlXulRuleSet.java

package nonjsp.tree;

import nonjsp.util.Util;

import javax.faces.FacesException;
import javax.faces.event.ActionListener;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.component.UIOutput;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
     *  should be added.
     */
    public void addRuleInstances(Digester digester) {

        digester.addCallMethod("*/page-url", "setPageUrl", 0);

        digester.addObjectCreate("*/window", "javax.faces.component.UIForm");
        digester.addSetNext("*/window", "addChild", "javax.faces.component.UIComponent");

        digester.addObjectCreate("*/label", "javax.faces.component.UIOutput");
        digester.addSetNext("*/label", "addChild", "javax.faces.component.UIComponent");

        digester.addObjectCreate("*/textbox", "javax.faces.component.UIInput");
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

        digester.addObjectCreate("*/image", "javax.faces.component.UIGraphic");
        digester.addSetNext("*/image", "addChild", "javax.faces.component.UIComponent");

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
        digester.addRule("*/image", cRule);

        ComponentNestedRule cnRule = new ComponentNestedRule();
        cnRule.setBuildComponent(buildComponent);
        digester.addRule("*/radio", cnRule);
        digester.addRule("*/menuitem", cnRule);

        ActionRule aRule = new ActionRule();
        digester.addRule("*/button", aRule);
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
        if ( c instanceof UIOutput) {
            ((UIOutput)c).setValue(value);
        }    
        return c;
    }
}

final class ComponentRule extends Rule {

    // Log instance for this class
    protected static Log log = LogFactory.getLog(ComponentRule.class);

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
        if (log.isTraceEnabled()) {
            log.trace("component: " + uic.getComponentId());
        }
        AttributesImpl attrs = new AttributesImpl(attributes);
        for (int i=0; i<attrs.getLength(); i++) {
            String qName = attributes.getQName(i);
            attrs.setLocalName(i, qName);
            attrs.setValue(i, attributes.getValue(qName));
            if (log.isTraceEnabled()) {
                log.trace("ComponentRule: qName: " + qName + " value: " + attributes.getValue(qName));
            }
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

final class ActionRule extends Rule {

    // Log instance for this class
    protected static Log log = LogFactory.getLog(ActionRule.class);

    public final static String LISTENER = "listener";

    public ActionRule() {
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
        if (log.isTraceEnabled()) {
            log.trace("component: " + uic.getComponentId());
        }
	AttributesImpl attrs = new AttributesImpl(attributes);

	for (int i=0; i<attrs.getLength(); i++) {
            String qName = attributes.getQName(i);
            if (qName.equals(LISTENER)) {
                log.trace("createActionListener");
                ActionListener handler =
                    createActionListener(attributes.getValue(qName));
                    if (log.isTraceEnabled()) {
                        log.trace("component: " + uic.toString());
                    }
	        if (uic instanceof UICommand) {
                    if (log.isTraceEnabled()) {
                        log.trace("addingActionListener: " + handler);
                    }
                    ((UICommand)uic).addActionListener(handler);
	        }
            }
	}
    }

    /**
     * <p>Create and return a new {@link ActionListener} to be registered
     * on our surrounding {@link UIComponent}.</p>
     *
     * @exception FacesException if a new instance cannot be created
     */
    protected ActionListener createActionListener(String className)
        throws FacesException {

        try {
            ClassLoader classLoader =
            Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = this.getClass().getClassLoader();
            }
            Class clazz = classLoader.loadClass(className);
            if (log.isTraceEnabled()) {
                log.trace("CreateActionListener: Class.toString(): " + clazz.toString());
            }

            return ((ActionListener) clazz.newInstance());
        } catch (Exception e) {
            throw new FacesException("Can't create ActionListener: " + e);
        }
    }

}

