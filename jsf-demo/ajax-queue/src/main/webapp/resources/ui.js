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
    if (typeof data.enqueue != 'undefined' && data.enqueue !== null) {
        txt = document.createTextNode(data.enqueue.parameters["javax.faces.partial.execute"]);
        addCell(txt);
    } else if (typeof data.dequeue != 'undefined' && data.dequeue !== null) {
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