var disabledImage = 'resources/button3.gif';
var enabledImage = 'resources/button2.gif';

function buttonpush(buttonName, element, event) {
    var button = document.getElementById(buttonName);
    if (!button.disabled) {
        button.src = disabledImage;
        button.disabled = true;
    }
    try {
        jsf.ajaxRequest(element, event, {execute: buttonName, render: buttonName});
    } catch (ex) {
        // Handle errors here
        alert(ex);
    }
    return false;
} 

function msg(eventName, data) {
    var txt = null;
    if (data.name === 'enqueue') {
        txt = document.createTextNode(data.execute);
        addCell(txt);
    } else if (data.name === 'dequeue') {
        txt = document.createTextNode(data.execute);
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

function errorMsg(eventName, data) {
    alert("Name: "+eventName+" Error Status: "+data.statusMessage);
}

// Listen for all queue events
OpenAjax.hub.subscribe("javax.faces.Event.**",msg);
// Listen for all error events
OpenAjax.hub.subscribe("javax.faces.Error.**",errorMsg);