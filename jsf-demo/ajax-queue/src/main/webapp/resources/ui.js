function process(buttonName, imageSrc) {
    var button = document.getElementById(buttonName);
    if (!button.disabled) {
        button.src = imageSrc;
        button.disabled = true;
    }
} 

// This method receives queue events
function msg(data) {
    var queuetable = document.getElementById("queuetable");
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
                button.src = "resources/button2.gif";
                break;
            }
        }
    }
}

// Set up the observer subscription

observer.subscribe(msg);

