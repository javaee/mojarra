


/**
* Reads a nested ul/li list from the DOM and add to TreeView
* @method readList
*/
YAHOO.widget.TreeView.prototype.readList= function() {
	this.getUlList(this.getEl());
};

/**
* Recursively get a nested ul/li list and add to TreeView
* @method getUlList
* @param nodeParent {Node} the node to be added to TreeView
* @param node {TreeView node} higher level recursion TreeView node
*/
YAHOO.widget.TreeView.prototype.getUlList= function(nodeParent, node) {
	if ( !nodeParent.getElementsByTagName('ul')[0] )
		return;

	if ( !node )
		node = this.getRoot();

	var nRootList = nodeParent.getElementsByTagName('ul')[0];

	for ( var i=0 ; i<nRootList.childNodes.length ; i++ ) {
		var childNode = nRootList.childNodes[i];
		if ( childNode.nodeName.toLowerCase() == 'li' ) {
			sLabel = this.getLiLabel( childNode );
			if ( childNode.className.toLowerCase() == "expand" ) {
				bExpand = true;
				node.expand(true);
			} else {
				bExpand = false;
			}
			newNode = new YAHOO.widget.HTMLNode(sLabel, node, bExpand, true);
			this.getUlList (childNode, newNode);
		}
	}
};

/**
* Remove any sub ul nodes to get the li label
* @method getLiLabel
* @param liNode {Node} li node to be searched
* @return {string} the li label to be displayed
*/
YAHOO.widget.TreeView.prototype.getLiLabel= function( liNode ) {
	nLabel = liNode.cloneNode(true);

	if ( nLabel.getElementsByTagName('ul')[0] ) {
		nUl = nLabel.getElementsByTagName('ul')[0];
		nLabel.removeChild(nUl);
	}
	return nLabel.innerHTML;
};