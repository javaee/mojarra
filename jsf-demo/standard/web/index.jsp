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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- $Id: index.jsp,v 1.8 2005/09/13 17:28:40 rogerk Exp $ -->
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

	  <li><p><a href="faces/156.jsp">156</a>
	  </p></li>
	</ul>

  </body>
</html>
