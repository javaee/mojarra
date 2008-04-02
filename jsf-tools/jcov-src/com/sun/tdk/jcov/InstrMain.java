/**
 * @(#)InstrMain.java	1.10 03/11/19
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.tdk.jcov;

import java.io.File;
import java.io.PrintStream;
import java.io.IOException;

import com.sun.tdk.jcov.tools.Options;
import com.sun.tdk.jcov.tools.OptionDescr;
import com.sun.tdk.jcov.tools.GUIRunnableTool;
import com.sun.tdk.jcov.tools.JcovAttrClassChecker;
import com.sun.tdk.jcov.tools.WildCardStringFilter;
import com.sun.tdk.jcov.tools.Messages;
import com.sun.tdk.jcov.tools.Utils;
import com.sun.tdk.jcov.tools.Log;

import com.sun.tdk.jcov.constants.InstrConstants;
import com.sun.tdk.jcov.insert.JcovInstrContext;
import com.sun.tdk.jcov.insert.UniversalInstrumenter;

/**
 * Main class of the instrumenter utility
 */
public class InstrMain implements GUIRunnableTool {
    public final static String sccsVersion = "1.10 11/19/03";
    
    /**
     * instrumenter context passed (after it's initialized) to the UniversalInstrumenter
     */
    protected JcovInstrContext instrContext;
    
    public final static String OPT_OUTPUT    = "output";
    public final static String OPT_OVERWRITE = "overwrite";
    public final static String OPT_VERBOSE   = "verbose";
    public final static String OPT_STATS     = "stats";
    public final static String OPT_INCLUDE   = "include";
    public final static String OPT_EXCLUDE   = "exclude";
    public final static String OPT_CHECK     = "check";

    public final static String USG_OUTPUT =
        "    -" + OPT_OUTPUT + "=<path>\n" +
        "        <path> specifies output file or directory, default directory is current\n";
    public final static String USG_OVERWRITE =
        "    -" + OPT_OVERWRITE + "\n" +
        "        overwrite archives/class files with their instrumented versions,\n" +
        "        without this option, instrumented files will have the same name\n" +
        "        with suffix \"" + InstrConstants.INSTR_FILE_SUFF + "\"\n" +
        "        this option is effective only if the -" + OPT_OUTPUT + " option\n" +
        "        is not specified\n";
    public final static String USG_VERBOSE =
        "    -" + OPT_VERBOSE + "\n" +
        "        enable verbose mode\n";
    public final static String USG_STATS =
        "    -" + OPT_STATS + "\n" +
        "        generate stats\n";
    public final static String USG_INCLUDE =
        "    -" + OPT_INCLUDE + "=<class name mask>\n";
    public final static String USG_EXCLUDE =
        "    -" + OPT_EXCLUDE + "=<class name mask>\n" +
        "        only instrument classes with full names satisfying the masks;\n" +
        "        wild cards (\'*\', \'?\') and UNIX-style character set specifiers\n" +
        "        (e.g. \'[a-z]\', \'[!a-z]\', \'[abAB]\' ) can be used\n";
    public final static String USG_CHECK =
        "    -" + OPT_CHECK + "\n" +
        "        do not instrument classes, just check if they are already instrumented\n" +
        "        or compiled with JCov support and report results\n";

    public final static OptionDescr DSC_OUTPUT =
        new OptionDescr(OPT_OUTPUT, "output directory", OptionDescr.VAL_SINGLE, USG_OUTPUT);
    public final static OptionDescr DSC_OVERWRITE =
        new OptionDescr(OPT_OVERWRITE, "overwrite", USG_OVERWRITE);
    public final static OptionDescr DSC_VERBOSE =
        new OptionDescr(OPT_VERBOSE, "verbose mode", USG_VERBOSE);
    public final static OptionDescr DSC_STATS =
        new OptionDescr(OPT_STATS, "print stats", USG_STATS);
    public final static OptionDescr DSC_INCLUDE =
        new OptionDescr(OPT_INCLUDE, "include", OptionDescr.VAL_MULTI, USG_INCLUDE);
    public final static OptionDescr DSC_EXCLUDE =
        new OptionDescr(OPT_EXCLUDE, "exclude", OptionDescr.VAL_MULTI, USG_EXCLUDE);
    public final static OptionDescr DSC_CHECK =
        new OptionDescr(OPT_CHECK, "check only", USG_CHECK);

    public final static OptionDescr DSC_SAVE_BEFORE       = JcovInstrContext.DSC_SAVE_BEFORE;
    public final static OptionDescr DSC_SAVE_AFTER        = JcovInstrContext.DSC_SAVE_AFTER;
    public final static OptionDescr DSC_SAVE_BEGIN        = JcovInstrContext.DSC_SAVE_BEGIN;
    public final static OptionDescr DSC_SAVE_AT_END       = JcovInstrContext.DSC_SAVE_AT_END;
    public final static OptionDescr DSC_TYPE              = JcovInstrContext.DSC_TYPE;
    public final static OptionDescr DSC_SYNCHRONIZED      = JcovInstrContext.DSC_SYNCHRONIZED;
    public final static OptionDescr DSC_AUTOCOLLECTION    = JcovInstrContext.DSC_AUTOCOLLECTION;
    public final static OptionDescr DSC_COMMON_TIME_STAMP = JcovInstrContext.DSC_COMMON_TIME_STAMP;
    public final static OptionDescr DCS_NO_SATELLITE      = JcovInstrContext.DSC_NO_SATELLITE;
    public final static OptionDescr DSC_HTTP_SAVE         = JcovInstrContext.DSC_HTTP_SAVE;

