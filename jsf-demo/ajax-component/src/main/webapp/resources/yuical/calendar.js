/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

if (typeof demo == "undefined" && !demo) {
    var demo = {};
}

if (typeof demo.calendar == "undefined" && !demo.calendar) {
    demo.calendar = {};
}

if (typeof demo.calendar.contextMap === "undefined" && !demo.calendar.contextMap) {
    demo.calendar.contextMap = [];
}

demo.calendar.init = function init(context, render) {

    // record the render attribute, if applied
    demo.calendar.contextMap[context] = render;

    demo.calendar.loader = new YAHOO.util.YUILoader({
        base: "http://ajax.googleapis.com/ajax/libs/yui/2.7.0/build/",
        require: ["calendar"],
        loadOptional: false,
        combine: false,
        filter: "RAW",
        allowRollup: false,
        onSuccess: function() {
            try {
                demo.calendar.cal1 = new YAHOO.widget.Calendar("demo.calendar.cal1", context+":calContainer");
                demo.calendar.cal1.render();
                demo.calendar.cal1.selectEvent.subscribe(demo.calendar.handleSelect, demo.calendar.cal1, true);
            } catch (e) {
                alert(e);
            }
        },
        // should a failure occur, the onFailure function will be executed
        onFailure: function(o) {
            alert("error: " + YAHOO.lang.dump(o));
        }

    });

    //
    // Calculate the dependency and insert the required scripts and css resources
    // into the document
    demo.calendar.loader.insert();
}

demo.calendar.handleSelect = function handleSelect(type, args, obj) {

    if (type === "select") {
        var calId = obj.containerId;
        var index = calId.indexOf(":") + 1;
        var tmpindex = calId.substring(index).indexOf(":") + 1;
        // keep looking until you get the last child index
        while (tmpindex !== 0) {
            index += tmpindex;
            tmpindex = calId.substr(index).indexOf(":") + 1;
        }
        var containerId = calId.substring(0,index - 1);
        var dateId = containerId + ":" + "date";
        var dates = args[0];
        var date = dates[0];
        var year = date[0], month = date[1], day = date[2];

        var txtDate = document.getElementById(dateId);
        txtDate.value = month + "/" + day + "/" + year;

        var render = demo.calendar.contextMap[containerId];
        try {
            // if a render is defined for the component, then include it.
            if (typeof render !== "undefined" && render ) {
                jsf.ajax.request(dateId,null,{
                    render: render,
                    execute: dateId
                })
            } else {
                jsf.ajax.request(dateId,null,{
                    execute: dateId
                })
            }
        } catch (e) {
            alert(e);
        }
    }
}
