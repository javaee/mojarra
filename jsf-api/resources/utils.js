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
 * Create our namespace - javax.faces.Ajax.utils
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
if (javax.faces.Ajax.utils == null || typeof javax.faces.Ajax.utils == "undefined") {
    javax.faces.Ajax["utils"] = new Object();
}


/**
 * Utility functions
 */
javax.faces.Ajax.utils = function() {

    this.getForm = function(element) {
        if (element) {
            var form = this.$(element);
            while (form && form.tagName && form.tagName.toLowerCase() != 'form') {
                if (form.form) return form.form;
                if (form.parentNode) {
                        form = form.parentNode;
                } else {
                    form = null;
                }
                if (form) return form;
            }
            return document.forms[0];
        }
    }

    this.getFormElements = function(form, options) {

        var elements = new Array();

        // Only interested in retrieving specified input elements..
        if (options && options.inputs) {
            var inputs = options.inputs.split(",");
            if (inputs) {
                for (var i = 0; i < inputs.length; i++) {
                    var element = document.getElementById(inputs[i]);
                    if (element &&  !isIgnoreType(element, options)) {
                        elements[i] = element;
                    }
                }
                return elements;
            }
        }

        // Check for controls to ignore..
        var ignore;
        if (options && options.ignore) {
            ignore = options.ignore.split(",");
        }

        var formElements = form.elements;
        for (var i = 0; i < formElements.length; i++) {
            var isIgnore = "false";
            if (ignore) {
                for (var j = 0; j < ignore.length; j++) {
                    if (formElements[i].id == ignore[j]) {
                        isIgnore = "true";
                        break;
                    }
                }
            }
            if (isIgnore == "false" && !isIgnoreType(formElements[i], options)) {
                elements.push(formElements[i]);
            }
        }

        // Include the control that triggered the event if it happens 
        //to be one of the ignore types..
        if (options && options.source && isIgnoreType(options.source)) 
            elements.push(options.source);
            
        return elements;
    }

    this.$ = function() {
        var results = [], element;
        for (var i = 0; i < arguments.length; i++) {
            element = arguments[i];
            if (typeof element == 'string')
                element = document.getElementById(element);
            results.push(element);
        }
        return this.reduce(results);
    }

    this.reduce = function(toReduce) {
        return toReduce.length > 1 ? toReduce : toReduce[0];
    }

    this.serializeElement = function(element) {
        element = this.$(element);
        var method = element.tagName.toLowerCase();
        var parameter = formElements[method](element);

        if (parameter) {
            var key = encodeURIComponent(parameter[0]);
            if (key.length == 0) return;

            if (parameter[1].constructor != Array)
                parameter[1] = [parameter[1]];

            return parameter[1].map(
                function(value) {
                    return key + '=' + encodeURIComponent(value);
                }
            ).join('&');
        }
    };

    var formElements =  {
        input: function(element) {
            switch (element.type.toLowerCase()) {
                case 'checkbox':
                case 'radio':
                    return this.inputSelector(element);
                default:
                    return this.textarea(element);
            }
            return false;
        },

        inputSelector: function(element) {
            if (element.checked)
                return [element.name, element.value];
        },

        textarea: function(element) {
            return [element.name, element.value];
        },

        select: function(element) {
            if (element.type == 'select-one') {
                return this.selectOne(element);
            } else {
                return this.selectMany(element);
            }
        },

        selectOne: function(element) {
            var value = '', opt, index = element.selectedIndex;
            if (index >= 0) {
                opt = element.options[index];
                value = opt.value || opt.text;
            }
            return [element.name, value];
        },

        selectMany: function(element) {
            var value = [];
            for (var i = 0; i < element.length; i++) {
                var opt = element.options[i];
                if (opt.selected)
                    value.push(opt.value || opt.text);
            }
            return [element.name, value];
        }
    }

    function isIgnoreType(element, options) {
        switch (element.type) {
            case 'image':
            case 'submit':
            case 'reset': return true;
        }
    }
}
