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

<tr><th>Component Content</th> <th>View JSP Source</th> <th>Execute JSP</th></tr>

<tr>

<td>Menu or Tree
</td>

<td><a href="ShowSource.jsp?filename=/menu.jsp"><img src="images/code.gif" width="24" height="24" border="0"></a></td>

<td><a href="faces/menu.jsp"><img src="images/execute.gif" width="24" height="24" border="0"></a>
</td>

</tr>

<tr>

<td>Result Set
</td>

<td><a href="ShowSource.jsp?filename=/result-set.jsp"><img src="images/code.gif" width="24" height="24" border="0"></a>
</td>

<td><a href="faces/result-set.jsp"><img src="images/execute.gif" width="24" height="24" border="0"></a>
</td>

</tr>

</table>

</body>
</head>
