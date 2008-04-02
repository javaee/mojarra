/*
 * $Id: ListBean.java,v 1.5 2005/12/14 22:27:46 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package standard;

import java.util.ArrayList;

/**
 * <p>JavaBean to represent a list of customer bean.</p>
 *
 * @version $Id: ListBean.java,v 1.5 2005/12/14 22:27:46 rlubke Exp $
 */

public class ListBean extends ArrayList {

    public ListBean() {
        System.out.println("Created ListBean");
        add(new CustomerBean("123456", "Alpha Beta Company", "ABC", 1234.56));
        add(new CustomerBean("445566", "General Services, Ltd.", "GS", 33.33));
        add(
              new CustomerBean("654321", "Summa Cum Laude, Inc.", "SCL",
                               76543.21));
        add(new CustomerBean("333333", "Yabba Dabba Doo", "YDD", 333.33));
    }
}
