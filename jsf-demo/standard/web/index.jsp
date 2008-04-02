<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- $Id: index.jsp,v 1.3 2003/10/17 03:53:49 eburns Exp $ -->
<html>
  <head>
    <title>JavaServer Faces 1.0 Standard RenderKit Demo</title>
  </head>

  <body>

<%
  pageContext.removeAttribute("list");
%>

    <h1>JavaServer Faces 1.0 Standard RenderKit Demo</h1>

	<ul>

	  <li><p><a href="faces/UICommand.jsp">UICommand</a>
	  </p></li>

	  <li><p><a href="faces/UIData.jsp">UIData</a>
	  </p></li>

	  <li><p><a href="faces/UIGraphic.jsp">UIGraphic</a>
	  </p></li>

	  <li><p><a href="faces/UIInput.jsp">UIInput</a>
	  </p></li>

	  <li><p><a href="faces/UIOutput.jsp">UIOutput</a>
          </p></li>

	  <li><p><a href="faces/UIPanel.jsp">UIPanel</a>
	  </p></li>

	  <li><p><a href="faces/UISelectBoolean.jsp">UISelectBoolean</a>
	  </p></li>

	  <li><p><a href="faces/UISelectMany.jsp">UISelectMany</a>
	  </p></li>

	  <li><p><a href="faces/UISelectOne.jsp">UISelectOne</a>
	  </p></li>

	  <li><p><a href="faces/DataModel.jsp">DataModel</a>
	  </p></li>

	</ul>

  </body>
</html>
