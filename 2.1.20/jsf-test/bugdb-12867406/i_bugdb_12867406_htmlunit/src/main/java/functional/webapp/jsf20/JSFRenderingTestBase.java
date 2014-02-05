/**
 * 
 */
package wlstest.functional.webapp.jsf20;

import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Properties;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import wlstest.functional.webapp.jsf20.JSF20TestBase;

import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlArea;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;

/**
 * @author haili Sep 23, 2006 12:14:56 PM
 *
 */
public class JSFRenderingTestBase extends JSF20TestBase{
	private final String KEY_HOST = "host";
  private final String KEY_PORT = "port";
  protected static final String HTML_INPUT = "Input";
  protected static final String HTML_PAGE = "Page";
  protected static final String HTML_ANCHOR = "Anchor";
  protected static final String HTML_CLICKABLE = "Clickable";
  protected static final String HTML_AREA = "Area";
  protected static final String HTML_IMAGE = "Image";
  protected static final String HTML_TABLE = "Table";
  protected static final String HTML_SELECT = "Select";
  protected static final String HTML_OPTION = "Option";
  protected static final String HTML_RADIO = "Radio";
  protected static final String HTML_CHECKBOX = "CheckBox";
  protected static final String HTML_SPAN = "Span";
  protected static final String HTML_TABLE_DATA_CELL = "TableDataCell";
  
  protected String hostName;
  protected int port;
  protected String urlPrefix;

	protected StringBuilder messages;
	protected Formatter formatter;
  
	/**
	 * @param name
	 */
	public JSFRenderingTestBase(String name) {
		super(name);
	}

	protected void setup(Properties p) {
  	 String hostName = p.getProperty(KEY_HOST, "").trim();
     String port = p.getProperty(KEY_PORT, "").trim();
     
     if (hostName != null && hostName.length() > 0) {
         this.hostName = hostName;
     } else {
         throw new IllegalArgumentException("'"+KEY_HOST+"' not specified");
     }
     
     if (port != null && port.length() > 0) {
         this.port = Integer.parseInt(port);
     } else {
    	 	throw new IllegalArgumentException("'"+KEY_PORT+"' not specified");
     }
    
     urlPrefix = "http://" + hostName + ':' + port;
     
     messages = new StringBuilder(128);
     formatter = new Formatter(messages);
  }
  
  protected Page getPage(WebClient client, String path) throws Exception{
  	try {
  		Page page = client.getPage(new URL(urlPrefix + path));
  		return page;
  	} catch (Exception ex) {
  		formatter.format("Unable to fetch page with '%s'", path);
  		throw ex;
  	} 	
  }

  protected HtmlPage getPage(String path) throws Exception {
  	WebClient client = new WebClient();
  	client.setPrintContentOnFailingStatusCode(true);
  	client.setRedirectEnabled(true);
  	client.setThrowExceptionOnScriptError(true);
  	client.setJavaScriptEnabled(true);
  	new JavaScriptEngine(client);
  	return (HtmlPage) getPage(client, path);
  } 
  
  protected JavaScriptPage getJavaScriptPage(String path) throws Exception {
  	WebClient client = new WebClient();
  	client.setPrintContentOnFailingStatusCode(true);
  	client.setRedirectEnabled(true);
  	client.setThrowExceptionOnScriptError(true);
  	client.setJavaScriptEnabled(true);
  	new JavaScriptEngine(client);
  	return (JavaScriptPage) getPage(client, path);
  } 
  
  protected String getTitle(HtmlPage root) {
  	return root.getTitleText();
  }
  
  protected HtmlInput getInputIncludingId(HtmlPage root, String id) {

  	return getInputIncludingAttr(root, "id", id);
  }
  
  protected HtmlInput getInputIncludingId(HtmlPage root, String part1, String part2) {

  	HtmlInput result = null;

  	List list = getAllElementsOfGivenClass(root, null, HtmlInput.class);
  	for (int i = 0; i < list.size(); i++) {
  		result = (HtmlInput) list.get(i);
  		if (-1 != result.getIdAttribute().indexOf(part1) && -1 != result.getIdAttribute().indexOf(part2)) {
  			break;
  		}
  		result = null;
  	}
  	return result;
  }
  
