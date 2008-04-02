if (typeof RISANDBOX == "undefined") {
    RISANDBOX = YAHOO.namespace("RISANDBOX");
}

RISANDBOX.calendar = function(divId, trigger, clientId, startMonth, selectedDate, multiSelect, showWeekdays,
        startWeekday, showWeekHeader, showWeekFooter, hideBlankWeeks) {
    this.divId = divId;
    this.clientId = clientId;
    this.hidden = true;

    var pos = YAHOO.util.Dom.getXY(trigger);
    var img_height = 20; 
    var elem = YAHOO.util.Dom.get(divId);

    elem.style.top  = pos[1] + img_height + "px";
    elem.style.left = pos[0] + "px";
    elem.style.position = 'absolute';

    this.calendar = new YAHOO.widget.Calendar(divId, divId,
        {
            pageDate: startMonth,
            selected: selectedDate,
            MULTI_SELECT: multiSelect,
            SHOW_WEEKDAYS: showWeekdays,
            START_WEEKDAY: startWeekday,
            SHOW_WEEK_HEADER: showWeekHeader,
            SHOW_WEEK_FOOTER: showWeekFooter,
            HIDE_BLANK_WEEKS: hideBlankWeeks
        });
    this.calendar.render();
    this.calendar.clientId = clientId;

    var el = YAHOO.util.Dom.get(trigger);
    el.calendar = this.calender;
    YAHOO.util.Event.addListener(trigger, "click", this.show, this, true);
}

RISANDBOX.calendar.prototype.show = function() {
    if (this.hidden == true) {
        this.calendar.show();
        this.hidden = false;
    } else {
        this.calendar.hide();
        this.hidden = true;
    }
}

YAHOO.widget.Calendar.prototype.onSelect = function() {
        var inputField = YAHOO.util.Dom.get(this.clientId);
        inputField.value = this.formatDate();
        if (inputField.onchange) {
            inputField.onchange();
        }
        this.hide();
}

YAHOO.widget.Calendar.prototype.formatDate = function() {
        var selDate = this.getSelectedDates()[0];
        var date_separator = "/";
        var day_ = selDate.getDate();
        var day = ( day_ < 10 ) ? "0" + day_ : day_;
        var month_ = selDate.getMonth()+1;
        var month = ( month_ < 10 ) ? "0" + month_ : month_;
        var year = selDate.getFullYear();
        return year + date_separator + month + date_separator + day;
}