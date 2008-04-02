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
  <link rel="stylesheet" type="text/css"
       href='<%= request.getContextPath() + "/tree-control-test.css" %>'>
<title>Demonstration Components - Menu</title>
</head>
<body bgcolor="white">

<h:form formName="menuForm" >

Render graph as a menu bar(graph retreived from model):<br>
<d:graph_menubar id="menu2" modelReference="sessionScope.graph" 
    selectedClass="tree-control-selected"
    unselectedClass="tree-control-unselected" />

<hr>
Render graph as a menu bar(graph specified via JSP):<br>
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
Render graph as a tree control (graph retreived from model):<br>
<d:graph_menutree id="menu4" modelReference="graph" graphClass="tree-control"
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
<a href='<%= request.getContextPath() + "/index.jsp" %>'>Back</a> to home page.

</h:form>

</body>
</html>
</f:use_faces>
