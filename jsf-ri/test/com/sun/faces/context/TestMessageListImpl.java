/*
 * $Id: TestMessageListImpl.java,v 1.4 2002/06/20 01:34:24 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestMessageListImpl.java

package com.sun.faces.context;

import com.sun.faces.ServletFacesTestCase;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.context.MessageList;
import com.sun.faces.context.MessageImpl;
import java.util.Iterator;
import javax.faces.FacesException;

/**
 *
 *  <B>TestMessageListImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestMessageListImpl.java,v 1.4 2002/06/20 01:34:24 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestMessageListImpl extends ServletFacesTestCase
{
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
 
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public TestMessageListImpl() {super("TestMessageListImpl");}
    public TestMessageListImpl(String name) {super(name);}
    //
    // Class methods
    //

    //
    // Methods from TestCase
    //

    //
    // General Methods
    //
    
    public void testAccessors() {
        boolean result = false;
        
        Assert.assert_it(getFacesContext() != null );
        
        MessageList messageList = getFacesContext().getMessageList();
        Assert.assert_it(messageList != null);
        
        // PENDING (visvan) add tests to test all add methods.
        // make sure we get an exception for a messageId that doesn't exist in 
        // resource file
        try {
            messageList.add("MSG01", "userName", null);
        } catch ( FacesException fe ) {
            result = true;
        }    
        assertTrue (result);
        
        Object[] params1 = {"JavaServerFaces"};
        messageList.add("MSG0001", "userName", params1);
        
        Object[] params2 = {"userId"};
        messageList.add("MSG0003","userId", params2);
        
        Object[] params3 = {"userId", "1000", "10000"};
        messageList.add("MSG0004", "userId", params3); 
        
        Iterator it = messageList.iterator();
        
        assertTrue(it.hasNext());
        MessageImpl msg = (MessageImpl) it.next();
        assertTrue(msg != null);
        
        String summary = msg.getSummary();
        String messageId = msg.getMessageId();
        System.out.println(messageId + ":summary " + summary);
        assertTrue(summary != null);
        assertTrue(summary.equals("JavaServerFaces is not a valid number."));
        
        assertTrue(it.hasNext());
        msg = (MessageImpl) it.next();
        summary = msg.getSummary();
        messageId = msg.getMessageId();
        System.out.println(messageId + ":summary " + summary);
        assertTrue(summary.equals("userId field cannot be empty."));
        
        assertTrue(it.hasNext());
        msg = (MessageImpl)it.next();
        messageId = msg.getMessageId();
        summary = msg.getSummary();
        System.out.println(messageId + ":summary " + summary);
        assertTrue(summary.equals("userId out of range."));
        
        String detail = msg.getDetail();
        System.out.println(messageId + ":detail " + detail);
        assertTrue(detail.equals("Value should be between 1000 and 10000.")); 
        
        int severity = messageList.getMaximumSeverity();
        assertTrue(severity == 40);
        int size = messageList.size();
        assertTrue(size == 3);
        
        severity = messageList.getMaximumSeverity("userName");
        assertTrue(severity == 30);
        size = messageList.size("userId");
        assertTrue(size == 2);
        
    }

} // end of class TestMessageListImpl
