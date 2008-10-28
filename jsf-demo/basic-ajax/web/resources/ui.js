/*
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 2 only ("GPL") or the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License. You can obtain
 a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.

 When distributing the software, include this License Header Notice in each
 file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 Sun designates this particular file as subject to the "Classpath" exception
 as provided by Sun in the GPL Version 2 section of the License file that
 accompanied this code.  If applicable, add the following below the License
 Header, with the fields enclosed by brackets [] replaced by your own
 identifying information: "Portions Copyrighted [year]
 [name of copyright owner]"

 Contributor(s):

 If you wish your version of this file to be governed by only the CDDL or
 only the GPL Version 2, indicate your decision by adding "[Contributor]
 elects to include this software in this distribution under the [CDDL or GPL
 Version 2] license."  If you don't indicate a single choice of license, a
 recipient has the option to distribute your version of this file under
 either the CDDL, the GPL Version 2 or to extend the choice of license to
 its licensees as provided above.  However, if you add GPL Version 2 code
 and therefore, elected the GPL Version 2 license, then the option applies
 only if the new code is made subject to such option by the copyright
 holder.
*/
var disabledImage = 'resources/button3.gif';
var enabledImage = 'resources/button2.gif';

function buttonpush(buttonName, element, event) {
    var button = document.getElementById(buttonName);
    if (!button.disabled) {
        button.src = disabledImage;
        button.disabled = true;
    }
    try {
        javax.faces.Ajax.ajaxRequest(element, event, {execute: buttonName, render: buttonName});
    } catch (ex) {
        // Handle errors here
        alert(ex);
    }
    return false;
} 

function msg(eventName, data) {
    var txt = null;
    if (typeof data.enqueue != 'undefined' || data.enqueue != null) {
        txt = document.createTextNode(data.enqueue.parameters["javax.faces.partial.execute"]);
        addCell(txt);
    } else if (typeof data.dequeue != 'undefined' || data.dequeue != null) {
        txt = document.createTextNode(data.dequeue.parameters["javax.faces.partial.execute"]);
        removeCell(txt);
    }
}

function addCell(cellData) {
    var cell = document.getElementById("tr1").insertCell(0);
    cell.setAttribute("height", "50px");
    cell.setAttribute("width", "50px");
    cell.innerHTML = cellData.nodeValue;
    cell.className = "queueCell";
}

function removeCell(cellData) {
    var row = document.getElementById("tr1");
    var cells = row.getElementsByTagName("td");
    if (typeof cells != 'undefined' || cells != null) {
        for (var i=0; i<cells.length; i++) {
            if (cells[i].firstChild.nodeValue == cellData.nodeValue) {
                row.deleteCell(i);
                var button = document.getElementById(cellData.nodeValue);
                button.disabled = false;
                button.src = enabledImage;
                break;
            }
        }
    }
}

// Set up the observer subscription

OpenAjax.hub.subscribe("javax.faces.AjaxEngine.Queue",msg);