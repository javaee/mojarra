package com.sun.faces;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;

import javax.faces.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class MessageFactoryImpl extends MessageFactory implements FacesFactory
{
	private	String	_resource = "JSFMessages";
	private	ClassLoader	_classLoader;

public Object newInstance(String facesName, ServletRequest req, 
			  ServletResponse res) throws FacesException
{
    throw new FacesException("Can't create MessageFactory from Request and Response");
}

public Object newInstance(String facesName, ServletContext ctx) throws FacesException
{
    throw new FacesException("Can't create MessageFactory from ServletContext");
}

public Object newInstance(String facesName) throws FacesException
{
    return new MessageFactoryImpl();
}

public Object newInstance(String facesName, Map args) throws FacesException
{
    throw new FacesException("Can't create MessageFactory from map");
}

    public MessageFactoryImpl() { 
    };
	
	/**
	 * A catalog of the message catalogs for this application. The key is a String and is
	 * formed from resource + language + country + varient.  The value is a MessageCatalog.
	 */
	private HashMap	_catalog = new HashMap(30);

	public Message newMessage(String msgId, String reference, Locale locale, Object[] parms)
		throws FacesException 
	{
		MessageCatalog catalog = findCatalog(locale);
		if (catalog == null)
		{
			TempLogger.error("No messages catalog for " + _resource);
			throw new IllegalArgumentException("No message catalogs for resource " 
				+ _resource);
		}
		MessageTemplate template = catalog.get(msgId);
		if (template == null)throw new IllegalArgumentException("The message id '" + msgId + "' was not found in the message catalog");
		
		return new MessageImpl(template, reference, parms);		
	}
	
    public void setClassLoader(ClassLoader loader)
    {
 		_classLoader = loader;   	
    }

	public void setResource(Object resource) 
	{
		_resource = (String)resource;
	}
	
	private MessageCatalog findCatalog(Locale locale)
	{
		String[] name = new String[4];
		int i = 3;
		StringBuffer b = new StringBuffer(100);
		b.append(_resource);
		name[i--] = _resource;
		
		b.append('_');
		b.append(locale.getLanguage());
		name[i--] = b.toString();
		
		b.append('_');
		b.append(locale.getCountry());
		name[i--] = b.toString();
		
		b.append('_');
		if (locale.getVariant().length() > 0)
		{
			b.append(locale.getVariant());
			name[i--] = b.toString();
		}
		
		for (int j=i+1; j<name.length; j++)
		{
			MessageCatalog cat = (MessageCatalog)_catalog.get(name[j]);
			if (cat == null)
			{
				InputStream in = _classLoader.getResourceAsStream(name[j]+".xml");
				if (in != null)
				{
					cat = loadMessages(in, locale);
					_catalog.put(name[j], cat);
				}
			}
			if (cat != null)
			{
				for (int k=i+1; k<j; k++)_catalog.put(name[k], cat);
				return cat;
			}
		}
		return null;
	}
	
	private MessageCatalog loadMessages(InputStream in, Locale locale)
	{
		// System.err.println("MessageFactoryImpl - loading messages");
		
		MessageLoader ml = new MessageLoader(locale);
		try
		{
			ml.load(in);
		}
		catch (Exception e)
		{
			TempLogger.error(e,e);
		}
		finally
		{
			try {in.close();}
			catch (Exception e){TempLogger.error(e,e);};
		}
		return new MessageCatalog(ml.getMap(), locale);
	}

}

class MessageLoader extends DefaultHandler
{
	/** An object that tells you where you are in the XML stream. */
	protected Locator	_locator;
	
	/** An optional name for the source that is used if error messages need 
	 *  to be generated.  */
	protected	String			_sourceName;
	
	/** Default parser name. */
	private static final String
		DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";
		
	private String _parserName = DEFAULT_PARSER_NAME;
				
	private XMLReader			_parser;
	
	/** current message */
	private	MessageTemplate	_message;
	
	private HashMap		_map = new HashMap(200);
	private Locale		_locale;

	public MessageLoader(Locale locale)
	{
		_locale = locale;
	}
	
	public void error(SAXParseException e) throws SAXException
	{
		TempLogger.error(formatException(e));
	}
	
	public void fatalError(SAXParseException e) throws SAXException
	{
		TempLogger.error(formatException(e));
	}
	
	public String formatException(SAXParseException e)
	{
		return (_sourceName == null ? "" : _sourceName + ": ")
			+ "line: " + e.getLineNumber()
			+ " col: " + e.getColumnNumber()
			+ " " + e.getMessage();
	}
	
	public HashMap getMap()
	{
		return _map;
	}
	
	public String getParserName()
	{
		return _parserName;
	}
	
	
	/**
	 * Start parsing the file.
	 *
	 * @param in The stream that we are processing
	 * @param out the writer that holds the copy
	 */
	public void load(InputStream in) throws Exception
	{
		InputSource is = new InputSource(in);
		load(is);
	}
	
	/**
	 * Start parsing the file.
	 *
	 * @param is The thing that we are processing.
	 * @param out The writer that holds the copy
	 */
	public void load(InputSource is) throws Exception
	{
		setParser();
		_parser.parse(is);
	}
	
	/**
	 * A call back method that tells us the location of the current parse event.
	 */
    public void setDocumentLocator (Locator locator)
    {
		_locator = locator;
    }
	
	/**
	 * Setup and initialize the parser
	 */
	protected void setParser()
	{
		if (_parser != null)return;
		try
		{	
			try
			{
				// It turns that this method doesn't work when running inside of Tomcat
				_parser = XMLReaderFactory.createXMLReader(getParserName());
			}
			catch (Exception e){}
			if (_parser == null)
				_parser = (XMLReader)getClass().getClassLoader().loadClass(getParserName()).newInstance();
			/*
				In the 1.3.1 level of the parser you could stop it from reading the DTD.  But this feature
				doesn't exist in the 1.2.1 parser, which is the parser that is currently being used by
				the platform.  
			*/
			try
			{
				_parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			}
			//FIXUP karasiuk: Use a real logger
			catch (Exception e1){TempLogger.error(e1,e1);}
			_parser.setContentHandler(this);
			_parser.setErrorHandler(this);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.toString());
		}
	
	}
	
	/**
	 * If you don't want to use the default parser you can explicitly tell 
	 * this class which one to use.
	 */
	public void setParserName(String name)
	{
		_parserName = name;
	}
	
	/**
	 * Set the name for the parse source that you would like to be displayed in 
	 * error messages.
	 */
	public void setSourceName(String name)
	{
		_sourceName = name;
	}
	
	public void startElement (String uri, String localName,
		      String qName, Attributes attr)
	{
		if (qName.equals("message"))
		{
			String id = attr.getValue("id");
			_message = new MessageTemplate(id);
			_message.setLocale(_locale);
			_map.put(id, _message);
			
			String severity = attr.getValue("severity");
			if (severity != null)
			try
			{
				int sev = Integer.parseInt(severity);
				_message.setSeverity(sev);
			}
			catch (Exception e)
			{
				TempLogger.error(e,e);
			}
			_message.setFirstLevel(attr.getValue("text"));
		} 
	}

        public void characters(char[] ch, int start, int length) {
            try {
               String secondlevel = new String(ch, start, length);
               if ( secondlevel != null ) {
                   secondlevel = secondlevel.trim();
               }
               if ((!secondlevel.equals("")) && _message != null ) {
                   _message.setSecondLevel(secondlevel);
               }
            } catch (Exception e ) {
            }  
        }			 

}
