<!--
 Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->

<%
    String pageRefresh = System.getProperty("PageRefreshPhases");

    if (pageRefresh.equals("true")) {
      out.println("/phaseListener01.jsp PASSED");
    } else {
      out.println("/phaseListener01.jsp FAILED");
    }
%>
