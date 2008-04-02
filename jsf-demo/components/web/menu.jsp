<!--
 Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 
 Redistribution and use in source and binary forms, with or
 without modification, are permitted provided that the following
 conditions are met:
 
 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
 
 - Redistribution in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials
   provided with the distribution.
    
 Neither the name of Sun Microsystems, Inc. or the names of
 contributors may be used to endorse or promote products derived
 from this software without specific prior written permission.
  
 This software is provided "AS IS," without a warranty of any
 kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
  
 You acknowledge that this software is not designed, licensed or
 intended for use in the design, construction, operation or
 maintenance of any nuclear facility.
-->

<%@ page import="components.model.Graph,components.model.Node" %>
<%@ taglib uri="http://java.sun.com/jsf/core"   prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html"   prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/demo/components" prefix="d" %>

<%

    // Construct a preconfigured Graph in session scope
    Graph graph = (Graph)
      pageContext.getAttribute("graph", PageContext.SESSION_SCOPE);
    if (graph == null) {

      Node root = new Node("Menu", "Menu", null, null, false, true);
      graph = new Graph(root);

      Node file = new Node("File", "File", "demo-test.jsp", null, true, true);
      root.addChild(file);
      file.addChild(new Node("File-New", "New", "demo-test.jsp", null, true, false));
      file.addChild(new Node("File-Open", "Open", "demo-test.jsp", null, true, false));
      Node close = new Node("File-Close", "Close", "demo-test.jsp", null, false, false);
      file.addChild(close);
      file.addChild(new Node("File-Exit", "Exit", "demo-test.jsp", null, true, false));

      Node edit = new Node("Edit", "Edit", "demo-test.jsp", null, true, false);
      root.addChild(edit);
      edit.addChild(new Node("Edit-Cut", "Cut", "demo-test.jsp", null, true, false));
      edit.addChild(new Node("Edit-Copy", "Copy","demo-test.jsp", null, true, false));
      edit.addChild(new Node("Edit-Paste", "Paste", "demo-test.jsp", null, false, false));

      graph.setSelected(close);
      pageContext.setAttribute("graph", graph, PageContext.SESSION_SCOPE);

  }

%>

<f:use_faces>
<html>
<head>
<title>Demonstration Components - Menu</title>
</head>
<body bgcolor="white">

<h:form formName="menuForm" >

<d:stylesheet path="/tree-control-test.css"/>

Render graph as a menu bar (graph retrieved from model):<br>
<d:graph_menubar id="menu2" valueRef="sessionScope.graph" 
    selectedClass="tree-control-selected"
    unselectedClass="tree-control-unselected" />

<hr>
Render graph as a menu bar (graph specified via JSP):<br>
<d:graph_menubar id="menu3" selectedClass="tree-control-selected"
      unselectedClass="tree-control-unselected">
    <d:graph_menunode  name="Menu" label="Menu" >
        <d:graph_menunode  name="File" label="File" expanded="true">
            <d:graph_menunode  name="File-New" label="New" action="/faces/demo-test.jsp" />
            <d:graph_menunode  name="File-Open" label="Open" action="/faces/demo-test.jsp" />
            <d:graph_menunode  name="File-Close" label="Close" enabled="false" />
            <d:graph_menunode  name="File-Exit" label="Exit" action="/faces/demo-test.jsp" />
        </d:graph_menunode>

       <d:graph_menunode  name="Edit" label="Edit" >
           <d:graph_menunode  name="Edit-Cut" label="Cut" action="demo-test.jsp"/>
           <d:graph_menunode  name="Edit-Copy" label="Copy" action="demo-test.jsp" />
           <d:graph_menunode  name="Edit-Paste" label="Paste" enabled="false" />
       </d:graph_menunode>
   </d:graph_menunode>
</d:graph_menubar>

<hr>
Render graph as a tree control (graph retrieved from model):<br>
<d:graph_menutree id="menu4" valueRef="graph" graphClass="tree-control"
     selectedClass="tree-control-selected" 
     unselectedClass="tree-control-unselected" />
