var disabledImage = 'resources/button3.gif';
var enabledImage = 'resources/button2.gif';


var errorMsg = function errorMsg(data) {
    alert("Error: "+data.status);
};

var msg = function msg(data) {
    if (data.status === 'begin') {
        activeCell(document.createTextNode(data.source.id));
    } else if (data.status === 'complete') {
        removeCell(document.createTextNode(data.source.id));
    }
};


function buttonpush(buttonName, element, event) {
    var button = document.getElementById(buttonName);
    if (!button.disabled) {
        button.src = disabledImage;
        button.disabled = true;
    }
    try {
        addCell(document.createTextNode(buttonName));
        jsf.ajax.request(element, event, {execute: buttonName, render: buttonName, onevent: msg, onerror: errorMsg});
    } catch (ex) {
        // Handle programming errors here
        alert(ex);
    }
    return false;
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


var statusUpdate = function statusUpdate(data) {
    var statusArea = document.getElementById("statusArea");
    var text = statusArea.value;
    text = text + "Name: "+data.source.id;
    if (data.type === "event") {
        text = text +" Event: "+data.status+"\n";
    } else {  // otherwise, it's an error
        text = text + " Error: "+data.status+"\n";
    }
    statusArea.value = text;
};

// Setup the statusUpdate function to hear all events on the page
jsf.ajax.addOnEvent(statusUpdate);
jsf.ajax.addOnError(statusUpdate);
