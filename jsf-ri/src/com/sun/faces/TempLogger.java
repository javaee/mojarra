package com.sun.faces;

/**
 * This is a temporary class, until we figure out what to do about logging. 
 * It will be replaced with a real logger.
 * 
 * Having a class like this is better than nothing, at least it will be easier to
 * fixup the logging later.
 */
public class TempLogger 
{
	public static void error(Object o)
	{
		System.err.println(o);
	}
	
	public static void error(Object o, Throwable t)
	{
		System.err.println(o);
		t.printStackTrace(System.err);
	}
	
	public static void warn(Object o)
	{
		System.err.println(o);
	}
	
	public static void warn(Object o, Throwable t)
	{
		System.err.println(o);
		t.printStackTrace(System.err);
	}
}

