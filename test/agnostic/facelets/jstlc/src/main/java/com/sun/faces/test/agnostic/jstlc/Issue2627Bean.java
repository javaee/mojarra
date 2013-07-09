/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.sun.faces.test.agnostic.jstlc;

import java.util.Set;
import java.util.TreeSet;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "issue2627Bean")
@SessionScoped
public class Issue2627Bean {

    /**
     * Stores set 1.
     */
    private final Set<String> set1;
    /**
     * Stores set 2.
     */
    private final Set<String> set2;
    /**
     * Stores the current set.
     */
    private Set<String> currentSet;

    /**
     * Constructor.
     */
    public Issue2627Bean() {
        this.set1 = populateSet("-SET1");
        this.set2 = populateSet("-SET2");
        this.currentSet = set1;
    }

    /**
     * Get the set.
     *
     * @return the current set.
     */
    public Set<String> getSet() {
        return currentSet;
    }

    /**
     * Set the current set.
     */
    public void setSet(Set<String> set) {
        this.currentSet = set;
    }

    /**
     * Toggle action.
     *
     * @return null.
     */
    public String toggle() {
        if (currentSet == set2) {
            this.currentSet = set1;
        } else {
            this.currentSet = set2;
        }
        return null;
    }

    /**
     * Create a set.
     *
     * @param suffix the suffix.
     * @return the set.
     */
    private Set<String> populateSet(String suffix) {
        Set<String> set = new TreeSet<String>();
        for (int j = 0; j < 3; j++) {
            set.add(j + suffix);
        }
        return set;
    }
}
