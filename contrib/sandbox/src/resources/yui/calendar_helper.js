if (typeof RISANDBOX == "undefined") {
    RISANDBOX = YAHOO.namespace("RISANDBOX");
}

RISANDBOX.Calendar = function(divId, trigger, daySelectId, monthSelectId, yearSelectId, clientId, startMonth, selectedDate, multiSelect, showWeekdays,
        startWeekday, showWeekHeader, showWeekFooter, hideBlankWeeks, minDate, maxDate, showMenus) {
    this.divId = divId;
    this.clientId = clientId;
    this.daySelectId = daySelectId;
    this.monthSelectId = monthSelectId;
    this.yearSelectId = yearSelectId;
    this.minDate = minDate;
    this.maxDate = maxDate;
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
RISANDBOX.Calendar.prototype.dayChanged = function() {
    this.update();
}
RISANDBOX.Calendar.prototype.monthChanged = function() {
    this.initDaySelects();
    this.update();
}
RISANDBOX.Calendar.prototype.yearChanged = function() {
    this.initMonthSelects();
    this.update();
}
RISANDBOX.Calendar.prototype.initDaySelects = function(d, m, y) {
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

    var option, i, len;

    len = daySelect.options.length;
    for (i=len-1; i > 0; i--) {
        daySelect.options[i] = null;
    }
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

RISANDBOX.Calendar.prototype.initMonthSelects = function(d, m, y) {
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

    var option, i, len;

    var monthNames = new Array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
  
    len = monthSelect.options.length;
    for (i=len-1; i > 0; i--) {
        monthSelect.options[i] = null;
    }
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

RISANDBOX.Calendar.prototype.initYearSelects = function(d, m, y) {
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

    for (var i=0; i < yearSelect.length; i++) { 
        if (yearSelect.options[i].value == year) {
            yearSelect.options[i].selected = true;
        }
    }  
    this.initMonthSelects(day, month, year);
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

RISANDBOX.Calendar.prototype.getMinDays = function(month, year) {
        if (isNaN(month)) {
            return 1;
        }
        if (year == this.minDate.substring(6) && month == this.minDate.substring(0,2)) {
            var minDay = this.minDate.substring(3,5);
            if (!isNaN(minDay)) {
                return parseInt(minDay);
            }
        }	
	return 1;
}
RISANDBOX.Calendar.prototype.getMaxDays = function(month, year) {
        if (isNaN(month)) {
            return 31;
        }
        var max = 31;
        if (year == this.maxDate.substring(6) && month == this.maxDate.substring(0,2)) {
            var maxDay = this.maxDate.substring(3,5);
            if (!isNaN(maxDay)) {
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
RISANDBOX.Calendar.prototype.getMinMonths = function(year) {
        if (isNaN(year)) {
            return 1;
        }	
        if (year == this.minDate.substring(6)) {
            var minMon = this.minDate.substring(0,2);
            if (!isNaN(minMon)) {
                return parseInt(minMon);
            }
        }
	return 1;
}
RISANDBOX.Calendar.prototype.getMaxMonths = function(year) {
        if (isNaN(year)) {
            return 12;
        }	
        if (year == this.maxDate.substring(6)) {
            var maxMon = this.maxDate.substring(0,2);
            if (!isNaN(maxMon)) {
                return parseInt(maxMon);
            }
        }
	return 12;
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

            var yearOrMonthChanged = false;
            if (! isNaN(year)) {
                for (var y=0;y<yearSelect.options.length;y++) {
                    if (yearSelect.options[y].value == year) {
                        if (yearSelect.options[y].selected != true) {                        
                            yearSelect.options[y].selected = true;
                            yearOrMonthChanged = true;
                            if (yearSelect.onchange) {
                                yearSelect.onchange();
                            }                     
                        }
                        break;
                    }
                }
            }
            if (! isNaN(month)) {
                for (var i=0;i<monthSelect.options.length;i++) {
                    if (monthSelect.options[i].value == month) {
                        if (monthSelect.options[i].selected != true) {                        
                            monthSelect.options[i].selected = true;
                            yearOrMonthChanged = true;
                            if (monthSelect.onchange) {
                                monthSelect.onchange();
                            }                     
                        }
                        break;
                    }
                }
            }
            if (! isNaN(day)) {
                for (var i=0;i<daySelect.options.length;i++) {
                    if (daySelect.options[i].value == day) {
                        if (daySelect.options[i].selected != true) {                        
                            daySelect.options[i].selected = true;
                            if (daySelect.onchange) {
                                daySelect.onchange();
                            }                     
                        }
                        break;
                    }
                }
            }
            // If the year or month changed, then we need to recreate the <select> menus.
            if (yearOrMonthChanged == true) {        
                this.risandbox.initMonthSelects();                                       
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