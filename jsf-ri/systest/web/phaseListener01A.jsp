<%
    String pageRefresh = System.getProperty("PageRefreshPhases");

    if (pageRefresh.equals("true")) {
        out.println("/phaseListener01.jsp PASSED");
    } else {
        out.println("/phaseListener01.jsp FAILED");
    }
%>
