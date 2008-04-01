package com.sun.faces;

import java.util.HashMap;
import java.util.Locale;

import javax.faces.Message;

public class MessageCatalog 
{
	private	Locale 	_locale;
	private	HashMap	_map;
	
	public MessageCatalog(HashMap map, Locale locale)
	{
		_map = map;
		_locale = locale;
	}
	
	public MessageTemplate get(Object msgId)
	{
		return (MessageTemplate)_map.get(msgId);
	}
}

