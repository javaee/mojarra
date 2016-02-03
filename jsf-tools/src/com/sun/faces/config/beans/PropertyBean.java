/*
 * $Id: PropertyBean.java,v 1.6 2007/04/27 22:02:43 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.config.beans;


/**
 * <p>Configuration bean for <code>&lt;property&gt; element.</p>
 */

public class PropertyBean extends FeatureBean {


    // -------------------------------------------------------------- Properties


    private String propertyClass;
    public String getPropertyClass() { return propertyClass; }
    public void setPropertyClass(String propertyClass)
    { this.propertyClass = propertyClass; }


    private String propertyName;
    public String getPropertyName() { return propertyName; }
    public void setPropertyName(String propertyName)
    { this.propertyName = propertyName; }


    private String suggestedValue;
    public String getSuggestedValue() { return suggestedValue; }
    public void setSuggestedValue(String suggestedValue)
    { this.suggestedValue = suggestedValue; }


    // -------------------------------------------------------------- Extensions


    // defaultValue == Non-standard default value (if any)
    private String defaultValue = null;
    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue)
    { this.defaultValue = defaultValue; }

    // passThrough == HTML attribute that passes through [default=false]
    private boolean passThrough = false;
    public boolean isPassThrough() { return passThrough; }
    public void setPassThrough(boolean passThrough)
    { this.passThrough = passThrough; }


    // readOnly == Do not generate a property setter [default=false]
    private boolean readOnly = false;
    public boolean isReadOnly() { return readOnly; }
    public void setReadOnly(boolean readOnly)
    { this.readOnly = readOnly; }


    // required == in TLD <attribute>, set required to true [default=false]
    private boolean required = false;
    public boolean isRequired() { return required; }
    public void setRequired(boolean required)
    { this.required = required; }


    // tagAttribute == Generate TLD attribute [default=true]
    private boolean tagAttribute = true;
    public boolean isTagAttribute() { return tagAttribute; }
    public void setTagAttribute(boolean tagAttribute)
    { this.tagAttribute = tagAttribute; }

    // Set to TRUE if property-extension contains method-signature element
    // [default=false]
    private boolean methodExpressionEnabled = false;
    public boolean isMethodExpressionEnabled() {
        return methodExpressionEnabled;
    }
    public void setMethodExpressionEnabled(boolean methodExpressionEnabled) {
        this.methodExpressionEnabled = methodExpressionEnabled;
    }

    private String methodSignature;
    public String getMethodSignature() { return methodSignature; }
    public void setMethodSignature(String methodSignature) {
        if (methodSignature != null) {
            methodSignature = methodSignature.trim();
            if (methodSignature.length() > 0) {
                setMethodExpressionEnabled(true);
                this.methodSignature = methodSignature.trim();
            }
        }
    }

    // value-expression-enabled - if the property can accept ValueExpressions
    // [default=false]
    private boolean valueExpressionEnabled = false;
    public boolean isValueExpressionEnabled() {
        return valueExpressionEnabled;
    }
    public void setValueExpressionEnabled(boolean valueExpressionEnabled) {
        this.valueExpressionEnabled = valueExpressionEnabled;
    }


    // ----------------------------------------------------------------- Methods


}
