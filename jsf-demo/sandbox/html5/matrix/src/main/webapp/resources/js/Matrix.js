/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2013 Oracle and/or its affiliates. All rights reserved.
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

var canvas;
var context;
var matrix;

/**
 * Initialize canvas and matrix.
 */
window.onload = function() {
    canvas = document.getElementById('canvas');
    context = canvas.getContext('2d');
    canvas.addEventListener("click", sendPixel, false);
    initDemo(context);
    showColorValue();
}

/**
 * Draw on the canvas.
 */
var draw = function (context, x, y, fillcolor, radius, linewidth, strokestyle) {
    context.beginPath();
    context.arc(x, y, radius, 0, 2 * Math.PI, false);
    context.fillStyle = fillcolor;
    context.fill();
    context.lineWidth = linewidth;
    context.strokeStyle = strokestyle;
    context.stroke();
};

/**
 * Circle shape containing coordinates.
 */
var Circle = function(x, y, radius) {
    this.x = x;
    this.y = y;
    this.left = x - radius;
    this.top = y - radius;
    this.right = x + radius;
    this.bottom = y + radius;
};

/**
 * Draw a circle shape on canvas.
 */
var drawCircle = function (context, x, y, fillcolor, radius, linewidth, strokestyle) {
    draw(context, x, y, fillcolor, radius, linewidth, strokestyle);
    var circle = new Circle(x, y, radius);
    return circle;
};

/**
 * Initializes canvas matrix.
 */
function initDemo(context) {
    matrix = new Array(8);
    for (var i=0; i<8; i++) {
        matrix[i] = new Array(8);
    }
    var x = 40;
    var y = 20;
    for (var i=0; i<8; i++) {
        for (var j=0; j<8; j++) {
            matrix[j][i] = drawCircle(context, x, y, "#cdc9c9", 12, 2, "#003300");
            x += 40;
        }
        x = 40;
        y += 40;
    }

    if (!Modernizr.inputtypes.color) {
        var parent = document.getElementById("color");
        var child = document.getElementById("clr");
        parent.removeChild(child);
        var input = document.createElement("input");
        input.className = "color";
        input.id = "clr";
        input.value="00bedd";
        input.setAttribute("onchange","showColorValue(); return false;");
        parent.appendChild(input);
        new jscolor.color(document.getElementById("clr"));
    }
}

/**
 * Determines the circle (representing a matrix pixel) that was clicked.
 * Produces a pipe delimited string:
 * <command> | <color> | <coordinates>
 * where command can be:
 * "P" (Control individual pixel)
 * "X" (control rows)
 * "Y" (control columns)
 * "D" (control diagonal)
 * "CP" (Control individual pixel - no trail)
 * "CX" (control rows - no trail)
 * "CY" (control columns - no trail)
 * "CD" (control diagonal - no trail)
 * "R" (reset)
 */
function sendPixel(event) {
    var sendStr;
    var clickedX = event.pageX - this.offsetLeft;
    var clickedY = event.pageY - this.offsetTop;
    for (var i = 0; i < 8; i++) {
        for (var j = 0; j < 8; j++) {
            if (clickedX < matrix[i][j].right && clickedX > matrix[i][j].left &&
                clickedY > matrix[i][j].top && clickedY < matrix[i][j].bottom) {
                if (document.getElementById("trail").checked) {
                    sendStr = "P|" + rgbClr.innerHTML + "|" +  i + "," + j; 
                } else {
                    sendStr = "CP|" + rgbClr.innerHTML + "|" +  i + "," + j; 
                }
                send(sendStr); return false;
            }

        }
    }
}

/**
 * Clears canvas to initial state.
 */
function resetMatrix() {
    canvas.removeEventListener("click", sendPixel, false);
    send("R"); return false;
}

/**
 * Sets "click" event listener for individual pixel control.
 */
function setPixel() {
    canvas.addEventListener("click", sendPixel, false);
}

/**
 * Handler for the "row" radio option.
 * Sends the "CX" command.
 */
function setRow() {
    var sendStr;
    canvas.removeEventListener("click", sendPixel, false);
    if (document.getElementById("trail").checked) {
        sendStr = "X|" + rgbClr.innerHTML;
    } else {
        sendStr = "CX|" + rgbClr.innerHTML;
    }
    send(sendStr); return false;
}

/**
 * Handler for the "column" radio option.
 * Send the "Y" command (pixel trail set).
 * Sends the "CY" command (if pixel trail option is not set).
 */
function setColumn() {
    var sendStr;
    canvas.removeEventListener("click", sendPixel, false);
    if (document.getElementById("trail").checked) {
        sendStr = "Y|" + rgbClr.innerHTML;
    } else {
        sendStr = "CY|" + rgbClr.innerHTML;
    }
    send(sendStr); return false;
}

/**
 * Handler for the "diagonal" radio option.
 * Sends the "D" command (pixel trail set)..
 * Sends the "CD" command (if pixel trail option is not set).
 */
function setDiagonal() {
    var sendStr;
    canvas.removeEventListener("click", sendPixel, false);
    if (document.getElementById("trail").checked) {
        sendStr = "D|" + rgbClr.innerHTML;
    } else {
        sendStr = "CD|" + rgbClr.innerHTML;
    }
    send(sendStr); return false;
}

