<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<t:panelTab label="#{example_messages['tabbed_tab2']}" rendered="#{tabbedPaneBean.tab2Visible}">
    <h:inputTextarea ></h:inputTextarea>
</t:panelTab>
