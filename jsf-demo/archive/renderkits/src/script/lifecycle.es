/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2005-2011 Oracle and/or its affiliates. All rights reserved.
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

/**
 * Global Variables
 */
    var delay = 5;
    var timer;
    var posX;
    var posY;
    var deltaX;
    var deltaY;
    var toX;
    var toY;
    var coordinates;
    var idx;
    var toggle = "stop";
    var vertical = "false";
    var convalError = "false";
    var initialRequest = "false";
    var postback = "false";

    /**
     * 'Initial request' simulation
     */ 
    function initialMove() {
        resetDemo();
        initialRequest = "true";
        coordinates = new Array(155, 270, 25, 270);
        idx = 0;
        posX = 25;
        posY = 120;
        deltaX = 2;
        deltaY = 2;
        setGraphicInfo();
        toX = 160;
        toY = 120;
        clearInterval(timer);
        timer = setInterval("moveControlPoints()", delay);
    }

    /**
     * 'Postback' simulation
     */
    function postbackMove() {
        resetDemo();
        postback = "true";
        coordinates = new Array(780, 275, 25, 270);
        idx = 0;
        posX = 25;
        posY = 120;
        deltaX = 2;
        deltaY = 2;
        setGraphicInfo();
        toX = 780;
        toY = 120;
        clearInterval(timer);
        timer = setInterval("moveControlPoints()", delay);
    }

    /**
     * 'Validation error' simulation
     */
    function postbackConValMove() {
        resetDemo();
        postback = "true";
        coordinates = new Array(660, 200, 170, 200, 170, 270, 25, 270);
        idx = 0;
        posX = 25;
        posY = 120;
        deltaX = 2;
        deltaY = 2;
        setGraphicInfo();
        toX = 660;
        toY = 120;
        clearInterval(timer);
        timer = setInterval("moveControlPoints()", delay);
        convalError = "true";
    }

    /**
     * Dynamically update 'request' symbol coordinates to
     * give the appearance of motion.
     */
    function setGraphicInfo() {
        var rect = window.document.getElementById("form:request");
        rect.setAttribute("x", posX);
        rect.setAttribute("y", posY);
        if (vertical == "true") {
            rect.setAttribute("width", "10");
            rect.setAttribute("height", "15");
        } else {
            rect.setAttribute("width", "15");
            rect.setAttribute("height", "10");
        }

        if (posX == 660 && posY == 120 && convalError == "true") {
            showConValError();
            var messagePanel = window.document.getElementById("form:messagePanel");
            messagePanel.setAttribute("style", "stroke:red; fill:#f0e68c; visibility:visible");
            var msg1 = window.document.getElementById("form:msg1");
            msg1.firstChild.nodeValue = "Process Validations Phase:";
            msg1.setAttribute("style", "stroke:red; fill:none; visibility:visible");
            var msg2 = window.document.getElementById("form:msg2");
            msg2.firstChild.nodeValue = "1. validation error transpired..  ";
            msg2.setAttribute("style", "stroke:red; fill:none; visibility:visible");
            var msg3 = window.document.getElementById("form:msg3");
             msg3.firstChild.nodeValue = "2. render response signal set";
            msg3.setAttribute("style", "stroke:red; fill:none; visibility:visible");
        }
        if (posX == 105 && posY == 120) {
            var messagePanel = window.document.getElementById("form:messagePanel");
            messagePanel.setAttribute("style", "stroke:red; fill:#f0e68c; visibility:visible");
            var msg1 = window.document.getElementById("form:msg1");
            msg1.firstChild.nodeValue = "Restore View Phase:";
            msg1.setAttribute("style", "stroke:red; fill:none; visibility:visible");
            if (initialRequest == "true") {
                var msg2 = window.document.getElementById("form:msg2");
                msg2.firstChild.nodeValue = "1. new view root created";
                msg2.setAttribute("style", "stroke:red; fill:none; visibility:visible");
                var msg3 = window.document.getElementById("form:msg3");
                msg3.firstChild.nodeValue = "2. render response signal set";
                msg3.setAttribute("style", "stroke:red; fill:none; visibility:visible");
                initialRequest == "false";
            } else {
                var msg2 = window.document.getElementById("form:msg2");
                msg2.firstChild.nodeValue = "1. existing view restored";
                msg2.setAttribute("style", "stroke:red; fill:none; visibility:visible");
            }
        } else if ((posX >= 100 && posX <= 200) && (posY >= 250 && posY <= 300)) {
            clearMessages();
            var messagePanel = window.document.getElementById("form:messagePanel");
            messagePanel.setAttribute("style", "stroke:red; fill:#f0e68c; visibility:visible");
            var msg1 = window.document.getElementById("form:msg1");
            msg1.firstChild.nodeValue = "Render Response Phase:";
            msg1.setAttribute("style", "stroke:red; fill:none; visibility:visible");
            var msg2 = window.document.getElementById("form:msg2");
            msg2.firstChild.nodeValue = "1. component view created";
            msg2.setAttribute("style", "stroke:red; fill:none; visibility:visible");
            var msg3 = window.document.getElementById("form:msg3");
            msg3.firstChild.nodeValue = "2. renderers generate markup";
            msg3.setAttribute("style", "stroke:red; fill:none; visibility:visible");
        } else if (posX == 355 && posY == 120) {
            clearMessages();
            var messagePanel = window.document.getElementById("form:messagePanel");
            messagePanel.setAttribute("style", "stroke:red; fill:#f0e68c; visibility:visible");
            var msg1 = window.document.getElementById("form:msg1");
            msg1.firstChild.nodeValue = "Apply Request Values Phase:";
            msg1.setAttribute("style", "stroke:red; fill:none; visibility:visible");
            var msg2 = window.document.getElementById("form:msg2");
            msg2.firstChild.nodeValue = "1. request parameter values propogated to component local values..  ";
            msg2.setAttribute("style", "stroke:red; fill:none; visibility:visible");
            var msg3 = window.document.getElementById("form:msg3");
            msg3.firstChild.nodeValue = "2. ActionEvent(s) queued for Command components.. ";
            msg3.setAttribute("style", "stroke:red; fill:none; visibility:visible");
        } else if (posX == 605 && posY == 120) {
            clearMessages();
            var messagePanel = window.document.getElementById("form:messagePanel");
            messagePanel.setAttribute("style", "stroke:red; fill:#f0e68c; visibility:visible");
            var msg1 = window.document.getElementById("form:msg1");
            msg1.firstChild.nodeValue = "Process Validations Phase:";
            msg1.setAttribute("style", "stroke:red; fill:none; visibility:visible");
            var msg2 = window.document.getElementById("form:msg2");
            msg2.firstChild.nodeValue = "1. validators are invoked..  ";
            msg2.setAttribute("style", "stroke:red; fill:none; visibility:visible");
            var msg3 = window.document.getElementById("form:msg3");
            msg3.firstChild.nodeValue = "2. ValueChangeEvent(s) queued for valid values.. ";
            msg3.setAttribute("style", "stroke:red; fill:none; visibility:visible");
        } else if ((posX >= 600 && posX <= 720) && (posY >= 250 && posY <= 300)) {
            clearMessages();
            var messagePanel = window.document.getElementById("form:messagePanel");
            messagePanel.setAttribute("style", "stroke:red; fill:#f0e68c; visibility:visible");
            var msg1 = window.document.getElementById("form:msg1");
            msg1.firstChild.nodeValue = "Update Model Values Phase:";
            msg1.setAttribute("style", "stroke:red; fill:none; visibility:visible");
            var msg2 = window.document.getElementById("form:msg2");
            msg2.firstChild.nodeValue = "1. component values propogated to model objects..  ";
            msg2.setAttribute("style", "stroke:red; fill:none; visibility:visible");
        } else if ((posX >= 350 && posX <= 470) && (posY >= 250 && posY <= 300)) {
            clearMessages();
            var messagePanel = window.document.getElementById("form:messagePanel");
            messagePanel.setAttribute("style", "stroke:red; fill:#f0e68c; visibility:visible");
            var msg1 = window.document.getElementById("form:msg1");
            msg1.firstChild.nodeValue = "Invoke Applications Phase:";
            msg1.setAttribute("style", "stroke:red; fill:none; visibility:visible");
            var msg2 = window.document.getElementById("form:msg2");
            msg2.firstChild.nodeValue = "1. application events handled..  ";
            msg2.setAttribute("style", "stroke:red; fill:none; visibility:visible");
            var msg3 = window.document.getElementById("form:msg3");
            msg3.firstChild.nodeValue = "2. next view is determined.. ";
            msg3.setAttribute("style", "stroke:red; fill:none; visibility:visible");
        }

    }

    /**
     * Calculate coordinates
     */
    function moveControlPoints() {
        if (posX < toX) {
            posX = posX + deltaX;
            if (posX >= toX) {
                posX = toX;
            }
            vertical = "false";
        } else if (posX > toX) {
            posX = posX - deltaX;
            if (posX <= toX) {
                posX = toX;
            }
            vertical = "false";
        }
        if (posY < toY) {
            posY = posY + deltaY;
            if (posY >= toY) {
                posY = toY;
            }
            vertical = "true";
        } else if (posY > toY) {
            posY = posY - deltaY;
            if (posY <= toY) {
                posY = toY;
            }
            vertical = "true";
        }
        setGraphicInfo();
        if (posX == toX && posY == toY) {
            clearInterval(timer);
            toX = coordinates[idx];
            idx++;
            toY= coordinates[idx];
            idx++;
            timer = setInterval("moveControlPoints()", delay);
        }
    }

    /**
     * Stop/Restore 
     */
    function stopMovement() {
        clearInterval(timer);
        if (toggle == "stop") {
            toggle = "resume";
            timer = setInterval("moveControlPoints()", delay);
        } else if (toggle == "resume") {
            toggle = "stop";
            clearInterval(timer);
        }
    }

    /**
     * Reset Demo
     */
    function resetDemo() {
        clearInterval(timer);
        clearErrorPaths();
        clearMessages();
        var request = window.document.getElementById("form:request");
        request.setAttribute("x", "25");
        request.setAttribute("y", "125");
        initialRequest = "false";
        postback = "false";
    }


    function showConValError() {
        var convalError1 = window.document.getElementById("form:convalError1");
        convalError1.setAttribute("style", "stroke:red; fill: none; stroke-dasharray:9,5; visibility:visible");
        var convalError2 = window.document.getElementById("form:convalError2");
        convalError2.setAttribute("style", "stroke:red; fill: none; stroke-dasharray:9,5; visibility:visible");
        var convalError3 = window.document.getElementById("form:convalError3");
        convalError3.setAttribute("style", "stroke:red; fill: none; stroke-dasharray:9,5; visibility:visible");
    }
                                                                                                                         
    function clearErrorPaths() {
        var convalError1 = window.document.getElementById("form:convalError1");
        convalError1.setAttribute("style", "stroke:red; fill: none; stroke-dasharray:9,5; visibility:hidden");
        var convalError2 = window.document.getElementById("form:convalError2");
        convalError2.setAttribute("style", "stroke:red; fill: none; stroke-dasharray:9,5; visibility:hidden");
        var convalError3 = window.document.getElementById("form:convalError3");
        convalError3.setAttribute("style", "stroke:red; fill: none; stroke-dasharray:9,5; visibility:hidden");
    }

    function clearMessages() {
        var messagePanel = window.document.getElementById("form:messagePanel");
        messagePanel.setAttribute("style", "stroke:red; fill:#f0e68c; visibility:hidden");
        var msg1 = window.document.getElementById("form:msg1");
        msg1.setAttribute("style", "stroke:red; fill: none; visibility:hidden");
        var msg2 = window.document.getElementById("form:msg2");
        msg2.setAttribute("style", "stroke:red; fill: none; visibility:hidden");
        var msg3 = window.document.getElementById("form:msg3");
        msg3.setAttribute("style", "stroke:red; fill: none; visibility:hidden");
    }

        

