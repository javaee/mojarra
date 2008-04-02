<%@ page contentType="text/html"
      %>
<%@ page import="javax.faces.context.FacesContext"
      %>
<%@ page import="javax.faces.el.ValueBinding"
      %>
<%@ page import="com.sun.faces.systest.model.TestBean"
      %>
<%

    // Instantiate our test bean in request scope
    TestBean bean = new TestBean();
    FacesContext context = FacesContext.getCurrentInstance();
    context.getExternalContext().getRequestMap().put
          ("testVB", bean);

    // Retrieve the bean itself with a value binding expression
    ValueBinding vb = context.getApplication().createValueBinding
          ("#{testVB}");
    Object result;
    try {
        result = vb.getValue(context);
    } catch (Exception e) {
        out.println("/valueBinding01.jsp FAILED - getValue() exception: " + e);
        e.printStackTrace(System.out);
        return;
    }

    // Validate the result
    if (result == null) {
        out.println("/valueBinding01.jsp FAILED - getValue() returned null");
    } else if (bean != result) {
        out.println("/valueBinding01.jsp FAILED - getValue() returned "
                    + result);
    } else {
        out.println("/valueBinding01.jsp PASSED");
    }

%>
