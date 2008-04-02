<html>
<head>
<title>Demonstration Components Home Page</title>
<style type="text/css" media="screen">
TD { text-align: center }
</style>
</head>
<body bgcolor="white">

<%
  pageContext.removeAttribute("graph", PageContext.SESSION_SCOPE);
%>

<p>Here is a small gallery of custom components built from JavaServer
Faces technology.</p>


<table border="1">

<tr>

<th>Component Content</th> 

<th>View JSP Source</th> 

<th>View Java Source</th> 

<th>Execute JSP</th></tr>

<tr>

<td>Image Map
</td>

<td><a href="ShowSource.jsp?filename=/imagemap.jsp"><img src="images/code.gif" width="24" height="24" border="0"></a>
</td>

<td>
<a href="ShowSource.jsp?filename=/src/components/taglib/MapTag.java">components/taglib/MapTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/taglib/AreaTag.java">components/taglib/AreaTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/taglib/ImageTag.java">components/taglib/ImageTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/components/UIArea.java">components/components/UIArea.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/components/UIImage.java">components/components/UIImage.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/components/UIMap.java">components/components/UIMap.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/model/ImageArea.java">components/model/ImageArea.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/renderkit/AreaRenderer.java">components/renderkit/AreaRenderer.java</a><br>

</td>

<td><a href="faces/imagemap.jsp"><img src="images/execute.gif" width="24" height="24" border="0"></a>
</td>

</tr>


<tr>

<td>Menu or Tree
</td>

<td><a href="ShowSource.jsp?filename=/menu.jsp"><img src="images/code.gif" width="24" height="24" border="0"></a></td>

<td>

<a href="ShowSource.jsp?filename=/src/components/taglib/GraphMenuBarTag.java">components/taglib/GraphMenuBarTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/taglib/GraphMenuNodeTag.java">components/taglib/GraphMenuNodeTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/taglib/GraphMenuTreeTag.java">components/taglib/GraphMenuTreeTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/taglib/GraphTreeNodeTag.java">components/taglib/GraphTreeNodeTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/components/GraphComponent.java">components/components/GraphComponent.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/model/Graph.java">components/model/Graph.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/model/Node.java">components/model/Node.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/renderkit/MenuBarRenderer.java">components/renderkit/MenuBarRenderer.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/renderkit/MenuTreeRenderer.java">components/renderkit/MenuTreeRenderer.java</a><br>

</td>

<td><a href="faces/menu.jsp"><img src="images/execute.gif" width="24" height="24" border="0"></a>
</td>

</tr>

<tr>

<td>Result Set
</td>

<td><a href="ShowSource.jsp?filename=/result-set.jsp"><img src="images/code.gif" width="24" height="24" border="0"></a>
</td>

<td>
<a href="ShowSource.jsp?filename=/src/components/taglib/ResultSetTag.java">components/taglib/ResultSetTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/renderkit/ResultSetControls.java">components/renderkit/ResultSetControls.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/renderkit/ResultSetRenderer.java">components/renderkit/ResultSetRenderer.java</a><br>
</td>

<td><a href="faces/result-set.jsp"><img src="images/execute.gif" width="24" height="24" border="0"></a>
</td>

</tr>

<tr>

<td>Tabbed Pane
</td>

<td><a href="ShowSource.jsp?filename=/tabbedpanes.jsp"><img src="images/code.gif" width="24" height="24" border="0"></a>
</td>

<td>
<a href="ShowSource.jsp?filename=/src/components/taglib/PaneTabTag.java">components/taglib/PaneTabTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/taglib/PaneTabLabelTag.java">components/taglib/PaneTabLabelTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/taglib/PaneTabbedTag.java">components/taglib/PaneTabbedTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/components/PaneComponent.java">components/components/PaneComponent.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/components/PaneSelectedEvent.java">components/components/PaneSelectedEvent.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/renderkit/TabLabelRenderer.java">components/renderkit/TabLabelRenderer.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/renderkit/TabRenderer.java">components/renderkit/TabRenderer.java</a><br>
<a href="ShowSource.jsp?filename=/src/components/renderkit/TabbedRenderer.java">components/renderkit/TabbedRenderer.java</a><br>
</td>

<td><a href="faces/tabbedpanes.jsp"><img src="images/execute.gif" width="24" height="24" border="0"></a>
</td>

</tr>

</table>

</body>
</head>
