SANDBOX.Calendar = function(divId, trigger, daySelectId, monthSelectId, yearSelectId, clientId, startMonth, selectedDate, multiSelect, showWeekdays,
        startWeekday, showWeekHeader, showWeekFooter, hideBlankWeeks, minDate, maxDate, showMenus) {
    this.divId = divId;
    this.clientId = clientId;
    this.daySelectId = daySelectId;
    this.monthSelectId = monthSelectId;
    this.yearSelectId = yearSelectId;
    this.minDate = minDate;
    this.maxDate = maxDate;
    this.startMonth = startMonth;
    this.showMenus = showMenus;

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
    this.calendar.risandbox = this;

    var el = YAHOO.util.Dom.get(trigger);
    el.calendar = this.calender;
    YAHOO.util.Event.addListener(trigger, "click", this.toggle, this, true);

    if (this.showMenus == true) {
        YAHOO.util.Event.addListener(daySelectId, "change", this.dayChanged, this, true);
        YAHOO.util.Event.addListener(monthSelectId, "change", this.monthChanged, this, true);
        YAHOO.util.Event.addListener(yearSelectId, "change", this.yearChanged, this, true);

        var selDate = this.calendar.getSelectedDates()[0];
        this.initYearSelects(selDate.getDate(), selDate.getMonth() + 1, selDate.getFullYear());   
    }
}
SANDBOX.Calendar.prototype.dayChanged = function() {
    this.update();
}
SANDBOX.Calendar.prototype.monthChanged = function() {
    this.initDaySelects();
    this.update();
}
SANDBOX.Calendar.prototype.yearChanged = function() {
    this.initYearSelects();
    this.update();
}
SANDBOX.Calendar.prototype.initDaySelects = function(d, m, y) {
    var daySelect = YAHOO.util.Dom.get(this.daySelectId);
    var monthSelect = YAHOO.util.Dom.get(this.monthSelectId);
    var yearSelect = YAHOO.util.Dom.get(this.yearSelectId);

    var day, month, year;
    if (d != undefined && m != undefined && y != undefined) {
        day = d;
        month = m;
        year = y;
    } else {
        day = parseInt(daySelect.options[daySelect.selectedIndex].value);
        month = parseInt(monthSelect.options[monthSelect.selectedIndex].value) + 1;
        year = parseInt(yearSelect.options[yearSelect.selectedIndex].value);
    }

    var option, i;

    daySelect.options.length=1;

    var maxDays = this.getMaxDays(month, year);
    var minDays = this.getMinDays(month, year);
    if (day > maxDays) {
        day = maxDays;
    } else if (day < minDays) {
        day = minDays;
    }
    for (i=minDays; i < maxDays + 1; i++) { 
        daySelect.options[daySelect.length] = new Option(i,i);
        if (i == day) {
            daySelect.options[daySelect.length - 1].selected = true;
        }
    }  
}

SANDBOX.Calendar.prototype.initMonthSelects = function(d, m, y) {
    var daySelect = YAHOO.util.Dom.get(this.daySelectId);
    var monthSelect = YAHOO.util.Dom.get(this.monthSelectId);
    var yearSelect = YAHOO.util.Dom.get(this.yearSelectId);

    var day, month, year;
    if (d != undefined && m != undefined && y != undefined) {
        day = d;
        month = m;
        year = y;
    } else {
        day = parseInt(daySelect.options[daySelect.selectedIndex].value);
        month = parseInt(monthSelect.options[monthSelect.selectedIndex].value) + 1;
        year = parseInt(yearSelect.options[yearSelect.selectedIndex].value);
    }

    var option, i;

    var monthNames = new Array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
  
    monthSelect.options.length=1;

    var maxMonths = this.getMaxMonths(year);
    var minMonths = this.getMinMonths(year);
    if (month > maxMonths ) {
        month = maxMonths;
    } else if  (month < minMonths ) {
        month = minMonths;
    }
    for (i=minMonths - 1; i < maxMonths; i++) { 
        monthSelect.options[monthSelect.length] = new Option(monthNames[i],i);
        if (i == month - 1) {
            monthSelect.options[monthSelect.length - 1].selected = true;
        }
    }  
    this.initDaySelects(day, month, year);
}

SANDBOX.Calendar.prototype.initYearSelects = function(d, m, y) {
    var daySelect = YAHOO.util.Dom.get(this.daySelectId);
    var monthSelect = YAHOO.util.Dom.get(this.monthSelectId);
    var yearSelect = YAHOO.util.Dom.get(this.yearSelectId);

    var day, month, year;
    if (d != undefined && m != undefined && y != undefined) {
        day = d;
        month = m;
        year = y;
    } else {
        day = parseInt(daySelect.options[daySelect.selectedIndex].value);
        month = parseInt(monthSelect.options[monthSelect.selectedIndex].value) + 1;
        year = parseInt(yearSelect.options[yearSelect.selectedIndex].value);
    }

    var option, i;

    yearSelect.options.length=1;

    var maxYears = this.getMaxYears(year);
    var minYears = this.getMinYears(year);
    if (year > maxYears ) {
        year = maxYears;
    } else if  (year < minYears ) {
        year = minYears;
    }

    for (i=minYears; i < maxYears +1; i++) { 
        yearSelect.options[yearSelect.length] = new Option(i,i);
        if (i == year) {
            yearSelect.options[yearSelect.length - 1].selected = true;
        }
    }  
    this.initMonthSelects(day, month, year);
}

