
if (typeof demo == "undefined" && !demo) {
    var demo = {};
}

if (typeof demo.calendar == "undefined" && !demo.calendar) {
    demo.calendar = {};
}

if (typeof demo.calendar.contextMap === "undefined" && !demo.calendar.contextMap) {
    demo.calendar.contextMap = [];
}

demo.calendar.init = function init(context, render) {

    // record the render attribute, if applied
    demo.calendar.contextMap[context] = render;

    demo.calendar.loader = new YAHOO.util.YUILoader({
        base: "http://ajax.googleapis.com/ajax/libs/yui/2.7.0/build/",
        require: ["calendar"],
        loadOptional: false,
        combine: false,
        filter: "RAW",
        allowRollup: false,
        onSuccess: function() {
            try {
                demo.calendar.cal1 = new YAHOO.widget.Calendar("demo.calendar.cal1", context+":calContainer");
                demo.calendar.cal1.render();
                demo.calendar.cal1.selectEvent.subscribe(demo.calendar.handleSelect, demo.calendar.cal1, true);
            } catch (e) {
                alert(e);
            }
        },
        // should a failure occur, the onFailure function will be executed
        onFailure: function(o) {
            alert("error: " + YAHOO.lang.dump(o));
        }

    });

    //
    // Calculate the dependency and insert the required scripts and css resources
    // into the document
    demo.calendar.loader.insert();
}

demo.calendar.handleSelect = function handleSelect(type, args, obj) {

    if (type === "select") {
        var calId = obj.containerId;
        var index = calId.indexOf(":") + 1;
        var tmpindex = calId.substring(index).indexOf(":") + 1;
        // keep looking until you get the last child index
        while (tmpindex !== 0) {
            index += tmpindex;
            tmpindex = calId.substr(index).indexOf(":") + 1;
        }
        var containerId = calId.substring(0,index - 1);
        var dateId = containerId + ":" + "date";
        var dates = args[0];
        var date = dates[0];
        var year = date[0], month = date[1], day = date[2];

        var txtDate = document.getElementById(dateId);
        txtDate.value = month + "/" + day + "/" + year;

        var render = demo.calendar.contextMap[containerId];
        try {
            // if a render is defined for the component, then include it.
            if (typeof render !== "undefined" && render ) {
                jsf.ajax.request(dateId,null,{
                    render: render,
                    execute: dateId
                })
            } else {
                jsf.ajax.request(dateId,null,{
                    execute: dateId
                })
            }
        } catch (e) {
            alert(e);
        }
    }
}
