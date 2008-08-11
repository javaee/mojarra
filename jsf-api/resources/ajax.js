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
if (javax.faces == null || typeof javax.faces == "undefined") {
    javax["faces"] = new Object();
}
if (javax.faces.Ajax == null || typeof javax.faces.Ajax == "undefined") {
    javax.faces["Ajax"] = new Object();
}

/**
 * <p><span class="changed_added_2_0">Collect</span> and encode
 * state for input controls associated with the specified
 * <code>form</code> element. 
 *
 * @param form The <code>form</code> element whose contained
 * <code>input</code> controls will be collected and encoded.
 * Only successful controls will be collected and encoded in 
 * accordance with: <a href="http://www.w3.org/TR/html401/interact/forms.html#h-17.13.2">
 * Section 17.13.2 of the HTML Specification</a>.
 * @return The encoded state for the specified form's input controls.
 * @function viewState
 */
javax.faces.Ajax.viewState = function(form) {

    var viewState = javax.faces.Ajax.AjaxEngine.serializeForm(form);
    return viewState;

}

/**
 * <p><span class="changed_added_2_0">Send</span> an Ajax request
 * to the server.
 *
 * @param event The DOM event that triggered this Ajax request.
 * @param options The set of available options that can be sent as
 * request parameters to control client and/or server side 
 * request processing. 
 */
javax.faces.Ajax.ajaxRequest = function(event, options) {

    // Make sure we have the event that triggered this Ajax request..
    event = event || window.event || null;

    // Determine the element that triggered this Ajax request..
    var source = event.target || event.srcElement || null;

    var utils = new javax.faces.Ajax.Utils();
    var form = utils.getForm(source); 
    var viewState = javax.faces.Ajax.viewState(form);

    // Set up additional arguments to be used in the request..
    var args = new Object();
    if (options.execute) {
        args["javax.faces.partial.execute"] = utils.toArray(options.execute,',').join(','); 
    }
    if (options.render) {
        args["javax.faces.partial.render"] = utils.toArray(options.render,',').join(','); 
    }

    utils.extend(args, options);
    args["javax.faces.partial.ajax"] = "true";
    args["method"] = "POST";
    args["url"] = form.action;
    // add source
    var action = utils.$(source);
    if (action && action.form) {
        args[action.name] = action.value || 'x';
    } else {
        args[source] = source;
    }

    var ajaxEngine = new javax.faces.Ajax.AjaxEngine();
    ajaxEngine.setupArguments(args);
    ajaxEngine.queryString = viewState;
    ajaxEngine.sendRequest();
} 

/**
 * <p><span class="changed_added_2_0">Receive</span> an Ajax response
 * from the server.
 *
 * @param request The <code>XMLHttpRequest</code> instance that 
 * contains the status code and response message from the server.
 */ 
javax.faces.Ajax.ajaxResponse = function(request) {

    var utils = new javax.faces.Ajax.Utils();
    var xmlReq = request.xmlReq;

    var xml = xmlReq.responseXML;
    var id, content, markup, str;

    var components = xml.getElementsByTagName('components')[0];
    var render = components.getElementsByTagName('render');

    for (var i = 0; i < render.length; i++) {
        id = render[i].getAttribute('id');
        // join the CDATA sections in the markup
        markup = '';
        for (var j = 0; j < render[i].firstChild.childNodes.length; j++) {
            content = render[i].firstChild.childNodes[j];
            markup += content.text || content.data;
        }
        str = utils.stripScripts(markup);
        var src = str;
         
        // If our special render all markup is present..
        if (-1 != id.indexOf("javax.faces.ViewRoot")) {
            // if src contains <html>, trim the <html> and </html>, if present.
            //   if src contains <head>
            //      extract the contents of <head> and replace current document's
            //      <head> with the contents.
            //   if src contains <body>
            //      extract the contents of <body> and replace the current
            //      document's <body> with the contents.
            //   if src does not contain <body>
            //      replace the current document's <body> with the contents.
            var
                htmlStartEx = new RegExp("< *html.*>", "gi"),
                htmlEndEx = new RegExp("< */ *html.*>", "gi"),
                headStartEx = new RegExp("< *head.*>", "gi"),
                headEndEx = new RegExp("< */ *head.*>", "gi"),
                bodyStartEx = new RegExp("< *body.*>", "gi"),
                bodyEndEx = new RegExp("< */ *body.*>", "gi"),
                htmlStart, htmlEnd, headStart, headEnd, bodyStart, bodyEnd;
            var srcHead = null, srcBody = null;
            // find the current document's "body" element
            var docBody = document.getElementsByTagName("body")[0];
            // if src contains <html>
            if (null != (htmlStart = htmlStartEx.exec(src))) {
                // if src contains </html>
                if (null != (htmlEnd = htmlEndEx.exec(src))) {
                    src = src.substring(htmlStartEx.lastIndex, htmlEnd.index);
                } else {
                    src = src.substring(htmlStartEx.lastIndex);
                }
            }
            // if src contains <head>
            if (null != (headStart = headStartEx.exec(src))) {
                // if src contains </head>
                if (null != (headEnd = headEndEx.exec(src))) {
                    srcHead = src.substring(headStartEx.lastIndex,
                        headEnd.index);
                } else {
                    srcHead = src.substring(headStartEx.lastIndex);
                }
                // find the "head" element
                var docHead = document.getElementsByTagName("head")[0];
                if (docHead) {
                    utils.elementReplace(docHead, "head", srcHead);
                }
            }       
            // if src contains <body>
            if (null != (bodyStart = bodyStartEx.exec(src))) {
                // if src contains </body>
                if (null != (bodyEnd = bodyEndEx.exec(src))) {
                    srcBody = src.substring(bodyStartEx.lastIndex,
                        bodyEnd.index);
                } else {
                    srcBody = src.substring(bodyStartEx.lastIndex);
                }
                result = utils.elementReplace(docBody, "body", srcBody);
            }
            if (!srcBody) {
                result = utils.elementReplace(docBody, "body", src);
            }
        
        } else {
            var d = utils.$(id);
            if (!d) {
                alert(id + " not found");
            }
            var parent = d.parentNode;
            var temp = document.createElement('div');
            var result = null;
            temp.id = d.id;
            temp.innerHTML = utils.trim(str);

            result = temp.firstChild;
            parent.replaceChild(temp.firstChild,d);
        }
    }

    // Now set the view state from the server into the DOM
    // If there are multiple forms, make sure they all have a 
    // viewState hidden field.

    var state = state || xml.getElementsByTagName('state')[0].firstChild;
    if (state) {
        var stateElem = utils.$("javax.faces.ViewState");
        if (stateElem) {
            stateElem.value = state.text || state.data;
        }
        var numForms = document.forms.length;
        var field;
        for (var i = 0; i < numForms; i++) {
            field = document.forms[i].elements["javax.faces.ViewState"];
            if (typeof field == 'undefined') {
                field = document.createElement("input");
                field.type = "hidden";
                field.name = "javax.faces.ViewState";
                document.forms[i].appendChild(field);
            }
            field.value = state.text || state.data;
        }
    }
}

