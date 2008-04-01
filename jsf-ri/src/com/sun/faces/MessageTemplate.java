package com.sun.faces;

import java.util.Locale;

import javax.faces.Message;

public class MessageTemplate 
{
	private	String	_id;
	private	int		_severity = Message.SEV_ERROR;
	private	String	_firstLevel;
	private	String	_secondLevel;
	
	private	Locale	_locale;
	
	public MessageTemplate(String id)
	{
		_id = id;
	}
	
	
	/**
	 * Gets the severity
	 * @return Returns a int
	 */
	public int getSeverity() {
		return _severity;
	}
	/**
	 * Sets the severity
	 * @param severity The severity to set
	 */
	public void setSeverity(int severity) {
		_severity = severity;
	}

	/**
	 * Gets the firstLevel
	 * @return Returns a String
	 */
	public String getFirstLevel() {
		return _firstLevel;
	}
	/**
	 * Sets the firstLevel
	 * @param firstLevel The firstLevel to set
	 */
	public void setFirstLevel(String firstLevel) {
		_firstLevel = firstLevel;
	}

	/**
	 * Gets the id
	 * @return Returns a String
	 */
	public String getId() {
		return _id;
	}
	/**
	 * Sets the id
	 * @param id The id to set
	 */
	public void setId(String id) {
		_id = id;
	}

	/**
	 * Gets the locale
	 * @return Returns a Locale
	 */
	public Locale getLocale() {
		return _locale;
	}
	/**
	 * Sets the locale
	 * @param locale The locale to set
	 */
	public void setLocale(Locale locale) {
		_locale = locale;
	}

	/**
	 * Gets the secondLevel
	 * @return Returns a String
	 */
	public String getSecondLevel() {
		return _secondLevel;
	}
	/**
	 * Sets the secondLevel
	 * @param secondLevel The secondLevel to set
	 */
	public void setSecondLevel(String secondLevel) {
		_secondLevel = secondLevel;
	}

}

