/**
 * 
 */
package wlstest.functional.webapp.jsf20.custombeanscope;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import java.util.List;
import wlstest.functional.webapp.jsf20.JSFRenderingTestBase;
import com.gargoylesoftware.htmlunit.WebAssert;

public class CustomBeanScopeTest extends JSFRenderingTestBase {

	private static final String CONTEXT_ROOT = "/custom-bean-scope";
	/**
	 * @param name
	 */
	public CustomBeanScopeTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void testCustomBeanScope() throws Exception {
		HtmlPage page;
		page = getPage(CONTEXT_ROOT+"/");
		assertNotNull(page);
		WebAssert.assertTitleEquals(page, "Custom Scope Example");
    WebAssert.assertTextPresentInElement( page, "Resolved", "create"); 
    WebAssert.assertTextPresentInElement( page, "Resolved", "scopeReference"); 
    WebAssert.assertTextPresentInElement( page, "Resolved", "nonCreate"); 
    WebAssert.assertElementPresent(page, "form");
    WebAssert.assertElementPresent(page, "form:reload");
    WebAssert.assertElementPresent(page, "form:destroy");
    
    HtmlTable table = page.getHtmlElementById("grid");
    assertEquals("PostConstruct",table.getCellAt(1,0).asText());
    assertEquals("Invoked",table.getCellAt(1,1).asText());
    assertEquals("PreDestroy",table.getCellAt(2,0).asText());
    assertEquals("",table.getCellAt(2,1).asText());
    
    HtmlInput reload = (HtmlInput)page.getElementById("form:reload");
    HtmlInput destroy = (HtmlInput)page.getElementById("form:destroy");
    page = reload.click();
    table = page.getHtmlElementById("grid");
    assertEquals("PostConstruct",table.getCellAt(1,0).asText());
    assertEquals("",table.getCellAt(1,1).asText());
    assertEquals("PreDestroy",table.getCellAt(2,0).asText());
    assertEquals("",table.getCellAt(2,1).asText());
    page = destroy.click();
    table = page.getHtmlElementById("grid");
    assertEquals("PostConstruct",table.getCellAt(1,0).asText());
    assertEquals("Invoked",table.getCellAt(1,1).asText());
    assertEquals("PreDestroy",table.getCellAt(2,0).asText());
    assertEquals("Invoked",table.getCellAt(2,1).asText());
	}
  
}
