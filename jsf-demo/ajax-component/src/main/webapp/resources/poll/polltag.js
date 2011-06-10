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


if (!window.jsfdemo) {
    var jsfdemo = {};
}

if (!jsfdemo.polltag) {
    jsfdemo.polltag = {};
}

if (!jsfdemo.polltag.init) {
    jsfdemo.polltag.init = function init(id, inc, to) {
        var componentID = id;
        var increment = inc;
        var timeout = to;
        var elapsed = 0;
        var token = null;

        // If increment isn't set, or it's less than some reasonable value (50) default to 200ms
        if (isNaN(increment) || increment <= 50) {
            increment = 200;
        }
        // If timeout isn't set, default to no timeout
        if (isNaN(timeout) || timeout == 0) {
            timeout = -1;
        }

        var poll = function poll() {
            var hiddenID = componentID + ":" + "hidden";
            var hidden = document.getElementById(hiddenID);
            hidden.click();  // this executes the ajax request
            if (timeout != -1) {
                // Not an accurate timeout - but simple to compute
                elapsed += increment;
                if (elapsed > timeout) {
                    window.clearInterval(token);
                }
            }
        }

        token = window.setInterval(poll, increment);

        return function cancelPoll(data) {
            if (data.source.id == componentID + ":" + "hidden") {
                window.clearInterval(token);
            }
        }
    }
}