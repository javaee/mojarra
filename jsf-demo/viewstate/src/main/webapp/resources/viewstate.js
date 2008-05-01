function buildOptions() {
    var form = document.form1;
    var optionsMenu = document.getElementById("optionsMenu");
    for (var i=0; i<3; i++) {
        if (getCheckedOption() == "none") {
            optionsMenu.options.length = 0;
            return;
        }
    }
    for (var i=0; i<form.length; i++) {
        var formElement = form.elements[i];
        if (formElement.id != "javax.faces.ViewState" && 
            formElement.id != "encodedStr") {
            optionsMenu.options[i] = new Option(formElement.id , formElement.value);
        }
    }
}

function displayViewState(element) {
    var ticker = document.getElementById("tickerID");
    var data = getViewState(element).split("&");
    var displayData = new Array();
    for (i = 0; i< data.length; i++) {
        displayData[i] = "<span style='color:#0000ff'>" + data[i] + "</span>";    
    }

    var seperator = "<span style='color:#00008b'>&</span>";
    ticker.firstChild.innerHTML = displayData.join(seperator); 
}

function getViewState(element) {
    var viewState = null;
    var options = new Object();
    var optionsMenu = document.form2.optionsMenu;
    if (getCheckedOption() == "inputs") {
        var inputs = new Array();
        for (var i=0; i< optionsMenu.options.length; i++) {
            if (optionsMenu.options[i].selected) {
                inputs.push(optionsMenu.options[i].text);
            }    
        }
        if (inputs.length > 0) {
            inputs.join(",");
            options["inputs"]=inputs.toString();
        }
    } else if (getCheckedOption() == "ignore") {
        var ignore = new Array();
        for (var i=0; i< optionsMenu.options.length; i++) {
            if (optionsMenu.options[i].selected) {
                ignore.push(optionsMenu.options[i].text);
            }    
        }
        if (ignore.length > 0) {
            ignore.join(",");
            options["ignore"]=ignore.toString();
        }
    }

    // The source element that triggered this event
    options["source"] = element;

    viewState = javax.faces.Ajax.viewState(document.form1, options); 
    
    return viewState;
}

function getCheckedOption() {
    var controlOptions = document.form2.options;
    for (var i=0; i<3; i++) {
        if (controlOptions[i].checked == true) {
            return controlOptions[i].value;
        }
    }
}

function displayAssociativeArray() {
    var associativeArray = document.getElementById("associativeArray");
    var controls = new Array();
    if (getCheckedOption() == "none") {
        associativeArray.innerHTML = "";
    } else {
        var optionsMenu = document.form2.optionsMenu;
        for (var i=0; i< optionsMenu.options.length; i++) {
            if (optionsMenu.options[i].selected) {
                controls.push(optionsMenu.options[i].text);
            }
        }
        if (controls.length > 0) {
            controls.join(",");
            var data = "{" + getCheckedOption() + ":'" + controls + "'}";
            associativeArray.innerHTML = "<span style='color:red; font-size:16px'>" + data + "</span>"; 
        } else {
            associativeArray.innerHTML = "";
        }
    } 
}

function resetValues() {
    document.getElementById("text1").value = "";
    document.getElementById("oneMenu").selectedIndex = -1;
    document.getElementById("checkbox").checked = false;
    document.getElementById("options:2").checked = true;
    document.getElementById("tickerID").firstChild.innerHTML = "";
    document.getElementById("associativeArray").innerHTML = "";
    document.getElementById("optionsMenu").selectedIndex = -1;
}

