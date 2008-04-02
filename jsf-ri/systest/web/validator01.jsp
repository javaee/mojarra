<%@ page contentType="text/html"
      %>
<%@ page import="javax.faces.FactoryFinder"
      %>
<%@ page import="javax.faces.application.Application"
      %>
<%@ page import="javax.faces.application.ApplicationFactory"
      %>
<%@ page import="javax.faces.validator.Validator"
      %>
<%

    // Acquire our Application instance
    ApplicationFactory afactory = (ApplicationFactory)
          FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
    Application appl = afactory.getApplication();

    // replace mappings provided by the JSF implementation for Length
    // LengthValidator and make sure that it can be retrieved using the
    // standard identifier.
    appl.addValidator("Length", "com.sun.faces.systest.TestValidator");

    // try to retrieve our component from Application
    Validator result = appl.createValidator("Length");
    // report the result
    if (result == null ||
        !(result instanceof com.sun.faces.systest.TestValidator)) {
        out.println("/validator01.jsp FAILED");
        return;
    } else {
        out.println("/validator01.jsp PASSED");
    }

%>
