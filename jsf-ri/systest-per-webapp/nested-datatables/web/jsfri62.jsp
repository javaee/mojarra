<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
  <html>
    <head>
      <title>Nested Tables</title>
    </head>
    <body>
    
    <p>Test from issue <a href="https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=62">62</a>.</p>
      <h:form id="form">
        <h:dataTable value="#{outer62.model}" var="yyy">
          <h:column>
            outer
            <h:dataTable value="#{yyy.model}" var="www">
              <h:column>
                inner
                <h:commandLink action="#{outer62.action}">link</h:commandLink>
             </h:column>
           </h:dataTable>
          </h:column>
         </h:dataTable>
         
         <p>Current Status = <h:outputText value="#{outer62.curStatus}" /></p>
      </h:form>
    </body>
  </html>
</f:view>

