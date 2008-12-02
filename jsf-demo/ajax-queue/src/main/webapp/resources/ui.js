var disabledImage = 'resources/button3.gif';
var enabledImage = 'resources/button2.gif';

// Setup the statusUpdate function to hear all events on the page
jsf.ajax.onEvent("statusUpdate");
jsf.ajax.onError("statusUpdate");

function buttonpush(buttonName, element, event) {
    var button = document.getElementById(buttonName);
    if (!button.disabled) {
        button.src = disabledImage;
        button.disabled = true;
    }
    try {
        addCell(document.createTextNode(buttonName));
        jsf.ajax.request(element, event, {execute: buttonName, render: buttonName, event: 'msg', error: 'errorMsg'});
    } catch (ex) {
        // Handle programming errors here
        alert(ex);
    }
    return false;
} 

function msg(data) {
    if (data.name === 'beforeOpen') {
        activeCell(document.createTextNode(data.execute));
    } else if (data.name === 'onCompletion') {
        removeCell(document.createTextNode(data.execute));
    }
}

function addCell(cellData) {
    var cell = document.getElementById("tr1").insertCell(0);
    cell.setAttribute("height", "50px");
    cell.setAttribute("width", "50px");
    cell.innerHTML = cellData.nodeValue;
    cell.className = "queueCell";
}

function activeCell(cellData) {
    var row = document.getElementById("tr1");
    var cells = row.getElementsByTagName("td");
    if (typeof cells != 'undefined' || cells != null) {
        for (var i=0; i<cells.length; i++) {
            if (cells[i].firstChild.nodeValue == cellData.nodeValue) {
                cells[i].className = "currentCell";
                break;
            }
        }
    }
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

function errorMsg(data) {
    alert("Error Name: "+data.name);
}

function statusUpdate(data) {
    var statusArea = document.getElementById("statusArea");
    var text = statusArea.value;
    text = text + "Name: "+data.execute;
    if (data.type === "event") {
        text = text +" Event: "+data.name+"\n";
    } else {  // otherwise, it's an error
        text = text + " Error: "+data.name+"\n";
    }
    statusArea.value = text;
}