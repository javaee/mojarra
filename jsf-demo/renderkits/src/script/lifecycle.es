/* Copyright 2005 Sun Microsystems, Inc. All rights reserved. You may not modify, use, reproduce, or distribute this software except in compliance with the terms of the License at: http://developer.sun.com/berkeley_license.html
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

    /**
     * 'Initial request' simulation
     */ 
    function initialMove() {
        coordinates = new Array(155, 270, 25, 270);
        idx = 0;
        posX = 25;
        posY = 120;
        deltaX = 1;
        deltaY = 1;
        setGraphicInfo();
        toX = 160;
        toY = 120;
        clearInterval(timer);
        timer = setInterval("moveControlPoints()", delay);
        clearErrorPaths(); 
    }

    /**
     * 'Postback' simulation
     */
    function postbackMove() {
        coordinates = new Array(780, 275, 25, 270);
        idx = 0;
        posX = 25;
        posY = 120;
        deltaX = 1;
        deltaY = 1;
        setGraphicInfo();
        toX = 780;
        toY = 120;
        clearInterval(timer);
        timer = setInterval("moveControlPoints()", delay);
        clearErrorPaths(); 
    }

    /**
     * 'Validation error' simulation
     */
    function postbackConValMove() {
        coordinates = new Array(660, 200, 170, 200, 170, 270, 25, 270);
        idx = 0;
        posX = 25;
        posY = 120;
        deltaX = 1;
        deltaY = 1;
        setGraphicInfo();
        toX = 660;
        toY = 120;
        clearInterval(timer);
        timer = setInterval("moveControlPoints()", delay);
        clearErrorPaths(); 
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

        if (posX == 660 && posY == 125 && convalError == "true") {
            showConValError();
        }
    }

    /**
     * Calculate coordinates
     */
    function moveControlPoints() {
        if (posX < toX) {
            posX = posX + deltaX;
            vertical = "false";
        } else if (posX > toX) {
            posX = posX - deltaX;
            vertical = "false";
        }
        if (posY < toY) {
            posY = posY + deltaY;
            vertical = "true";
        } else if (posY > toY) {
            posY = posY - deltaY;
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
        