SANDBOX.Calendar.prototype.getMinDays = function(month, year) {
        var defaultValue = 1;
        if (isNaN(month)) {
            return defaultValue;
        }
        if (year == this.minDate.substring(6) && month == this.minDate.substring(0,2)) {
            var min = this.minDate.substring(3,5);
            if (!isNaN(min) && min != "") {
                return parseInt(min);
            }
        }	
	return defaultValue;
}
SANDBOX.Calendar.prototype.getMaxDays = function(month, year) {
        var defaultValue = 31;
        if (isNaN(month)) {
            return defaultValue;
        }
        var max = defaultValue;
        if (year == this.maxDate.substring(6) && month == this.maxDate.substring(0,2)) {
            var maxDay = this.maxDate.substring(3,5);
            if (!isNaN(maxDay) && maxDay != "") {
                max = parseInt(maxDay);
            }
        }

	var monthLength = new Array(31,28,31,30,31,30,31,31,30,31,30,31);	
        monthLength[1] = ( year%4==0 && (year%100!=0 || year%400==0) ) ? 29 : 28;

        if ( max < monthLength[month-1]) {
            return max;
        } 
        return monthLength[month-1];
}
SANDBOX.Calendar.prototype.getMinMonths = function(year) {
        var defaultValue = 1;
        if (isNaN(year)) {
            return defaultValue;
        }	
        if (year == this.minDate.substring(6)) {
            var min = this.minDate.substring(0,2);
            if (!isNaN(min) && min != "") {
                return parseInt(min);
            }
        }
	return defaultValue;
}
SANDBOX.Calendar.prototype.getMaxMonths = function(year) {
        var defaultValue = 12;
        if (isNaN(year)) {
            return defaultValue;
        }	
        if (year == this.maxDate.substring(6)) {
            var max = this.maxDate.substring(0,2);
            if (!isNaN(max) && max != "") {
                return parseInt(max);
            }
        }
	return defaultValue;
}

SANDBOX.Calendar.prototype.getMinYears = function(year) {
        var defaultValue = parseInt(this.startMonth.substring(3,7)) - 50;
        var min = this.minDate.substring(6,10);
        if (!isNaN(min) && min != "") {
            return parseInt(min);
        }

        if (isNaN(year)) {
            return defaultValue;
        }	
	return parseInt(year) - 50;
}

SANDBOX.Calendar.prototype.getMaxYears = function(year) {
        var defaultValue = parseInt(this.startMonth.substring(3,7)) + 50;
        var max = this.maxDate.substring(6,10);
        if (!isNaN(max) && max != "") {
            return parseInt(max);
        } 

        if (isNaN(year)) {
            return defaultValue
        }	
	return parseInt(year) + 50;
}

SANDBOX.Calendar.prototype.toggle = function() {
    if (YAHOO.util.Dom.get(this.divId).style.display=="none") {
        this.calendar.show();
    } else {
        this.calendar.hide();
    }
}

SANDBOX.Calendar.prototype.update = function () {
    if (YAHOO.util.Dom.get(this.divId).style.display!="none") {
        var showCalendar = true;
    }

    var daySelect = YAHOO.util.Dom.get(this.daySelectId);
    var monthSelect = YAHOO.util.Dom.get(this.monthSelectId);
    var yearSelect = YAHOO.util.Dom.get(this.yearSelectId);

    var day = parseInt(daySelect.options[daySelect.selectedIndex].value);
    var month = parseInt(monthSelect.options[monthSelect.selectedIndex].value) + 1;
    var year = parseInt(yearSelect.options[yearSelect.selectedIndex].value);
					
    if (! isNaN(month) && ! isNaN(year)) {

        if (! isNaN(day)) {
            var date = month + "/" + day + "/" + year;
            this.calendar.select(date);
        } else {
            this.calendar.deselectAll();
        }
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
        if (this.risandbox.showMenus == true) {
            var daySelect = YAHOO.util.Dom.get(this.risandbox.daySelectId);
            var monthSelect = YAHOO.util.Dom.get(this.risandbox.monthSelectId);
            var yearSelect = YAHOO.util.Dom.get(this.risandbox.yearSelectId);

            var selDate = this.getSelectedDates()[0];
            var day = selDate.getDate();
            var month = selDate.getMonth();
            var year = selDate.getFullYear();

            if (year != yearSelect.value) {
                this.risandbox.initYearSelects(day, month+1, year);  
            } else if (month != monthSelect.value) {
                this.risandbox.initMonthSelects(day, month+1, year);  
            } else  if (day != daySelect.value) {
                this.risandbox.initDaySelects(day, month+1, year);  
            }
        } else {
            var inputField = YAHOO.util.Dom.get(this.risandbox.clientId);
            inputField.value = this.formatDate();
            if (inputField.onchange) {
                inputField.onchange();
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