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
 * Utility functions
 */
javax.faces.Ajax.Utils = function() {

    this.createClass = function() {
        return function() {
            this.initialize.apply(this, arguments);
        };
    };

    this.extend = function(destination, source) {
        for (var property in source) {
            destination[property] = source[property];
        }
        return destination;
    };

    this.getForm = function(element) {
        if (element) {
            var form = this.$(element);
            while (form && form.tagName && form.tagName.toLowerCase() !== 'form') {
                if (form.form) {
                    return form.form;
                }
                if (form.parentNode) {
                        form = form.parentNode;
                } else {
                    form = null;
                }
                if (form) {
                    return form;
                }
            }
            return document.forms[0];
        }
        return null;
    };

    this.$ = function() {
        var results = [], element;
        for (var i = 0; i < arguments.length; i++) {
            element = arguments[i];
            if (typeof element == 'string')
                element = document.getElementById(element);
            results.push(element);
        }
        return this.reduce(results);
    };

    this.reduce = function(toReduce) {
        return toReduce.length > 1 ? toReduce : toReduce[0];
    };

    this.toArray = function(s,e) {
        var sarray;
        if (typeof s == 'string') {
            sarray = s.split((e)?e:' ');
            for (var i=0; i<sarray.length; i++) {
                sarray[i] = this.trim(sarray[i]);
            }
        }
        return sarray;
    };

    this.trim = function(toTrim) {
        var result = null;
        if (null != toTrim) {
            var s = toTrim.replace( /^\s+/g, "" );
            result = s.replace( /\s+$/g, "" );
        }
        return result;
    };

    this.scriptFrag = '(?:<script.*?>)((\n|\r|.)*?)(?:<\/script>)';

    this.stripScripts = function(src) {
        return src.replace(new RegExp(this.scriptFrag, 'img'), '');
    };

    this.evalScripts = function(src) {
        return this.extractScripts(src).map(function(script) { return eval(script) });
    };

    this.extractScripts = function(src) {
        var matchAll = new RegExp(this.scriptFrag, 'img');
        var matchOne = new RegExp(this.scriptFrag, 'im');
        return (src.match(matchAll) || []).map(function(scriptTag) {
            return (scriptTag.match(matchOne) || ['', ''])[1];
        });
    };

this.elementReplace = function(d, tempTagName, src) {
        var parent = d.parentNode;
        var temp = document.createElement(tempTagName);
        var result = null;
        temp.id = d.id;

        // If we are creating a head element...
        if (-1 != d.tagName.toLowerCase().indexOf("head") && d.tagName.length == 4) {

            // head replacement only appears to work on firefox.
            if (-1 == BrowserDetect.browser.indexOf("Firefox")) {
                return result;
            }

            // Strip link elements from src.
            if (-1 != src.indexOf("link")) {
                var
                    linkStartEx = new RegExp("< *link.*>", "gi");
                var linkStart;
                while (null != (linkStart = linkStartEx.exec(src))) {
                    src = src.substring(0, linkStart.index) +
                        src.substring(linkStartEx.lastIndex);
                    linkStartEx.lastIndex = 0;
                }
            }

            // Strip style elements from src
            if (-1 != src.indexOf("style")) {
                var
                    styleStartEx = new RegExp("< *style.*>", "gi"),
                    styleEndEx = new RegExp("< */ *style.*>", "gi");
                var styleStart, styleEnd;
                while (null != (styleStart = styleStartEx.exec(src))) {
                    styleEnd = styleEndEx.exec(src);
                    src = src.substring(0, styleStart.index) +
                        src.substring(styleStartEx.lastIndex);
                    styleStartEx.lastIndex = 0;
                }
            }

            temp.innerHTML = src;

            // clone all the link elements...
            var i, links, styles;
            links = d.getElementsByTagName("link");
            if (links) {
                for (i = 0; i < links.length; i++) {
                    temp.appendChild(links[i].cloneNode(true));
                }
            }
            // then clone all the style elements.
            styles = d.getElementsByTagName("style");
            if (styles) {
                for (i = 0; i < styles.length; i++) {
                    temp.appendChild(styles[i].cloneNode(true));
                }
            }
        } else {
            temp.innerHTML = src;
        }

        result = temp;
        parent.replaceChild(temp, d);
        return result;
    };

    // Copy the direct properties of an object to another, new object
    // Do not copy functions or inherited properties
    this.deepObjCopy = function deepObjCopy(dupeObj) {
        var retObj = {};
        if (dupeObj === null) {
            return null;
        }
        if (typeof dupeObj === 'object') {
            if (typeof dupeObj.length !== 'undefined') {
                retObj = [];
            }
            for (var objInd in dupeObj) {
                if (dupeObj.hasOwnProperty(objInd)) {
                    if (typeof dupeObj[objInd] === 'object') {
                        retObj[objInd] = deepObjCopy(dupeObj[objInd]);
                    } else if (typeof dupeObj[objInd] === 'string') {
                        retObj[objInd] = dupeObj[objInd];
                    } else if (typeof dupeObj[objInd] === 'number') {
                        retObj[objInd] = dupeObj[objInd];
                    } else if (typeof dupeObj[objInd] === 'boolean') {
                        if (dupeObj[objInd] === true) {
                            retObj[objInd] = true;
                        } else {
                            retObj[objInd] = false;
                        }
                    } // else igore functions
                }
            }
        }
        return retObj;
    };
};
