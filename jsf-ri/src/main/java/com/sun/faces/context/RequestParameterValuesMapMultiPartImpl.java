package com.sun.faces.context;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletRequest;

import com.sun.faces.context.BaseContextMap.EntryIterator;
import com.sun.faces.context.BaseContextMap.KeyIterator;
import com.sun.faces.context.BaseContextMap.ValueIterator;
import com.sun.faces.util.Util;


public class RequestParameterValuesMapMultiPartImpl extends RequestParameterValuesMap {

	// ------------------------------------------------------------ Private Data Members
	private RequestParameterMapMultiPartImpl requestParameterMapMultiPartImpl;

	// ------------------------------------------------------------ Constructors
	public RequestParameterValuesMapMultiPartImpl(ServletRequest request, RequestParameterMapMultiPartImpl requestParameterMapMultiPartImpl) {
		super(request);
		this.requestParameterMapMultiPartImpl = requestParameterMapMultiPartImpl;
	}
    // -------------------------------------------------------- Methods from Map


    @Override
    public String[] get(Object key) {
        Util.notNull("key", key);
		String propertySingleValue = requestParameterMapMultiPartImpl.get(key);
		String[] propertyValueArray = new String[] { propertySingleValue };
        return propertyValueArray;
    }


    @Override
    public boolean containsKey(Object key) {
        return (requestParameterMapMultiPartImpl.containsKey(key));
    }

    // --------------------------------------------- Methods from BaseContextMap


    protected Iterator<Map.Entry<String, String[]>> getEntryIterator() {
        return new EntryIterator(Collections.enumeration(requestParameterMapMultiPartImpl.keySet()));
    }


    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(Collections.enumeration(requestParameterMapMultiPartImpl.keySet()));
    }


    protected Iterator<String[]> getValueIterator() {
        return new ValueIterator(Collections.enumeration(requestParameterMapMultiPartImpl.keySet()));
    }

}
