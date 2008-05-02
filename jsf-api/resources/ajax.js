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

/*******************************************************************************
 * OpenAjax.js
 *
 * Reference implementation of the OpenAjax Hub, as specified by OpenAjax Alliance.
 * Specification is under development at: 
 *
 *   http://www.openajax.org/member/wiki/OpenAjax_Hub_Specification
 *
 * Copyright 2006-2008 OpenAjax Alliance
 ******************************************************************************/

// prevent re-definition of the OpenAjax object
if(!window["OpenAjax"]){
	OpenAjax = new function(){
		var t = true;
		var f = false;
		var g = window;
		var libs;
		var ooh = "org.openajax.hub.";

		var h = {};
		this.hub = h;
		h.implementer = "http://openajax.org";
		h.implVersion = "1.0";
		h.specVersion = "1.0";
		h.implExtraData = {};
		var libs = {};
		h.libraries = libs;

		h.registerLibrary = function(prefix, nsURL, version, extra){
			libs[prefix] = {
				prefix: prefix,
				namespaceURI: nsURL,
				version: version,
				extraData: extra 
			};
			this.publish(ooh+"registerLibrary", libs[prefix]);
		}
		h.unregisterLibrary = function(prefix){
			this.publish(ooh+"unregisterLibrary", libs[prefix]);
			delete libs[prefix];
		}

		h._subscriptions = { c:{}, s:[] };
		h._cleanup = [];
		h._subIndex = 0;
		h._pubDepth = 0;

		h.subscribe = function(name, callback, scope, subscriberData, filter)			
		{
			if(!scope){
				scope = window;
			}
			var handle = name + "." + this._subIndex;
			var sub = { scope: scope, cb: callback, fcb: filter, data: subscriberData, sid: this._subIndex++, hdl: handle };
			var path = name.split(".");
	 		this._subscribe(this._subscriptions, path, 0, sub);
			return handle;
		}

		h.publish = function(name, message)		
		{
			var path = name.split(".");
			this._pubDepth++;
			this._publish(this._subscriptions, path, 0, name, message);
			this._pubDepth--;
			if((this._cleanup.length > 0) && (this._pubDepth == 0)) {
				for(var i = 0; i < this._cleanup.length; i++) 
					this.unsubscribe(this._cleanup[i].hdl);
				delete(this._cleanup);
				this._cleanup = [];
			}
		}

		h.unsubscribe = function(sub) 
		{
			var path = sub.split(".");
			var sid = path.pop();
			this._unsubscribe(this._subscriptions, path, 0, sid);
		}
		
		h._subscribe = function(tree, path, index, sub) 
		{
			var token = path[index];
			if(index == path.length) 	
				tree.s.push(sub);
			else { 
				if(typeof tree.c == "undefined")
					 tree.c = {};
				if(typeof tree.c[token] == "undefined") {
					tree.c[token] = { c: {}, s: [] }; 
					this._subscribe(tree.c[token], path, index + 1, sub);
				}
				else 
					this._subscribe( tree.c[token], path, index + 1, sub);
			}
		}

		h._publish = function(tree, path, index, name, msg) {
			if(typeof tree != "undefined") {
				var node;
				if(index == path.length) {
					node = tree;
				} else {
					this._publish(tree.c[path[index]], path, index + 1, name, msg);
					this._publish(tree.c["*"], path, index + 1, name, msg);			
					node = tree.c["**"];
				}
				if(typeof node != "undefined") {
					var callbacks = node.s;
					var max = callbacks.length;
					for(var i = 0; i < max; i++) {
						if(callbacks[i].cb) {
							var sc = callbacks[i].scope;
							var cb = callbacks[i].cb;
							var fcb = callbacks[i].fcb;
							var d = callbacks[i].data;
							if(typeof cb == "string"){
								// get a function object
								cb = sc[cb];
							}
							if(typeof fcb == "string"){
								// get a function object
								fcb = sc[fcb];
							}
							if((!fcb) || 
							   (fcb.call(sc, name, msg, d))) {
								cb.call(sc, name, msg, d);
							}
						}
					}
				}
			}
		}
			
		h._unsubscribe = function(tree, path, index, sid) {
			if(typeof tree != "undefined") {
				if(index < path.length) {
					var childNode = tree.c[path[index]];
					this._unsubscribe(childNode, path, index + 1, sid);
					if(childNode.s.length == 0) {
						for(var x in childNode.c) 
					 		return;		
						delete tree.c[path[index]];	
					}
					return;
				}
				else {
					var callbacks = tree.s;
					var max = callbacks.length;
					for(var i = 0; i < max; i++) 
						if(sid == callbacks[i].sid) {
							if(this._pubDepth > 0) {
								callbacks[i].cb = null;	
								this._cleanup.push(callbacks[i]);						
							}
							else
								callbacks.splice(i, 1);
							return; 	
						}
				}
			}
		}
		// The following function is provided for automatic testing purposes.
		// It is not expected to be deployed in run-time OpenAjax Hub implementations.
		h.reinit = function()
		{
			for (var lib in OpenAjax.hub.libraries) {
				delete OpenAjax.hub.libraries[lib];
			}
			OpenAjax.hub.registerLibrary("OpenAjax", "http://openajax.org/hub", "1.0", {});

			delete OpenAjax._subscriptions;
			OpenAjax._subscriptions = {c:{},s:[]};
			delete OpenAjax._cleanup;
			OpenAjax._cleanup = [];
			OpenAjax._subIndex = 0;
			OpenAjax._pubDepth = 0;
		}
	};
	// Register the OpenAjax Hub itself as a library.
	OpenAjax.hub.registerLibrary("OpenAjax", "http://openajax.org/hub", "1.0", {});

}


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
 * Our top level namespace
 */
