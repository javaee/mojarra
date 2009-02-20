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
 @project JSF JavaScript Library
 @version 2.0
 @description This is the standard implementation of the JSF JavaScript Library.
 */

/**
 * Register with OpenAjax
 */
if (typeof OpenAjax !== "undefined" &&
    typeof OpenAjax.hub.registerLibrary !== "undefined") {
    OpenAjax.hub.registerLibrary("jsf", "www.sun.com", "2.0", null);
}

// Detect if this is already loaded, and if loaded, if it's a higher version
if (!((jsf && jsf.specversion && jsf.specversion > 20000 ) &&
      (jsf.implversion && jsf.implversion > 1))) {

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

        /**
         * @ignore
         */
        var getTransport = function getTransport() {
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

        /**
         * @ignore
         */
        var $ = function $() {
            var results = [], element;
            for (var i = 0; i < arguments.length; i++) {
                element = arguments[i];
                if (typeof element == 'string') {
                    element = document.getElementById(element);
                }
                results.push(element);
            }
            return results.length > 1 ? results : results[0];
        };

        /**
         * @ignore
         */
        var getForm = function getForm(element) {
            if (element) {
                var form = $(element);
                while (form && form.nodeName && form.nodeName.toLowerCase() !== 'form') {
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

        /**
         * Check if a value exists in an array
         * @ignore
         */
        var isInArray = function isInArray(array, value) {
            for (var i = 0; i < array.length; i++) {
                if (array[i] === value) {
                    return true;
                }
            }
            return false;
        };


        /**
         * Replace DOM element
         * @ignore
         */
        var elementReplace = function elementReplace(d, tempTagName, src) {
            var parent = d.parentNode;
            var temp = document.createElement(tempTagName);
            var result = null;
            temp.id = d.id;

            // Creating a head element isn't allowed in IE, so we'll disallow it
            if (d.nodeName.toLowerCase() === "head") {
                throw new Error("Attempted to replace a head element");
            } else {
                temp.innerHTML = src;
            }

            result = temp;
            parent.replaceChild(temp, d);
            return result;
        };


        /**
         * Do update.
         * @ignore
         */
        var doUpdate = function doUpdate(element) {
            var id, content, markup, str, state;
            var stateElem;

            id = element.getAttribute('id');
            if (id === "javax.faces.ViewState") {
                state = element.firstChild;

                // Now set the view state from the server into the DOM
                // If there are multiple forms, make sure they all have a
                // viewState hidden field.

                stateElem = $("javax.faces.ViewState");
                if (stateElem) {
                    stateElem.value = state.text || state.data;
                }
                var field;
                for (var k = 0; k < document.forms.length; k++) {
                    field = document.forms[k].elements["javax.faces.ViewState"];
                    if (typeof field == 'undefined') {
                        field = document.createElement("input");
                        field.type = "hidden";
                        field.name = "javax.faces.ViewState";
                        document.forms[k].appendChild(field);
                    }
                    field.value = state.text || state.data;
                }
                return;
            }

            // join the CDATA sections in the markup
            // RELEASE_PENDING are there allowed to be more than one CDATA?
            markup = '';
            for (var j = 0; j < element.childNodes.length; j++) {
                content = element.childNodes[j];
                markup += content.text || content.data;
            }

            // RELEASE_PENDING - doc what this does
            str = markup.replace(new RegExp('(?:<script.*?>)((\n|\r|.)*?)(?:<\/script>)', 'img'), '');

            var src = str;

            // If our special render all markup is present..
            if (id === "javax.faces.ViewRoot") {
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
                if (null !== (htmlStart = htmlStartEx.exec(src))) {
                    // if src contains </html>
                    if (null !== (htmlEnd = htmlEndEx.exec(src))) {
                        src = src.substring(htmlStartEx.lastIndex, htmlEnd.index);
                    } else {
                        src = src.substring(htmlStartEx.lastIndex);
                    }
                }
                // if src contains <head>
                if (null !== (headStart = headStartEx.exec(src))) {
                    // if src contains </head>
                    if (null !== (headEnd = headEndEx.exec(src))) {
                        srcHead = src.substring(headStartEx.lastIndex,
                                headEnd.index);
                    } else {
                        srcHead = src.substring(headStartEx.lastIndex);
                    }
                    // find the "head" element
                    var docHead = document.getElementsByTagName("head")[0];
                    if (docHead) {
                        elementReplace(docHead, "head", srcHead);
                    }
                }
                // if src contains <body>
                if (null !== (bodyStart = bodyStartEx.exec(src))) {
                    // if src contains </body>
                    if (null !== (bodyEnd = bodyEndEx.exec(src))) {
                        srcBody = src.substring(bodyStartEx.lastIndex,
                                bodyEnd.index);
                    } else {
                        srcBody = src.substring(bodyStartEx.lastIndex);
                    }
                    elementReplace(docBody, "body", srcBody);
                }
                if (!srcBody) {
                    elementReplace(docBody, "body", src);
                }

            } else {
                var d = $(id);
                if (!d) {
                    throw new Error("jsf.ajax.response: " + id + " not found");
                }
                var parent = d.parentNode;
                var temp = document.createElement('div');
                temp.id = d.id;
                // Trim space padding before assigning to innerHTML
                temp.innerHTML = str.replace(/^\s+/g,'').replace(/\s+$/g,'');

                parent.replaceChild(temp.firstChild, d);
            }
        };

        /**
         * Delete a node specified by the element.
         * @param element
         * @ignore
         */
        var doDelete = function doDelete(element) {
            var id = element.getAttribute('id');
            var target = $(id);
            var parent = target.parentNode;
            parent.removeChild(target);
        };

        /**
         * Insert a node specified by the element.
         * @param element
         * @ignore
         */
        var doInsert = function doInsert(element) {
            // RELEASE_PENDING there may be an insert issue in IE tables.  Needs testing.
            var target = $(element.firstChild.getAttribute('id'));
            var parent = target.parentNode;
            var tempElement = document.createElement('span');
            tempElement.innerHTML = element.firstChild.firstChild.nodeValue;
            if (element.firstChild.nodeName === 'after') {
                // Get the next in the list, to insert before
                target = target.nextSibling;
            }  // otherwise, this is a 'before' element
            parent.insertBefore(tempElement.firstChild, target);
            document.removeChild(tempElement);
        };

        /**
         * Modify attributes of given element id.
         * @param element
         * @ignore
         */
        var doAttributes = function doAttributes(element) {

            // RELEASE_PENDING IE 5-7 does not allow setting styles with setAttribute
            // RELEASE_PENDING IE 5-7 will clear event attributes if you try to set them

            // Get id of element we'll act against
            var id = element.getAttribute('id');

            var target = $(id);

            // There can be multiple attributes modified.  Loop through the list.
            var nodes = element.childNodes;
            for (var i = 0; i < nodes.length; i++) {
                var name = nodes[i].firstChild.firstChild.nodeValue;
                var value = nodes[i].firstChild.nextSibling.firstChild.nodeValue;
                target.setAttribute(name,value);
            }
        };

        /**
         * Eval the CDATA of the element.
         * @param element to eval
         * @ignore
         */
        var doEval = function doEval(element) {
            var evalText = element.firstChild.nodeValue;
            eval(evalText);
        }

        /**
         * Ajax Request Queue
         * @ignore
         */
        var Queue = new function Queue() {

            // Create the internal queue
            var queue = [];


            // the amount of space at the front of the queue, initialised to zero
            var queueSpace = 0;

            /** Returns the size of this Queue. The size of a Queue is equal to the number
             * of elements that have been enqueued minus the number of elements that have
             * been dequeued.
             * @ignore
             */
            this.getSize = function getSize() {
                return queue.length - queueSpace;
            };

            /** Returns true if this Queue is empty, and false otherwise. A Queue is empty
             * if the number of elements that have been enqueued equals the number of
             * elements that have been dequeued.
             * @ignore
             */
            this.isEmpty = function isEmpty() {
                return (queue.length === 0);
            };

            /** Enqueues the specified element in this Queue.
             *
             * @param element - the element to enqueue
             * @ignore
             */
            this.enqueue = function enqueue(element) {
                // Queue the request
                queue.push(element);
            };


            /** Dequeues an element from this Queue. The oldest element in this Queue is
             * removed and returned. If this Queue is empty then undefined is returned.
             *
             * @returns Object The element that was removed from the queue.
             * @ignore
             */
            this.dequeue = function dequeue() {
                // initialise the element to return to be undefined
                var element = undefined;

                // check whether the queue is empty
                if (queue.length) {
                    // fetch the oldest element in the queue
                    element = queue[queueSpace];

                    // update the amount of space and check whether a shift should occur
                    if (++queueSpace * 2 >= queue.length) {
                        // set the queue equal to the non-empty portion of the queue
                        queue = queue.slice(queueSpace);
                        // reset the amount of space at the front of the queue
                        queueSpace = 0;
                    }
                }
                // return the removed element
                return element;
            };

            /** Returns the oldest element in this Queue. If this Queue is empty then
             * undefined is returned. This function returns the same value as the dequeue
             * function, but does not remove the returned element from this Queue.
             * @ignore
             */
            this.getOldestElement = function getOldestElement() {
                // initialise the element to return to be undefined
                var element = undefined;

                // if the queue is not element then fetch the oldest element in the queue
                if (queue.length) {
                    element = queue[queueSpace];
                }
                // return the oldest element
                return element;
            };
        }();


        /**
         * AjaxEngine handles Ajax implementation details.
         * @ignore
         */
        var AjaxEngine = function AjaxEngine() {

            var req = {};                  // Request Object
            req.url = null;                // Request URL
            req.context = {};              // Context of request and response
            req.context.source = null;             // Source of this request
            req.context.onerror = null;            // Error handler for request
            req.context.onevent = null;            // Event handler for request
            req.xmlReq = null;             // XMLHttpRequest Object
            req.async = true;              // Default - Asynchronous
            req.parameters = {};           // Parameters For GET or POST
            req.queryString = null;        // Encoded Data For GET or POST
            req.method = null;             // GET or POST
            req.status = null;             // Response Status Code From Server
            req.fromQueue = false;         // Indicates if the request was taken off the queue
            // before being sent.  This prevents the request from
            // entering the queue redundantly.

            req.que = Queue;

            // Get an XMLHttpRequest Handle
            req.xmlReq = getTransport();
            if (req.xmlReq === null) {
                return null;
            }

            // Set up request/response state callbacks
            /**
             * @ignore
             */
            req.xmlReq.onreadystatechange = function() {
                if (req.xmlReq.readyState === 4) {
                    req.onComplete();
                }
            };

            /**
             * This function is called when the request/response interaction
             * is complete.  If the return status code is successfull,
             * dequeue all requests from the queue that have completed.  If a
             * request has been found on the queue that has not been sent,
             * send the request.
             * @ignore
             */
            req.onComplete = function onComplete() {
                if (req.xmlReq.status && (req.xmlReq.status >= 200 && req.xmlReq.status < 300)) {
                    sendEvent(req.xmlReq, req.context, "complete");
                    jsf.ajax.response(req.xmlReq, req.context);
                } else {
                    sendEvent(req.xmlReq, req.context, "complete");
                    sendError(req.xmlReq, req.context, "httpError");
                }

                // Regardless of whether the request completed successfully (or not),
                // dequeue requests that have been completed (readyState 4) and send
                // requests that ready to be sent (readyState 0).

                var nextReq = req.que.getOldestElement();
                if (nextReq === null || typeof nextReq === 'undefined') {
                    return;
                }
                while ((typeof nextReq.xmlReq !== 'undefined' && nextReq.xmlReq !== null) &&
                       nextReq.xmlReq.readyState === 4) {
                    req.que.dequeue();
                    nextReq = req.que.getOldestElement();
                    if (nextReq === null || typeof nextReq === 'undefined') {
                        break;
                    }
                }
                if (nextReq === null || typeof nextReq === 'undefined') {
                    return;
                }
                if ((typeof nextReq.xmlReq !== 'undefined' && nextReq.xmlReq !== null) &&
                    nextReq.xmlReq.readyState === 0) {
                    nextReq.fromQueue = true;
                    nextReq.sendRequest();
                }
            };

            /**
             * Utility method that accepts additional arguments for the AjaxEngine.
             * If an argument is passed in that matches an AjaxEngine property, the
             * argument value becomes the value of the AjaxEngine property.
             * Arguments that don't match AjaxEngine properties are added as
             * request parameters.
             * @ignore
             */
            req.setupArguments = function(args) {
                for (var i in args) {
                    if (args.hasOwnProperty(i)) {
                        if (typeof req[i] === 'undefined') {
                            req.parameters[i] = args[i];
                        } else {
                            req[i] = args[i];
                        }
                    }
                }
            };

            /**
             * This function does final encoding of parameters, determines the request method
             * (GET or POST) and sends the request using the specified url.
             * @ignore
             */
            req.sendRequest = function() {
                if (req.xmlReq !== null) {
                    // if there is already a request on the queue waiting to be processed..
                    // just queue this request
                    if (!req.que.isEmpty()) {
                        if (!req.fromQueue) {
                            req.que.enqueue(req);
                            return;
                        }
                    }
                    // If the queue is empty, queue up this request and send
                    if (!req.fromQueue) {
                        req.que.enqueue(req);
                    }
                    // Some logic to get the real request URL
                    if (req.generateUniqueUrl && req.method == "GET") {
                        req.parameters["AjaxRequestUniqueId"] = new Date().getTime() + "" + req.requestIndex;
                    }
                    var content = null; // For POST requests, to hold query string
                    for (var i in req.parameters) {
                        if (req.parameters.hasOwnProperty(i)) {
                            if (req.queryString.length > 0) {
                                req.queryString += "&";
                            }
                            req.queryString += encodeURIComponent(i) + "=" + encodeURIComponent(req.parameters[i]);
                        }
                    }
                    if (req.method === "GET") {
                        if (req.queryString.length > 0) {
                            req.url += ((req.url.indexOf("?") > -1) ? "&" : "?") + req.queryString;
                        }
                    }
                    req.xmlReq.open(req.method, req.url, req.async);
                    if (req.method === "POST") {
                        if (typeof req.xmlReq.setRequestHeader !== 'undefined') {
                            req.xmlReq.setRequestHeader('Faces-Request', 'partial/ajax');
                            req.xmlReq.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                        }
                        content = req.queryString;
                    }
                    sendEvent(req.xmlReq, req.context, "begin");
                    req.xmlReq.send(content);
                }
            };

            return req;
        };

        /**
         * Error handling callback.
         * Assumes that the request has completed.
         * @ignore
         */
        var sendError = function sendError(request, context, name, serverErrorName, serverErrorMessage) {

            // Possible errornames:
            // httpError
            // emptyResponse
            // serverError  RELEASE_PENDING or facesError?
            // malformedXML

            var sent = false;
            var data = {};  // data payload for function
            data.type = "error";
            data.name = name;
            // RELEASE_PENDING won't work in response
            data.source = context.source;
            data.responseCode = request.status;
            // RELEASE_PENDING pass a copy, not a reference
            data.responseXML = request.responseXML;
            // RELEASE_PENDING won't work in response
            data.responseText = request.responseText;

            if (name == "serverError") {
                data.errorName = serverErrorName;
                data.errorMessage = serverErrorMessage;
            }

            // If we have a registered callback, send the error to it.
            if (context.onerror) {
                context.onerror.call(null, data);
                sent = true;
            }

            for (var i in errorListeners) {
                if (errorListeners.hasOwnProperty(i)) {
                    errorListeners[i].call(null, data);
                    sent = true;
                }
            }

            if (!sent && jsf.getProjectStage() === "Development") {
                switch (name) {
                    case "httpError":
                        alert("httpError " + request.status);
                        break;
                    case "serverError":
                        alert("serverError: " + serverErrorName + " " + serverErrorMessage);
                        break;
                    default:
                        alert("Error " + name);
                        break;
                }
            }
        };

        /**
         * Event handling callback.
         * Request is assumed to have completed, except in the case of event = 'begin'.
         * @ignore
         */
        var sendEvent = function sendEvent(request, context, name) {

            var data = {};
            data.type = "event";
            data.name = name;
            data.source = context.source;
            if (name !== 'begin') {
                data.responseCode = request.status;
                // RELEASE_PENDING pass a copy, not a reference
                data.responseXML = request.responseXML;
                data.responseText = request.responseText;
            }

            if (context.onevent) {
                context.onevent.call(null, data);
            }

            for (var i in eventListeners) {
                if (eventListeners.hasOwnProperty(i)) {
                    eventListeners[i].call(null, data);
                }
            }
        };

        // Use module pattern to return the functions we actually expose
        return {
            /**
             * Register a callback for error handling.
             * <p><b>Usage:</b></p>
             * <pre><code>
             * jsf.ajax.addOnError(handleError);
             * ...
             * var handleError = function handleError(data) {
             * ...
             * }
             * </pre></code>
             * <p><b>Implementation Requirements:</b></p>
             * This function must accept a reference to an existing JavaScript function.
             * The JavaScript function reference must be added to a list of callbacks, making it possible
             * to register more than one callback by invoking <code>jsf.ajax.addOnError</code>
             * more than once.  This function must throw an error if the <code>callback</code>
             * argument is not a function.
             *
             * @member jsf.ajax
             * @param callback a reference to a function to call on an error
             */
            addOnError: function addOnError(callback) {
                if (typeof callback === 'function') {
                    errorListeners[errorListeners.length] = callback;
                } else {
                    throw new Error("jsf.ajax.addOnError:  Added a callback that was not a function.");
                }
            },
            /**
             * Register a callback for event handling.
             * <p><b>Usage:</b></p>
             * <pre><code>
             * jsf.ajax.addOnEvent(statusUpdate);
             * ...
             * var statusUpdate = function statusUpdate(data) {
             * ...
             * }
             * </pre></code>
             * <p><b>Implementation Requirements:</b></p>
             * This function must accept a reference to an existing JavaScript function.
             * The JavaScript function reference must be added to a list of callbacks, making it possible
             * to register more than one callback by invoking <code>jsf.ajax.addOnEvent</code>
             * more than once.  This function must throw an error if the <code>callback</code>
             * argument is not a function.
             *
             * @member jsf.ajax
             * @param callback a reference to a function to call on an event
             */
            addOnEvent: function addOnEvent(callback) {
                if (typeof callback === 'function') {
                    eventListeners[eventListeners.length] = callback;
                } else {
                    throw new Error("jsf.ajax.addOnEvent: Added a callback that was not a function");
                }
            },
            /**
             * <p>Send an asynchronous Ajax request to the server.
             * <p><b>Usage:</b></p>
             * <pre><code>
             * Example showing all optional arguments:
             *
             * &lt;commandButton id="button1" value="submit"
             *     onclick="jsf.ajax.request(this,event,
             *       {execute:'button1',render:'status',onevent: handleEvent,onerror: handleError});return false;"/&gt;
             * &lt;/commandButton/&gt;
             * </pre></code>
             * <p><b>Implementation Requirements:</b></p>
             * This function must:
             * <ul>
             * <li>Be used within the context of a <code>form</code>.</li>
             * <li>Capture the element that triggered this Ajax request
             * (from the <code>source</code> argument, also known as the
             * <code>source</code> element.</li>
             * <li>If the <code>source</code> element is <code>null</code> or
             * <code>undefined</code> throw an error.</li>
             * <li>If the <code>source</code> argument is not a <code>string</code> or
             * DOM element object, throw an error.</li>
             * <li>If the <code>source</code> argument is a <code>string</code>, find the
             * DOM element for that <code>string</code> identifier.
             * <li>If the DOM element could not be determined, throw an error.</li>
             * <li>If the <code>onerror</code> and <code>onevent</code> arguments are set,
             * they must be functions, or throw an error.
             * <li>Determine the <code>source</code> element's <code>form</code>
             * element.</li>
             * <li>Get the <code>form</code> view state by calling
             * {@link jsf.getViewState} passing the
             * <code>form</code> element as the argument.</li>
             * <li>Collect post data arguments for the Ajax request.
             * <ul>
             * <li>The following name/value pairs are required post data arguments:
             * <ul>
             * <li><code>javax.faces.partial.source</code> with the value as the
             * source element identifier.</li>
             * <li>The name and value of the <code>source</code> element that
             * triggered this request;</li>
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
             * <li>Create a request <code>context</code> object and set the properties:
             * <ul><li><code>source</code> (the source DOM element for this request)</li>
             * <li><code>onerror</code> (the error handler for this request)</li>
             * <li><code>onevent</code> (the event handler for this request)</li></ul>
             * The request context will be used during error/event handling.</li>
             * <li>Send a <code>begin</code> event following the procedure as outlined
             * in the Chapter 13 "Sending Events" section of the spec prose document <a
             *  href="../../javadocs/overview-summary.html#prose_document">linked in the
             *  overview summary</a></li>
             * <li>Set the request header with the name: <code>Faces-Request and the
             * value: <code>partial/ajax</code>.</li>
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
             * @param source The DOM element that triggered this Ajax request, or an id string of the
             * element to use as the triggering element.
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
             * <td><code>onevent</code></td>
             * <td><code>function to callback for event</code></td>
             * </tr>
             * <tr>
             * <td><code>onerror</code></td>
             * <td><code>function to callback for error</code></td>
             * </tr>
             * <tr>
             * <td><code>params</code></td>
             * <td><code>object containing parameters to include in the request</code></td>
             * </tr>
             * </table>
             * The <code>options</code> argument is optional.
             * @member jsf.ajax
             * @function jsf.ajax.request
             * @throws ArgNotSet Error if first required argument <code>element</code> is not specified
             */
            request: function request(source, event, options) {

                var element;

                if (typeof source === 'undefined' || source === null) {
                    throw new Error("jsf.ajax.request: source not set");
                }
                if (typeof source === 'string') {
                    element = document.getElementById(source);
                } else if (typeof source === 'object') {
                    element = source;
                } else {
                    throw new Error("jsf.request: source must be object or string");
                }

                if (typeof(options) === 'undefined' || options === null) {
                    options = {};
                }

                // Error handler for this request
                var onerror = false;

                if (options.onerror && typeof options.onerror === 'function') {
                    onerror = options.onerror;
                } else if (options.onerror && typeof options.onerror !== 'function') {
                    throw new Error("jsf.ajax.request: Added an onerror callback that was not a function");
                }

                // Event handler for this request
                var onevent = false;

                if (options.onevent && typeof options.onevent === 'function') {
                    onevent = options.onevent;
                } else if (options.onevent && typeof options.onevent !== 'function') {
                    throw new Error("jsf.ajax.request: Added an onevent callback that was not a function");
                }

                var form = getForm(element);
                if (!form) {
                    throw new Error("jsf.ajax.request: Method must be called within a form");
                }
                var viewState = jsf.getViewState(form);

                // Set up additional arguments to be used in the request..
                // Make sure "javax.faces.partial.source" is set up.
                // If there were "execute" ids specified, make sure we
                // include the identifier of the source element in the
                // "execute" list.  If there were no "execute" ids
                // specified, determine the default.

                var args = {};

                args["javax.faces.partial.source"] = element.id;

                // RELEASE_PENDING Get rid of commas.  It's supposed to be spaces.
                if (options.execute) {
                    var temp = options.execute.replace(/\s+/, ' ').split(' ');
                    // RELEASE_PENDING refactor isInArray function
                    if (!isInArray(temp, element.name)) {
                        options.execute = element.name + " " + options.execute;
                    }
                } else {
                    options.execute = element.id;
                }

                args["javax.faces.partial.execute"] = options.execute.replace(/\s+/, ' ');
                if (options.render) {
                    args["javax.faces.partial.render"] = options.render.replace(/\s+/, ' ');
                }

                // remove non-passthrough options
                delete options.execute;
                delete options.render;
                delete options.onerror;
                delete options.onevent;
                // copy all other options to args
                for (var property in options) {
                    if (options.hasOwnProperty(property)) {
                        args[property] = options[property];
                    }
                }

                args["javax.faces.partial.ajax"] = "true";
                args["method"] = "POST";
                args["url"] = form.action;
                // add source
                var action = $(element);
                if (action && action.form) {
                    args[action.name] = action.value || 'x';
                } else {
                    args[element] = element;
                }

                var ajaxEngine = new AjaxEngine();
                ajaxEngine.setupArguments(args);
                ajaxEngine.queryString = viewState;
                ajaxEngine.context.onevent = onevent;
                ajaxEngine.context.onerror = onerror;
                ajaxEngine.context.source = element;
                ajaxEngine.sendRequest();
            },
            /**
             * <p>Receive an Ajax response from the server.
             * <p><b>Usage:</b></p>
             * <pre><code>
             * jsf.ajax.response(request, context);
             * </pre></code>
             * <p><b>Implementation Requirements:</b></p>
             * This function must evaluate the markup returned in the
             * <code>request.responseXML</code> object and perform the following action:
             * <ul>
             * <p>If there is no XML response returned, signal an <code>emptyResponse</code>
             * error. If the XML response does not follow the format as outlined
             * in Appendix A of the spec prose document <a
             *  href="../../javadocs/overview-summary.html#prose_document">linked in the
             *  overview summary</a> signal a <code>malformedError</code> error.  Refer to
             * section "Signaling Errors" in Chapter 13 of the spec prose document <a
             *  href="../../javadocs/overview-summary.html#prose_document">linked in the
             *  overview summary</a>.</p>
             * <p>If the response was successfully processed, send a <code>success</code>
             * event as outlined in Chapter 13 "Sending Events" section of the spec prose
             * document <a
             * href="../../javadocs/overview-summary.html#prose_document">linked in the
             * overview summary</a>.</p>
             * <p><i>Update Element Processing</i></p>
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
             * <p><i>Insert Element Processing</i></p>
             * <li>If an <code>&lt;insert&gt;</code> element is found in the response with the
             * attribute <code>before</code>:
             * <pre><code>&lt;insert id="insert id" before="before id"&gt;
             *    &lt;![CDATA[...]]&gt;
             * &lt;/insert&gt;</code></pre>
             * <ul>
             * <li>Extract this <code>&lt;insert&gt;</code> element's <code>CDATA</code> contents
             * from the response.</li>
             * <li>Find the DOM element whose identifier matches <code>before id</code> and insert
             * the <code>&lt;insert&gt;</code> element's <code>CDATA</code> content before
             * the DOM element in the document.</li>
             * </ul>
             * </li>
             * <li>If an <code>&lt;insert&gt;</code> element is found in the response with the
             * attribute <code>after</code>:
             * <pre><code>&lt;insert id="insert id" after="after id"&gt;
             *    &lt;![CDATA[...]]&gt;
             * &lt;/insert&gt;</code></pre>
             * <ul>
             * <li>Extract this <code>&lt;insert&gt;</code> element's <code>CDATA</code> contents
             * from the response.</li>
             * <li>Find the DOM element whose identifier matches <code>after id</code> and insert
             * the <code>&lt;insert&gt;</code> element's <code>CDATA</code> content after
             * the DOM element in the document.</li>
             * </ul>
             * </li>
             * <p><i>Delete Element Processing</i></p>
             * <li>If a <code>&lt;delete&gt;</code> element is found in the response:
             * <pre><code>&lt;delete id="delete id"/&gt;</code></pre>
             * Find the DOM element whose identifier matches <code>delete id</code> and remove it
             * from the DOM.</li>
             * <p><i>Element Attribute Update Processing</i></p>
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
             * <p><i>JavaScript Processing</i></p>
             * <li>If an <code>&lt;eval&gt;</code> element is found in the response:
             * <pre><code>&lt;eval&gt;
             *    &lt;![CDATA[...JavaScript...]]&gt;
             * &lt;/eval&gt;</code></pre>
             * <ul>
             * <li>Extract this <code>&lt;eval&gt;</code> element's <code>CDATA</code> contents
             * from the response and execute it as if it were JavaScript code.</li>
             * </ul>
             * </li>
             * <p><i>Redirect Processing</i></p>
             * <li>If a <code>&lt;redirect&gt;</code> element is found in the response:
             * <pre><code>&lt;redirect url="redirect url"/&gt;</code></pre>
             * Cause a redirect to the url <code>redirect url</code>.</li>
             * <p><i>Error Processing</i></p>
             * <li>If an <code>&lt;error&gt;</code> element is found in the response:
             * <pre><code>&lt;error&gt;
             *    &lt;error-name&gt;..fully qualified class name string...&lt;error-name&gt;
             *    &lt;error-message&gt;&lt;![CDATA[...]]&gt;&lt;error-message&gt;
             * &lt;/error&gt;</code></pre>
             * Extract this <code>&lt;error&gt;</code> element's <code>error-name</code> contents
             * and the <code>error-message</code> contents. Signal a <code>serverError</code> passing
             * the <code>errorName</code> and <code>errorMessage</code>.  Refer to
             * section "Signaling Errors" in Chapter 13 of the spec prose document <a
             *  href="../../javadocs/overview-summary.html#prose_document">linked in the
             *  overview summary</a>.</li>
             * <p><i>Extensions</i></p>
             * <li>The <code>&lt;extensions&gt;</code> element provides a way for framework
             * implementations to provide their own information.</li>
             * </ul>
             *
             * </p>
             *
             * @param request The <code>XMLHttpRequest</code> instance that
             * contains the status code and response message from the server.
             *
             * @param context An object containing the request context, including the following properties:
             * the source element, per call onerror callback function, and per call onevent callback function.
             *
             * @throws EmptyResponse error if request contains no data
             *
             * @function jsf.ajax.response
             */
            response: function response(request, context) {
                if (!request) {
                    throw new Error("jsf.ajax.response: Request parameter is unset");
                }

                var xmlReq = request;

                var xml = xmlReq.responseXML;
                if (xml === null) {
                    sendError(request, context, "emptyResponse");
                    return;
                }

                if (xml.firstChild.tagName === "parsererror") {
                    sendError(request, context, "malformedXML");
                    return;
                }

                var responseType = xml.getElementsByTagName("partial-response")[0].firstChild;

                if (responseType.nodeName === "error") { // it's an error
                    var errorName = responseType.firstChild.firstChild.nodeValue;
                    var errorMessage = responseType.firstChild.nextSibling.firstChild.nodeValue;
                    sendError(request, context, "serverError", errorName, errorMessage);
                    return;
                }


                if (responseType.nodeName === "redirect") {
                    window.location = responseType.getAttribute("url");
                    return;
                }


                if (responseType.nodeName !== "changes") {
                    sendError(request, context, "malformedXML");
                    return;
                }


                var changes = responseType.childNodes;


                for (var i = 0; i < changes.length; i++) {
                    switch (changes[i].nodeName) {
                        case "update":
                            doUpdate(changes[i]);
                            break;
                        case "delete":
                            doDelete(changes[i]);
                            break;
                        case "insert":
                            doInsert(changes[i]);
                            break;
                        case "attributes":
                            doAttributes(changes[i]);
                            break;
                        case "eval":
                            doEval(changes[i]);
                            break;
                        case "extension":
                        // RELEASE_PENDING no action?
                            break;
                        default:
                            sendError(request, context, "malformedXML");
                            return;
                    }
                }
                sendEvent(request, context, "success");

            }
        };
    }();

    /**
     *
     * <p>Return the value of <code>Application.getProjectStage()</code> for
     * the currently running application instance.  Calling this method must
     * not cause any network transaction to happen to the server.</p>
     * <p><b>Usage:</b></p>
     * <pre><code>
     * var stage = jsf.getProjectStage();
     * if (stage === ProjectStage.Development) {
     *  ...
     * } else if stage === ProjectStage.Production) {
     *  ...
     * }
     * </code></pre>
     *
     * @returns String <code>String</code> representing the current state of the
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
     * <p><b>Usage:</b></p>
     * <pre><code>
     * var state = jsf.getViewState(form);
     * </pre></code>
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
        if (!form) {
            throw new Error("jsf.getViewState:  form must be set");
        }
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
     * The namespace for JavaServer Faces JavaScript utilities.
     * @name jsf.util
     * @namespace
     */
    jsf.util = {};

    /**
     * <p>A varargs function that invokes an arbitrary number of scripts.
     * If any script in the chain returns false, the chain is short-circuited
     * and subsequent scripts are not invoked.  Any number of scripts may
     * specified after the <code>event</code> argument.</p>
     *
     * @param source The DOM element that triggered this Ajax request, or an 
     * id string of the element to use as the triggering element.
     * @param event The DOM event that triggered this Ajax request.  The
     * <code>event</code> argument is optional.
     *
     * @function jsf.util.chain
     */
    jsf.util.chain = function(source, event) {

        var length = arguments.length;
        if (length < 3)
            return;

        var thisArg = (typeof source === 'object') ? source : null;

        // Call back any scripts that were passed in
        for (var i = 2; i < arguments.length; i++) {

          var f = new Function("event", arguments[i]);
          var returnValue = f.call(thisArg, event);   

          if (returnValue === false)
            break;
        }
    }

    /**
     * <p>An integer specifying the specification version that this file implements.
     * It's format is: rightmost two digits, bug release number, next two digits,
     * minor release number, leftmost digits, major release number.
     * This number may only be incremented by a new release of the specification.</p>
     */
    jsf.specversion = 20000;

    /**
     * <p>An integer specifying the implementation version that this file implements.
     * It's a monotonically increasing number, reset with every increment of
     * <code>jsf.specversion</code>
     * This number is implementation dependent.</p>
     */
    jsf.implversion = 1;


} //end if version detection block
