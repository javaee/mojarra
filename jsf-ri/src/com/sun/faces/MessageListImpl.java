package com.sun.faces;

import java.util.*;

import javax.faces.Constants;
import javax.faces.MessageList;
import javax.faces.ObjectManager;
import javax.faces.Message;
import javax.faces.FacesException;
import javax.faces.MessageFactory;
import javax.faces.FacesFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;

public class MessageListImpl extends MessageList implements FacesFactory
{
	private String _factoryId = Constants.DEFAULT_MESSAGE_FACTORY_ID;
	
	private	Locale	_locale;
	
	/** List of Message's */
	private	ArrayList	_list = new ArrayList(5);

public Object newInstance(String facesName, ServletRequest req, 
			  ServletResponse res) throws FacesException
{
    throw new FacesException("Can't create MessageList from Request and Response");
}

public Object newInstance(String facesName, ServletContext ctx) throws FacesException
{
    throw new FacesException("Can't create MessageList from ServletContext");
}

public Object newInstance(String facesName) throws FacesException
{
    return new MessageListImpl();
}

public Object newInstance(String facesName, Map args) throws FacesException
{
    throw new FacesException("Can't create MessageList from map");
}


	public void addMessage(Message msg)
	{
		_list.add(msg);
	}

	public Message addMessage(String msgId, String reference, Object[] parms)
		throws FacesException 
	{
		ObjectManager om = ObjectManager.getInstance();
		MessageFactory mf = (MessageFactory)om.get(_factoryId);
		
		Message m = mf.newMessage(msgId, reference, _locale, parms);
		_list.add(m);
		return m;		
	}
	
    public int highestSeverity()
    {
    	int r = 0;
    	Iterator it = iterator();
    	while(it.hasNext())
    	{
    		Message m = (Message)it.next();
    		if (m.getSeverity() > r)r = m.getSeverity();
    	}
    	return r;
    }
	
    public Iterator iterator()
    {
    	return _list.iterator();
    }
    
    public Iterator iterator(String reference)
    {
    	ArrayList list = new ArrayList(_list.size());
    	Iterator it = iterator();
    	while(it.hasNext())
    	{
    		Message m = (Message)it.next();
    		if (reference == null)
    		{
    			if (m.getReferenceId() == null)list.add(m);
    		}
    		else
    		{
    			if (reference.equals(m.getReferenceId()))list.add(m);
    		}
    	}
    	return list.iterator();
    }

	
    public void setLocale(Locale locale)
    {
    	_locale = locale;
    }

	
	public void setMessageFactory(String factoryId) 
	{
		_factoryId = factoryId;
	}

}

