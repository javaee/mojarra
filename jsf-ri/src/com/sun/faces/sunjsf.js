/*
 * $Id: sunjsf.js,v 1.7 2008/03/31 16:56:40 rlubke Exp $
 */
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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



/*
 * This function deletes any hidden parameters added
 * to the form by checking for a variable called 'adp'
 * defined on the form.  If present, this variable will
 * contain all the params added by 'apf'.
 *
 * @param f - the target form
 */
function dpf(f) {
    var adp = f.adp;
    if (adp != null) {
        for (var i = 0;i < adp.length;i++) {
            f.removeChild(adp[i]);
        }
    }
};

/*
 * This function adds any parameters specified by the
 * parameter 'pvp' to the form represented by param 'f'.
 * Any parameters added will be stored in a variable
 * called 'adp' and stored on the form.
 *
 * @param f - the target form
 * @param pvp - associative array of parameter
 *  key/value pairs to be added to the form as hidden input
 *  fields.
 */
function apf(f, pvp) {
    var adp = new Array();
    f.adp = adp;
    var i = 0;
    for (k in pvp) {
        var p = document.createElement("input");
        p.type = "hidden";
        p.name = k;
        p.value = pvp[k];
        f.appendChild(p);
        adp[i++] = p;
    }
};

/*
 * This is called by the JSF command link.  It provides
 * the form it is nested in, the parameters that need to be
 * added and finally, the target of the action.  This function
 * will delete any parameters added <em>after</em> the form
 * has been submitted to handle DOM caching issues.
 *
 * @param f - the target form
 * @param pvp - associative array of parameter
 *  key/value pairs to be added to the form as hidden input
 *  fields.
 * @param t - the target of the form submission
 */
function jsfcljs(f, pvp, t) {        
    apf(f, pvp);
    var ft = f.target;
    if (t) {
        f.target = t;
    }
    f.submit();   
    f.target = ft;
    dpf(f);    
};