  protected HtmlInput getInputIncludingValue(HtmlPage root, String value) {

  	return getInputIncludingAttr(root, "value", value);
  }
  
  protected HtmlInput getInputIncludingAttr(HtmlPage root, String attr, String value) {

  	HtmlInput result = null;

  	List list = getAllElementsOfGivenClass(root, null, HtmlInput.class);
  	for (int i = 0; i < list.size(); i++) {
  		result = (HtmlInput) list.get(i);
  		if (-1 != result.getAttribute(attr).indexOf(value)) {
  			break;
  		}
  		result = null;
  	}
  	return result;
  }
  
  protected HtmlInput getInputIncludingAttrInside(HtmlElement root, String attr, String value) {

  	HtmlInput result = null;

  	List list = getAllElementsOfGivenClassInside(root, null, HtmlInput.class);
  	for (int i = 0; i < list.size(); i++) {
  		result = (HtmlInput) list.get(i);
  		if (-1 != result.getAttribute(attr).indexOf(value)) {
  			break;
  		}
  		result = null;
  	}
  	return result;
  }
  
  protected HtmlAnchor getAnchorIncludingId(HtmlPage root, String id) {

  	HtmlAnchor result = null;

  	List list = getAllElementsOfGivenClass(root, null, HtmlAnchor.class);
  	for (int i = 0; i < list.size(); i++) {
  		result = (HtmlAnchor) list.get(i);
  		if (-1 != result.getIdAttribute().indexOf(id)) {
  			break;
  		}
  		result = null;
  	}
  	return result;
  }
  
  protected HtmlAnchor getAnchorIncludingText(HtmlPage root, String text) {

  	HtmlAnchor result = null;

  	List list = getAllElementsOfGivenClass(root, null, HtmlAnchor.class);
  	for (int i = 0; i < list.size(); i++) {
  		result = (HtmlAnchor) list.get(i);
  		if (result.asText().equals(text)) {
  			return result;
  		}
  	}
  	return null;
  }
  
  protected HtmlAnchor getAnchorIncludingOnclick(HtmlPage root, String text1, String text2) {
  	HtmlAnchor result = null;

  	List list = getAllElementsOfGivenClass(root, null, HtmlAnchor.class);
  	for (int i = 0; i < list.size(); i++) {
  		result = (HtmlAnchor) list.get(i);
  		if (-1!=result.getOnClickAttribute().indexOf(text1) && -1!=result.getOnClickAttribute().indexOf(text2)) {
  			break;
  		}
  		result = null;
  	}
  	return result;
  }
  
  protected HtmlArea getFirstAreaIncludingAlt(HtmlPage root, String alt) {

  	HtmlArea result = null;

  	List list = getAllElementsOfGivenClass(root, null, HtmlArea.class);
  	for (int i = 0; i < list.size(); i++) {
  		result = (HtmlArea) list.get(i);
  		if (-1 != result.getAltAttribute().indexOf(alt)) {
  			break;
  		}
  		result = null;
  	}
  	return result;
  }
  
  protected ClickableElement getClickableIncludingId(HtmlPage root, String id) {

  	ClickableElement result = null;

  	List list = getAllElementsOfGivenClass(root, null, ClickableElement.class);
  	for (int i = 0; i < list.size(); i++) {
  		result = (ClickableElement) list.get(i);
  		if (-1 != result.getIdAttribute().indexOf(id)) {
  			break;
  		}
  		result = null;
  	}
  	return result;
  }
  
  protected ClickableElement getClickableIncludingId(HtmlPage root, String part1, String part2) {

  	ClickableElement result = null;

  	List list = getAllElementsOfGivenClass(root, null, ClickableElement.class);
  	for (int i = 0; i < list.size(); i++) {
  		result = (ClickableElement) list.get(i);
  		if (-1 != result.getIdAttribute().indexOf(part1) && -1 != result.getIdAttribute().indexOf(part2)) {
  			break;
  		}
  		result = null;
  	}
  	return result;
  }
  
  protected ClickableElement getClickableIncludingName(HtmlPage root, String name) {

  	ClickableElement result = null;

  	List list = getAllElementsOfGivenClass(root, null, ClickableElement.class);
  	for (int i = 0; i < list.size(); i++) {
  		result = (ClickableElement) list.get(i);
  		if (result.getAttribute("name").equals(name)) {
  			break;
  		}
  		result = null;
  	}
  	return result;
  }
  
