<%@ include file="header.inc" %>
<f:view>
    <risb:multiFileUpload maxFileSize="10240" 
          fileHolder="#{testBean.fileHolder}" destinationUrl="#{testBean.destination}"
          width="500px" height="250px" type="full" buttonText="Custom Text!"/>
</f:view>
<%@ include file="footer.inc" %>