javax.faces.Ajax = function() {

    // private functions

    var setOptions = function(options) {
        this.options= {};
        for (prop in options) {
            this.options[prop] = prop;
        }
    }

    var getForm = function(element) {
        if (element) {
            var form = $(element);
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

    var getFormElements = function(form, options) {
        var ignoreTypes = ['button','submit','reset','image'];
        var elements = new Array();
        if (options && options.inputs) {
            var inputs = options.inputs.split(",");
            if (inputs) {
                for (i = 0; i < inputs.length; i++) {
                    var element = document.getElementById(inputs[i]);
                    if (element && (ignoreTypes.indexOf(element.type) == -1)) {
                        elements[i] = element;
                    }
                }
                return elements;
            }
        }
        var ignore;
        if (options && options.ignore) {
            ignore = options.ignore.split(",");
        }
        for (var tagName in formElements) {
            var tagElements = form.getElementsByTagName(tagName);
            var isIgnore = "false";
            for (var j = 0; j < tagElements.length; j++) {
                if (ignore) {
                    for (var k = 0; k < ignore.length; k++) {
                        if (tagElements[j].id == ignore[k]) {
                            isIgnore = "true"; 
                            break;
                        }
                    }
                }
                if (isIgnore == "false" && (ignoreTypes.indexOf(tagElements[j].type) == -1)) {
                    elements.push(tagElements[j]);
                } else {
                    isIgnore = "false";
                }
            }
        }
        return elements;
    }

    var $ = function() {
        var results = [], element;
        for (var i = 0; i < arguments.length; i++) {
            element = arguments[i];
            if (typeof element == 'string')
                element = document.getElementById(element);
            results.push(element);
        }
        return reduce(results);
    }

    var reduce = function(toReduce) {
        return toReduce.length > 1 ? toReduce : toReduce[0];
    }

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

    var serializeElement = function(element) {
        element = $(element);
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
    }

    /****************************
     * public functions
     ****************************/

    return  {

        /**
         * <p><span class="changed_added_2_0">Collect</span> and encode
         * state for input controls associated with the specified
         * <code>form</code> element.
         *
         * @param form The <code>form</code> element whose contained
         * <code>input</code> controls will be collected and encoded.
         * @param options An associative array containing options that 
         * specify which controls should be included or ignored during the
         * collection process.
         */
        viewState:function(form, options) {
            if (options != null && options != 'undefined') {
                if (options.constructor != Array) {
                    // error
                }
            }

            setOptions(options);

            var elements = getFormElements(form, options);
            var queryComponents = new Array();
            var javaxViewState = document.getElementById("javax.faces.ViewState");
            var queryComponent = null;
            for (var i = 0; i < elements.length; i++) {
                queryComponent = serializeElement(elements[i]);
                if (queryComponent) {
                    // We'll add the viewState at the end..
                    if (-1 == queryComponent.indexOf("javax.faces.ViewState")) {
                        queryComponents.push(queryComponent);
                    }
                }
            }

            // include the element that triggered this event (the source)
            if (options.source)
                queryComponents.push(serializeElement(options.source));

            if (javaxViewState)
                queryComponents.push(serializeElement(javaxViewState));

            viewState = queryComponents.join('&');

            return viewState;

        }
    };
}(); 
