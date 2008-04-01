package com.sun.faces;

import java.text.MessageFormat;
import java.util.HashMap;

import javax.faces.Message;

public class MessageImpl implements Message 
{
	/** the static part of the message */
	private	MessageTemplate	_template;
	
	/** an optional object manager id to the component that generated the message. */
	private	String		_reference;
	
	/** substitution parameters */
	private	Object[]	_parms;
	
	/** cached copy of the substituted first level text */
	private	String		_firstLevel;
	
	/** cached copy of the substituted second level text */
	private	String		_secondLevel;
	
	/** optional message attributes */
	private HashMap		_attributes;
	
	
	public MessageImpl(MessageTemplate template, String reference, Object[] parms)
	{
		_template = template;
		_reference = reference;
		_parms = parms;	
	}
	
	public int getSeverity() 
	{
		return _template.getSeverity();
	}

	public String getFirstLevelText() 
	{
		String r = _template.getFirstLevel();
		if (r == null)
		{
			r = "There is a problem in the message file for entry '" + _template.getId()
				+ "' it is missing the first level message";
		}
		return r; 
	}

	public String getSecondLevelText() 
	{
		return _template.getSecondLevel();
	}

	public String getReferenceId() 
	{
		return _reference;
	}
	
	public String toString()
	{
		if (_firstLevel == null)
		{
			StringBuffer b = new StringBuffer(100);
			b.append(_template.getId());
			b.append(": ");
			TempLogger.warn("First level: " + getFirstLevelText());
			MessageFormat mf = new MessageFormat(getFirstLevelText());
			if (_template.getLocale() != null)mf.setLocale(_template.getLocale());
			b.append(mf.format(_parms));
			_firstLevel = b.toString();
		}
		return _firstLevel;
	}
	
	public String	getSecondLevelMessage()
	{
		if (_secondLevel == null)
		{
			if (_template.getSecondLevel() == null)
			{
				_secondLevel = "Sorry, there is no additional information for this message";
			}
			else
			{
				StringBuffer b = new StringBuffer(100);
				// b.append(_template.getId());
				// b.append(": ");
				MessageFormat mf = new MessageFormat(_template.getSecondLevel());
				if (_template.getLocale() != null)mf.setLocale(_template.getLocale());
				b.append(mf.format(_parms));
				_secondLevel = b.toString();
			}
		}
		return _secondLevel;
		
	}
	
	public void putAttribute(Object key, Object value)
	{
		if (_attributes == null)_attributes = new HashMap(5);
		_attributes.put(key, value);
	}
	
	public Object getAttribute(Object key)
	{
		if (_attributes == null)return null;
		return _attributes.get(key);
	}

}

