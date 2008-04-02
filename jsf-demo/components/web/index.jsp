<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

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
  pageContext.removeAttribute("list", PageContext.SESSION_SCOPE);
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
<a href="ShowSource.jsp?filename=/src/java/components/components/AreaComponent.java">components/components/AreaComponent.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/components/AreaSelectedEvent.java">components/components/AreaSelectedEvent.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/components/AreaSelectedListener.java">components/components/AreaSelectedListener.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/components/MapComponent.java">components/components/MapComponent.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/model/ImageArea.java">components/model/ImageArea.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/renderkit/AreaRenderer.java">components/renderkit/AreaRenderer.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/renderkit/MapRenderer.java">components/renderkit/MapRenderer.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/taglib/AreaTag.java">components/taglib/AreaTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/taglib/MapTag.java">components/taglib/MapTag.java</a><br>



</td>

<td><a href="imagemap.faces"><img src="images/execute.gif" width="24" height="24" border="0"></a>
</td>

</tr>


<tr>

<td>Menu or Tree
</td>

<td><a href="ShowSource.jsp?filename=/menu.jsp"><img src="images/code.gif" width="24" height="24" border="0"></a></td>

<td>

<a href="ShowSource.jsp?filename=/src/java/components/taglib/GraphMenuBarTag.java">components/taglib/GraphMenuBarTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/taglib/GraphMenuNodeTag.java">components/taglib/GraphMenuNodeTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/taglib/GraphMenuTreeTag.java">components/taglib/GraphMenuTreeTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/taglib/GraphTreeNodeTag.java">components/taglib/GraphTreeNodeTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/components/GraphComponent.java">components/components/GraphComponent.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/model/Graph.java">components/model/Graph.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/model/Node.java">components/model/Node.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/renderkit/MenuBarRenderer.java">components/renderkit/MenuBarRenderer.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/renderkit/MenuTreeRenderer.java">components/renderkit/MenuTreeRenderer.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/demo/model/GraphBean.java">demo/model/GraphBean.java</a>
</td>

<td><a href="menu.faces"><img src="images/execute.gif" width="24" height="24" border="0"></a>
</td>

</tr>

<tr>

<td>Repeater
</td>

<td><a href="ShowSource.jsp?filename=/repeater.jsp"><img src="images/code.gif" width="24" height="24" border="0"></a>
</td>

<td>
<a href="ShowSource.jsp?filename=/src/java/components/taglib/DataRepeaterTag.java">components/taglib/DataRepeaterTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/renderkit/RepeaterRenderer.java">components/renderkit/RepeaterRenderer.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/demo/model/RepeaterBean.java">demo/model/RepeaterBean.java</a><br>
</td>

<td><a href="repeater.faces"><img src="images/execute.gif" width="24" height="24" border="0"></a>
</td>

</tr>

<tr>

<td>Scroller
</td>

<td><a href="ShowSource.jsp?filename=/result-set.jsp"><img src="images/code.gif" width="24" height="24" border="0"></a>
</td>

<td>
<a href="ShowSource.jsp?filename=/src/java/components/taglib/ScrollerTag.java">components/taglib/ScrollerTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/components/ScrollerComponent.java">components/components/ScrollerComponent.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/demo/model/ResultSetBean.java">demo/model/ResultSetBean.java</a>
</td>

<td><a href="result-set.faces"><img src="images/execute.gif" width="24" height="24" border="0"></a>
</td>

</tr>

<tr>

<td>Tabbed Pane
</td>

<td><a href="ShowSource.jsp?filename=/tabbedpanes.jsp"><img src="images/code.gif" width="24" height="24" border="0"></a>
</td>

<td>
<a href="ShowSource.jsp?filename=/src/java/components/taglib/PaneTabTag.java">components/taglib/PaneTabTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/taglib/PaneTabLabelTag.java">components/taglib/PaneTabLabelTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/taglib/PaneTabbedTag.java">components/taglib/PaneTabbedTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/components/PaneComponent.java">components/components/PaneComponent.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/components/PaneSelectedEvent.java">components/components/PaneSelectedEvent.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/renderkit/TabLabelRenderer.java">components/renderkit/TabLabelRenderer.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/renderkit/TabRenderer.java">components/renderkit/TabRenderer.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/renderkit/TabbedRenderer.java">components/renderkit/TabbedRenderer.java</a><br>
</td>

<td><a href="tabbedpanes.faces"><img src="images/execute.gif" width="24" height="24" border="0"></a>
</td>

</tr>

<tr>

<td>Chart
</td>

<td><a href="ShowSource.jsp?filename=/chart.jsp"><img src="images/code.gif" width="24" height="24" border="0"></a></td>

<td>

<a href="ShowSource.jsp?filename=/src/java/components/taglib/ChartTag.java">components/taglib/ChartTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/taglib/ChartItemTag.java">components/taglib/ChartItemTag.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/components/ChartComponent.java">components/components/ChartComponent.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/components/ChartItemComponent.java">components/components/ChartItemComponent.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/renderkit/ChartServlet.java">components/renderkit/ChartServlet.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/components/model/ChartItem.java">components/model/ChartItem.java</a><br>
<a href="ShowSource.jsp?filename=/src/java/demo/model/ChartBean.java">demo/model/ChartBean.java</a>
</td>

<td><a href="chart.faces"><img src="images/execute.gif" width="24" height="24" border="0"></a>
</td>

</tr>

<tr>

</table>

</body>
</head>
