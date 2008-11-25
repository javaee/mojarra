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

// RELEASE_PENDING need to decide on naming for AjaxEngine
var mojarra = mojarra || {};

/**
 * AjaxEngine contains the JavaScript for performing Ajax functions. 
 */
jsf.AjaxEngine = function() {

    var req = {};                  // Request Object
    req.url = null;                // Request URL
    req.xmlReq = null;             // XMLHttpRequest Object
    req.async = true;              // Default - Asynchronous
    req.parameters = {};           // Parameters For GET or POST
    req.queryString = null;        // Encoded Data For GET or POST
    req.method = null;             // GET or POST
    req.responseTxt = null;        // Response Content (Text)
    req.responseXML = null;        // Response Content (XML)
    req.status = null;             // Response Status Code From Server
    req.fromQueue = false;         // Indicates if the request was taken off the queue
                                   // before being sent.  This prevents the request from
                                   // entering the queue redundantly.

    req.que = jsf.AjaxEngine.Queue;

    // Get an XMLHttpRequest Handle
    req.xmlReq = jsf.AjaxEngine.getTransport();
    if (req.xmlReq === null) {
        return null;
    }

    // Set up request/response state callbacks
    req.xmlReq.onreadystatechange = function() {
        if (req.xmlReq.readyState === 4) {
            req.onComplete(req);
        }
    };

    /**
     * This function is called when the request/response interaction
     * is complete.  'onComplete', 'onSuccess' or 'onError' callbacks
     * will be called if they have been registered,
     * If the return status code is successfull, dequeue all requests
     * from the queue that have completed.  If a request has been found
     * on the queue that has not been sent, send the request.
     */
    req.onComplete = function onComplete() {
        var status = req.xmlReq.status;
        if ((status !== null && typeof status !== 'undefined' && status !== 0) && (status >= 200 && status < 300)) {
            jsf.ajax.response(req.xmlReq);
        } else {
            jsf.AjaxEngine.sendError(req);
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
     */
    req.setupArguments = function(args) {
        for (var i in args) {
            if (typeof req[i] === 'undefined') {
                req.parameters[i] = args[i];
            } else {
                req[i] = args[i];
            }
        }
    };

    /**
     * This function does final encoding of parameters, determines the request method
     * (GET or POST) and sends the request using the specified url.
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
                if (req.queryString.length > 0) {
                    req.queryString += "&";
                }
                req.queryString += encodeURIComponent(i) + "=" + encodeURIComponent(req.parameters[i]);
            }
            if (req.method === "GET") {
                if (req.queryString.length > 0) {
                    req.url += ((req.url.indexOf("?") > -1) ? "&" : "?") + req.queryString;
                }
            }
            req.xmlReq.open(req.method, req.url, req.async);
            if (req.method === "POST") {
                if (typeof req.xmlReq.setRequestHeader !== 'undefined') {
                    req.xmlReq.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                }
                content = req.queryString;
            }
            req.xmlReq.send(content);
        }
    };

    return req;
};

/**
 * Utility function to serialize form elements.
 */
jsf.AjaxEngine.serializeForm = function(theform) {
    var els = theform.elements;
    var len = els.length;
    var qString = "";
    this.addField = function(name, value) {
        if (qString.length > 0) {
            qString += "&";
        }
        qString += encodeURIComponent(name) + "=" + encodeURIComponent(value);
    };
    for (var i = 0; i < len; i++) {
        var el = els[i];
        if (!el.disabled) {
            switch (el.type) {
                case 'text': case 'password': case 'hidden': case 'textarea':
                    this.addField(el.name, el.value);
                    break;
                case 'select-one':
                    if (el.selectedIndex >= 0) {
                        this.addField(el.name, el.options[el.selectedIndex].value);
                    }
                    break;
                case 'select-multiple':
                    for (var j = 0; j < el.options.length; j++) {
                        if (el.options[j].selected) {
                            this.addField(el.name, el.options[j].value);
                        }
                    }
                    break;
                case 'checkbox': case 'radio':
                    this.addField(el.name, el.checked+"");
                    break;
            }
        }
    }
    return qString;
};

/**
 * Utility function to get an XMLHttpRequest handle.
 */
jsf.AjaxEngine.getTransport = function() {
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
 * Simple queue implementaton.
 */

if (!window["jsf.AjaxEngine.Queue"]) {
  jsf.AjaxEngine.Queue = new function() {

        var utils = jsf.Utils;

        // Create the internal queue
        var queue = [];


        // the amount of space at the front of the queue, initialised to zero
        var queueSpace = 0;

        /* Returns the size of this Queue. The size of a Queue is equal to the number
         * of elements that have been enqueued minus the number of elements that have
         * been dequeued.
         */
        this.getSize = function getSize() {
            return queue.length - queueSpace;
        };

        /* Returns true if this Queue is empty, and false otherwise. A Queue is empty
         * if the number of elements that have been enqueued equals the number of
         * elements that have been dequeued.
         */
        this.isEmpty = function isEmpty() {
            return (queue.length === 0);
        };

        /* Enqueues the specified element in this Queue.
         * After the element is put in the queue, an event is fired.
         *
         * @param element - the element to enqueue
         */
        this.enqueue = function enqueue(element) {
            // Queue the request
            queue.push(element);

            // Send the message that the request is enqueued
            jsf.AjaxEngine.sendMessage("Event","enqueue","request enqueued", element);
        };



        /* Dequeues an element from this Queue. The oldest element in this Queue is
         * removed and returned. If this Queue is empty then undefined is returned.
         *
         * @returns The element that was removed rom the queue.
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
            if (element != "undefined") {
                jsf.AjaxEngine.sendMessage("Event","dequeue","request dequeued", element);
            }

            // return the removed element
            return element;
        };

    /* Returns the oldest element in this Queue. If this Queue is empty then
     * undefined is returned. This function returns the same value as the dequeue
     * function, but does not remove the returned element from this Queue.
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
}

/**
 * Send an error to the user via an OpenAjax message.
 * @param element object which caused the error
 * @private
 */
jsf.AjaxEngine.sendError = function(request) {

    if (!request) {
        throw new Error("AjaxEngine.sendError:  invalid value passed as argument");
    }

    if (request.xmlReq.status === 0) {
        jsf.AjaxEngine.sendMessage("Error", "SERVERDOWN",
                "Cannot communicate with server", request );
    } else if (request.xmlReq.status == 404) {
        jsf.AjaxEngine.sendMessage("Error", "NOTFOUND",
                "URL not found on server", request);
    } else if (request.xmlReq.status == 500) {
        jsf.AjaxEngine.sendMessage("Error", "SERVERERROR",
                "Server Error prevents completing request", request);
    } else { //
        jsf.AjaxEngine.sendMessage("Error", "MISCSERVER",
                "There was an error on the server", request);
    }
};

/**
 * Publish an event message to the user via OpenAjax, to every client listed in target
 * @param type of message - either "Event" or "Error"
 * @param name Name of the message to pass
 * @param message Message string
 * @param request XMLHttpRequest object associated with this message
 * @throws Error if params are not set, or
 * @private
 */

jsf.AjaxEngine.sendMessage = function(type, name, message, request){

    if (!type || !name || !message || !request || (type !== "Event" && type !== "Error")) {
        throw new Error("AjaxEngine.sendMessage: invalid value passed as argument");
    }

    var utils = jsf.Utils;

    var args = {};
    args.type = type;
    args.name = name;
    args.statusMessage = message;
    args.execute = request.parameters["javax.faces.partial.execute"];
    args.render = request.parameters["javax.faces.partial.render"];
    args.statusCode = request.xmlReq.status;
    //args.readyState = request.xmlReq.readyState;

    var execArray = utils.execConvert(request.parameters["javax.faces.partial.execute"]);

    if (execArray[0] == "none" || execArray[0] == "all") {
        OpenAjax.hub.publish("javax.faces."+type, args);        
    }
    for (var exec in execArray) {
        OpenAjax.hub.publish("javax.faces."+type+"."+execArray[exec], args);
    }
};