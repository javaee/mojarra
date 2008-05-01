function Ticker(name, id, shiftBy, interval) {
    this.name     = name;
    this.id       = id;
    this.shiftBy  = shiftBy ? shiftBy : 1;
    this.interval = interval ? interval : 100;
    this.runId	= null;

    this.div = document.getElementById(id);

    // remove extra textnodes that may separate the child nodes
    // of the ticker div

    var node = this.div.firstChild;
    var next;

    while (node) {
        next = node.nextSibling;
        if (node.nodeType == 3)
            this.div.removeChild(node);
        node = next;
    }

    //end of extra textnodes removal

    this.left = 15;
    this.shiftLeftAt = this.div.firstChild.offsetWidth;
    this.div.style.height	= this.div.firstChild.offsetHeight;
    this.div.style.width = 2 * screen.availWidth;
    this.div.style.visibility = 'visible';
}

function startTicker() {
    this.stop();
  
    this.left -= this.shiftBy;

    if (this.left <= -this.shiftLeftAt) {
        this.left = 15;
        this.div.appendChild(this.div.firstChild);
        this.shiftLeftAt = this.div.firstChild.offsetWidth;
    }

    this.div.style.left = (this.left + 'px');
    this.runId = setTimeout(this.name + '.start()', this.interval);
}

function stopTicker() {
    if (this.runId)
        clearTimeout(this.runId);
    
    this.runId = null;
}

function changeTickerInterval(newinterval) {
    if (typeof(newinterval) == 'string')
        newinterval =  parseInt('0' + newinterval, 10); 
	
    if (typeof(newinterval) == 'number' && newinterval > 0)
        this.interval = newinterval;
    
    this.stop();
    this.start();
}

/* Prototypes for Ticker */
Ticker.prototype.start = startTicker;
Ticker.prototype.stop = stopTicker;
Ticker.prototype.changeInterval = changeTickerInterval;

var ticker = null; /* ticker object */

function setTickerSpeed() {
    ticker.changeInterval(document.getElementById("speed").value);
}

function startticker() {
    ticker = new Ticker('ticker', 'tickerID', 1, document.getElementById("speed").value);
    ticker.start();
}

