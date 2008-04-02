<%@ page import="javax.servlet.RequestDispatcher" %><%

  // Generically link to a Faces Response for a Tree Id
  //
  // Query Parameters:
  //  responseTreeId           Tree Id to display via Faces

  String toTreeId = request.getParameter("responseTreeId");
  RequestDispatcher rd = application.getRequestDispatcher
    ("/faces" + toTreeId);
  rd.forward(request, response);

  // Should always forward or throw an exception

%>
