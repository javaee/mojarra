<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>TestViewTag</title>
    <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
    <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

  </head>

  <body>
    <h1>TestViewTag</h1>

<f:view locale="ps-PS_Traditional">
<h:form formName="testViewTag" styleClass="formClass" accept="html">

<table>

<tr>
<td>Name:</td>
<td><h:input_text value="Gilligan"/></td>
<td><h:command_button value="submit"/></td>
</tr>
</table>

</h:form>
</f:view>


    <hr>
    <address><a href="mailto:Ed Burns <ed.burns@sun.com>"></a></address>
<!-- Created: Wed Oct 15 17:31:05 Eastern Daylight Time 2003 -->
<!-- hhmts start -->
Last modified: Wed Oct 15 17:34:55 Eastern Daylight Time 2003
<!-- hhmts end -->
  </body>
</html>
