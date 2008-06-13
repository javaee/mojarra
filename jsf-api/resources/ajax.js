/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Register with OpenAjax
 */
if (typeof OpenAjax != "undefined" &&
    typeof OpenAjax.hub.registerLibrary != "undefined") {
    OpenAjax.hub.registerLibrary("javax", "www.sun.com", "1.0", null);
}

/**
 * Create our top level namespace - javax.faces.Ajax
 */
if (javax == null || typeof javax == "undefined") {
    var javax = new Object();
}
if (javax.faces == null || typeof java.faces == "undefined") {
    javax["faces"] = new Object();
}
if (javax.faces.Ajax == null || typeof javax.faces.Ajax == "undefined") {
    javax.faces["Ajax"] = new Object();
}

/**
 * Namespace containing JSF 2.0 JavaScript  
 * @namespace javax.faces.Ajax 
 */
javax.faces.Ajax = function() {

    /**
     * <p><span class="changed_added_2_0">Collect</span> and encode
     * state for input controls associated with the specified
     * <code>form</code> element. 
     *
     * @param form The <code>form</code> element whose contained
     * <code>input</code> controls will be collected and encoded.
     * @param options An associative array that may contain
     * the following options: 
     * <ul>
     * <li><code>inputs</code>: A comma seperated <code>String</code> 
     * containing a list of client ids of input elements that must be 
     * colected and encoded.  If not specified, all input controls must 
     * be collected and encoded.</li>
     * <li><code>ignore</code>: A comma seperated <code>String</code>
     * containing a list of client ids of input elements that must not
     * be collected and encoded.</li>   
     * </ul>
     * Only successful controls will be collected and encoded in 
     * accordance with: <a href="http://www.w3.org/TR/html401/interact/forms.html#h-17.13.2">
     * Section 17.13.2 of the HTML Specification</a>.
     * @return The encoded state for the specified form's input controls.
     * @function viewState
     */
    this.viewState = function(form, options) {
        if (options != null && options != 'undefined') {
            if (options.constructor != Array) {
                // PENDING better error or log message
                throw "Second argument must be an associative array"; 
            }
        }

        var utils = new javax.faces.Ajax.utils();
        var elements = utils.getFormElements(form, options);

        var queryComponents = new Array();
        var javaxViewState = document.getElementById("javax.faces.ViewState");
        var queryComponent = null;
        var sourceSerialized = false;
        for (var i = 0; i < elements.length; i++) {
            queryComponent = utils.serializeElement(elements[i]);
            if (queryComponent) {
                // We'll add the viewState at the end..
                if (-1 == queryComponent.indexOf("javax.faces.ViewState")) {
                    queryComponents.push(queryComponent);
                }
            }
        }

        if (javaxViewState)
            queryComponents.push(utils.serializeElement(javaxViewState));

        viewState = queryComponents.join('&');

        return viewState;

    }
} 
