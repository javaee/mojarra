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

package @package@;

import javax.faces.context.FacesContext;
import javax.faces.application.Application;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class SharedUtils {

    /*
    * Determine whether String is a mixed value binding expression or not.
    */
    static boolean isMixedExpression(String expression) {

        if (null == expression) {
            return false;
        }

        // if it doesn't start and end with delimiters
        return (!(expression.startsWith("#{") && expression.endsWith("}")))
                  && isExpression(expression);

    }


    /*
    * Determine whether String is a value binding expression or not.
    */
    static boolean isExpression(String expression) {

        if (null == expression) {
            return false;
        }

        //check to see if attribute has an expression
        int start = expression.indexOf("#{");
        return start != -1 && expression.indexOf('}', start+2) != -1;
    }

    static Map<String, List<String>> evaluateExpressions(FacesContext context, Map<String, List<String>> map) {
        if (map != null && !map.isEmpty()) {
            Map<String, List<String>> ret = new HashMap<String, List<String>>(map.size());
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                ret.put(entry.getKey(), evaluateExpressions(context, entry.getValue()));
            }
            
            return ret;
        }
        
        return map;
    }
     
    static List<String> evaluateExpressions(FacesContext context, List<String> values) {
         if (!values.isEmpty()) {
             List<String> ret = new ArrayList<String>(values.size());
             Application app = context.getApplication();
             for (String val : values) {
                 if (val != null) {
                     String value = val.trim();
                     if (isExpression(value)) {
                         value = app.evaluateExpressionGet(context,
                                                           value,
                                                           String.class);
                     }
                     ret.add(value);
                 }
             }
             
             return ret;
         }
         return values;
     }



}

     

