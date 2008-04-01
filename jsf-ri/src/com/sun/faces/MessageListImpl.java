package com.sun.faces;

import java.util.*;

import javax.faces.*;

public class MessageListImpl extends MessageList 
{
	public final static String DEFAULT_MESSAGE_FACTORY_ID = "javax.faces.DefaultMessageFactory";
	private String _factoryId = DEFAULT_MESSAGE_FACTORY_ID;
	
	private	Locale	_locale;
	
	/** List of Message's */
	private	ArrayList	_list = new ArrayList(5);

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

