if (typeof YUISF == "undefined") {
    YUISF = YAHOO.namespace("YUISF");
}

YUISF.Menu = function (menuElemId, config) {
	this.menuElemId = menuElemId;
	this.config = config;
	YAHOO.util.Event.onAvailable(menuElemId, this.onAvailable, this, true);
}

YUISF.Menu.prototype.onAvailable = function() {
	this._menu = new YAHOO.widget.Menu(this.menuElemId, this.config);
	this._menu.render();
	this._menu.show();
}