/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Configuration bean for <code>&lt;attribute&gt; element.</p>
 */

public class AttributeBean extends FeatureBean {


    // -------------------------------------------------------------- Properties


    private String attributeClass;
    public String getAttributeClass() { return attributeClass; }
    public void setAttributeClass(String attributeClass)
    { this.attributeClass = attributeClass; }


    private String attributeName;
    public String getAttributeName() { return attributeName; }
    public void setAttributeName(String attributeName)
    { this.attributeName = attributeName; }


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

    private boolean renderAttributeIgnore = false;
    public boolean isAttributeIgnoredForRenderer() {
        return renderAttributeIgnore;
    }
    public void setAttributeIgnoredForRenderer(boolean renderAttributeIgnore) {
        this.renderAttributeIgnore = renderAttributeIgnore;
    }


    // Behavior attribute, [dafault=false]
    private List<String> behaviors = null;
	/**
	 * <p class="changed_added_2_0"></p>
	 * @return the behaviorAttribute
	 */
	public List<String> getBehaviors() {
		return behaviors;
	}
	/**
	 * <p class="changed_added_2_0"></p>
	 * @param behaviorAttribute the behaviorAttribute to set
	 */
	public void addBehavior(String behavior) {
		if(null == this.behaviors){
			this.behaviors = new ArrayList<String>(5);
		}
		this.behaviors.add(behavior);
	}
	
	public void addAllBehaviors(List<String>behaviors) {
		if(null != behaviors){
			if(null == this.behaviors){
				this.behaviors = new ArrayList<String>(behaviors);
			} else {
				this.behaviors.addAll(behaviors);
			}
		}
	}

    private boolean defaultBehavior = false;
	/**
	 * <p class="changed_added_2_0"></p>
	 * @return the defaultBehavior
	 */
	public boolean isDefaultBehavior() {
		return defaultBehavior;
	}
	/**
	 * <p class="changed_added_2_0"></p>
	 * @param defaultBehavior the defaultBehavior to set
	 */
	public void setDefaultBehavior(boolean defaultBehavior) {
		this.defaultBehavior = defaultBehavior;
	}
    
    // ----------------------------------------------------------------- Methods


}
