<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>autocomplete.jsp</title>

 <%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
 <%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

  </head>
  <body>
      <f:view>
          autocomplete on -&gt; no attribute rendered: <h:inputSecret id="a" autocomplete="on"/>
          autocomplete off -&gt; attribute rendered: <h:inputSecret id="b" autocomplete="off"/>
          no autocomplete defined -&gt; no attribute rendered: <h:inputSecret id="c"/>
          autocomplete on -&gt; no attribute rendered: <h:inputText id="d" autocomplete="on"/>
          autocomplete off -&gt; attribute rendered: <h:inputText id="e" autocomplete="off"/>
          no autocomplete defined -&gt; no attribute rendered: <h:inputText id="f"/>
      </f:view>
  </body>
</html>


