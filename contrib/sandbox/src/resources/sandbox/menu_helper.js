SANDBOX.Menu = function (menuElemId, config) {
	this.menuElemId = menuElemId;
	this.config = config;
	YAHOO.util.Event.onAvailable(menuElemId, this.onAvailable, this, true);
}

SANDBOX.Menu.prototype.onAvailable = function() {
	this._menu = new YAHOO.widget.Menu(this.menuElemId, this.config);
	this._menu.render();
	this._menu.show();
}