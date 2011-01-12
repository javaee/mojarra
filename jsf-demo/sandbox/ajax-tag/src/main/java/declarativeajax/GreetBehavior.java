/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package declarativeajax;


import javax.faces.context.FacesContext;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;


/**
 * <p>A trivial Behavior implementation that shows a greeting to the
 * user when invoked.</p>
 */
@FacesBehavior(value="custom.behavior.Greet")
public class GreetBehavior extends ClientBehaviorBase {

    public GreetBehavior() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScript(ClientBehaviorContext behaviorContext) {

        String name = (this.name == null) ? "World" : this.name;

        StringBuilder builder = new StringBuilder(19 + name.length());
        builder.append("alert('Hello, ");
        builder.append(name);
        builder.append("!');");

        return builder.toString();
    }

    @Override
    public Object saveState(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (initialStateMarked()) {
            Object superState = super.saveState(context);
            if (superState == null) {
                return null;
            } else {
                return new Object[] { superState };
            }
        } else {
            Object[] values = new Object[2];
      
            values[0] = super.saveState(context);
            values[1] = name;

            return values;
        }

    }


    @Override
    public void restoreState(FacesContext context, Object state) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (state == null) {
            return;
        }
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        if (values.length == 2) {
            name = (String)values[1];
        }

    }

    private String name;
}
