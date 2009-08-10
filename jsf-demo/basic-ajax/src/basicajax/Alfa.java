/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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

package basicajax;

import javax.faces.event.ActionEvent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.TreeMap;
import java.util.Collection;
import java.io.Serializable;


@ManagedBean(name = "alfa")
@SessionScoped
public class Alfa implements Serializable {

    private static final long serialVersionUID = 7869093182474067698L;

    // The list of the NATO Phonetic Alphabet
    String[] alfa = {"alfa", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel",
            "india", "juliet", "kilo", "lima", "mike", "november", "oscar", "papa", "quebec",
            "romeo", "sierra", "tango", "uniform", "victor", "whiskey", "xray", "yankee", "zulu"};

    // Map to hold values
    TreeMap<String, String> alfaMap= new TreeMap<String,String>();


    public Alfa() {
        //initialize map
        char ch = 'a';
        int i = 0;
        do {
            Character c = ch;
            alfaMap.put(c.toString(),alfa[i]);
            ch++;
            i++;
        } while (ch <= 'z');
    }

    public String translate(String alfa) {
         return alfaMap.get(alfa);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void process(ActionEvent ae) {
        // ValueExpression ve = ae.getComponent().getValueExpression("str");
        // ve.getValue();
    }

    public Collection getList() {
        return alfaMap.values();
    }

}
