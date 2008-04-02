if (typeof RISANDBOX == "undefined") {
    RISANDBOX = YAHOO.namespace("RISANDBOX");
}

RISANDBOX.Calendar = function(divId, trigger, daySelectId, monthSelectId, yearSelectId, clientId, startMonth, selectedDate, multiSelect, showWeekdays,
        startWeekday, showWeekHeader, showWeekFooter, hideBlankWeeks, minDate, maxDate, showInput, showSelects) {
    this.divId = divId;
    this.clientId = clientId;
    this.daySelectId = daySelectId;
    this.monthSelectId = monthSelectId;
    this.yearSelectId = yearSelectId;
    this.minDate = minDate;
    this.maxDate = maxDate;
    this.showInput = showInput;
    this.showSelects = showSelects;

    var pos = YAHOO.util.Dom.getXY(trigger);
    var img_height = 20; 
    var elem = YAHOO.util.Dom.get(divId);

    elem.style.top  = pos[1] + img_height + "px";
    elem.style.left = pos[0] + "px";
    elem.style.position = 'absolute';

    this.calendar = new YAHOO.widget.Calendar(divId+'Table', divId,
        {
            mindate: minDate,
            maxdate: maxDate,
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
    this.calendar.daySelectId = daySelectId;
    this.calendar.monthSelectId = monthSelectId;
    this.calendar.yearSelectId = yearSelectId;
    this.calendar.showInput = showInput;
    this.calendar.showSelects = showSelects;

    var el = YAHOO.util.Dom.get(trigger);
    el.calendar = this.calender;
    YAHOO.util.Event.addListener(trigger, "click", this.toggle, this, true);

    if (showSelects == true) {
        YAHOO.util.Event.addListener([daySelectId,monthSelectId,yearSelectId], "change", this.update, this, true);
    }
}

RISANDBOX.Calendar.prototype.toggle = function() {
    if (YAHOO.util.Dom.get(this.divId).style.display=="none") {
        this.calendar.show();
    } else {
        this.calendar.hide();
    }
}

RISANDBOX.Calendar.prototype.update = function () {
    if (YAHOO.util.Dom.get(this.divId).style.display!="none") {
        var showCalendar = true;
    }

    var daySelect = YAHOO.util.Dom.get(this.daySelectId);
    var monthSelect = YAHOO.util.Dom.get(this.monthSelectId);
    var yearSelect = YAHOO.util.Dom.get(this.yearSelectId);

    var day = parseInt(daySelect.options[daySelect.selectedIndex].value);
    var month = parseInt(monthSelect.options[monthSelect.selectedIndex].value) + 1;
    var year = parseInt(yearSelect.options[yearSelect.selectedIndex].value);
					
    if (! isNaN(month) && ! isNaN(day) && ! isNaN(year)) {
        var date = month + "/" + day + "/" + year;

        this.calendar.select(date);
        this.calendar.cfg.setProperty("pagedate", month + "/" + year);	
        this.calendar.render();       
         
        // The this.calendar.select(date); statement above reverses the container.style.display setting.
        // So, we use a Boolean that is set at the start for greater readability.
        if (showCalendar == true) {
            this.calendar.show();
        }   
    }
}

YAHOO.widget.Calendar.prototype.onSelect = function() {
        var inputField = YAHOO.util.Dom.get(this.clientId);
        var daySelect = YAHOO.util.Dom.get(this.daySelectId);
        var monthSelect = YAHOO.util.Dom.get(this.monthSelectId);
        var yearSelect = YAHOO.util.Dom.get(this.yearSelectId);

        if (this.showInput == true) {
            inputField.value = this.formatDate();
            if (inputField.onchange) {
                inputField.onchange();
            }
        }

        // TODO: this needs on change support too...
        if (this.showSelects == true) {
            var selDate = this.getSelectedDates()[0];
            var day = selDate.getDate();
            var month = selDate.getMonth();
            var year = selDate.getFullYear();

            daySelect.selectedIndex = day;
            monthSelect.selectedIndex = month + 1;
            for (var y=0;y<yearSelect.options.length;y++) {
    		if (yearSelect.options[y].value == year) {
                    yearSelect.selectedIndex = y;
                    break;
                }
            }
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