  private ClickableElement getClickableIncludingClass(HtmlPage root, String name, Class cls) {

  	ClickableElement result = null;

  	List list = getAllElementsOfGivenClass(root, null, cls);
  	for (int i = 0; i < list.size(); i++) {
  		result = (ClickableElement) list.get(i);
  		if (result.getClassAttribute().equals(name)) {
  			break;
  		}
  		result = null;
  	}
  	return result;
  }
  
  protected HtmlSpan getHtmlSpanIncludingClass(HtmlPage root, String className) {
  	ClickableElement result = null;
  	result = getClickableIncludingClass(root, className, HtmlSpan.class);
  	if(result != null)
  		return (HtmlSpan)result;
  	return null;
  }
  
  protected HtmlSpan getHtmlSpanIncludingAttr(HtmlPage root, String attrName, String attrValue) {
  	HtmlSpan result = null;

  	List list = getAllElementsOfGivenClass(root, null, HtmlSpan.class);
  	for (int i = 0; i < list.size(); i++) {
  		result = (HtmlSpan) list.get(i);
  		if (-1 != result.getAttribute(attrName).indexOf(attrValue)) {
  			break;
  		}
  		result = null;
  	}
  	return result;
  }
  
  protected ClickableElement getClickableIncludingClass(HtmlPage root, String cls) {
  	return getClickableIncludingClass(root, cls, ClickableElement.class);
  }
  
  protected HtmlImage getFirstImageIncludingSrc(HtmlPage root, String src) {
  	
  	HtmlImage result = null;

  	List list = getAllElementsOfGivenClass(root, null, HtmlImage.class);
  	for (int i = 0; i < list.size(); i++) {
  		result = (HtmlImage) list.get(i);
  		System.out.println("[image]"+result.asXml());
  		if (-1 != result.getSrcAttribute().indexOf(src)) {
  			break;
  		}
  		result = null;
  	}
  	return result;
  }
  
  protected HtmlTable getFirstTableIncludingClass(HtmlPage root, String className) {
  	ClickableElement result = null;
  	result = getClickableIncludingClass(root, className, HtmlTable.class);
  	if(result != null)
  		return (HtmlTable)result;
  	return null;
  }
 
  protected HtmlInput getInputIncludingValue(HtmlElement root, String value) {
  	HtmlInput result = null;

  	List list = getAllElementsOfGivenClassInside(root, null, HtmlInput.class);
  	for (int i = 0; i < list.size(); i++) {
  		result = (HtmlInput) list.get(i);
  		if (-1 != result.getValueAttribute().indexOf(value)) {
  			break;
  		}
  		result = null;
  	}
  	return result;	
  }

  
  protected List getAllElementsOfGivenClass(HtmlPage root,
  		List<HtmlElement> list,
  		Class matchClass) {

  	return getAllElementsOfGivenClass(root.getDocumentElement(),
  			list,
  			matchClass);

  }
  
  protected List getAllElementsOfGivenClassInside(HtmlElement root,
  		List<HtmlElement> list,
  		Class matchClass) {

  	return getAllElementsOfGivenClass(root, list, matchClass);
  }
  
  protected List getAllElementsOfGivenClass(HtmlElement root,
  		List<HtmlElement> list,
  		Class matchClass) {

  	if (null == root) {
  		return list;
  	}
  	if (null == list) {
  		list = new ArrayList<HtmlElement>();
  	}

  	for (Iterator<HtmlElement> i = root.getAllHtmlChildElements().iterator(); i.hasNext(); ) {
  		Object obj = i.next();
      
      getAllElementsOfGivenClass((HtmlElement)obj, list, matchClass);
  	}

  	if (matchClass.isInstance(root)) {
  		if (!list.contains(root)) {
  			list.add(root);
  		}
  	}
  	return list;
  }
  
  protected Element getElementWithId(Document root, String id) {

  	return root.getElementById(id);
  }
  
  protected void validateExistenceWithId(String id,
  		String elementName,
  		HtmlElement element) {
  	boolean expected = true;
  	validateExistenceWithId(expected, id, elementName, element);
  }

