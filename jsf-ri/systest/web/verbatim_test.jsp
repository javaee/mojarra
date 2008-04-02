<%-- $Id: verbatim_test.jsp,v 1.8 2004/02/05 05:05:10 horwat Exp $ --%>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>

  <html>

    <head>
      <title>Test of the Verbatim Tag</title>
    </head>

    <body>

      <h1>Test of the Verbatim Tag</h1>

      <p>
        <f:verbatim>
          [DEFAULT]
          This text <b>has angle brackets</b>.
          The angle brackets MUST NOT be escaped.
        </f:verbatim>
      </p>

      <p>
        <f:verbatim escape="false">
          [FALSE]
          This text <b>has angle brackets</b>.
          The angle brackets MUST NOT be escaped.
        </f:verbatim>
      </p>

      <p>
        <f:verbatim escape="true">
          [TRUE]
          This text <b>has angle brackets</b>.
          The angle brackets MUST be escaped.
        </f:verbatim>
      </p>

    </body>

  </html>

</f:view>

