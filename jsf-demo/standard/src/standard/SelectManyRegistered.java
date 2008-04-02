/*
 * $Id: SelectManyRegistered.java,v 1.4 2005/08/22 22:09:43 ofung Exp $
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


import java.io.Serializable;


/**
 * <p>Test bean for valid options of a SelectMany that are not strings,
 * and for which a converter has been registered.</p>
 */

public class SelectManyRegistered implements Serializable {

    public SelectManyRegistered() {
        this("NONE");
    }

    public SelectManyRegistered(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return (this.name);
    }

    public boolean equals(Object o) {
        if (o instanceof SelectManyRegistered) {
            return (getName().equals(((SelectManyRegistered) o).getName()));
        } else {
            return (false);
        }
    }

    public String toString() {
        return (getName());
    }


}