  protected void validateExistenceWithId(boolean expected, String id,
  		String elementName,
  		HtmlElement element) {

  	validateExistenceWith(expected, "ID", elementName, id, element);
  }
  
  protected void validateExistenceWithName(String name,
  		String elementName,
  		HtmlElement element) {
  	boolean expected = true;
  	validateExistenceWithName(expected, name, elementName, element);
  }
  
  protected void validateExistenceWithName(boolean expected, String name,
  		String elementName,
  		HtmlElement element) {
  	validateExistenceWith(expected, "Name", elementName, name, element);	
  }
  
  protected void validateExistenceWithAlt(String alt,
  		String elementName,
  		HtmlElement element) {

  	boolean expected = true;
  	validateExistenceWithAlt(expected, alt, elementName, element);
  }
  
	protected void validateExistenceWithAlt(boolean expected, String alt, String elementName, HtmlElement element) {
		validateExistenceWith(expected, "Alt", elementName, alt, element);	
	}

	protected void validateExistenceWithSrc(String src,
  		String elementName,
  		HtmlElement element) {
		boolean expected = true;
  	validateExistenceWithSrc(expected, src, elementName, element);
  }
  
	protected void validateExistenceWithSrc(boolean expected, String alt, String elementName, HtmlElement element) {
		validateExistenceWith(expected, "Src", elementName, alt, element);	
	}
	
  protected void validateExistenceWithClass(String cls,
  		String elementName,
  		HtmlElement element) {
  	boolean expected = true;
  	validateExistenceWithClass(expected, cls, elementName, element);
  }

	private void validateExistenceWithClass(boolean expected, String cls, String elementName, HtmlElement element) {
		validateExistenceWith(expected, "Class", elementName, cls, element);	
	}

	protected void validateExistenceWith(boolean expected, String attr, String elementType, String value, Object element) {
  	if (element == null && expected) {
  		formatter.format("Unable to find rendered '%s' element containing " +
  				"the %s '%s'\n", elementType, attr, value);
  		fail(messages.toString());
  	} else if(element!=null && !expected) {
  		formatter.format("Should not find rendered '%s' element containing " +
  				"the %s '%s'\n", elementType, attr, value);
  		fail(messages.toString());
  	}
  }

	protected void validateEqualWith(String attr, String elementType, Object expObj, Object myObj) {
		validateEqualWith(true, attr, elementType, expObj, myObj);
	}
	
	protected void validateEqualWith(boolean expected, String attr, String elementType, Object expObj, Object myObj) {
  	if (!expObj.equals(myObj) && expected) {
  		formatter.format("'%s' of %s is '%s', not equal with '%s'\n", attr, elementType, myObj ,expObj );
  		fail(messages.toString());
  	} else if(expObj.equals(myObj) && !expected) {
  		formatter.format("'%s' of %s is '%s', should not equal with '%s'\n", attr, elementType, myObj, expObj);
  		fail(messages.toString());
  	}
  }
	
	protected void validateStringIncludeWith(boolean expected, String attr, String elementType, String expObj, String myObj) {
  	if (-1 == expObj.indexOf(myObj) && expected) {
  		formatter.format("'%s' of %s is '%s', not includes '%s'\n", attr, elementType, myObj ,expObj );
  		fail(messages.toString());
  	} else if(-1 != expObj.indexOf(myObj) && !expected) {
  		formatter.format("'%s' of %s is '%s', should not includes '%s'\n", attr, elementType, myObj, expObj);
  		fail(messages.toString());
  	}
  }
	
	protected void validateStartWith(String attr, String elementType, String expObj, String myObj) {
		validateStartWith(true, attr, elementType, expObj, myObj);
	}
	
	private void validateStartWith(boolean expected, String attr, String elementType, String expObj, String myObj) {
  	if (!myObj.startsWith(expObj) && expected) {      
  		fail(String.format("'%s' of %s is '%s', not start with '%s'\n", attr, elementType, myObj, expObj));
  	} else if(myObj.startsWith(expObj) && !expected) {
  		fail(String.format("'%s' of %s is '%s', should not start with '%s'\n", attr, elementType, myObj, expObj));
  	}
  }
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Properties p = System.getProperties();
		this.setup(p);
	}
}
