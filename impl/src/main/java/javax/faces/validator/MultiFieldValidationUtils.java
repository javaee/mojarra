/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

package javax.faces.validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import static javax.faces.validator.BeanValidator.ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME;
import static javax.faces.validator.BeanValidator.VALIDATOR_ID;
import javax.validation.groups.Default;

class MultiFieldValidationUtils {
    
    static final String MULTI_FIELD_VALIDATION_CANDIDATES =
            VALIDATOR_ID + ".MULTI_FIELD_VALIDATION_CANDIDATES";
    
    /**
     * <p class="changed_added_2_3">Special value to indicate the proposed value
     * for a property failed field-level validation.  This prevents any attempt
     * to perform class level validation.</p>
     */
    static final String FAILED_FIELD_LEVEL_VALIDATION = VALIDATOR_ID + ".FAILED_FIELD_LEVEL_VALIDATION";
    
    
   /*
    * <p class="changed_added_2_3">Returns a data structure that stores
    * the information necessary to perform class-level validation by
    * <code>&lt;f:validateWholeBean &gt;</code> components elsewhere in
    * the tree.  The lifetime of this data structure does not extend
    * beyond the current {@code FacesContext}.  The data structure must
    * conform to the following specification.</p>
    * 
    * <div class="changed_added_2_3">
    * 
    * <ul>
    * 
    * <li><p>It is a non-thread-safe {@code Map}.</p></li>
    * 
    * <li><p>Keys are CDI bean instances that are referenced by the
    * {@code value} attribute of <code>&lt;f:validateWholeBean
    * &gt;</code> components.</p></li>
    * 
    * <li>
    * 
    * <p>Values are {@code Map}s that represent the properties to be stored 
    * on the CDI bean instance that is the current key.  The inner {@code Map}
    * must conform to the following specification.</p>
    * 
    * <ul>
    * 
    * <li><p>It is a non-thread-safe {@code Map}.</p></li>
    * 
    * <li><p>Keys are property names.</p></li>
    * 
    * <li><p>Values are {@code Map} instances. In this innermost map, the following keys are supported.</p> 
    *
    * <p>component: Object that is the EditableValueHolder</p>
    * <p>value: Object that is the value of the property</p>
    *
    * </li>
    * 
    * </ul>
    * 
    * </li>
    * 
    * 
    * 
    * </ul>
    * 
    * </div>
    * 
    * @param context the {@link FacesContext} for this request
    * 
    * @param create if {@code true}, the data structure must be created if not present.
    * If {@code false} the data structure must not be created and {@code Collections.emptyMap()}
    * must be returned.
    *
    * @return the data structure representing the multi-field validation candidates
    * 
    * @since 2.3
    */
    static Map<Object, Map<String, Map<String, Object>>> getMultiFieldValidationCandidates(FacesContext context, boolean create) {
        Map<Object, Object> attrs = context.getAttributes();
        Map<Object, Map<String, Map<String, Object>>> result;
        result = (Map<Object, Map<String, Map<String, Object>>>) attrs.get(MULTI_FIELD_VALIDATION_CANDIDATES);
        if (null == result) {
            if (create) {
                result = new HashMap<>();
                attrs.put(MULTI_FIELD_VALIDATION_CANDIDATES, result);
            } else {
                result = Collections.emptyMap();
            } 
        }
        
        return result;
    }
    
    static boolean wholeBeanValidationEnabled(FacesContext context, 
            Class [] validationGroupsArray) {
        boolean result;
        
        Map<Object,Object> attrs = context.getAttributes();
        if (!(attrs.containsKey(ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME) &&
             (Boolean)attrs.get(ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME))) { // NOPMD
            return false;
        }
        
        result = !(1 == validationGroupsArray.length && Default.class == validationGroupsArray[0]);
        
        return result;
    }
    
            

}
