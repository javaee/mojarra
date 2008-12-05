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
 @project JSF Ajax Library
 @version 2.0
 @description This is the standard implementation of the JSF Ajax Library.
 */

/**
 * Register with OpenAjax
 */
if (typeof OpenAjax !== "undefined" &&
    typeof OpenAjax.hub.registerLibrary !== "undefined") {
    OpenAjax.hub.registerLibrary("javax", "www.sun.com", "1.0", null);
}

// RELEASE_PENDING - add version detection.

/**
 * The top level global namespace for JavaServer Faces functionality.
 * @name jsf
 * @namespace
 */
var jsf = {};

/**
 * The namespace for Ajax functionality.
 * @name jsf.ajax
 * @namespace
 * @exec
 */
jsf.ajax = function() {

    var eventListeners = [];
    var errorListeners = [];

    var getTransport = function() {
        var methods = [
            function() {
                return new XMLHttpRequest();
            },
            function() {
                return new ActiveXObject('Msxml2.XMLHTTP');
            },
            function() {
                return new ActiveXObject('Microsoft.XMLHTTP');
            }
        ];

        var returnVal;
        for (var i = 0, len = methods.length; i < len; i++) {
            try {
                returnVal = methods[i]();
            } catch(e) {
                continue;
            }
            return returnVal;
        }
        throw new Error('Could not create an XHR object.');
    };

    return {
        /**
         * Register a callback for error handling.
         * @member jsf.ajax
         * @param {String} callback string representing a function to call on an error
         */
        onError: function(callback) {
            errorListeners[errorListeners.length] = callback;
        },
        /**
         * Register a callback for event handling.
         * @member jsf.ajax
         * @param {String} callback string representing a function to call on an event
         */
        onEvent: function(callback) {
            eventListeners[eventListeners.length] = callback;
        },
        /**
         * <p>Send an asynchronous Ajax request to the server.
         * This function must:
         * <ul>
         * <li>Capture the element that triggered this Ajax request
         * (from the <code>element</code> argument, also known as the
         * <code>source</code> element.</li>
         * <li>Add the name of the source element in
         * <li>Determine the <code>source</code> element's <code>form</code>
         * element.</li>
         * <li>Get the <code>form</code> view state by calling
         * {@link jsf.viewState} passing the
         * <code>form</code> element as the argument.</li>
         * <li>Collect post data arguments for the Ajax request.
         * <ul>
         * <li>The following name/value pairs are required post data arguments:
         * <ul>
         * <li>The name and value of the <code>source</code> element that
         * triggered this request;</li>
         * <li><code>javax.faces.partial.ajax</code> with the value
         * <code>true</code></li>
         * </ul>
         * </li>
         * </ul>
         * </li>
         * <li>Collect optional post data arguments for the Ajax request.
         * <ul>
         * <li>Determine additional arguments (if any) from the <code>options</code>
         * argument. If <code>options.execute</code> exists, create the post data argument
         * with the name <code>javax.faces.partial.execute</code> and the value as a
         * space delimited <code>string</code> of client identifiers.  If
         * <code>options.render</code> exists, create the post data argument with the name
         * <code>javax.faces.partial.render</code> and the value as a space delimited
         * <code>string</code> of client identifiers.</li>
         * <li>Determine additional arguments (if any) from the <code>event</code>
         * argument.  The following name/value pairs may be used from the
         * <code>event</code> object:
         * <ul>
         * <li><code>target</code> - the ID of the element that triggered the event.</li>
         * <li><code>captured</code> - the ID of the element that captured the event.</li>
         * <li><code>type</code> - the type of event (ex: onkeypress)</li>
         * <li><code>alt</code> - <code>true</code> if ALT key was pressed.</li>
         * <li><code>ctrl</code> - <code>true</code> if CTRL key was pressed.</li>
         * <li><code>shift</code> - <code>true</code> if SHIFT key was pressed. </li>
         * <li><code>meta</code> - <code>true</code> if META key was pressed. </li>
         * <li><code>right</code> - <code>true</code> if right mouse button
         * was pressed. </li>
         * <li><code>left</code> - <code>true</code> if left mouse button
         * was pressed. </li>
         * <li><code>keycode</code> - the key code.
         * </ul>
         * </li>
         * </ul>
         * </li>
         * <li>Encode the set of post data arguments.</li>
         * <li>Join the encoded view state with the encoded set of post data arguments
         * to form the <code>query string</code> that will be sent to the server.</li>
         * <li>Send the request as an <code>asynchronous POST</code> using the
         * <code>action</code> property of the <code>form</code> element as the
         * <code>url</code>.</li>
         * </ul>
         * Before the request is sent it must be put into a queue to ensure requests
         * are sent in the same order as when they were initiated.  The request callback function
         * must examine the queue and determine the next request to be sent.  The behavior of the
         * request callback function must be as follows:
         * <ul>
         * <li>If the request completed successfully invoke {@link jsf.ajax.response}
         * passing the <code>request</code> object.</li>
         * <li>If the request did not complete successfully, notify the client.</li>
         * <li>Regardless of the outcome of the request (success or error) every request in the
         * queue must be handled.  Examine the status of each request in the queue starting from
         * the request that has been in the queue the longest.  If the status of the request is
         * <code>complete</code> (readyState 4), dequeue the request (remove it from the queue).
         * If the request has not been sent (readyState 0), send the request.  Requests that are
         * taken off the queue and sent should not be put back on the queue.</li>
         * </ul>
         *
         * </p>
         *
         * @param element The DOM element that triggered this Ajax request.
         * @param event The DOM event that triggered this Ajax request.  The
         * <code>event</code> argument is optional.
         * @param options The set of available options that can be sent as
         * request parameters to control client and/or server side
         * request processing. Acceptable name/value pair options are:
         * <table border="1">
         * <tr>
         * <th>name</th>
         * <th>value</th>
         * </tr>
         * <tr>
         * <td><code>execute</code></td>
         * <td><code>space seperated list of client identifiers</code></td>
         * </tr>
         * <tr>
         * <td><code>render</code></td>
         * <td><code>space seperated list of client identifiers</code></td>
         * </tr>
         * <tr>
         * <td><code>onEvent</code></td>
         * <td><code>name of a function to callback for event</code></td>
         * </tr>
         * <tr>
         * <td><code>onError</code></td>
         * <td><code>name of a function to callback for error</code></td>
         * </tr>
         * </table>
         * The <code>options</code> argument is optional.
         * @member jsf.ajax
         * @function jsf.ajax.request
         * @throws ArgNotSet Error if first required argument <code>element</code> is not specified
         */
        request: function(element, event, options) {

            if (typeof(options) === 'undefined' || options === null) {
                options = {};
            }

            // Error handler for this request
            var onError = false;

            if (options.onError) {
                error = options.onError;
            }

            // Event handler for this request
            var onEvent = false;

            if (options.onEvent) {
                onEvent = options.onEvent;
            }


            if (typeof element === 'undefined' || element === null) {
                throw new Error("jsf.ajax.request: Element not set");
            }

            // Capture the element that triggered this Ajax request.
            var source = element;

            var utils = jsf.Utils;
            var form = utils.getForm(source);
            var viewState = jsf.getViewState(form);

            // Set up additional arguments to be used in the request..
            // If there were "execute" ids specified, make sure we
            // include the identifier of the source element in the
            // "execute" list.  If there were no "execute" ids
            // specified, determine the default.

            var args = {};

            if (options.execute) {
                var temp = utils.toArray(options.execute, ',');
                if (!utils.isInArray(temp, source.name)) {
                    options.execute = source.name + "," + options.execute;
                }
            } else {
                options.execute = source.id;
            }

            args["javax.faces.partial.execute"] = utils.toArray(options.execute, ',').join(',');
            options.execute = null;
            if (options.render) {
                args["javax.faces.partial.render"] = utils.toArray(options.render, ',').join(',');
                options.render = null;
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

            var ajaxEngine = new jsf.AjaxEngine();
            ajaxEngine.setupArguments(args);
            ajaxEngine.queryString = viewState;
            ajaxEngine.onEvent = onEvent;
            ajaxEngine.onError = onError;
            ajaxEngine.sendRequest();
        }
    }
}();

/**
 * <p>Receive an Ajax response from the server.
 * This function must evaluate the markup returned in the
 * <code>responseXML</code> object and update the <code>DOM</code>
 * as follows:
 * <ul>
 * <p><b>Update Element Processing</b></p>
 * <li>If an <code>update</code> element is found in the response
 * with the identifier <code>javax.faces.ViewRoot</code>:
 * <pre><code>&lt;update id="javax.faces.ViewRoot"&gt;
 *    &lt;![CDATA[...]]&gt;
 * &lt;/update&gt;</code></pre>
 * Update the entire DOM as follows:
 * <ul>
 * <li>Extract the <code>CDATA</code> content and trim the &lt;html&gt;
 * and &lt;/html&gt; from the <code>CDATA</code> content if it is present.</li>
 * <li>If the <code>CDATA</code> content contains a &lt;head&gt; element,
 * and the document has a <code>&lt;head&gt;</code> section, extract the
 * contents of the &lt;head&gt; element from the <code>&lt;update&gt;</code>
 * element's <code>CDATA</code> content and replace the document's &lt;head&gt;
 * section with this contents.</li>
 * <li>If the <code>CDATA</code> content contains a &lt;body&gt; element,
 * and the document has a <code>&lt;body&gt;</code> section, extract the contents
 * of the &lt;body&gt; element from the <code>&lt;update&gt;</code>
 * element's <code>CDATA</code> content and replace the document's &lt;body&gt;
 * section with this contents.</li>
 * <li>If the <code>CDATA</code> content does not contain a &lt;body&gt; element,
 * replace the document's &lt;body&gt; section with the <code>CDATA</code>
 * contents.</li>
 * </ul>
 * <li>If an <code>update</code> element is found in the response with the identifier
 * <code>javax.faces.ViewState</code>:
 * <pre><code>&lt;update id="javax.faces.ViewState"&gt;
 *    &lt;![CDATA[...]]&gt;
 * &lt;/update&gt;</code></pre>
 * Include this <code>state</code> in the document as follows:
 * <ul>
 * <li>Extract this <code>&lt;update&gt;</code> element's <code>CDATA</code> contents
 * from the response.</li>
 * <li>If the document contains an element with the identifier
 * <code>javax.faces.ViewState</code> replace its contents with the
 * <code>CDATA</code> contents.</li>
 * <li>For each <code>&lt;form&gt;</code> element in the document:
 * <ul>
 * <li>If the <code>&lt;form&gt;</code> element contains an <code>&lt;input&gt;</code>
 * element with the identifier <code>javax.faces.ViewState</code>, replace the
 * <code>&lt;input&gt;</code> element contents with the <code>&lt;update&gt;</code>
 * element's <code>CDATA</code> contents.</li>
 * <li>If the <code>&lt;form&gt;</code> element does not contain an element with
 * the identifier <code>javax.faces.ViewState</code>, create an
 * <code>&lt;input&gt;</code> element of the type <code>hidden</code>,
 * with the identifier <code>javax.faces.ViewState</code>, set its contents
 * to the <code>&lt;update&gt;</code> element's <code>CDATA</code> contents, and
 * add the <code>&lt;input&gt;</code> element as a child to the
 * <code>&lt;form&gt;</code> element.</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * <li>For any other <code>&lt;update&gt;</code> element:
 * <pre><code>&lt;update id="update id"&gt;
 *    &lt;![CDATA[...]]&gt;
 * &lt;/update&gt;</code></pre>
 * Find the DOM element with the identifier that matches the
 * <code>&lt;update&gt;</code> element identifier, and replace its contents with
 * the <code>&lt;update&gt;</code> element's <code>CDATA</code> contents.</li>
 * </li>
 * <p><b>Insert Element Processing</b></p>
 * <li>If an <code>&lt;input&gt;</code> element is found in the response with the
 * attribute <code>before</code>:
 * <pre><code>&lt;insert id="insert id" before="before id"&gt;
 *    &lt;![CDATA[...]]&gt;
 * &lt;/insert&gt;</code></pre>
 * <ul>
 * <li>Extract this <code>&lt;input&gt;</code> element's <code>CDATA</code> contents
 * from the response.</li>
 * <li>Find the DOM element whose identifier matches <code>before id</code> and insert
 * the <code>&lt;input&gt;</code> element's <code>CDATA</code> content before
 * the DOM element in the document.</li>
 * </ul>
 * </li>
 * <li>If an <code>&lt;input&gt;</code> element is found in the response with the
 * attribute <code>after</code>:
 * <pre><code>&lt;insert id="insert id" after="after id"&gt;
 *    &lt;![CDATA[...]]&gt;
 * &lt;/insert&gt;</code></pre>
 * <ul>
 * <li>Extract this <code>&lt;input&gt;</code> element's <code>CDATA</code> contents
 * from the response.</li>
 * <li>Find the DOM element whose identifier matches <code>after id</code> and insert
 * the <code>&lt;input&gt;</code> element's <code>CDATA</code> content after
 * the DOM element in the document.</li>
 * </ul>
 * </li>
 * <p><b>Delete Element Processing</b></p>
 * <li>If a <code>&lt;delete&gt;</code> element is found in the response:
 * <pre><code>&lt;delete id="delete id"/&gt;</code></pre>
 * Find the DOM element whose identifier matches <code>delete id</code> and remove it
 * from the DOM.</li>
 * <p><b>Element Attribute Update Processing</b></p>
 * <li>If an <code>&lt;attributes&gt;</code> element is found in the response:
 * <pre><code>&lt;attributes id="id of element with attribute"&gt;
 *    &lt;attribute name="attribute name" value="attribute value"&gt;
 *    ...
 * &lt/attributes&gt;</code></pre>
 * <ul>
 * <li>Find the DOM element that matches the <code>&lt;attributes&gt;</code> identifier.</li>
 * <li>For each nested <code>&lt;attribute&gt;</code> element in <code>&lt;attribute&gt;</code>,
 * update the DOM element attribute value (whose name matches <code>attribute name</code>),
 * with <code>attribute value</code>.</li>
 * </ul>
 * </li>
 * <p><b>JavaScript Processing</b></p>
 * <li>If an <code>&lt;eval&gt;</code> element is found in the response:
 * <pre><code>&lt;eval&gt;
 *    &lt;![CDATA[...JavaScript...]]&gt;
 * &lt;/eval&gt;</code></pre>
 * <ul>
 * <li>Extract this <code>&lt;eval&gt;</code> element's <code>CDATA</code> contents
 * from the response and execute it as if it were JavaScript code.</li>
 * </ul>
 * </li>
 * <p><b>Redirect Processing</b></p>
 * <li>If a <code>&lt;redirect&gt;</code> element is found in the response:
 * <pre><code>&lt;redirect url="redirect url"/&gt;</code></pre>
 * Cause a redirect to the url <code>redirect url</code>.</li>
 * <p><b>Error Processing</b></p>
 * <li>If an <code>&lt;error&gt;</code> element is found in the response:
 * <pre><code>&lt;error&gt;
 *    &lt;error-class&gt;..fully qualified class name string...&lt;error-class&gt;
 *    &lt;error-message&gt;&lt;![CDATA[...]]&gt;&lt;error-message&gt;
 * &lt;/error&gt;</code></pre>
 * Extract this <code>&lt;error&gt;</code> element's <code>error-class</code> contents
 * and the <code>error-message</code> contents.  These identify the Java class that
 * caused the error, and the exception message, respectively.
 * This is an error from the server, and implementations can use this information
 * as needed.</li>
 * <p><b>Extensions</b></p>
 * <li>The <code>&lt;extensions&gt;</code> element provides a way for framework
 * implementations to provide their own information.</li>
 * </ul>
 *
 * </p>
 *
 * @param request The <code>XMLHttpRequest</code> instance that
 * contains the status code and response message from the server.
 *
 * @throws EmptyResponse error if request contains no data
 *
 * @function jsf.ajax.response
 */
jsf.ajax.response = function(request) {

    //  RELEASE_PENDING: We need to add more robust error handing - this error should probably be caught upstream
    if (request === null || typeof request === 'undefined') {
        throw new Error("jsf.ajax.response: Request is null");
    }

    var utils = jsf.Utils;
    var xmlReq = request;

    var xml = xmlReq.responseXML;
    //  RELEASE_PENDING: We need to add more robust error handing - this error should probably be caught upstream
    if (xml === null) {
        throw new Error("jsf.ajax.response: Reponse contains no data");
    }

    var id, content, markup, str, state;

    //////////////////////
    // Check for updates..
    //////////////////////

    var update = xml.getElementsByTagName('update');

    for (var i = 0; i < update.length; i++) {
        id = update[i].getAttribute('id');
        if (id === "javax.faces.ViewState") {
            state = state || update[i].firstChild;
            continue;
        }
        // join the CDATA sections in the markup
        markup = '';
        for (var j = 0; j < update[i].childNodes.length; j++) {
            content = update[i].childNodes[j];
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
                utils.elementReplace(docBody, "body", srcBody);
            }
            if (!srcBody) {
                utils.elementReplace(docBody, "body", src);
            }

        } else {
            var d = utils.$(id);
            if (!d) {
                throw new Error("jsf.ajax.response: " + id + " not found");
            }
            var parent = d.parentNode;
            var temp = document.createElement('div');
            temp.id = d.id;
            temp.innerHTML = utils.trim(str);

            parent.replaceChild(temp.firstChild, d);
        }
    }

    //////////////////////
    // Check For Inserts.
    //////////////////////

    //////////////////////
    // Check For Deletes.
    //////////////////////

    //////////////////////
    // Update Attributes.
    //////////////////////

    //////////////////////
    // JavaScript Eval.
    //////////////////////

    //////////////////////
    // Redirect.
    //////////////////////

    //////////////////////
    // Error.
    //////////////////////

    // Now set the view state from the server into the DOM
    // If there are multiple forms, make sure they all have a
    // viewState hidden field.

    if (state) {
        var stateElem = utils.$("javax.faces.ViewState");
        if (stateElem) {
            stateElem.value = state.text || state.data;
        }
        var numForms = document.forms.length;
        var field;
        for (var k = 0; k < numForms; k++) {
            field = document.forms[k].elements["javax.faces.ViewState"];
            if (typeof field == 'undefined') {
                field = document.createElement("input");
                field.type = "hidden";
                field.name = "javax.faces.ViewState";
                document.forms[k].appendChild(field);
            }
            field.value = state.text || state.data;
        }
    }
};