function sendMessage() {
    var sendStr;
    var msg = document.getElementById("msg");
    canvas.removeEventListener("click", sendPixel, false);
    sendStr = "M|" + rgbClr.innerHTML + "|" + msg.value;
    send(sendStr); return false;
}

/**
 * Sends a Web Socket message.
 */
function send(msg, arg) {
    if (arg !== undefined) msg = msg + "," + arg;
    var socket = JSF.ws.getSocket("ws://HOSTNAME:8021/matrix/matrix");
    socket.send(msg);
}

/**
 * Web Socket callback that updates the matrix UI.
 */
function matrixHandler(eventData) {
    if (eventData.length == 0) {
        return;
    }
    var i = 0;
    var j = 0;
    var data = eventData.data;
    var values = data.split("|");
    var rgb = "rgb(" + values[1] + ")";
    var decrement = false;
    var myTimer = null;
    var level = 0;
    if (values[0] == "R") {
        reset();
        return;
    } else if (values[0] == "P") {
        var coords = values[2].split(",");
        i = coords[0];
        j = coords[1];
        var rgb = "rgb(" + values[1] + ")";
        draw(context, matrix[i][j].x, matrix[i][j].y, rgb, 12, 2, "#003300");
    } else if (values[0] == "CP") {
        reset();
        var iPrev = i;
        var jPrev = j;
        var coords = values[2].split(",");
        i = coords[0];
        j = coords[1];
        var rgb = "rgb(" + values[1] + ")";
        reset();
        draw(context, matrix[i][j].x, matrix[i][j].y, rgb, 12, 2, "#003300");
    } else if (values[0] == "X") {
        myTimer=setInterval(loopX,50);
    } else if (values[0] == "Y") {
        myTimer=setInterval(loopY,50);
    } else if (values[0] == "D") {
        myTimer=setInterval(loopD,50);
    } else if (values[0] == "CX") {
        myTimer=setInterval(loopCX,100);
    } else if (values[0] == "CY") {
        myTimer=setInterval(loopCY,100);
    } else if (values[0] == "CD") {
        myTimer=setInterval(loopCD,100);
    }

    function loopX() {
        draw(context, matrix[i][j].x, matrix[i][j].y, rgb, 12, 2, "#003300");
        i++;
        if (i > 7) {
            i = 0;
            j++;
        }
        if (j > 7) {
            clearInterval(myTimer);
            myTimer = null;
        }
    }
    function loopY() {
        draw(context, matrix[i][j].x, matrix[i][j].y, rgb, 12, 2, "#003300");
        j++;
        if (j > 7) {
            j = 0;
            i++;
        }
        if (i > 7) {
            clearInterval(myTimer);
            myTimer = null;
        }
    }

    function loopD() {
        draw(context, matrix[i][j].x, matrix[i][j].y, rgb, 12, 2, "#003300");
        if (decrement) {
            i--;
        } else {
            i++;
        }
        j++;
        if (j > 7) {
            if (!decrement) {
                i = 7;
                j = 0;
                decrement = true;
            } else {
                clearInterval(myTimer);
                myTimer = null;
            }
        }
    }

    /**
     * Draws a "moving" pixel by row.
     */
    function loopCX() {
        reset();
        draw(context, matrix[i][j].x, matrix[i][j].y, rgb, 12, 2, "#003300");
        var iPrev = i;
        var jPrev = j;
        i++;
        if (i > 7) {
            i = 0;
            j++;
        }
        if (j > 7) {
            clearInterval(myTimer);
            myTimer = null;
            draw(context, matrix[iPrev][jPrev].x, matrix[iPrev][jPrev].y, "#cdc9c9", 12, 2, "#003300");
        } 
    }

    /**
     * Draws a "moving" pixel by column.
     */
    function loopCY() {
        reset();
        draw(context, matrix[i][j].x, matrix[i][j].y, rgb, 12, 2, "#003300");
        var iPrev = i;
        var jPrev = j;
        j++;
        if (j > 7) {
            j = 0;
            i++;
        }
        if (i > 7) {
            clearInterval(myTimer);
            myTimer = null;
            draw(context, matrix[iPrev][jPrev].x, matrix[iPrev][jPrev].y, "#cdc9c9", 12, 2, "#003300");
        }
    }

    /**
     * Draws a "moving" pixel diagonally.
     */
    function loopCD() {
        reset();
        draw(context, matrix[i][j].x, matrix[i][j].y, rgb, 12, 2, "#003300");
        var iPrev = i;
        var jPrev = j;
        if (decrement) {
            i--;
        } else {
            i++;
        }
        j++;
        if (j > 7) {
            if (!decrement) {
                i = 7;
                j = 0;
                decrement = true;
            } else {
                clearInterval(myTimer);
                myTimer = null;
                draw(context, matrix[iPrev][jPrev].x, matrix[iPrev][jPrev].y, "#cdc9c9", 12, 2, "#003300");
            }
        }
    }


    /**
     * Reset the matrix pixels.
     */
    function reset() {
        for (var i=0; i<8; i++) {
            for (var j=0; j<8; j++) {
                draw(context, matrix[i][j].x, matrix[i][j].y, "#cdc9c9", 12, 2, "#003300");
            }
        }
    }
}

