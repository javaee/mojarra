<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<html><head><title>test From Michael Youngstrom</title></head>
<body>
<p>Test from issue <a href="https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=159">159</a>.</p>


<p>When nestedTest is loaded you are presented with 4 links.  If you click on "test
bean: 4" then "You clicked on link: 4" is correctly printed to the console. 
However, if you click on "test bean: 1" then "You clicked on link: 3" is
INCORRECTLY displayed.</p>

<p>The problem is there are actually 2 DataModels associated with the datatable
with var="nestedBean".  But when the Action Event is fired there is no way for
the UIData to switch to the correct DataModel.</p>

<f:view>
	<h:form>
		<h:dataTable value="#{nestedTestList}" var="nestedList">
			<h:column>
				<h:dataTable value="#{nestedList}" var="nestedBean">
					<h:column>
						<h:commandLink actionListener="#{nestedBean.executeLink}"><h:outputText
value="test bean: #{nestedBean.id}"/></h:commandLink>
					</h:column>
				</h:dataTable>
			</h:column>
		</h:dataTable>
	</h:form>
	
	<p><h:outputText value="#{whichLink}" /></p>
	
</f:view>

</body>
</html>