/**
 *
 * <p>Return the value of <code>Application.getProjectStage()</code> for
 * the currently running application instance.  Calling this method must
 * not cause any network transaction to happen to the server.</p>
 *
 * @returns The <code>String</code> representing the current state of the
 * running application in a typical product development lifecycle.  Refer
 * to <code>javax.faces.application.Application.getProjectStage</code> and
 * <code>javax.faces.application.ProjectStage</code>.
 * @function jsf.getProjectStage
 */
jsf.getProjectStage = function() {
    return "#{facesContext.application.projectStage}";
};


/**
 * <p>Collect and encode state for input controls associated
 * with the specified <code>form</code> element.</p>
 *
 * @param form The <code>form</code> element whose contained
 * <code>input</code> controls will be collected and encoded.
 * Only successful controls will be collected and encoded in
 * accordance with: <a href="http://www.w3.org/TR/html401/interact/forms.html#h-17.13.2">
 * Section 17.13.2 of the HTML Specification</a>.
 *
 * @returns String The encoded state for the specified form's input controls.
 * @function jsf.getViewState
 */
jsf.getViewState = function(form) {
    var els = form.elements;
    var len = els.length;
    var qString = "";
    var addField = function(name, value) {
        if (qString.length > 0) {
            qString += "&";
        }
        qString += encodeURIComponent(name) + "=" + encodeURIComponent(value);
    };
    for (var i = 0; i < len; i++) {
        var el = els[i];
        if (!el.disabled) {
            switch (el.type) {
                case 'text':
                case 'password':
                case 'hidden':
                case 'textarea':
                    addField(el.name, el.value);
                    break;
                case 'select-one':
                    if (el.selectedIndex >= 0) {
                        addField(el.name, el.options[el.selectedIndex].value);
                    }
                    break;
                case 'select-multiple':
                    for (var j = 0; j < el.options.length; j++) {
                        if (el.options[j].selected) {
                            addField(el.name, el.options[j].value);
                        }
                    }
                    break;
                case 'checkbox':
                case 'radio':
                    addField(el.name, el.checked + "");
                    break;
            }
        }
    }
    return qString;
};


/**
 * A String value which represents the current clientID separator character.
 */
jsf.separator = function() {
    // RELEASE_PENDING : Still needs to be wired to the back end
    return ":";
}();
