/*
 * $Id: ListBean.java,v 1.1 2002/09/09 23:52:28 visvan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package basic;


/**
 * <p>JavaBean to represent a list of customer bean.</p>
 */

public class ListBean extends java.util.ArrayList{

    public ListBean() {
        System.out.println("Created ListBean");
        add(new CustomerBean("123456", "Alpha Beta Company", "ABC", 1234.56));
        add(new CustomerBean("445566", "General Services, Ltd.", "GS", 33.33));
        add(new CustomerBean("654321", "Summa Cum Laude, Inc.", "SCL", 76543.21));
        add(new CustomerBean("333333", "Yabba Dabba Doo", "YDD",  333.33));
    }
}