    public final static OptionDescr DSC_NO_SATELLITE      = JcovInstrContext.DSC_NO_SATELLITE;
    public final static OptionDescr DSC_SATELLITE         = JcovInstrContext.DSC_SATELLITE;
    public final static OptionDescr DSC_NOCOV             = JcovInstrContext.DSC_NOCOV;

    public final static OptionDescr[] VALID_OPTIONS = {
        DSC_OUTPUT,
        DSC_OVERWRITE,
        DSC_VERBOSE,
        DSC_STATS,
        DSC_INCLUDE,
        DSC_EXCLUDE,
        DSC_CHECK,
        DSC_SAVE_BEFORE,
        DSC_SAVE_AFTER,
        DSC_SAVE_BEGIN,
        DSC_SAVE_AT_END,
        DSC_TYPE,
        DSC_SYNCHRONIZED,
        DSC_NOCOV,
        DSC_COMMON_TIME_STAMP,
        DSC_SATELLITE,
        DSC_HTTP_SAVE,
        DSC_NO_SATELLITE,
        DSC_AUTOCOLLECTION
    };

    protected Log log = new Log(System.out);

    protected void checkJcovAttributesOnly(Options opts, String[] args, Log log) throws Exception {
        String[] opts_set = opts.getNames();
        for (int i = 0; i < opts_set.length; i++) {
            String o = opts_set[i];
            if (!(o.equals(OPT_VERBOSE) || o.equals(OPT_CHECK) ||
                  o.equals(OPT_INCLUDE) || o.equals(OPT_EXCLUDE))) {

                throw new Exception("option -" + o + " can not be used with -" + OPT_CHECK);
            } 
        }
        
        WildCardStringFilter filter = Utils.createClassNameFilter(opts.getValues(OPT_INCLUDE),
                                                                  opts.getValues(OPT_EXCLUDE));
        JcovAttrClassChecker checker = new JcovAttrClassChecker(log);
        checker.setFilter(filter);
        checker.setVerbose(opts.isSet(OPT_VERBOSE));
        checker.check(args);
    }

    public void run(Options opts, String[] args, Log log) throws Exception {
        this.log = log;
        if (args == null || args.length == 0) {
            usage();
            return;
        }
        if (opts.isSet(OPT_CHECK)) {
            checkJcovAttributesOnly(opts, args, log);
            return;
        }
        instrContext = new JcovInstrContext();
        try {
            instrContext.init(opts);
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }
        log.setVerbose(opts.isSet(OPT_VERBOSE));
        File out_dir = null;
        if (opts.isSet(OPT_OUTPUT)) {
            String dir_name = opts.getValue(OPT_OUTPUT);
            if (dir_name.endsWith(File.separator))
                dir_name = dir_name.substring(0, dir_name.length() - 1);
            out_dir = new File(dir_name);
            if (!out_dir.exists()) {
                log.fatalError("output directory (" + dir_name + ") does not exist");
                return;
            }
        }
        WildCardStringFilter filter = Utils.createClassNameFilter(opts.getValues(OPT_INCLUDE),
                                                                  opts.getValues(OPT_EXCLUDE));
        UniversalInstrumenter instrumenter = 
            new UniversalInstrumenter(out_dir, filter, instrContext, opts.isSet(OPT_OVERWRITE));

        instrumenter.setLog(log);
        instrumenter.setPrintStats(opts.isSet(OPT_STATS));

        for (int i = 0; args != null && i < args.length; instrumenter.instrument(args[i++]));

        if ((opts.isSet(JcovInstrContext.OPT_SAVE_BEFORE) ||
             opts.isSet(JcovInstrContext.OPT_SAVE_AFTER)  ||
             opts.isSet(JcovInstrContext.OPT_SAVE_BEGIN)  ||
             opts.isSet(JcovInstrContext.OPT_SAVE_AT_END)) &&
            instrumenter.getSavePointCount() < 1) {

            log.warning("no coverage data savepoints have been inserted");
        }
    }
    
    /**
     * entry point. Parses options, constructs apropriate context,
     * class name fileter, then invokes UniversalInstrumenter to do the job
     */
    public static void main(String[] args) {
        InstrMain instrumenter = new InstrMain();
        Log log = instrumenter.log;
        Options opts = new Options(VALID_OPTIONS);
        int i = 0;
        try {
            for (i = 0; i < args.length && opts.parse(args[i]); i++);
        } catch (Exception e) {
            log.error(e.getMessage());
            instrumenter.usage();
            System.exit(1);
        }
        int src_total = args.length - i;
        if (src_total <= 0) {
            log.error("no input files specified");
            instrumenter.usage();
            System.exit(0);
        }
        String[] srcs = new String[src_total];
        System.arraycopy(args, i, srcs, 0, src_total);
        try {
            instrumenter.run(opts, srcs, log);
        } catch (Exception e) {
            log.error(e.getMessage());
            System.exit(1);
        }
        System.exit(0);
    }
    
    /**
     * prints out usage information
     */
    public void usage(PrintStream out) {
        out.println("Usage:");
        out.print("  > java com.sun.tdk.jcov.InstrMain [options]");
        out.println(" { <dir> | <class> | <zip archive> | <jar archive> }");
        out.println("options:");
        for (int i = 0; i < VALID_OPTIONS.length; i++) {
            OptionDescr od = VALID_OPTIONS[i];

            if (od.equals(DSC_AUTOCOLLECTION) || od.equals(DSC_NO_SATELLITE)) {
                continue;
            }
            out.print(od.usage);
        }
    }

    public void usage() {
        usage(log.getOut());
    }
}