<hr>
Render graph as a tree control (graph specified via JSP):<br>
<d:graph_menutree id="menu5" selectedClass="tree-control-selected"
    unselectedClass="tree-control-unselected" graphClass="tree-control" >
    <d:graph_treenode  name="Menu" label="Menu" enabled="false" expanded="true">

        <d:graph_treenode  name="File" label="File"
             icon="folder_16_pad.gif" enabled="false">

            <d:graph_treenode  name="File-New" label="New"
                icon="folder_16_pad.gif" action="/faces/demo-test.jsp"/>
            <d:graph_treenode  name="File-Open" label="Open"
                icon="folder_16_pad.gif" action="/faces/demo-test.jsp" />
            <d:graph_treenode  name="File-Close" label="Close" enabled="false"
                icon="folder_16_pad.gif" />
            <d:graph_treenode  name="File-Exit" label="Exit"
                icon="folder_16_pad.gif" action="/faces/demo-test.jsp" />
       </d:graph_treenode>

       <d:graph_treenode  name="Edit" label="Edit" 
           icon="folder_16_pad.gif" expanded="true" enabled="false"> 

           <d:graph_treenode  name="Edit-Cut" label="Cut" 
               icon="folder_16_pad.gif" action="demo-test.jsp"/>
           <d:graph_treenode  name="Edit-Copy" label="Copy"
               icon="folder_16_pad.gif" action="demo-test.jsp" />
           <d:graph_treenode  name="Edit-Paste" label="Paste" enabled="false" 
               icon="folder_16_pad.gif" />
       </d:graph_treenode>
   </d:graph_treenode>
</d:graph_menutree>

<hr>
</h:form>
<a href='<%= request.getContextPath() + "/index.jsp" %>'>Back</a> to home page.

<h1>How to Use this Component</h1>

<p>This component renders a <code>Graph</code> as either a menu bar or a tree control.  The <code>Graph</code> can be specified as model data, or it can be specified in <code>JSP</code>.</p>

<h2>JSP Attributes</h2>

<p>This component allows the user to define CSS classes via JSP attributes that are output in the rendered markup.  This makes it possible to produce highly customizable output.  You can compare the rendered source of this page, using the "View Source" feature of your browser, with <a href="ShowSource.jsp?filename=/menu.jsp">the JSP source</A> for this page.</p>

<table border="1">

<tr>
<th>JSP Attribute Name</th>
<th>What it Does</th>
</tr>

<tr>

<td><code>selectedClass</code></td>

<td>A style sheet class which controls the display attributes of the selected menu bar or tree element.  This is used to distinguish the selected portion from the other unselected portions.</td>

</tr>

<tr>

<td><code>unselectedClass</code></td>

<td>A style sheet class which controls the display attributes of an unselected menu bar or tree element.  This is used to distinguish an unselected portion from a selected portion.</
td>

</tr>

</table>

<h2>Menu Bar</h2>

<p>The menu bar can be described from a <code>Graph</code> specified in the model or it can be described from <code>JSP</code> tags.</p>

<h3>Described From JSP</h3>

<p>The <code>graph_menubar</code> tag consists of multiple <code>graph_menunode</code> tags.  Each <code>graph_menunode</code> tag corresponds to an item on the menu bar, and you can nest <code>graph_menunode</code> tags within each other.  The <code>graph_menunode</code> tag has attributes that control the visual aspects of the node, and it has an <code>action</code> attribute that can be used to specify a context-relative URL for when the node is selected.  Refer to the tag library descriptor <code>tld</code> file for a complete list of attributes.</p>

<h3>Described From Model</h3>

<p>The <code>graph_menubar</code> tag refers to a <code>Graph</code> model component through the <code>valueRef</code> attribute.  The <code>Graph</code> model component consists of multiple <code>Node</code> components.  Each <code>Node</code> component describes an item on the menu bar.</p>

<h2>Tree Control</h2>

<p>The tree control can be described from a <code>Graph</code> specified in the model or it can be described from <code>JSP</code> tags.</p>

<h3>Described From JSP</h3>

<p>The <code>graph_menutree</code> tag consists of multiple <code>graph_treenode</code> tags.  Each <code>graph_treenode</code> tag corresponds to a node in the tree, and you can nest <code>graph_treenode</code> tags within each other.  The <code>graph_treenode</code> tag has attributes that control the visual aspects of the node, and it has an <code>action</code> attribute that can be used to specify a context-relative URL for when the node is selected.  Refer to the tag library descriptor <code>tld</code> file for a complete list of attributes.</p>

<h3>Described From Model</h3>

<p>The <code>graph_menutree</code> tag refers to a <code>Graph</code> model component through the <code>valueRef</code> attribute.  The <code>Graph</code> model component consists of multiple <code>Node</code> components.  Each <code>Node</code> component describes an item in the tree.</p>

 
<hr>

<a href='<%= request.getContextPath() + "/index.jsp" %>'>Back</a> to home page.

</body>
</html>
</f:use_faces>
