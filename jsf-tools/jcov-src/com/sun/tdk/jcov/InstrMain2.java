/**
 * @(#)InstrMain2.java	1.10 03/11/19
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.tdk.jcov;

import com.sun.tdk.jcov.tools.Options2;


/**
 * Main class of the instrumenter utility
 */
public class InstrMain2 extends InstrMain {

    public static void main(String[] args) {
        InstrMain2 instrumenter = new InstrMain2();
        Options2 opts = new Options2(VALID_OPTIONS);
        int i = 0;
        try {
            for (i = 0; i < args.length && opts.parse(args[i]); i++);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            instrumenter.usage();
            System.exit(1);
        }
        int src_total = args.length - i;
        if (src_total <= 0) {
            System.out.println("no input files specified");
            instrumenter.usage();
            System.exit(0);
        }
        String[] srcs = new String[src_total];
        System.arraycopy(args, i, srcs, 0, src_total);
        try {
            instrumenter.run(opts, srcs, instrumenter.log);
        } catch (Exception e) {
            instrumenter.log.error(e.getMessage());
            System.exit(1);
        }
        System.exit(0);
    }

}
