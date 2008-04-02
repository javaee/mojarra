/*
 * $Id: BuildComponentFromTagImpl.java,v 1.1 2004/05/20 21:24:51 rkitain Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
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

// BuildComponentFromTagImpl.java

package nonjsp.application;

import nonjsp.util.Util;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.el.MethodBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * <B>BuildComponentFromTagImpl</B> is a class ...
 *
 * Copy of com.sun.faces.tree.BuildComponentFromTagImpl in order to remove
 * demo dependancy on RI.
 *
 * @version $Id: BuildComponentFromTagImpl.java,v 1.1 2004/05/20 21:24:51 rkitain Exp $
 */

public class BuildComponentFromTagImpl extends Object
    implements BuildComponentFromTag {

    //
    // Protected Constants
    //

    protected static String PARENT = "faces.parent:";

    protected static String PARENT_SELECTONE = PARENT +
        "javax.faces.UISelectOne";

    /**
     * stores model value of the form temporarily to support implicit mapping
     * of components to properties in the bean.
     */
    protected static String FORM_MODELREF = "faces.FORM_MODELREF";

    protected static Log log = LogFactory.getLog(
        BuildComponentFromTagImpl.class);

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    /**
     * Store the mapping between shortTagName and component class
     */

    protected Hashtable classMap;


    //
    // Constructors and Initializers    
    //

    public BuildComponentFromTagImpl() {
        super();
        initializeClassMap();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    /**
     * This is a total RAD hack.
     */

    private void initializeClassMap() {
        classMap = new Hashtable(30);
        // PENDING(edburns): read this from a persistent store
        classMap.put("Form", "javax.faces.component.UIForm");
        classMap.put("Command_Button", "javax.faces.component.UICommand");
        classMap.put("Command_Link", "javax.faces.component.UICommand");
        classMap.put("SelectBoolean_Checkbox",
                     "javax.faces.component.UISelectBoolean");
        classMap.put("RadioGroup", "javax.faces.component.UISelectOne");
        classMap.put("SelectOne_Radio", PARENT_SELECTONE);
        classMap.put("SelectOne_Listbox", "javax.faces.component.UISelectOne");
        classMap.put("SelectOne_Option", PARENT_SELECTONE);
        classMap.put("Output_Text", "javax.faces.component.UIOutput");
        classMap.put("TextEntry_Input", "javax.faces.component.UIInput");
        classMap.put("TextEntry_Secret", "javax.faces.component.UIInput");
        classMap.put("TextEntry_TextArea", "javax.faces.component.UIInput");
        classMap.put("Errors", "javax.faces.component.UIOutput");
    }


    protected boolean isSupportedTag(String shortTagName) {
        return (null != classMap.get(shortTagName));
    }


    /**
     * @return the property name in the UIComponent class that corresponds to
     *         the attrName.
     */
    private String mapAttrNameToPropertyName(String attrName) {
        if (null == attrName) {
            return attrName;
        }

        if (attrName.equals("converter")) {
            attrName = "converterReference";
        }

        if (attrName.equals("selectedValueModel")) {
            attrName = "selectedModelReference";
        }
        return attrName;
    }


    /**
     * @return the property name in the UIComponent class that corresponds to
     *         the attrName.
     */
    private boolean attrRequiresSpecialTreatment(String attrName) {
        boolean result = false;

        if (null == attrName) {
            return result;
        }

        if (attrName.equals("valueChangeListener") ||
            attrName.equals("actionListener") ||
            attrName.equals("action") ||
            attrName.equals("required") ||
            attrName.equals("format") ||
            attrName.equals("rangeMaximum") ||
            attrName.equals("lengthMaximum") ||
            attrName.equals("value")) {
            result = true;
        }
        return result;
    }


    private void handleSpecialAttr(UIComponent child, String attrName,
                                   String attrValue)
        throws IllegalAccessException,
        IllegalArgumentException,
        InvocationTargetException,
        NoSuchMethodException {

        Class[] stringArg = {String.class};
        Method attrMethod = null;
        Object objValue = attrValue;

        if (attrName.equals("valueChangeListener")) {
            try {
                attrMethod =
                    child.getClass().getMethod("addValueChangeListener",
                                               stringArg);
            } catch (SecurityException e) {
                log.trace("handleSpecialAttr: " + e);
            }
        } else if (attrName.equals("actionListener")) {
            try {
                attrMethod =
                    child.getClass().getMethod("addActionListener", stringArg);
            } catch (SecurityException e) {
                log.trace("handleSpecialAttr: " + e);
            }
        } else if (attrName.equals("action")) {
            try {
                Class[] mbArgs = {MethodBinding.class};
                attrMethod = child.getClass().getMethod("setAction",
                                                        mbArgs);
                objValue = new ConstantMethodBinding(attrValue);
            } catch (SecurityException e) {
                log.trace("handleSpecialAttr: " + e);
            }
        } else if ((child instanceof UICommand) && attrName.equals("value")) {
            ((UICommand) child).setValue(attrValue);
            return;
        }

        Object[] args = {objValue};
        if (null != attrMethod) {
            attrMethod.invoke(child, args);
        }
    }


    // 
    // Methods from BuildComponentFromTag
    //


    public UIComponent createComponentForTag(String shortTagName) {
        UIComponent result = null;
        if (!tagHasComponent(shortTagName)) {
            return result;
        }

        String className = (String) classMap.get(shortTagName);
        Class componentClass;

        // PENDING(edburns): this can be way optimized
        try {
            componentClass = Util.loadClass(className);
            result = (UIComponent) componentClass.newInstance();
        } catch (IllegalAccessException iae) {
            throw new RuntimeException("Can't create instance for " +
                                       className + ": " + iae.getMessage());
        } catch (InstantiationException ie) {
            throw new RuntimeException("Can't create instance for " +
                                       className + ": " + ie.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find class for " +
                                       className + ": " + e.getMessage());
        }

        return result;
    }


    public boolean tagHasComponent(String shortTagName) {
        boolean result = false;
        String value;

        // Return true if the classMap has an entry for shortTagName, and
        // the entry does not start with the string PARENT.

        if (null != (value = (String) classMap.get(shortTagName))) {
            result = 0 != value.indexOf(PARENT);
        }
        return result;
    }


    public boolean isNestedComponentTag(String shortTagName) {
        boolean result = false;
        String value;

        // Return true if the classMap has an entry for shortTagName, and
        // the entry DOES start with the string PARENT.

        if (null != (value = (String) classMap.get(shortTagName))) {
            result = 0 == value.indexOf(PARENT);
        }
        return result;
    }


    public void handleNestedComponentTag(UIComponent parent,
                                         String shortTagName, Attributes attrs) {

        if (null == parent || null == shortTagName || null == attrs) {
            return;
        }
        String val = (String) classMap.get(shortTagName);
        if (null == val || (0 != val.indexOf(PARENT))) {
            return;
        }
    
        // At this point, we know that we are in a nested tag.

        // PENDING(edburns): check that parent is really the correct parent
        // for this shortTagName, for now, it's just UISelectOne

        UISelectOne uiSelectOne = (UISelectOne) parent;

        int attrLen, i = 0;
        String attrName, attrValue;
        String itemLabel = null, itemValue = null, itemDesc = null;
        boolean checked = false;

        attrLen = attrs.getLength();
        for (i = 0; i < attrLen; i++) {
            attrName = mapAttrNameToPropertyName(attrs.getLocalName(i));
            attrValue = attrs.getValue(i);

            if (attrName.equals("value")) {
                itemValue = attrValue;
            }
            if (attrName.equals("label")) {
                itemLabel = attrValue;
            }
            if (attrName.equals("description")) {
                itemDesc = attrValue;
            }
            if ((attrName.equals("checked") || attrName.equals("selected")) &&
                attrValue.equals("true")) {
                checked = true;
            }
        }


        /********** PENDING(edburns): Fix this for non-jsp example
         SelectItem [] oldItems = (SelectItem []) uiSelectOne.getItems();
         SelectItem [] newItems = null;

         if (null != oldItems) {
         newItems = new SelectItem[oldItems.length + 1];
         System.arraycopy(oldItems, 0, newItems, 0, oldItems.length);
         newItems[oldItems.length] = new SelectItem(itemValue, itemLabel,
         itemDesc);
         }
         else {
         newItems = new SelectItem[1];
         newItems[0] = new SelectItem(itemValue, itemLabel, itemDesc);
         }

         uiSelectOne.setItems(newItems);
         // if it is checked, make sure the model knows about it.
         // we should update selectedValue only if it is null
         // in the model bean otherwise we would be overwriting
         // the value in model bean, losing any earlier updates.
         if (checked && null == uiSelectOne.getSelectedValue()) {
         uiSelectOne.setSelectedValue(itemValue);
         }
         ***************/
    }


    public void applyAttributesToComponentInstance(UIComponent child,
                                                   Attributes attrs) {
        int attrLen, i = 0;
        String attrName, attrValue;

        attrLen = attrs.getLength();
        for (i = 0; i < attrLen; i++) {
            attrName = mapAttrNameToPropertyName(attrs.getLocalName(i));
            attrValue = attrs.getValue(i);

            // First, try to set it as a bean property
            try {
                PropertyUtils.setNestedProperty(child, attrName, attrValue);
            } catch (NoSuchMethodException e) {
                // If that doesn't work, see if it requires special
                // treatment
                try {
                    if (attrRequiresSpecialTreatment(attrName)) {
                        handleSpecialAttr(child, attrName, attrValue);
                    } else {
                        // If that doesn't work, this will.
                        child.getAttributes().put(attrName, attrValue);
                    }
                } catch (IllegalAccessException innerE) {
                    innerE.printStackTrace();
                    log.trace(innerE.getMessage());
                } catch (IllegalArgumentException innerE) {
                    innerE.printStackTrace();
                    log.trace(innerE.getMessage());
                } catch (InvocationTargetException innerE) {
                    innerE.printStackTrace();
                    log.trace(innerE.getMessage());
                } catch (NoSuchMethodException innerE) {
                    innerE.printStackTrace();
                    log.trace(innerE.getMessage());
                }
            } catch (IllegalArgumentException e) {
                try {
                    if (attrRequiresSpecialTreatment(attrName)) {
                        handleSpecialAttr(child, attrName, attrValue);
                    } else {
                        // If that doesn't work, this will.
                        child.getAttributes().put(attrName, attrValue);
                    }
                } catch (IllegalAccessException innerE) {
                    innerE.printStackTrace();
                    log.trace(innerE.getMessage());
                } catch (IllegalArgumentException innerE) {
                    innerE.printStackTrace();
                    log.trace(innerE.getMessage());
                } catch (InvocationTargetException innerE) {
                    innerE.printStackTrace();
                    log.trace(innerE.getMessage());
                } catch (NoSuchMethodException innerE) {
                    innerE.printStackTrace();
                    log.trace(innerE.getMessage());
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                log.trace(e.getMessage());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                log.trace(e.getMessage());
            }
        }
    
        // cleanup: make sure we have the necessary required attributes
        if (child.getId() == null) {
            String gId = "foo" + Util.generateId();
            child.setId(gId);
        }

    }

} // end of class BuildComponentFromTagImpl
