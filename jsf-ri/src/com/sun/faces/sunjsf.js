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
 * sunjsf.js $Id: sunjsf.js,v 1.1 2006/08/29 17:37:04 rlubke Exp $ 
 *
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
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
 * @param pvp - parameter value pairs.  This is a string in the form
 *   of 'param1,value1,param2,value2' where the value of param1 is
 *   value1, and the value of param2 is value2
 */
function apf(f, pvp) {
    var adp = new Array();
    f.adp = adp;
    var ps = pvp.split(',');
    for (var i = 0,ii = 0;i < ps.length;i++,ii++) {
        var p = document.createElement("input");
                p.type = "hidden";
                p.name = ps[i];
                p.value = ps[i + 1];
                f.appendChild(p);
                adp[ii] = p;
                i += 1;
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
 * @param pvp - parameter value pairs.  This is a string in the form
 *   of 'param1,value1,param2,value2' where the value of param1 is
 *   value1, and the value of param2 is value2
 * @param t - the target of the form submission
 */
function jsfcljs(f, pvp, t) {        
    apf(f, pvp);    
    if (t) {
        f.target = t;
    }
    f.submit();   
    dpf(f);    
};
